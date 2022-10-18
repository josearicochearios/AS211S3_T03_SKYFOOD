package controller;

import dao.VentaImp;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import model.Cliente;
import model.Empleado;
import model.Platillo;
import model.Venta;
import model.Ventadetalle;
import lombok.Data;
import reporte.reporteCliente;

@Named(value = "VentaC")
@SessionScoped
@Data
public class VentaC implements Serializable {

    private int item;
    private double subtotal;
    private double total;
    private Venta venta;
    private Cliente cli;
    private Empleado emp;
    private Platillo platillo;
    private Ventadetalle ventadetalle;
    private VentaImp dao;
    private List<Venta> listadoven;
    private List<Empleado> listadoEmp;
    private List<Cliente> listadoCli;
    private List<Platillo> listadopla;
    private List<Ventadetalle> listadovendet;
    private int tipo = 1;

    public VentaC() {
        venta = new Venta();
        cli = new Cliente();
        emp = new Empleado();
        platillo = new Platillo();
        listadovendet = new ArrayList();
        ventadetalle = new Ventadetalle();
        dao = new VentaImp();
    }

    public void registrar() throws Exception {
        try {
            venta.setCodigoCliente(cli.getCodigoCliente());
            venta.setCodigoEmpleado(emp.getCodigoEmpleado());
            dao.guardar(venta);
            registrardetalle();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ok", "Registrado con éxito"));
        } catch (Exception e) {
            System.out.println("Error en registrar VentaC/registrar: " + e.getMessage());
        }
    }

    public void registrardetalle() throws Exception {
        int ultimoID = dao.ventasMaximas();

        for (int i = 0; i < listadovendet.size(); i++) {
            ventadetalle = new Ventadetalle();
            ventadetalle.setFkVenta(ultimoID);
            ventadetalle.setFkPlatillo(listadovendet.get(i).getFkPlatillo());
            ventadetalle.setCantidadVentaDetalle(listadovendet.get(i).getCantidadVentaDetalle());
            ventadetalle.setSubtotalPlatillo(listadovendet.get(i).getSubtotalPlatillo());
            dao.registrarDetalle(ventadetalle);
        }
    }

    public void filtrado() throws Exception {
        try {
            dao.filtrarPersona(cli);
            if (cli.getNombreCliente().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "DNI incorrecto, Datos no encontrados"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Datos encontrados con exito"));

            }
        } catch (Exception e) {
            System.out.println("Error en filtrar/Cliente/VentaC: " + e.getMessage());
        }
    }

    public void filtrado1() throws Exception {
        try {
            dao.filtrarPersona1(emp);
            if (emp.getNombreEmpleado().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "DNI incorrecto, Datos no encontrados"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Datos encontrados con exito"));

            }
        } catch (Exception e) {
            System.out.println("Error en filtrar/Empleado/VentaC: " + e.getMessage());
        }
    }

    public void mostrar() throws Exception {
        try {
            System.err.println("Se esta buscando los datos de los platos... Atento!!!");
            dao.mostrarDatos(platillo);
            System.out.println(platillo.getPrecioPlatillo());
        } catch (Exception e) {
            System.out.println("Error en MostrarPlatillos/VentaC" + e.getMessage());
        }
    }

    public List<String> autocompletePrueba(String query) throws Exception {
        try {
            return dao.autocompletar(query);

        } catch (Exception e) {
            System.out.println("Erro please" + e.getMessage());
            throw e;
        }
    }

    public void listaTemporal() throws Exception {
        item = item + 1;
        total = 0.0;
        subtotal = platillo.getCantidadPlatillo() * platillo.getPrecioPlatillo();

        ventadetalle = new Ventadetalle();

        ventadetalle.setFkPlatillo(platillo.getCodigoPlatillo());
        ventadetalle.setCantidadVentaDetalle(platillo.getCantidadPlatillo());
        ventadetalle.setNombrePlatillo(platillo.getNombrePlatillo());
        ventadetalle.setPrecioPlatillo(platillo.getPrecioPlatillo());
        ventadetalle.setSubtotalPlatillo(subtotal);
        listadovendet.add(ventadetalle);
        calcularTotalVenta();
        //Se mostrará los datos del platillo y el precio subtotal.
        System.out.println(platillo.getCantidadPlatillo());
        System.out.println("Subtotal" + ventadetalle.getSubtotalPlatillo());
        platillo = new Platillo();

    }

    public void calcularTotalVenta() {
        for (int i = 0; i < listadovendet.size(); i++) {
            total = total + listadovendet.get(i).getSubtotalPlatillo();
        }
    }

    public void listarVenta() {
        try {
            listadoven = dao.listarVenta(tipo);

        } catch (Exception e) {
            System.out.println("Error en controlador listarVenta" + e.getMessage());
        }
    }

    public void limpiar() {
        venta = new Venta();
        cli = new Cliente();
        emp = new Empleado();
        platillo = new Platillo();
        listadovendet = new ArrayList();
        ventadetalle = new Ventadetalle();
        dao = new VentaImp();
    }
    
    public void ReportePDF() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        reporteCliente reCliente = new reporteCliente();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("/reportes/Ventas.jasper");
        reCliente.getReportePdf(root);
        FacesContext.getCurrentInstance().responseComplete();
    }
}
