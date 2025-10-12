import java.awt.*;
import java.util.*;

public class SpiralSquares {
    
    public static void drawSpiral(int cantidad) {
        Canvas canvas = Canvas.getCanvas();
        int[][] posiciones = Posicion.generateSpiral(cantidad);
        
        int lado = 20;  // tamaño de cada cuadrado
        int centroX = 300;
        int centroY = 300;
        
        for (int i = 0; i < posiciones.length; i++) {
            int x = centroX + posiciones[i][0];
            int y = centroY - posiciones[i][1];

            // Definir un cuadrado como polígono
            int[] xPoints = {x, x + lado, x + lado, x};
            int[] yPoints = {y, y, y + lado, y + lado};
            Polygon cuadrado = new Polygon(xPoints, yPoints, 4);

            // Dibujar en canvas
            canvas.draw(cuadrado, "black", cuadrado);
            
            // Pequeña pausa para ver cómo se forma
            canvas.wait(50);
        }
    }

    public static void main(String[] args) {
        drawSpiral(50);
    }
}
