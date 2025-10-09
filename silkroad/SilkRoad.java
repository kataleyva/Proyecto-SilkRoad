import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.*;

/**
 * Clase que representa el simulador de la Ruta de la Seda.
 * 
 * Esta clase permite administrar tiendas (Stores) y robots (Robots)
 * que interactúan en una ruta representada en espiral. 
 * Se pueden colocar y remover tiendas y robots, mover robots a través de la ruta,
 * reiniciar el sistema, calcular las ganancias totales y visualizar la simulación.
 *
 */
public class SilkRoad {
    private HashMap<Integer, Store> stores;
    private ArrayList<Robot> robots;
    private final int lenRoad;
    private int[][] posicion;
    private boolean isVisible;

    /**
     * Constructor de la clase SilkRoad.
     * Inicializa la ruta de seda con una longitud específica y genera
     * la estructura en espiral para posicionar elementos.
     * 
     * @param lengthRoad Longitud de la ruta de seda. Debe ser mayor a 0.
     */
    public SilkRoad(int lengthRoad) {
        if (lengthRoad < 0 || lengthRoad == 0){
            this.lenRoad = 0;
            this.stores = new HashMap<>();
            this.robots = new ArrayList<>();
            this.posicion = new int[0][0];
            this.isVisible = false;
        } else {
            this.lenRoad = lengthRoad;
            this.stores = new HashMap<>();
            this.robots = new ArrayList<>();
            this.posicion = Posicion.generateSpiral(lenRoad);
            this.isVisible = false;
        }
    }
    
    /**
     * Constructor que crea una ruta de seda con configuración multi-día.
     * Requisito 10: Permite crear una ruta de seda con la entrada del problema de la maratón
     * donde cada día puede tener una configuración diferente.
     * La longitud de la ruta se calcula automáticamente basándose en la posición
     * máxima encontrada en todos los días.
     * 
     * @param days Matriz donde cada fila representa la configuración de un día.
     *             Formato por fila: [n, s1, t1, s2, t2, ..., sn, tn, r1, r2, ...]
     *             donde n es el número de tiendas, si y ti son posición y tenges,
     *             y ri son las posiciones iniciales de los robots.
     */
    public SilkRoad(int days[][]) {
        if (days == null || days.length == 0) {
            this.lenRoad = 0;
            this.stores = new HashMap<>();
            this.robots = new ArrayList<>();
            this.posicion = new int[0][0];
            this.isVisible = false;
            return;
        }
        
        int maxPosition = 0;
        for (int[] day : days) {
            if (day.length > 0) {
                int n = day[0];

                for (int i = 0; i < n && (1 + 2*i) < day.length; i++) {
                    if (day[1 + 2*i] > maxPosition) {
                        maxPosition = day[1 + 2*i];
                    }
                }

                for (int i = 1 + 2*n; i < day.length; i++) {
                    if (day[i] > maxPosition) {
                        maxPosition = day[i];
                    }
                }
            }
        }
        this.lenRoad = maxPosition + 1;
        this.stores = new HashMap<>();
        this.robots = new ArrayList<>();
        this.posicion = Posicion.generateSpiral(this.lenRoad);
        this.isVisible = false;

        if (days.length > 0) {
            create(days[0]);
        }
    }
    
    /**
     * Crea una ruta de seda con la entrada del problema de la maratón de programación.
     * Requisito 10: Permite crear una ruta de seda basada en el formato del problema.
     * 
     * @param input Array de enteros que representa la entrada del problema de la maratón.
     *              Formato: [n, s1, t1, s2, t2, ..., sn, tn, r1, r2, ..., rk]
     *              donde n es el número de tiendas, si y ti son posición y tenges de cada tienda,
     *              y ri son las posiciones iniciales de los robots.
     * @throws IllegalArgumentException si el input es null, vacío o tiene formato inválido.
     */
    public void create(int[] input) {
        if (input == null || input.length < 1) {
            throw new IllegalArgumentException("Input no puede ser null o vacío");
        }
        int n = input[0];
        if (input.length < 1 + 2*n) {
            throw new IllegalArgumentException("Input insuficiente para el número de tiendas especificado");
        }
        
        for (int i = 0; i < n; i++) {
            int storePosition = input[1 + 2*i];
            int storeTenges = input[1 + 2*i + 1];
            if (storePosition < 0 || storePosition >= lenRoad) {
                throw new IllegalArgumentException("Posición de tienda fuera del rango válido: " + storePosition);
            }
            placeStore(storePosition, storeTenges);
        }
        
        int robotStartIndex = 1 + 2*n;
        for (int i = robotStartIndex; i < input.length; i++) {
             int robotPosition = input[i];
             if (robotPosition < 0 || robotPosition >= lenRoad) {
                 throw new IllegalArgumentException("Posición de robot fuera del rango válido: " + robotPosition);
                }
             placeRobot(robotPosition);
        }
    }

