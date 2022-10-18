package dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Platillo;

public class PlatilloImp extends Conexion implements ICRUD<Platillo> {

    @Override
    public void guardar(Platillo platillo) throws Exception {
        this.conectar();
        try {
            String sql = "INSERT INTO PLATILLO"
                    + " (NOMPLA,DESPLA,PREPLA,STOCKPLA,ESTPLA)"
                    + " values (?, ?, ?, ?, ?)";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, platillo.getNombrePlatillo());
            ps.setString(2, platillo.getDescripcionPlatillo());
            ps.setDouble(3, platillo.getPrecioPlatillo());
            ps.setInt(4, platillo.getStockPlatillo());
            ps.setString(5, "A");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en PlatilloImpl/guardar: " + e.getMessage());
        }
    }

    @Override
    public void modificar(Platillo platillo) throws Exception {
        this.conectar();
        String sql = "UPDATE PLATILLO SET NOMPLA=?,DESPLA=?,PREPLA=?,STOCKPLA=?,ESTPLA=? where IDPLA=?";
        try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
            ps.setString(1, platillo.getNombrePlatillo());
            ps.setString(2, platillo.getDescripcionPlatillo());
            ps.setDouble(3, platillo.getPrecioPlatillo());
            ps.setInt(4, platillo.getStockPlatillo());
            ps.setString(5, "A");
            ps.setInt(6, platillo.getCodigoPlatillo());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en PlatilloImpl/modificar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Platillo platillo) throws Exception {
        this.conectar();
        try {
            String sql = "UPDATE PLATILLO set ESTPLA=? where IDPLA=?";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, platillo.getEstadoPlatillo());
            ps.setInt(2, platillo.getCodigoPlatillo());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Error en Eliminar Cliente: " + e.getMessage());
        }
    }

    public void restaurar(Platillo platillo) throws Exception {
        this.conectar();
        try {
            String sql = "UPDATE PLATILLO set ESTPLA = 'A' where IDPLA=?";
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setInt(1, platillo.getCodigoPlatillo());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error en restaurar/PersonaImpl: " + e.getMessage());
        } finally {
            this.cerrar();

        }
    }

    //@Override
    public List<Platillo> listarTodos(int tipo) throws InstantiationException, IllegalAccessException {
        this.conectar();
        List<Platillo> listado = new ArrayList<>();
        ResultSet rs;
        String sql = "";
        switch (tipo) {
            case 1:
                sql = "SELECT * FROM PLATILLO WHERE ESTPLA = 'A' ORDER BY IDPLA desc";
                break;
            case 2:
                sql = "SELECT * FROM PLATILLO WHERE ESTPLA = 'I' ORDER BY IDPLA desc";
                break;
            case 3:
                sql = "SELECT * FROM PLATILLO ORDER BY IDPLA desc";
                break;
        }
        try {
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Platillo pla = new Platillo();
                pla.setCodigoPlatillo(rs.getInt("IDPLA"));
                pla.setNombrePlatillo(rs.getString("NOMPLA"));
                pla.setDescripcionPlatillo(rs.getString("DESPLA"));
                pla.setPrecioPlatillo(rs.getDouble("PREPLA"));
                pla.setStockPlatillo(rs.getInt("STOCKPLA"));
                pla.setEstadoPlatillo(rs.getString("ESTPLA"));
                listado.add(pla);
            }
        } catch (SQLException e) {
            System.out.println("Error en PlatilloImpl/Listar: " + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }

    public void cambiarEstado(Platillo platillo) throws Exception {
        try {
            String sql = "UPDATE PLATILLO SET ESTPLA=? WHERE IDPLA=?";
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setString(1, platillo.getEstadoPlatillo());
                ps.setInt(2, platillo.getCodigoPlatillo());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error en PlatilloImp/cambiarEstado: " + e.getMessage());
        }
    }

    @Override
    public List<Platillo> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
