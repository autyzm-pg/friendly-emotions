package pg.autyzm.graprzyjazneemocje.api.managers;



import java.util.List;

import pg.autyzm.graprzyjazneemocje.api.entities.Picture;
import pg.autyzm.graprzyjazneemocje.api.entities.PictureMovementType;
import pg.autyzm.graprzyjazneemocje.api.entities.PicturesContainer;
import pg.autyzm.graprzyjazneemocje.api.exceptions.EmptyInternalStorageException;


public interface AnimationManagement {

    String storageAppMainDirectoryName = "happyApplicationsAnimations";
    String picturesDirectoryName = "pictures";
    String backgroundDirectoryName = "background";
    String animationMovementsDirectoryName = "animationMovements";

    List<PicturesContainer> getAllAnimationsFromStorage();
    boolean deleteAnimationFromStorage(Picture picture);
    void deleteAllAnimationsFromStorage();
    void prepareInternalAnimations() throws EmptyInternalStorageException;

    // not sure if necessary - maybe those files should be copied manually
    List<PictureMovementType> giveAllPictureMovementTypesFromStorage();
    List<PicturesContainer> giveAllAnimationsFromStorageWithCategoriesProvided(String[] categories);

    /*
    może gdzieś zrób: utwórz obraz na podstawie animacji (to ze Stacka, zeby sie ludzie nie meczyli)
     */

}
