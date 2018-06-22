package pg.autyzm.graprzyjazneemocje.api.managers;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pg.autyzm.graprzyjazneemocje.api.entities.Picture;
import pg.autyzm.graprzyjazneemocje.api.entities.PicturesContainer;


public class StorageAnimationsManager {

    private String storageAppMainDirectoryName = "happyApplicationsAnimations";
    private String picturesDirectoryName = "pictures";

    private File friendlyAppsDirectoryInExternalStorage;
    private List<PicturesContainer> picturesFromExternalStorage;

    // singleton

    private static StorageAnimationsManager instance = null;

    protected StorageAnimationsManager() {

        openAnimationsDirectoryInExternalStorage();
        setPicturesFromExternalStorage(getAllAnimationsFromStorage());

    }

    public static StorageAnimationsManager getInstance() {
        if(instance == null) {
            instance = new StorageAnimationsManager();
        }
        return instance;
    }

    public List<PicturesContainer> getAllAnimationsFromStorage() {

        List<PicturesContainer> externalStorageAssets = new ArrayList<>();
        File picturesDirectory = new File(friendlyAppsDirectoryInExternalStorage, picturesDirectoryName);
        File[] directoriesWithPictures = picturesDirectory.listFiles();

        if(directoriesWithPictures != null) {

            for (File directoryWithPictures : directoriesWithPictures) {
                if (!directoryWithPictures.getName().contains(".")) {

                    if (directoryWithPictures.getName().equals("suns")) {
                        continue;
                    }

                    PicturesContainer picturesContainer = new PicturesContainer((directoryWithPictures.getName()));
                    File[] pictureFilesInDirectory = directoryWithPictures.listFiles();

                    for (File pictureFile : pictureFilesInDirectory) {
                        picturesContainer.addPicture(new Picture(pictureFile.getName(), pictureFile.getAbsolutePath(), picturesContainer));
                    }

                    externalStorageAssets.add(picturesContainer);
                }
            }

        }

        return externalStorageAssets;
    }


    public List<PicturesContainer> giveAllAnimationsFromStorageWithCategoriesProvided(String[] categories) {

        List<PicturesContainer> picturesContainerList = getAllAnimationsFromStorage();
        Iterator<PicturesContainer> picturesContainerIterator = picturesContainerList.iterator();

        label:
        while (picturesContainerIterator.hasNext()) {

            PicturesContainer picturesContainer = picturesContainerIterator.next();

            for (String category : categories) {
                if (picturesContainer.getCategoryName().equals(category)) {
                    continue label;
                }
            }

            picturesContainerIterator.remove();
        }

        return picturesContainerList;
    }


    private void openAnimationsDirectoryInExternalStorage(){


        friendlyAppsDirectoryInExternalStorage = new File(Environment.getExternalStorageDirectory(), storageAppMainDirectoryName);
        if (!friendlyAppsDirectoryInExternalStorage.exists()) {
            Log.i("Files", storageAppMainDirectoryName + " does not exists. You need to run AnimationLoader first!");
        }

        Log.i("Files", storageAppMainDirectoryName + " directory was opened");

    }

    public void setPicturesFromExternalStorage(List<PicturesContainer> picturesFromExternalStorage) {
        this.picturesFromExternalStorage = picturesFromExternalStorage;
    }

    public void deletePictureFromStorage(Picture picture){

        try{

            File file = new File(picture.getPath());

            if(file.delete()){
                Log.i("Animations", file.getName() + " was deleted");
                System.out.println(file.getName() + " is deleted!");
            }else{
                Log.i("Animations", file.getName() + " was not deleted");
            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    public void deletePicturesContainerFromStorage(PicturesContainer picturesContainer) {

        // delete pictures in the folder
        for(Picture picture : picturesContainer.getPicturesInCategory()){
            deletePictureFromStorage(picture);
        }


        // delete empty folder
        try{

            File file = new File(friendlyAppsDirectoryInExternalStorage,
                    picturesDirectoryName + "/" + picturesContainer.getCategoryName());

            if(file.delete()){
                Log.i("Animations", file.getName() + " was deleted");
                System.out.println(file.getName() + " is deleted!");
            }else{
                Log.i("Animations", file.getName() + " was not deleted");
            }

        }catch(Exception e){

            e.printStackTrace();

        }
    }




}
