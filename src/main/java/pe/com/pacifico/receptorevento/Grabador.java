package pe.com.pacifico.receptorevento;

import java.sql.*;
import java.util.Properties;

public class Grabador {

    private final static String DB_PASSWORD = "DB_PASSWORD";
    private final static String DB_SQL_INSERT = "insert into registro (id_evento, fecha_evento, id_mensaje, texto_mensaje) values (?, ?, ? , ?)";
    private final static String DB_STRING = "DB_STRING";
    private final static String DB_USER_NAME = "DB_USER_NAME";

    private Registro registro;
    private PreparedStatement stmtInsert;

    public Grabador(Registro registro) {
        this.registro = registro;
    }

    public void conectar() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", System.getenv(DB_USER_NAME));
        connectionProps.put("password", System.getenv(DB_PASSWORD));
        String cadenaConexion = "jdbc:mysql://" + System.getenv(DB_STRING);
        Connection conn = DriverManager.getConnection(cadenaConexion, connectionProps);
        conn.setAutoCommit(true);
        stmtInsert = conn.prepareStatement(DB_SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
    }

    public String grabar() throws SQLException {
        conectar();
        insertar();
        return null;
    }

    public void insertar() throws SQLException {
        stmtInsert.setString(1, registro.getMessageId());
        stmtInsert.setDate(2, new java.sql.Date(registro.getFechaEvento().getMillis()));
        stmtInsert.setDouble(3, registro.getId());
        stmtInsert.setString(4, registro.getContenido());
        stmtInsert.executeUpdate();
        ResultSet rs = stmtInsert.getGeneratedKeys();
        if (rs.next()) {
            registro.setKey(rs.getLong(1));
        }
    }

}
