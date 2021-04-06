package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Objetos.Adaptador_Persona;
import es.iesoretania.bdd_navigationdrawer.Objetos.CuadroDialogo;
import es.iesoretania.bdd_navigationdrawer.Objetos.Empleado;
import es.iesoretania.bdd_navigationdrawer.Objetos.Persona;
import es.iesoretania.bdd_navigationdrawer.R;



public class Fragment_Listar extends DialogFragment implements PopupMenu.OnMenuItemClickListener {
    int posicionPulsada;
    ArrayList<Empleado> lista;
    Spinner spOpciones;
    EditText etFiltro;
    Switch swActual;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_listar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spOpciones = view.findViewById(R.id.spOpcionesLista);
        etFiltro = view.findViewById(R.id.etFiltro);
        swActual = view.findViewById(R.id.swActual);

        String[] opciones = new String[] {"dni", "nombre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, opciones);
        spOpciones.setAdapter(adapter);
        swActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swActual.isChecked()){
                    if(etFiltro.getText().toString().isEmpty())
                        listar(view, 1);
                    else{
                        if(spOpciones.getSelectedItem().toString().equals("dni"))
                            listar(view, 4);
                        else if(spOpciones.getSelectedItem().toString().equals("nombre"))
                            listar(view, 5);
                    }
                }else{
                    if(etFiltro.getText().toString().isEmpty()){
                        listar(view, 0);
                    }else{
                        if(spOpciones.getSelectedItem().toString().equals("dni"))
                            listar(view, 2);
                        else if(spOpciones.getSelectedItem().toString().equals("nombre"))
                            listar(view, 3);
                    }
                }
            }
        });

        etFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(spOpciones.getSelectedItem().toString().equals("dni")){
                    if(swActual.isChecked())
                        listar(view, 4);
                    else
                        listar(view, 2);
                }else if(spOpciones.getSelectedItem().toString().equals("nombre")){
                    if(swActual.isChecked())
                        listar(view, 5);
                    else
                        listar(view, 3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spOpciones.getItemAtPosition(position).toString().equals("dni")){
                    if(swActual.isChecked())
                        listar(getView(), 4);
                    else
                        listar(getView(), 2);
                }else if(spOpciones.getItemAtPosition(position).toString().equals("nombre")){
                    if(swActual.isChecked())
                        listar(getView(), 5);
                    else
                        listar(getView(), 3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listar(view, 0);
    }

    /**
     * @brief Función que realiza el listado llamando a otras funciones
     * @param view
     */
    public void listar(View view, int opcion){
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        ListView listView = view.findViewById(R.id.lvListarTutores);
        lista = new ArrayList<>();
        Cursor cursor = null;

        switch (opcion){
            case 0:
                cursor = baseDeDatos.rawQuery("select * from empleados_abriendoCamino", null);
                break;
            case 1:
                cursor = baseDeDatos.rawQuery("select * from empleados_abriendoCamino where fechaSalida = '$'", null);
                break;
            case 2:
                cursor = baseDeDatos.rawQuery("select * from empleados_abriendoCamino where dni like '%" + etFiltro.getText().toString() + "%'", null);
                break;
            case 3:
                cursor = baseDeDatos.rawQuery("select * from empleados_abriendoCamino where dni in (select dni from persona where nombre like '%" + etFiltro.getText().toString() + "%')", null);
                break;
            case 4:
                cursor = baseDeDatos.rawQuery("select * from empleados_abriendoCamino where dni like '%" + etFiltro.getText().toString() + "%' and fechaSalida = '$'", null);
                break;
            case 5:
                cursor = baseDeDatos.rawQuery("select * from empleados_abriendoCamino where dni in (select dni from persona where nombre like '%" + etFiltro.getText().toString() + "%') and fechaSalida = '$'", null);
                break;
        }

        if(cursor.moveToFirst()){
            do{
                lista.add(new Empleado(cursor.getString(1), cursor.getString(2), cursor.getBlob(3),cursor.getString(4)));
            }while(cursor.moveToNext());
        }

        cursor.close();
        baseDeDatos.close();
        admin.close();

        Adaptador_Persona adaptador = new Adaptador_Persona(view.getContext(), R.layout.persona_item, lista);
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

    /**
     * @brief Función que muestra un menú PopUp
     * @param view
     */
    public void muestraPopmenu(View view)
    {
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(),view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater=popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup,popup.getMenu());
        popup.show();
    }

    /**
     * @brief Función que controla cuando se pulsa un elemento de la lista
     * @param item Elemento pulsado
     * @return true si se ha pulsado uno de los dos elementos (borrar o editar); false en caso contrario
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        NavController navController = Navigation.findNavController(getView());
        switch(item.getItemId()){
            case R.id.idBorrar:
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setMessage("Estas seguro que quieres borrar").setTitle("Alerta de Borrado");
                builder.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrar(getView());
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return  true;
            case R.id.idEditar:
                navController.navigate(Fragment_ListarDirections.actionNavListarToFragmentMod(lista.get(posicionPulsada).getDni(), lista.get(posicionPulsada).getFecha()));
                return  true;

            default: return false;
        }
    }

    /**
     * @brief Función que guarda en una lista todos los empleados de la bdd y los manda a un nuevo cuadro de diálogo que creará la lista
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreateDialog(View view)
    {
        Dialog dialog=null;
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        byte[] blob = lista.get(posicionPulsada).getImagen();
        ByteArrayInputStream bais = new ByteArrayInputStream(blob);
        Bitmap b = BitmapFactory.decodeStream(bais);
        Bitmap x = Bitmap.createScaledBitmap(b, 800, 500, true);
        Drawable d = new BitmapDrawable(getResources(), x);
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        ArrayList<Persona> personas = new ArrayList<>();
        Cursor persona = baseDeDatos.rawQuery("select * from persona where dni='" + lista.get(posicionPulsada).getDni() + "'", null);
        if(persona.moveToFirst()){
            do{
                personas.add(new Persona(persona.getString(0), persona.getString(1), persona.getString(2)));
            }while(persona.moveToNext());
        }
        baseDeDatos.close();
        admin.close();
        String aux = "";
        if(lista.get(posicionPulsada).getFechaSalida().equals("$")){
            aux = "Vacío";
        }else{
            aux = lista.get(posicionPulsada).getFechaSalida();
        }
        String datos = lista.get(posicionPulsada).getDni()+ "#" +lista.get(posicionPulsada).getFecha() + "#" + aux + "#" + personas.get(0).getNombre() + "#" + personas.get(0).getApellidos();
        new CuadroDialogo(view.getContext(),d,datos);
    }

    /**
     * @brief Función que elimina una ficha de la base de datos
     * @param view
     */
    public void borrar(View view){
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        int cantidad = baseDeDatos.delete("empleados_abriendoCamino","fecha='" + lista.get(posicionPulsada).getFecha() + "'", null);
        if(cantidad == 1){
            Toast.makeText(view.getContext(), "Ficha eliminada correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(view.getContext(), "Error al borrar la ficha", Toast.LENGTH_SHORT).show();
        }
        baseDeDatos.close();
        listar(view, 0);
    }

}