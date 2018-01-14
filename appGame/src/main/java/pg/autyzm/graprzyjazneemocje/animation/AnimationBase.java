package pg.autyzm.graprzyjazneemocje.animation;

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

    public Animation getAnim() {
        return anim;
    }

    abstract void createAnimation();

    public RelativeLayout getLayout(Context context) {
        this.context=context;
        layout = new RelativeLayout(context);
        createAnimation();
        layout.setBackgroundResource(getBackground());
        return layout;
    }

    void goLeftToRight(Context context, int images[]) {
        ImageView image;

        for (int i = 0; i < 3; i++) {
            image = initImage(images[rand.nextInt(images.length)]);

            image.setY(160 * i + 15);
            anim = AnimationUtils.loadAnimation(context, R.anim.right);
            anim.setStartOffset(rand.nextInt(1500));
            anim.setDuration(rand.nextInt(1000)+1500);
            image.startAnimation(anim);
        }
    }

    void straightFlyUp(Context context, int images[]) {
        ImageView image;

        for (int i = 0; i < 4; i++) {

            image = initImage(images[rand.nextInt(images.length)]);
            image.setRotation(270);
            image.setY(650);
            image.setX(i*230);

            anim = AnimationUtils.loadAnimation(context, R.anim.up);
            anim.setStartOffset(rand.nextInt(1500));
            anim.setDuration(rand.nextInt(1000)+1500);
            image.startAnimation(anim);
        }
    }

    void spiral(Context context, int images_up[]) {
        ImageView image;

        image = initImage(images_up[rand.nextInt(images_up.length)]);
        image.setRotation(270);
        image.setY(270);
        image.setX(630);
        image.setAdjustViewBounds(true);
        anim = AnimationUtils.loadAnimation(context, R.anim.spiral);
        image.startAnimation(anim);

        image = initImage(images_up[rand.nextInt(images_up.length)]);
        image.setRotation(270);
        image.setY(270);
        image.setX(0);
        anim = AnimationUtils.loadAnimation(context, R.anim.spirall);
        image.startAnimation(anim);
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
