import java.util.*;

public class Robot {
    private Circle robot;
    private int index;
    private int[] location;
    private int tenge;
    private int initialLocation[];
    private int initialIndex; 
    private ArrayList<Integer> profitsInMovements;
    
    public Robot(int[] location, int index){
        this.robot = new Circle(location[0], location[1]);
        this.location = new int[]{location[0], location[1]};
        this.tenge = 0;
        this.index = index;
        this.initialIndex = index;
        this.initialLocation = new int[]{location[0], location[1]};
        this.profitsInMovements = new ArrayList<>();
    }
    
    public int[] getLocation() {
        return this.location;
    }
    
    public int getTenge() {
        return this.tenge;
    }
    
    public int[] getInitialLocation(){
        return this.initialLocation;
    }
    
    public void setTenge(int tenge) {
        this.tenge = tenge;
    }
    
    public ArrayList<Integer> getMovements() {
        return this.profitsInMovements;
    }
    
    public String getColor(){
        return this.robot.getColor();
    }
    
    public void addProfitsInMovements(int profit) {
        this.profitsInMovements.add(profit);
    }
    
    public void resetRobotLocation(){
        this.location = new int[]{initialLocation[0], initialLocation[1]};
        this.index = initialIndex; 
        if (this.robot != null) {
            boolean wasVisible = this.robot.isVisible(); // Guardar estado
            this.robot.makeInvisible();
            String color = this.robot.getColor();
            this.robot = new Circle(this.location[0], this.location[1], color);
            if (wasVisible) {  // Solo hacer visible si estaba visible antes
                this.robot.makeVisible();
            }
        }
    }
    
    public void resetProfits() {
        this.tenge = 0;
        this.profitsInMovements.clear();
    }
    
    public int getIndex(){
        return this.index;
    }
    
    public void removeRobot(){
        if (this.robot != null) {
            this.robot.makeInvisible();
        }
    }
    
    public void setIndexLocation(int index) {
        this.index = index;
    }
    
    public void setLocation(int[] location) {
    this.location = new int[]{location[0], location[1]};
    if (this.robot != null) {
        boolean wasVisible = this.robot.isVisible(); // Guardar estado
        this.robot.makeInvisible();
        String color = this.robot.getColor();
        this.robot = new Circle(location[0], location[1], color);
        if (wasVisible) {  // Solo hacer visible si estaba visible antes
            this.robot.makeVisible();
        }
    }
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