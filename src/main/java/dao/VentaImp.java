package dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cliente;
import model.Empleado;
import model.Platillo;
import model.Venta;
import model.Ventadetalle;

public class VentaImp extends Conexion implements ICRUD<Venta> {

    private static final Logger LOG = Logger.getLogger(VentaImp.class.getName());

    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void guardar(Venta venta) throws Exception {
        String sql = "INSERT INTO VENTA (FECVEN, IDEMP, IDCLI, ESTVEN) VALUES (?,?,?,?)";
        try {
            System.out.println("Esta es mi fecha" + venta.getFechaVenta());
            System.out.println("Esta es mi fecha" + formatter.format(venta.getFechaVenta()));
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, formatter.format(venta.getFechaVenta()));
            ps.setInt(2, venta.getCodigoEmpleado());
            ps.setInt(3, venta.getCodigoCliente());
            ps.setString(4, "A");
            ps.executeUpdate();
            ps.close();
        } catch (Exception E) {
            System.out.println("Error en VentaImp/guardar" + E.getMessage());
        }
    }

    @Override
    public void modificar(Venta venta) throws Exception {
        //Nose utliza esta parte del codigo por defecto
    }

    @Override
    public void eliminar(Venta venta) throws Exception {
        //Nose utliza esta parte del codigo por defecto 
    }

    @Override
    public List<Venta> listarTodos() throws Exception {
        return null;

    }

    public void cambiarEstado(Venta venta) throws Exception {
        try {
            String sql = "UPDATE VENTA SET ESTVEN=? WHERE IDVEN=?";
            try ( PreparedStatement ps = this.getCn().prepareStatement(sql)) {
                ps.setString(1, venta.getEstadoVenta());
                ps.setInt(2, venta.getCodigoVenta());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error en VentaImp/cambiarEstado: " + e.getMessage());
        }
    }

    public int ventasMaximas() throws Exception {
        this.conectar();
        int nroVentas = 0;
        String sql = "SELECT MAX(IDVEN) FROM VENTA";
        try {
            Statement st = this.getCn().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                nroVentas = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error en ObtenerVentasMaxima/VentaImp: " + e.getMessage());
        }
        return nroVentas;

    }

    public void registrarDetalle(Ventadetalle ventadetalle) throws InstantiationException, IllegalAccessException {
        this.conectar();
        String sql = "INSERT INTO VENTA_DETALLE (IDVEN,CANTVEND,SUBTOTVEN,IDPLA) VALUES (?,?,?,?)";
        try {
            LOG.log(Level.INFO, "RegistrarDetalle DTO {0}", ventadetalle.toString());
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setInt(1, ventadetalle.getFkVenta());
            ps.setDouble(2, ventadetalle.getCantidadVentaDetalle());
            ps.setDouble(3, ventadetalle.getSubtotalPlatillo());
            ps.setInt(4, ventadetalle.getFkPlatillo());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en RegistrarDetalle/VentaImp: " + e.getMessage());
        }
    }
    
        //@Override
    public List<Venta> listarVenta(int tipo) throws InstantiationException, IllegalAccessException {
        this.conectar();
        List<Venta> listado = new ArrayList<>();
        ResultSet rs;
        String sql = "";
        switch (tipo) {
            case 1:
                sql = "SELECT * FROM DetalleCompletoVenta ORDER BY IDVEN desc";
                break;
        }
        try {
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Venta v = new Venta();
                v.setCodigoVenta(rs.getInt("IDVEN"));
                v.setFechaVenta(rs.getDate("FECVEN"));
                v.setCodigoEmpleado(rs.getInt("IDEMP"));
                v.setNombreEmpleado(rs.getString("NOMCOMEMP"));
                v.setApellidoEmpleado(rs.getString("APELLIDOEMP"));
                v.setDniEmpleado(rs.getString("DNIEMP"));
                v.setCodigoCliente(rs.getInt("IDCLI"));
                v.setNombreCliente(rs.getString("NOMCOMCLI"));
                v.setApellidoCliente(rs.getString("APELLIDOCLI"));
                v.setDniCliente(rs.getString("DNICLI"));
                v.setCodigoPlatillo(rs.getInt("IDPLA"));
                v.setNombrePlatillo(rs.getString("NOMPLA"));
                v.setCantidaVendida(rs.getInt("CANTVEND"));
                v.setPrecioPlatillo(rs.getDouble("PREPLA"));
                v.setTotalDeVenta(rs.getDouble("SUBTOTVEN"));
                listado.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Error en VentaImpl/Listar: " + e.getMessage());
        } finally {
            this.cerrar();
        }
        return listado;
    }

    public void filtrarPersona(Cliente cli) throws Exception {
        this.conectar();
        try {
            String sql = "select * from CLIENTE where DNICLI =" + cli.getDniCliente();
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cli.setCodigoCliente(rs.getInt("IDCLI"));
                cli.setNombreCliente(rs.getString("NOMCOMCLI"));
                cli.setApellidoCliente(rs.getString("APELLIDOCLI"));
                cli.setDniCliente(rs.getString("DNICLI"));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al FiltrarCliente/VentaImp: " + e.getMessage());
        }
    }
    
    public void filtrarPersona1(Empleado emp) throws Exception {
        this.conectar();
        try {
            String sql = "select * from EMPLEADO where DNIEMP =" + emp.getDniEmpleado();
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                emp.setCodigoEmpleado(rs.getInt("IDEMP"));
                emp.setNombreEmpleado(rs.getString("NOMCOMEMP"));
                emp.setApellidoEmpleado(rs.getString("APELLIDOEMP"));
                emp.setDniEmpleado(rs.getString("DNIEMP"));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error al FiltrarEmpleado/VentaImp: " + e.getMessage());
        }
    }


    public void mostrarDatos(Platillo platillo) throws Exception {
        this.conectar();
        try {
            String sql = "Select * from PLATILLO where NOMPLA =?";
            PreparedStatement ps = this.getCn().prepareStatement(sql);
            ps.setString(1, platillo.getNombrePlatillo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                platillo.setCodigoPlatillo(rs.getInt("IDPLA"));
                platillo.setNombrePlatillo(rs.getString("NOMPLA"));
                platillo.setPrecioPlatillo(rs.getDouble("PREPLA"));
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Error en mostrar datos" + e.getMessage());
        }
    }

    public List<String> autocompletar(String consulta) throws Exception {
        this.conectar();
        Platillo platillo = new Platillo();
        List<String> listado = new ArrayList<>();
        String sql = "Select * from PLATILLO WHERE NOMPLA LIKE ?";
        try {
            PreparedStatement ps = this.getCn().prepareCall(sql);
            ps.setString(1, consulta + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                platillo.setNombrePlatillo(rs.getString("NOMPLA"));
                listado.add(rs.getString("NOMPLA"));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en autocompletar Menu " + e.getMessage());
        }
        return listado;

    }
}
