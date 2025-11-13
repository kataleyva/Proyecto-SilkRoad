package test;
import silkroad.SilkRoad;
import silkroad.SilkRoadContest;
import javax.swing.JOptionPane;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class SilkRoadATest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class SilkRoadATest
{
    /**
     * Default constructor for test class SilkRoadATest
     */
    public SilkRoadATest()
    {
    }

    @Test
    /**
     * Prueba de aceptación visual para la clase SilkRoad.
     * 
     * Crea una simulación con tiendas y robots, los mueve y reinicia,
     */
    public void testAceptacion() {
        SilkRoad simulador = new SilkRoad(10);
        simulador.makeVisible();
        JOptionPane.showMessageDialog(null, "Ruta creada con longitud 10.", 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        
        simulador.placeStore(2, 40);
        simulador.placeStore(5, 80);
        simulador.placeStore(8, 30);
        JOptionPane.showMessageDialog(null, 
            "Tiendas colocadas:\n" +
            "Posición 2: 40 tenges\n" +
            "Posición 5: 80 tenges\n" +
            "Posición 8: 30 tenges\n", 
            "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        
        simulador.placeRobot(0);
        simulador.placeRobot(3);
        JOptionPane.showMessageDialog(null, "Robots colocados en posiciones 0 y 3.", 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, 
            "Robots colocados:\n" +
            " Posición 0 \n" +
            " Posición 3\n", 
            "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        
        pause(1500);
        
        simulador.moveRobot(0, 2); 
        pause(1500);
        simulador.moveRobot(3, 2); 
        pause(1500);
        
        JOptionPane.showMessageDialog(null, "Ganancia total hasta ahora: " + simulador.profit(), 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        
        simulador.resupplyStores();
        JOptionPane.showMessageDialog(null, "Tiendas reabastecidas.", 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        pause(1500);
        
        simulador.moveRobots();
        pause(2000);
        
        JOptionPane.showMessageDialog(null, "Ganancia total tras movimiento automático: " + simulador.profit(), 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE); 
        
        simulador.reboot();
        pause(2000);
        
        simulador.makeInvisible();
        JOptionPane.showMessageDialog(null, "FIN DE PRUEBA DE ACEPTACIÓN", 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
    }

    @Test
    /**
     * Prueba de aceptación completa que demuestra el ciclo completo del simulador
     * con visualización gráfica y mensajes en consola.
     */
    public void testAceptacion2() {
        int lenRoad = 20;
        SilkRoad simulador = new SilkRoad(lenRoad);
        simulador.makeVisible();
        JOptionPane.showMessageDialog(null, "Ruta creada con longitud " + lenRoad, 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        simulador.pause(1000);
    
        simulador.placeStore(2, 100);
        simulador.placeStore(5, 75);
        simulador.placeStore(8, 50);
        simulador.placeStore(12, 120);
        JOptionPane.showMessageDialog(null, 
            "Tiendas colocadas:\n" +
            "Posición 2: 100 tenges\n" +
            "Posición 5: 75 tenges\n" +
            "Posición 8: 50 tenges\n" +
            "Posición 12: 120 tenges", 
            "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        simulador.pause(1500);
        
        simulador.placeRobot(0);
        simulador.placeRobot(4);
        simulador.placeRobot(10);
        JOptionPane.showMessageDialog(null, "Robots colocados en posiciones 0, 4 y 10", 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, 
            "Robots colocados:\n" +
            "Posición 0\n" +
            "Posición 4\n" +
            "Posición 10\n" +
            "Posición 12", 
            "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        simulador.pause(2000);
    
        simulador.pause(1000);
        
        simulador.moveRobot(0, 2);
        simulador.pause(1500);
        
        simulador.moveRobot(4, 1);

        simulador.pause(1500);
        
        simulador.moveRobot(10, 2);

        simulador.pause(1500);
        
        int gananciaFase1 = simulador.profit();
        JOptionPane.showMessageDialog(null, "Ganancia después de movimientos manuales: " + gananciaFase1 + " tenges", 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        simulador.pause(2000);
    
        int[][] historial = simulador.emptiedStores();
        StringBuilder historialMsg = new StringBuilder("Historial de tiendas:\n");
        for (int i = 0; i < historial.length; i++) {
            historialMsg.append(" Tienda posición ").append(historial[i][0])
                        .append(": vaciada ").append(historial[i][1]).append(" vez(es)\n");
        }
        JOptionPane.showMessageDialog(null, historialMsg.toString(), 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        pause(2000);
    

        simulador.resupplyStores();
        pause(1500);
        
        int[][] tiendasReabastecidas = simulador.stores();
        StringBuilder tiendasMsg = new StringBuilder("Tiendas reabastecidas:\n");
        for (int i = 0; i < tiendasReabastecidas.length; i++) {
            tiendasMsg.append(" Posición ").append(tiendasReabastecidas[i][0])
                      .append(": ").append(tiendasReabastecidas[i][1]).append(" tenges\n");
        }
        JOptionPane.showMessageDialog(null, tiendasMsg.toString(), 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        pause(2000);
    
        int profitAntes = simulador.profit();
        JOptionPane.showMessageDialog(null, 
            "Ganancia antes del movimiento: " + profitAntes + " tenges", 
            "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        pause(1500);
        
        simulador.moveRobots();
        simulador.pause(2000);
        
        int profitDespues = simulador.profit();
        int gananciaObtenida = profitDespues - profitAntes;
        JOptionPane.showMessageDialog(null, 
            "Robots movidos automáticamente\n" +
            "Ganancia obtenida: " + gananciaObtenida + " tenges\n" +
            "Ganancia total acumulada: " + profitDespues + " tenges", 
            "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        pause(2000);
        
        int[][] robotsInfo = simulador.robots();
        StringBuilder robotsMsg = new StringBuilder("Información de robots:\n");
        for (int i = 0; i < robotsInfo.length; i++) {
            robotsMsg.append(" Robot en posición ").append(robotsInfo[i][0])
                     .append(" con ").append(robotsInfo[i][1]).append(" tenges acumulados\n");
        }
        JOptionPane.showMessageDialog(null, robotsMsg.toString(), 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        pause(2000);
        
        int[][] profitsPorMov = simulador.profitPerMove();
        StringBuilder movimientosMsg = new StringBuilder("Historial de movimientos por robot:\n");
        for (int i = 0; i < profitsPorMov.length; i++) {
            movimientosMsg.append("Robot pos ").append(profitsPorMov[i][0]).append(" Movimientos: [");
            boolean primero = true;
            for (int j = 1; j < profitsPorMov[i].length; j++) {
                if (profitsPorMov[i][j] > 0) {
                    if (!primero) movimientosMsg.append(", ");
                    movimientosMsg.append(profitsPorMov[i][j]);
                    primero = false;
                }
            }
            movimientosMsg.append("]\n");
        }
        
        JOptionPane.showMessageDialog(null, movimientosMsg.toString(), 
                                       "Prueba de Aceptación", JOptionPane.INFORMATION_MESSAGE);
        simulador.pause(2000);
        
        simulador.reboot();
        simulador.pause(1500);
        
        int gananciaFinal = simulador.profit();

        pause(2000);
        
        simulador.makeInvisible();
    }

    @Test
    public void SilkRoadATest() {
        int lenRoad = 20;
        SilkRoad simulador = new SilkRoad(lenRoad);

        simulador.makeVisible();
        JOptionPane.showMessageDialog(null,
            "Ruta creada con longitud: " + lenRoad,
            "Inicio", JOptionPane.INFORMATION_MESSAGE);
    
        simulador.placeStore(5, 75);
        simulador.placeStore(8, 50);
        simulador.placeStore(12, 120);
    
        JOptionPane.showMessageDialog(null,
            "Tiendas creadas:\n" +
            "Posición 2: 100 tenges Normal\n" +
            "Posición 5: 75 tenges Autonomous\n" + 
            "Fighter Posición 8: 50 tenges Fighter\n" +
            "Posición 12 : 120 tenges Normal",
            "Tiendas", JOptionPane.INFORMATION_MESSAGE);
    
        simulador.placeRobot(0);
        simulador.placeRobot("neverBack", 4);
        simulador.placeRobot("Tender", 10);
    
        JOptionPane.showMessageDialog(null,
            "Robots colocados:\n" +
            "Posición 0 : normal\n" +
            "Posición 4 : NeverBack\n" +
            "Posición 10 : Tender",
            "Robots", JOptionPane.INFORMATION_MESSAGE);
    
        simulador.moveRobot(0, 2);
        simulador.moveRobot(4, 5);
        simulador.moveRobot(10, 8);
    
        int profitManual = simulador.profit();
        JOptionPane.showMessageDialog(null,
            "Ganancia acumulada: " + profitManual + " tenges",
            "Movimiento manual", JOptionPane.INFORMATION_MESSAGE);
    
        simulador.moveRobots();
        int profitTotal = simulador.profit();
    
        JOptionPane.showMessageDialog(null,
            "Ganancia total acumulada: " + profitTotal + " tenges",
            "Movimiento automático", JOptionPane.INFORMATION_MESSAGE);
    
        simulador.resupplyStores();
    
        int respuesta = JOptionPane.showConfirmDialog(null,
            "Ganancia final: " + profitTotal + " tenges.\n\n" +
            "¿El comportamiento observado fue el esperado?",
            "Resultado final", JOptionPane.YES_NO_OPTION);
    
        if (respuesta == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null,
                "Prueba de aceptación aprobada por el usuario.",
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                "Prueba de aceptación rechazada por el usuario.",
                "Resultado", JOptionPane.ERROR_MESSAGE);
        }
    
        simulador.makeInvisible();
    }
    
    @Test
    /**
    * Prueba de aceptación B para SilkRoad - Enfocada en tiendas especiales y movimientos estratégicos
    */
    public void SilkRoadBTest() {
        int lenRoad = 20;
        SilkRoad simulador = new SilkRoad(lenRoad);
        simulador.makeVisible();
        JOptionPane.showMessageDialog(null, "Ruta creada con longitud: " + lenRoad + "\n" +
        "Esta prueba se enfoca en tiendas Fighter, Bonus y comportamientos especiales",
        "Inicio Prueba B", JOptionPane.INFORMATION_MESSAGE);

        simulador.placeStore("fighter", 3, 200);
        simulador.placeStore("bonus", 6, 100);
        simulador.placeStore("autonomous", 9, 150);
        simulador.placeStore("normal", 12, 80);
        JOptionPane.showMessageDialog(null,
        "Tiendas especiales creadas:\n" +
        "Posición 3 : 200 tenges Fighter\n" +
        "Posición 6 : 100 tenges Bonus\n" +
        "Posición 9 : 150 tenges Autonomous\n" +
        "Posición 12 : 80 tenges Normal",
        "Tiendas Especiales", JOptionPane.INFORMATION_MESSAGE);

        simulador.placeRobot(0); 
        simulador.placeRobot("neverBack", 2);  
        simulador.placeRobot("Tender", 5);    
        JOptionPane.showMessageDialog(null,
        "Robots colocados:\n" +
        "Posición: 0\n" +
        "Posición: 2 Neverback\n" +
        "Posición: 5 Tender\n",
        "Robots Especializados", JOptionPane.INFORMATION_MESSAGE);

        simulador.moveRobot(0, 3);
        
        int profitFase1 = simulador.profit();
        JOptionPane.showMessageDialog(null,
        "Ganancia acumulada: " + profitFase1 + " tenges",
        "Movimiento 1", JOptionPane.INFORMATION_MESSAGE);

        simulador.moveRobot(5, 1);
        int profitFase2 = simulador.profit();
        JOptionPane.showMessageDialog(null, "Ganancia acumulada: " + profitFase2 + " tenges",
        "Movimiento 2", JOptionPane.INFORMATION_MESSAGE);

        simulador.moveRobots();
        int profitAutomatico = simulador.profit();
        JOptionPane.showMessageDialog(null, "Ganancia después de movimiento automático: " + profitAutomatico + " tenges\n",
        "Movimiento Automático", JOptionPane.INFORMATION_MESSAGE);

        simulador.resupplyStores();
        simulador.returnRobots();

        int[][] storesInfo = simulador.stores();
        int[][] robotsInfo = simulador.robots();
        StringBuilder estadoFinal = new StringBuilder();
        estadoFinal.append("ESTADO FINAL DEL SISTEMA\n\n");
        estadoFinal.append("TIENDAS REABASTECIDAS:\n");
        for (int[] store : storesInfo) {
            estadoFinal.append("Posición ").append(store[0])
            .append(": ").append(store[1]).append(" tenges\n");
        }
        estadoFinal.append("\nROBOTS EN POSICIONES INICIALES:\n");
        for (int[] robot : robotsInfo) {
            estadoFinal.append("• Posición ").append(robot[0])
            .append(": ").append(robot[1]).append(" tenges acumulados\n");
        }
        estadoFinal.append("\nGanancia total final: ").append(profitAutomatico).append(" tenges");
        JOptionPane.showMessageDialog(null,
        estadoFinal.toString(),
        "Estado Final", JOptionPane.INFORMATION_MESSAGE);
        // Evaluación final
        int respuesta = JOptionPane.showConfirmDialog(null, "¿El comportamiento observado fue el esperado?",
        "Evaluación Final - Prueba B", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null,
                "Prueba de aceptación aprobada por el usuario.",
                "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                "Prueba de aceptación rechazada por el usuario.",
                "Resultado", JOptionPane.ERROR_MESSAGE);
        }
        simulador.makeInvisible();
    }
       
    //Para simulate
    @Test
    public void testAceptacion0() {
        SilkRoadContest srContest = new SilkRoadContest(); 
        // Expected Output: 0, 10, 35, 50, 50, 60
        int[][] sampleInput1 = {
            {1, 20},        
            {2, 15, 15},    
            {2, 40, 50},    
            {1, 50},    
            {2, 80, 20},   
            {2, 70, 30}    
        };
        srContest.simulate(sampleInput1, true);
    }
    
    public void testAceptacion1() {
        SilkRoadContest srContest = new SilkRoadContest(); 
        int[][] sampleInput1 = {
            {1, 10},        // Día 1: Robot en 20
            {2, 5, 15},     // Día 2: Tienda en 15 con 15 tenges
            {1, 20},        //
            {2, 14, 50}, 
            {1, 6},         // Día 3: Tienda en 40 con 50 tenges// Día 4: Robot en 50
            {2, 4, 20},     // Día 5: Tienda en 80 con 20 tenges
            {2, 11, 30}     // Día 6: Tienda en 70 con 30 tenges
        };
        srContest.simulate(sampleInput1, true);
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
}