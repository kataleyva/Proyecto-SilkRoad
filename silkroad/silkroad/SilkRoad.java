package silkroad;
import shapes.Rectangle;
import shapes.Circle;
import shapes.Line;
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
    private static boolean isVisible;
    private ArrayList<Line> lineasCamino;

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
            this.lineasCamino = new ArrayList<>();  
        } else {
            this.lenRoad = lengthRoad;
            this.stores = new HashMap<>();
            this.robots = new ArrayList<>();
            this.posicion = Posicion.generateSpiral(lenRoad);
            this.isVisible = false;
            this.lineasCamino = new ArrayList<>();  
            crearCaminoVisual();
        }
    }
    
    private void crearCaminoVisual() {
        for (int i = 0; i < posicion.length - 1; i++) {
            Line linea = new Line(
                posicion[i][0], posicion[i][1],
                posicion[i+1][0], posicion[i+1][1]
            );
            lineasCamino.add(linea);

            if (isVisible){
                linea.makeVisible();
            }
        }
    }
  
    /**
    * Constructor que crea una ruta de seda con configuración acumulativa por días.
    * 
    * Según las especificaciones de la maratón:
    * - Cada día representa una acción (agregar robot o tienda)
    * - Formato: [1, x] → agregar robot en posición x
    *           [2, x, c] → agregar tienda en posición x con c tenges
    * 
    * La longitud de la ruta se calcula automáticamente basándose en la posición
    * máxima encontrada en todos los días.
    * 
    * @param days Matriz donde cada fila representa una acción de un día.
    *             - Fila i: [1, x] → agregar robot
    *             - Fila i: [2, x, c] → agregar tienda
    * 
    * @throws IllegalArgumentException si days es null o vacío
    */
    public SilkRoad(int[][] days) {
        if (days == null || days.length == 0) {
            this.lenRoad = 0;
            this.stores = new HashMap<>();
            this.robots = new ArrayList<>();
            this.posicion = new int[0][0];
            this.isVisible = false;
            this.lineasCamino = new ArrayList<>();
            return;
        }
        int maxPosition = 0;
        for (int[] day : days) {
            if (day.length >= 2) {
                int actionType = day[0];
                int position = day[1];
                if (position > maxPosition) {
                    maxPosition = position;
                }
            }
        }
        this.lenRoad = maxPosition + 1;
        this.stores = new HashMap<>();
        this.robots = new ArrayList<>();
        this.posicion = Posicion.generateSpiral(this.lenRoad);
        this.isVisible = false;
        this.lineasCamino = new ArrayList<>();
        if (this.lenRoad > 0) {
            crearCaminoVisual();
        }
        for (int[] day : days) {
            procesarDia(day);
        }
    }

    /**
    * Procesa un día específico, agregando un robot o una tienda.
    * 
    * @param day Array con la acción del día: [1, x] o [2, x, c]
    */
    private void procesarDia(int[] day) {
        if (day == null || day.length < 2) {
            return;
        }
        int actionType = day[0];
    
        if (actionType == 1) {
            //Agregamos un robor
            if (day.length >= 2) {
                int robotPosition = day[1];
                if (robotPosition >= 0 && robotPosition < lenRoad) {
                    placeRobot(robotPosition);
                }
            }
        } 
        else if (actionType == 2) {
            //Agregamos una tienda
            if (day.length >= 3) {
                int storePosition = day[1];
                int tenges = day[2];
                if (storePosition >= 0 && storePosition < lenRoad) {
                    placeStore(storePosition, tenges);
                }
            }
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
            if (isVisible){
                showMessage("No se puede insertar una tienda sobre una ya existente.");
            }
        }else if(getFirstRobotAtLocation(location) != null){
            if (isVisible){
                showMessage("No se puede insertar una tienda sobre un robot ya existente.");
            }
        }else{
            Store store = new Store(this.posicion[location], tenges, location);
            stores.put(location, store);
            if (isVisible) {
                store.makeVisible();
            }
        }
    }
    
    /**
     *Place a specific type of store on the route
     *@param type  Tipo de tienda("autonomus","fighter","bonus" o "normal"
     *@param location Posicion sigerida donde colocar la tienda
     *@param tenges Cantidad inicial de tenges
     */
    public void placeStore(String type, int location, int tenges){
        if (location<0 || location >=lenRoad){
            if(isVisible) showMessage("Posicion"+location+"fuera de rango valido");
            return;
        }
        if (stores.get(location) != null){
            if (isVisible) showMessage("Ya existe una tienda en la posicion");
            return;
        }
        if (getFirstRobotAtLocation(location) != null){
            if (isVisible) showMessage("No se puede colocar la tienda en una posicion ocupada por un robot");
            return;
        }
        Store store;
        switch (type.toLowerCase()){
            case "fighter":
                store = new Fighter(this.posicion[location],tenges,location);
                break;
            case "autonomous":
                int autonomousLocation=findAvailableLocation(location);
                store = new Autonomous(this.posicion[autonomousLocation],tenges,autonomousLocation,lenRoad);
                break;
            case "bonus":
                store = new Bonus(this.posicion[location],tenges,location);
                break;
            case "normal":
            default:
                store=new Store(this.posicion[location],tenges,location);
                break;
        }
        stores.put(location,store);
        
        if (isVisible){
            store.makeVisible();
        }
            
    }
    
    /**
     * Metodo auxiliar para encontrar una ubicacion cerca disponible
     * para la tienda autonomous
     */
    private int findAvailableLocation(int suggestedLocation){
        Random random = new Random();
        int attempts = 0;
        int newLocation = suggestedLocation;
        while(attempts < 0 ){
            int offset= random.nextInt(5)-2 ;
            newLocation=Math.max(0,Math.min(lenRoad-1,suggestedLocation+offset));
            if(stores.get(newLocation)==null && getFirstRobotAtLocation(newLocation)==null){
                return newLocation;
            }
            attempts++;
        }
        return suggestedLocation;
    }
    
    /**
     * 
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
            if (isVisible){
                showMessage("No hay tienda en la posición " + location);
            }
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
            if (isVisible) {
                showMessage("No se puede insertar un robot sobre uno ya existente.");
            }
        }else if(getFirstStoreAtLocation(location) != null){
            if (isVisible) {
                showMessage("No se puede insertar un robot sobre una tienda ya existente.");
            }
        } else {
            Robot robot = new Robot(posicion[location], location);
            robots.add(robot);
            if (isVisible) {
                robot.makeVisible();
            }
        }
    }

    /**
     * place Robot depends the type
     * @param String type robot
     * @param int location where the robot will placed
     * @return void
     */
    public void placeRobot(String type, int location){
        if (getFirstRobotAtLocation(location) != null){
            if (isVisible) {
                showMessage("No se puede insertar un robot sobre uno ya existente.");
            }
        }else if(getFirstStoreAtLocation(location) != null){
            if (isVisible) {
                showMessage("No se puede insertar un robot sobre una tienda ya existente.");
            }
        } else {
            if (type == "Tender"){
                Robot robot = new TenderRobot(posicion[location], location);
                robots.add(robot);
                if (isVisible) {
                    robot.makeVisible();
                }
            }
            
            if (type == "neverBack"){
                Robot robot = new NeverBackRobot(posicion[location], location);
                robots.add(robot);
                if (isVisible) {
                    robot.makeVisible();
                }
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
            if (isVisible){
                showMessage("No hay robots en la posición indicada.");
            }
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

    public Robot getRobot(int index) {
        if (index < 0 || index >= robots.size()) return null;
        return robots.get(index);
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
            if (isVisible){
                showMessage("No hay robot en la posición " + location);
            }
            return;
        }

        int newLocation = location + meters;
        if (newLocation < 0 || newLocation >= lenRoad) {
            if (isVisible){
                showMessage("Movimiento inválido: posición " + newLocation + " fuera de rango");
            }
            return;
        }
        
        if (!(robot instanceof TenderRobot)){
            moveNormalRobot(robot, location, meters);
        } else {
            moveTenderRobot(robot, location, meters);
        }
        
        getRobotHighestProfits();
    }
    
    /**
     * Mueve el robot usando el sistema de attemptCollection para cumplir con el comportamiento
     * y reglas de Fighter
     * @param Location Posicion actual del robot
     * @param meter Distancia que se mueve
     */
    public void moveRobotInFighter(int location, int meters) {
    if (meters == 0) return;
     
    Robot robot = getFirstRobotAtLocation(location);
    if (robot == null) {
        if (isVisible){
            showMessage("No hay robot en la posición " + location);
        }
        return;
    }

    int newLocation = location + meters;
    if (newLocation < 0 || newLocation >= lenRoad) {
        if (isVisible){
            showMessage("Movimiento inválido: posición " + newLocation + " fuera de rango");
        }
        return;
    }
     
    int distance = Math.abs(meters);
    robot.setIndexLocation(newLocation);
    robot.setLocation(posicion[newLocation]);
    
    Store storeAtNewLocation = stores.get(newLocation);
    int collectedTenges = 0;
    
    if (storeAtNewLocation != null && storeAtNewLocation.getTenge() > 0) {
        collectedTenges = storeAtNewLocation.attemptCollection(robot.getTenge());
    }
    
    int gananciaNeta = collectedTenges - distance;
    robot.setTenge(robot.getTenge() + gananciaNeta);
    robot.addProfitsInMovements(gananciaNeta);
    }

    /**
     * Mueve el robot normal que está en la posición location exactamente meters metros.
     * El robot se mueve la distancia exacta especificada (positiva o negativa).
     * Si encuentra una tienda en la posición destino, recolecta sus tenges.
     * @paaram robot que se desea mover.
     * @param location Posición actual del robot en la ruta.
     * @param meters Distancia a mover en metros. Positivo para avanzar, negativo para retroceder.
     */
    private void moveNormalRobot(Robot robot, int location, int meters){
        int distance = Math.abs(meters);
        int newLocation = location+meters;
        robot.setIndexLocation(newLocation);
        robot.setLocation(posicion[newLocation]);
            
        Store storeAtNewLocation = stores.get(newLocation);
        int collectedTenges = 0;
        
        if (storeAtNewLocation != null && storeAtNewLocation.getTenge() > 0) {
            collectedTenges = storeAtNewLocation.attemptCollection(robot.getTenge());
            storeAtNewLocation.setTenge(0);
            storeAtNewLocation.incrementTimesEmpty();
        }
        
        int gananciaNeta = collectedTenges - distance;
        robot.setTenge(robot.getTenge() + gananciaNeta);
        
        robot.addProfitsInMovements(gananciaNeta);
    }
    
    /**
     * Mueve el robot Tender que está en la posición location exactamente meters metros.
     * El robot se mueve la distancia exacta especificada (positiva o negativa).
     * Si encuentra una tienda en la posición destino, recolecta sus tenges.
     * @paaram robot que se desea mover.
     * @param location Posición actual del robot en la ruta.
     * @param meters Distancia a mover en metros. Positivo para avanzar, negativo para retroceder.
     */
    private void moveTenderRobot(Robot robot, int location, int meters){
        int distance = Math.abs(meters);
        int newLocation = location+meters;
        robot.setIndexLocation(newLocation);
        robot.setLocation(posicion[newLocation]);
            
        Store storeAtNewLocation = stores.get(newLocation);
        int storeTenges;
        int collectedTenges;
        int newStoreTenges = 0; 
            
        if (storeAtNewLocation != null && storeAtNewLocation.getTenge() > 0) {
            storeTenges = storeAtNewLocation.getTenge();
            newStoreTenges = storeTenges/2; 
            storeAtNewLocation.setTenge(newStoreTenges);
            storeAtNewLocation.incrementTimesEmpty();
        } 
        
        int gananciaNeta = newStoreTenges - distance;
        robot.setTenge(robot.getTenge() + gananciaNeta);
        
        robot.addProfitsInMovements(gananciaNeta);
    }
    
    /**
     * Requisito 11 :Debe permitir a los robots decidir sus movimientos buscando maximizar la ganancia.
     */
    public void moveRobots() {
        if (robots.isEmpty()) {
            if (isVisible){
                showMessage("No hay robots para mover");
            }
            return;
        }
        
        if (stores.isEmpty()) {
            return;
        }
        
        // Crear lista de tiendas ordenadas por posición
        List<Integer> storePositions = new ArrayList<>(stores.keySet());
        Collections.sort(storePositions);
        
        // Para cada robot, calcular la mejor ganancia posible
        int mejorGananciaTotal = 0;
        Map<Robot, List<Integer>> mejorAsignacion = new HashMap<>();
        
        // Probar todas las particiones posibles de tiendas entre robots
        calcularMejorParticion(new ArrayList<>(robots), storePositions, 0, 
                              new HashMap<>(), 0, mejorAsignacion);
        
        // Ejecutar la mejor asignación encontrada
        for (Map.Entry<Robot, List<Integer>> entry : mejorAsignacion.entrySet()) {
            Robot robot = entry.getKey();
            List<Integer> rutaTiendas = entry.getValue();
            
            if (rutaTiendas.isEmpty()) continue;
            
            int posActual = robot.getIndex();
            int gananciaRobot = 0;
            
            // Visitar cada tienda en orden
            for (Integer storePosicion : rutaTiendas) {
                Store tienda = stores.get(storePosicion);
                if (tienda == null || tienda.getTenge() <= 0) continue;
                
                int distancia = Math.abs(storePosicion - posActual);
                int tenges = tienda.getTenge();
                int gananciaNeta = tenges - distancia;
                
                gananciaRobot += gananciaNeta;
                posActual = storePosicion;
                
                // Vaciar tienda
                tienda.setTenge(0);
                tienda.incrementTimesEmpty();
            }
            
            // Actualizar robot
            robot.setIndexLocation(posActual);
            robot.setLocation(posicion[posActual]);
            robot.setTenge(robot.getTenge() + gananciaRobot);
            robot.addProfitsInMovements(gananciaRobot);
        }
        
        getRobotHighestProfits();
    }

    /**
     * Calcula recursivamente la mejor partición de tiendas entre robots.
     * 
     * @param robots Lista de robots disponibles
     * @param tiendas Lista de posiciones de tiendas
     * @param indiceTienda Índice actual en la lista de tiendas
     * @param asignacionActual Asignación actual de robots a rutas
     * @param indiceRobot Robot actual procesando
     * @param mejorAsignacion Output: mejor asignación encontrada
     */
    private void calcularMejorParticion(
        List<Robot> robots,
        List<Integer> tiendas,
        int indiceTienda,
        Map<Robot, List<Integer>> asignacionActual,
        int indiceRobot,
        Map<Robot, List<Integer>> mejorAsignacion
    ) {
        // Caso base: procesamos todas las tiendas
        if (indiceTienda >= tiendas.size()) {
            int gananciaActual = calcularGananciaTotal(asignacionActual);
            int gananciaActualMejor = calcularGananciaTotal(mejorAsignacion);
            
            if (gananciaActual > gananciaActualMejor) {
                mejorAsignacion.clear();
                for (Map.Entry<Robot, List<Integer>> entry : asignacionActual.entrySet()) {
                    mejorAsignacion.put(entry.getKey(), new ArrayList<>(entry.getValue()));
                }
            }
            return;
        }
        
        // Probar asignar la tienda actual a cada robot
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = robots.get(i);
            Integer tiendaPos = tiendas.get(indiceTienda);
            
            // Agregar tienda a la ruta del robot
            if (!asignacionActual.containsKey(robot)) {
                asignacionActual.put(robot, new ArrayList<>());
            }
            asignacionActual.get(robot).add(tiendaPos);
            
            // Recursión
            calcularMejorParticion(robots, tiendas, indiceTienda + 1, 
                                  asignacionActual, i, mejorAsignacion);
            
            // Backtrack
            asignacionActual.get(robot).remove(asignacionActual.get(robot).size() - 1);
            if (asignacionActual.get(robot).isEmpty()) {
                asignacionActual.remove(robot);
            }
        }
        
        // También probar NO asignar la tienda a ningún robot
        calcularMejorParticion(robots, tiendas, indiceTienda + 1, 
                              asignacionActual, indiceRobot, mejorAsignacion);
    }

    /**
     * Calcula la ganancia total de una asignación de robots a rutas.
     * 
     * @param asignacion Mapa de Robot -> Lista de posiciones de tiendas
     * @return Ganancia total
     */
    private int calcularGananciaTotal(Map<Robot, List<Integer>> asignacion) {
        int total = 0;
        
        for (Map.Entry<Robot, List<Integer>> entry : asignacion.entrySet()) {
            Robot robot = entry.getKey();
            List<Integer> ruta = entry.getValue();
            
            int posActual = robot.getIndex();
            
            for (Integer tiendaPos : ruta) {
                Store tienda = stores.get(tiendaPos);
                if (tienda == null) continue;
                
                int distancia = Math.abs(tiendaPos - posActual);
                int tenges = tienda.getTenge();
                int ganancia = tenges - distancia;
                
                total += ganancia;
                posActual = tiendaPos;
            }
        }
        
        return total;
    }
    
    /**
     * Reabastece todas las tiendas que se quedaron sin tenges.
     * Restaura cada tienda a su cantidad inicial de tenges.
     */
    public void resupplyStores() {
        for (Store store : stores.values()) {
            int tengeStore = store.getTenge();
            if (tengeStore <= 0){
                store.setInitialTenge();
            }
        }
    }

    /**
     * Retorna todos los robots a su posición inicial en la ruta.
     * Cada robot conoce y regresa a su ubicación original.
     */
    public void returnRobots(){
        for (Robot robot: robots) {
            if (!(robot instanceof NeverBackRobot) && robot.getLocation() != robot.getInitialLocation()){
                robot.resetRobotLocation();
            }
        }
        getRobotHighestProfits();
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
        if (isVisible){
            showMessage("El robot ha recolectado hasta este punto: " + r.getTenge());
        }
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
        if (isVisible){
            for (int j = 0; j < 3; j++) {
                rb.makeInvisible();
                pause(1000);
                rb.makeVisible();
            }
        }
    }
    
    /** 
     * Método que muestra el robot con mayor beneficio en cada movimiento
     */
    private void showRobotHighestProfit(){
        getRobotHighestProfits();
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
        
        for (Line linea : lineasCamino) {
            linea.makeVisible();
        }
        
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
        
        for (Line linea : lineasCamino) {
            linea.makeInvisible();
        }    
        
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
        for (Store store : stores.values()){
            store.makeInvisible();
        }
        
        for (Robot robot : robots){
            robot.makeInvisible();
        }
        
        stores.clear();
        robots.clear();
        makeInvisible();
        if (isVisible){
            showMessage("Juego finalizado\n");
            showMessage("Cantidad de tiendas:"+ stores.size() + "\n");
            showMessage("Cantidad de robots:"+ robots.size() + "\n");
        }
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
        JOptionPane.showMessageDialog(null, message, "Silk Road Simulator", 
                                        JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Método auxiliar que pausa la ejecución por unos milisegundos
     * para que el usuario pueda observar los cambios visuales.
     */
    public void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public boolean getVisibility(){
        return isVisible;
    }
    
    public Store getStore(int position){
        return stores.get(position);
    }
}
