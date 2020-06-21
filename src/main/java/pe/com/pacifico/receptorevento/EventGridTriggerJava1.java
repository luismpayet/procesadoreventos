package pe.com.pacifico.receptorevento;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.EventGridTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;

import java.util.logging.Level;

/**
 * Azure Functions with Event Grid trigger.
 */
public class EventGridTriggerJava1 {
    /**
     * This function will be invoked when an event is received from Event Grid.
     */
    @FunctionName("EventGridTriggerJava1")
    public void run(@EventGridTrigger(name = "eventGridEvent") String message, final ExecutionContext context) {
        context.getLogger().info("Java Event Grid trigger function executed.");
        Procesador prc = new Procesador();
        try {
            String idMensaje = prc.procesarMensaje(message);
            context.getLogger().info(String.format("mensaje %s procesado", idMensaje));
        } catch (Exception ex) {
            String msg = String.format("Error al procesar mensaje: %s  ", ex.getMessage());
            context.getLogger().log(Level.SEVERE, msg, ex);
        }
    }
}