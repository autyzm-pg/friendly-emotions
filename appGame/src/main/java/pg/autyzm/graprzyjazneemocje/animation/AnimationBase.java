package pg.autyzm.graprzyjazneemocje.animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.List;
import java.util.Random;

import pg.autyzm.graprzyjazneemocje.R;
import pg.autyzm.graprzyjazneemocje.api.entities.Picture;
import pg.autyzm.graprzyjazneemocje.api.entities.PicturesContainer;

/**
 * Created by joagi on 13.01.2018.
 */

public class AnimationBase {

    RelativeLayout layout;
    Context context;
    Random rand = new Random();
    Animation anim;


    Activity activity;
    PicturesContainer picturesContainer;


    public AnimationBase(PicturesContainer picturesContainer){
        this.picturesContainer = picturesContainer;
    }


    public Animation getAnim() {
        return anim;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void createAnimation(List<Picture> pictureList, AnimationType[] animationTypes){


        Random rand = new Random();
        AnimationType animationType = animationTypes[rand.nextInt(animationTypes.length)];

        switch (animationType) {
            case STRAIGHT_FLY_UP_DOWN:
                straightFlyUpDown(context, pictureList);
                break;
            case GO_LEFT_TO_RIGHT:
                goLeftToRight(context, pictureList);
                break;
            case SPIRAL:
                spiral(context, pictureList);
                break;
        }
    }


    public RelativeLayout getLayout(Context context) {
        this.context=context;

        if(picturesContainer.getCategoryName().equals("planes") || picturesContainer.getCategoryName().equals("ships")){
            createAnimation(picturesContainer.getPicturesInCategory(),
                    new AnimationType[]{AnimationType.SPIRAL, AnimationType.GO_LEFT_TO_RIGHT});
        }
        else if(picturesContainer.getCategoryName().equals("trains")){
            createAnimation(picturesContainer.getPicturesInCategory(), new AnimationType[]{AnimationType.GO_LEFT_TO_RIGHT});
        }
        else{
            createAnimation(picturesContainer.getPicturesInCategory(), AnimationType.values());
        }

        return layout;
    }

    void goLeftToRight(Context context, List<Picture> images) {

        ImageView animImage;

        activity.setContentView(R.layout.activity_anim_right_left);

        int imagesNr[] = {R.id.image1, R.id.image2, R.id.image3};
        for (int image : imagesNr) {

            animImage = (ImageView) activity.findViewById(image);

            Picture drawnPicture = images.get(rand.nextInt(images.size()));
            loadPictureToAnimation(animImage, drawnPicture);

            anim = AnimationUtils.loadAnimation(context, R.anim.right);
            anim.setStartOffset(rand.nextInt(1500));
            anim.setDuration(rand.nextInt(1000) + 1500);
            animImage.startAnimation(anim);
        }
    }

    private void loadPictureToAnimation(ImageView imageView, Picture picture){

        File imgFile = new  File(picture.getPath());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
    }

    void straightFlyUpDown(Context context, List<Picture> images) {
        ImageView animImage;

        activity.setContentView(R.layout.activity_anim_straight);

        int imagesNr[] = {R.id.image1, R.id.image2, R.id.image3, R.id.image4};
        int n = 0;
        for (int image : imagesNr) {

            animImage = (ImageView) activity.findViewById(image);

            Picture drawnPicture = images.get(rand.nextInt(images.size()));
            loadPictureToAnimation(animImage, drawnPicture);

            if((n++)%2==0) {
                animImage.setRotation(270);
                anim = AnimationUtils.loadAnimation(context, R.anim.up);
            } else {
                animImage.setRotation(90);
                anim = AnimationUtils.loadAnimation(context, R.anim.down);
            }
            animImage.startAnimation(anim);
        }
    }

    void spiral(Context context, List<Picture> images) {
        ImageView animImage;

        activity.setContentView(R.layout.activity_anim_spiral);
        animImage = (ImageView) activity.findViewById(R.id.image1);
        Picture drawnPicture = images.get(rand.nextInt(images.size()));
        loadPictureToAnimation(animImage, drawnPicture);

        anim = AnimationUtils.loadAnimation(context, R.anim.spiral);
        animImage.startAnimation(anim);

        animImage = (ImageView) activity.findViewById(R.id.image2);
        drawnPicture = images.get(rand.nextInt(images.size()));
        loadPictureToAnimation(animImage, drawnPicture);

        anim = AnimationUtils.loadAnimation(context, R.anim.spirall);
        animImage.startAnimation(anim);

    }

    private ImageView initImage(int imageNumber) {
        ImageView image;

        image = new ImageView(context);
        layout.addView(image);
        image.setImageResource(imageNumber);
        return image;
    }

    int getBackground() {
        return R.drawable.background_main_gradient;
    }
}
