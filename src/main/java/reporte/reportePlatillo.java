package reporte;

import dao.Conexion;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperRunManager;

public class reportePlatillo extends Conexion{

    public void getReportePdf(String root) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        this.conectar();
        try {
            File reportfile = new File(root);
            Map<String, Object> parameter = new HashMap<String, Object>();
            byte[] bytes = JasperRunManager.runReportToPdf(reportfile.getPath(), parameter, this.getCn());
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setContentLength(bytes.length);
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();
            outputStream.close();
            System.out.println("Se genero el reporte de los platillos correctamente, gracias");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
