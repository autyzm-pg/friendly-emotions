package pg.autyzm.graprzyjazneemocje.api.entities;

import android.hardware.Camera;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbirg on 19.03.2018.
 */

public class PicturesContainer {

    public PicturesContainer(String categoryName){
        this.categoryName = categoryName;
    }



    private String categoryName;
    private List<Picture> picturesInCategory = new ArrayList<>();

    public String getCategoryName() {
        return categoryName;
    }


    public List<Picture> getPicturesInCategory() {
        return picturesInCategory;
    }


    public void addPicture(Picture picture){
        picturesInCategory.add(picture);
    }


}
