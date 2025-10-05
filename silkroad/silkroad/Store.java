package silkroad;

import java.util.ArrayList;
import java.util.HashMap;


public class Store {
    private Rectangle base;
    private int[] location;
    private int tengeInitial;
    private int tenge;

    public Store(int[] location, int tenges) {
       this.base = new Rectangle(location[0], location[1]);
       this.location = location;
       this.tengeInitial = tenges;
       this.tenge = tenges;
       this.base.makeVisible();
    }

    public int[] getLocation() {
        return this.location;
    }

    public int getTenge() {
        return this.tenge;
    }

    public void removeStore(){
        this.base.makeInvisible();
    }
}
