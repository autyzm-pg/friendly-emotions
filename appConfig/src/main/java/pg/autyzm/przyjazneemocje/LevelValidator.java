package pg.autyzm.przyjazneemocje;

import pg.autyzm.przyjazneemocje.lib.entities.Level;

/**
 * Created by user on 26.08.2017.
 */

public class LevelValidator {

    Level validatedLevel;

    public LevelValidator(Level l){
        validatedLevel = l;
    }




    boolean validateLevel(){

        // sprawdzenie dlugosci nazwy poziomu
        if(validatedLevel.getName().length() == 0 || validatedLevel.getName().length() > 50) {
            return false;
        }
        // prosta walidacja, czy w ogle zaznaczono jakies emocje (minimum dwa)/zdjecia
        if(validatedLevel.getEmotions().size() < 2 || validatedLevel.getPhotosOrVideosIdList().size() == 0){
            return false;
        }
        if(everyEmotionHasAtLestOnePhoto()){
            return true;
        }
        else {
            return true;
        }
    }

    public boolean everyEmotionHasAtLestOnePhoto(){

        for(int emotion : validatedLevel.getEmotions()){

        }

        return true;

    }
}
