package silkroad;
import shapes.Rectangle;

/**
 * BonusStore class that gives extra tenges when looted.
 * When a robot collects from this store, it receives 50% extra tenges.
 * 
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza
 * @version 25.10.2025
 */
public class Bonus extends Store {
    
    public Bonus(int[] location, int tenges, int index) {
        super(location, tenges, index);
        this.base.changeColor(245, 113, 201);
    }

    @Override
    public int attemptCollection(int robotCurrentTenge) {
        if (this.tenge > 0) {
            int bonusTenges = (int)(this.tenge * 1.5); // 50% extra
            this.tenge = 0;
            this.incrementTimesEmpty();
            updateVisualState();
            return bonusTenges;
        }
        return 0;
    }

    @Override
    public void makeVisible() {
        super.makeVisible();
    }
}
