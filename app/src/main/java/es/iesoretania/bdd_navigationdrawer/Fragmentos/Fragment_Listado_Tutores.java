package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Objetos.Adaptador_Persona;
import es.iesoretania.bdd_navigationdrawer.Objetos.Adaptador_Tutores;
import es.iesoretania.bdd_navigationdrawer.Objetos.CuadroDialogo;
import es.iesoretania.bdd_navigationdrawer.Objetos.CuadroTutores;
import es.iesoretania.bdd_navigationdrawer.Objetos.Persona;
import es.iesoretania.bdd_navigationdrawer.R;


public class Fragment_Listado_Tutores extends Fragment implements PopupMenu.OnMenuItemClickListener {
    EditText etTutor;
    Spinner spTutor;
    Button btnFichar;
    Switch swTutor;
    ArrayList<Persona> lista;
    int posicionPulsada;
    String dniTutelado = null;
    boolean add = false;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__listado__tutores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etTutor = view.findViewById(R.id.etTutores);
        spTutor = view.findViewById(R.id.spTutores);
        btnFichar= view.findViewById(R.id.btnRegistrarTutor);
        swTutor = view.findViewById(R.id.swTutores);

        String[] opciones = new String[] {"dni", "nombre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, opciones);
        spTutor.setAdapter(adapter);

        if(!getArguments().isEmpty()){
            Fragment_Listado_TutoresArgs args = Fragment_Listado_TutoresArgs.fromBundle(getArguments());
            dniTutelado = args.getDniTutelado();
            if(dniTutelado != null){
                add = true;
            }
        }
        
        swTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swTutor.isChecked()){
                    if(etTutor.getText().toString().isEmpty())
                        listar(view, 1);
                    else{
                        if(spTutor.getSelectedItem().toString().equals("dni"))
                            listar(view, 4);
                        else if(spTutor.getSelectedItem().toString().equals("nombre"))
                            listar(view, 5);
                    }
                }else{
                    if(etTutor.getText().toString().isEmpty()){
                        listar(view, 0);
                    }else{
                        if(spTutor.getSelectedItem().toString().equals("dni"))
                            listar(view, 2);
                        else if(spTutor.getSelectedItem().toString().equals("nombre"))
                            listar(view, 3);
                    }
                }
            }
        });

        etTutor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(spTutor.getSelectedItem().toString().equals("dni")){
                    if(swTutor.isChecked())
                        listar(view, 4);
                    else
                        listar(view, 2);
                }else if(spTutor.getSelectedItem().toString().equals("nombre")){
                    if(swTutor.isChecked())
                        listar(view, 5);
                    else
                        listar(view, 3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spTutor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spTutor.getItemAtPosition(position).toString().equals("dni")){
                    if(swTutor.isChecked())
                        listar(getView(), 4);
                    else
                        listar(getView(), 2);
                }else if(spTutor.getItemAtPosition(position).toString().equals("nombre")){
                    if(swTutor.isChecked())
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
                //false no modificar
                if(!add)
                    NavHostFragment.findNavController(Fragment_Listado_Tutores.this).navigate(Fragment_Listado_TutoresDirections.actionNavTutorToFragmentFicharTutor(new Persona("","","","",0,"","","","",""),false, false, null));
                else
                    NavHostFragment.findNavController(Fragment_Listado_Tutores.this).navigate(Fragment_Listado_TutoresDirections.actionNavTutorToFragmentFicharTutor(new Persona("","","","",0,"","","","",""),false, false, dniTutelado));
            }
        });
        listar(view, 0);
    }

    private void listar(View view, int opcion) {
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        ListView listView = view.findViewById(R.id.lvTutores);
        lista = new ArrayList<>();
        Cursor cursor = null;

        switch (opcion){
            case 0:
                cursor = baseDeDatos.rawQuery("select * from persona where cp is not null", null);
                break;
            case 1:
                cursor = baseDeDatos.rawQuery("select * from persona where dni in (select dni from empleados_abriendoCamino where fechaSalida = '$') and cp is not null", null);
                break;
            case 2:
                cursor = baseDeDatos.rawQuery("select * from persona where dni like '%" + etTutor.getText().toString() + "%' and cp is not null", null);
                break;
            case 3:
                cursor = baseDeDatos.rawQuery("select * from persona where nombre like '%" + etTutor.getText().toString() + "%' and cp is not null", null);
                break;
            case 4:
                cursor = baseDeDatos.rawQuery("select * from persona where dni like '%" + etTutor.getText().toString() + "%' and dni in (select dni from empleados_abriendoCamino where fechaSalida = '$') and cp is not null", null);
                break;
            case 5:
                cursor = baseDeDatos.rawQuery("select * from persona where nombre like '%" + etTutor.getText().toString() + "%' and dni in (select dni from empleados_abriendoCamino where fechaSalida = '$') and cp is not null", null);
                break;
        }

        if(cursor.moveToFirst()){
            do{
                lista.add(new Persona(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9)));
            }while(cursor.moveToNext());
        }

        cursor.close();
        baseDeDatos.close();
        admin.close();

        Adaptador_Tutores adaptador = new Adaptador_Tutores(view.getContext(), R.layout.tutores_item, lista);
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
                if(!add) {
                    onCreateDialog(view);
                }else{
                    Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
                    SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
                    String consulta = "insert into rel_tutelados (dni_tutelado, dni_tutor) values (?, ?)";
                    SQLiteStatement ins = baseDeDatos.compileStatement(consulta);
                    ins.clearBindings();
                    ins.bindString(1, dniTutelado);
                    ins.bindString(2, lista.get(posicionPulsada).getDni());
                    ins.executeInsert();
                    NavHostFragment.findNavController(Fragment_Listado_Tutores.this).navigate(Fragment_Listado_TutoresDirections.actionNavTutorToNavRelTutelados(dniTutelado));
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreateDialog(View view)
    {
        String datos = lista.get(posicionPulsada).getDni()+ "#" +lista.get(posicionPulsada).getNombre() + "#" + lista.get(posicionPulsada).getApellidos() + "#" + lista.get(posicionPulsada).getDomicilio() + "#" + lista.get(posicionPulsada).getCp() + "#" + lista.get(posicionPulsada).getPoblacion() + "#" + lista.get(posicionPulsada).getProvincia()
                + "#" + lista.get(posicionPulsada).getTelefono() + "#" + lista.get(posicionPulsada).getEmail() + "#" + lista.get(posicionPulsada).getObservaciones();

        new CuadroTutores(view.getContext(),datos);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {


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
                NavHostFragment.findNavController(Fragment_Listado_Tutores.this).navigate(Fragment_Listado_TutoresDirections.actionNavTutorToFragmentFicharTutor(lista.get(posicionPulsada),true, false, null));
                return true;


            default: return false;
        }
    }

    public void muestraPopmenu(View view)
    {
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(),view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater=popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup,popup.getMenu());
        popup.show();
    }

    private void borrar(View view) {

        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        String dni = lista.get(posicionPulsada).getDni();
        String nombre = lista.get(posicionPulsada).getNombre();
        String apellidos = lista.get(posicionPulsada).getApellidos();
        int cantidad = baseDeDatos.delete("persona", "dni='" + dni + "'", null);
        if(cantidad == 1){
            lista.remove(posicionPulsada);
            SQLiteStatement insert2 = baseDeDatos.compileStatement("insert into persona(dni,nombre,apellidos) values(?,?,?)");
            insert2.clearBindings();
            insert2.bindString(1, dni);
            insert2.bindString(2, nombre);
            insert2.bindString(3, apellidos);
            long cantidad2 = insert2.executeInsert();
            if(cantidad2 != -1) {
                Toast.makeText(view.getContext(), "Tutor eliminado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(view.getContext(), "Problema en la base de datos.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(view.getContext(), "Error al borrar el tutor", Toast.LENGTH_SHORT).show();
        }
        baseDeDatos.close();
        listar(view, 0);


    }
}