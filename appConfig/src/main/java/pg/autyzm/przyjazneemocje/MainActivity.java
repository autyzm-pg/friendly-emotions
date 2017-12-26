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
import java.lang.reflect.Field;
import java.util.ArrayList;

import pg.autyzm.przyjazneemocje.View.*;
import pg.autyzm.przyjazneemocje.View.LevelConfiguration;
import pg.autyzm.przyjazneemocje.lib.SqlliteManager;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static pg.autyzm.przyjazneemocje.lib.SqlliteManager.getInstance;

public class MainActivity extends AppCompatActivity {

    public SqlliteManager sqlm;
    ArrayList<String> list;
    ArrayList<Boolean> active_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlm = getInstance(this);

        updateLevelList();
        //generate list

        sqlm.cleanTable("photos"); //TODO not clean and add, but only update

        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

        File createDir = new File(root + "Emotions" + File.separator);
        if (!createDir.exists()) {
            createDir.mkdir();

            Field[] drawables = pg.autyzm.przyjazneemocje.R.drawable.class.getFields();
            for (Field f : drawables) {
                try {
                    if (IfConstainsEmotionName(f.getName()))
                    {
                        String emotName = f.getName();
                        int resID = getResources().getIdentifier(emotName, "drawable", getPackageName());

                        Bitmap bm = BitmapFactory.decodeResource(getResources(), resID);

                        String path = root + "Emotions" + File.separator;

                        File file = new File(path, emotName + ".jpg");
                        FileOutputStream outStream = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        outStream.flush();
                        outStream.close();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if(new File(root + "/Emotions").list() != null) {

            for (String emotName : new File(root + "/Emotions").list()) {

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


    }


    // napisuje, bo chce, by po dodaniu poziomu lista poziomow w main activity automatycznie sie odswiezala - Pawel
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here

        updateLevelList();
        
    }

    public boolean IfConstainsEmotionName(String inputString)
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

        Intent intent = new Intent(this, LevelConfiguration.class);
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
}