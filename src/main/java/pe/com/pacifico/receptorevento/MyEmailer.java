package pe.com.pacifico.receptorevento;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MyEmailer {

    private static final String SMTP_AUTH_PWD = "SMTP_PASSWORD";
    private static final String SMTP_AUTH_USER = "SMTP_USER";
    private static final String SMTP_HOST_NAME = "SMTP_SERVER";
    private static final String SMTP_ORIGEN = "SMTP_ORIGEN";
    private Session mailSession;
    private final Registro registro;

    public MyEmailer(final Registro registro) {
        this.registro = registro;
    }

    public void conectar() throws MessagingException {
        final Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", System.getenv(SMTP_HOST_NAME));
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.auth", "true");
        final Authenticator auth = new SMTPAuthenticator();
        mailSession = Session.getDefaultInstance(properties, auth);
    }

    public MimeMessage crearMensaje() throws MessagingException {
        final MimeMessage message = new MimeMessage(mailSession);
        final Multipart multipart = new MimeMultipart("alternative");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<p>Hola %s,</p><p>Gracias por su mensaje <b>%s</b>.", registro.getNombre(), registro.getMessageId()));
        sb.append(String.format("<p>La respuesta a su consulta de DNI fue: %s", registro.getMensaje()));
        sb.append(String.format("<p>El mensaje ha sido registrado en la base de datos. La llave primaria del registro es: [%d]", registro.getKey()));
        sb.append(String.format("<p>El contenido de su mensaje fue:<br/>%s</p>", registro.getContenido()));
        multipart.addBodyPart(getBodyPart(sb.toString()));
        message.setFrom(new InternetAddress(System.getenv(SMTP_ORIGEN)));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(registro.getEmail()));
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(System.getenv(SMTP_ORIGEN)));
        message.setSubject("Gracias por su mensaje.");
        message.setContent(multipart);
        return message;
    }

    public void enviarMensaje() throws MessagingException {
        conectar();
        final Message message = crearMensaje();
        final Transport transport = mailSession.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    public BodyPart getBodyPart(String msg) throws MessagingException {
        final BodyPart part1 = new MimeBodyPart();
        part1.setContent(String.format(msg, registro.getMessageId()), "text/html");
        return part1;
    }

    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(System.getenv(SMTP_AUTH_USER), System.getenv(SMTP_AUTH_PWD));
        }
    }

}