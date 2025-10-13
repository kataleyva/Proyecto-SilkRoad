import javax.swing.JOptionPane;
import java.util.*;

/**
 * Clase que resuelve y visualiza el problema de la maratón ICPC 2024 - Problem J
 * "The Silk Road... with Robots!"
 */
public class SilkRoadContest2 {
    private SilkRoad simulator;
    
    /**
     * Constructor que recibe los días en formato de matriz
     * @param days Cada fila: [tipo, posición, (tenges si tipo=2)]
     */
    public SilkRoadContest2(int[][] days) {
        if (days == null || days.length == 0) {
            throw new IllegalArgumentException("No hay datos de días para simular.");
        }
        
        // Calcular longitud máxima
        int maxPosition = 0;
        for (int[] day : days) {
            if (day.length >= 2) {
                maxPosition = Math.max(maxPosition, day[1]);
            }
        }
        
        this.simulator = new SilkRoad(maxPosition + 1);
    }
    
    /**
     * Simula el problema de la maratón día por día con visualización
     * CORREGIDO: Funciona acumulativamente según las reglas del problema ICPC
     * 
     * @param days Matriz donde cada fila es [tipo, posición, (tenges)]
     * @param slow Si true, hace pausas para visualización
     */
    public void simulate(int[][] days, boolean slow) {
        if (days == null || days.length == 0) {
            System.out.println("No hay datos para simular.");
            return;
        }
        
        simulator.makeVisible();
        
        int[] ganancias = new int[days.length];
        
        for (int i = 0; i < days.length; i++) {
            System.out.println("┌─── DÍA " + (i + 1) + " ───────────────────────────────────┐");
            
            // PASO 1: Reabastecer y retornar (excepto día 1)
            if (i > 0) {
                simulator.resupplyStores();
                simulator.returnRobots();
                resetRobotProfits();
                System.out.println("Tiendas reabastecidas");
                System.out.println("Robots retornados");
                System.out.println("Ganancias reseteadas");
            }
            
            if (slow) simulator.esperar(800);
            
            // PASO 2: Agregar nuevo elemento del día
            int[] day = days[i];
            int tipo = day[0];
            int posicion = day[1];
            
            if (tipo == 1) {
                simulator.placeRobot(posicion);
                System.out.println("ROBOT en posición " + posicion);
            } else if (tipo == 2) {
                int tenges = day[2];
                simulator.placeStore(posicion, tenges);
                System.out.println("TIENDA en pos " + posicion + " con " + tenges + " tenges");
            }
            
            if (slow) simulator.esperar(1000);
            
            // PASO 3: Mostrar estado
            System.out.println("");
            mostrarEstadoActual();
            
            if (slow) simulator.esperar(1000);
            
            // PASO 4: Mover robots
            System.out.println("Moviendo robots...");
            simulator.moveRobots();
            
            if (slow) simulator.esperar(1500);
            
            // PASO 5: Calcular ganancia
            int ganancia = simulator.profit();
            ganancias[i] = ganancia;
            
            System.out.println("GANANCIA DEL DÍA: " + ganancia + " tenges");
            
            if (slow) simulator.esperar(2000);
        }
        
        // Mostrar resumen
        mostrarResumen(ganancias);
    }
    
    /**
     * Resetea las ganancias de todos los robots a 0
     * (simula el inicio de un nuevo día)
     */
    private void resetRobotProfits() {
        int[][] robotsInfo = simulator.robots();
        for (int i = 0; i < robotsInfo.length; i++) {
            Robot robot = simulator.getRobot(i);
            if (robot != null) {
                robot.resetProfits();
            }
        }
    }
    
    /**
     * Muestra el estado actual de tiendas y robots
     */
    private void mostrarEstadoActual() {
        int[][] tiendas = simulator.stores();
        int[][] robots = simulator.robots();
        
        System.out.println("Tiendas (" + tiendas.length + "):");
        for (int i = 0; i < tiendas.length; i++) {
            System.out.println("Pos " + tiendas[i][0] + ": " + tiendas[i][1] + " tenges");
        }
        
        System.out.println("Robots (" + robots.length + "):");
        for (int i = 0; i < robots.length; i++) {
            System.out.println("Pos " + robots[i][0] + ": " + robots[i][1] + " tenges");
        }
    }
    
    /**
     * Muestra el resumen final de ganancias
     */
    private void mostrarResumen(int[] ganancias) {
        StringBuilder resumen = new StringBuilder();
        
        for (int i = 0; i < ganancias.length; i++) {
            String linea = String.format("Día %2d: %5d tenges /n", 
                                        (i + 1), ganancias[i]);
            resumen.append(linea);
        }
        
        System.out.println("\n" + resumen.toString());
        
        JOptionPane.showMessageDialog(
            null,
            resumen.toString(),
            "Resultados - Silk Road Contest",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Ejemplo con el Sample Input 1 del problema ICPC
     */
    public static void main(String[] args) {
        // Sample Input 1:
        // Expected Output: 0, 10, 35, 50, 50, 60
        
        int[][] sampleInput1 = {
            {1, 20},        // Día 1: Robot en 20
            {2, 15, 15},    // Día 2: Tienda en 15 con 15 tenges
            {2, 40, 50},    // Día 3: Tienda en 40 con 50 tenges
            {1, 50},        // Día 4: Robot en 50
            {2, 80, 20},    // Día 5: Tienda en 80 con 20 tenges
            {2, 70, 30}     // Día 6: Tienda en 70 con 30 tenges
        };
        
        System.out.println("  Expected Output: 0, 10, 35, 50, 50, 60");

        
        SilkRoadContest2 contest = new SilkRoadContest2(sampleInput1);
        contest.simulate(sampleInput1, true);
    }
}