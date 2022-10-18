package controller;

import dao.ClienteImp;
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
import model.Cliente;
import model.Ubigeo;
import reporte.reporteCliente;
import service.ReniecDniCliente;
import lombok.Data;

@Named(value = "ClienteC")
@SessionScoped
@Data
public class ClienteC implements Serializable {

    private Cliente cli;
    private Ubigeo ubi;
    private ClienteImp dao;
    private List<Cliente> listadoCli;
    private List<Ubigeo> listadoUbi;
    private ReniecDniCliente reniecCliente;
    private Boolean disabled = false;
    private int tipo = 1;

    @PostConstruct
    public void init() {
        try {
            this.listar();
        } catch (Exception e) {
            System.out.println("Error en iniciar el PostConstruct en ClienteC: " + e.getMessage());

        }
    }

    public ClienteC() {
        cli = new Cliente();
        dao = new ClienteImp();
        ubi = new Ubigeo();
    }

    public void BuscadorDniCliente() throws Exception {
        try {
            reniecCliente.BuscadorDniCliente(cli);
            if (reniecCliente.disabled == true) {
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
        System.out.println("Intentando registrar un cliente... Atento!!!");
        try {
            if (cli.getDniCliente().length() == 8 && cli.getCelularCliente().length() == 9) {
                if (existe(cli, listadoCli)) {
                    cli.setEstadoCliente("A");
                    dao.guardar(cli);
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
            System.out.println("Error en registrarC " + e.getMessage());
        }
    }

    public void modificar() throws Exception {
        System.out.println("Intentando modificar un cliente... Atento!!!");
        try {
            if (cli.getDniCliente().length() == 8 && cli.getCelularCliente().length() == 9) {
                if (modificarExiste(cli, listadoCli)) {
                    dao.modificar(cli);
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
            System.out.println("Error en modificarC" + e.getMessage());
        }
    }

    public void eliminarA() throws Exception {
        try {
            cli.setEstadoCliente("I");
            dao.eliminar(cli);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Eliminado con éxito"));
            limpiar();
            listar();
        } catch (Exception e) {
            System.out.println("Error en ClienteC/Eliminar" + e.getMessage());
        }
    }

    public void restaurar() throws Exception {
        try {
            dao.restaurar(cli);
            listar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ok", "Se restauro correctamente"));
        } catch (Exception e) {
            System.out.println("error en ClienteC/Restaurar: " + e.getMessage());
        }
    }

//Listar los datos implementado en ClienteImp
    public void listar() throws Exception {
        try {
            listadoCli = dao.listarTodos(tipo);
        } catch (Exception e) {
            System.out.println("Error en ClienteC/Listar: " + e.getMessage());
        }

    }

//Listar los datos de Ubigeo en ClienteImp
    public List<Ubigeo> listarUbig() {

        try {
            listadoUbi = dao.listarUbigeo();
        } catch (Exception e) {
            System.out.println("Error en listar Ubigeo" + e.getMessage());
        }
        return listadoUbi;
    }

//Limpiar los datos del formulario al registrar
    public void limpiar() throws Exception {
        cli = new Cliente();
    }

//Comprobar si los datos del Cliente ya existe en la Base de Datos - al registrar los datos
    public boolean existe(Cliente modelo, List<Cliente> listaModelo) {
        for (Cliente clie : listaModelo) {
            if (modelo.getDniCliente().equals(clie.getDniCliente())) {
                return false;
            }
        }
        return true;
    }

//Comprobar si los datos del Cliente ya existe en la Base de Datos - al momento de modificar los datos
    public boolean modificarExiste(Cliente modelo, List<Cliente> listaModelo) {
        for (Cliente clie : listaModelo) {
            if (modelo.getDniCliente().equals(clie.getDniCliente())) {
                return modelo.getCodigoCliente() == clie.getCodigoCliente();
            }
        }

        return true;
    }

    public void ReportePDF() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        reporteCliente reCliente = new reporteCliente();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("/reportes/Cliente.jasper");
        reCliente.getReportePdf(root);
        FacesContext.getCurrentInstance().responseComplete();
    }
}
