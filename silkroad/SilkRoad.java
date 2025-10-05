import java.util.HashMap;
import java.util.ArrayList;
import java.util.*;

/**
 * Clase que representa el simulador de la Ruta de la Seda.
 * 
 * Esta clase permite administrar tiendas (Stores) y robots (Robots)
 * que interactúan en una ruta representada en espiral. 
 * Se pueden colocar y remover tiendas y robots, mover robots a través de la ruta,
 * reiniciar el sistema, calcular las ganancias totales y visualizar la simulación.
 * 
 * Además, mantiene registros iniciales de tiendas y robots para permitir
 * un reseteo o reinicio de las condiciones iniciales.
 */
public class SilkRoad {
    private static HashMap<Integer, Store> stores;
    private static ArrayList <Robot> robots;
    private static int idRobot = 0;
    private final int lenRoad;
    private int[][] posicion;
    private ArrayList<Integer> StoresEmptiedByLocation;

    /**
     * Constructor de la clase SilkRoad.
     * 
     * @param lengthRoad Longitud de la ruta de seda.
     */
    public SilkRoad(int lengthRoad) {
        if (lengthRoad < 0 || lengthRoad == 0){
            System.out.println("No es posible cerar una ruta de seda de longitud " + lengthRoad);
            this.lenRoad = 0;
        } else {
            this.lenRoad = lengthRoad;
            this.stores = new HashMap<>();
            this.robots = new ArrayList<>();
            this.posicion = Posicion.generateSpiral(lenRoad);
            System.out.println("Se creó la ruta de seda de longitud " + lenRoad);
    
        }
    }
    
    
    /**
    * Extensión para crear una ruta de seda con la entrada del problema de la maratón
    * Requisito 10: Debe permitir crear una ruta de seda con la entrada del problema de la maratón
    * 
    * @param input Array de enteros que representa la entrada del problema de la maratón
    *              Formato: [n, s1, t1, s2, t2, ..., sn, tn, r1, r2, ..., rk]
    *              donde n es el número de tiendas, si y ti son posición y tenges de cada tienda,
    *              y ri son las posiciones iniciales de los robots
    */

    public void create(int[] input) {
        if (input == null || input.length < 1) {
            throw new IllegalArgumentException("Input no puede ser null o vacío");
        }
        int n = input[0]; // número de tiendas
        if (input.length < 1 + 2*n) {
            throw new IllegalArgumentException("Input insuficiente para el número de tiendas especificado");
        }
        // Crear las tiendas según la entrada
        for (int i = 0; i < n; i++) {
            int storePosition = input[1 + 2*i];     // posición de la tienda
            int storeTenges = input[1 + 2*i + 1];   // tenges iniciales de la tienda
            // Validar que la posición esté dentro del rango válido
            if (storePosition < 0 || storePosition >= lenRoad) {
                throw new IllegalArgumentException("Posición de tienda fuera del rango válido: " + storePosition);
            }
            placeStore(storePosition, storeTenges);
        }
        // Crear los robots con las posiciones restantes del input
        int robotStartIndex = 1 + 2*n;
        for (int i = robotStartIndex; i < input.length; i++) {
             int robotPosition = input[i];
             // Validar que la posición esté dentro del rango válido
             if (robotPosition < 0 || robotPosition >= lenRoad) {
                 throw new IllegalArgumentException("Posición de robot fuera del rango válido: " + robotPosition);
                }
             placeRobot(robotPosition);
        }
        System.out.println("Ruta de seda creada con " + n + " tiendas y " + 
                           (input.length - robotStartIndex) + " robots");
    }
    
