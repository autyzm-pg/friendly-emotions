package pg.autyzm.przyjazneemocje.View;

/**
 * Created by joagi on 30.12.2017.
 */

public class CheckboxGridBean {

    private String name;
    private boolean checked;

    public CheckboxGridBean(String name, boolean checked) {
        this.setName(name);
        this.setChecked(checked);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
