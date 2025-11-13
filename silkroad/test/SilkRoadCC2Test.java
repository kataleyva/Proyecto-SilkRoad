package test;
import silkroad.SilkRoad;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SilkRoadCC2Test {

    private SilkRoad silkRoad;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    /**
    * Verifica que showRobotProfits muestre correctamente las ganancias de un robot e identifica bugs.
    */
     @Test
     public void ShowRobotProfitsLRShouldDisplayCorrectProfits()  {
         SilkRoad silkRoad = new SilkRoad(20);
         silkRoad.placeRobot(0);
         silkRoad.placeStore(1, 50);
         silkRoad.placeStore(3, 100);

         silkRoad.moveRobot(0, 1);
         silkRoad.moveRobot(1, 2);
         
         assertEquals(147, silkRoad.profit());
     }
}