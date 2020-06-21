package pe.com.pacifico.receptorevento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.microsoft.azure.eventgrid.models.EventGridEvent;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

public class Procesador {

    private void enviarConfirmacion(Registro registro) throws MessagingException {
        MyEmailer emailer = new MyEmailer(registro);
        emailer.enviarMensaje();
    }

    private Registro extraerRegistro(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        EventGridEvent evento = mapper.readValue(body, EventGridEvent.class);
        Object payload = evento.data();
        String jsonPayload = (String) payload;
        Registro mensaje = new Registro(jsonPayload);
        mensaje.setMessageId(evento.id());
        mensaje.setFechaEvento(evento.eventTime());
        return mensaje;
    }

    private void grabarRegistro(Registro registro) throws SQLException {
        Grabador grabador = new Grabador(registro);
        grabador.grabar();
    }

    public String procesarMensaje(String body) throws IOException, MessagingException, SQLException {
        Registro reg = extraerRegistro(body);
        grabarRegistro(reg);
        enviarConfirmacion(reg);
        return reg.getMessageId();
    }

}