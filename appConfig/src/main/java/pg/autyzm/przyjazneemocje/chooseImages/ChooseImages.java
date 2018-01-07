package pg.autyzm.przyjazneemocje.chooseImages;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.ArrayMap;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import pg.autyzm.przyjazneemocje.R;
import pg.autyzm.przyjazneemocje.lib.SqlliteManager;

import static pg.autyzm.przyjazneemocje.lib.SqlliteManager.getInstance;


/**
 * Created by Joanna on 2016-10-08.
 */

public class ChooseImages extends Activity implements android.widget.CompoundButton.OnCheckedChangeListener {

    private ListView listView;
    private String choosenEmotion;
    private RowBean[] tabPhotos;
    private TextView textView;
    private String emoInLanguage;
    private ArrayList<Integer> listSelectedPhotos;

    public void saveImagesToList(View view) {

        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("selected_photos", listSelectedPhotos);
        Intent returnIntent = new Intent();
        returnIntent.putExtras(bundle);
        setResult(RESULT_OK, returnIntent);

        finish();
    }

    public void close(View view) {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choose_images);

        SqlliteManager sqlm = getInstance(this);

        Bundle bundle = getIntent().getExtras();
        emoInLanguage = bundle.getString("SpinnerValue");

        Map<String, String> mapEmo = new ArrayMap<>();
        mapEmo.put(getResources().getString(R.string.emotion_happy), "happy");
        mapEmo.put(getResources().getString(R.string.emotion_sad), "sad");
        mapEmo.put(getResources().getString(R.string.emotion_angry), "angry");
        mapEmo.put(getResources().getString(R.string.emotion_scared), "scared");
        mapEmo.put(getResources().getString(R.string.emotion_surprised), "surprised");
        mapEmo.put(getResources().getString(R.string.emotion_bored), "bored");

        choosenEmotion = mapEmo.get(emoInLanguage);

        textView = (TextView) findViewById(R.id.TextViewChoose);
        String str = getResources().getString(R.string.select);

        Cursor cursor = sqlm.givePhotosWithEmotion(choosenEmotion);

        //tu dodaje
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";

        String newFileName = "";

        String[] photosNameList = new File(root + "/FriendlyEmotions/Photos").list();
        if(cursor.getCount() < photosNameList.length)
        {
            for(String fileName : photosNameList)
            {

                String tmp = fileName.replace(".jpg","").replaceAll("[0-9]","");
                if(tmp.equals(choosenEmotion))
                {
                    cursor = sqlm.givePhotosWithEmotion(choosenEmotion);
                    boolean finded = true;
                    while(cursor.moveToNext())
                    {
                        finded = false;
                        if(cursor.getString(3).equals(fileName))
                        {
                            finded = true;
                            break;
                        }
                    }
                    if(finded == false)
                        sqlm.addPhoto(1,choosenEmotion,fileName);
                }
            }
        }

        cursor = sqlm.givePhotosWithEmotion(choosenEmotion);


        //

        int n = cursor.getCount();
        tabPhotos = new RowBean[n];
        while (cursor.moveToNext()) {

            tabPhotos[--n] = (new RowBean(cursor.getString(3),cursor.getInt(1), false, getContentResolver(), cursor.getInt(0)));
        }

        //wybrane wczesniej
        listSelectedPhotos = bundle.getIntegerArrayList("selected_photos");
        for (int selected : listSelectedPhotos) {
            for (RowBean el : tabPhotos) {
                if (el.getId() == selected) {
                    el.setSelected(true);
                }
            }
        }

        textView.setText(emoInLanguage + " " + str + ": " + countSelectedPhotos());

        RowAdapter adapter = new RowAdapter(this, R.layout.item, tabPhotos);
        listView = (ListView) findViewById(R.id.image_list);
        listView.setAdapter(adapter);
    }

    private int countSelectedPhotos() {
        int numberOfPhotos = 0;
        for (RowBean el : tabPhotos) {
            if (el.selected) {
                numberOfPhotos++;
            }
        }
        return numberOfPhotos;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            int pos = listView.getPositionForView(buttonView);
            if (pos != ListView.INVALID_POSITION) {
                if (isChecked) {
                    tabPhotos[pos].setSelected(true);
                    listSelectedPhotos.add(tabPhotos[pos].getId());
                    System.out.println("To trafia do tablicy z idkami photos " + tabPhotos[pos].getId());
                } else {
                    tabPhotos[pos].setSelected(false);
                     listSelectedPhotos.remove((Object)tabPhotos[pos].getId());
                }
            }

            String str = getResources().getString(R.string.select);
            textView.setText(emoInLanguage + " " + str + ": " + countSelectedPhotos());
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }
}