    /**
 * Constructor que crea una ruta de seda con configuración multi-día
 * Requisito 10: Permite crear una ruta de seda con la entrada del problema de la maratón
 * 
 * @param days Matriz donde cada fila representa la configuración de un día
 *             Formato por fila: [n, s1, t1, s2, t2, ..., sn, tn, r1, r2, ...]
 */
public SilkRoad(int days[][]) {
    if (days == null || days.length == 0) {
        System.out.println("No es posible crear una ruta de seda sin configuración de días");
        this.lenRoad = 0;
        this.stores = new HashMap<>();
        this.robots = new ArrayList<>();
        this.posicion = new int[0][0];
        this.StoresEmptiedByLocation = new ArrayList<>();
        return;
    }
    
    // Calcular longitud necesaria de la ruta
    int maxPosition = 0;
    for (int[] day : days) {
        if (day.length > 0) {
            int n = day[0];
            
            // Revisar posiciones de tiendas
            for (int i = 0; i < n && (1 + 2*i) < day.length; i++) {
                if (day[1 + 2*i] > maxPosition) {
                    maxPosition = day[1 + 2*i];
                }
            }
            
            // Revisar posiciones de robots
            for (int i = 1 + 2*n; i < day.length; i++) {
                if (day[i] > maxPosition) {
                    maxPosition = day[i];
                }
            }
        }
    }
    
    // Inicializar la ruta con longitud calculada
    this.lenRoad = maxPosition + 1;
    this.stores = new HashMap<>();
    this.robots = new ArrayList<>();
    this.posicion = Posicion.generateSpiral(this.lenRoad);
    this.StoresEmptiedByLocation = new ArrayList<>();
    
    // Usar el método create() existente para configurar el primer día
    create(days[0]);
    
    System.out.println("Ruta de seda creada con " + days.length + 
                      " días configurados, longitud: " + this.lenRoad);
}

    /**
     * Coloca una tienda en la ruta en una ubicación específica.
     * 
     * @param location Posición en la ruta.
     * @param tenges   Cantidad inicial de tenges de la tienda.
     */
    public void placeStore(int location, int tenges){
        if (stores.get(location) != null){
            System.out.println("No se puede insertar una tienda sobre una ya existente.");
        } else {
            Store store = new Store(this.posicion[location], tenges, location);
            stores.put(location, store);   
        }
    }

    /**
     * Elimina una tienda de la ruta en una ubicación específica.
     * 
     * @param ocation Posición de la tienda a eliminar.
     */
    public void removeStore(int location){
        Store store = stores.get(location);
        if (store != null) {
            store.removeStore();
            stores.remove(location);
        }
    }

    /**
     * Coloca un robot en la ruta en una ubicación específica.
     * 
     * @param location Posición inicial del robot.
     */
    public void placeRobot(int location) {
        if (robots.get(location) != null){
            System.out.println("No se puede insertar un robot sobre uno ya existente.");
        } else {
            Robot robot = new Robot(posicion[location], location);
            robots.add(robot);
        
            System.out.println("Robot ubicado en la posición " + location);
        }
    }

    /**
     * Elimina un robot de una ubicación específica en la ruta.
     * 
     * @param location Posición del robot a eliminar.
     */
    public void removeRobot(int location) {
        Robot robot = robots.get(location);
        if (robot != null){
            int index = robot.getIndex();
            robot.removeRobot();
            robots.remove(index);
            
        } else {
            System.out.println("No hay robots en la posición indicada.");
        }
    }
    
    /**
     * Obtiene el primer robot que se encuentre en una ubicación dada.
     * 
     * @param location Posición en la ruta.
     * @return El primer robot en la posición o null si no existe.
     */
    private Robot getFirstRobotAtLocation(int location) {
        for (Robot robot: robots){
            if (robot.getIndex() == location) {
                return robot;
            }
        }
        return null;
    }

    /**
     * Obtiene la primera tienda en una ubicación dada.
     * 
     * @param location Posición en la ruta.
     * @return La primera tienda en la posición o null si no existe.
     */
    private Store getFirstStoreAtLocation(int location) {
        var it = stores.values().iterator();
        while (it.hasNext()) {
            Store store = it.next();
            if (store.getLoc() == location) {
                return store;
            }
        }
        return null;
    }

