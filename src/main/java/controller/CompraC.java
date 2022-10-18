package controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Named(value = "compraC")
@SessionScoped
public class CompraC implements Serializable {

    public CompraC() {
    }
    
}
