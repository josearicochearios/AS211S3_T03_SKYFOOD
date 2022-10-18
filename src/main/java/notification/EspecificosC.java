package notification;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.ToggleEvent;

@Named(value = "EspecificosC")
@RequestScoped
public class EspecificosC {

    public void NotificacionObjetivosEspecificos(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Objetivos Especificos: ", "Visibilidad:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
