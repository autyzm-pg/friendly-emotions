package pg.autyzm.graprzyjazneemocje.animation;

import java.util.Random;

import pg.autyzm.graprzyjazneemocje.R;

/**
 * Created by joagi on 13.01.2018.
 */

public class AnimationButterflies extends AnimationBase {

    static int numberAnimations = 2;

    int butterfliesUpImages[] = {R.drawable.butterfly_green, R.drawable.butterfly_red, R.drawable.butterfly_blue, R.drawable.butterfly_color};


    void createAnimation() {
        Random rand = new Random();
        switch (rand.nextInt(numberAnimations)) {
            case 0:
                straightFlyUp(context, butterfliesUpImages);
                break;
            case 1:
                spiral(context, butterfliesUpImages);
                break;
        }
    }

}