    /**
     * Transfiere los tenges de una tienda a un robot.
     * 
     * @param robot Robot que recibe los tenges.
     * @param store Tienda que transfiere los tenges.
     * @return Cantidad total de tenges acumulados en el robot después de la transacción.
     */
    private int takeTenges(Robot robot, Store store) {
        int newTenges = 0;
        int newRobotTenges = store.getTenge() + robot.getTenge();
        store.setTenge(newTenges);
        return newRobotTenges;
    }
    /**
     * Mueve un robot específico desde su ubicación actual una distancia fija
     * @param location Posición actual del robot a mover
     * @param meters Distancia exacta a mover (positivo = adelante, negativo = atrás)
     */
     public void moveRobot(int location, int meters) {
         if (meters == 0) return;
         Robot robot = getFirstRobotAtLocation(location);
         if (robot == null) {
             System.out.println("No hay robot en la posición " + location);
             return;}

         int newLocation = location + meters;
         if (newLocation < 0 || newLocation >= lenRoad) {
             System.out.println("Movimiento inválido: posición " + newLocation + " fuera de rango");
             return;
         }
         robot.removeRobot();
         robot.setIndexLocation(newLocation);
         robot.setLocation(posicion[newLocation]);
         robot.makeVisible();
         Store storeAtNewLocation = getFirstStoreAtLocation(newLocation);
         if (storeAtNewLocation != null && storeAtNewLocation.getTenge() > 0) {
             int totalTenges = takeTenges(robot, storeAtNewLocation);
             robot.setTenge(totalTenges);
             System.out.println("Robot recolectó " + storeAtNewLocation.getTenge() + 
                          " tenges en posición " + newLocation);
         }
         updateStoresVisualState();
     }
    /**
    * Mueve todos los robots de forma inteligente para maximizar ganancia total
    * Requisito 11: Los robots deciden sus movimientos buscando maximizar la ganancia
    */
   public void moveRobots() {
       if (robots.isEmpty()) {
        System.out.println("No hay robots para mover");
        return;
    }
    System.out.println("=== Moviendo todos los robots de forma inteligente ===");
    ArrayList<Robot> robotsCopy = new ArrayList<>(robots);
    int robotsMoved = 0;
    for (Robot robot : robotsCopy) {
        int currentLocation = robot.getIndex();
        int bestMove = 0;
        int maxGain = 0;
        for (int distance = -1; distance <= 1; distance++) {
            if (distance == 0) continue;
            int targetLocation = currentLocation + distance;
            if (targetLocation >= 0 && targetLocation < lenRoad) {
                Store store = getFirstStoreAtLocation(targetLocation);
                int gain = (store != null && store.getTenge() > 0) ? store.getTenge() : 0;
                
                if (gain > maxGain) {
                    maxGain = gain;
                    bestMove = distance;
                }
            }
        }

        if (bestMove != 0) {
            int newLocation = currentLocation + bestMove;
            robot.removeRobot();

            robot.setIndexLocation(newLocation);
            robot.setLocation(posicion[newLocation]);
            robot.makeVisible();
            Store storeAtNewLocation = getFirstStoreAtLocation(newLocation);
            if (storeAtNewLocation != null && storeAtNewLocation.getTenge() > 0) {
                int totalTenges = takeTenges(robot, storeAtNewLocation);
                robot.setTenge(totalTenges);
                System.out.println("Robot movido de posición " + currentLocation + 
                                  " a " + newLocation + " (ganó " + maxGain + " tenges)");
            }
            
            robotsMoved++;
        }
    }
    updateStoresVisualState();
    }
    
    /**
     * Reabastece las tiendas que se quedaron sin tenges.
     */
    public void resupplyStores() {
        for (Store store : stores.values()) {
            if (store.getTenge() <= 0) {
                store.setInitialTenge();
            }
        }
        System.out.println("Tiendas reabastecidas correctamente");
    }

    /**
    * Retorna los robots a su posicion inicial
    */
    public void returnRobots(){
        for (Robot robot: robots) {
            if (robot.getLocation() != robot.getInitialLocation()){
                robot.makeInvisible();
                robot.resetRobotLocation();
                robot.makeVisible();
            } 
        }
    }
    
    /** 
     * Permite consultar las ganancias que ha logrado cada robot en cada movimiento
     */
    private void showRobotProfits(Robot r){
        System.out.println("El robot ha recolectado hasta este punto: " + r.getTenge());
    }

    /**
     * Retorna la cantidad de veces que una tienda ha sido desocupado por su ubicación.
     */
    public void getTimesStoresEmptied(){
        for (Store store : stores.values()){
            int totalEmptiedTimes  = store.getTimesEmpty();
            int location = store.getLoc();
            System.out.println("La tienda de la ubicación: " + location + " ha sido desocupada " + totalEmptiedTimes + " veces.");
        }
    }
     
    /**
     * Permite identificar el robot con mayor ganancias
     */
    //Preguntarle a la profe.
    private void getRobotHighestProfits(){
        ArrayList<Integer> profits = new ArrayList<>();
        ArrayList<Robot> robs = new ArrayList<>();
        for (Robot rb:robots.values()){
            int profit = rb.getTenge();
            profits.add(profit);
            robs.add(rb);
        }
        int maxProfit = Collections.max(profits);
        
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < profits.size(); i++) {
            if (profits.get(i) == maxProfit) {
                indices.add(i);
            }
        }
        
