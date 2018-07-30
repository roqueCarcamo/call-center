package com.examen.callcenter.modelo.filtro;

import com.examen.callcenter.enumerador.EstadoEmpleado;
import com.examen.callcenter.enumerador.TipoEmpleado;
import com.examen.callcenter.modelo.Empleado;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * Clase de pruebas de filtro de empleado
 *
 * @author Rodolfo Cárcamo
 */
public class FiltroEmpleadoTest {

    private final FiltroEmpleado empleadoFiltro;

    public FiltroEmpleadoTest() {
        this.empleadoFiltro = new FiltroEmpleado();
    }

    @Test
    public void testAsignarDirector() {
        Empleado operator = mockEmpleadoOcupado(TipoEmpleado.OPERADOR);
        Empleado supervisor = mockEmpleadoOcupado(TipoEmpleado.SUPERVISOR);
        Empleado director = Empleado.crearDirector("director");
        List<Empleado> listaEmpleados = Arrays.asList(operator, supervisor, director);
        Empleado empleado = this.empleadoFiltro.buscarEmpleado(listaEmpleados);
        assertNotNull(empleado);
        assertEquals(TipoEmpleado.DIRECTOR, empleado.getTipoEmpleado());
    }

    private static Empleado mockEmpleadoOcupado(TipoEmpleado empleadoTipo) {
        Empleado empleado = mock(Empleado.class);
        when(empleado.getTipoEmpleado()).thenReturn(empleadoTipo);
        when(empleado.getEstadoEmpleado()).thenReturn(EstadoEmpleado.OCUPADO);
        return empleado;
    }

}
