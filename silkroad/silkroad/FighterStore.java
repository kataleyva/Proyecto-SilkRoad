package silkroad;

import shapes.Rectangle;

/**
 * FighterStore class that can only be looted by robots with more money than the store.
 * 
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza
 * @version 25.10.2025
 */
public class FighterStore extends Store {
    private int defenseThreshold;
    
    public FighterStore(int[] location, int tenges, int index) {
        super(location, tenges, index);
        this.defenseThreshold = tenges; // Solo robots con más tenges pueden saquear
        // Cambiar color para distinguir visualmente
        this.base.changeColor("yellow");
        this.initialColor = "yellow";
    }
    
    /**
     * Verifica si un robot puede saquear esta tienda
     * @param robotTenge cantidad de tenges que tiene el robot
     * @return true si el robot puede saquear, false si no cumple el requisito
     */
    public boolean canBeLootedBy(int robotTenge) {
        return robotTenge > this.defenseThreshold;
    }
    
    /**
     * Override del método setTenge para incluir lógica de defensa
     */
    @Override
    public void setTenge(int tenge) {
        // Las tiendas fighter no pueden ser saqueadas fácilmente
        // La lógica de saqueo se maneja en SilkRoad.moveRobot()
        super.setTenge(tenge);
    }
    
    /**
     * Override para mostrar comportamiento visual diferente
     */
    @Override
    public void makeVisible() {
        super.makeVisible();
        if (base != null) {
            base.changeColor("yellow");
        }
    }
    
    public int getDefenseThreshold() {
        return this.defenseThreshold;
    }
}