    /**
     * Coloca una tienda en la ruta en una ubicación específica.
     * Si ya existe una tienda en esa ubicación, muestra un mensaje de error.
     * 
     * @param location Posición en la ruta donde se colocará la tienda (0 <= location < lenRoad).
     * @param tenges Cantidad inicial de tenges que tendrá la tienda.
     */
    public void placeStore(int location, int tenges){
        if (stores.get(location) != null){
            showMessage("No se puede insertar una tienda sobre una ya existente.");
        } else {
            Store store = new Store(this.posicion[location], tenges, location);
            stores.put(location, store);
            if (isVisible) {
                store.makeVisible();
            }
        }
    }

    /**
     * Elimina una tienda de la ruta en una ubicación específica.
     * Si no existe una tienda en esa ubicación, muestra un mensaje de error.
     * 
     * @param location Posición de la tienda a eliminar.
     */
    public void removeStore(int location){
        Store store = stores.get(location);
        if (store != null) {
            store.makeInvisible();
            stores.remove(location);
        } else {
            showMessage("No hay tienda en la posición " + location);
        }
    }

    /**
     * Coloca un robot en la ruta en una ubicación específica.
     * Si ya existe un robot en esa ubicación, muestra un mensaje de error.
     * 
     * @param location Posición inicial del robot en la ruta (0 <= location < lenRoad).
     */
    public void placeRobot(int location) {
        if (getFirstRobotAtLocation(location) != null){
            showMessage("No se puede insertar un robot sobre uno ya existente.");
        } else {
            Robot robot = new Robot(posicion[location], location);
            robots.add(robot);
            if (isVisible) {
                robot.makeVisible();
            }
        }
    }

    /**
     * Elimina el robot ubicado en una posición específica de la ruta.
     * Si no existe un robot en esa ubicación, muestra un mensaje de error.
     * 
     * @param location Posición del robot a eliminar.
     */
    public void removeRobot(int location) {
        Robot robot = getFirstRobotAtLocation(location);
        if (robot != null){
            robot.removeRobot();
            robots.remove(robot);
        } else {
            showMessage("No hay robots en la posición indicada.");
        }
    }
    
    /**
     * Obtiene el primer robot que se encuentre en una ubicación dada.
     * Método auxiliar privado utilizado para buscar robots por posición.
     * 
     * @param location Posición en la ruta donde buscar el robot.
     * @return El primer robot encontrado en la posición, o null si no existe ninguno.
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
     * Método auxiliar privado que utiliza el HashMap para búsqueda eficiente.
     * 
     * @param location Posición en la ruta donde buscar la tienda.
     * @return La tienda en la posición especificada, o null si no existe.
     */
    private Store getFirstStoreAtLocation(int location) {
        return stores.get(location);
    }

    /**
     * Mueve el robot que está en la posición location exactamente meters metros.
     * El robot se mueve la distancia exacta especificada (positiva o negativa).
     * Si encuentra una tienda en la posición destino, recolecta sus tenges.
     * 
     * @param location Posición actual del robot en la ruta.
     * @param meters Distancia a mover en metros. Positivo para avanzar, negativo para retroceder.
     */
    public void moveRobot(int location, int meters) {
         if (meters == 0) return;
         
         Robot robot = getFirstRobotAtLocation(location);
         if (robot == null) {
             showMessage("No hay robot en la posición " + location);
             return;
         }

         int newLocation = location + meters;
         if (newLocation < 0 || newLocation >= lenRoad) {
             showMessage("Movimiento inválido: posición " + newLocation + " fuera de rango");
             return;
         }
         
         robot.setIndexLocation(newLocation);
         robot.setLocation(posicion[newLocation]);
         
         Store storeAtNewLocation = stores.get(newLocation);
         if (storeAtNewLocation != null && storeAtNewLocation.getTenge() > 0) {
             int collectedTenges = storeAtNewLocation.getTenge();
             int totalTenges = robot.getTenge() + collectedTenges;
             robot.setTenge(totalTenges);
             storeAtNewLocation.setTenge(0);
             storeAtNewLocation.incrementTimesEmpty();
             robot.addProfitsInMovements(collectedTenges);
         }
     }

