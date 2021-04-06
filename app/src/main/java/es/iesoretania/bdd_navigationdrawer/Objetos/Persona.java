package es.iesoretania.bdd_navigationdrawer.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

public class Persona implements Parcelable {
    private String dni;
    private String nombre;
    private String apellidos;
    private String domicilio = null;
    private int cp;
    private String poblacion;
    private String provincia;
    private String telefono;
    private String email;
    private String observaciones;

    public Persona() {
    }

    public Persona(String dni, String nombre, String apellidos) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Persona(String dni, String nombre, String apellidos, String domicilio, int cp, String poblacion, String provincia, String telefono, String email, String observaciones) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.domicilio = domicilio;
        this.cp = cp;
        this.poblacion = poblacion;
        this.provincia = provincia;
        this.telefono = telefono;
        this.email = email;
        this.observaciones = observaciones;
    }

    protected Persona(Parcel in) {
        dni = in.readString();
        nombre = in.readString();
        apellidos = in.readString();
        domicilio = in.readString();
        cp = in.readInt();
        poblacion = in.readString();
        provincia = in.readString();
        telefono = in.readString();
        email = in.readString();
        observaciones = in.readString();
    }

    public static final Creator<Persona> CREATOR = new Creator<Persona>() {
        @Override
        public Persona createFromParcel(Parcel in) {
            return new Persona(in);
        }

        @Override
        public Persona[] newArray(int size) {
            return new Persona[size];
        }
    };

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dni);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeString(domicilio);
        dest.writeInt(cp);
        dest.writeString(poblacion);
        dest.writeString(provincia);
        dest.writeString(telefono);
        dest.writeString(email);
        dest.writeString(observaciones);
    }
}

