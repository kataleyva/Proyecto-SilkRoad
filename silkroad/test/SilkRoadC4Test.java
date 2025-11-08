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