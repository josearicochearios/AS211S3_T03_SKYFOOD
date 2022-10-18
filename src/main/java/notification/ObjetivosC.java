package notification;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.ToggleEvent;

@Named(value = "ObjetivosC")
@RequestScoped
public class ObjetivosC {

    public void NotificacionObjetivos(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Objetivos Generales: ", "Visibilidad:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
