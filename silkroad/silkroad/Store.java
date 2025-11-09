package silkroad;
import shapes.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

public class Store {
    protected Rectangle base;  
    protected int[] location;
    protected int index;
    protected int tengeInitial;
    protected int tenge;
    protected int timesEmpty = 0;
    protected String initialColor;
    
    public Store(int[] location, int tenges, int index) {
       this.base = new Rectangle(location[0], location[1]);
       this.location = location;
       this.index = index;
       this.tengeInitial = tenges;
       this.tenge = tenges;
       this.initialColor = this.base.getColor();
    }
    
    public int[] getLocation() {
        return this.location;
    }
    
    public int getTenge() {
        return this.tenge;
    }
    
    public void setTenge(int tenge){
        this.tenge = tenge;
        updateVisualState();
    }
    
    public void incrementTimesEmpty(){
        this.timesEmpty++;
    }
    
    public int getTimesEmpty(){
        return this.timesEmpty;
    }
    
    public int getInitialTenge(){
        return this.tengeInitial;
    }
    
    public void setInitialTenge(){
        this.tenge = this.tengeInitial;
        updateVisualState();
    }
    
    public int getIndex(){
        return this.index;
    }

    public void makeVisible(){
        if (base != null) {
            base.makeVisible();
        }
    }

    public void makeInvisible(){
        if (base != null) {
            base.makeInvisible();
        }
    }
    
    public void removeStore(){
        this.base.makeInvisible();
    }

    protected void updateVisualState() {
        if (base != null) {
            if (tenge <= 0) {
                base.changeColor("gray");
            } else {
                base.changeColor(initialColor);
            }
        }
    }
    
    public int attemptCollection(int robotCurrentTenge){
        if (this.tenge>0){
            int collected=this.tenge;
            this.tenge=0;
            this.incrementTimesEmpty();
            updateVisualState();
            return collected;
        }
        return 0;
    }
}
