package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Funcionalidades.OcultarTeclado;
import es.iesoretania.bdd_navigationdrawer.R;

public class Fragment_Ajustes extends Fragment {
    EditText correo;
    Button guardar;
    String aux;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_ajustes,container,false);
        correo = (EditText) view.findViewById(R.id.etDestinatario);
        cargardatos(view);
        guardar = (Button) view.findViewById(R.id.btnGuardar);

        guardar.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { guardado(v);}});

        return view;
    }

    /**
     * @brief Función que guarda el correo en la base de datos
     * @param v
     */
    private void guardado(View v) {
        if(correo.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Debes rellenar todos los campos", Toast.LENGTH_LONG).show();
        }
        else
        {
            Administracion_BDD admin = new Administracion_BDD(v.getContext(), "abriendoCamino", null, 1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor cursor = baseDeDatos.rawQuery("select * from datos_correos", null);
            if(cursor.moveToFirst())
            {
                aux=cursor.getString(0);
                int cantidad = baseDeDatos.delete("datos_correos","destinatario='" + aux + "'", null);
                if(cantidad==1)
                {
                    String sql = "insert into datos_correos (destinatario) values(?)";
                    SQLiteStatement insert = baseDeDatos.compileStatement(sql);
                    insert.clearBindings();
                    insert.bindString(1,correo.getText().toString());
                    insert.executeInsert();
                    Toast.makeText(getContext(), "Nuevo correo modificado", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Error al guardar el correo", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                String sql = "insert into datos_correos (destinatario) values(?)";
                SQLiteStatement insert = baseDeDatos.compileStatement(sql);
                insert.clearBindings();
                insert.bindString(1,correo.getText().toString());
                insert.executeInsert();
                Toast.makeText(getContext(), "Correo guardado", Toast.LENGTH_SHORT).show();

            }
            cursor.close();
            baseDeDatos.close();
            admin.close();
            OcultarTeclado ocultarTeclado=new OcultarTeclado();
            ocultarTeclado.hideKeyboard(getActivity(),v);
            NavHostFragment.findNavController(Fragment_Ajustes.this).navigate(R.id.action_fragment_Ajustes_to_nav_inicio);
        }
    }


    /**
     * @brief Función que obtiene el correo guardado en la base de datos (en la tabla datos_correos)
     * @param view
     */
    private void cargardatos(View view) {
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        Cursor cursor = baseDeDatos.rawQuery("select * from datos_correos", null);
        if(cursor.moveToFirst())
        {
            correo.setHint(cursor.getString(0));
        }
        else
        {
            correo.setHint("Introduce un Mail");
        }
        cursor.close();
        baseDeDatos.close();
        admin.close();

    }


}