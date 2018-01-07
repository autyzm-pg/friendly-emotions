package pg.autyzm.graprzyjazneemocje;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Ann on 29.12.2017.
 */

public class VideoWelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_welcome);

        TextView txt = (TextView) findViewById(R.id.question);
        final String commandText = "Jakie jest dziecko"; //getResources().getString(R.string.command_label); TODO: Change to generic
        txt.setText(commandText);
        final Speaker speaker = Speaker.getInstance(VideoWelcomeActivity.this);

        ImageButton speakerButton = (ImageButton) findViewById(R.id.speakerButton);
        speakerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                speaker.speak(commandText);
            }
        });
    }

    public void goToGame(View view){
        finish();
    }
}
