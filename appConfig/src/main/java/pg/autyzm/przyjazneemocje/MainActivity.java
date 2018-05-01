package pg.autyzm.przyjazneemocje;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import pg.autyzm.przyjazneemocje.lib.SqliteManager;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static pg.autyzm.przyjazneemocje.lib.SqliteManager.getInstance;

public class MainActivity extends AppCompatActivity {

    public SqliteManager sqlm;
    ArrayList<String> list;
    ArrayList<Boolean> active_list;
    String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File createMainDir = new File(root + "FriendlyEmotions" + File.separator);

        if (!createMainDir.exists())
            createMainDir.mkdir();

        sqlm = getInstance(this);

        updateLevelList();
        //generate list

        sqlm.cleanTable("photos"); //TODO not clean and add, but only update
        sqlm.cleanTable("videos");

        File createDir = new File(root + "FriendlyEmotions/Photos" + File.separator);
        if (!createDir.exists()) {
            createDir.mkdir();

            Field[] drawables = pg.autyzm.przyjazneemocje.R.drawable.class.getFields();
            for (Field f : drawables) {
                try {
                    if (ifConstainsEmotionName(f.getName()))
                    {
                        extractFromDrawable(f, "Photos", ".jpg", Bitmap.CompressFormat.JPEG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
/*
        File createDirV = new File(root + "FriendlyEmotions/Videos" + File.separator);
        if (!createDirV.exists()) {
            createDirV.mkdir();

            Field[] raw = pg.autyzm.przyjazneemocje.R.raw.class.getFields();
            for (Field f : raw) {
                try {
                    extractFromDrawable(f, "Videos", ".mp4", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/

        if(new File(root + "FriendlyEmotions/Photos").list() != null) {

            for (String emotName : new File(root + "FriendlyEmotions/Photos").list()) {

                try {
                    int resID = getResources().getIdentifier(emotName, "drawable", getPackageName());
                    if (emotName.contains("happy"))
                        sqlm.addPhoto(resID, "happy", emotName);
                    else if (emotName.contains("angry"))
                        sqlm.addPhoto(resID, "angry", emotName);
                    else if (emotName.contains("surprised"))
                        sqlm.addPhoto(resID, "surprised", emotName);
                    else if (emotName.contains("bored"))
                        sqlm.addPhoto(resID, "bored", emotName);
                    else if (emotName.contains("scared"))
                        sqlm.addPhoto(resID, "scared", emotName);
                    else if (emotName.contains("sad"))
                        sqlm.addPhoto(resID, "sad", emotName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(new File(root + "FriendlyEmotions/Videos").list() != null) {

            for (String emotName : new File(root + "FriendlyEmotions/Videos").list()) {

                try {
                    int resID = getResources().getIdentifier(emotName, "raw", getPackageName());
                    if (emotName.contains("happy"))
                        sqlm.addVideo(resID, "happy", emotName);
                    else if (emotName.contains("angry"))
                        sqlm.addVideo(resID, "angry", emotName);
                    else if (emotName.contains("surprised"))
                        sqlm.addVideo(resID, "surprised", emotName);
                    else if (emotName.contains("bored"))
                        sqlm.addVideo(resID, "bored", emotName);
                    else if (emotName.contains("scared"))
                        sqlm.addVideo(resID, "scared", emotName);
                    else if (emotName.contains("sad"))
                        sqlm.addVideo(resID, "sad", emotName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // napisuje, bo chce, by po dodaniu poziomu lista poziomow w main activity automatycznie sie odswiezala - Pawel
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here

        updateLevelList();
        
    }

    public boolean ifConstainsEmotionName(String inputString)
    {
        Cursor cur = sqlm.giveAllEmotions();
        while(cur.moveToNext()) {
            String emotion = cur.getString(1);
            if(inputString.contains(emotion))
                return true;
        }
        return false;
    }


    public void sendMessage(View view) {
        // Do something in response to button

        Intent intent = new Intent(this, LevelConfigurationActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "String";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    public void updateLevelList(){

        Cursor cur = sqlm.giveAllLevels();
        list = new ArrayList<String>();
        active_list = new ArrayList<Boolean>();

        while(cur.moveToNext())
        {
            String name = cur.getString(cur.getColumnIndex("name"));
            String levelId = cur.getInt(0) + " " + name;
            //String levelId = "Level " + cur.getInt(0);

            int active = cur.getInt(cur.getColumnIndex("is_level_active"));
            boolean isLevelActive = (active != 0);
            active_list.add(isLevelActive);
            list.add(levelId);

        }

        //instantiate custom adapter
        CustomList adapter = new CustomList(list, active_list, this);

        //handle listview and assign adapter
        ListView lView = (ListView) findViewById(R.id.list);
        lView.setAdapter(adapter);

    }

    private void extractFromDrawable(Field field, String dir, String fileExt, Bitmap.CompressFormat format) throws IOException {

        String emotName = field.getName();
        int resID = getResources().getIdentifier(emotName, "drawable", getPackageName());
        String path = root + "FriendlyEmotions/" + dir + File.separator;
        File file = new File(path, emotName + fileExt);
        FileOutputStream outStream = new FileOutputStream(file);

        if(format != null)
        {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), resID);
            bm.compress(format, 100, outStream);
            outStream.flush();
            outStream.close();

        } else {
            resID = getResources().getIdentifier(emotName, "raw", getPackageName());
            InputStream in = getResources().openRawResource(resID);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    outStream.write(buff, 0, read);
                }
            } finally {
                in.close();
                outStream.close();
            }
        }
    }
}