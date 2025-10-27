import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import javax.swing.JOptionPane;
import java.util.Arrays;

/**
 * Clase que resuelve el problema de optimización de la Ruta de la Seda
 * de la maratón
 * 
 *LOGICA DE LA MARATON
 * - Cada movimiento cuesta 1 tenge por metro
 * - Ganancia neta = tenges tienda - distancia
 * - Las tiendas y robots se acumulan día a día
 * - Cada tienda solo se vacía una vez por día
 * - Antes de cada día: reabastecimiento y reset de posiciones
 */
public class SilkRoadContest {
    private static SilkRoad simulator;
    private HashMap<Integer, Integer> allStores;              
    private ArrayList<Integer> allRobots;                     
    private int currentDay;                                
    private int maxDailyUtility;                     
    private ArrayList<Integer> optimalRobotPositions;        
    private ArrayList<Integer> optimalDistances;            
    private int[][] days;                                  
    private int[][] posiciones;   
    
    //METODO SOLVE CICLO3
    /**
     * Requisito 14: Resuelve el problema de la maratón para todos los días.
     * Usa el método moveRobots() de SilkRoad para calcular la ganancia óptima.
     * @param days Matriz int[][] donde cada fila es una acción del día:
     *             [1, x] → agregar robot en posición x
     *             [2, x, c] → agregar tienda en posición x con c tenges
     * 
     * @return Array int[] donde result[i] = máxima ganancia acumulada del día i
     */
    public static int[] solve(int[][] days) {
        if (days == null || days.length == 0) {
            return new int[0];
        }
        int maxPos = calculateMaxPosition(days);
        SilkRoad simulator = new SilkRoad(maxPos + 1);
        ArrayList<Integer> robots = new ArrayList<>();
        HashMap<Integer, Integer> stores = new HashMap<>();
        int[] results = new int[days.length];
    
        for (int i = 0; i < days.length; i++) {
            int[] day = days[i];
            if (day.length < 2) {
                continue;
            }
            int tipo = day[0];
            int position = day[1];
            if (tipo == 1) {
                robots.add(position);
            } else if (tipo == 2 && day.length >= 3) {
                int tenges = day[2];
                stores.put(position, tenges);
            }
            results[i] = calculateProfitWithSimulator(robots, stores);
        }
        ShowResults(results);
        return results;
    }
    
    /**
     * Calcula la posición máxima en los días
     */
    private static int calculateMaxPosition(int[][] days) {
        int maxPos = 0;
        for (int[] day : days) {
            if (day.length >= 2) {
                maxPos = Math.max(maxPos, day[1]);
            }
        }
        return maxPos;
    }
    
    /**
     * Calcula la máxima ganancia usando un simulador temporal de SilkRoad.
     * IMPORTANTE: Este simulador es temporal y NO se visualiza
     */
    private static int calculateProfitWithSimulator(ArrayList<Integer> robots, 
                                             HashMap<Integer, Integer> stores) {
        if (robots.isEmpty() || stores.isEmpty()) {
            return 0;
        }
        int maxPos = 0;
        for (Integer robotPos : robots) {
            maxPos = Math.max(maxPos, robotPos);
        }
        for (Integer storePos : stores.keySet()) {
            maxPos = Math.max(maxPos, storePos);
        }
        SilkRoad simulator = new SilkRoad(maxPos + 1);
        for (Integer robotPos : robots) {
            simulator.placeRobot(robotPos);
        }
        for (Integer storePos : stores.keySet()) {
            simulator.placeStore(storePos, stores.get(storePos));
        }
        simulator.moveRobots();
        int profit = simulator.profit();
        return profit;
    }
    
    /**
     * Muestra el resumen final de ganancias
     */
    private static void ShowResults(int[] gains) {
        StringBuilder resumn = new StringBuilder();
        
        for (int i = 0; i < gains.length; i++) {
            String line = String.format("Día %2d: %5d tenges \n", 
                                        (i + 1), gains[i]);
            resumn.append(line);
        }
        System.out.println("\n" + resumn.toString());
        JOptionPane.showMessageDialog(
            null,
            resumn.toString(),
            "Resultados - Silk Road Contest",
            JOptionPane.INFORMATION_MESSAGE
        );
    }  
    
    /**
     * Simula el problema de la maratón día por día con visualización
     * Funciona acumulativamente según las reglas del problema ICPC
     * 
     * @param days Matriz donde cada fila es [tipo, posición, (tenges)]
     * @param slow Si true, hace pausas para visualización
     */
    public static void simulate(int[][] days, boolean slow) {
        if (days == null || days.length == 0) {
            showMessage("No hay datos para simular");
            return;
        }
        simulator.makeVisible();
        int[] ganancias = new int[days.length];
        for (int i = 0; i < days.length; i++) {
            showMessage(" DÍA " + (i + 1) );
            // PASO 1: Reabastecer y retornar (excepto día 1)
            if (i > 0) {
                simulator.resupplyStores();
                simulator.returnRobots();
                resetRobotProfits();
                showMessage("Juego y Ganancias reiniciadas.");
            }
            if (slow) simulator.pause(800);
            // PASO 2: Agregar nuevo elemento del día
            int[] day = days[i];
            int tipo = day[0];
            int posicion = day[1];
            if (tipo == 1) {
                simulator.placeRobot(posicion);
               showMessage("ROBOT en posición " + posicion);
            } else if (tipo == 2) {
                int tenges = day[2];
                simulator.placeStore(posicion, tenges);
                showMessage("TIENDA en pos " + posicion + " con " + tenges + " tenges");
            }
            if (slow) simulator.pause(1000);
            // PASO 3: Mostrar estado
            showCurrentStatus();
            if (slow) simulator.pause(1000);
            // PASO 4: Mover robots
            showMessage("Moviendo robots...");
            simulator.moveRobots();
            if (slow) simulator.pause(1500);
            // PASO 5: Calcular ganancia
            int ganancia = simulator.profit();
            ganancias[i] = ganancia;
            if (slow) simulator.pause(2000);
        }
    }
    
    /**
     * Resetea las ganancias de todos los robots a 0
     * (simula el inicio de un nuevo día)
     */
    private static void resetRobotProfits() {
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
    private static void showCurrentStatus() {
        int[][] tiendas = simulator.stores();
        int[][] robots = simulator.robots();
        
        showMessage("Tiendas (" + tiendas.length + "):");
        for (int i = 0; i < tiendas.length; i++) {
            showMessage("Pos " + tiendas[i][0] + ": " + tiendas[i][1] + " tenges");
        }
        
        System.out.println("Robots (" + robots.length + "):");
        for (int i = 0; i < robots.length; i++) {
            showMessage("Pos " + robots[i][0] + ": " + robots[i][1] + " tenges");
        }
    }
    
    /**
     * Método auxiliar para pausar la simulación.
     */
    private static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
        /**
     * Muestra un mensaje al usuario utilizando un popup (JOptionPane).
     * Los mensajes solo se muestran si el simulador está en modo visible.
     * Método auxiliar privado para comunicación con el usuario.
     * 
     * @param message Mensaje a mostrar al usuario.
     */
    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Silk Road Simulator", 
                                        JOptionPane.INFORMATION_MESSAGE);
    }
    
    //Pruebas de aceptación 
    public void testAceptacion0() {
        simulate(new int[][] {{1, 20}, {2, 15, 15}, {2, 40, 50}, {1, 50}, {2, 80, 20}, {2, 70, 30} }, true);
    }
}