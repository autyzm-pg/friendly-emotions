package pg.autyzm.przyjazneemocje.lib.entities;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ann on 25.10.2016.
 */
public class Level {

    private int id;

    private String photosOrVideosFlag;
    private int timeLimit;
    private int photosOrVideosShowedForOneQuestion;
    private boolean isLevelActive;
    private int sublevelsPerEachEmotion;
    private int amountOfAllowedTriesForEachEmotion;
    private boolean isForTests;
    private String name;

    private int hintTypesAsNumber = 0;

    private List<Integer> photosOrVideosIdList;
    private List<Integer> emotions = new ArrayList<>();
    private String praises = "";

    private int secondsToHint;
    private boolean shouldQuestionBeReadAloud;

    private Question questionType;
    private List<Hint> hintTypes = new ArrayList<>();

    public Question getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Question questionType) {
        this.questionType = questionType;
    }

    public List<Hint> getHintTypes() {
        return hintTypes;
    }

    public void setHintTypes(List<Hint> hintTypes) {
        this.hintTypes = hintTypes;
    }

    public String getPraises() {
        return praises;
    }

    public void setPraises(String praises) {
        this.praises = praises;
    }

    public void addPraise(String newPraise) {
        this.praises += ";" + newPraise;
    }

    public int getHintTypesAsNumber() {
        return hintTypesAsNumber;
    }

    public void setHintTypesAsNumber(int hintTypesAsNumber) {
        this.hintTypesAsNumber = hintTypesAsNumber;
    }

    public enum Question {
        EMOTION_NAME, SHOW_WHERE_IS_EMOTION_NAME, SHOW_EMOTION_NAME
    }

    public enum Hint {
        FRAME_CORRECT, ENLARGE_CORRECT, MOVE_CORRECT, GREY_OUT_INCORRECT
    }

    public void addHintType(Hint hint){
        hintTypes.add(hint);
    }

    public Level(Cursor cur, Cursor cur2, Cursor cur3){

        setPhotosOrVideosIdList(new ArrayList<Integer>());
        setEmotions(new ArrayList<Integer>());

        while(cur.moveToNext())
        {
            setId(cur.getInt(cur.getColumnIndex("id")));
            setPhotosOrVideosFlag(cur.getString(cur.getColumnIndex("photos_or_videos")));
            setTimeLimit(cur.getInt(cur.getColumnIndex("time_limit")));
            setPhotosOrVideosShowedForOneQuestion(cur.getInt(cur.getColumnIndex("photos_or_videos_per_level")));
            int active = cur.getInt(cur.getColumnIndex("is_level_active"));
            setPraises(cur.getString(cur.getColumnIndex("praises")));

            setAmountOfAllowedTriesForEachEmotion(cur.getInt(cur.getColumnIndex("correctness")));
            setSublevelsPerEachEmotion(cur.getInt(cur.getColumnIndex("sublevels_per_each_emotion")));

            setLevelActive((active != 0));
            setName(cur.getString(cur.getColumnIndex("name")));
            setQuestionType(Question.valueOf(cur.getString(cur.getColumnIndex("question_type"))));
            setHintTypesAsNumber(cur.getInt(cur.getColumnIndex("hint_types_as_number")));
        }

        if(cur2 != null){

            while(cur2.moveToNext()){
                getPhotosOrVideosIdList().add(cur2.getInt(cur2.getColumnIndex("photoid")));

            }
        }

        if(cur3 != null){


            while(cur3.moveToNext()){

                getEmotions().add(cur3.getInt(cur3.getColumnIndex("emotionid")) - 1);

            }
        }

    }


    public Level(){

        setPhotosOrVideosIdList(new ArrayList<Integer>());
        setEmotions(new ArrayList<Integer>());

        setLevelActive(true);
        setId(0);

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotosOrVideosFlag() {
        return photosOrVideosFlag;
    }

    public void setPhotosOrVideosFlag(String photosOrVideosFlag) {
        this.photosOrVideosFlag = photosOrVideosFlag;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getPhotosOrVideosShowedForOneQuestion() {
        return photosOrVideosShowedForOneQuestion;
    }

    public void setPhotosOrVideosShowedForOneQuestion(int photosOrVideosShowedForOneQuestion) {
        this.photosOrVideosShowedForOneQuestion = photosOrVideosShowedForOneQuestion;
    }

    public boolean isLevelActive() {
        return isLevelActive;
    }

    public void setLevelActive(boolean levelActive) {
        isLevelActive = levelActive;
    }

    public int getSublevelsPerEachEmotion() {
        return sublevelsPerEachEmotion;
    }

    public void setSublevelsPerEachEmotion(int sublevelsPerEachEmotion) {
        this.sublevelsPerEachEmotion = sublevelsPerEachEmotion;
    }

    public int getAmountOfAllowedTriesForEachEmotion() {
        return amountOfAllowedTriesForEachEmotion;
    }

    public void setAmountOfAllowedTriesForEachEmotion(int amountOfAllowedTriesForEachEmotion) {
        this.amountOfAllowedTriesForEachEmotion = amountOfAllowedTriesForEachEmotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPhotosOrVideosIdList() {
        return photosOrVideosIdList;
    }

    public void setPhotosOrVideosIdList(List<Integer> photosOrVideosIdList) {
        this.photosOrVideosIdList = photosOrVideosIdList;
    }

    public List<Integer> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<Integer> emotions) {
        this.emotions = emotions;
    }

    public void addEmotion(int newEmotionId) {

        // make sure the emotion is new

        boolean isNew = true;

        for(Integer emotion : emotions){
            if(emotion.equals(newEmotionId)) {
                isNew = false;
                break;
            }
        }

        if(isNew)
            this.emotions.add(newEmotionId);
    }

    public void deleteEmotion(int i) {
        this.emotions.remove(emotions.get(i));
    }

    public Map getInfo(){
        Map out = new HashMap();
        for(Field field : this.getClass().getDeclaredFields()){
            try {
                out.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    public int getSecondsToHint() {
        return secondsToHint;
    }

    public void setSecondsToHint(int secondsToHint) {
        this.secondsToHint = secondsToHint;
    }

    public boolean isShouldQuestionBeReadAloud() {
        return shouldQuestionBeReadAloud;
    }

    public void setShouldQuestionBeReadAloud(boolean shouldQuestionBeReadAloud) {
        this.shouldQuestionBeReadAloud = shouldQuestionBeReadAloud;
    }



    public void addPhoto(Integer photoId){
        photosOrVideosIdList.add(photoId);
    }


    public int getAllSublevelsInLevelAmount(){
        return emotions.size() * sublevelsPerEachEmotion;
    }

    public boolean isForTests() {
        return isForTests;
    }

    public void setForTests(boolean forTests) {
        isForTests = forTests;
    }

    public void addHintTypeAsNumber(int newType){
        setHintTypesAsNumber(getHintTypesAsNumber() + newType);
    }

    public void removeHintTypeAsNumber(int newType){
        setHintTypesAsNumber(getHintTypesAsNumber() - newType);
    }

    public void incrementEmotionIdsForGame(){

        List<Integer> newEmotions = new ArrayList<>();

        for(Integer emotionId : emotions){
            newEmotions.add(emotionId + 1);
        }

        emotions = newEmotions;

    }



}
