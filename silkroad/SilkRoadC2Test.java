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
        silkRoad.makeVisible();
        System.setOut(new PrintStream(outContent));
    }
    
    //resupplyStores()
    
    /**
     * Verifica que resupplyStores restaure correctamente las tiendas vacías
     */
    @Test
    public void resupplyStoresLRShouldRestoreEmptyStores() {
        silkRoad.placeStore(0, 100);
        silkRoad.placeStore(1, 50);
    
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 1);
    
        silkRoad.getStore(0).setTenge(0);
        silkRoad.getStore(1).setTenge(0);
    
        int initialTenge0 = silkRoad.getStore(0).getInitialTenge();
        int initialTenge1 = silkRoad.getStore(1).getInitialTenge();
        
        silkRoad.resupplyStores();
    
        assertEquals(initialTenge0, silkRoad.getStore(0).getTenge());
    
        assertEquals(initialTenge1, silkRoad.getStore(1).getTenge());
    
        assertTrue(silkRoad.getStore(0).getTenge() > 0 &&
                   silkRoad.getStore(1).getTenge() > 0);
    }
    
    /**
     * Verifica que resupplyStores no afecte tiendas que ya tienen tenges
     */
    @Test
    public void resupplyStoresLRShouldOnlyAffectEmptyStores() {
        // Arrange
    
        silkRoad.placeStore(0, 100);
        silkRoad.placeStore(3, 75);
        silkRoad.placeStore(5, 40);
    
        silkRoad.placeRobot(2);
        silkRoad.placeRobot(4);
    
        silkRoad.getStore(3).setTenge(0);
        silkRoad.getStore(5).setTenge(0);
    
        int tengeBefore0 = silkRoad.getStore(0).getTenge();
        int tengeBefore3 = silkRoad.getStore(3).getTenge();
        int tengeBefore5 = silkRoad.getStore(5).getTenge();
    
        int initialTenge3 = silkRoad.getStore(3).getInitialTenge();
        int initialTenge5 = silkRoad.getStore(5).getInitialTenge();
    
        silkRoad.resupplyStores();
    
        assertEquals(tengeBefore0, silkRoad.getStore(0).getTenge());
    
        assertEquals(initialTenge3, silkRoad.getStore(3).getTenge());
        assertEquals(initialTenge5, silkRoad.getStore(5).getTenge());
    
        assertNotEquals(tengeBefore3, silkRoad.getStore(3).getTenge());
        assertNotEquals(tengeBefore5, silkRoad.getStore(5).getTenge());
    }

    // profit
    
    /**
     * Verifica que showRobotProfits muestre correctamente las ganancias de un robot e identifica bugs.
     */
    @Test
    public void profitLRShouldDisplayCorrectProfits()  {
        silkRoad.placeRobot(0);
        silkRoad.placeStore(1, 50);
        silkRoad.placeStore(3, 100);
 
        silkRoad.moveRobot(0, 1);
        silkRoad.moveRobot(1, 2);
        
        assertEquals(150, silkRoad.profit());
    }
    
    /**
     * Verifica que Profit no se ejecute cuando no hay robots
     */
    @Test
    public void profitLRShouldReturnZero_WhenNoRobots() {
        // Arrange
        SilkRoad silkRoad = new SilkRoad(10);
    
        // Act
        int totalProfit = silkRoad.profit();
    
        // Assert
        assertEquals( 0, totalProfit);
    }

    @Test
    public void profitLRShouldSumAllRobotGains() {
        SilkRoad silkRoad = new SilkRoad(10);
        silkRoad.placeRobot(0);   // Robot en posición 0 → índice 0 en ArrayList
        silkRoad.placeRobot(3);   // Robot en posición 3 → índice 1 en ArrayList
        silkRoad.placeRobot(5);   // Robot en posición 5 → índice 2 en ArrayList
    
        // ✓ Estos accesos están correctos
        silkRoad.getRobot(0).setTenge(50);  // Accede al robot índice 0
        silkRoad.getRobot(1).setTenge(30);  // Accede al robot índice 1
        silkRoad.getRobot(2).setTenge(20);  // Accede al robot índice 2
    
        int totalProfit = silkRoad.profit();
    
        assertEquals(100, totalProfit);
    }

    // emptiedStore()
    
    /**
     * Verifica que getTimesStoresEmptied muestre correctamente las veces que cada tienda ha sido vaciada
     */
    @Test
    public void emptiedStoresLRShouldIncrementOnlyForEmptyStores() {
        SilkRoad silkRoad = new SilkRoad(10);
        silkRoad.placeStore(0, 30);
        silkRoad.placeStore(2, 40);
        
        // Coloca un robot y haz que vacíe la tienda en posición 0
        silkRoad.placeRobot(0);  // Robot en posición 0
        
        int before0 = silkRoad.getStore(0).getTimesEmpty();
        int before2 = silkRoad.getStore(2).getTimesEmpty();
        
        // El robot está en 0, se queda ahí (movimiento de 0 metros) pero NO recoge nada
        // Mejor: movemos el robot para que recoja de la tienda 0
        // Como el robot YA está en 0, debemos moverlo primero a otro lado y luego regresar
        // O simplemente colocarlo en otra posición inicial
        
        // Mejor enfoque: el robot empieza en otra posición
        silkRoad.placeRobot(1);
        silkRoad.moveRobot(1, -1); // Mueve el robot de pos 1 a pos 0, recoge los 30 tenges
        
        int[][] emptyStores = silkRoad.emptiedStores();
        
        // Ahora la tienda 0 debe tener timesEmpty incrementado
        assertTrue(silkRoad.getStore(0).getTimesEmpty() > before0);
        // La tienda 2 no debe haber cambiado
        assertEquals(before2, silkRoad.getStore(2).getTimesEmpty());
    }
    
    @Test
    public void emptiedStores_ShouldNotAffectStoresWithTenge() {
        // Arrange
        silkRoad.placeStore(2, 40);
        silkRoad.placeStore(4, 80);
    
        int before2 = silkRoad.getStore(2).getTimesEmpty();
        int before4 = silkRoad.getStore(4).getTimesEmpty();
    
        // Act
        silkRoad.emptiedStores();
    
        // Assert
        assertEquals(before2, silkRoad.getStore(2).getTimesEmpty());
        assertEquals(before4, silkRoad.getStore(4).getTimesEmpty());
    }
    
    @Test
    public void emptiedStores_ShouldNotFailWithNoStores() {
        // Arrange — no se agregan tiendas
    
        // Act
        silkRoad.emptiedStores();
        
        // Assert
        assertEquals(0, silkRoad.stores().length);
    }
    
    //PlaceStore-RemoveStore
    
     @Test
    public void PlaceStore() {
        silkRoad.placeStore(2, 100);
        int[][] stores = silkRoad.stores();
        
        assertEquals(1, stores.length);
        assertEquals(2, stores[0][0]);
        assertEquals(100, stores[0][1]);
    }
    
    @Test
    public void testPlaceStoreDuplicate() {
        silkRoad.placeStore(2, 100);
        silkRoad.placeStore(2, 200);
        
        int[][] stores = silkRoad.stores();
        assertEquals(1, stores.length);
    }
    
    @Test
    public void testRemoveStore() {
        silkRoad.placeStore(2, 100);
        silkRoad.removeStore(2);
        
        int[][] stores = silkRoad.stores();
        assertEquals(0, stores.length);
    }
    
    @Test
    public void testRemoveNonExistentStore() {
        silkRoad.removeStore(5);
        assertTrue(true);
    }
    
    //PlaceRobot-RemoveRobot
     
    @Test
    public void testPlaceRobot() {
        silkRoad.placeRobot(3);
        int[][] robots = silkRoad.robots();
        
        assertEquals(1, robots.length);
        assertEquals(3, robots[0][0]);
    }
    
    @Test
    public void testPlaceRobotDuplicate() {
        silkRoad.placeRobot(3);
        silkRoad.placeRobot(3);
        
        int[][] robots = silkRoad.robots();
        assertEquals(1, robots.length);
    }
    
    @Test
    public void testRemoveRobot() {
        silkRoad.placeRobot(3);
        silkRoad.removeRobot(3);
        
        int[][] robots = silkRoad.robots();
        assertEquals(0, robots.length);
    }
    
    @Test
    public void testRemoveNonExistentRobot() {
        silkRoad.removeRobot(5);
        assertTrue(true);
    }
    
    //Reboot
    @Test
    public void testReboot() {
        silkRoad.placeStore(1, 100);
        silkRoad.placeStore(3, 50);
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(2);
        silkRoad.moveRobot(0, 1);
        silkRoad.moveRobot(2, 1);
        
        int profitBefore = silkRoad.profit();
        silkRoad.reboot();
        int profitAfter = silkRoad.profit();
        
        assertEquals(0, profitAfter);
    }
    
    //Stores
     @Test
    public void testStores() {
        silkRoad.placeStore(1, 100);
        silkRoad.placeStore(3, 50);
        
        int[][] stores = silkRoad.stores();
        
        assertEquals(2, stores.length);
    }
    
    @Test
    public void testStoresEmpty() {
        int[][] stores = silkRoad.stores();
        assertEquals(0, stores.length);
    }
    //Robots
    
      @Test
    public void testRobots() {
        silkRoad.placeRobot(1);
        silkRoad.placeRobot(3);
        
        int[][] robots = silkRoad.robots();
        
        assertEquals(2, robots.length);

        assertTrue(robots[0][0] <= robots[1][0]);
    }
    
    @Test
    public void testRobotsEmpty() {
        int[][] robots = silkRoad.robots();
        assertEquals(0, robots.length);
    }
    
    //MakeVisible-MakeInvisible
    @Test
    public void testMakeVisible() {
        silkRoad.placeStore(1, 100);
        silkRoad.placeRobot(0);
        
        silkRoad.makeVisible();

        assertTrue(true);
    }
    
    @Test
    public void testMakeInvisible() {
        silkRoad.makeVisible();
        silkRoad.makeInvisible();
        
        assertTrue(true);
    }
 
    //MoveRobot
     @Test
    public void testMoveRobotValid() {
        silkRoad.placeStore(2, 100);
        silkRoad.placeRobot(0);
        
        silkRoad.moveRobot(0, 2);
        
        int[][] robots = silkRoad.robots();
        assertEquals(2, robots[0][0]);
        assertTrue(robots[0][1] > 0);
    }
    
    @Test
    public void testMoveRobotCollectTenges() {
        silkRoad.placeStore(2, 100);
        silkRoad.placeRobot(0);
        
        int profitBefore = silkRoad.profit();
        silkRoad.moveRobot(0, 2);
        int profitAfter = silkRoad.profit();
        
        assertEquals(100, profitAfter);
    }
    
    @Test
    public void testMoveRobotToEmptyStore() {
        silkRoad.placeStore(2, 100);
        silkRoad.placeRobot(0);

        silkRoad.moveRobot(0, 2);
        silkRoad.moveRobot(2, 1);
        
        int profit = silkRoad.profit();
        assertEquals(100, profit);
    }
    
    @Test
    public void testMoveRobotOutOfBounds() {
        silkRoad.placeRobot(0);
        
        silkRoad.moveRobot(0, 15); // Fuera de límites
        
        int[][] robots = silkRoad.robots();
        assertEquals(0, robots[0][0]);
    }
    
    @Test
    public void testMoveNonExistentRobot() {
        silkRoad.moveRobot(5, 2); // Robot que no existe
        // No debe lanzar excepción
        assertTrue(true);
    }
    
    @Test
    public void testMoveRobotZeroMeters() {
        silkRoad.placeRobot(3);
        silkRoad.moveRobot(3, 0); // Movimiento cero
        
        int[][] robots = silkRoad.robots();
        assertEquals(3, robots[0][0]);
    }
    
    @After
    public void tearDown() {
        System.setOut(originalOut);
        if (silkRoad != null) {
            //silkRoad.finish();
            silkRoad.makeInvisible();
        }
    }
    
}