package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Joanna on 2016-11-03.
 */


public class AnimationActivity extends Activity implements Animation.AnimationListener {

    protected ImageView animImage;
    protected Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        randomAward();

        anim.setAnimationListener(this);
        animImage.startAnimation(anim);

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }


    private void awardSpiral() {
        setContentView(R.layout.activity_anim_spiral);
//        animImage = (ImageView) findViewById(R.id.butterfly_image);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.spiral);
        animImage.startAnimation(anim);
        animImage = (ImageView) findViewById(R.id.image2);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.spirall);
    }

    private void awardStraight() {
        setContentView(R.layout.activity_anim_straight);

//        int butterflyimages[] = {R.id.butterfly_image, R.id.butterfly2_image, R.id.butterfly3_image, R.id.butterfly4_image};
        int n = 0;
//        for (int image : butterflyimages) {
//
//            animImage = (ImageView) findViewById(image);
//            if((n++)%2==0) {
//                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
//            } else {
//                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down);
//            }
//            animImage.startAnimation(anim);
//        }
    }

    private void awardUp() {
        setContentView(R.layout.activity_anim_straight);
//        animImage = (ImageView) findViewById(R.id.butterfly_image);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
        animImage.startAnimation(anim);

        animImage = (ImageView) findViewById(R.id.image2);
        animImage.setImageResource(R.drawable.butterfly_red_up);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
        anim.setStartOffset(500);
        animImage.startAnimation(anim);

        animImage = (ImageView) findViewById(R.id.image3);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
        anim.setStartOffset(1000);
        animImage.startAnimation(anim);

        animImage = (ImageView) findViewById(R.id.image4);
        animImage.setImageResource(R.drawable.butterfly_green_up);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
        anim.setStartOffset(1500);

    }

    protected void randomAward() {
        Random rand = new Random();
        int k = rand.nextInt(3);
        switch (k) {
            case 0:
                awardSpiral();
                break;
            case 1:
                awardStraight();
                break;
            case 2:
                awardUp();
                break;
            default:
                awardSpiral();
                break;
        }
    }

}
