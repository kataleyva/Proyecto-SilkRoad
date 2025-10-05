package silkroad;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.HashMap;

public class Robot {
    private Circle robot;
    private int loc;
    private static int id;
    private int[] location;
    private int tenge;

    public Robot(int id, int[] location, int loc){
        this.robot = new Circle(location[0],location[1]);
        this.id = id;
        this.location = location;
        this.tenge = 0;
        this.loc = loc;
        this.robot.makeVisible();
    }

    public int getId() {
        return id;
    }

    public int[] getLocation() {
        return this.location;
    }

    public int getTenge() {
        return this.tenge;
    }

    public int getLoc(){
        return this.loc;
    }
    
    public void removeRobot(){
        this.robot.makeInvisible();
    }
}
