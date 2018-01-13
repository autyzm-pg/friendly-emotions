package pg.autyzm.graprzyjazneemocje;

import java.util.ArrayList;
import java.util.List;

import pg.autyzm.przyjazneemocje.lib.entities.Level;


/**
 * Created by user on 17.11.2017.
 */

public class LevelInGame {

    private Level levelFromDatabase;


    private int currentSublevelNumber = 0;
    private int wrongAnswersSublevel = 0;
    private int rightAnswersSublevel = 0;
    private int timeoutSubLevel = 0;
    private boolean levelPassed = false;


    // for example [1, 2, 2, 1] - sublevel when you have to guess emotion with id = 1, sublevel [...] id = 2 etc.
    private List<Integer> sublevelsList;


    public LevelInGame(Level l){
        setLevelFromDatabase(l);
    }

    void prepareLevel(){

        resetLevelCounters();

        // tworzymy tablice do permutowania

        setSublevelsList(new ArrayList<Integer>());

        for(int i = 0; i < getLevelFromDatabase().getEmotions().size(); i++){

            for(int j = 0; j < getLevelFromDatabase().getSublevelsPerEachEmotion(); j++){

                getSublevelsList().add(getLevelFromDatabase().getEmotions().get(i));

            }

        }

        java.util.Collections.shuffle(getSublevelsList());

    }

    public void resetLevelCounters(){
        currentSublevelNumber = -1;
        setWrongAnswersSublevel(0);
        setRightAnswersSublevel(0);
        setTimeoutSubLevel(0);
        levelPassed = false;
    }




    public Level getLevelFromDatabase() {
        return levelFromDatabase;
    }

    public void setLevelFromDatabase(Level levelFromDatabase) {
        this.levelFromDatabase = levelFromDatabase;
    }

    public int getCurrentSublevelNumber() {
        return currentSublevelNumber;
    }


    public int getWrongAnswersSublevel() {
        return wrongAnswersSublevel;
    }

    public void setWrongAnswersSublevel(int wrongAnswersSublevel) {
        this.wrongAnswersSublevel = wrongAnswersSublevel;
    }

    public int getRightAnswersSublevel() {
        return rightAnswersSublevel;
    }

    public void setRightAnswersSublevel(int rightAnswersSublevel) {
        this.rightAnswersSublevel = rightAnswersSublevel;
    }

    public int getTimeoutSubLevel() {
        return timeoutSubLevel;
    }

    public void setTimeoutSubLevel(int timeoutSubLevel) {
        this.timeoutSubLevel = timeoutSubLevel;
    }

    public boolean isLevelPassed() {
        return levelPassed;
    }

    public void setLevelPassed(boolean levelPassed) {
        this.levelPassed = levelPassed;
    }

    public List<Integer> getSublevelsList() {
        return sublevelsList;
    }

    public void setSublevelsList(List<Integer> sublevelsList) {
        this.sublevelsList = sublevelsList;
    }

    public void nextSublevel(){

        currentSublevelNumber++;
        if(currentSublevelNumber == levelFromDatabase.getAllSublevelsInLevelAmount()){
            levelPassed = true;
        }

    }

    public void incrementRightAnswersSublevel(){
        rightAnswersSublevel++;
    }

    public void incrementWrongAnswersSublevel(){
        wrongAnswersSublevel++;
    }

    public void incrementTimeoutSublevel(){
        timeoutSubLevel++;
    }

}
