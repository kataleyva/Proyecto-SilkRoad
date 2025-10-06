import java.util.*;
import java.util.ArrayList;

public class Robot {
    private Circle robot;
    private int index;
    private int[] location;
    private int tenge;
    private static int[] initialLocation;
    private static ArrayList<Integer> movements;

    public Robot(int[] location, int index){
        this.robot = new Circle(location[0], location[1]);
        this.location = location;
        this.tenge = 0;
        this.index = index;
        this.robot.makeVisible();
        this.initialLocation = location;
    }

    public int[] getLocation() {
        return this.location;
    }

    public void addProfitsInMovements(int profit){
        this.movements.add(profit);
    }
    
    public ArrayList<Integer> getMovements(){
        return this.movements;
    }
    
    public int getTenge() {
        return this.tenge;
    }

    public int[] getInitialLocation(){
        return this.initialLocation;
    }
    
    public void resetRobotLocation(){
        this.location = initialLocation;
    }
    
    public int getIndex(){
        return this.index;
    }

    public void setTenge(int tenge){
        this.tenge = tenge;
    }
    
    public void removeRobot(){
        this.robot.makeInvisible();
    }

    public void setIndexLocation(int index) {
        this.index = index;
    }

    public void setLocation(int[] location) {
        removeRobot();
        this.location = location;
    }

    public void makeVisible() {
        if (robot != null) {
            robot.makeVisible();
        }
    }

    public void makeInvisible() {
        if (robot != null) {
            robot.makeInvisible();
        }
    }
    
}
