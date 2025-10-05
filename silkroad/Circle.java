import java.awt.*;
import java.awt.geom.*;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0.  (15 July 2000)
 */

public class Circle{

    public static final double PI=3.1416;
    private int diameter;
    private int xPosition;
    private int yPosition;
    private String color;
    private Colour colour;
    private boolean isVisible;
    private String[] colors;


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
        isVisible = true;
    }    
    
    public void makeVisible(){
        isVisible = true;
        draw();
    }

    public void makeInvisible(){
        erase();
        isVisible = false;
    }

    private void draw(){
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

    /**
     * Move the circle a few pixels to the right.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Move the circle a few pixels to the left.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Move the circle a few pixels up.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Move the circle a few pixels down.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Move the circle horizontally.
     * @param distance the desired distance in pixels
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Move the circle vertically.
     * @param distance the desired distance in pixels
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Get x position
     */
    public int getXPosition(){
        return this.xPosition;
    }

    /**
     * Get y position
     */
    public int getYPosition(){
        return this.yPosition;
    }

    /**
     * Slowly move the circle horizontally.
     * @param distance the desired distance in pixels
     */
    public void slowMoveHorizontal(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * Slowly move the circle vertically
     * @param distance the desired distance in pixels
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        }else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Change the size.
     * @param newDiameter the new size (in pixels). Size must be >=0.
     */
    public void changeSize(int newDiameter){
        erase();
        diameter = newDiameter;
        draw();
    }

    /**
     * Change the color.
     * @param color the new color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta" and "black".
     */
    public void changeColor(String newColor){
        this.color = newColor;
        draw();
    }
    
    public void changeColor(){
        this.color = colour.chooseColor();
    }



}
