package pg.autyzm.graprzyjazneemocje.api.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by pbirg on 19.03.2018.
 */

@DatabaseTable(tableName = "pictures_containers")
public class PicturesContainer {

    @DatabaseField(generatedId=true)
    private Integer id;



    public PicturesContainer(String categoryName){
        this.setCategoryName(categoryName);
    }

    public PicturesContainer(){}


    @DatabaseField(unique = true)
    private String categoryName;

    @DatabaseField
    private Integer enabled = 1;

    @DatabaseField
    private String animationTypes = "LEFT_TO_RIGHT;SPIRAL;UP_DOWN";

    @ForeignCollectionField(eager = true)
    private Collection<Picture> picturesInCategory = new ArrayList<>();

    public String getCategoryName() {
        return categoryName;
    }


    public Collection<Picture> getPicturesInCategory() {

        //return new ArrayList<Picture>(picturesInCategory);
        return picturesInCategory;

    }


    public void addPicture(Picture picture){
        getPicturesInCategory().add(picture);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getAnimationTypes() {
        return animationTypes;
    }

    public void setAnimationTypes(String animationTypes) {
        this.animationTypes = animationTypes;
    }
}
