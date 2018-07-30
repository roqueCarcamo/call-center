package com.examen.callcenter.dispatcher;

import com.examen.callcenter.modelo.Empleado;
import com.examen.callcenter.modelo.Llamada;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de prueba de despachador de llamadas
 *
 * @author Rodolfo Cárcamo
 */
public class DispatcherTest {

    private static final int MIN_DURACION_LLAMADA = 5;

    private static final int MAX_DURACION_LLAMADA = 10;

    private static final int CANTIDAD_LLAMADAS = 10;

    @Before
    public void executedBeforeEach() {
        Dispatcher.llamadasEntrantes.clear();
    }

    @Test(expected = NullPointerException.class)
    public void testDispatcherCreacionEmpleadosNull() {
        new Dispatcher(null, false);
    }

    @Test
    public void testDispatchEmpleadosLlamadas() throws InterruptedException {
        List<Empleado> listaEmpleados = crearListaEmpleados();
        Dispatcher dispatcher = new Dispatcher(listaEmpleados, false);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);
        crearListaLlamadas().stream().forEach(call -> {
            Dispatcher.dispatchCall(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                fail();
            }
        });
        executorService.awaitTermination(MAX_DURACION_LLAMADA * 2, TimeUnit.SECONDS);
        assertEquals(CANTIDAD_LLAMADAS, listaEmpleados.stream().mapToInt(employee -> employee.getAttendedCalls().size()).sum());
    }

    @Test(expected = NullPointerException.class)
    public void testDispatcherCreacionNull() {
        new Dispatcher(new ArrayList<>(), null, false);
    }

    private static List<Empleado> crearListaEmpleados() {
        //Crear empleados de tipo Operador
        Empleado operador1 = Empleado.crearOperador("operador1");
        Empleado operador2 = Empleado.crearOperador("operador2");
        Empleado operador3 = Empleado.crearOperador("operador3");
        //Crear empleado de tipo Director
        Empleado director = Empleado.crearDirector("director");
        //Crear empleado de tipo Supervisor
        Empleado supervisor1 = Empleado.crearSupervisor("supervisor1");
        Empleado supervisor2 = Empleado.crearSupervisor("supervisor2");
        return Arrays.asList(operador1, operador2, operador3, supervisor1, supervisor2, director);
    }

    @Test(expected = NullPointerException.class)
    public void testDispatcherCreacionGenerarLlamadasNull() {
        new Dispatcher(new ArrayList<>(), null);
    }

    private static List<Llamada> crearListaLlamadas() {
        return Llamada.contruirListaAleatoriaLlamadas(CANTIDAD_LLAMADAS, MIN_DURACION_LLAMADA, MAX_DURACION_LLAMADA);
    }

}
