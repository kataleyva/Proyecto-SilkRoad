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
    private SilkRoad silkroad;
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
        silkroad = new SilkRoad(20);
        silkroad.makeInvisible();
    }
    
    //placeRobot
    
    @Test
    public void placeRobotLRShouldPlaceRobotsWithDifferentColors() {
        silkroad.placeRobot("Tender", 0);     // azul
        silkroad.placeRobot("neverBack", 6);  // verde
        silkroad.placeRobot(4);               // otro color

        int[][] robots = silkroad.robots();

        assertEquals(3, robots.length);
    }
    
    @Test
    public void placeRobotLRShouldIdentifyTheRobotsInEachPosition(){
        silkroad.placeRobot("Tender", 0);   
        silkroad.placeRobot("neverBack", 6);  
        silkroad.placeRobot(4);               
    
        int[][] robots = silkroad.robots();
    
        assertEquals(0, robots[0][0]);
        assertEquals(4, robots[1][0]);
        assertEquals(6, robots[2][0]);
    }

    //robots move correctly
    
    @Test
    public void moveRobotLRTenderRobotShouldTakeOnlyTheHalfOfTheStoreProfit(){
        silkroad.placeRobot("Tender", 0);
        silkroad.placeStore(3, 100); 
        silkroad.moveRobot(0, 3);
        int profit = silkroad.profit();
        assertEquals(47, profit); 
    }
    
    @Test
    public void moveRobotLRNeverBackRobotShouldTakeAllOfTheStoreProfit(){
        silkroad.placeRobot("neverBack", 0);
        silkroad.placeRobot("Tender", 10);
        silkroad.placeStore(3, 100); 
        silkroad.moveRobots();
        int profit = silkroad.profit();
        assertEquals(97, profit); 
    }    
    
    @Test
    public void returnRobotsLRShouldNotCameBackNeverBackRobotsToTheInitialPosition() {
        silkroad.placeRobot("neverBack", 0);
        silkroad.placeRobot("Tender", 10);
        silkroad.placeStore(3, 100);
        silkroad.moveRobots();
        int[][] robotsInfo = silkroad.robots();
        // El neverBack empezó en 0, así que buscamos el robot que esté en posición 3 (nuevo lugar)
        int neverBackPosition = robotsInfo[0][0]; // primer robot: el que se puso en 0    
        assertEquals(3, neverBackPosition);
    }
    
    //placeStore
    @Test
    public void placeStoreLRShouldPlaceNormalStoreCorrectly() {
        silkroad.placeStore("normal", 5, 100);
        int[][] stores = silkroad.stores();
        
        assertEquals(1, stores.length);
        assertEquals(5, stores[0][0]);
        assertEquals(100, stores[0][1]);
    }

    @Test
    public void placeStoreLRShouldPlaceFighterStoreWithDefense() {
        silkroad.placeStore("fighter", 3, 150);
        int[][] stores = silkroad.stores();
        
        assertEquals(1, stores.length);
        assertEquals(3, stores[0][0]);
        assertEquals(150, stores[0][1]);
    }

    @Test
    public void placeStoreLRShouldPlaceBonusStoreWithExtraTenges() {
        silkroad.placeStore("bonus", 7, 80);
        int[][] stores = silkroad.stores();
        
        assertEquals(1, stores.length);
        assertEquals(7, stores[0][0]);
        assertEquals(80, stores[0][1]);
    }

    @Test
    public void placeStoreLRShouldPlaceAutonomousStoreInDifferentLocation() {
        silkroad.placeStore("autonomous", 10, 120);
        int[][] stores = silkroad.stores();
        assertEquals(1, stores.length);
        // AutonomousStore podría colocarse en una posición diferente
        assertTrue(stores[0][0] >= 8 && stores[0][0] <= 12); // Cerca de la posición 10
        assertEquals(120, stores[0][1]);
    }

    @Test
    public void placeStoreLRAutonomousStoreShouldChooseDifferentPosition() {
        // Colocar AutonomousStore en posición 8
        silkroad.placeStore("autonomous", 8, 100);
        int[][] stores = silkroad.stores();
        int actualPosition = stores[0][0];
        
        // Verificar que AutonomousStore no siempre usa la posición exacta
        // (puede ser la misma o diferente, pero el comportamiento debe ser consistente)
        assertTrue(actualPosition >= 6 && actualPosition <= 10);
    }
    @Test
    public void placeStoreLRShouldNotAllowDuplicateStores() {
        silkroad.placeStore("normal", 5, 100);
        silkroad.placeStore("fighter", 5, 150); // Misma posición
        
        int[][] stores = silkroad.stores();
        // Solo debería haber una tienda en posición 5
        assertEquals(1, stores.length);
        assertEquals(5, stores[0][0]);
    }

    @Test
    public void placeStoreLRShouldNotAllowStoresOnOccupiedRobotPositions() {
        silkroad.placeRobot(4);
        silkroad.placeStore("normal", 4, 100); // Misma posición que robot
        
        int[][] stores = silkroad.stores();
        // No debería permitir colocar tienda donde hay robot
        assertEquals(0, stores.length);
    }

    @Test
    public void placeStoreLRShouldHandleInvalidPositions() {
        silkroad.placeStore("normal", -1, 100); // Posición inválida
        silkroad.placeStore("fighter", 20, 150); // Posición fuera de rango (lenRoad=15)
        
        int[][] stores = silkroad.stores();
        // No debería colocar tiendas en posiciones inválidas
        assertEquals(0, stores.length);
    }

    @Test
    public void placeStoreLRShouldHandleUnknownStoreType() {
        silkroad.placeStore("unknown", 5, 100); // Tipo desconocido
        
        int[][] stores = silkroad.stores();
        // Debería usar el tipo por defecto (normal)
        assertEquals(1, stores.length);
        assertEquals(5, stores[0][0]);
        assertEquals(100, stores[0][1]);
    }
    
     @Test
    public void placeStoreLRDifferentStoreTypesShouldHaveDifferentBehaviors() {
        silkroad.placeStore("fighter", 3, 200);
        silkroad.placeStore("bonus", 6, 100);
        silkroad.placeRobot(0);
        silkroad.getRobot(0).setTenge(250);
        
        // Mover a fighter store (debería poder saquear)
        silkroad.moveRobot(0, 3);
        int profitAfterFighter = silkroad.profit();
        
        // Mover a bonus store (debería dar bonus)
        silkroad.moveRobot(3, 3);
        int profitAfterBonus = silkroad.profit();
        
        // El profit después del bonus debería ser mayor
        assertTrue(profitAfterBonus > profitAfterFighter);
    }
        @Test
    public void placeStoreLRFighterStoreShouldDefendFromPoorRobots() {
        silkroad.placeStore("fighter", 5, 200);
        silkroad.placeRobot(0); // Robot pobre (0 tenges)
        silkroad.moveRobotInFighter(0, 5); // Intenta saquear la tienda fighter
        int profit = silkroad.profit();
        // El robot no debería poder saquear porque tiene menos de 200 tenges
        // Profit = 0 (no saqueo) - 5 (distancia) = -5
        assertEquals(-5, profit);
    }

    @Test
    public void placeStoreLRBonusStoreShouldGiveExtraTenges() {
        silkroad.placeStore("bonus", 3, 100);
        silkroad.placeRobot(0);
        silkroad.moveRobotInFighter(0, 3);
        int profit = silkroad.profit();
        // BonusStore da 50% extra: 100 * 1.5 = 150 - 3 de distancia = 147
        assertEquals(147, profit);
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
        if (silkroad != null) {
            silkroad.finish();
            //silkRoad.makeInvisible();
        }
    }
}