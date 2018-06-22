package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.provider.MediaStore.Images.Media;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pg.autyzm.graprzyjazneemocje.animation.AnimationActivity;
import pg.autyzm.przyjazneemocje.lib.SqliteManager;
import pg.autyzm.przyjazneemocje.lib.entities.Level;

import static pg.autyzm.przyjazneemocje.lib.SqliteManager.getInstance;


public class MainActivity extends Activity implements View.OnClickListener {

    int sublevelsLeft;
    List<Integer> sublevelsList;

    List<String> photosWithEmotionSelected;
    List<String> photosWithRestOfEmotions;
    List<String> photosToUseInSublevel;
    String goodAnswer;
    Cursor cur0;
    Cursor videoCursor;
    SqliteManager sqlm;
    int wrongAnswers;
    int rightAnswers;
    int wrongAnswersSublevel;
    int rightAnswersSublevel;
    int timeout;
    int timeoutSubLevel;
    String commandText;
    boolean animationEnds = true;
    Level level;
    CountDownTimer timer;
    public Speaker speaker;
    boolean videos = false; //TODO: Get from database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sqlm = getInstance(this);

        sqlm.getReadableDatabase();

        // Birgiel

        cur0 = sqlm.giveAllLevels();

        videoCursor = sqlm.giveAllVideos(); //TODO: Change to "giveVideosInLevel(int levelId)
        videoCursor.moveToFirst();

        findNextActiveLevel();

        if(!videos)
            setContentView(R.layout.activity_main);

        else {
            Intent i = new Intent(this, VideoWelcomeActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_videos);
        }

        generateView(photosToUseInSublevel);


