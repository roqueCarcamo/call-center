package com.examen.callcenter.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Clase de pruebas de generar llamadas
 *
 * @author Rodolfo Cárcamo
 */
public class GeneradorLlamadaTest {

    @Test
    public void testGenerarLlamadasActivas() throws InterruptedException {
        Integer max = 16;
        GeneradorLlamada generadorLlamada = new GeneradorLlamada(true);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(generadorLlamada);
        TimeUnit.SECONDS.sleep(1);
        executorService.awaitTermination(2, TimeUnit.SECONDS);
        assertNotNull(generadorLlamada);
        assertEquals(generadorLlamada.getActivo(), true);
        assertTrue(Dispatcher.llamadasEntrantes.size() <= max);
        generadorLlamada.stop();
    }

    @Test(expected = NullPointerException.class)
    public void testLLamadasGeneradasNull() {
        new GeneradorLlamada(null);
    }

    @Test
    public void testGenerarLlamadasNoActivas() throws InterruptedException {
        GeneradorLlamada generadorLlamada = new GeneradorLlamada(false);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(generadorLlamada);
        TimeUnit.SECONDS.sleep(1);
        executorService.awaitTermination(2, TimeUnit.SECONDS);
        assertNotNull(generadorLlamada);
        assertEquals(generadorLlamada.getActivo(), false);
    }
}
