package test;
import silkroad.SilkRoad;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Clase de pruebas unitarias para SilkRoad
 * Prueba los métodos principales del simulador de la Ruta de la Seda
 * 
 * @author [Nombres]
 * @version 1.0
 */
public class SilkRoadTest {
    private SilkRoad silkRoad;
    
    /**
     * Configuración inicial antes de cada prueba
     */
    @Before
    public void setUp() {
        silkRoad = new SilkRoad(100); 
    }
    
    // Constructor
    
    @Test
    public void constructorLRshouldCreateRouteWithCorrectLength() {
        SilkRoad ruta = new SilkRoad(50);
        assertEquals(50, ruta.getLength());
    }
    
    @Test
    public void constructorLRshouldCreateRouteWithoutStoresInitially() {
        SilkRoad ruta = new SilkRoad(100);
        assertEquals(0, ruta.getNumberOfStores());
    }
    
    @Test
    public void constructorLRshouldCreateRouteWithoutRobotsInitially() {
        SilkRoad ruta = new SilkRoad(100);
        assertEquals(0, ruta.getNumberOfRobots());
    }
    
    // Place/removeStore
    
    @Test
    public void placeStoreLRshouldAddStoreCorrectly() {
        silkRoad.placeStore(10, 500);
        assertEquals(1, silkRoad.getNumberOfStores());
    }
    
    @Test
    public void removeStoreLRshouldRemoveExistingStore() {
        silkRoad.placeStore(10, 500);
        silkRoad.removeStore(10);
        assertEquals(0, silkRoad.getNumberOfStores());
    }
    
    @Test
    public void removeStoreLRshouldNotRemoveNonExistentStore() {
        silkRoad.placeStore(10, 500);
        silkRoad.removeStore(10);
        assertEquals(0, silkRoad.getNumberOfStores());
    }
    
    // ressuply stores
    
    @Test
    public void resupplyStoresLRshouldRestoreStoresToInitialAmount() {
        silkRoad.placeStore(10, 500);
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 10);
        silkRoad.resupplyStores();
        assertEquals(500, silkRoad.getStoreAmount(10));
    }
    
    @Test
    public void resupplyStoresLRshouldRestoreMultipleStores() {
        silkRoad.placeStore(10, 500);
        silkRoad.placeStore(20, 300);
        silkRoad.resupplyStores();
        assertEquals(500, silkRoad.getStoreAmount(10));
        assertEquals(300, silkRoad.getStoreAmount(20));
    }
    
    // place/remove robot
    
    @Test
    public void placeRobotLRshouldAddRobotCorrectly() {
        silkRoad.placeRobot(0);
        assertEquals(1, silkRoad.getNumberOfRobots());
    }
    
    @Test
    public void removeRobotLRshouldRemoveExistingRobot() {
        silkRoad.placeRobot(0);
        silkRoad.removeRobot(0);
        assertEquals(0, silkRoad.getNumberOfRobots());
    }
    
    @Test
    public void removeRobotLRshouldNotRemoveNonExistentRobot() {
        silkRoad.placeRobot(0);
        silkRoad.removeRobot(0);
        assertEquals(0, silkRoad.getNumberOfRobots());
    }
    
    // return robots
    
    @Test
    public void returnRobotsLRshouldReturnRobotToInitialPosition() {
        silkRoad.placeRobot(5);
        silkRoad.moveRobot(5, 20);
        silkRoad.returnRobots();
        assertEquals(5, silkRoad.robots()[0][0]);
    }
    
    @Test
    public void returnRobotsLRshouldReturnMultipleRobotsToInitialPositions() {
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(10);
        silkRoad.moveRobot(0, 30);
        silkRoad.moveRobot(10, 40);
        silkRoad.returnRobots();
        assertEquals(0, silkRoad.robots()[0][0]);
        assertEquals(10, silkRoad.robots()[1][0]);
    }
    
    @Test
    public void returnRobotsLRshouldMaintainRobotCount() {
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(10);
        silkRoad.moveRobot(0, 30);
        silkRoad.returnRobots();
        assertEquals(2, silkRoad.getNumberOfRobots());
    }

    
    // move Robot
    
    @Test
    public void moveRobotLRshouldMoveRobotToDestinationPosition() {
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 25);
        assertEquals(25, silkRoad.robots()[0][0]);
    }
    
    @Test
    public void moveRobotLRshouldDeductAmountWhenPassingThroughStore() {
        silkRoad.placeStore(10, 500);
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 10);
        assertEquals(0, silkRoad.getStoreAmount(10));
    }
    
    @Test
    public void moveRobotLRshouldIncreaseProfitWhenLootingStore() {
        silkRoad.placeStore(10, 500);
        silkRoad.placeRobot(0);
        int gananciaInicial = silkRoad.profit();
        silkRoad.moveRobot(0, 10);
        assertEquals(gananciaInicial + 490, silkRoad.profit());
    }
    
    // reboot
    
    @Test
    public void rebootLRshouldRestoreStoresToOriginalState() {
        silkRoad.placeStore(10, 500);
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 10);
        silkRoad.reboot();
        assertEquals(500, silkRoad.getStoreAmount(10));
    }
    
    @Test
    public void rebootLRshouldResetProfitToZero() {
        silkRoad.placeStore(10, 500);
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 10);
        silkRoad.reboot();
        assertEquals(0, silkRoad.profit());
    }
    
    // profit
    
    @Test
    public void profitLRshouldReturnZeroInitially() {
        assertEquals(0, silkRoad.profit());
    }
    
    @Test
    public void profitLRshouldCalculateProfitCorrectly() {
        silkRoad.placeStore(10, 500);
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 10);
        assertEquals(490, silkRoad.profit());
    }
    
    @Test
    public void profitLRshouldAccumulateProfitsFromMultipleStores() {
        silkRoad.placeStore(10, 500);
        silkRoad.placeStore(30, 400);
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 10);
        silkRoad.moveRobot(10, 20);
        assertEquals(870, silkRoad.profit());
    }
}