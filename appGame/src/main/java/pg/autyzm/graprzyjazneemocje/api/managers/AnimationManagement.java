package pg.autyzm.graprzyjazneemocje.api.managers;


import com.example.pbirg.animationsmanagement.api.entities.Picture;
import com.example.pbirg.animationsmanagement.api.entities.PictureMovementType;
import com.example.pbirg.animationsmanagement.api.exceptions.EmptyInternalStorageException;

import java.util.List;


public interface AnimationManagement {

    String storageAppMainDirectoryName = "happyApplicationsAnimations";
    String picturesDirectoryName = "pictures";
    String backgroundDirectoryName = "background";
    String animationMovementsDirectoryName = "animationMovements";

    List<Picture> getAllAnimationsFromStorage();
    boolean deleteAnimationFromStorage(Picture picture);
    void deleteAllAnimationsFromStorage();
    void prepareInternalAnimations() throws EmptyInternalStorageException;

    // not sure if necessary - maybe those files should be copied manually
    List<PictureMovementType> giveAllPictureMovementTypesFromStorage();
    List<Picture> giveAllAnimationsFromStorageWithNameLike(String namePattern);

    /*
    może gdzieś zrób: utwórz obraz na podstawie animacji (to ze Stacka, zeby sie ludzie nie meczyli)
     */

}
