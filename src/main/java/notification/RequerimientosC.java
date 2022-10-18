package notification;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.ToggleEvent;

@Named(value = "RequerimientosC")
@RequestScoped
public class RequerimientosC {

    public void NotificacionRequerimientos(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Requerimientos Fncionales: ", "Visibilidad:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
