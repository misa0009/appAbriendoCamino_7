package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Funcionalidades.OcultarTeclado;
import es.iesoretania.bdd_navigationdrawer.Objetos.Empleado;
import es.iesoretania.bdd_navigationdrawer.Objetos.Persona;
import es.iesoretania.bdd_navigationdrawer.R;


public class Fragment_Modificar extends Fragment {

    private EditText etNOMBRE, etAPELLIDOS;
    private TextView etFecha, etFecha2;
    private ImageView imagen;
    private String dni;
    private String fecha;
    ArrayList<Empleado> lista;
    ArrayList<Persona> al;
    Button btnAceptar;
    Button btnVolver;
    Button btnInicio;
    private int horas, minutos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view=inflater.inflate(R.layout.fragment_modificar,container,false);


        btnAceptar = view.findViewById(R.id.btnAceptar);
        btnVolver = view.findViewById(R.id.btnVolverFicha);
        btnInicio = view.findViewById(R.id.btnInicio);

        etNOMBRE = view.findViewById(R.id.etNombre);
        etAPELLIDOS =view.findViewById(R.id.etApellidos);
        etFecha= view.findViewById(R.id.etFecha);
        etFecha2 = view.findViewById(R.id.etFecha2);
        imagen = view.findViewById(R.id.imageView3);

        if(getArguments()!=null){
            Fragment_ModificarArgs args = Fragment_ModificarArgs.fromBundle(getArguments());
            dni = args.getDNI();
            fecha = args.getFecha();
        }

        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        lista = new ArrayList<>();
        Cursor cursor = baseDeDatos.rawQuery("select * from empleados_abriendoCamino where dni='" + dni+"'" + " and fecha='" + fecha + "'", null);
        if(cursor.moveToFirst()){
            do{
                lista.add(new Empleado(cursor.getString(1), cursor.getString(2), cursor.getBlob(3),cursor.getString(4)));
            }while(cursor.moveToNext());
        }
        etFecha.setText(lista.get(0).getFecha());
        etFecha2.setText(lista.get(0).getFechaSalida());

        cursor.close();
        al = new ArrayList<>();
        Cursor cursor2 = baseDeDatos.rawQuery("select * from persona where dni='" + lista.get(0).getDni() + "'", null);
        cursor2.moveToFirst();
        al.add(new Persona(cursor2.getString(0), cursor2.getString(1), cursor2.getString(2)));
        etNOMBRE.setText(al.get(0).getNombre());
        etAPELLIDOS.setText(al.get(0).getApellidos());

        byte[] blob = lista.get(0).getImagen();
        ByteArrayInputStream bais = new ByteArrayInputStream(blob);
        Bitmap b = BitmapFactory.decodeStream(bais);
        imagen.setImageBitmap(b);

        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                Fragment_DatePicker newDateFragment = Fragment_DatePicker.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etFecha.setText(twoDigits(year) + "/" + twoDigits(month + 1) + "/" + twoDigits(dayOfMonth));
                    }
                });


                horas = c.get(Calendar.HOUR_OF_DAY);
                minutos = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etFecha.setText(etFecha.getText() + " - " + twoDigits(hourOfDay) + ":" + twoDigits(minute) + ":00");
                    }
                }, horas, minutos, true);
                timePickerDialog.show();
                newDateFragment.show(getActivity().getSupportFragmentManager(),"datePicker");
            }

        });

        etFecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                Fragment_DatePicker newDateFragment = Fragment_DatePicker.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etFecha2.setText(twoDigits(year) + "/" + twoDigits(month + 1) + "/" + twoDigits(dayOfMonth));
                    }
                });


                horas = c.get(Calendar.HOUR_OF_DAY);
                minutos = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etFecha2.setText(etFecha2.getText() + " - " + twoDigits(hourOfDay) + ":" + twoDigits(minute) + ":00");
                    }
                }, horas, minutos, true);
                timePickerDialog.show();
                newDateFragment.show(getActivity().getSupportFragmentManager(),"datePicker");
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar(v);
                borraTeclado();
                NavHostFragment.findNavController(Fragment_Modificar.this)
                        .navigate(R.id.action_fragment__Mod_to_nav_listar);

            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borraTeclado();
                NavHostFragment.findNavController(Fragment_Modificar.this)
                        .navigate(R.id.action_fragment__Mod_to_nav_listar);


            }
        });
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borraTeclado();
                NavHostFragment.findNavController(Fragment_Modificar.this)
                        .navigate(R.id.action_fragment__Mod_to_nav_inicio);
            }
        });

        return view;
    }

    /**
     * @brief Función que quita el teclado de la pantalla
     */
    private void borraTeclado() {
        OcultarTeclado ot=new OcultarTeclado();
        ot.hideKeyboard(getActivity(),getView());
    }

    /**
     * @brief Función que modifica un elemento de la base de datos
     * @param view
     */
    public void modificar(View view){
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        if(etNOMBRE.getText().toString().isEmpty() || etAPELLIDOS.getText().toString().isEmpty()){
            Toast.makeText(view.getContext(),"Debes introducir todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues empleado =  new ContentValues();
            empleado.put("dni", lista.get(0).getDni());
            empleado.put("fecha", etFecha.getText().toString());
            empleado.put("img", lista.get(0).getImagen());
            empleado.put("fechaSalida", lista.get(0).getFechaSalida());

            ContentValues persona = new ContentValues();
            persona.put("dni", al.get(0).getDni());
            persona.put("nombre", etNOMBRE.getText().toString());
            persona.put("apellidos", etAPELLIDOS.getText().toString());

            int cantidad = baseDeDatos.update("empleados_abriendoCamino", empleado, "fecha='"+ fecha + "'",null);
            int cantidad2 = baseDeDatos.update("persona", persona, "dni='" + dni + "'", null);
            if(cantidad == 1 && cantidad2 == 1){
                Toast.makeText(view.getContext(), "Registro modificado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(view.getContext(), "Error al modificar el empleado", Toast.LENGTH_SHORT).show();
            }
        }
        baseDeDatos.close();
        admin.close();
    }

    /**
     * @brief Función que convierte un número entero a un String con dos dígitos
     * @param n : número entero
     * @return String con dos dígitos
     */
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

}