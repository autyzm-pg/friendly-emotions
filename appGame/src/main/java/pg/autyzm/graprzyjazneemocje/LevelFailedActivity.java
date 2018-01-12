package pg.autyzm.graprzyjazneemocje;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class LevelFailedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_failed);
    }


    public void repeatLevel(View view){

        finish();

    }




}
