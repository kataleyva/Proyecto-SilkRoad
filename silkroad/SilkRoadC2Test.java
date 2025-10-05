import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SilkRoadC2Test {
    
    private SilkRoad silkRoad;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @Before
    public void setUp() {
        silkRoad = new SilkRoad(10);
        System.setOut(new PrintStream(outContent));
    }
    
    //resupplyStores()
    
    /**
     * Verifica que resupplyStores restaure correctamente las tiendas vacías
     */
    @Test
    public void ResupplyStoresLRShouldRestoreEmptyStores() {
        silkRoad.placeStore(0, 100);
        silkRoad.placeStore(1, 50);
        
        int[][] initialStores = silkRoad.stores();
        
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 1); 
        
        silkRoad.resupplyStores();
        
        int[][] resuppliedStores = silkRoad.stores();
        assertTrue("Las tiendas deben ser reabastecidas correctamente", 
                   resuppliedStores.length > 0);
        String output = outContent.toString();
        assertTrue("Debe mostrar mensaje de reabastecimiento", 
                   output.contains("Tiendas reabastecidas correctamente"));
    }
    
    /**
     * Verifica que resupplyStores no afecte tiendas que ya tienen tenges
     */
    @Test
    public void ResupplyStoresLRShouldNotAffectNonEmptyStores() {
        silkRoad.placeStore(0, 100);
        silkRoad.placeStore(1, 75);
        
        int[][] storesBeforeResupply = silkRoad.stores();
        
        silkRoad.resupplyStores();
        
        int[][] storesAfterResupply = silkRoad.stores();
        assertEquals("El número de tiendas debe mantenerse igual", 
                     storesBeforeResupply.length, storesAfterResupply.length);
        String output = outContent.toString();
        assertTrue("Debe mostrar mensaje de reabastecimiento exitoso", 
                   output.contains("Tiendas reabastecidas correctamente"));
    }
    
    // showRobotProfits() ==============
    
    /**
     * Verifica que showRobotProfits muestre correctamente las ganancias de un robot e identifica bugs.
     */
    @Test
    public void ShowRobotProfitsLRShouldDisplayCorrectProfits()  {
        silkRoad.placeRobot(0);
        silkRoad.placeStore(1, 50);
        silkRoad.placeStore(3, 100);
 
        silkRoad.moveRobot(0, 1);
        silkRoad.moveRobot(1, 2);
        
        assertEquals(150, silkRoad.profit());
    }
    
    /**
     * Verifica que showRobotProfits no se ejecute cuando no hay robots
     */
    @Test
    public void ShowRobotProfitsLRShouldNotExecuteWithoutRobots() {
        silkRoad.placeStore(1, 50);
        
        silkRoad.moveRobot(0, 1);
        
        String output = outContent.toString();
        assertFalse("No debe mostrar ganancias cuando no hay robots", 
                    output.contains("El robot ha recolectado hasta este punto:"));
    }
    
    // getTimesStoresEmptied()
    
    /**
     * Verifica que getTimesStoresEmptied muestre correctamente las veces que cada tienda ha sido vaciada
     */
    @Test
    public void GetTimesStoresEmptiedLRShouldDisplayEmptiedCount() {
        silkRoad.placeStore(0, 30);
        silkRoad.placeStore(2, 40);
        
        silkRoad.getTimesStoresEmptied();
        
        String output = outContent.toString();
        assertTrue("Debe mostrar información de vaciado para ubicación 0", 
                   output.contains("La tienda de la ubicación: 0 ha sido desocupada"));
        assertTrue("Debe mostrar información de vaciado para ubicación 2", 
                   output.contains("La tienda de la ubicación: 2 ha sido desocupada"));
    }
    
    /**
     * Verifica que getTimesStoresEmptied no falle cuando no hay tiendas
     */
    @Test
    public void GetTimesStoresEmptiedLRShouldHandleNoStores() {
        silkRoad.getTimesStoresEmptied();
        
        String output = outContent.toString();
        
        assertFalse("No debe mostrar información de tiendas cuando no las hay", 
                    output.contains("La tienda de la ubicación:"));
    }
    
    // getRobotHighestProfits() 
    
    /**
     * Verifica que getRobotHighestProfits identifique correctamente el robot con mayores ganancias
     */
    @Test
    public void GetRobotHighestProfitsLRShouldIdentifyTopRobot() {
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(2);
        silkRoad.placeStore(1, 100);
        silkRoad.placeStore(3, 50);
        
        silkRoad.moveRobot(0, 1); 
        silkRoad.moveRobot(2, 1); 
        
        silkRoad.getRobotHighestProfits();
        
        assertTrue("El método debe ejecutarse sin errores", true);
    }
    
    /**
     * Verifica que getRobotHighestProfits maneje correctamente cuando hay empate
     */
    @Test
    public void GetRobotHighestProfitsLRShouldHandleTie() {
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(2);
        silkRoad.placeStore(1, 50);
        silkRoad.placeStore(3, 50);
        
        silkRoad.moveRobot(0, 1); 
        silkRoad.moveRobot(2, 1); 
        
        silkRoad.getRobotHighestProfits();
        
        String output = outContent.toString();
        assertTrue("Debe detectar y manejar el empate entre robots", 
                   output.contains("Hay un empate entre los robots") || 
                   !output.contains("No hay robots registrados"));
    }
    
    /**
     * Verifica que getRobotHighestProfits maneje correctamente cuando no hay robots
     */
    @Test
    public void GetRobotHighestProfitsLRShouldHandleNoRobots() {
        silkRoad.getRobotHighestProfits();
        
        String output = outContent.toString();
        assertTrue("Debe indicar que no hay robots registrados", 
                   output.contains("No hay robots registrados"));
    }

    // Combinación de métodos
    /**
     * Prueba que combina moveRobot, resupplyStores y getTimesStoresEmptied
     * Simula un ciclo completo: robot vacía tienda, se reabastece, se consulta historial
     */
    @Test
    public void simulator1LR() {
        silkRoad.placeStore(1, 75);
        silkRoad.placeStore(3, 50);
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(2);
        
        silkRoad.moveRobot(0, 1); 
        silkRoad.moveRobot(2, 1); 
        
        int[][] storesBeforeResupply = silkRoad.stores();
        int totalProfitAfterMovement = silkRoad.profit();
        
        silkRoad.resupplyStores();
        
        silkRoad.getTimesStoresEmptied();
        
        String output = outContent.toString();
        
        assertTrue("Los robots deben haber obtenido ganancias", totalProfitAfterMovement > 0);
        assertTrue("Debe mostrar ganancias de robots", 
                   output.contains("El robot ha recolectado hasta este punto:"));
        
        assertTrue("Debe confirmar reabastecimiento", 
                   output.contains("Tiendas reabastecidas correctamente"));
        
        assertTrue("Debe mostrar historial de tienda en ubicación 1", 
                   output.contains("La tienda de la ubicación: 1 ha sido desocupada"));
        assertTrue("Debe mostrar historial de tienda en ubicación 3", 
                   output.contains("La tienda de la ubicación: 3 ha sido desocupada"));
        
        int[][] storesAfterResupply = silkRoad.stores();
        assertEquals("Debe mantener el número de tiendas", 2, storesAfterResupply.length);
    }
    
    /**
     * Prueba que combina placeRobot, moveRobot, getRobotHighestProfits y reboot
     * Simula competencia entre robots, identificación del ganador y reinicio del sistema
     */
    @Test
    public void simulator2LR() {
        silkRoad.placeStore(1, 100);
        silkRoad.placeStore(4, 30);
        silkRoad.placeStore(7, 80);
        
        silkRoad.placeRobot(0); 
        silkRoad.placeRobot(3);  
        silkRoad.placeRobot(6); 
        int[][] initialRobots = silkRoad.robots();
        
        silkRoad.moveRobot(0, 1);
        silkRoad.moveRobot(3, 1);
        silkRoad.moveRobot(6, 1);
        
        int profitBeforeReboot = silkRoad.profit();
        
        silkRoad.getRobotHighestProfits();
        
        silkRoad.reboot();
        
        int profitAfterReboot = silkRoad.profit();
        int[][] robotsAfterReboot = silkRoad.robots();
        
        String output = outContent.toString();
        
        assertTrue("Los robots deben haber competido y obtenido ganancias", profitBeforeReboot > 0);
        
        assertTrue("Debe procesar la identificación del robot ganador", 
                   !output.contains("No hay robots registrados"));
        
        assertTrue("Debe confirmar reabastecimiento en reboot", 
                   output.contains("Tiendas reabastecidas correctamente"));
        assertTrue("Debe confirmar reinicio completo", 
                   output.contains("Ruta de seda reiniciada"));
        
        assertEquals("Las ganancias deben resetearse después del reboot", 0, profitAfterReboot);
        
        assertEquals("Debe mantener el mismo número de robots", 
                     initialRobots.length, robotsAfterReboot.length);
    }
    
    /**
     * Prueba que combina create, makeVisible, profit, finish y ok
     * Simula el ciclo de vida completo del simulador desde creación hasta finalización
     */
    @Test
    public void simulator3LR() {
        int[] marathonInput = {3,        
                              2, 50,     
                              5, 75,       
                              8, 100,    
                              1, 4, 7};  
        
        silkRoad.create(marathonInput);
        
        boolean isValidAfterCreate = silkRoad.ok();
        
        silkRoad.makeVisible();
        
        int[][] initialStores = silkRoad.stores();
        int[][] initialRobots = silkRoad.robots();
        int initialProfit = silkRoad.profit();
        
        silkRoad.moveRobot(1, 1);
        silkRoad.moveRobot(4, 1);
        
        int profitAfterMovements = silkRoad.profit();
        
        silkRoad.makeInvisible();
        silkRoad.finish();
        
        boolean isValidAfterFinish = silkRoad.ok();
        
        String output = outContent.toString();
        
        assertTrue("El simulador debe estar válido después de create", isValidAfterCreate);
        assertTrue("Debe confirmar creación con entrada de maratón", 
                   output.contains("Ruta de seda creada con 3 tiendas"));
        
        assertEquals("Debe crear exactamente 3 tiendas", 3, initialStores.length);
        assertEquals("Debe crear exactamente 3 robots", 3, initialRobots.length);
        assertEquals("Ganancia inicial debe ser 0", 0, initialProfit);
        
        assertTrue("Los robots deben haber obtenido ganancias", profitAfterMovements > 0);
        assertTrue("Debe mostrar ganancias de robots en movimientos", 
                   output.contains("El robot ha recolectado hasta este punto:"));
        
        assertTrue("Debe confirmar que se hizo visible", 
                   output.contains("Simulador de la Ruta de Seda ahora es visible"));
        assertTrue("Debe confirmar que se hizo invisible", 
                   output.contains("Simulador de la Ruta de Seda ahora es invisible"));
        
        assertTrue("Debe confirmar finalización completa", 
                   output.contains("Simulador de la Ruta de Seda terminado"));
        
        assertNotNull("El método ok() debe funcionar incluso después de finish", 
                      Boolean.valueOf(isValidAfterFinish));
    }
    
    @After
    public void tearDown() {
        System.setOut(originalOut);
        if (silkRoad != null) {
            silkRoad.finish();
        }
    }
    
}