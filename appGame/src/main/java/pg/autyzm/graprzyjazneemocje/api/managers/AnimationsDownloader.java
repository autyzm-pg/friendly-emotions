package pg.autyzm.graprzyjazneemocje.api.managers;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AnimationsDownloader extends AsyncTask<String, Void, Void> {

    private File storageMainDirectory;
    private DbxClientV2 mDbxClient;

    private final String ACCESS_TOKEN = "bgHKRr_8uBAAAAAAAAAAB8hFqM9vKzHITfQo54V0G19VhQ0zAOvNs9Eo29XSE0Rf";
    private final String storageAppMainDirectoryName = "happyApplicationsAnimations";
    private final String picturesDirectoryName = "/pictures/";

    public AnimationsDownloader(){
        DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("FriendlyApplications").build();
        mDbxClient = new DbxClientV2(requestConfig, ACCESS_TOKEN);

        prepareAppDirectoryInExternalStorage();

    }


    protected Void doInBackground(String... urls) {

        downloadPicturesFromDropbox(picturesDirectoryName);
        return null;

    }

    private void downloadPicturesFromDropbox(String folderName)
    {

        createDirectoryInExternalStorageIfNecessary(folderName);


        try
        {
            // Get files and folder metadata from Dropbox root directory
            ListFolderResult result = mDbxClient.files().listFolder(folderName);
            while (true) {
                for (Metadata metadata : result.getEntries()) {

                    if(isItDirectory(metadata)){
                        downloadPicturesFromDropbox(metadata.getPathLower());
                    }
                    else{
                        System.out.println(metadata.getPathLower());
                        downloadFile(metadata);
                    }
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = mDbxClient.files().listFolderContinue(result.getCursor());
            }
        }
        catch (DbxException dbxe)
        {
            dbxe.printStackTrace();
        }
    }

    private boolean isItDirectory(Metadata metadata){
        if(! metadata.getName().contains("."))
            return true;
        else
            return false;
    }

    private void downloadFile(Metadata metadata){


        File downloadedFile = new File(storageMainDirectory, metadata.getPathLower());

        // Download the file.
        try  {

            OutputStream outputStream = new FileOutputStream(downloadedFile);
            mDbxClient.files().downloadBuilder(metadata.getPathLower()).download(outputStream);

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

        File newDirectory = new File(storageMainDirectory, directoryName);

        if (!newDirectory.exists()) {
            newDirectory.mkdir();
            Log.i("Files", directoryName + " directory was created");
        }
    }

    private void prepareAppDirectoryInExternalStorage(){

        storageMainDirectory = new File(Environment.getExternalStorageDirectory(), storageAppMainDirectoryName);

        if (!storageMainDirectory.exists()) {
            storageMainDirectory.mkdir();
            Log.i("Files", storageAppMainDirectoryName + " directory was created");
        }
    }
}
