package model;

import lombok.Data;

@Data

public class Ventadetalle {

    private int codigoVentaDetalle;
    private int fkPlatillo;
    private int fkVenta;
    private int cantidadVentaDetalle;
    private Double subtotalVentaDetalle = Double.valueOf(0); 
    private String nombrePlatillo;
    private double precioPlatillo;
    private double subtotalPlatillo;

    public Ventadetalle() {
        subtotalVentaDetalle = 0d; //Para implementarlo en Venta
    }
    
    @Override
    public String toString() {
        return "Ventadetalle{" + "codigoVentaDetalle=" + codigoVentaDetalle + ", fkPlatillo=" + fkPlatillo + ", fkVenta=" + fkVenta + ", cantidadVentaDetalle=" + cantidadVentaDetalle + ", subtotalVentaDetalle=" + subtotalVentaDetalle + ", nombrePlatillo=" + nombrePlatillo + ", precioPlatillo=" + precioPlatillo + ", subtotalPlatillo=" + subtotalPlatillo + '}';
    }

    
    
}
