package model;

import lombok.Data;

@Data

public class Platillo {

    private int codigoPlatillo;
    private String nombrePlatillo;
    private String descripcionPlatillo;
    private Double precioPlatillo = Double.valueOf(0);
    private int stockPlatillo;
    private String estadoPlatillo;
    private int cantidadPlatillo;

    public Platillo() {
        precioPlatillo = 0d; //Para implementarlo en Venta
    }
}