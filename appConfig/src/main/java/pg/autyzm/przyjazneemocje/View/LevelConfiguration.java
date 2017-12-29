package pg.autyzm.przyjazneemocje.View;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;

import java.util.Locale;

import pg.autyzm.przyjazneemocje.R;
import pg.autyzm.przyjazneemocje.lib.SqlliteManager;

import static pg.autyzm.przyjazneemocje.lib.SqlliteManager.getInstance;

/**
 * Created by joagi on 26.12.2017.
 */

public class LevelConfiguration extends AppCompatActivity {

    private int emotionsNumber = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_view);

        createTabMaterial();
    }

    private void createTabMaterial() {
        createTabs();
        activeButtonsPlusMinus();
        createListOfSpinners();
    }

    private void createListOfSpinners() {
        final ListView selectedEmotionsList = (ListView) findViewById(R.id.selected_emotions_list);

        CustomAdapter adapter = new CustomAdapter();
        selectedEmotionsList.setAdapter(adapter);
    }

    private GridCheckboxImageBean[] getEmotionPhotos(String choosenEmotion){
        SqlliteManager sqlm = getInstance(this);
        Cursor cursor = sqlm.givePhotosWithEmotion(choosenEmotion);
        int n = cursor.getCount();
        GridCheckboxImageBean tabPhotos[] = new GridCheckboxImageBean[n];
        while (cursor.moveToNext()) {
            tabPhotos[--n] = (new GridCheckboxImageBean(cursor.getString(3),cursor.getInt(1), true, getContentResolver(), cursor.getInt(0)));
        }
        return tabPhotos;
    }

    private String getEmotionName(int emotionNumber) {
        Configuration config = new Configuration(getBaseContext().getResources().getConfiguration());
        config.setLocale(Locale.ENGLISH);
        return getBaseContext().createConfigurationContext(config).getResources().getStringArray(R.array.emotions_array)[emotionNumber];
    }

    private void updateEmotionsGrid(int emotionNumber) {
        String emotion =  getEmotionName(emotionNumber);
        GridCheckboxImageBean[] tabPhotos = getEmotionPhotos(emotion);

        final GridView listView = (GridView) findViewById(R.id.grid_photos);
        CheckboxImageAdapter adapter = new CheckboxImageAdapter(this, R.layout.grid_element_checkbox_image, tabPhotos);
        listView.setAdapter(adapter);
    }

    private void activeButtonsPlusMinus() {
        final EditText nrEmotions = (EditText) findViewById(R.id.nr_emotions);

        final Button minusButton = (Button) findViewById(R.id.button_minus);
        minusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                emotionsNumber = Integer.parseInt(nrEmotions.getText().toString());
                emotionsNumber--;
                nrEmotions.setText(Integer.toString(emotionsNumber));
            }
        });

        final Button plusButton = (Button) findViewById(R.id.button_plus);
        plusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                emotionsNumber = Integer.parseInt(nrEmotions.getText().toString());
                emotionsNumber++;
                nrEmotions.setText(Integer.toString(emotionsNumber));
            }
        });
    }

    private void createTabs() {
        TabHost tab = (TabHost) findViewById(R.id.tabHost);
        tab.setup();

        String[] tabsFiles = {"tab1_material", "tab2_learning_ways", "tab3_consolidation", "tab4_test", "tab_save"};
        TabHost.TabSpec spec;
        for (String tabFile : tabsFiles) {
            spec = tab.newTabSpec(tabFile);
            spec.setIndicator(getResourceString(tabFile));
            spec.setContent(getResourceId(tabFile));
            tab.addTab(spec);
        }
    }

    private int getResource(String variableName, String resourceName) {
        return getResources().getIdentifier(variableName, resourceName, getPackageName());
    }

    private int getResourceId(String resourceName) {
        return getResource(resourceName, "id");
    }

    private String getResourceString(String resourceName) {
        return getString(getResource(resourceName, "string"));
    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return emotionsNumber;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.row_spinner, parent, false);

            Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner_emotions);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(LevelConfiguration.this,
                    R.array.emotions_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            AdapterView.OnItemSelectedListener emotionSelectedListener = new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> spinner, View container,
                                           int position, long id) {
                    updateEmotionsGrid(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            };
            spinner.setOnItemSelectedListener(emotionSelectedListener);

            return convertView;
        }
    }
}
