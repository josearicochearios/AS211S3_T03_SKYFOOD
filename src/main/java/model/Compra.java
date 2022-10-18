package model;

import java.util.Date;
import java.util.GregorianCalendar;
import lombok.Data;

@Data

public class Compra {

    private int codigoCompra;
    private Date fechaCompra = GregorianCalendar.getInstance().getTime();
    private int codigoProveedor;
    private String estadoCompra;
    private int codigoPersona;

    @Override
    public String toString() {
        return "Compra{" + "codigoCompra=" + codigoCompra + ", fechaCompra=" + fechaCompra + ", codigoProveedor=" + codigoProveedor + ", estadoCompra=" + estadoCompra + ", codigoPersona=" + codigoPersona + '}';
    }

}
