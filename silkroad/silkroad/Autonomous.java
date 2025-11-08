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
    
    public Autonomous(int[] location, int tenges, int index, int maxRoadLength) {
        super(chooseRandomPosition(location, maxRoadLength), tenges, index);
        // Cambiar color para distinguir visualmente
        this.base.changeColor("orange");
        this.initialColor = "orange";
    }
    
    /**
     * Elige una posición aleatoria cerca de la posición sugerida
     */
    private static int[] chooseRandomPosition(int[] suggestedLocation, int maxRoadLength) {
        int offsetX = random.nextInt(61) - 30; // -30 a +30
        int offsetY = random.nextInt(61) - 30; // -30 a +30
        
        int newX = Math.max(0, Math.min(suggestedLocation[0] + offsetX, maxRoadLength * 80 - 30));
        int newY = Math.max(0, Math.min(suggestedLocation[1] + offsetY, maxRoadLength * 80 - 30));
        
        return new int[]{newX, newY};
    }
    
    /**
     * Override para mostrar comportamiento diferente
     */
    @Override
    public void makeVisible() {
        super.makeVisible();
        // Comportamiento visual adicional para tienda autónoma
        if (base != null) {
            base.changeColor("orange");
        }
    }
}