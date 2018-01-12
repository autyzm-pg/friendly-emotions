package pg.autyzm.przyjazneemocje.View;

import android.content.ContentResolver;

/**
 * Created by joagi on 02.12.2017.
 */

public class GridCheckboxImageBean {


    String photoName;
    boolean selected;
    ContentResolver cr;
    int id;


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public GridCheckboxImageBean() {

    }

    public GridCheckboxImageBean(String photoName, int icon, boolean selected, ContentResolver cr, int id) {
        this.photoName = photoName;
        this.selected = selected;
        this.cr = cr;
        this.id = id;
    }
}