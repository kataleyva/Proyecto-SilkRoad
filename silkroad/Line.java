import java.awt.*;
import java.awt.geom.*;

/**
 * Una línea que puede ser manipulada y dibujada en el canvas.
 * Conecta dos puntos en el espacio del canvas.
 */
public class Line {
    
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private String color;
    private boolean isVisible;

    /**
     * Crea una línea desde (x1, y1) hasta (x2, y2)
     * Las coordenadas se ajustan automáticamente al centro del canvas
     */
    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1 + 300;
        this.y1 = y1 + 300;
        this.x2 = x2 + 300;
        this.y2 = y2 + 300;
        this.color = "black";
        this.isVisible = false;
    }

    /**
     * Hace visible la línea en el canvas
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Hace invisible la línea en el canvas
     */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /**
     * Cambia el color de la línea
     * @param newColor el nuevo color ("red", "blue", "green", "black", etc.)
     */
    public void changeColor(String newColor) {
        this.color = newColor;
        if (isVisible) {
            draw();
        }
    }

    /**
     * Dibuja la línea en el canvas
     */
    private void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, new Line2D.Double(x1, y1, x2, y2));
            canvas.wait(10);
        }
    }

    /**
     * Borra la línea del canvas
     */
    public void erase() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}