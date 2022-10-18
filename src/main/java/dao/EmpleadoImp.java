package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Empleado;
import model.Ubigeo;

public class EmpleadoImp extends Conexion implements ICRUD<Empleado> {
    
    @Override
    public void guardar(Empleado persona) throws Exception {
        this.conectar();
        try {
            String sql = "INSERT INTO EMPLEADO"
                    + " (NOMCOMEMP,APELLIDOEMP,DNIEMP,COREMP,CELEMP,CODUBI,DIREMP,TURNO,ESTEMP)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, persona.getNombreEmpleado());
            ps.setString(2, persona.getApellidoEmpleado());
            ps.setString(3, persona.getDniEmpleado());
            ps.setString(4, persona.getCorreoEmpleado());
            ps.setString(5, persona.getCelularEmpleado());
            ps.setString(6, persona.getCodigoUbigeo());
            ps.setString(7, persona.getDireccionEmpleado());
            ps.setString(8, persona.getTurno());
            ps.setString(9, "A");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en EmpleadoImpl/Guardar: " + e.getMessage());
        }
    }

    @Override
    public void modificar(Empleado persona) throws Exception {
        this.conectar();
        String sql = "UPDATE EMPLEADO SET NOMCOMEMP=?,APELLIDOEMP=?,DNIEMP=?,COREMP=?,CELEMP=?,CODUBI=?,DIREMP=?,TURNO=?,ESTEMP=? where IDEMP=?";
        try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
            ps.setString(1, persona.getNombreEmpleado());
            ps.setString(2, persona.getApellidoEmpleado());
            ps.setString(3, persona.getDniEmpleado());
            ps.setString(4, persona.getCorreoEmpleado());
            ps.setString(5, persona.getCelularEmpleado());
            ps.setString(6, persona.getCodigoUbigeo());
            ps.setString(7, persona.getDireccionEmpleado());
            ps.setString(8, persona.getTurno());
            ps.setString(9, "A");
            ps.setInt(10, persona.getCodigoEmpleado());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en EmpleadoImpl/Modificar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(Empleado persona) throws Exception {
        this.conectar();
        try {
            String sql = "UPDATE EMPLEADO set ESTEMP=? where IDEMP=?";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, persona.getEstadoEmpleado());
            ps.setInt(2, persona.getCodigoEmpleado());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            System.out.println("Error en EmpleadoImpl/Eliminar " + e.getMessage());
        }
    }

    public void restaurar(Empleado persona) throws Exception {
        this.conectar();
        try {
            String sql = "UPDATE EMPLEADO set ESTEMP = 'A' where IDEMP=?";
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setInt(1, persona.getCodigoEmpleado());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error en EmpleadoImpl/Restaurar " + e.getMessage());
        } finally {
            this.cerrar();

        }
    }

    //@Override
    public List<Empleado> listarTodos(int tipo) throws InstantiationException, IllegalAccessException {
        this.conectar();
        List<Empleado> listado = new ArrayList<>();
        ResultSet rs;
        String sql = "";
        switch (tipo) {
            case 1:
                sql = "SELECT * FROM DatosEmpleados WHERE ESTEMP = 'A' ORDER BY IDEMP desc";
                break;
            case 2:
                sql = "SELECT * FROM DatosEmpleados WHERE ESTEMP = 'I' ORDER BY IDEMP desc";
                break;
            case 3:
                sql = "SELECT * FROM DatosEmpleados ORDER BY IDEMP desc";
                break;
        }
        try {
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setCodigoEmpleado(rs.getInt("IDEMP"));
                emp.setNombreEmpleado(rs.getString("NOMCOMEMP"));
                emp.setApellidoEmpleado(rs.getString("APELLIDOEMP"));
                emp.setDniEmpleado(rs.getString("DNIEMP"));
                emp.setCorreoEmpleado(rs.getString("COREMP"));
                emp.setCelularEmpleado(rs.getString("CELEMP"));
                emp.setCodigoUbigeo(rs.getString("CODUBI"));
                emp.setDepartamento(rs.getString("DEPAR"));
                emp.setProvincia(rs.getString("PROVI"));
                emp.setDistrito(rs.getString("DISTRI"));
                emp.setDireccionEmpleado(rs.getString("DIREMP"));
                emp.setTurno(rs.getString("TURNO"));
                emp.setEstadoEmpleado(rs.getString("ESTEMP"));
                listado.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("Error en EmpleadoImpl/Listar: " + e.getMessage());
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
            System.out.println("Error en EmpleadoImp/Ubigeo: " + e.getMessage());
        }
        return listUbig;

    }

    public void cambiarEstado(Empleado persona) throws Exception {
        try {
            String sql = "UPDATE EMPLEADO SET ESTEMP=? WHERE IDEMP=?";
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setString(1, persona.getEstadoEmpleado());
                ps.setInt(2, persona.getCodigoEmpleado());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error en EmpleadoImpl/CambiarEstado: " + e.getMessage());
        }
    }

    @Override
    public List<Empleado> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