        if (indices.size() > 1){
            System.out.println("Hay un empate entre los robots");
            for (int i : indices){
                Robot rb = robs.get(i);
                System.out.println("Robot en la ubicación: " + rb.getLoc());        
            }
        } else if (indices.size() <= 0){
            System.out.println("No hay robots registrados");
        } else {
            robs.get(profits.get(0)).makeInvisible();
            robs.get(profits.get(0)).makeVisible();
            robs.get(profits.get(0)).makeInvisible();
            robs.get(profits.get(0)).makeVisible();
        }  
    }
    
    /**
     * Reinicia la simulación de la Ruta de la Seda,
     * reabasteciendo tiendas y retornando robots a sus posiciones iniciales.
     */
    public void reboot(){
        resupplyStores();
        returnRobots();
        System.out.println("Ruta de seda reiniciada - Tiendas reabastecidas y robots reposicionados");
    }

    /**
     * Calcula la ganancia total obtenida por los robots.
     * 
     * @return Cantidad total de tenges obtenida.
     */
     public int profit(){
        int totalProfit;
        for(Robot robot : robots){
            totalProfit += robot.getTenge();
        }
        return totalProfit;
    }

    /**
    * Devuelve información de las tiendas actuales en el simulador.
    * 
    * @return Matriz con ubicación y tenges de cada tienda.
    */
    public int[][] stores(){
        int[][] storesInfo = new int[stores.size()][2];
        int index = 0;
        for (var entry : stores.entrySet()){
            int location = entry.getKey();
            Store store = entry.getValue();
            storesInfo[index][0] = location;
            storesInfo[index][1] = store.getTenge();
            index++;
        }
        return storesInfo;
    }

    /**
    * Devuelve información de los robots actuales en el simulador.
    * 
    * @return Matriz con id, ubicación y tenges de cada robot.
    */
    public int[][] robots(){
        int[][] robotsInfo = new int[robots.size()][2];
        int index = 0;
        for (Robot robot : robots){
            robotsInfo[index][0] = robot.getIndex();
            robotsInfo[index][1] = robot.getTenge();
            index++;
        }
        return robotsInfo;
     }
    
    /**
    * Hace visible la simulación gráfica de la Ruta de la Seda.
    */
    public void makeVisible(){
        for (var entry : stores.entrySet()){
            Store store = entry.getValue();
            if(store.base != null){
                store.base.makeVisible();
            }
        }
        
        for (Robot robot : robots){
            robot.makeVisible();
        }
        updateStoresVisualState();
        System.out.println("Simulador de la Ruta de Seda ahora es visible");
    }

    /**
    * Hace invisible la simulación gráfica de la Ruta de la Seda.
    */
    public void makeInvisible(){
        for (var entry : stores.entrySet()){
            Store store = entry.getValue();
            if (store.base != null) {
                store.base.makeInvisible();
            }
        }
        for (Robot robot : robots){
            robot.makeInvisible();
        }
        System.out.println("Simulador de la Ruta de Seda ahora es invisible");
    }

    /**
    * Termina la simulación de la Ruta de la Seda,
    * liberando todos los recursos (tiendas y robots).
    */
    public void finish(){
        for (var entry : stores.entrySet()){
            Store store = entry.getValue();
            store.removeStore();
        }
        
        for (Robot robot : robots){
            robot.removeRobot();
        }
        
        stores.clear();
        robots.clear();

        System.out.println("Simulador de la Ruta de Seda terminado - Todos los recursos liberados");
    }

    /**
    * Verifica si el simulador está correctamente configurado.
    * 
    * @return true si está en un estado válido, false en caso contrario.
    */
    public boolean ok(){
        try {
            if(stores == null || robots == null){
                return false;
            }
            if(lenRoad <= 0){
                return false;
            }
            if(posicion == null || posicion.length != lenRoad){
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //Requisito de Usabilidad-Las tiendas desocupadas deben lucir diferentes
    /**
    * Actualiza la apariencia visual de todas las tiendas según su estado
    * Las tiendas desocupadas (tenge = 0) lucen diferentes a las ocupadas
    */
    private void updateStoresVisualState() {
        for (var entry : stores.entrySet()) {
            Store store = entry.getValue();
            if (store != null && store.base != null) {
                if (store.getTenge() <= 0) {
                   // Tienda desocupada seran de color gris
                   store.base.changeColor("gray");
                } else {
                    // Tienda con tenges seran de color azul
                    store.base.changeColor("blue");
                }
            }
        }
    }

}