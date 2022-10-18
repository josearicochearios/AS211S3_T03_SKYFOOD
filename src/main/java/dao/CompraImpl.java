package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;
import model.Compra;

public class CompraImpl extends Conexion implements ICRUD<Compra> {

    private static final Logger LOG = Logger.getLogger(CompraImpl.class.getName());

    DateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");

    @Override
    public void guardar(Compra gen) throws InstantiationException, IllegalAccessException {
        LOG.info("[CompraImpl] Iniciando con la creacion de la compra");
        final String sql = "INSERT INTO COMPRA (FECCOM,IDPROV,ESTCOM,IDPER) VALUES (?,?,?,?)";
        try {
            LOG.info("[CompraImpl] Conectandose a la base de datos");
            this.conectar();
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setString(1, formatter.format(gen.getFechaCompra()));
                ps.setInt(2, gen.getCodigoProveedor());
                ps.setString(3, "A");
                ps.setInt(4, gen.getCodigoPersona());
                LOG.info("[CompraImpl] Iniciando insercion");
                int eu = ps.executeUpdate();
                if (eu == 1) {
                    LOG.info("[CompraImpl] Insercion Correcta");
                }
                ps.close();
                LOG.info("[CompraImpl] Creacion de la compra exitosa");
            }
        } catch (SQLException e) {
            LOG.severe(e.getSQLState());
        } finally {
            this.cerrar();
            LOG.info("Conexion cerrada");
        }
    }

    @Override
    public void modificar(Compra gen) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(Compra gen) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Compra> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
