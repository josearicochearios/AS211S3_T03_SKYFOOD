package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Ubigeo;

public class ClienteImp extends Conexion implements ICRUD<Cliente> {

    @Override
    public void guardar(Cliente cliente) throws Exception {
        this.conectar();
        try {
            String sql = "INSERT INTO CLIENTE"
                    + " (NOMCOMCLI, APELLIDOCLI, DNICLI, CORCLI, CELCLI, CODUBI, DIRCLI, ESTCLI)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, cliente.getNombreCliente());
            ps.setString(2, cliente.getApellidoCliente());
            ps.setString(3, cliente.getDniCliente());
            ps.setString(4, cliente.getCorreoCliente());
            ps.setString(5, cliente.getCelularCliente());
            ps.setString(6, cliente.getCodigoUbigeo());
            ps.setString(7, cliente.getDireccionCliente());
            ps.setString(8, "A");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en ClienteImpl/Guardar: " + e.getMessage());
        }
    }

    @Override
    public void modificar(Cliente cliente) throws Exception {
        this.conectar();
        String sql = "UPDATE CLIENTE SET NOMCOMCLI=?,APELLIDOCLI=?,DNICLI=?,CORCLI=?,CELCLI=?,CODUBI=?,DIRCLI=?,ESTCLI=? where IDCLI=?";
        try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCliente());
            ps.setString(2, cliente.getApellidoCliente());
            ps.setString(3, cliente.getDniCliente());
            ps.setString(4, cliente.getCorreoCliente());
            ps.setString(5, cliente.getCelularCliente());
            ps.setString(6, cliente.getCodigoUbigeo());
            ps.setString(7, cliente.getDireccionCliente());
            ps.setString(8, "A");
            ps.setInt(9, cliente.getCodigoCliente());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en ClienteImpl/Modificar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Cliente cliente) throws Exception {
        this.conectar();
        try {
            String sql = "UPDATE CLIENTE set ESTCLI=? where IDCLI=?";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, cliente.getEstadoCliente());
            ps.setInt(2, cliente.getCodigoCliente());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Error en ClienteImp/Eliminar " + e.getMessage());
        }
    }

    public void restaurar(Cliente cliente) throws Exception {
        this.conectar();
        try {
            String sql = "UPDATE CLIENTE set ESTCLI = 'A' where IDCLI=?";
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setInt(1, cliente.getCodigoCliente());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error en ClienteImp/Restaurar " + e.getMessage());
        } finally {
            this.cerrar();

        }
    }

    //@Override
    public List<Cliente> listarTodos(int tipo) throws InstantiationException, IllegalAccessException {
        this.conectar();
        List<Cliente> listado = new ArrayList<>();
        ResultSet rs;
        String sql = "";
        switch (tipo) {
            case 1:
                sql = "SELECT * FROM DatosClientes WHERE ESTCLI = 'A' ORDER BY IDCLI desc";
                break;
            case 2:
                sql = "SELECT * FROM DatosClientes WHERE ESTCLI = 'I'  ORDER BY IDCLI desc";
                break;
            case 3:
                sql = "SELECT * FROM DatosClientes ORDER BY IDCLI desc";
                break;
        }
        try {
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCodigoCliente(rs.getInt("IDCLI"));
                cli.setNombreCliente(rs.getString("NOMCOMCLI"));
                cli.setApellidoCliente(rs.getString("APELLIDOCLI"));
                cli.setDniCliente(rs.getString("DNICLI"));
                cli.setCorreoCliente(rs.getString("CORCLI"));
                cli.setCelularCliente(rs.getString("CELCLI"));
                cli.setCodigoUbigeo(rs.getString("CODUBI"));
                cli.setDepartamento(rs.getString("DEPAR"));
                cli.setProvincia(rs.getString("PROVI"));
                cli.setDistrito(rs.getString("DISTRI"));
                cli.setDireccionCliente(rs.getString("DIRCLI"));
                cli.setEstadoCliente(rs.getString("ESTCLI"));
                listado.add(cli);
            }
        } catch (SQLException e) {
            System.out.println("Error en ClienteImpl/Listar: " + e.getMessage());
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
            System.out.println("Error en ClienteImp/Ubigeo: " + e.getMessage());
        }
        return listUbig;

    }

    public void cambiarEstado(Cliente cliente) throws Exception {
        try {
            String sql = "UPDATE CLIENTE SET ESTCLI=? WHERE IDCLI=?";
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setString(1, cliente.getEstadoCliente());
                ps.setInt(2, cliente.getCodigoCliente());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error en ClienteImp/CambiarEstado: " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
