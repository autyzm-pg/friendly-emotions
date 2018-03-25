package pg.autyzm.graprzyjazneemocje.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pg.autyzm.graprzyjazneemocje.api.entities.Picture;
import pg.autyzm.graprzyjazneemocje.api.entities.PicturesContainer;
import pg.autyzm.graprzyjazneemocje.api.managers.ExternalAnimationManager;


/**
 * Created by joagi on 13.01.2018.
 */

public class AnimationActivity extends Activity implements Animation.AnimationListener {

    protected Animation anim;
    private String[] selectedPrizes;

    AnimationBase animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        String selectedPrizesString = b.getString("prizes");
        selectedPrizes = selectedPrizesString.split(";");

        createView();
        anim.setAnimationListener(this);
    }

    private AnimationBase randomAward() {

        AnimationBase animation;
        List<PicturesContainer> picturesContainers = ExternalAnimationManager.getInstance().getPicturesFromExternalStorage();

        if(picturesContainers.isEmpty()){
            // use internal storage, if there's nothing in the external one
            animation = null;
            throw new RuntimeException("This path is still not implemented");

        }else {

            if (selectedPrizes.length > 0)
                filterOutCategoriesNotSelectedDuringConfiguration(picturesContainers);

            int pictureCategoriesAmount = picturesContainers.size();
            int pictureCategoriesIndexDrawn = new Random().nextInt(pictureCategoriesAmount);
            animation = new AnimationBase(picturesContainers.get(pictureCategoriesIndexDrawn));
            animation.setActivity(this);

        }

        return animation;

    }

    private void filterOutCategoriesNotSelectedDuringConfiguration(List<PicturesContainer> picturesContainers) {

        Iterator<PicturesContainer> picturesContainerIterator = picturesContainers.iterator();

        label:
        while (picturesContainerIterator.hasNext()) {

            PicturesContainer picturesContainer = picturesContainerIterator.next();

            for (String category : selectedPrizes) {
                if (picturesContainer.getCategoryName().equals(category)) {
                    continue label;
                }
            }

            picturesContainerIterator.remove();
        }

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
