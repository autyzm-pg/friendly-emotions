package pg.autyzm.graprzyjazneemocje.api.managers;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AnimationsDownloader extends AsyncTask<String, Void, Void> {

    private DbxClientV2 mDbxClient;

    private final String ACCESS_TOKEN = "bgHKRr_8uBAAAAAAAAAAB8hFqM9vKzHITfQo54V0G19VhQ0zAOvNs9Eo29XSE0Rf";
    private final String AnimationsModuleDirectoryName = "/animations/";

    public AnimationsDownloader(){
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("FriendlyApplications").build();
        mDbxClient = new DbxClientV2(requestConfig, ACCESS_TOKEN);

    }


    protected Void doInBackground(String... urls) {

        downloadDirectoryMetadataFromDropbox(AnimationsModuleDirectoryName);
        return null;

    }

    private void downloadDirectoryMetadataFromDropbox(String folderName)
    {

        createDirectoryInExternalStorageIfNecessary(folderName);


        try
        {
            // Get files and folder metadata from Dropbox root directory
            ListFolderResult listFolderResult = mDbxClient.files().listFolder(folderName);
            while (true) {
                for (Metadata metadata : listFolderResult.getEntries()) {

                    if(isItDirectory(metadata)){
                        downloadDirectoryMetadataFromDropbox(metadata.getPathLower());
                    }
                    else if(isItZipFile(metadata)){
                        downloadFile(metadata);
                        try {

                            String archiveDirectory =  metadata.getPathLower().
                                    substring(0, metadata.getPathLower().lastIndexOf(File.separator));

                            unzipFile(new File(Environment.getExternalStorageDirectory().getPath() + metadata.getPathLower()),
                                    new File(Environment.getExternalStorageDirectory().getPath() + archiveDirectory));
                            new File(Environment.getExternalStorageDirectory().getPath() + metadata.getPathLower()).delete();
                            Log.i("Animations", "Archive file unzipped");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        downloadFile(metadata);
                    }
                }

                if (!listFolderResult.getHasMore()) {
                    break;
                }

                listFolderResult = mDbxClient.files().listFolderContinue(listFolderResult.getCursor());
            }
        }
        catch (DbxException dbxe)
        {
            dbxe.printStackTrace();
        }
    }

    private boolean isItZipFile(Metadata metadata){
        if(metadata.getName().contains(".zip"))
            return true;
        else
            return false;
    }

    private boolean isItDirectory(Metadata metadata){
        if(! metadata.getName().contains("."))
            return true;
        else
            return false;
    }

    private void downloadFile(Metadata metadata){


        File downloadedFile = new File(Environment.getExternalStorageDirectory(), metadata.getPathLower());

        // Download the file.
        try  {

            OutputStream outputStream = new FileOutputStream(downloadedFile);
            mDbxClient.files().downloadBuilder(metadata.getPathLower()).download(outputStream);
            System.out.println("Downloaded: " + metadata.getPathLower());

        }        //exception handled
        catch (DbxException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private void createDirectoryInExternalStorageIfNecessary(String directoryName){

        File newDirectory = new File(Environment.getExternalStorageDirectory(), directoryName);

        if (!newDirectory.exists()) {
            newDirectory.mkdir();
            Log.i("Files", directoryName + " directory was created");
        }
    }

    private void unzipFile(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }


}
