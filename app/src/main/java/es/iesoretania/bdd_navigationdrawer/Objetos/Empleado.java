package es.iesoretania.bdd_navigationdrawer.Objetos;

import java.sql.Blob;

public class Empleado {

    private String dni;
    private String fecha;
    private byte[] imagen;
    private String fechaSalida;

    public Empleado(String dni, String fecha, byte[] imagen,String fechaSalida) {
        this.dni = dni;
        this.fecha = fecha;
        this.imagen = imagen;
        this.fechaSalida = fechaSalida;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fecha_salida) {
        this.fechaSalida = fecha_salida;
    }
}
