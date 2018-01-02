package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ann on 12.11.2016.
 */
public class EndActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Bundle extras = getIntent().getExtras();
        boolean pass = extras.getBoolean("PASS");

        if (pass) {
            passLevel();
        } else {
            failLevel();
        }

//        txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, 100);

        int wrongAnswers = extras.getInt("WRONG");
        int rightAnswers = extras.getInt("RIGHT");
        int timeout = extras.getInt("TIMEOUT");
        TextView right = (TextView) findViewById(R.id.rightAnswers);
        right.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
        right.setText(getResources().getString(R.string.label_rightAnswers) + " " + rightAnswers);
        TextView wrong = (TextView) findViewById(R.id.wrongAnswers);
        wrong.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
        wrong.setText(getResources().getString(R.string.label_wrongAnswers) + " " + wrongAnswers);

        TextView time = (TextView) findViewById(R.id.timeout);
        time.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
        time.setText(getResources().getString(R.string.label_timeout) + " " + timeout);

    }

    private void passLevel() {
        Intent i = new Intent(this, AnimationEndActivity.class);
        startActivity(i);

        TextView txt = (TextView) findViewById(R.id.endTextMain);
        txt.setText(getResources().getString(R.string.label_congratulations));
        ImageButton prevButton = (ImageButton) findViewById(R.id.prevButton);
        prevButton.setVisibility(View.INVISIBLE);

    }

    private void failLevel(){
        ImageView sunImage = (ImageView) findViewById(R.id.sun_image_end);
        sunImage.setVisibility(View.INVISIBLE);

        TextView txt = (TextView) findViewById(R.id.endTextMain);
        txt.setText(getResources().getString(R.string.failed_level));
    }

    public void repeatLevel(View view){

        finish();

    }

}
