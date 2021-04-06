package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.R;


public class Fragment_BorrarBD extends Fragment {
    private Button Borrartodo;
    private Button Borrarcorreo;
    int ErrorCorreo,ErrorUsuarios,ErrorRegistros;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.fragment_borrar_bdd,container,false);
        Borrartodo=(Button) view.findViewById(R.id.btnBorrarToda);
        Borrarcorreo=(Button) view.findViewById(R.id.btnBorrarCorreo);
        ErrorCorreo=0;
        ErrorUsuarios=0;
        ErrorRegistros=0;

        Borrartodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setMessage("¿Estas seguro que quieres borrar el correo?").setTitle("Alerta de Borrado");
                builder.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrartodo(v);

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();



            }
        });

        Borrarcorreo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setMessage("¿Estas seguro que quieres borrar el correo?").setTitle("Alerta de Borrado");
                builder.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrarcorreo(v);

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
            }
        });

        return  view;
    }

    /**
     * @brief Función que borra por completo la base de datos llamando a las funciones que la borran parcialmente
     * @param v
     */
    private void borrartodo(View v) {
        borrarcorreo(v);
        borrarregistros(v);
        borrarpersonas(v);

    }

    /**
     * @brief Función que borra el registro de entrada y salida
     * @param v
     */
    private void borrarregistros(View v) {
        Administracion_BDD admin = new Administracion_BDD(v.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        Cursor cursor;
        String pr;
        cursor=baseDeDatos.rawQuery("select * from empleados_abriendoCamino",null);
        int cantidad;
        if(cursor.moveToFirst())
        {
            do {
                pr=cursor.getString(0);
                cantidad=baseDeDatos.delete("empleados_abriendoCamino","id='"+pr+"'",null);
                if(cantidad==0)
                {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

                }

            }while(cursor.moveToNext());
        }
        else
        {
            Toast.makeText(getContext(), "No hay registros", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        baseDeDatos.close();
        admin.close();
    }

    /**
     * @brief Función que borra el registro de usuarios que han pasado por la empresa
     * @param v
     */
    private void borrarpersonas(View v) {
        Administracion_BDD admin = new Administracion_BDD(v.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        Cursor cursor = baseDeDatos.rawQuery("select * from persona", null);
        String pr;

        cursor=baseDeDatos.rawQuery("select * from persona",null);
        int cantidad;
        if(cursor.moveToFirst())
        {
            do {
                pr=cursor.getString(0);
                cantidad=baseDeDatos.delete("persona","dni='"+pr+"'",null);
                if(cantidad==0)
                {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }while(cursor.moveToNext());
        }
        else
        {
            Toast.makeText(getContext(), "No hay registros", Toast.LENGTH_SHORT).show();

        }
        cursor.close();
        baseDeDatos.close();
        admin.close();

    }

    /**
     * @brief Función que borra el correo destinatario al que le llegan las exportaciones
     * @param v
     */
    private void borrarcorreo(View v) {
        Administracion_BDD admin = new Administracion_BDD(v.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        Cursor cursor = baseDeDatos.rawQuery("select * from datos_correos", null);
        if(cursor.moveToFirst())
        {
            String aux=cursor.getString(0);
            int cantidad = baseDeDatos.delete("datos_correos","destinatario='" + aux + "'", null);
            if(cantidad==1)
            {
                Toast.makeText(getContext(), "Borrado exitosamente", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Error al borrar", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getContext(), "Debes de introducir un correo primero", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        baseDeDatos.close();
        admin.close();
    }
}