        if (!videos && level.isShouldQuestionBeReadAloud()) {
            findViewById(R.id.matchEmotionsSpeakerButton).setVisibility(View.GONE);

            speaker = Speaker.getInstance(MainActivity.this);
            final ImageButton speakerButton = (ImageButton) findViewById(R.id.matchEmotionsSpeakerButton);
            speakerButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    speaker.speak(commandText);
                }
            });
        }
    }


    boolean findNextActiveLevel() {

        if (sublevelsLeft != 0) {
            generateSublevel(sublevelsList.get(sublevelsLeft - 1));
            return true;
        }

        // zaraz zostanie zaladowany nowy poziom (skonczyly sie podpoziomy. trzeba ustalic, czy dziecko odpowiedzialo wystarczajaco dobrze, by przejsc dalej

        while (cur0.moveToNext()) {

            if (!loadLevel()) {
                continue;
            } else {
                return true;
            }
        }

        return false;

    }

    boolean loadLevel() {

        wrongAnswersSublevel = 0;
        rightAnswersSublevel = 0;
        timeoutSubLevel = 0;


        int levelId = 0;
        int photosPerLvL = 0;
        level = null;

        System.out.println(cur0.getCount());


        levelId = cur0.getInt(cur0.getColumnIndex("id"));

        Cursor cur2 = sqlm.giveLevel(levelId);
        Cursor cur3 = sqlm.givePhotosInLevel(levelId);
        Cursor cur4 = sqlm.giveEmotionsInLevel(levelId);

        level = new Level(cur2, cur3, cur4);

        if(level.getPhotosOrVideosFlag().equals("videos")) {
            videos = true;
        }
        else {
            videos = false;
        }

        level.incrementEmotionIdsForGame();

        photosPerLvL = level.getPhotosOrVideosShowedForOneQuestion();


        if (!level.isLevelActive()) return false;

        // tworzymy tablice do permutowania

        if(!videos)
            sublevelsLeft = level.getEmotions().size() * level.getSublevelsPerEachEmotion();
        else
            sublevelsLeft = videoCursor.getCount();

        sublevelsList = new ArrayList<Integer>();

        for (int i = 0; i < level.getEmotions().size(); i++) {

            for (int j = 0; j < level.getSublevelsPerEachEmotion(); j++) {

                sublevelsList.add(level.getEmotions().get(i));

            }

        }

        java.util.Collections.shuffle(sublevelsList);
        generateSublevel(sublevelsList.get(sublevelsLeft - 1));

        // wylosuj emocje z wybranych emocji, odczytaj jej imie (bo mamy liste id)
        //int emotionIndexInList = selectEmotionToChoose(level);

        return true;

    }

    void generateSublevel(int emotionIndexInList) {


        Cursor emotionCur = sqlm.giveEmotionName(emotionIndexInList);

        emotionCur.moveToFirst();
        String selectedEmotionName = emotionCur.getString(emotionCur.getColumnIndex("emotion"));
        // po kolei czytaj nazwy emocji wybranych zdjec, jesli ich emocja = wybranej emocji, idzie do listy a, jesli nie, lista b

        photosWithEmotionSelected = new ArrayList<String>();
        photosWithRestOfEmotions = new ArrayList<String>();
        photosToUseInSublevel = new ArrayList<String>();


        for (int e : level.getPhotosOrVideosIdList()) {

            //System.out.println("Id zdjecia: " + e);
            Cursor curEmotion = sqlm.givePhotoWithId(e);


            curEmotion.moveToFirst();
            String photoEmotionName = curEmotion.getString(curEmotion.getColumnIndex("emotion"));
            String photoName = curEmotion.getString(curEmotion.getColumnIndex("name"));


            if (photoEmotionName.equals(selectedEmotionName)) {
                photosWithEmotionSelected.add(photoName);
            } else {
                photosWithRestOfEmotions.add(photoName);
            }

        }

        // z listy a wybieramy jedno zdjecie, ktore bedzie prawidlowa odpowiedzia

        goodAnswer = selectPhotoWithSelectedEmotion();

        // z listy b wybieramy zdjecia nieprawidlowe

        selectPhotoWithNotSelectedEmotions(level.getPhotosOrVideosShowedForOneQuestion());

        // laczymy dobra odpowiedz z reszta wybranych zdjec i przekazujemy to dalej

        photosToUseInSublevel.add(goodAnswer);

        java.util.Collections.shuffle(photosToUseInSublevel);


        // z tego co rozumiem w photosList powinny byc name wszystkich zdjec, jakie maja sie pojawic w lvl (czyli - 3 pozycje)


        StartTimer(level);
           /* final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
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

                    }
                    timeout ++;
                }
            }, level.timeLimit * 1000);*/


        // /birgiel

    }

    void generateView(List<String> photosList) {

        String rightEmotion = goodAnswer.replace(".jpg", "").replaceAll("[0-9.]", "");

        if(!videos) {
            TextView txt = (TextView) findViewById(R.id.rightEmotion);
            String rightEmotionLang = getResources().getString(getResources().getIdentifier("emotion_" + rightEmotion, "string", getPackageName()));

            if(level.getQuestionType().equals(Level.Question.SHOW_WHERE_IS_EMOTION_NAME))
                commandText = getResources().getString(R.string.label_show_emotion) + " " + rightEmotionLang;
            else if(level.getQuestionType().equals(Level.Question.SHOW_EMOTION_NAME))
                commandText = getResources().getString(R.string.label_show_emotion_short) + " " + rightEmotionLang;
            else if(level.getQuestionType().equals(Level.Question.EMOTION_NAME))
                commandText = rightEmotionLang;

            txt.setText(commandText);
        }
        else
        {
            rightEmotion = videoCursor.getString(videoCursor.getColumnIndex("emotion"));
            String videoName = videoCursor.getString(videoCursor.getColumnIndex("name"));

            VideoView videoView =(VideoView)findViewById(R.id.videoView);

            MediaController mediaController= new MediaController(this);
            mediaController.setAnchorView(videoView);

            videoView.setMediaController(mediaController);

            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            videoView.setVideoPath(root + "FriendlyEmotions/Videos" + File.separator + videoName);
            videoView.requestFocus();
            videoView.start();

            if(!videoCursor.isAfterLast())
                videoCursor.moveToNext();
        }

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.imageGallery);

        linearLayout1.removeAllViews();
        int listSize = photosList.size();

        int height;
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50 - (150 / listSize), getResources().getDisplayMetrics());

        if(!videos) {

            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 790 / listSize, getResources().getDisplayMetrics());

        }
        else {

            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 450 / listSize, getResources().getDisplayMetrics());
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(height, height);
        lp.setMargins(45 / listSize, 10, 45 / listSize, margin);
        lp.gravity = Gravity.CENTER;
        for (String photoName : photosList) {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            File fileOut = new File(root + "FriendlyEmotions/Photos" + File.separator + photoName);
            try {

                ImageView image = new ImageView(MainActivity.this);
                image.setLayoutParams(lp);

                if (photoName.contains(rightEmotion)) {
                    image.setId(1);
                } else {
                    image.setId(0);
                }

                image.setOnClickListener(this);
                Bitmap captureBmp = Media.getBitmap(getContentResolver(), Uri.fromFile(fileOut));
                image.setImageBitmap(captureBmp);
                linearLayout1.addView(image);
            } catch (IOException e) {
                System.out.println("IO Exception " + photoName);
            }
        }


    }


    public void onClick(View v) {
        if (v.getId() == 1) {
            animationEnds = false;
            sublevelsLeft--;
            rightAnswers++;
            rightAnswersSublevel++;
            timer.cancel();

            boolean correctness = true;

            if (sublevelsLeft == 0) {
                correctness = checkCorrectness();
            }

            if (correctness) {
                startAnimationActivity();
            } else {
                startEndActivity(false);
            }

        } else { //jesli nie wybrano wlasciwej
            wrongAnswers++;
            wrongAnswersSublevel++;
        }
    }

    boolean checkCorrectness() {
        if (wrongAnswersSublevel > level.getAmountOfAllowedTriesForEachEmotion()) {
            return false;
        }
        return true;
    }


    /*
        int selectEmotionToChoose(Level level){

            Random rand = new Random();

            int emotionIndexInList = rand.nextInt(level.emotions.size());

            return emotionIndexInList;
        }
    */
    String selectPhotoWithSelectedEmotion() {

        Random rand = new Random();

        int photoWithSelectedEmotionIndex = rand.nextInt(photosWithEmotionSelected.size());

        String name = photosWithEmotionSelected.get(photoWithSelectedEmotionIndex);

        return name;
    }

    void selectPhotoWithNotSelectedEmotions(int howMany) {

        Random rand = new Random();

        for (int i = 0; i < howMany - 1; i++) {

            if (photosWithRestOfEmotions.size() > 0) {
                int photoWithSelectedEmotionIndex = rand.nextInt(photosWithRestOfEmotions.size());
                String name = photosWithRestOfEmotions.get(photoWithSelectedEmotionIndex);

                photosToUseInSublevel.add(name);
                photosWithRestOfEmotions.remove(name);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                animationEnds = true;

                if (!findNextActiveLevel()) {

                    startEndActivity(true);
                }
                else
                {
                    generateView(photosToUseInSublevel);
                    System.out.println("Wygenerowano view");
                }


                break;
            case 2:


                java.util.Collections.shuffle(sublevelsList);
                if(!videos)
                    sublevelsLeft = level.getEmotions().size() * level.getSublevelsPerEachEmotion();
                else
                    sublevelsLeft = videoCursor.getCount();

                wrongAnswersSublevel = 0;
                rightAnswersSublevel = 0;
                timeoutSubLevel = 0;

                generateSublevel(sublevelsList.get(sublevelsLeft - 1));

                break;
        }
    }

    private void startAnimationActivity() {
        if(speaker == null) {
            speaker = Speaker.getInstance(MainActivity.this);
        }
        Intent i = new Intent(MainActivity.this, AnimationActivity.class);
        i.putExtra("praises", level.getPraises());
        startActivityForResult(i, 1);
    }

    private void startEndActivity(boolean pass) {
        Intent in = new Intent(this, EndActivity.class);
        in.putExtra("PASS", pass);
        in.putExtra("WRONG", wrongAnswers);
        in.putExtra("RIGHT", rightAnswers);
        in.putExtra("TIMEOUT", timeout);
        startActivityForResult(in, 2);
    }

    private void StartTimer(Level l) {
        //timer! seconds * 1000
        if (l.getTimeLimit() != 0) {
            final int hintTypes = l.getHintTypesAsNumber();
            final Context currentContext = this;
            timer = new CountDownTimer(l.getTimeLimit() * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {

                    LinearLayout imagesLinear = (LinearLayout) findViewById(R.id.imageGallery);

                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation((float) 0.1);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

                    final int childcount = imagesLinear.getChildCount();
                    for (int i = 0; i < childcount; i++) {
                        ImageView image = (ImageView) imagesLinear.getChildAt(i);
                        if (image.getId() != 1) {
                            if(hintTypes == 8 || hintTypes == 9 || hintTypes == 10 || hintTypes == 11 || hintTypes == 12 || hintTypes == 13 || hintTypes == 15) {
                                image.setColorFilter(filter);
                            }
                        } else {

                            if(hintTypes == 1 || hintTypes == 3 || hintTypes == 5 || hintTypes == 9 || hintTypes == 11 || hintTypes == 13 || hintTypes == 15) {
                                image.setPadding(40, 40, 40, 40);
                                image.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            }
                            if(hintTypes == 4 || hintTypes == 5 || hintTypes == 6 || hintTypes == 7 || hintTypes == 12 || hintTypes == 13 || hintTypes == 15) {
                                Animation shake = AnimationUtils.loadAnimation(currentContext, R.anim.shake);
                                image.startAnimation(shake);
                            }
                            if(hintTypes == 2 || hintTypes == 3 || hintTypes == 6 || hintTypes == 7 || hintTypes == 11 || hintTypes == 14 || hintTypes == 15) {
                                Animation zooming = AnimationUtils.loadAnimation(currentContext, R.anim.zoom);
                                image.startAnimation(zooming);
                            }
                        }
                    }
                    timeout++;
                    timeoutSubLevel++;
                }
            }.start();

        }
    }
}
