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

    enum ERROR_TYPE{
        EMPTY_LEVEL_NAME, TOO_LONG_LEVEL_NAME, NOT_ENOUGH_EMOTIONS_SELECTED,
        NOT_ENOUGH_PHOTOS_PER_EMOTION_SELECTED, NO_PRIZES_SELECTED, OK
    }




    ERROR_TYPE validateLevel(){

        // sprawdzenie dlugosci nazwy poziomu
        if(validatedLevel.getName().length() == 0) {
            return ERROR_TYPE.EMPTY_LEVEL_NAME;
        }
        if(validatedLevel.getName().length() > 50) {
            return ERROR_TYPE.TOO_LONG_LEVEL_NAME;
        }
        // prosta walidacja, czy w ogle zaznaczono jakies emocje (minimum dwa)/zdjecia
        if(validatedLevel.getEmotions().size() < 2){
            return ERROR_TYPE.NOT_ENOUGH_EMOTIONS_SELECTED;
        }
        if(validatedLevel.getPhotosOrVideosIdList().size() == 0){
            return ERROR_TYPE.NOT_ENOUGH_PHOTOS_PER_EMOTION_SELECTED;
        }
        if(! everyEmotionHasEnoughPhotos()){
            return ERROR_TYPE.OK;
        }
        if(validatedLevel.getPrizes().equals("")){
            return ERROR_TYPE.NO_PRIZES_SELECTED;
        }
        else {
            return ERROR_TYPE.OK;
        }
    }

    public boolean everyEmotionHasEnoughPhotos(){

        for(int emotion : validatedLevel.getEmotions()){

        }

        return true;
    }
}
