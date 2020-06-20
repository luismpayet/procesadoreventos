package pe.com.pacifico.receptorevento;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

// import org.joda.time.format.DateTimeFormat;
// import org.joda.time.format.DateTimeFormatter;
// DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss:SSS");

public class Registro {

    private String contenido;
    private String email;
    private DateTime fechaEvento;
    private Double id;
    private long key = -1;
    private String messageId;

    public Registro(String contenido) {
        final Map<String, Object> retMap = new Gson().fromJson(contenido, new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.email = (String) retMap.get("email");
        this.messageId = (String) retMap.get("messageId");
        this.id = (double) retMap.get("id");
        this.contenido = (String) retMap.get("contenido");
    }

    public String getContenido() {
        return contenido;
    }

    public String getEmail() {
        return email;
    }

    public DateTime getFechaEvento() {
        return fechaEvento;
    }

    public Double getId() {
        return id;
    }

    public long getKey() {
        return key;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setContenido(final String contenido) {
        this.contenido = contenido;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setFechaEvento(final DateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public void setId(final Double id) {
        this.id = id;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

}