package controller;

import dao.PlatilloImp;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import model.Platillo;
import reporte.reportePlatillo;
import lombok.Data;

@Named(value = "PlatilloC")
@SessionScoped
@Data
public class PlatilloC implements Serializable {

    private Platillo platillo;
    private PlatilloImp dao;
    private List<Platillo> listadopla;
    private int tipo = 1;

    @PostConstruct
    public void init() {
        try {
            this.listar();
        } catch (Exception e) {
            System.out.println("controller.PlatilloC.init()");
        }
    }

    public PlatilloC() {
        platillo = new Platillo();
        dao = new PlatilloImp();
    }

    public void registrar() throws Exception {
        try {
            dao.guardar(platillo);
            listar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ok", "Registrado con éxito"));
        } catch (Exception e) {
            System.out.println("Error en registrar PlatilloC/registrar: " + e.getMessage());
        }
    }

    public void modificar() throws Exception {
        try {
            dao.modificar(platillo);
            listar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
        } catch (Exception e) {
            System.out.println("Error en modificar PlatilloC/modificar" + e.getMessage());
        }
    }

    public void eliminarA() throws Exception {
        try {
            platillo.setEstadoPlatillo("I");
            dao.eliminar(platillo);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Eliminado con éxito"));
            listar();

        } catch (Exception e) {
            System.out.println("Error en eliminarC/ClienteC" + e.getMessage());
        }
    }

    public void restaurar() throws Exception {
        try {
            dao.restaurar(platillo);
            listar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ok", "Se restauro correctamente"));
        } catch (Exception e) {
            System.out.println("error en restaurar/ClienteC " + e);
        }
    }

    public void listar() throws Exception {
        try {
            listadopla = dao.listarTodos(tipo);
        } catch (Exception e) {
            System.out.println("Error en ListarPlatilloC: " + e.getMessage());
        }

    }

    public void limpiar() {
        platillo = new Platillo();
    }

    public void ReportePDF() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        reportePlatillo reCliente = new reportePlatillo();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("reportes/Platillos.jasper");
        reCliente.getReportePdf(root);
        FacesContext.getCurrentInstance().responseComplete();
    }
}
