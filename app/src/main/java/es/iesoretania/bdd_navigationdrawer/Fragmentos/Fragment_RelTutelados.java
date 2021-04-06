package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Objetos.Adaptador_Tutores;
import es.iesoretania.bdd_navigationdrawer.Objetos.CuadroTutores;
import es.iesoretania.bdd_navigationdrawer.Objetos.Cuadro_Tutelados;
import es.iesoretania.bdd_navigationdrawer.Objetos.Persona;
import es.iesoretania.bdd_navigationdrawer.R;


public class Fragment_RelTutelados extends Fragment implements PopupMenu.OnMenuItemClickListener {
    Spinner spnRelTutelados;
    EditText etRelTutelados;
    ListView lvRelTutelados;
    Switch swRelTutelados;
    Button btnAgregar;
    String dni;
    ArrayList<Persona> lista;
    int posicionPulsada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__rel_tutelados, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spnRelTutelados = view.findViewById(R.id.spnOpcionesTutores);
        etRelTutelados = view.findViewById(R.id.etBuscarTutores);
        lvRelTutelados = view.findViewById(R.id.lvRelTutores);
        swRelTutelados = view.findViewById(R.id.swRelTutores);
        btnAgregar = view.findViewById(R.id.btnCrearRelTutor);

        String[] opciones = new String[] {"dni", "nombre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, opciones);
        spnRelTutelados.setAdapter(adapter);

        if(getArguments() != null){
            Fragment_RelTuteladosArgs args = Fragment_RelTuteladosArgs.fromBundle(getArguments());
            dni = args.getDni();
        }

        swRelTutelados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swRelTutelados.isChecked()){
                    if(etRelTutelados.getText().toString().isEmpty())
                        listar(view, 1);
                    else{
                        if(spnRelTutelados.getSelectedItem().toString().equals("dni"))
                            listar(view, 4);
                        else if(spnRelTutelados.getSelectedItem().toString().equals("nombre"))
                            listar(view, 5);
                    }
                }else{
                    if(etRelTutelados.getText().toString().isEmpty()){
                        listar(view, 0);
                    }else{
                        if(spnRelTutelados.getSelectedItem().toString().equals("dni"))
                            listar(view, 2);
                        else if(spnRelTutelados.getSelectedItem().toString().equals("nombre"))
                            listar(view, 3);
                    }
                }
            }
        });

        etRelTutelados.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(spnRelTutelados.getSelectedItem().toString().equals("dni")){
                    if(swRelTutelados.isChecked())
                        listar(view, 4);
                    else
                        listar(view, 2);
                }else if(spnRelTutelados.getSelectedItem().toString().equals("nombre")){
                    if(swRelTutelados.isChecked())
                        listar(view, 5);
                    else
                        listar(view, 3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spnRelTutelados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnRelTutelados.getItemAtPosition(position).toString().equals("dni")){
                    if(swRelTutelados.isChecked())
                        listar(getView(), 4);
                    else
                        listar(getView(), 2);
                }else if(spnRelTutelados.getItemAtPosition(position).toString().equals("nombre")){
                    if(swRelTutelados.isChecked())
                        listar(getView(), 5);
                    else
                        listar(getView(), 3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Fragment_RelTutelados.this).navigate(Fragment_RelTuteladosDirections.actionNavRelTuteladosToNavTutor(dni));
            }
        });
    }

    private void listar(View view, int opcion) {
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        lista = new ArrayList<>();
        Cursor cursor = null;

        switch (opcion) {
            case 0:
                cursor = baseDeDatos.rawQuery("select * from persona where cp is not null and dni in (select dni_tutor from rel_tutelados where dni_tutelado='" + this.dni  + "')", null);
                break;
            case 1:
                cursor = baseDeDatos.rawQuery("select * from persona where dni in (select dni from empleados_abriendoCamino where fechaSalida = '$') and cp is not null and dni in (select dni_tutor from rel_tutelados where dni_tutelado='" + this.dni  + "')", null);
                break;
            case 2:
                cursor = baseDeDatos.rawQuery("select * from persona where dni like '%" + etRelTutelados.getText().toString() + "%' and cp is not null and dni in (select dni_tutor from rel_tutelados where dni_tutelado='" + this.dni  + "')", null);
                break;
            case 3:
                cursor = baseDeDatos.rawQuery("select * from persona where nombre like '%" + etRelTutelados.getText().toString() + "%' and cp is not null and dni in (select dni_tutor from rel_tutelados where dni_tutelado='" + this.dni  + "')", null);
                break;
            case 4:
                cursor = baseDeDatos.rawQuery("select * from persona where dni like '%" + etRelTutelados.getText().toString() + "%' and dni in (select dni from empleados_abriendoCamino where fechaSalida = '$') and cp is not null and dni in (select dni_tutor from rel_tutelados where dni_tutelado='" + this.dni  + "')", null);
                break;
            case 5:
                cursor = baseDeDatos.rawQuery("select * from persona where nombre like '%" + etRelTutelados.getText().toString() + "%' and dni in (select dni from empleados_abriendoCamino where fechaSalida = '$') and cp is not null and dni in (select dni_tutor from rel_tutelados where dni_tutelado='" + this.dni  + "')", null);
                break;
        }

        if (cursor.moveToFirst()) {
            do {
                lista.add(new Persona(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        baseDeDatos.close();
        admin.close();

        Adaptador_Tutores adaptador = new Adaptador_Tutores(view.getContext(), R.layout.tutores_item, lista);
        lvRelTutelados.setAdapter(adaptador);
        lvRelTutelados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posicionPulsada = position;
                muestraPopmenu(view);
                return true;
            }
        });


        lvRelTutelados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicionPulsada = position;
                onCreateDialog(view);
            }
        });
    }


    public void muestraPopmenu(View view)
    {
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(),view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater=popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup2,popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.idBorrar2:
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setMessage("¿Estás seguro que quieres borrar?").setTitle("Alerta de Borrado");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrar(getView());
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });builder.create().show();
                return true;

            default: return false;
        }
    }

    private void borrar(View view) {
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        int cantidad = baseDeDatos.delete("rel_tutelados", "dni_tutelado='" + dni + "' and dni_tutor= '"+lista.get(posicionPulsada).getDni()+"'", null);
        if(cantidad == 1){
            lista.remove(posicionPulsada);
            Toast.makeText(view.getContext(), "Tutor eliminado correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(view.getContext(), "Error al borrar el Tutor", Toast.LENGTH_SHORT).show();
        }
        baseDeDatos.close();
        listar(view, 0);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreateDialog(View view)
    {

        String datos = lista.get(posicionPulsada).getDni()+ "#" +lista.get(posicionPulsada).getNombre() + "#" + lista.get(posicionPulsada).getApellidos() + "#" + lista.get(posicionPulsada).getDomicilio() + "#" + lista.get(posicionPulsada).getCp() + "#" + lista.get(posicionPulsada).getPoblacion() + "#" + lista.get(posicionPulsada).getProvincia()
                + "#" + lista.get(posicionPulsada).getTelefono() + "#" + lista.get(posicionPulsada).getEmail() + "#" + lista.get(posicionPulsada).getObservaciones();

        new CuadroTutores(view.getContext(),datos);
    }

}