    /**
     * Mueve todos los robots de forma inteligente para maximizar la ganancia total.
     * Requisito 11: Los robots deciden automáticamente sus movimientos buscando
     * la tienda con mayor cantidad de tenges disponibles en toda la ruta.
     * Cada robot se mueve hacia la tienda que le proporcione la máxima ganancia.
     */
    public void moveRobots() {
        if (robots.isEmpty()) {
            showMessage("No hay robots para mover");
            return;
        }
        
        ArrayList<Robot> robotsCopy = new ArrayList<>(robots);
        
        for (Robot robot : robotsCopy) {
            int currentLocation = robot.getIndex();
            int bestMove = 0;
            int maxGain = 0;
            
            for (int distance = -lenRoad; distance <= lenRoad; distance++) {
                if (distance == 0) continue;
                
                int targetLocation = currentLocation + distance;
                if (targetLocation >= 0 && targetLocation < lenRoad) {
                    Store store = stores.get(targetLocation);
                    int gain = (store != null && store.getTenge() > 0) ? store.getTenge() : 0;
                    
                    if (gain > maxGain) {
                        maxGain = gain;
                        bestMove = distance;
                    }
                }
            }

            if (bestMove != 0 && maxGain > 0) {
                int newLocation = currentLocation + bestMove;
                
                robot.setIndexLocation(newLocation);
                robot.setLocation(posicion[newLocation]);
                
                Store storeAtNewLocation = stores.get(newLocation);
                if (storeAtNewLocation != null && storeAtNewLocation.getTenge() > 0) {
                    int totalTenges = robot.getTenge() + maxGain;
                    robot.setTenge(totalTenges);
                    storeAtNewLocation.setTenge(0);
                    storeAtNewLocation.incrementTimesEmpty();
                    robot.addProfitsInMovements(maxGain);
                }
            }
        }
    }
    
    /**
     * Reabastece todas las tiendas que se quedaron sin tenges.
     * Restaura cada tienda a su cantidad inicial de tenges.
     */
    public void resupplyStores() {
        for (Store store : stores.values()) {
            store.setInitialTenge();
        }
    }

    /**
     * Retorna todos los robots a su posición inicial en la ruta.
     * Cada robot conoce y regresa a su ubicación original.
     */
    public void returnRobots(){
        for (Robot robot: robots) {
            robot.resetRobotLocation();
        }
    }

    /**
     * Retorna la cantidad de veces que cada tienda ha sido desocupada,
     * ordenadas por localización de menor a mayor.
     * 
     * @return Matriz int[n][2] donde cada fila contiene [ubicación, veces_vacía].
     *         Las filas están ordenadas por ubicación de menor a mayor.
     */
    public int[][] emptiedStores() {
        int[][] result = new int[stores.size()][2];
        int index = 0;
        
        Integer[] locations = stores.keySet().toArray(new Integer[0]);
        Arrays.sort(locations);
        
        for (Integer location : locations) {
            Store store = stores.get(location);
            result[index][0] = location;
            result[index][1] = store.getTimesEmpty();
            index++;
        }
        
        return result;
    }
    
    /** 
     * Permite consultar las ganancias que ha logrado cada robot en cada movimiento
     */
    private void showRobotProfits(Robot r){
        showMessage("El robot ha recolectado hasta este punto: " + r.getTenge());
    }
    
    /**
     * Permite identificar el robot con mayor ganancias
     * @return void
     */
    private void getRobotHighestProfits() {
        if (robots.isEmpty()) return; 
    
        ArrayList<Integer> profits = new ArrayList<>();
        ArrayList<Robot> robs = new ArrayList<>();
    
        for (Robot rb : robots) {
            profits.add(rb.getTenge());
            robs.add(rb);
        }
    
        int maxProfit = Collections.max(profits);
    
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < profits.size(); i++) {
            if (profits.get(i) == maxProfit) {
                indices.add(i);
            }
        }

        if (indices.size() != 1) return;
    
