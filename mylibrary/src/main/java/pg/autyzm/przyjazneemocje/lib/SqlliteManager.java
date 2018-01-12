package pg.autyzm.przyjazneemocje.lib;

/**
 * Created by Ann on 26.09.2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pg.autyzm.przyjazneemocje.lib.entities.Level;

public class SqlliteManager extends SQLiteOpenHelper {

    private static SqlliteManager sInstance;

    private static final String DATABASE_NAME = "przyjazneemocje";


    private SQLiteDatabase db;



    public static synchronized SqlliteManager getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new SqlliteManager(context.getApplicationContext());
        }
        return sInstance;
    }


    private SqlliteManager (final Context context)
    {
        super(new DatabaseContext(context), DATABASE_NAME, null, 14);
        db = getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db)
    {

        this.db = db;

        createTablesInDatabase();
        addEmotionsToDatabase();

    }

    public void onOpen(SQLiteDatabase db){

        this.db = db;

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        this.db = db;

        deleteTablesFromDatabase();
        createTablesInDatabase();
        addEmotionsToDatabase();
    }

    public void addEmotion(String emotion)
    {
        ContentValues values = new ContentValues();
        values.put("emotion",emotion);
        System.out.println(db.insertOrThrow("emotions", null, values) + " addEmotion");
    }

    public void addPhoto(int path, String emotion, String fileName)
    {
        ContentValues values = new ContentValues();
        values.put("path",path);
        values.put("emotion",emotion);
        values.put("name",fileName);
        db.insertOrThrow("photos", null, values);
    }

    public void addVideo(int path, String emotion, String fileName)
    {
        ContentValues values = new ContentValues();
        values.put("path",path);
        values.put("emotion",emotion);
        values.put("name",fileName);
        db.insertOrThrow("videos", null, values);
    }

    public void saveLevelToDatabase(Level level)
    {
        ContentValues values = new ContentValues();
        values.put("photos_or_videos", level.getPhotosOrVideosFlag());
        values.put("name", level.getName());
        values.put("photos_or_videos_per_level", level.getPhotosOrVideosShowedForOneQuestion());
        values.put("time_limit", level.getTimeLimit());
        values.put("is_level_active", level.isLevelActive());
        values.put("correctness", level.getAmountOfAllowedTriesForEachEmotion());
        values.put("sublevels_per_each_emotion", level.getSublevelsPerEachEmotion());
        values.put("is_for_tests", level.isForTests());
        values.put("question_type", level.getQuestionType().toString());
        values.put("hint_types_as_number", level.getHintTypesAsNumber());
        values.put("praises", level.getPraises());


        if(level.getId() != 0) {
            //values.put("id", level.id);
            db.update("levels", values, "id=" + level.getId(), null);


            //usunac wszystkie rekordy polaczone z tym poziomem.
            delete("levels_photos", "levelid", String.valueOf(level.getId()));
            delete("levels_emotions", "levelid", String.valueOf(level.getId()));

        }
        else {
            long longId = db.insertOrThrow("levels", null, values);
            level.setId((int) longId);
        }

        // Dodaj rekordy wiele do wielu ze zdjeciami/video

        for(Integer photoOrVideo : level.getPhotosOrVideosIdList()){

            values = new ContentValues();
            values.put("levelid", level.getId());
            values.put("photoid",photoOrVideo);

            db.insertOrThrow("levels_photos", null, values);
        }

        for(Integer emotion : level.getEmotions()){

            values = new ContentValues();
            values.put("levelid", level.getId());
            values.put("emotionid",emotion + 1);

            db.insertOrThrow("levels_emotions", null, values);
        }


    }

    public void delete(String tableName, String columnName, String value)
    {
        String[] args = {"" + value};
        db.delete(tableName, columnName + "=?",args);
    }

    public void cleanTable(String tableName)
    {
        db.execSQL("delete from "+ tableName);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + tableName +"'");
    }

    public Cursor givePhotosWithEmotion(String emotion)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,"emotion like " + "'%" + emotion + "%'", null, null, null, null);
        return cursor;
    }

    public Cursor givePhotoWithPath(String path)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,"path like " + "'%" + path + "%'", null, null, null, null);
        return cursor;
    }

    public Cursor givePhotoWithId(int id)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,"id like " + "'%" + id + "%'", null, null, null, null);
        return cursor;
    }

    public Cursor givePhotosInLevel(int levelId)
    {
        String[] columns = {"id", "levelid", "photoid"};
        Cursor cursor = db.query("levels_photos", columns,"levelid like " + "'%" + levelId + "%'", null, null, null, null);
        return cursor;
    }

    public Cursor giveAllVideos() //TODO: Change to "giveVideosInLevel(int levelId)
    {
        String[] columns = {"id", "emotion", "name"};
        Cursor cursor = db.query("videos", columns, null, null, null, null, null);
        return cursor;
    }


    public Cursor giveEmotionsInLevel(int levelId)
    {
        String[] columns = {"id", "levelid", "emotionid"};
        Cursor cursor = db.query("levels_emotions", columns,"levelid like " + "'%" + levelId + "%'", null, null, null, null);
        return cursor;
    }


    public Cursor giveEmotionId(String name){

        String[] columns = {"id", "emotion"};
        Cursor cursor = db.query("emotions", columns,"emotion like " + "'%" + name + "%'", null, null, null, null);
        return cursor;

    }

    public Cursor giveEmotionName(int id){

        String[] columns = {"id", "emotion"};
        Cursor cursor = db.query("emotions", columns,"id like " + "'%" + id + "%'", null, null, null, null);
        return cursor;

    }

    public Cursor giveAllEmotions()
    {
        Cursor cursor =  db.rawQuery("select * from emotions", null);
        return cursor;
    }

    public Cursor giveAllLevels()
    {
        Cursor cursor =  db.rawQuery("select * from levels", null);
        return cursor;
    }

    public Cursor giveLevel(int id)
    {
        Cursor cursor =  db.rawQuery("select * from levels where id='" + id + "'" , null);
        return cursor;
    }


    public String giveNameOfEmotionFromPhoto(String nameOfPhoto)
    {
        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,null, null, null, null, null);
        while(cursor.moveToNext()) {
            String name = cursor.getString(3);
            if(name.equals(nameOfPhoto))
                return cursor.getString(2);
        }
        return "Fail";
    }


    private void createTablesInDatabase(){

        System.out.println("Tworze tablee");
        db.execSQL("create table photos(" + "id integer primary key autoincrement," + "path int," + "emotion text," + "name text);" + "");
        db.execSQL("create table emotions(" + "id integer primary key autoincrement," + "emotion text);" + "");
        db.execSQL("create table levels(" + "id integer primary key autoincrement, photos_or_videos text, photos_or_videos_per_level int, " +
                "time_limit int, is_level_active int, name text, correctness int, sublevels_per_each_emotion int, is_for_tests int, question_type text, hint_types_as_number int, praises text);" + "");
        db.execSQL("create table levels_photos(" + "id integer primary key autoincrement,"  + "levelid integer references levels(id)," + "photoid integer references photos(id));" + "");
        db.execSQL("create table levels_emotions(" + "id integer primary key autoincrement," + "levelid integer references levels(id),"  + "emotionid integer references emotions(id));" + "");


    }

    private void deleteTablesFromDatabase(){

        db.execSQL("drop table levels_emotions");
        db.execSQL("drop table levels_photos");
        db.execSQL("drop table levels");
        db.execSQL("drop table emotions");
        db.execSQL("drop table photos");
    }

    private void addEmotionsToDatabase(){

        addEmotion("happy");
        addEmotion("sad");
        addEmotion("angry");
        addEmotion("scared");
        addEmotion("surprised");
        addEmotion("bored");
    }

    public int getPhotoIdByName(String name){

        String[] columns = {"id", "path", "emotion", "name"};
        Cursor cursor = db.query("photos", columns,"name like " + "'%" + name + "%'", null, null, null, null);
        cursor.moveToNext();


        return cursor.getInt(0);
    }

}
