# call-center

La principal solución es utilizar Threads, para cada uno de los empleados, generar llamadas y el mismo despachador para asignar las llamadas, y para las llamadas que los empleados no pueden manejar, se colocan en una cola concurrente y se espera que un empleado ya este disponible.
