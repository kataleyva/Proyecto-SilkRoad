package silkroad;

/**
 * Write a description of class SilkRoad here.
 *
 * @author Maria Katalina Leyva Diaz - Michelle Dayanna Ruiz Carranza
 * @version 7.09.2025
 */

import java.lang.Math;
import java.util.HashMap;

public class SilkRoad{
    private static HashMap<Integer, Store> stores;
    private static HashMap <Integer, Store> initialStores;
    private static HashMap <Integer, Robot> robots;
    private static HashMap <Integer, Robot> initialRobots;
    private static int idRobot = 0;
    private final int lenRoad;
    private int[][] posicion;

    public SilkRoad(int lengthRoad) {
        this.lenRoad = lengthRoad;
        this.stores = new HashMap<>();
        this.initialStores = new HashMap<>();
        this.robots = new HashMap<>();
        this.initialRobots = new HashMap<>();
        this.posicion = Posicion.generateSpiral(lenRoad);
    
        System.out.println("Se creó la ruta de seda de longitud "+lenRoad);
        return;
    }
    

    public void placeStore(int location, int tenges){
        Store store = new Store(this.posicion[location], tenges);
        stores.put(location, store);
        initialStores.put(location, store);
        return;
    }

    public void removeStore(int location){
        Store store = stores.get(location);
        store.removeStore();
        stores.remove(location);
        return;
    }

    public void placeRobot(int location){
        idRobot ++;
        Robot robot = new Robot(idRobot, posicion[location], location);
        System.out.println(robot.getId());
        robots.put(robot.getId(), robot);
        initialRobots.put(robot.getId(), robot);
        System.out.println(location);
        return;
    }
    
    public void removeRobot(int location){
        for(Robot robot : robots.values()){
            robot.removeRobot();
            int id = robot.getId();
            this.robots.remove(id);
        }
    }
    
    public void ressuplyStores(){
        for (var i : stores.entrySet()){
            if (i.getValue().getTenge() == 0){
                int location = i.getKey();
                Store initialTenge = initialStores.get(location);
                i.setValue(initialTenge);
            }
        }
      return;
    };

    public void returnRobots(){
        for (var i : robots.entrySet()){
            int robotId = i.getKey();
            Robot initialLocation = initialRobots.get(robotId);
            i.setValue(initialLocation);
        }
        return;
    }
}
