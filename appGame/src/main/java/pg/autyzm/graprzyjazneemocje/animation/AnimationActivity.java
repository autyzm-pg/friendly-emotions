package pg.autyzm.graprzyjazneemocje.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import java.util.Random;


/**
 * Created by joagi on 13.01.2018.
 */

public class AnimationActivity extends Activity implements Animation.AnimationListener {

    protected Animation anim;

    AnimationBase animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
        anim.setAnimationListener(this);
    }

    private AnimationBase randomAward() {
        AnimationBase animation;
        Random random = new Random();
        switch (random.nextInt(6)) {
            case 0:
                animation = new AnimationBalls();
                break;
            case 1:
                animation = new AnimationButterflies();
                break;
            case 2:
                animation = new AnimationCars();
                break;
            case 3:
                animation = new AnimatonShips();
                break;
            case 4:
                animation = new AnimatonTrains();
                break;
            default:
                animation = new AnimatonPlanes();
                break;
        }
        animation.setActivity(this);
        return animation;
    }

    private void createView() {
        animationView = randomAward();
        RelativeLayout layout = animationView.getLayout(getApplicationContext());
        anim = animationView.getAnim();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
