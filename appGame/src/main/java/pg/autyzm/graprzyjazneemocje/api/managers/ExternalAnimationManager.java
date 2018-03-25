package pg.autyzm.graprzyjazneemocje.api.managers;


import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pg.autyzm.graprzyjazneemocje.api.entities.Picture;
import pg.autyzm.graprzyjazneemocje.api.entities.PictureMovementType;
import pg.autyzm.graprzyjazneemocje.api.entities.PicturesContainer;
import pg.autyzm.graprzyjazneemocje.api.exceptions.EmptyInternalStorageException;


public class ExternalAnimationManager implements AnimationManagement {


    private File friendlyAppsDirectoryInExternalStorage;
    private List<PicturesContainer> picturesFromExternalStorage;


    // singleton

    private static ExternalAnimationManager instance = null;

    protected ExternalAnimationManager() {

        openAnimationsDirectoryInExternalStorage();
        setPicturesFromExternalStorage(getAllAnimationsFromStorage());

    }

    public static ExternalAnimationManager getInstance() {
        if(instance == null) {
            instance = new ExternalAnimationManager();
        }
        return instance;
    }

    //




    @Override
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
                        picturesContainer.addPicture(new Picture(pictureFile.getName(), pictureFile.getAbsolutePath()));
                    }

                    externalStorageAssets.add(picturesContainer);

                }
            }

        }


        return externalStorageAssets;
    }

    @Override
    public boolean deleteAnimationFromStorage(Picture picture) {

        boolean result = false;
        File file = new File(friendlyAppsDirectoryInExternalStorage, picture.getName());
        if (file.exists()){
            result = file.delete();
        }

        return result;

    }

    @Override
    public void deleteAllAnimationsFromStorage() {

    }

    @Override
    public List<PictureMovementType> giveAllPictureMovementTypesFromStorage(){
        return null;
    }

    @Override
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

    public void prepareInternalAnimations() throws EmptyInternalStorageException {

        /*
            1. Check internal/ directory. If it doesn't exists or it's empty, do next step.
            2. Take first 3 animations from external storage and save them to internal.
        */

        throw new EmptyInternalStorageException("Internal storage is empty and it was impossible to copy animations from External storage to it.");
    }




    public void setPicturesFromExternalStorage(List<PicturesContainer> picturesFromExternalStorage) {
        this.picturesFromExternalStorage = picturesFromExternalStorage;
    }
}
