package pg.autyzm.graprzyjazneemocje.animation;

import android.app.Activity;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import pg.autyzm.graprzyjazneemocje.R;

/**
 * Created by joagi on 13.01.2018.
 */

abstract public class AnimationBase {

    RelativeLayout layout;
    Context context;
    Random rand = new Random();
    Animation anim;


    Activity activity;

    public Animation getAnim() {
        return anim;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    abstract void createAnimation();


    public RelativeLayout getLayout(Context context) {
        this.context=context;
        createAnimation();
        return layout;
    }

    void goLeftToRight(Context context, int images[]) {

        ImageView animImage;

        activity.setContentView(R.layout.activity_anim_right_left);

        int imagesNr[] = {R.id.image1, R.id.image2, R.id.image3};
        for (int image : imagesNr) {

            animImage = (ImageView) activity.findViewById(image);
            animImage.setImageResource(images[rand.nextInt(images.length)]);
            anim = AnimationUtils.loadAnimation(context, R.anim.right);
            anim.setStartOffset(rand.nextInt(1500));
            anim.setDuration(rand.nextInt(1000) + 1500);
            animImage.startAnimation(anim);
        }
    }

    void straightFlyUpDown(Context context, int images[]) {
        ImageView animImage;

        activity.setContentView(R.layout.activity_anim_straight);

        int imagesNr[] = {R.id.image1, R.id.image2, R.id.image3, R.id.image4};
        int n = 0;
        for (int image : imagesNr) {

            animImage = (ImageView) activity.findViewById(image);
            animImage.setImageResource(images[rand.nextInt(images.length)]);
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

    void spiral(Context context, int images[]) {
        ImageView animImage;

        activity.setContentView(R.layout.activity_anim_spiral);

        animImage = (ImageView) activity.findViewById(R.id.image1);
        animImage.setImageResource(images[rand.nextInt(images.length)]);
        anim = AnimationUtils.loadAnimation(context, R.anim.spiral);
        animImage.startAnimation(anim);

        animImage = (ImageView) activity.findViewById(R.id.image2);
        animImage.setImageResource(images[rand.nextInt(images.length)]);
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
