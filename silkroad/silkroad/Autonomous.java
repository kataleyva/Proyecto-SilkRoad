package silkroad;

import shapes.Rectangle;
import java.util.Random;

/**
 * AutonomousStore class that chooses its own position instead of using the given one.
 * 
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza
 * @version 25.10.2025
 */
public class Autonomous extends Store {
    private static final Random random = new Random();
    private int actualLocation;
    
    public Autonomous(int[] suggestedLocation, int tenges, int index, int maxRoadLenght) {
        super(chooseRandomPosition(suggestedLocation), tenges, index);
        this.actualLocation=index;
        this.base.changeColor(247, 172, 42);
    }
    /**
     * Choose a random position close to the suggested position
     * @param Posicion sugerida por el usuario
     * @return Arreglo con la nueva posicion cercana a la sugerida
     */
    private static int [] chooseRandomPosition(int[] suggestedLocation){
        int offsetX = random.nextInt(121) - 60; 
        int offsetY = random.nextInt(121) - 60;
        int newX = Math.max(50, Math.min(suggestedLocation[0] + offsetX, 550));
        int newY = Math.max(50, Math.min(suggestedLocation[1] + offsetY, 550));
        return new int[]{newX, newY};  
    }

    @Override
    public int attemptCollection(int robotCurrentTenge) {
        int collected = super.attemptCollection(robotCurrentTenge);
        if (collected > 0) {
            System.out.println("AutonomousStore en posición única [" + this.location[0] + "," + this.location[1] + "] fue saqueada");
        }
        return collected;
    }
    
    @Override
    public void makeVisible() {
        super.makeVisible();
    }
}
