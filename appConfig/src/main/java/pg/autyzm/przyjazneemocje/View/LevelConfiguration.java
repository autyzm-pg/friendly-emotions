package pg.autyzm.przyjazneemocje.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import pg.autyzm.przyjazneemocje.R;

/**
 * Created by joagi on 26.12.2017.
 */

public class LevelConfiguration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_view);

        createTabs();
    }

    private void createTabs(){
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
}
