package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
import es.iesoretania.bdd_navigationdrawer.Objetos.Adaptador_Tutelados;
import es.iesoretania.bdd_navigationdrawer.Objetos.Adaptador_Tutores;
import es.iesoretania.bdd_navigationdrawer.Objetos.CuadroTutores;
import es.iesoretania.bdd_navigationdrawer.Objetos.Cuadro_Tutelados;
import es.iesoretania.bdd_navigationdrawer.Objetos.Persona;
import es.iesoretania.bdd_navigationdrawer.Objetos.Tutelado;
import es.iesoretania.bdd_navigationdrawer.R;

public class Fragment_Listado_Tutelados extends Fragment implements PopupMenu.OnMenuItemClickListener{

    EditText etTutelado;
    Spinner spTutelado;
    Button btnFichar;
    Switch swTutelado;
    ArrayList<Tutelado> lista;
    int posicionPulsada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__listado__tutelados, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTutelado = view.findViewById(R.id.etTutelados);
        spTutelado = view.findViewById(R.id.spTutelados);
        btnFichar = view.findViewById(R.id.btnRegistrarTutelado);
        swTutelado = view.findViewById(R.id.swTutelados);

        String[] opciones = new String[] {"dni", "nombre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, opciones);
        spTutelado.setAdapter(adapter);

        swTutelado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swTutelado.isChecked()){
                    if(etTutelado.getText().toString().isEmpty())
                        listar(view, 1);
                    else{
                        if(spTutelado.getSelectedItem().toString().equals("dni"))
                            listar(view, 4);
                        else if(spTutelado.getSelectedItem().toString().equals("nombre"))
                            listar(view, 5);
                    }
                }else{
                    if(etTutelado.getText().toString().isEmpty()){
                        listar(view, 0);
                    }else{
                        if(spTutelado.getSelectedItem().toString().equals("dni"))
                            listar(view, 2);
                        else if(spTutelado.getSelectedItem().toString().equals("nombre"))
                            listar(view, 3);
                    }
                }
            }
        });

        etTutelado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(spTutelado.getSelectedItem().toString().equals("dni")){
                    if(swTutelado.isChecked())
                        listar(view, 4);
                    else
                        listar(view, 2);
                }else if(spTutelado.getSelectedItem().toString().equals("nombre")){
                    if(swTutelado.isChecked())
                        listar(view, 5);
                    else
                        listar(view, 3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spTutelado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spTutelado.getItemAtPosition(position).toString().equals("dni")){
                    if(swTutelado.isChecked())
                        listar(getView(), 4);
                    else
                        listar(getView(), 2);
                }else if(spTutelado.getItemAtPosition(position).toString().equals("nombre")){
                    if(swTutelado.isChecked())
                        listar(getView(), 5);
                    else
                        listar(getView(), 3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnFichar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Fragment_Listado_Tutelados.this).navigate(Fragment_Listado_TuteladosDirections.actionFragmentListadoTuteladosToFragmentFicharTutelado(false, new Tutelado("","","",0,"",0,"","","","","")));
            }
        });
        listar(view, 0);
    }

    private void listar(View view, int opcion) {
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        ListView listView = view.findViewById(R.id.lvTutelados);
        lista = new ArrayList<>();
        Cursor cursor = null;

        switch (opcion){
            case 0:
                cursor = baseDeDatos.rawQuery("select * from tutelado", null);
                break;
            case 1:
                cursor = baseDeDatos.rawQuery("select * from tutelado where dni in (select dni from empleados_abriendoCamino where fechaSalida = '$')", null);
                break;
            case 2:
                cursor = baseDeDatos.rawQuery("select * from tutelado where dni like '%" + etTutelado.getText().toString() + "%'", null);
                break;
            case 3:
                cursor = baseDeDatos.rawQuery("select * from tutelado where nombre like '%" + etTutelado.getText().toString() + "%'", null);
                break;
            case 4:
                cursor = baseDeDatos.rawQuery("select * from tutelado where dni like '%" + etTutelado.getText().toString() + "%' and dni in (select dni from empleados_abriendoCamino where fechaSalida = '$')", null);
                break;
            case 5:
                cursor = baseDeDatos.rawQuery("select * from tutelado where nombre like '%" + etTutelado.getText().toString() + "%' and dni in (select dni from empleados_abriendoCamino where fechaSalida = '$')", null);
                break;
        }

        if(cursor.moveToFirst()){
            do{
                lista.add(new Tutelado(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10)));
            }while(cursor.moveToNext());
        }

        cursor.close();
        baseDeDatos.close();
        admin.close();

        Adaptador_Tutelados adaptador = new Adaptador_Tutelados(view.getContext(), R.layout.tutores_item, lista);
        listView.setAdapter(adaptador);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posicionPulsada = position;
                muestraPopmenu(view);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicionPulsada = position;
                onCreateDialog(view);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreateDialog(View view)
    {
        Dialog dialog=null;
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        String datos = lista.get(posicionPulsada).getDni()+ "#" +lista.get(posicionPulsada).getNombre() + "#" + lista.get(posicionPulsada).getApellidos() + "#" + lista.get(posicionPulsada).getEdad() + "#" + lista.get(posicionPulsada).getDomicilio() + "#" + lista.get(posicionPulsada).getCp() + "#" + lista.get(posicionPulsada).getPoblacion() + "#" + lista.get(posicionPulsada).getProvincia()
                + "#" + lista.get(posicionPulsada).getTelefono() + "#" + lista.get(posicionPulsada).getEmail() + "#" + lista.get(posicionPulsada).getObservaciones();

        new Cuadro_Tutelados(view.getContext(),datos);
    }

    public void muestraPopmenu(View view)
    {
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(),view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater=popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_tutelados,popup.getMenu());
        popup.show();
    }

    private void borrar(View view) {
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        int cantidad = baseDeDatos.delete("tutelado", "dni='" + lista.get(posicionPulsada).getDni() + "'", null);
        if(cantidad == 1){
            lista.remove(posicionPulsada);
            Toast.makeText(view.getContext(), "Tutelado eliminado correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(view.getContext(), "Error al borrar el tutelado", Toast.LENGTH_SHORT).show();
        }
        baseDeDatos.close();
        listar(view, 0);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        NavController navController= Navigation.findNavController(getView());
        switch(item.getItemId())
        {
            case R.id.idBorrar:
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
            case R.id.idEditar:
                NavHostFragment.findNavController(Fragment_Listado_Tutelados.this).navigate(Fragment_Listado_TuteladosDirections.actionFragmentListadoTuteladosToFragmentFicharTutelado(true, lista.get(posicionPulsada)));
                return true;
            case R.id.AñadirTutor:
                    NavHostFragment.findNavController(Fragment_Listado_Tutelados.this).navigate(Fragment_Listado_TuteladosDirections.actionNavTuteladoToNavRelTutelados(lista.get(posicionPulsada).getDni()));
                return true;
            default: return false;
        }
    }
}