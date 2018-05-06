package pg.autyzm.graprzyjazneemocje.api.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "pictures")
public class Picture {

    @DatabaseField(generatedId=true)
    private Integer id;

    @DatabaseField
    private String name;

    @DatabaseField(unique = true)
    private String path;

    @DatabaseField
    private int enabled = 1;

    @DatabaseField(foreign = true, foreignAutoRefresh= true)
    private PicturesContainer picturesContainer;

    public Picture(){}

    public Picture(String name, String path, PicturesContainer picturesContainer){
        this.setPath(path);
        this.setName(name);
        this.picturesContainer = picturesContainer;
    }



    public String getName(){
        return name;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public PicturesContainer getPicturesContainer() {
        return picturesContainer;
    }

    public void setPicturesContainer(PicturesContainer picturesContainer) {
        this.picturesContainer = picturesContainer;
    }
}
