package pg.autyzm.przyjazneemocje.lib;

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

    private String photosOrVideos;
    private int timeLimit;
    private int pvPerLevel;
    private boolean isLevelActive;
    private int sublevels;
    private int correctness;

    private String name;

    private List<Integer> photosOrVideosList;
    private List<Integer> emotions;




    public Level(Cursor cur, Cursor cur2, Cursor cur3){

        setPhotosOrVideosList(new ArrayList<Integer>());
        setEmotions(new ArrayList<Integer>());

        while(cur.moveToNext())
        {
            setId(cur.getInt(cur.getColumnIndex("id")));
            setPhotosOrVideos(cur.getString(cur.getColumnIndex("photos_or_videos")));
            setTimeLimit(cur.getInt(cur.getColumnIndex("time_limit")));
            setPvPerLevel(cur.getInt(cur.getColumnIndex("photos_or_videos_per_level")));
            int active = cur.getInt(cur.getColumnIndex("is_level_active"));

            setCorrectness(cur.getInt(cur.getColumnIndex("correctness")));
            setSublevels(cur.getInt(cur.getColumnIndex("sublevels")));

            setLevelActive((active != 0));
            setName(cur.getString(cur.getColumnIndex("name")));
        }

        if(cur2 != null){

            while(cur2.moveToNext()){
                getPhotosOrVideosList().add(cur2.getInt(cur2.getColumnIndex("photoid")));

            }
        }

        if(cur3 != null){


            while(cur3.moveToNext()){

                getEmotions().add(cur3.getInt(cur3.getColumnIndex("emotionid")));

            }
        }

    }


    public Level(){

        setPhotosOrVideosList(new ArrayList<Integer>());
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

    public String getPhotosOrVideos() {
        return photosOrVideos;
    }

    public void setPhotosOrVideos(String photosOrVideos) {
        this.photosOrVideos = photosOrVideos;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getPvPerLevel() {
        return pvPerLevel;
    }

    public void setPvPerLevel(int pvPerLevel) {
        this.pvPerLevel = pvPerLevel;
    }

    public boolean isLevelActive() {
        return isLevelActive;
    }

    public void setLevelActive(boolean levelActive) {
        isLevelActive = levelActive;
    }

    public int getSublevels() {
        return sublevels;
    }

    public void setSublevels(int sublevels) {
        this.sublevels = sublevels;
    }

    public int getCorrectness() {
        return correctness;
    }

    public void setCorrectness(int correctness) {
        this.correctness = correctness;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPhotosOrVideosList() {
        return photosOrVideosList;
    }

    public void setPhotosOrVideosList(List<Integer> photosOrVideosList) {
        this.photosOrVideosList = photosOrVideosList;
    }

    public List<Integer> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<Integer> emotions) {
        this.emotions = emotions;
    }

    public void addEmotion(int i) {
        this.emotions.add(i);
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
}
