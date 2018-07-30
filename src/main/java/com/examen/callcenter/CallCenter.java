package com.examen.callcenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.examen.callcenter.dispatcher.Dispatcher;
import com.examen.callcenter.modelo.Empleado;

/**
 * Clase Principal de ejecución
 *
 * @author Rodolfo Cárcamo
 */
public class CallCenter {

    private static final Logger logger = LoggerFactory.getLogger(CallCenter.class);

    public static void main(String[] args) {
        
        //Crear lista de empleados
        List<Empleado> listaEmpleados = new ArrayList<>();
        
        //Crear empleados de tipo Operador
        listaEmpleados.add(Empleado.crearOperador("Karen"));
        listaEmpleados.add(Empleado.crearOperador("Camilo"));
        listaEmpleados.add(Empleado.crearOperador("Jose"));

        //Crear empleados de tipo Supervisor
        listaEmpleados.add(Empleado.crearSupervisor("Melisa"));
        listaEmpleados.add(Empleado.crearSupervisor("Lina"));
        listaEmpleados.add(Empleado.crearSupervisor("Aura"));
        listaEmpleados.add(Empleado.crearSupervisor("Andres"));

        //Crear empleados de tipo Director
        listaEmpleados.add(Empleado.crearDirector("Gisel"));
        listaEmpleados.add(Empleado.crearDirector("Fernando"));
 
        Dispatcher dispatcher = new Dispatcher(listaEmpleados, true);
        dispatcher.start();
        try {
            TimeUnit.SECONDS.sleep(1);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(dispatcher);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            logger.error("Ejecución interrumpida " + ex.getMessage());
        }
    }
}
