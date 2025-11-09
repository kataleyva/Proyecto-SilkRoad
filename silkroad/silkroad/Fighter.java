package silkroad;
import shapes.Rectangle;

/**
 * FighterStore class that can only be looted by robots with more money than the store.
 * 
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza
 * @version 25.10.2025
 */
public class Fighter extends Store {
    private int tengesStoreF;
    
    public Fighter(int[] location, int tenges, int index) {
        super(location, tenges, index);
        this.tengesStoreF = tenges; // Solo robots con más tenges pueden saquear
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
        return robotTenge > this.tengesStoreF;
    }
    
    /**
     * Intenta recolectar tenges de esta tienda fighter
     * Solo permite la recolección si el robot tiene más tenges que la tienda
     * @param robotCurrentTenge los tenges actuales del robot que intenta saquear
     * @return los tenges que realmente fueron recolectados (0 si no cumple el requisito)
     */
    public int attemptCollection(int robotCurrentTenge) {
        if (this.tenge > 0 && canBeLootedBy(robotCurrentTenge)) {
            int collected = this.tenge;
            this.tenge = 0;
            this.incrementTimesEmpty();
            updateVisualState();
            return collected;
        } else if (this.tenge > 0) {
            return 0;
        }
        return 0;
    }
    
    /**
     * Override del método setTenge para prevenir cambios no autorizados
     * Pero mantiene funcionalidad para reabastecimiento
     */
    @Override
    public void setTenge(int newTenge) {
        // Solo permite cambios si es reabastecimiento (nuevo valor = valor inicial)
        // o si está siendo vaciada por un robot autorizado (ya manejado en attemptCollection)
        if (newTenge == this.tengeInitial || newTenge == 0) {
            super.setTenge(newTenge);
        }
        // Si alguien intenta establecer un valor diferente, la tienda fighter no permite el saqueo
        else if (newTenge != this.tenge) {;
        }
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
    

}