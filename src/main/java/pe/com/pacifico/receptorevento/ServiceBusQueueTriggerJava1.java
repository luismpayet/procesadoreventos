package pe.com.pacifico.receptorevento;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import java.util.logging.Level;

/**
 * Azure Functions with Service Bus Trigger.
 */
public class ServiceBusQueueTriggerJava1 {
    /**
     * This function will be invoked when a new message is received at the Service Bus Queue.
     */
    @FunctionName("ServiceBusQueueTriggerJava1")
    public void run(
            @ServiceBusQueueTrigger(name = "message", queueName = "pruebaconcepto", connection = "sbplataformaunica_SERVICEBUS") String message,
            final ExecutionContext context
    ) {
        context.getLogger().info("Java Service Bus Queue trigger function executed.");
        Procesador prc = new Procesador();
        try {
            String idMensaje = prc.procesarMensaje(message);
            context.getLogger().info(String.format("mensaje %s procesado", idMensaje));
        } catch (Exception ex) {
            String msg = String.format("Error al procesar mensaje: %s  ", ex.getMessage());
            context.getLogger().log(Level.SEVERE, msg, ex);
        }
        context.getLogger().info(message);
    }
}
