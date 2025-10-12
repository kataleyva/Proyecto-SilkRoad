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
    public SilkRoadContest(int[][] days) {
        if (days == null || days.length == 0) {
            throw new IllegalArgumentException("El array de días no puede ser null o vacío");
        }
        
        this.days = days;
        this.currentDay = 0;
        this.maxDailyUtility = 0;
        this.optimalRobotPositions = new ArrayList<>();
        this.optimalDistances = new ArrayList<>();
        this.allStores = new HashMap<>();         
        this.allRobots = new ArrayList<>();   
        int maxPos = 0;
        for (int[] day : days) {
            if (day.length >= 2 && day[1] > maxPos) {
                maxPos = day[1];
            }
        }

        if (maxPos > 0) {
            this.posiciones = Posicion.generateSpiral(maxPos + 1);
        }
    }
    
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