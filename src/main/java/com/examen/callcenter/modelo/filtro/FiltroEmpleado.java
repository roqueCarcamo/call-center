package com.examen.callcenter.modelo.filtro;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.examen.callcenter.enumerador.EstadoEmpleado;
import com.examen.callcenter.modelo.Empleado;

/**
 * Filtro para buscar empleado
 *
 * @author Rodolfo Cárcamo
 */
public class FiltroEmpleado {

    private static final Logger logger = LoggerFactory.getLogger(FiltroEmpleado.class);

    /**
     * Encuentrar un empleado disponible
     *
     * El orden de atención está dado por la disponibilidad de empleados es el
     * siguiente: primero asisten los operadores, segundo los supervisores y
     * finalmente los directores.
     *
     * @param listaEmpleados Lista de empleados que trabajan
     * @return primer empleado disponible para tomar una llamada o nulo si todos
     * los empleados están ocupados.
     */
    public Empleado buscarEmpleado(Collection<Empleado> listaEmpleados) {
        Validate.notNull(listaEmpleados);
        Comparator<Empleado> byLevelEmployee = (Empleado e1, Empleado e2) -> Integer
                .compare(e2.getTipoEmpleado().getNivel(), e1.getTipoEmpleado().getNivel());
        Comparator<Empleado> nombreEmpleado = (Empleado e1, Empleado e2) -> e1.getEmpleadoNombre()
                .compareTo(e2.getEmpleadoNombre());
        Comparator<Empleado> comparador = byLevelEmployee.thenComparing(nombreEmpleado);
        Optional<Empleado> empleado = listaEmpleados.stream().filter(e -> e.getEstadoEmpleado() == EstadoEmpleado.DISPONIBLE)
                .sorted(comparador).findFirst();
        if (!empleado.isPresent()) {
            logger.debug("No existen operadores disponibles.");
            return null;
        }
        return empleado.get();
    }
}
