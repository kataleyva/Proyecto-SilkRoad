package shapes;

import java.awt.*;

/**
 * A rectangle that can be manipulated and that draws itself on a canvas.
 *
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 1.0  (15 July 2000)()
 */



public class Rectangle extends Figure{

    public static int EDGES = 4;

    private int height;
    private int width;

    /**
     * Create a new rectangle at default position with default color.
     */
    public Rectangle(){
        height = 30;
        width = 30;
        xPosition = 70;
        yPosition = 15;
        this.colour = new Colour();
        this.color = colour.chooseColorExcluding("black");
        isVisible = false;
    }

    public Rectangle(int xPosition, int yPosition){
        height = 30;
        width = 30;
        this.xPosition = xPosition + 300;
        this.yPosition = yPosition + 300;
        this.colour = new Colour();
        this.color = colour.chooseColorExcluding("black");
        isVisible = false;
    }    
    
    public Rectangle(int xPosition, int yPosition, String color){
        height = 30;
        width = 30;
        this.xPosition = xPosition + 300;
        this.yPosition = yPosition + 300;
        this.colour = new Colour();
        this.color = color;
        isVisible = false;
    }    

    /**
     * Change the size to the new size
     * @param newFeatures the new size in pixels. newFeatures must be >=0.
     */
    @Override
    public void changeSize(int newFeatures) {
        erase();
        height = newFeatures;
        width = newFeatures;
        draw();
    }
    
    /**
     * Change the size to the new size
     * @param newHeight the new height in pixels. newHeight must be >=0.
     * @param newWidht the new width in pixels. newWidth must be >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
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
    
    public void changeColor(int r, int g, int b){
        draw();
        this.color = colour.changeColor(r, g, b);
        erase();
    }

    /*
     * Draw the rectangle with current specifications on screen.
     */
    @Override
    public void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                    new java.awt.Rectangle(xPosition, yPosition,
                            width, height));
            canvas.wait(10);
        }
    }

    /*
     * Erase the rectangle on screen.
     */
    public void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}



