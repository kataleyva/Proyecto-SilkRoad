package shapes;

/**
 * Clase para representar una barra de progreso visual en posición fija
 * 
 * @author Maria Katalina Leyva Díaz y Michelle Dayana Ruíz Carranza
 * @version 25.10.2025
 */
public class ProgressBar extends Figure {
    private Rectangle background;
    private Rectangle progress;
    private Rectangle border;
    private int maxValue;
    private int currentValue;
    private int width;
    private int height;
    private static final int FIXED_X = 720 - 300;
    private static final int FIXED_Y = 100;
    
    public ProgressBar(int width, int height, int maxValue) {
        this.xPosition = FIXED_X;
        this.yPosition = FIXED_Y;
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;
        this.currentValue = 0;
        this.isVisible = false;
        // Crear borde de la barra(negro)
        this.border = new Rectangle(FIXED_X - 5, FIXED_Y - 5);
        this.border.changeSize(width + 10, height + 10 );
        this.border.changeColor("black");
        // Crear fondo de la barra (gris)
        this.background = new Rectangle(FIXED_X, FIXED_Y);
        this.background.changeSize(width, height );
        this.background.changeColor("blue");
        
        // Crear barra de progreso (verde)
        this.progress = new Rectangle(FIXED_X, FIXED_Y);
        this.progress.changeSize(10, height ); // Inicia con ancho 0
        this.progress.changeColor("green");
    }
    
    /**
     * Actualiza el valor actual de la barra de progreso
     * @param newValue nuevo valor a mostrar
     */
    public void updateValue(int newValue) {
        this.currentValue = Math.min(Math.max(0, newValue), maxValue);
        updateProgressWidth();
        if (isVisible){
            erase();
            draw();
        }
    }
    
    /**
     * Establece el valor máximo de la barra
     * @param maxValue valor máximo
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = Math.max(1, maxValue); 
        updateProgressWidth();
    }
    
    /**
     * Calcula y actualiza el ancho de la barra de progreso
     */
    private void updateProgressWidth() {
        if (maxValue > 0) {
            int progressWidth = (int)((currentValue * width) / maxValue);
            progressWidth = Math.max(0, Math.min(width, progressWidth));
            
            if (progress != null) {
                progress.changeSize(progressWidth, height );
            }
        }
    }
    
    /**
     * Cambia el color de la barra de progreso
     * @param color nuevo color (ej: "green", "blue", "red")
     */
    public void changeProgressColor(String color) {
        if (progress != null) {
            progress.changeColor(color);
        }
    }
    
    /**
     * Obtiene el valor actual mostrado en la barra
     * @return valor actual
     */
    public int getCurrentValue() {
        return currentValue;
    }
    
    /**
     * Obtiene el valor máximo de la barra
     * @return valor máximo
     */
    public int getMaxValue() {
        return maxValue;
    }
    
    @Override
    public void draw() {
        if (isVisible) {
            if (border !=null) border.makeVisible();
            if(background != null) background.makeVisible();
            if (progress != null) progress.makeVisible();
        }
    }
    
    @Override
    public void erase() {
        if (border !=null) border.makeInvisible();
        if (background != null) background.makeInvisible();
        if (progress != null) progress.makeInvisible();
    }
    
    @Override
    public void changeSize(int newSize) {
    }
    
    @Override
    public void makeVisible() {
        isVisible = true;
        //if (border != null){
           // border.makeVisible();
       // }
        //if (background != null){
          // background.makeVisible(); 
        //}
        //if (progress != null){
          //  progress.makeVisible();
        //}
        draw();
    }
    
    @Override
    public void makeInvisible() {
        isVisible = false;
        erase();
    }
}