package pe.com.pacifico.receptorevento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.microsoft.azure.eventgrid.models.EventGridEvent;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

    private Logger log;

    private void enviarConfirmacion(Registro registro) {
        MyEmailer emailer = new MyEmailer(registro);
        try {
            emailer.enviarMensaje();
        } catch (MessagingException ex) {
            log.severe("el mail no pudo ser enviado por " + ex.getMessage());
        }
    }

    private Registro extraerRegistro(String body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JodaModule());
            EventGridEvent evento = mapper.readValue(body, EventGridEvent.class);
            Object payload = evento.data();
            String jsonPayload = (String) payload;
            Registro mensaje = new Registro(jsonPayload);
            mapper.readValue(jsonPayload, Registro.class);
            mensaje.setMessageId(evento.id());
            mensaje.setFechaEvento(evento.eventTime());
            log.info(String.format("id del evento: %s", evento.id()));
            return mensaje;
        } catch (IOException ex) {
            log.severe(String.format("error al convertir el evento: %s", ex.getMessage()));
            return null;
        }
    }

    private boolean grabarRegistro(Registro registro) throws Exception {
        Grabador grabador = new Grabador(registro);
        String result = grabador.grabar();
        if (result != null) {
            log.info("error al grabar: " + result);
            return false;
        } else {
            return true;
        }
    }

    private String procesarMensaje(String body) throws Exception {
        Registro reg = extraerRegistro(body);
        if (grabarRegistro(reg)) {
            enviarConfirmacion(reg);
            return reg.getMessageId();
        } else {
            return null;
        }
    }

    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    //@FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                    name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS)
                    HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        log = context.getLogger();
        log.info("Procesando un mensaje.");
        final String body = request.getBody().orElse(null);
        if (body == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("El request no tenía body").build();
        }
        try {
            String idMensaje = procesarMensaje(body);
            return request.createResponseBuilder(HttpStatus.OK).body(String.format("Mensaje %s procesado", idMensaje)).build();
        } catch (Exception ex) {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al convertir mensaje").build();
        }
    }

}