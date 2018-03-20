package pg.autyzm.graprzyjazneemocje.api.entities;


public class Picture {

    private String name;
    private String path;


    public Picture(String name, String path){
        this.path = path;
        this.name = name;
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
}
