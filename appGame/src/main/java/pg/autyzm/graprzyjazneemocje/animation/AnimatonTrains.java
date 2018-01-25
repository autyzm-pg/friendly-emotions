package pg.autyzm.graprzyjazneemocje.animation;

import java.util.Random;

import pg.autyzm.graprzyjazneemocje.R;

/**
 * Created by gicze on 13.01.2018.
 */

public class AnimatonTrains extends AnimationBase {

    static int numberAnimations = 1;

    int shipsRightImages[] = {R.drawable.train_a, R.drawable.train_b, R.drawable.train_c, R.drawable.train_d};

    @Override
    void createAnimation() {
        goLeftToRight(context, shipsRightImages);
    }


}
