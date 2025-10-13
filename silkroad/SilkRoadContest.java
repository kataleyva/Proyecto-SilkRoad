import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * Constructor de la clase SilkRoadContest.
     * Inicializa el simulador con la longitud suficiente
     * para cubrir todas las posiciones de los días dados.
     *
     * @param days Matriz con la configuración de cada día.
     */
    public SilkRoadContest(int[][] days) {
        if (days == null || days.length == 0) {
            throw new IllegalArgumentException("No hay datos de días para simular.");
        }
        // El constructor de SilkRoad con days calcula la longitud automáticamente
        this.simulator = new SilkRoad(days);
    }

    /**
     * Simula la ejecución del problema de la maratón de programación.
     * Usa los métodos de SilkRoad para crear las configuraciones diarias,
     * mover los robots automáticamente, calcular las ganancias y reiniciar
     * entre días.
     *
     * @param days Matriz donde cada fila representa un día:
     *             [n, s1, t1, s2, t2, ..., sn, tn, r1, r2, ...]
     */
    public void simulate(int[][] days) {
        if (days == null || days.length == 0) {
            System.out.println("No hay datos para simular.");
            return;
        }
            
        System.out.println("=== INICIO DE SIMULACIÓN MARATÓN ===");
        simulator.makeVisible();
        esperar(1000);
    
        for (int d = 0; d < days.length; d++) {
            System.out.println("\n--- Día " + (d + 1) + " ---");
    
            // Día 1 ya fue creado automáticamente por el constructor de SilkRoad(days)
            if (d > 0) {
                // Reiniciar: reabastecer tiendas y devolver robots al inicio
                simulator.reboot();
                // Agregar las nuevas tiendas y robots de este día
            simulator.create(days[d]);
            } else {
            // Solo reiniciar al principio del día 1
                simulator.reboot();
            }
    
            esperar(1000);
    
            // Mostrar tiendas y robots del día actual
            mostrarConfiguracion(simulator);
        
            // Mover robots automáticamente
            System.out.println("Moviendo robots automáticamente...");
            simulator.moveRobots();
            esperar(1500);
    
            // Calcular ganancia del día
            int ganancia = simulator.profit();
            System.out.println("Ganancia total del día " + (d + 1) + ": " + ganancia + " tenges");
    
            // Mostrar tiendas vaciadas
            int[][] vacias = simulator.emptiedStores();
            for (int i = 0; i < vacias.length; i++) {
                System.out.println("  Tienda en pos " + vacias[i][0] + " vaciada " + vacias[i][1] + " vez(es)");
            }
        }


        simulator.makeInvisible();
        System.out.println("\n=== FIN DE SIMULACIÓN ===");
    }

    /**
     * Muestra las tiendas y robots actuales del simulador.
     */
    private void mostrarConfiguracion(SilkRoad sim) {
        System.out.println("Tiendas:");
        int[][] tiendas = sim.stores();
        for (int i = 0; i < tiendas.length; i++) {
            System.out.println("  → Posición " + tiendas[i][0] + " con " + tiendas[i][1] + " tenges");
        }

        System.out.println("Robots:");
        int[][] robs = sim.robots();
        for (int i = 0; i < robs.length; i++) {
            System.out.println("  → Robot en posición " + robs[i][0]);
        }
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