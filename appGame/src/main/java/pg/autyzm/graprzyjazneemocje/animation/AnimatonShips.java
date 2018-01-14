package pg.autyzm.graprzyjazneemocje.animation;

import java.util.Random;

import pg.autyzm.graprzyjazneemocje.R;

/**
 * Created by gicze on 13.01.2018.
 */

public class AnimatonShips extends AnimationBase {

    static int numberAnimations = 2;

    int shipsRightImages[] = {R.drawable.ship_pirate, R.drawable.container_ship, R.drawable.soldek, R.drawable.boat, R.drawable.boat_small, R.drawable.ship_color};

    @Override
    void createAnimation() {
        rand = new Random();
        switch (rand.nextInt(numberAnimations)) {
            case 0:
                goLeftToRight(context, shipsRightImages);
                break;
            case 1:
                spiral(context, shipsRightImages);
                break;
        }
    }

    @Override
    int getBackground() {
        return R.drawable.background_sky_gradient;
    }

}
