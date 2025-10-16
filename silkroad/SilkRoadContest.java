import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import javax.swing.JOptionPane;

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
    private SilkRoad simulator;
    //////////
    private HashMap<Integer, Integer> allStores;              
    private ArrayList<Integer> allRobots;                     
    private int currentDay;                                
    private int maxDailyUtility;                     
    private ArrayList<Integer> optimalRobotPositions;        
    private ArrayList<Integer> optimalDistances;            
    private int[][] days;                                  
    private int[][] posiciones;                  
    
    /**
     * Constructor de SilkRoadContest.
     * 
     * Inicializa el contestador con la configuración de los días.
     * Cada día representa una acción: agregar robot o tienda.
     * 
     * @param days Matriz int[][] donde cada fila representa un día:
     *Fila i: [1, x]agregar robot en posición x
     *Fila i: [2, x, c]agregar tienda en posición x con c tenges
     * 
     * @throws IllegalArgumentException si days es null o vacío
     */
    //public SilkRoadContest(int[][] days) {
    //    if (days == null || days.length == 0) {
    //        throw new IllegalArgumentException("El array de días no puede ser null o vacío");
    //    }
    //    
    //    this.days = days;
    //    this.currentDay = 0;
    //    this.maxDailyUtility = 0;
    //    this.optimalRobotPositions = new ArrayList<>();
    //    this.optimalDistances = new ArrayList<>();
    //    this.allStores = new HashMap<>();         
    //    this.allRobots = new ArrayList<>();   
    //    int maxPos = 0;
    //    for (int[] day : days) {
    //        if (day.length >= 2 && day[1] > maxPos) {
    //            maxPos = day[1];
    //        }
    //    }

    //    if (maxPos > 0) {
    //        this.posiciones = Posicion.generateSpiral(maxPos + 1);
    //    }
    //}
    
    /**
     * Constructor alternativo para un solo día.
     * 
     * @param dayInput Array con una sola acción del día
     */
    public SilkRoadContest(int[] dayInput) {
        if (dayInput == null || dayInput.length == 0) {
            throw new IllegalArgumentException("El input del día no puede ser null o vacío");
        }
        int[][] days = new int[1][];
        days[0] = dayInput;
        this.days = days;
        this.currentDay = 0;
        this.maxDailyUtility = 0;
        this.optimalRobotPositions = new ArrayList<>();
        this.optimalDistances = new ArrayList<>();
        this.allStores = new HashMap<>();
        this.allRobots = new ArrayList<>();
    }
    //METODO RESOLVE CICLO3
    /**
     * Requisito 14: Resuelve el problema de la maratón para todos los días.
     * Método principal que retorna la máxima ganancia para cada día.
     * Para cada día:
     * 1. Se agrega el nuevo robot o tienda (acumulativo)
     * 2. Se calcula la máxima ganancia considerando costos de movimiento
     * 3. Se retorna la ganancia máxima
     * @param days Matriz int[][] donde cada fila es una acción del día:
     *[1, x] agregar robot en posición x
     *[2, x, c] agregar tienda en posición x con c tenges
     * @return Array int[] donde result[i] = máxima utilidad acumulada del día i
     */
    public int[] solve(int[][] days) {
        if (days == null || days.length == 0) {
            return new int[0];
        }
        this.days = days;
        this.allStores.clear();
        this.allRobots.clear();
        this.optimalRobotPositions.clear();
        this.optimalDistances.clear();
        int[] results = new int[days.length];
        for (int i = 0; i < days.length; i++) {
            results[i] = solveDay(i);
        }
        
        return results;
    }
    
    /**
     * Constructor que recibe los días en formato de matriz
     * @param days Cada fila: [tipo, posición, (tenges si tipo=2)]
     */
    public SilkRoadContest(int[][] days) {
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
            System.out.println(" DÍA " + (i + 1) );
            
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
            String linea = String.format("Día %2d: %5d tenges \n", 
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
     * Método auxiliar para pausar la simulación.
     */
    private void esperar(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Resuelve el problema para todos los días de forma acumulativa (sin parámetros).
     * Utiliza los días ya configurados en el constructor.
     * @return Array int[] donde result[i] = máxima utilidad del día i
     */
    public int[] solveAll() {
        return solve(this.days);
    }
    
    /**
     * Resuelve un día específico de forma acumulativa.
     * @param dayIndex Índice del día (0-based)
     * @return Máxima utilidad para ese día
     */
    public int solveDay(int dayIndex) {
        if (dayIndex < 0 || dayIndex >= days.length) {
            return 0;
        }
        this.currentDay = dayIndex;
        int[] dayAction = days[dayIndex];
        if (dayAction.length < 2) {
            throw new IllegalArgumentException("Acción del día inválida");
        }
        int actionType = dayAction[0];
        if (actionType == 1) {
            int robotPos = dayAction[1];
            if (!allRobots.contains(robotPos)) {
                allRobots.add(robotPos);
            }
        } else if (actionType == 2) {
            if (dayAction.length < 3) {
                throw new IllegalArgumentException("Tienda debe tener posición y cantidad de tenges");
            }
            int storePos = dayAction[1];
            int tenges = dayAction[2];
            allStores.put(storePos, tenges);
        }
        optimalRobotPositions.clear();
        optimalDistances.clear();
        this.maxDailyUtility = moveRobots();
        
        return maxDailyUtility;
    }
    
    /**
     * Mueve todos los robots de forma inteligente para maximizar la ganancia total.
     * Requisito 11: Los robots deciden automáticamente sus movimientos buscando
     * @return Ganancia total máxima obtenida por todos los robots
     */
    private int moveRobots() {
        if (allRobots.isEmpty() || allStores.isEmpty()) {
            return 0;
        }
        int totalProfit = 0;
        HashMap<Integer, Integer> availableStores = new HashMap<>(allStores);
        for (Integer robotPos : allRobots) {
            int bestStore = -1;
            int maxTenges = 0;
            for (Integer storePos : availableStores.keySet()) {
                int storeMoney = availableStores.get(storePos);
                if (storeMoney > maxTenges) {
                    maxTenges = storeMoney;
                    bestStore = storePos;
                }
            }
            if (bestStore != -1 && maxTenges > 0) {
                int distance = Math.abs(bestStore - robotPos);
                int netGain = maxTenges - distance;
                optimalRobotPositions.add(robotPos);
                optimalDistances.add(bestStore - robotPos);
                totalProfit += netGain;
                availableStores.put(bestStore, 0);
            }
        }
        
        return totalProfit;
    }
}