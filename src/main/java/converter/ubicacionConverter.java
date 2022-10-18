package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("UbicaciónConverter")
public class ubicacionConverter implements Converter {

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
                case "1": tipo = "Lima - Cañete - Imperial"; break;
                case "2": tipo = "Lima - Cañete - Nuevo Imperial"; break;
                case "3": tipo = "Lima - Cañete - San Vicente"; break;
                case "4": tipo = "Lima - Cañete - Cerro Azul"; break;
                case "5": tipo = "Lima - Cañete - Mala"; break;
            }
        }

        return tipo;
    }
}
