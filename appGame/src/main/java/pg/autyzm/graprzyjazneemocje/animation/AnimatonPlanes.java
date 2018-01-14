package pg.autyzm.graprzyjazneemocje.animation;

import java.util.Random;

import pg.autyzm.graprzyjazneemocje.R;

/**
 * Created by gicze on 13.01.2018.
 */

public class AnimatonPlanes extends AnimationBase {

    static int numberAnimations = 2;

    int planesRightImages[] = {R.drawable.plane_a, R.drawable.plane_b, R.drawable.plane_c,  R.drawable.plane_d};

    @Override
    void createAnimation() {
        rand = new Random();
        switch (rand.nextInt(numberAnimations)) {
            case 0:
                goLeftToRight(context, planesRightImages);
                break;
            case 1:
                spiral(context, planesRightImages);
                break;
        }
    }

    @Override
    int getBackground(){
        return R.drawable.background_sky_gradient;
    }

}
