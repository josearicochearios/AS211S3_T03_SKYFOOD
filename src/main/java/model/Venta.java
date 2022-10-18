package model;

import java.util.Date;
import java.util.GregorianCalendar;
import lombok.Data;

@Data

public class Venta {

    private int codigoVenta;
    private Date fechaVenta= GregorianCalendar.getInstance().getTime();
    private int codigoEmpleado;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private String dniEmpleado;
    private int codigoCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String dniCliente;
    private int codigoPlatillo;
    private String nombrePlatillo;
    private int cantidaVendida;
    private double precioPlatillo = Double.valueOf(0);
    private double totalDeVenta = Double.valueOf(0);
    private String estadoVenta;

}