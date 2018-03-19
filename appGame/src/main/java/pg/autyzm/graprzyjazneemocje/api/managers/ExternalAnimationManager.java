package pg.autyzm.graprzyjazneemocje.api.managers;


import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pg.autyzm.graprzyjazneemocje.api.entities.Picture;
import pg.autyzm.graprzyjazneemocje.api.entities.PictureMovementType;
import pg.autyzm.graprzyjazneemocje.api.exceptions.EmptyInternalStorageException;


public class ExternalAnimationManager implements AnimationManagement {

    private File friendlyAppsDirectoryInExternalStorage;
    private List<Picture> picturesFromExternalStorage;


    public ExternalAnimationManager(){

        openAnimationsDirectoryInExternalStorage();

    }



    @Override
    public List<Picture> getAllAnimationsFromStorage() {

        if(picturesFromExternalStorage == null) {

            File directoryWithAnimations = new File(friendlyAppsDirectoryInExternalStorage, picturesDirectoryName);

            picturesFromExternalStorage = new ArrayList<>();
            File[] files = directoryWithAnimations.listFiles();

            // could use stream here one day
            for (File file : files) {
                if (!file.getName().endsWith(".png")) {
                    continue;
                }
                picturesFromExternalStorage.add(new Picture(file.getName()));
            }

        }

        return picturesFromExternalStorage;
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
    public List<Picture> giveAllAnimationsFromStorageWithNameLike(String namePattern) {

        List<Picture> picturesWithNamePattern = new ArrayList<>();

        for(Picture p : picturesFromExternalStorage){
            if(p.getName().contains(namePattern)){
                picturesWithNamePattern.add(p);
            }
        }

        return picturesWithNamePattern;
    }


    private void openAnimationsDirectoryInExternalStorage(){

        String animationsFolder = "happyApplicationsAnimations";

        friendlyAppsDirectoryInExternalStorage = new File(Environment.getExternalStorageDirectory(), animationsFolder);
        if (!friendlyAppsDirectoryInExternalStorage.exists()) {
            Log.i("Files", animationsFolder + " does not exists. You need to run AnimationLoader first!");
        }

        Log.i("Files", animationsFolder + " directory was opened");

    }

    public void prepareInternalAnimations() throws EmptyInternalStorageException {

        /*
            1. Check internal/ directory. If it doesn't exists or it's empty, do next step.
            2. Take first 3 animations from external storage and save them to internal.
        */

        throw new EmptyInternalStorageException("Internal storage is empty and it was impossible to copy animations from External storage to it.");
    }



}
