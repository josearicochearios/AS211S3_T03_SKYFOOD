package model;

import lombok.Data;

@Data

public class CompraDetalle {

    private int codigoCompraDetalle;
    private int codigoCompra;
    private int cantidadProducto;
    private Double subTotalDeCompra;
    private int codigoProducto;

    @Override
    public String toString() {
        return "CompraDetalle{" + "codigoCompraDetalle=" + codigoCompraDetalle + ", codigoCompra=" + codigoCompra + ", cantidadProducto=" + cantidadProducto + ", subTotalDeCompra=" + subTotalDeCompra + ", codigoProducto=" + codigoProducto + '}';
    }

}
