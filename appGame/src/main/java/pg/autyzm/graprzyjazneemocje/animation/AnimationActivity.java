package pg.autyzm.graprzyjazneemocje.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;

import pg.autyzm.graprzyjazneemocje.api.managers.AnimationBuilder;


/**
 * Created by joagi on 13.01.2018.
 */

public class AnimationActivity extends Activity implements Animation.AnimationListener {

    protected Animation anim;
    private String[] selectedPrizes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
        anim.setAnimationListener(this);
    }

    private void createView() {

        Bundle b = getIntent().getExtras();
        String selectedPrizesString = b.getString("prizes");
        selectedPrizes = selectedPrizesString.split(";");

        anim = new AnimationBuilder(this).prepareAndReturnRandomAward(selectedPrizes);

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
