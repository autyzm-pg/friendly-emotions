package pg.autyzm.graprzyjazneemocje.animation;

import java.util.Random;

import pg.autyzm.graprzyjazneemocje.R;

/**
 * Created by joagi on 13.01.2018.
 */

public class AnimationBalls extends AnimationBase {

    static int numberAnimations = 3;

    int balls[] = {R.drawable.basketball, R.drawable.beach_ball, R.drawable.tennis_ball, R.drawable.soccer_ball, R.drawable.rugby_ball, R.drawable.bowling_ball};

    void createAnimation() {
        Random rand = new Random();
        switch (rand.nextInt(numberAnimations)) {
            case 0:
                straightFlyUpDown(context, balls);
                break;
            case 1:
                goLeftToRight(context, balls);
                break;
            case 2:
                spiral(context, balls);
                break;
        }
    }
}
