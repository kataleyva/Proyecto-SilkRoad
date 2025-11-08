package shapes;

import java.awt.*;
import java.awt.geom.*;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0.  (15 July 2000)
 */

public class Circle extends Figure{

    public static final double PI=3.1416;
    private int diameter;

    public Circle(int diameter){
        this.diameter = diameter;
        xPosition = 70;
        yPosition = 15;
        this.colour = new Colour();
        this.color = colour.chooseColor();
        isVisible = false;
    }

    public Circle(int xPosition, int yPosition){
        this.diameter = 25;
        this.xPosition = xPosition + 300;
        this.yPosition = yPosition + 300;
        this.colour = new Colour();
        this.color = colour.chooseColor();
        isVisible = false;
    }    
    
    public Circle(int xPosition, int yPosition, String color){
        this.diameter = 25;
        this.xPosition = xPosition + 300;
        this.yPosition = yPosition + 300;
        this.colour = new Colour();
        this.color = color;
        isVisible = false;
    }    

    @Override
    public void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                    new Ellipse2D.Double(xPosition, yPosition,
                            diameter, diameter));
            canvas.wait(10);
        }
    }

    public void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }

    public void changeColor(int r, int g, int b){
        draw();
        this.color = colour.changeColor(r, g, b);
        erase();
    }
    
    /**
     * Change the size.
     * @param newDiameter the new size (in pixels). Size must be >=0.
     */
    @Override
    public void changeSize(int newFeatures){
        erase();
        diameter = newFeatures;
        draw();
    }
}

