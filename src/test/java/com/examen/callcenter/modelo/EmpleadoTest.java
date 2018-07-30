package com.examen.callcenter.modelo;

import com.examen.callcenter.enumerador.EstadoEmpleado;
import com.examen.callcenter.enumerador.TipoEmpleado;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Clase de pruebas de Empleado
 *
 * @author Rodolfo Cárcamo
 */
public class EmpleadoTest {

    @Test
    public void testEmpleadoDisponible() throws InterruptedException {
        Empleado empleado = Empleado.crearOperador("operador");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(empleado);
        empleado.responder(Llamada.construirLlamadaAleatoria(0, 1));
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(1, empleado.getAttendedCalls().size());
    }

    @Test(expected = NullPointerException.class)
    public void testEmpleadoConNombreInvalido() {
        new Empleado(null, TipoEmpleado.OPERADOR);
    }

    @Test
    public void testEmpleadoEstadoRespuesta() throws InterruptedException {
        Empleado empleado = Empleado.crearOperador("operador");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(empleado);
        assertEquals(EstadoEmpleado.DISPONIBLE, empleado.getEstadoEmpleado());
        TimeUnit.SECONDS.sleep(1);
        empleado.responder(Llamada.construirLlamadaAleatoria(2, 3));
        TimeUnit.SECONDS.sleep(4);
        empleado.responder(Llamada.construirLlamadaAleatoria(0, 1));
        assertEquals(EstadoEmpleado.OCUPADO, empleado.getEstadoEmpleado());
        executorService.awaitTermination(6, TimeUnit.SECONDS);
        assertEquals(2, empleado.getAttendedCalls().size());
    }

    @Test
    public void testCreacionEmpleados() {
        Empleado empleado = Empleado.crearOperador("operador");
        assertNotNull(empleado);
        assertEquals(TipoEmpleado.OPERADOR, empleado.getTipoEmpleado());
        assertEquals(EstadoEmpleado.DISPONIBLE, empleado.getEstadoEmpleado());
    }

}
