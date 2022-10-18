package notification;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.ToggleEvent;

@Named(value = "PortadaC")
@RequestScoped
public class PortadaC {

    public void NotificacionPortada(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Descripción del Modelo de Negocio: ", "Visibilidad:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
