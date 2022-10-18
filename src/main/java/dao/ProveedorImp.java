package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Proveedor;
import model.Ubigeo;

public class ProveedorImp extends Conexion implements ICRUD<Proveedor> {

    @Override
    public void guardar(Proveedor proveedor) throws Exception {
        this.conectar();
        try {
            String sql = "INSERT INTO PROVEEDOR"
                    + " (RAZSOCPROV ,RUCPROV ,PROVEND ,EMAIPROV ,CELPROV ,CODUBI ,DIRPROV ,ESTPROV)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, proveedor.getRazonSocialProveedor());
            ps.setString(2, proveedor.getRucProveedor());
            ps.setString(3, proveedor.getProductoVender());
            ps.setString(4, proveedor.getCorreoProveedor());
            ps.setString(5, proveedor.getCelularProveedor());
            ps.setString(6, proveedor.getCodigoUbigeo());
            ps.setString(7, proveedor.getDireccionProveedor());
            ps.setString(8, "A");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en ProveedorImpl/Guardar: " + e.getMessage());
        }
    }

    @Override
    public void modificar(Proveedor proveedor) throws Exception {
        this.conectar();
        String sql = "UPDATE PROVEEDOR SET RAZSOCPROV=?,RUCPROV=?,PROVEND=?,EMAIPROV=?,CELPROV=?,CODUBI=?,DIRPROV=?,ESTPROV=? where IDPROV=?";
        try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
            ps.setString(1, proveedor.getRazonSocialProveedor());
            ps.setString(2, proveedor.getRucProveedor());
            ps.setString(3, proveedor.getProductoVender());
            ps.setString(4, proveedor.getCorreoProveedor());
            ps.setString(5, proveedor.getCelularProveedor());
            ps.setString(6, proveedor.getCodigoUbigeo());
            ps.setString(7, proveedor.getDireccionProveedor());
            ps.setString(8, "A");
            ps.setInt(9, proveedor.getCodigoProveedor());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en ProveedorImpl/Modificar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Proveedor proveedor) throws Exception {
        this.conectar();
        try {
            String sql = "UPDATE PROVEEDOR set ESTPROV=? where IDPROV=?";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, proveedor.getEstadoProveedor());
            ps.setInt(2, proveedor.getCodigoProveedor());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Error en Eliminar ProveedorImpl: " + e.getMessage());
        }
    }

    public void restaurar(Proveedor proveedor) throws Exception {
        this.conectar();
        try {
            String sql = "UPDATE proveedor set ESTPROV = 'A' where IDPROV=?";
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setInt(1, proveedor.getCodigoProveedor());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error en ProveedorImpl/Restaurar: " + e.getMessage());
        } finally {
            this.cerrar();

        }
    }

    //@Override
    public List<Proveedor> listarTodos(int tipo) throws InstantiationException, IllegalAccessException {
        this.conectar();
        List<Proveedor> listado = new ArrayList<>();
        ResultSet rs;
        String sql = "";
        switch (tipo) {
            case 1:
                sql = "SELECT * FROM DatosProveedor WHERE ESTPROV = 'A' ORDER BY IDPROV desc";
                break;
            case 2:
                sql = "SELECT * FROM DatosProveedor WHERE ESTPROV = 'I' ORDER BY IDPROV desc";
                break;
            case 3:
                sql = "SELECT * FROM DatosProveedor ORDER BY IDPROV desc";
                break;
        }
        try {
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Proveedor prov = new Proveedor();
                prov.setCodigoProveedor(rs.getInt("IDPROV"));
                prov.setRazonSocialProveedor(rs.getString("RAZSOCPROV"));
                prov.setRucProveedor(rs.getString("RUCPROV"));
                prov.setProductoVender(rs.getString("PROVEND"));
                prov.setCorreoProveedor(rs.getString("EMAIPROV"));
                prov.setCelularProveedor(rs.getString("CELPROV"));
                prov.setCodigoUbigeo(rs.getString("CODUBI"));
                prov.setDepartamento(rs.getString("DEPAR"));
                prov.setProvincia(rs.getString("PROVI"));
                prov.setDistrito(rs.getString("DISTRI"));
                prov.setDireccionProveedor(rs.getString("DIRPROV"));
                prov.setEstadoProveedor(rs.getString("ESTPROV"));
                listado.add(prov);
            }
        } catch (SQLException e) {
            System.out.println("Error en ProveedorImpl/Listar: " + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }

    public List listarUbigeo() throws Exception {
        this.conectar();
        List<Ubigeo> listUbig = null;
        Ubigeo ubi;
        String sql = "SELECT * FROM UBIGEO";
        try {
            listUbig = new ArrayList();
            Statement st = this.getCn().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ubi = new Ubigeo();
                ubi.setCodigoUbigeo(rs.getString("CODUBI"));
                ubi.setDepartamento(rs.getString("DEPAR"));
                ubi.setProvincia(rs.getString("PROVI"));
                ubi.setDistrito(rs.getString("DISTRI"));
                listUbig.add(ubi);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.out.println("Error en ProveedorImp/Ubigeo: " + e.getMessage());
        }
        return listUbig;

    }

    public void cambiarEstado(Proveedor proveedor) throws Exception {
        try {
            String sql = "UPDATE PROVEEDOR SET ESTPROV=? WHERE IDPROV=?";
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setString(1, proveedor.getEstadoProveedor());
                ps.setInt(2, proveedor.getCodigoProveedor());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error en ProveedorImp/CambiarEstado: " + e.getMessage());
        }
    }

    @Override
    public List<Proveedor> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
