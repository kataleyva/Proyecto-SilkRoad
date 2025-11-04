package shapes;

import java.awt.geom.*;

/**
 * Una línea que puede ser manipulada y dibujada en el canvas.
 */
public class Line extends Figure {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1 + 300;
        this.y1 = y1 + 300;
        this.x2 = x2 + 300;
        this.y2 = y2 + 300;
        this.color = "black";
        this.isVisible = false;
    }

    @Override
    public void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, new Line2D.Double(x1, y1, x2, y2));
            canvas.wait(10);
        }
    }

    @Override
    public void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }

    /**
     * Cambia el tamaño de la línea según un nuevo largo (aproximado).
     * Mantiene el punto inicial fijo y escala el punto final.
     */
    @Override
    public void changeSize(int newLength) {
        erase();
        double angle = Math.atan2(y2 - y1, x2 - x1);
        x2 = x1 + (int) (newLength * Math.cos(angle));
        y2 = y1 + (int) (newLength * Math.sin(angle));
        draw();
    }
}
