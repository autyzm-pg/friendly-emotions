package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.provider.MediaStore.Images.Media;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pg.autyzm.przyjazneemocje.lib.SqlliteManager;
import pg.autyzm.przyjazneemocje.lib.entities.Level;

import static pg.autyzm.przyjazneemocje.lib.SqlliteManager.getInstance;


public class MainActivity extends Activity implements View.OnClickListener {


    List<LevelInGame> levelsInGame = new ArrayList<>();
    List<Level> levels = new ArrayList<>();
    LevelInGame currentLevelInGame;
    List<String> photosWithEmotionSelected;
    List<String> photosWithRestOfEmotions;
    List<String> photosToUseInSublevel;
    String goodAnswer;
    SqlliteManager sqlm;
    int wrongAnswers;
    int rightAnswers;
    int timeout;
    String commandText;

    CountDownTimer timer;
    public Speaker speaker;

    private final int REQUEST_CODE_SUBLEVEL_END = 1;
    private final int REQUEST_CODE_GAME_END = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        sqlm = getInstance(this);
        loadLevelsFromDatabase();
        prepareLevelsInGame();

        generateNextSublevel();

        speaker = Speaker.getInstance(MainActivity.this);

        final ImageButton speakerButton = (ImageButton) findViewById(R.id.matchEmotionsSpeakerButton);
        speakerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                speaker.speak(commandText);
            }
        });

    }

    void loadLevelsFromDatabase(){

        Cursor cursorLevels = sqlm.giveAllLevels();

        while(cursorLevels.moveToNext()){

            int levelId = cursorLevels.getInt(cursorLevels.getColumnIndex("id"));
            Cursor cursorLevelTemp = sqlm.giveLevel(levelId);
            Cursor cursorPhotos = sqlm.givePhotosInLevel(levelId);
            Cursor cursorEmotions = sqlm.giveEmotionsInLevel(levelId);

            Level newLevel = new Level(cursorLevelTemp, cursorPhotos, cursorEmotions);

            if(newLevel.isLevelActive())
                levels.add(newLevel);
        }

    }

    void prepareImagesForNextSublevel(int emotionId){


        Cursor emotionCur = sqlm.giveEmotionName(emotionId);

        emotionCur.moveToFirst();
        int columnIndex = emotionCur.getColumnIndex("emotion");
        String selectedEmotionName = emotionCur.getString(columnIndex);
        // po kolei czytaj nazwy emocji wybranych zdjec, jesli ich emocja = wybranej emocji, idzie do listy a, jesli nie, lista b

        photosWithEmotionSelected = new ArrayList<String>();
        photosWithRestOfEmotions = new ArrayList<String>();
        photosToUseInSublevel = new ArrayList<String>();



        for(int e : currentLevelInGame.getLevelFromDatabase().getPhotosOrVideosIdList()){

            Cursor curEmotion = sqlm.givePhotoWithId(e);

            curEmotion.moveToFirst();
            String photoEmotionName = curEmotion.getString(curEmotion.getColumnIndex("emotion"));
            String photoName = curEmotion.getString(curEmotion.getColumnIndex("name"));

            if(photoEmotionName.equals(selectedEmotionName)){
                photosWithEmotionSelected.add(photoName);
            }
            else{
                photosWithRestOfEmotions.add(photoName);

            }

        }

        // z listy a wybieramy jedno zdjecie, ktore bedzie prawidlowa odpowiedzia

        goodAnswer = selectPhotoWithSelectedEmotion();

        // z listy b wybieramy zdjecia nieprawidlowe

        selectPhotoWithNotSelectedEmotions(currentLevelInGame.getLevelFromDatabase().getPhotosOrVideosShowedForOneQuestion());

        // laczymy dobra odpowiedz z reszta wybranych zdjec i przekazujemy to dalej

        photosToUseInSublevel.add(goodAnswer);

        java.util.Collections.shuffle(photosToUseInSublevel);


        // z tego co rozumiem w photosList powinny byc name wszystkich zdjec, jakie maja sie pojawic w lvl (czyli - 3 pozycje)

        StartTimer(currentLevelInGame.getLevelFromDatabase());

    }

    void displaySublevel(List<String> photosList){


        TextView txt = (TextView) findViewById(R.id.rightEmotion);
        // txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,100);
        String rightEm = goodAnswer.replace(".jpg","").replaceAll("[0-9.]", "");
        String rightEmotionLang = getResources().getString(getResources().getIdentifier("emotion_" + rightEm, "string", getPackageName()));
        commandText = getResources().getString(R.string.label_show_emotion) + " " + rightEmotionLang;
        txt.setText(commandText);

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.imageGallery);

        linearLayout1.removeAllViews();
        int listSize=photosList.size();

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 790/listSize, getResources().getDisplayMetrics());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50-(150/listSize), getResources().getDisplayMetrics());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(height, height);
        lp.setMargins(45/listSize, 10, 45/listSize, margin);
        lp.gravity = Gravity.CENTER;
        for(String photoName:photosList)
        {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
            File fileOut = new File(root + "Emotions" + File.separator + photoName);
            try {

                ImageView image = new ImageView(MainActivity.this);
                image.setLayoutParams(lp);

                if(photoName.equals(goodAnswer)){
                    image.setId(1);
                }
                else{
                    image.setId(0);
                }


                image.setOnClickListener(this);
                Bitmap captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(fileOut));
                image.setImageBitmap(captureBmp);
                linearLayout1.addView(image);
            }
            catch(IOException e) {
                System.out.println("IO Exception " + photoName);
            }
        }
    }


    public void onClick(View v) {


        if(v.getId() == 1) {

            rightAnswers++;
            currentLevelInGame.incrementRightAnswersSublevel();
            timer.cancel();

            if(checkCorrectness()) {
                Intent i = new Intent(this, AnimationActivity.class);
                startActivityForResult(i, REQUEST_CODE_SUBLEVEL_END);
            }
            else{
                startEndActivity(false);
                currentLevelInGame.resetLevelCounters();
            }
        }
        else //jesli nie wybrano wlasciwej
        {
            wrongAnswers++;
            currentLevelInGame.incrementWrongAnswersSublevel();
        }
    }

    boolean checkCorrectness(){

        return currentLevelInGame.getWrongAnswersSublevel() > currentLevelInGame.getLevelFromDatabase().getAmountOfAllowedTriesForEachEmotion() ? false : true;

    }


    String selectPhotoWithSelectedEmotion(){

        Random rand = new Random();

        int photoWithSelectedEmotionIndex = rand.nextInt(photosWithEmotionSelected.size());

        String name = photosWithEmotionSelected.get(photoWithSelectedEmotionIndex);

        return name;
    }

    void selectPhotoWithNotSelectedEmotions(int howMany){

        Random rand = new Random();

        for(int i = 0; i < howMany - 1; i++) {

            if(photosWithRestOfEmotions.size() > 0){
                int photoWithSelectedEmotionIndex = rand.nextInt(photosWithRestOfEmotions.size());
                String name = photosWithRestOfEmotions.get(photoWithSelectedEmotionIndex);

                photosToUseInSublevel.add(name);
                photosWithRestOfEmotions.remove(name);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        generateNextSublevel();
    }

    private void startEndActivity(boolean pass){
        Intent in = new Intent(this, EndActivity.class);
        in.putExtra("PASS", pass);
        in.putExtra("WRONG", wrongAnswers);
        in.putExtra("RIGHT", rightAnswers);
        in.putExtra("TIMEOUT", timeout);


        startActivityForResult(in, REQUEST_CODE_GAME_END);

    }



    private void generateNextSublevel(){

        getCurrentLevelInGame();

        if(currentLevelInGame == null) {
            startEndActivity(true);
            resetGameCounters();
        } else {
            prepareImagesForNextSublevel(currentLevelInGame.getSublevelsList().get(currentLevelInGame.getCurrentSublevelNumber()));
            displaySublevel(photosToUseInSublevel);
        }


    }

    boolean getCurrentLevelInGame(){

        currentLevelInGame = null;
        for(LevelInGame levelInGame : levelsInGame){

            levelInGame.nextSublevel();

            if(levelInGame.isLevelPassed())
                continue;
            else{
                currentLevelInGame = levelInGame;
                return true;
            }
        }

        return false;

    }



    private void resetGameCounters(){
        wrongAnswers = 0;
        rightAnswers = 0;
        timeout = 0;

        resetCountersForAllLevels();
    }

    void resetCountersForAllLevels(){

        for(LevelInGame level : levelsInGame){
            level.resetLevelCounters();
        }
    }

    void prepareLevelsInGame(){

        for(Level level : levels){
            LevelInGame newLevelInGame = new LevelInGame(level);
            newLevelInGame.prepareLevel();
            levelsInGame.add(newLevelInGame);
        }
    }

    private void StartTimer(Level l)
    {
        //timer! seconds * 1000
        if(l.getTimeLimit() != 0)
        {
            timer = new CountDownTimer(l.getTimeLimit() * 1000, 1000) {

                public void onTick(long millisUntilFinished) {


                }
                public void onFinish() {
                    LinearLayout imagesLinear = (LinearLayout)findViewById(R.id.imageGallery);

                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation((float)0.1);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

                    final int childcount = imagesLinear.getChildCount();
                    for (int i = 0; i < childcount; i++)
                    {
                        ImageView image = (ImageView) imagesLinear.getChildAt(i);
                        if(image.getId() != 1)
                        {
                            image.setColorFilter(filter);
                        }
                        else
                        {
                            image.setPadding(40,40,40,40);
                            image.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        }

                    }
                    timeout ++;
                    currentLevelInGame.incrementTimeoutSublevel();
                }
            }.start();

        }
    }


}
