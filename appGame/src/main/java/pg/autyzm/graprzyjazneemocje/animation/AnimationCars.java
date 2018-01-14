package pg.autyzm.graprzyjazneemocje.animation;

import java.util.Random;

import pg.autyzm.graprzyjazneemocje.R;

/**
 * Created by joagi on 13.01.2018.
 */

public class AnimationCars extends AnimationBase {

    static int numberAnimations = 5;

    int carsRightImages[] = {R.drawable.car_red, R.drawable.monster_truck, R.drawable.race_car};
    int carsOverhandRightImages[] = {R.drawable.car_red_overhand, R.drawable.car_purple_overhand, R.drawable.car_turquoise_overhand, R.drawable.car_green_overhand, R.drawable.car_orange_overhand};


    void createAnimation() {
        Random rand = new Random();
        switch (rand.nextInt(numberAnimations)) {
            case 0:
                spiral(context, carsRightImages);
                break;
            case 1:
                goLeftToRight(context, carsOverhandRightImages);
                break;
            case 2:
                goLeftToRight(context, carsRightImages);
                break;
            case 3:
                spiral(context, carsOverhandRightImages);
                break;
            case 4:
                straightFlyUp(context, carsOverhandRightImages);
                break;
        }
    }

    @Override
    int getBackground(){
        return R.drawable.background_car_gradient;
    }


}
