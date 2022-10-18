package controller;

import dao.ProveedorImp;
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
import model.Proveedor;
import model.Ubigeo;
import reporte.reporteProveedor;
import lombok.Data;
import service.SunatRucProveedor;

@Named(value = "ProveedorC")
@SessionScoped
@Data
public class ProveedorC implements Serializable {

    private Proveedor pro;
    private Ubigeo ubi;
    private ProveedorImp dao;
    private List<Proveedor> listadoprov;
    private List<Ubigeo> listadoubi;
    private SunatRucProveedor sunatProveedor;
    private Boolean disabled = false;
    private int tipo = 1;

    @PostConstruct
    public void init() {
        try {
            this.listar();
        } catch (Exception e) {
            System.out.println("controller.PlatilloC.init()");
        }
    }

    public ProveedorC() {
        pro = new Proveedor();
        dao = new ProveedorImp();
        ubi = new Ubigeo();
    }

    public void BuscadorRucProveedor() throws Exception {
        try {
            sunatProveedor.BuscadorRucProveedor(pro);
            if (sunatProveedor.disabled == true) {
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
        System.out.println("Intentando registrar un proveedor... Atento!!!");
        try {
            if (pro.getRucProveedor().length() == 11 && pro.getCelularProveedor().length() == 9) {
                if (existe(pro, listadoprov)) {
                    pro.setEstadoProveedor("A");
                    dao.guardar(pro);
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
        System.out.println("Intentando modificar un proveedor... Atento!!!");
        try {
            if (pro.getRucProveedor().length() == 11 && pro.getCelularProveedor().length() == 9) {
                if (modificarExiste(pro, listadoprov)) {
                    dao.modificar(pro);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Modificado con éxito"));
                    limpiar();
                    listar();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Numero de RUC repetido"));
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
            pro.setEstadoProveedor("I");
            dao.eliminar(pro);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Eliminado con éxito"));
            listar();

        } catch (Exception e) {
            System.out.println("Error en ProveedorC/Eliminar" + e.getMessage());
        }
    }

    public void restaurar() throws Exception {
        try {
            dao.restaurar(pro);
            listar();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ok", "Se restauro correctamente"));
        } catch (Exception e) {
            System.out.println("error en ProveedorC/Restaurar: " + e.getMessage());
        }
    }

//Listar los datos implementado en ProveedorImp
    public void listar() throws Exception {
        try {
            listadoprov = dao.listarTodos(tipo);
        } catch (Exception e) {
            System.out.println("Error en ProveedorC/Listar: " + e.getMessage());
        }
    }

//Limpiar los datos del formulario al registrar
    public void limpiar() {
        pro = new Proveedor();
    }

    //Comprobar si los datos del Cliente ya existe en la Base de Datos - al registrar los datos
    public boolean existe(Proveedor modelo, List<Proveedor> listaModelo) {
        for (Proveedor prov : listaModelo) {
            if (modelo.getRucProveedor().equals(prov.getRucProveedor())) {
                return false;
            }
        }
        return true;
    }

//Comprobar si los datos del Cliente ya existe en la Base de Datos - al momento de modificar los datos
    public boolean modificarExiste(Proveedor modelo, List<Proveedor> listaModelo) {
        for (Proveedor prov : listaModelo) {
            if (modelo.getRucProveedor().equals(prov.getRucProveedor())) {
                return modelo.getCodigoProveedor() == prov.getCodigoProveedor();
            }
        }

        return true;
    }

//Listar los datos de Ubigeo en ClienteImp
    public List<Ubigeo> listarUbig() {

        try {
            listadoubi = dao.listarUbigeo();
        } catch (Exception e) {
            System.out.println("Error en listar Ubigeo" + e.getMessage());
        }
        return listadoubi;
    }

    public void ReportePDF() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        reporteProveedor reCliente = new reporteProveedor();
        FacesContext facescontext = FacesContext.getCurrentInstance();
        ServletContext servletcontext = (ServletContext) facescontext.getExternalContext().getContext();
        String root = servletcontext.getRealPath("reportes/Proveedor.jasper");
        reCliente.getReportePdf(root);
        FacesContext.getCurrentInstance().responseComplete();
    }
}
