package pg.autyzm.graprzyjazneemocje.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Random;

import pg.autyzm.graprzyjazneemocje.api.entities.PicturesContainer;
import pg.autyzm.graprzyjazneemocje.api.managers.ExternalAnimationManager;


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

        List<PicturesContainer> picturesContainers = ExternalAnimationManager.getInstance().getPicturesFromExternalStorage();
        int pictureCategoriesAmount = picturesContainers.size();
        int pictureCategoriesIndexDrawn = new Random().nextInt(pictureCategoriesAmount);
        AnimationBase animation = new AnimationBase(picturesContainers.get(pictureCategoriesIndexDrawn));
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
