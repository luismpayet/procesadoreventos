package pe.com.pacifico.receptorevento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.microsoft.azure.eventgrid.models.EventGridEvent;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.Map;

public class Procesador {

    private Registro crearRegistro(String body) {
        //mensaje.setMessageId(evento.id());
        //mensaje.setFechaEvento(evento.eventTime());
        return new Registro(body);
    }

    private void enviarConfirmacion(Registro registro) throws MessagingException {
        MyEmailer emailer = new MyEmailer(registro);
        emailer.enviarMensaje();
    }

    String extraerMensaje(String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        EventGridEvent evento = mapper.readValue(body, EventGridEvent.class);
        Object payload = evento.data();
        return (String) payload;
    }

    private void grabarRegistro(Registro registro) throws SQLException {
        Grabador grabador = new Grabador(registro);
        grabador.grabar();
    }

    public String procesarMensaje(String body) throws MessagingException, SQLException, IOException, MalformedURLException {
        Registro reg = crearRegistro(body);
        validarCliente(reg);
        grabarRegistro(reg);
        enviarConfirmacion(reg);
        return reg.getMessageId();
    }

    public void validarCliente(Registro reg) throws MalformedURLException, IOException {
        String dni = reg.getDni();
        String uri = String.format(System.getenv("SERVICIO_DNI"), dni);
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", System.getenv("AUTH_SERVICIO"));
        StringBuilder sb = new StringBuilder();
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        InputStreamReader in = new InputStreamReader(connection.getInputStream());
        int charsRead;
        while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            sb.append(buffer, 0, charsRead);
        }
        String response = sb.toString();
        String resultado = "";
        Map<String, String> respMap = null;
        try {
            respMap = new Gson().fromJson(response, new TypeToken<Map<String, String>>() {}.getType());
        } catch (Exception ex) {
            resultado = response;
        }
        if (connection.getResponseCode() == 200) {
            if (respMap != null) {
                if (respMap.containsKey("status")) {
                    resultado = String.format("codigo: %s - mensaje: %s", respMap.get("status"), respMap.get("message"));
                } else {
                    resultado = String.format("nombre: %s - apellido: %s", respMap.get("names"), respMap.get("firstSurname"));
                }
            } else {
                resultado = "Error al convertir resultado JSon: " + response;
            }
        } else {
            resultado = String.format("error: %s - mensaje: %s", connection.getResponseCode(), response);        
        }  
        connection.disconnect();
        reg.setMensaje(resultado);
    }

}