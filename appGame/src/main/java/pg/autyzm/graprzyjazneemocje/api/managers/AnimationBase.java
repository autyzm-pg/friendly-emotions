package pg.autyzm.graprzyjazneemocje.api.managers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import pg.autyzm.graprzyjazneemocje.R;
import pg.autyzm.graprzyjazneemocje.api.entities.Picture;
import pg.autyzm.graprzyjazneemocje.api.entities.PicturesContainer;

/**
 * Created by joagi on 13.01.2018.
 */

public class AnimationBase {

    private Animation animation;
    private Activity activity;
    private PicturesContainer picturesContainer;


    public AnimationBase(Activity activity){
        this.activity = activity;
    }


    private void createAnimation(List<Picture> pictureList, AnimationType[] animationTypes){


        Random rand = new Random();
        AnimationType animationType = animationTypes[rand.nextInt(animationTypes.length)];

        switch (animationType) {
            case STRAIGHT_FLY_UP_DOWN:
                straightFlyUpDown(activity, pictureList);
                break;
            case GO_LEFT_TO_RIGHT:
                goLeftToRight(activity, pictureList);
                break;
            case SPIRAL:
                spiral(activity, pictureList);
                break;
        }
    }


    private void prepareLayout() {

        AnimationType[] allowedAnimationTypes;

        if(picturesContainer.getCategoryName().equals("planes") || picturesContainer.getCategoryName().equals("ships")){
            allowedAnimationTypes = new AnimationType[]{AnimationType.SPIRAL, AnimationType.GO_LEFT_TO_RIGHT};
        }
        else if(picturesContainer.getCategoryName().equals("trains")){
            allowedAnimationTypes = new AnimationType[]{AnimationType.GO_LEFT_TO_RIGHT};
        }
        else{
            allowedAnimationTypes = AnimationType.values();
        }

        createAnimation(picturesContainer.getPicturesInCategory(), allowedAnimationTypes);

    }

    private void goLeftToRight(Context context, List<Picture> images) {

        ImageView animImage;

        activity.setContentView(R.layout.activity_anim_right_left);

        int imagesNr[] = {R.id.image1, R.id.image2, R.id.image3};
        for (int image : imagesNr) {

            animImage = (ImageView) activity.findViewById(image);

            Picture drawnPicture = images.get(new Random().nextInt(images.size()));
            loadPictureToAnimation(animImage, drawnPicture);

            animation = AnimationUtils.loadAnimation(context, R.anim.right);
            animation.setStartOffset(new Random().nextInt(1500));
            animation.setDuration(new Random().nextInt(1000) + 1500);
            animImage.startAnimation(animation);
        }
    }

    private void loadPictureToAnimation(ImageView imageView, Picture picture){

        File imgFile = new  File(picture.getPath());

        if(imgFile.exists()){

            try {
                FileInputStream fileInputStream = new FileInputStream(imgFile);
                Bitmap myBitmap = BitmapFactory.decodeStream(fileInputStream);
                imageView.setImageBitmap(myBitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //imageView.setImageBitmap(myBitmap);
            Log.i("Files", "Picture loaded from path: " + picture.getPath());

        }
    }

    private void straightFlyUpDown(Context context, List<Picture> images) {
        ImageView animImage;

        activity.setContentView(R.layout.activity_anim_straight);

        int imagesNr[] = {R.id.image1, R.id.image2, R.id.image3, R.id.image4};
        int n = 0;
        for (int image : imagesNr) {

            animImage = (ImageView) activity.findViewById(image);

            Picture drawnPicture = images.get(new Random().nextInt(images.size()));
            loadPictureToAnimation(animImage, drawnPicture);

            if((n++)%2==0) {
                animImage.setRotation(270);
                animation = AnimationUtils.loadAnimation(context, R.anim.up);
            } else {
                animImage.setRotation(90);
                animation = AnimationUtils.loadAnimation(context, R.anim.down);
            }
            animImage.startAnimation(animation);
        }
    }

    private void spiral(Context context, List<Picture> images) {
        ImageView animImage;

        activity.setContentView(R.layout.activity_anim_spiral);
        animImage = (ImageView) activity.findViewById(R.id.image1);
        Picture drawnPicture = images.get(new Random().nextInt(images.size()));
        loadPictureToAnimation(animImage, drawnPicture);

        animation = AnimationUtils.loadAnimation(context, R.anim.spiral);
        animImage.startAnimation(animation);

        animImage = (ImageView) activity.findViewById(R.id.image2);
        drawnPicture = images.get(new Random().nextInt(images.size()));
        loadPictureToAnimation(animImage, drawnPicture);

        animation = AnimationUtils.loadAnimation(context, R.anim.spirall);
        animImage.startAnimation(animation);

    }

    private enum AnimationType {

        STRAIGHT_FLY_UP_DOWN, GO_LEFT_TO_RIGHT, SPIRAL


    }

    public Animation prepareAndReturnRandomAward(String[] selectedPrizes) {

        List<PicturesContainer> picturesContainers = ExternalAnimationManager.getInstance().getAllAnimationsFromStorage();

        if(picturesContainers.isEmpty()){
            // use internal storage, if there's nothing in the external one
            throw new RuntimeException("This path is still not implemented");

        }else {

            if (selectedPrizes.length > 0) {
                picturesContainers =
                        ExternalAnimationManager.getInstance().giveAllAnimationsFromStorageWithCategoriesProvided(selectedPrizes);
            }

            int pictureCategoriesAmount = picturesContainers.size();
            int pictureCategoriesIndexDrawn = new Random().nextInt(pictureCategoriesAmount);
            picturesContainer = picturesContainers.get(pictureCategoriesIndexDrawn);


            prepareLayout();
            return animation;

        }


    }

}
