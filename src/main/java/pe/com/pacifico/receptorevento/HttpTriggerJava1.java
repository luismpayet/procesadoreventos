package pe.com.pacifico.receptorevento;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;
import java.util.logging.Level;

/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerJava1 {

    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpTriggerJava1")
    public HttpResponseMessage run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("HttpTriggerJava1: Procesando un mensaje.");
        final String body = request.getBody().orElse(null);
        if (body == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("El request no tenía body").build();
        }
        try {
            Procesador prc = new Procesador();
            String idMensaje = prc.procesarMensaje(body);
            return request.createResponseBuilder(HttpStatus.OK).body(String.format("Mensaje %s procesado", idMensaje)).build();
        } catch (Exception ex) {
            String msg = String.format("Error al procesar mensaje: %s  ", ex.getMessage());
            context.getLogger().log(Level.SEVERE, msg, ex);
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body(msg).build();
        }
    }

}