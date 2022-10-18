package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("TurnoConverter")
public class turnoConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        return string;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object t) {
        String tipo = "";
        if (t != null) {
            tipo = (String) t;
            switch (tipo) {
                case "T": tipo = "Tardesita"; break;
                case "M": tipo = "Mañanita"; break;
                case "N": tipo = "Nochesita"; break;
                case "t": tipo = "Tardesita"; break;
                case "m": tipo = "Mañanita"; break;
                case "n": tipo = "Nochesita"; break;
            }
        }

        return tipo;
    }
}