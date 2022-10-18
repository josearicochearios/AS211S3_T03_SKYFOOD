package service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.Empleado;

public class ReniecDniEmpleado {

    public static Boolean disabled = true;

    public static void BuscadorDniEmpleado(Empleado emp) throws Exception {
        String dni = emp.getDniEmpleado();
        String token = "?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImNhcmxvcy5zYW5jaGV6QHZhbGxlZ3JhbmRlLmVkdS5wZSJ9.uQNTFAu2cl_8wP-XCStAGqLpqPdQvLAiqSl8426yJnc";
        String enlace = "https://dniruc.apisperu.com/api/v1/dni/" + dni + token;
        try {
            URL url = new URL(enlace);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            if (root.isJsonObject()) {
                JsonObject rootobj = root.getAsJsonObject();

                String apellido_paterno = rootobj.get("apellidoPaterno").getAsString();
                String apellido_materno = rootobj.get("apellidoMaterno").getAsString();
                String nombres = rootobj.get("nombres").getAsString();

                emp.setNombreEmpleado(nombres);
                emp.setApellidoEmpleado(apellido_paterno + " " + apellido_materno);
                System.out.println(nombres + "\n" + apellido_paterno + "\n" + apellido_materno + "\n");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "BUSQUEDA", "DNI Exitoso"));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Busqueda", "DNI no encontrado"));
        }
    }

    public static void main(String[] args) throws Exception {
        Empleado emp = new Empleado();
        emp.setDniEmpleado("73829932");
        BuscadorDniEmpleado(emp);
    }
}
