package es.iesoretania.bdd_navigationdrawer.BaseDeDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Administracion_BDD extends SQLiteOpenHelper {

    public Administracion_BDD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * @brief Función que crea las tablas de la base de datos
     * @param db
     * @table empleados_abriendoCamino: Tabla donde se guardan todas las fichas
     * @table datos_correos: Tabla que contiene el correo del usuario al que se le enviará
     * @table persona: Contiene los datos de todas las personas que han entrado a la empresa
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table empleados_abriendoCamino(id integer primary key autoincrement, dni text, fecha TEXT, img BLOB, fechaSalida TEXT)");
        db.execSQL("create table datos_correos (destinatario TEXT primary key)");
        db.execSQL("create table persona(dni text primary key, nombre text, apellidos text, domicilio text, cp integer, poblacion text, provincia text, telefono text, email text, observaciones text)");
        db.execSQL("create table tutelado(dni text primary key, nombre text, apellidos text, edad integer, domicilio text, cp integer, poblacion text, provincia text, telefono text, email text, observaciones text)");
        db.execSQL("create table rel_tutelados (id integer primary key autoincrement, dni_tutelado text, dni_tutor text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
