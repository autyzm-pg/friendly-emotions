package pg.autyzm.przyjazneemocje;

import android.content.Intent;
import android.database.Cursor;
import android.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pg.autyzm.przyjazneemocje.chooseImages.ChooseImages;
import pg.autyzm.przyjazneemocje.lib.Level;
import pg.autyzm.przyjazneemocje.lib.SqlliteManager;

import static pg.autyzm.przyjazneemocje.lib.SqlliteManager.getInstance;

public class LevelConfiguration extends AppCompatActivity implements View.OnClickListener{


    List<String> emotionsList = new ArrayList<String>();
    List<Integer> emotionsIdsList = new ArrayList<Integer>();
    public SqlliteManager sqlm;
    int editedLevelId;
    Level l;

    ArrayList<Integer> photosOrVideosList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_configuration);

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null)
            value = b.getInt("key");
        System.out.println(value + " to zostalo przekazane do konfiguracji");


       // TextView textPhotos = (TextView) findViewById(R.id.photos);

        //this.deleteDatabase("friendly_emotions.db");

        sqlm = getInstance(this);

        Map<String, String> mapEmo = new ArrayMap<>();
        mapEmo.put("happy",getResources().getString(R.string.emotion_happy));
        mapEmo.put("sad",getResources().getString(R.string.emotion_sad));
        mapEmo.put("angry",getResources().getString(R.string.emotion_angry));
        mapEmo.put("scared",getResources().getString(R.string.emotion_scared));
        mapEmo.put("surprised",getResources().getString(R.string.emotion_surprised));
        mapEmo.put("bored",getResources().getString(R.string.emotion_bored));


        Cursor cur = sqlm.giveAllEmotions();
        while(cur.moveToNext())
        {

            String emotionId = "emotion" + cur.getInt(0);

            int resID = getResources().getIdentifier(emotionId, "id", getPackageName());
            if(resID == 0) break;

            CheckBox checkBox = (CheckBox)findViewById(resID);
            checkBox.setText(mapEmo.get(cur.getString(1)));
            checkBox.setOnCheckedChangeListener(new myCheckBoxChnageClicker());
        }


        //Wywolanie kamery - nie dziala na emulatorze
        /*final Camera camera = CameraOptions.getCameraInstance();
        final CameraPreview cameraPreview = new CameraPreview(this, camera);

        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(cameraPreview);

        Button captureButton = (Button) findViewById(R.id.cameraButton);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null,  CameraOptions.getCameraPhoto());
            }
        });*/

        View buttonChoose = findViewById(R.id.button_choose_images);
        buttonChoose.setOnClickListener(this);

        ImageButton buttonCamera = (ImageButton) findViewById(R.id.button_take_photo);
        buttonCamera.setOnClickListener(this);

        //updateLevelList();

        // jesli zostal przechwycony jakis id, to znaczy ze jestemy w trybie edycji poziomu, a nie jego tworzenia
        // ladujemy do interfejsu wartosci z rekordu tabeli Level
        if(value > 0){

            loadLevel(value);

        }


    }


    void loadLevel(int value){


        editedLevelId = value;

        Cursor cur2 = sqlm.giveLevel(editedLevelId);
        Cursor cur3 = sqlm.givePhotosInLevel(editedLevelId);
        Cursor cur4 = sqlm.giveEmotionsInLevel(editedLevelId);

        l = new Level(cur2, cur3, cur4);

        EditText timeLimit = (EditText)findViewById(R.id.time_limit);
        EditText vpPerLevel = (EditText)findViewById(R.id.pv_per_level);
        EditText correctness = (EditText)findViewById(R.id.correctness);
        EditText sublevels = (EditText)findViewById(R.id.sublevels);
        EditText levelName  = (EditText)findViewById(R.id.level_name);
//        Spinner photosOrVideos = (Spinner)findViewById(R.id.spinner2);
//        TextView tv = (TextView) findViewById(R.id.imagesCount);



        timeLimit.setText(Integer.toString(l.getTimeLimit()));
        vpPerLevel.setText(Integer.toString(l.getPvPerLevel()));
        correctness.setText(Integer.toString(l.getCorrectness()));
        sublevels.setText(Integer.toString(l.getSublevels()));



        for(Integer i : l.getPhotosOrVideosList()){

            photosOrVideosList.add(i);

        }


        levelName.setText(l.getName());

        updateListSize();

        for(int i : l.getEmotions()){

            int id = 0;

            switch (i) {
                case 1:
                    id = R.id.emotion1;
                    break;
                case 2:
                    id = R.id.emotion2;
                    break;
                case 3:
                    id = R.id.emotion3;
                    break;
                case 4:
                    id = R.id.emotion4;
                    break;
                case 5:
                    id = R.id.emotion5;
                    break;
                case 6:
                    id = R.id.emotion6;
                    break;
            }

            CheckBox checkBox = (CheckBox)findViewById(id);
            checkBox.setChecked(true);

        }



    }





    //jako glowna emocja pokazuje sie stara (mimo usuniecia), az do ponownego wyboru
    //trzeba cos zmienic w spinnerze?
    class myCheckBoxChnageClicker implements CheckBox.OnCheckedChangeListener
    {


        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
        {

            String emotionNameInLanguage = buttonView.getText().toString();

            Map<String, String> mapEmo = new android.util.ArrayMap<>();
            mapEmo.put(getResources().getString(R.string.emotion_happy),"happy");
            mapEmo.put(getResources().getString(R.string.emotion_sad),"sad");
            mapEmo.put(getResources().getString(R.string.emotion_angry),"angry");
            mapEmo.put(getResources().getString(R.string.emotion_scared),"scared");
            mapEmo.put(getResources().getString(R.string.emotion_surprised),"surprised");
            mapEmo.put(getResources().getString(R.string.emotion_bored),"bored");

            String emotionName = mapEmo.get(emotionNameInLanguage);


            Cursor cc = sqlm.giveEmotionId(emotionName);
            int emotionId = -1;

            while(cc.moveToNext()){
                emotionId = cc.getInt(cc.getColumnIndex("id"));
            }

            if(isChecked) {
                emotionsList.add(emotionNameInLanguage);
                emotionsIdsList.add(emotionId);
            }
            if(!isChecked) {
                emotionsList.remove(emotionNameInLanguage);
                emotionsIdsList.remove((Object)emotionId);
                removeImgEmo(emotionName);
            }

            Spinner spinner = (Spinner)findViewById(R.id.spinner);
            updateEmotionsList(spinner, emotionsList);
            updateListSize();
        }
    }


    private void removeImgEmo(String emotion) {
        Cursor cursor = sqlm.givePhotosWithEmotion(emotion);
        while(cursor.moveToNext()) {
            if (photosOrVideosList.contains(cursor.getInt(0))) {
                photosOrVideosList.remove((Object) cursor.getInt(0));
            }
        }
    }


    public static void updateEmotionsList(Spinner spinner, List<String> emotionsList)
    {
        if (emotionsList.size() != 0)
        {
            SpinnerAdapter oldAdapter = spinner.getAdapter();
            boolean changed = true;
            if (oldAdapter != null && oldAdapter.getCount() == emotionsList.size())
            {
                changed = false;
                for (int i = 0; i < emotionsList.size(); i++)
                {
                    if (!emotionsList.get(i).equals(oldAdapter.getItem(i))) {
                        changed = true;
                        break;
                    }
                }
            }
            if (changed)
            {

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, emotionsList);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        }
        else
        {
            spinner.setAdapter(null);
        }
    }


    public void addLevel(View view) {


        Level l = new Level();

        EditText timeLimit = (EditText) findViewById(R.id.time_limit);
        EditText correctness = (EditText) findViewById(R.id.correctness);
        EditText sublevels = (EditText) findViewById(R.id.sublevels);
        EditText levelName = (EditText) findViewById(R.id.level_name);
        EditText vpPerLevel = (EditText) findViewById(R.id.pv_per_level);

        l.setTimeLimit(Integer.parseInt(timeLimit.getText() + ""));
        l.setPvPerLevel(Integer.parseInt(vpPerLevel.getText() + ""));
        l.setCorrectness(Integer.parseInt(correctness.getText() + ""));
        l.setSublevels(Integer.parseInt(sublevels.getText() + ""));

        l.setName(levelName.getText().toString());

        if (editedLevelId > 0) {
            l.setId(editedLevelId);


            // po przekazaniu informacji, ze mamy juz jakies id (czyli jest to edycja i jakis rekord ma byc nadpisany), zerujemy id, na wypadek,
            // gdyby user jeszcze raz wlaczyl zapisz - wtedy z braku id-ka uzna, ze to tworzenie nowego poziomu i stworzy nowy rekord
            editedLevelId = 0;
        }


        l.setPhotosOrVideosList(photosOrVideosList);
        l.setEmotions(emotionsIdsList);

        LevelValidator levelValidator = new LevelValidator(l);
        if (levelValidator.validateLevel()){

            sqlm.addLevel(l);
            final TextView msg = (TextView) findViewById(R.id.saveMessage);
            msg.setVisibility(View.VISIBLE);
            msg.postDelayed(new Runnable() {
                public void run() {
                    msg.setVisibility(View.INVISIBLE);
                }
            }, 2000);

        }
        else{
            // do nothing (for now)
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_choose_images:
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                Bundle bundle = new Bundle();
                bundle.putString("SpinnerValue", spinner.getSelectedItem().toString());      //TODO gdy mamy zaznaczone wszystkie i jedno się odznaczy to przesyła ostatnio zaznaczoną emocję

                // Birgiel. Dodanie emocji wybranych, gdy lvl byl tworzony

                // przerabiamy krotkie id na dlugie (path)

//                ArrayList<Integer> list = new ArrayList<Integer>();

//                if (l != null){
//
//                    for (Integer photoPath : l.photosOrVideosList) {
//
//                        Cursor pp = sqlm.givePhotoWithId(photoPath);
//                        pp.moveToFirst();
//                        int path = pp.getInt(pp.getColumnIndex("path"));
//                        list.add(path);
//
//                    }
//                }
//
                if(l != null) {
                    bundle.putIntegerArrayList("selected_photos", ( ArrayList<Integer>) l.getPhotosOrVideosList());
                }
                //
                else {
                    bundle.putIntegerArrayList("selected_photos", photosOrVideosList);
                }

                Intent i = new Intent(this,ChooseImages.class);
                i.putExtras(bundle);
                startActivityForResult(i,1);
                break;
            case R.id.button_take_photo:
                Spinner spinner2 = (Spinner) findViewById(R.id.spinner);
                Bundle bundle2 = new Bundle();
                bundle2.putString("SpinnerValue", spinner2.getSelectedItem().toString());
                Intent in = new Intent(this, CameraActivity.class);
                in.putExtras(bundle2);
                startActivityForResult(in,1);
                break;
        }
    }

    private void updateListSize(){
        TextView tv = (TextView) findViewById(R.id.imagesCount);
        String str = getResources().getString(R.string.select);
        tv.setText(str + ": " + photosOrVideosList.size());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String choosenImg="";
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                photosOrVideosList = bundle.getIntegerArrayList("selected_photos");
                //photosOrVideosList.addAll(bundle.getIntegerArrayList("selected_photos"));
                updateListSize();
            }
        }

    }
    public void closee(View view) {
        finish();
    }
}
