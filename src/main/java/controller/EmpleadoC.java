package controller;

import dao.EmpleadoImp;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import model.Empleado;
import model.Ubigeo;
import reporte.reporteEmpleado;
import lombok.Data;
import service.ReniecDniEmpleado;

@Named(value = "EmpleadoC")
@SessionScoped
@Data
public class EmpleadoC implements Serializable {

    private Empleado emp;
    private Ubigeo ubi;
    private EmpleadoImp dao;
    private List<Empleado> listadoEmp;
    private List<Ubigeo> listadoubi;
    private ReniecDniEmpleado reniecEmpleado;
    private Boolean disabled = false;
    private int tipo = 1;

    @PostConstruct
    public void init() {
        try {
            this.listar();
        } catch (Exception e) {
            System.out.println("Error en iniciar el PostConstruct en EmpleadoC: " + e.getMessage());
        }
    }

    public EmpleadoC() {
        emp = new Empleado();
        dao = new dao.EmpleadoImp();
        ubi = new Ubigeo();
    }

    public void BuscadorDniEmpleado() throws Exception {
        try {
            reniecEmpleado.BuscadorDniEmpleado(emp);
            if (reniecEmpleado.disabled == true) {
                this.disabled = true;
            } else {
                this.disabled = false;
                limpiar();
            }
        } catch (Exception e) {
            System.out.println("Error en buscarPorDNI_C " + e.getMessage());
        }
    }

    public void registrar() throws Exception {
        System.out.println("Intentando registrar un empleado... Atento!!!");
        try {
            if (emp.getDniEmpleado().length() == 8 && emp.getCelularEmpleado().length() == 9) {
                if (existe(emp, listadoEmp)) {
                    emp.setEstadoEmpleado("A");
                    dao.guardar(emp);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Registrado con éxito"));
                    limpiar();
                    listar();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Error Cliente ya registado"));

                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese datos coherentes"));

            }

        } catch (Exception e) {
            System.out.println("Error en EmpleadoC/Registrar:  " + e.getMessage());
        }
    }

    public void modificar() throws Exception {
        System.out.println("Intentando modificar un empleado... Atento!!!");
        try {
            if (emp.getDniEmpleado().length() == 8 && emp.getCelularEmpleado().length() == 9) {
                if (modificarExiste(emp, listadoEmp)) {
                    dao.modificar(emp);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
                    limpiar();
                    listar();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Numero de DNI repetido"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Ingrese datos coherentes"));
            }
        } catch (Exception e) {
            System.out.println("Error en EmpleadoC/Modificar: " + e.getMessage());
        }
    }

    public void eliminarA() throws Exception {
        try {
            emp.setEstadoEmpleado("I");
            dao.eliminar(emp);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Eliminado con éxito"));
            listar();

        } catch (Exception e) {
            System.out.println("Error en EmpleadoC/Eliminar: " + e.getMessage());
        }
    }

    public void restaurar() throws Exception {
        try {
            dao.restaurar(emp);
            listar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ok", "Se restauro correctamente"));
        } catch (Exception e) {
            System.out.println("error en EmpleadoC/Restaurar: " + e.getMessage());
        }
    }

//Listar los datos implementado en EmpleadoImp
    public void listar() throws Exception {
        try {
            listadoEmp = dao.listarTodos(tipo);
        } catch (Exception e) {
            System.out.println("Error en EmpleadoC/Listar: " + e.getMessage());
        }
    }

//Listar los datos de Ubigeo en ClienteImp
    public List<Ubigeo> listarUbig() {

        try {
            listadoubi = dao.listarUbigeo();
        } catch (Exception e) {
            System.out.println("Error en listar Ubigeo: " + e.getMessage());
        }
        return listadoubi;
    }

//Limpiar los datos del formulario al registrar
    public void limpiar() {
        emp = new Empleado();
    }

//Comprobar si los datos del Cliente ya existe en la Base de Datos - al registrar los datos
    public boolean existe(Empleado modelo, List<Empleado> listaModelo) {
        for (Empleado empl : listaModelo) {
            if (modelo.getDniEmpleado().equals(empl.getDniEmpleado())) {
                return false;
            }
        }
        return true;
    }

//Comprobar si los datos del Cliente ya existe en la Base de Datos - al momento de modificar los datos
    public boolean modificarExiste(Empleado modelo, List<Empleado> listaModelo) {
        for (Empleado empl : listaModelo) {
            if (modelo.getDniEmpleado().equals(empl.getDniEmpleado())) {
                return modelo.getCodigoEmpleado() == empl.getCodigoEmpleado();
            }
        }

        return true;
    }

    public void ReportePDF() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        reporteEmpleado reCliente = new reporteEmpleado();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("reportes/Empleado.jasper");
        reCliente.getReportePdf(root);
        FacesContext.getCurrentInstance().responseComplete();
    }

}
