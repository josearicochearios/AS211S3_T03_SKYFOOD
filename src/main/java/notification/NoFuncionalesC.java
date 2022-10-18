package notification;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.ToggleEvent;

@Named(value = "NoFuncionalesC")
@RequestScoped
public class NoFuncionalesC {

    public void NotificacionNoFuncionales(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Requerimientos No Funcionales: ", "Visibilidad:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
