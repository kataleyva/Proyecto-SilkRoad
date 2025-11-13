package test;
import silkroad.SilkRoadContest;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class SilkRoadCTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SilkRoadCTest
{

    /**
     * Verifica que solve() devuelva un array vacío si la entrada es null.
     */
    @Test
    public void solveLRshouldReturnEmptyWithNullImput() {
        int[] resultado = SilkRoadContest.solve(null);
        assertEquals(0, resultado.length, 
            "Debe devolver un arreglo vacío si la entrada es null");
    }
    
    /**
     * Verifica que solve() maneje un array vacío correctamente.
     */
    @Test
    public void solveLRshouldReturnArrayEmptyWithEmptyArray() {
        int[] resultado = SilkRoadContest.solve(new int[0][]);
        assertEquals(0, resultado.length, 
            "Debe devolver un arreglo vacío si no hay días");
    }
}