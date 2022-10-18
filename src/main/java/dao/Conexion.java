package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Conexion {

    private static final Logger LOG = Logger.getLogger(Conexion.class.getName());

    private Connection cn;

    public void conectar() throws InstantiationException, IllegalAccessException {
        String user = "DBSKYFOOD";
        String pwd = "@abc123@";
        String driver = "oracle.jdbc.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521/XE";
        try {
            Class.forName(driver).newInstance();
            cn = DriverManager.getConnection(url, user, pwd);
            System.out.println("Conexion Exitosa, ya puede seguir avanzando con seguridad su proyecto");
        } catch (ClassNotFoundException | SQLException e) {
            LOG.severe(e.getMessage());
            System.out.println("Sin Conexión, por favor verifique el codigo del archivo Conexion");
        }
    }

    public void cerrar() {
        try {
            if (cn != null) {
                if (cn.isClosed() == false) {
                    cn.close();
                }
            }
        } catch (SQLException e) {
            LOG.severe(e.getMessage());
        }
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Conexion dao = new Conexion();
        dao.conectar();
        
    }

    public Connection getCn() {
        return cn;
    }

    public void setCn(Connection cn) {
        this.cn = cn;
    }
}
