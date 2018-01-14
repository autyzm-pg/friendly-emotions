package pg.autyzm.graprzyjazneemocje;

import android.view.animation.AnimationUtils;
import android.widget.ImageView;

//import AnimationActivity;

/**
 * Created by Joanna on 2016-12-01.
 */

public class AnimationEndActivity extends AnimationActivity {

    @Override
    protected void randomAward() {
        setContentView(R.layout.activity_anim_end);

        int offset = -300;
        int butterflyimages[] = {R.id.butterfly1_image, R.id.image2, R.id.image3, R.id.image4, R.id.butterfly5_image};

        for (int image : butterflyimages) {
            animImage = (ImageView) findViewById(image);
            anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.spiralend);
            anim.setStartOffset(offset += 300);
            animImage.startAnimation(anim);
        }


        animImage = (ImageView) findViewById(R.id.sun_image);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sun);
        animImage.startAnimation(anim);

    }
}
