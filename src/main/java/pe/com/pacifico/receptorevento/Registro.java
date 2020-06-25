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
    private String dni;
    private String email;
    private DateTime fechaEvento;
    private long key = -1;
    private String messageId;
    private String nombre;
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Registro(String contenido) {
        final Map<String, Object> retMap = new Gson().fromJson(contenido, new TypeToken<HashMap<String, Object>>() {}.getType());
        this.email = (String) retMap.get("email");
        this.messageId = (String) retMap.get("messageId");
        this.nombre = (String) retMap.get("nombre");
        this.contenido = (String) retMap.get("contenido");
        this.dni = (String) retMap.get("dni");
    }

    public String getContenido() {
        return contenido;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail() {
        return email;
    }

    public DateTime getFechaEvento() {
        return fechaEvento;
    }

    public long getKey() {
        return key;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setContenido(final String contenido) {
        this.contenido = contenido;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setFechaEvento(final DateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public void setMessageId(final String messageId) {
        this.messageId = messageId;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

}