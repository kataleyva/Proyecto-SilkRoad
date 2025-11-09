package test;
import silkroad.SilkRoad;
import silkroad.Robot;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * The test class SilkRoadC4Test.
 *
 * @author  Maria Katalina Leyva Díaz y Michelle Dayana Ruiz Carranza
 * @version 8.11.2025
 */
public class SilkRoadC4Test
{
    private SilkRoad silkRoad;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    /**
     * Default constructor for test class SilkRoadC4Test
     */
    public SilkRoadC4Test()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        silkRoad = new SilkRoad(20);
        silkRoad.makeInvisible();
    }
    
    //placeRobot
    
    @Test
    public void placeRobotLRShouldPlaceRobotsWithDifferentColors() {
        silkRoad.placeRobot("Tender", 0);     // azul
        silkRoad.placeRobot("neverBack", 6);  // verde
        silkRoad.placeRobot(4);               // otro color

        int[][] robots = silkRoad.robots();

        assertEquals(3, robots.length);
    }
    
    @Test
    public void placeRobotLRShouldIdentifyTheRobotsInEachPosition(){
        silkRoad.placeRobot("Tender", 0);   
        silkRoad.placeRobot("neverBack", 6);  
        silkRoad.placeRobot(4);               
    
        int[][] robots = silkRoad.robots();
    
        assertEquals(0, robots[0][0]);
        assertEquals(4, robots[1][0]);
        assertEquals(6, robots[2][0]);
    }

    //robots move correctly
    
    @Test
    public void moveRobotLRTenderRobotShouldTakeOnlyTheHalfOfTheStoreProfit(){
        silkRoad.placeRobot("Tender", 0);
        silkRoad.placeStore(3, 100); 
        silkRoad.moveRobot(0, 3);
        int profit = silkRoad.profit();
        assertEquals(47, profit); 
    }
    
    @Test
    public void moveRobotLRNeverBackRobotShouldTakeAllOfTheStoreProfit(){
        silkRoad.placeRobot("neverBack", 0);
        silkRoad.placeRobot("Tender", 10);
        silkRoad.placeStore(3, 100); 
        silkRoad.moveRobots();
        int profit = silkRoad.profit();
        assertEquals(97, profit); 
    }    
    
    @Test
    public void returnRobotsLRShouldNotCameBackNeverBackRobotsToTheInitialPosition() {
        silkRoad.placeRobot("neverBack", 0);
        silkRoad.placeRobot("Tender", 10);
        silkRoad.placeStore(3, 100);
        silkRoad.moveRobots();
        int[][] robotsInfo = silkRoad.robots();
        // El neverBack empezó en 0, así que buscamos el robot que esté en posición 3 (nuevo lugar)
        int neverBackPosition = robotsInfo[0][0]; // primer robot: el que se puso en 0    
        assertEquals(3, neverBackPosition);
    }
    
    //placeStore
    @Test
    public void placeStoreLRShouldPlaceNormalStoreCorrectly() {
        silkRoad.placeStore("normal", 5, 100);
        int[][] stores = silkRoad.stores();
        
        assertEquals(1, stores.length);
        assertEquals(5, stores[0][0]);
        assertEquals(100, stores[0][1]);
    }

    @Test
    public void placeStoreLRShouldPlaceFighterStoreWithDefense() {
        silkRoad.placeStore("fighter", 3, 150);
        int[][] stores = silkRoad.stores();
        
        assertEquals(1, stores.length);
        assertEquals(3, stores[0][0]);
        assertEquals(150, stores[0][1]);
    }

    @Test
    public void placeStoreLRShouldPlaceBonusStoreWithExtraTenges() {
        silkRoad.placeStore("bonus", 7, 80);
        int[][] stores = silkRoad.stores();
        
        assertEquals(1, stores.length);
        assertEquals(7, stores[0][0]);
        assertEquals(80, stores[0][1]);
    }

    @Test
    public void placeStoreLRShouldPlaceAutonomousStoreInDifferentLocation() {
        silkRoad.placeStore("autonomous", 10, 120);
        int[][] stores = silkRoad.stores();
        assertEquals(1, stores.length);
        // AutonomousStore podría colocarse en una posición diferente
        assertTrue(stores[0][0] >= 8 && stores[0][0] <= 12); // Cerca de la posición 10
        assertEquals(120, stores[0][1]);
    }
    
     @Test
    public void placeStoreLRFighterStoreShouldDefendFromPoorRobots() {
        silkRoad.placeStore("fighter", 5, 200);
        silkRoad.placeRobot(0); // Robot pobre (0 tenges)
        silkRoad.moveRobot(0, 5); // Intenta saquear la tienda fighter
        int profit = silkRoad.profit();
        // El robot no debería poder saquear porque tiene menos de 200 tenges
        assertEquals(0, profit); // O un valor negativo por el movimiento
    }

    @Test
    public void placeStoreLRBonusStoreShouldGiveExtraTenges() {
        silkRoad.placeStore("bonus", 3, 100);
        silkRoad.placeRobot(0);
        
        silkRoad.moveRobot(0, 3);
        int profit = silkRoad.profit();
        
        // BonusStore da 50% extra: 100 * 1.5 = 150 - 3 de distancia = 147
        assertEquals(147, profit);
    }

    @Test
    public void placeStoreLRAutonomousStoreShouldChooseDifferentPosition() {
        // Colocar AutonomousStore en posición 8
        silkRoad.placeStore("autonomous", 8, 100);
        int[][] stores = silkRoad.stores();
        int actualPosition = stores[0][0];
        
        // Verificar que AutonomousStore no siempre usa la posición exacta
        // (puede ser la misma o diferente, pero el comportamiento debe ser consistente)
        assertTrue(actualPosition >= 6 && actualPosition <= 10);
    }
    @Test
    public void placeStoreLRShouldNotAllowDuplicateStores() {
        silkRoad.placeStore("normal", 5, 100);
        silkRoad.placeStore("fighter", 5, 150); // Misma posición
        
        int[][] stores = silkRoad.stores();
        // Solo debería haber una tienda en posición 5
        assertEquals(1, stores.length);
        assertEquals(5, stores[0][0]);
    }

    @Test
    public void placeStoreLRShouldNotAllowStoresOnOccupiedRobotPositions() {
        silkRoad.placeRobot(4);
        silkRoad.placeStore("normal", 4, 100); // Misma posición que robot
        
        int[][] stores = silkRoad.stores();
        // No debería permitir colocar tienda donde hay robot
        assertEquals(0, stores.length);
    }

    @Test
    public void placeStoreLRShouldHandleInvalidPositions() {
        silkRoad.placeStore("normal", -1, 100); // Posición inválida
        silkRoad.placeStore("fighter", 20, 150); // Posición fuera de rango (lenRoad=15)
        
        int[][] stores = silkRoad.stores();
        // No debería colocar tiendas en posiciones inválidas
        assertEquals(0, stores.length);
    }

    @Test
    public void placeStoreLRShouldHandleUnknownStoreType() {
        silkRoad.placeStore("unknown", 5, 100); // Tipo desconocido
        
        int[][] stores = silkRoad.stores();
        // Debería usar el tipo por defecto (normal)
        assertEquals(1, stores.length);
        assertEquals(5, stores[0][0]);
        assertEquals(100, stores[0][1]);
    }
    
     @Test
    public void placeStoreLRDifferentStoreTypesShouldHaveDifferentBehaviors() {
        silkRoad.placeStore("fighter", 3, 200);
        silkRoad.placeStore("bonus", 6, 100);
        silkRoad.placeRobot(0);
        silkRoad.getRobot(0).setTenge(250);
        
        // Mover a fighter store (debería poder saquear)
        silkRoad.moveRobot(0, 3);
        int profitAfterFighter = silkRoad.profit();
        
        // Mover a bonus store (debería dar bonus)
        silkRoad.moveRobot(3, 3);
        int profitAfterBonus = silkRoad.profit();
        
        // El profit después del bonus debería ser mayor
        assertTrue(profitAfterBonus > profitAfterFighter);
    }
    
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        System.setOut(originalOut);
        if (silkRoad != null) {
            silkRoad.finish();
            //silkRoad.makeInvisible();
        }
    }
}