/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package RedBayesiana;

/**
 *
 * @author Silvia Vacacela
 */
public class Parametro {
    
     private String nombre;
     private String estado;

    public Parametro() {
    }

    public Parametro(String nombre, String estado) {
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
     
}