        Robot rb = robs.get(indices.get(0));
        for (int j = 0; j < 2; j++) {
            rb.makeInvisible();
            rb.makeVisible();
        }
    }

    /**
     * Ejecutar constantemente el programa.
     */
    //Código generado por IA para poder visualizar constantemente el robot con mayor beneficio dado que no sabíamos cómo ejecutarlo.
    private void startRobotProfitMonitor() {
        Thread monitorThread = new Thread(() -> {
            while (true) {
                try {
                    getRobotHighestProfits();
                    Thread.sleep(3000); // cada 3 segundos
                } catch (InterruptedException e) {
                    break; // sale del hilo si se interrumpe
                }
            }
        });
        monitorThread.setDaemon(true); // no bloquea la finalización del programa
        monitorThread.start();
    }


    /**
     * Reinicia la simulación de la Ruta de la Seda.
     * Reabastece todas las tiendas a sus valores iniciales,
     * retorna los robots a sus posiciones iniciales y reinicia sus ganancias.
     */
    public void reboot(){
        resupplyStores();
        returnRobots();
        for (Robot robot : robots) {
            robot.resetProfits();
        }
    }

    /**
     * Calcula la ganancia total obtenida por todos los robots.
     * Suma los tenges acumulados de cada robot en el simulador.
     * 
     * @return Cantidad total de tenges obtenida por todos los robots.
     */
    public int profit(){
        int totalProfit = 0;
        for(Robot robot : robots){
            totalProfit += robot.getTenge();
        }
        return totalProfit;
    }

    /**
     * Devuelve información de las tiendas actuales en el simulador.
     * 
     * @return Matriz int[n][2] donde cada fila contiene [ubicación, tenges_actuales]
     *         de cada tienda en el simulador.
     */
    public int[][] stores(){
        int[][] storesInfo = new int[stores.size()][2];
        int index = 0;
        for (Map.Entry<Integer, Store> entry : stores.entrySet()){
            int location = entry.getKey();
            Store store = entry.getValue();
            storesInfo[index][0] = location;
            storesInfo[index][1] = store.getTenge();
            index++;
        }
        return storesInfo;
    }

    /**
     * Devuelve información de los robots actuales en el simulador,
     * ordenados por localización de menor a mayor.
     * 
     * @return Matriz int[n][2] donde cada fila contiene [ubicación, tenges_actuales]
     *         de cada robot. Las filas están ordenadas por ubicación.
     */
    public int[][] robots(){
        int[][] robotsInfo = new int[robots.size()][2];
        
        ArrayList<Robot> sortedRobots = new ArrayList<>(robots);
        sortedRobots.sort((r1, r2) -> Integer.compare(r1.getIndex(), r2.getIndex()));
        
        int index = 0;
        for (Robot robot : sortedRobots){
            robotsInfo[index][0] = robot.getIndex();
            robotsInfo[index][1] = robot.getTenge();
            index++;
        }
        return robotsInfo;
     }
    
    /**
     * Retorna las ganancias de cada robot por movimiento,
     * ordenadas por localización de menor a mayor.
     * 
     * @return Matriz int[n][m] donde:
     * Primera columna contiene la ubicación del robot
     * Columnas siguientes contienen las ganancias obtenidas en cada movimiento
     * Las filas están ordenadas por ubicación de menor a mayor.
     */
    public int[][] profitPerMove() {
        int maxMovs = 0;
        for (Robot robot : robots) {
            int movs = robot.getMovements().size();
            if (movs > maxMovs) {
                maxMovs = movs;
            }
        }

        int[][] matriz = new int[robots.size()][1 + maxMovs];
        
        ArrayList<Robot> sortedRobots = new ArrayList<>(robots);
        sortedRobots.sort((r1, r2) -> Integer.compare(r1.getIndex(), r2.getIndex()));
    
        int fila = 0;
        for (Robot robot : sortedRobots) {
            matriz[fila][0] = robot.getIndex();
    
            ArrayList<Integer> movs = robot.getMovements();
            for (int i = 0; i < movs.size(); i++) {
                matriz[fila][i + 1] = movs.get(i);  
            }
            fila++;
        }
        
        return matriz;
    }
    /**
     * Hace visible la simulación gráfica de la Ruta de la Seda.
     * Muestra visualmente todas las tiendas y robots en sus posiciones actuales.
     * A partir de este momento, los mensajes de error se mostrarán como popups.
     */
    public void makeVisible(){
        isVisible = true;
        for (Map.Entry<Integer, Store> entry : stores.entrySet()){
            Store store = entry.getValue();
            store.makeVisible();
        }
        
        for (Robot robot : robots){
            robot.makeVisible();
        }
    }

    /**
     * Hace invisible la simulación gráfica de la Ruta de la Seda.
     * Oculta visualmente todas las tiendas y robots.
     * Los mensajes de error dejarán de mostrarse como popups.
     */
    public void makeInvisible(){
        isVisible = false;
        for (Map.Entry<Integer, Store> entry : stores.entrySet()){
            Store store = entry.getValue();
            store.makeInvisible();
        }
        
        for (Robot robot : robots){
            robot.makeInvisible();
        }
    }

    /**
     * Termina la simulación de la Ruta de la Seda y finaliza la ejecución del programa.
     * Libera todos los recursos (tiendas y robots) y cierra la aplicación.
     */
    public void finish(){
        for (Map.Entry<Integer, Store> entry : stores.entrySet()){
            Store store = entry.getValue();
            store.makeInvisible();
        }
        
        for (Robot robot : robots){
            robot.removeRobot();
        }
        
        stores.clear();
        robots.clear();
        
        System.exit(0);
    }

    /**
     * Verifica si el simulador está correctamente configurado y en un estado válido.
     * Comprueba la integridad de todas las estructuras de datos y posiciones.
     * 
     * @return true si el simulador está en un estado válido y puede operar correctamente,
     *         false si hay algún problema de configuración o datos inconsistentes.
     */
    public boolean ok(){
        if(stores == null || robots == null){
            return false;
        }
        if(lenRoad <= 0){
            return false;
        }
        if(posicion == null || posicion.length != lenRoad){
            return false;
        }
        
        for (Integer location : stores.keySet()) {
            if (location < 0 || location >= lenRoad) {
                return false;
            }
        }
        
        for (Robot robot : robots) {
            if (robot.getIndex() < 0 || robot.getIndex() >= lenRoad) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Muestra un mensaje al usuario utilizando un popup (JOptionPane).
     * Los mensajes solo se muestran si el simulador está en modo visible.
     * Método auxiliar privado para comunicación con el usuario.
     * 
     * @param message Mensaje a mostrar al usuario.
     */
    private void showMessage(String message) {
        if (isVisible) {
            JOptionPane.showMessageDialog(null, message, "Silk Road Simulator", 
                                        JOptionPane.INFORMATION_MESSAGE);
        }
    }
//Pruebas de aceptacion
/**
 * Prueba de aceptación visual para la clase SilkRoad.
 * 
 * Crea una simulación con tiendas y robots, los mueve y reinicia,
 */
public void testAceptacion() {
    System.out.println(" INICIO PRUEBA DE ACEPTACIÓN DE SILKROAD ");
    SilkRoad simulador = new SilkRoad(10);
    simulador.makeVisible();
    System.out.println("Ruta creada con longitud 10.");
    simulador.placeStore(2, 40);
    simulador.placeStore(5, 80);
    simulador.placeStore(8, 30);
    System.out.println("Tiendas colocadas en posiciones 2, 5 y 8.");
    simulador.placeRobot(0);
    simulador.placeRobot(3);
    System.out.println("Robots colocados en posiciones 0 y 3.");

    esperar(1500);
    System.out.println("Moviendo robots manualmente...");
    simulador.moveRobot(0, 2); 
    esperar(1500);
    simulador.moveRobot(3, 2); 
    esperar(1500);
    System.out.println("Ganancia total hasta ahora: " + simulador.profit());
    simulador.resupplyStores();
    System.out.println("Tiendas reabastecidas.");
    esperar(1500);

    System.out.println("Moviendo robots automáticamente (buscando la tienda más rentable)...");
    simulador.moveRobots();
    esperar(2000);
    System.out.println("Ganancia total tras movimiento automático: " + simulador.profit());

    System.out.println("Reiniciando simulación...");
    simulador.reboot();
    esperar(2000);

    simulador.makeInvisible();
    System.out.println("FIN DE PRUEBA DE ACEPTACIÓN");
}

/**
 * Método auxiliar que pausa la ejecución por unos milisegundos
 * para que el usuario pueda observar los cambios visuales.
 */
private void esperar(int milisegundos) {
    try {
        Thread.sleep(milisegundos);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
}