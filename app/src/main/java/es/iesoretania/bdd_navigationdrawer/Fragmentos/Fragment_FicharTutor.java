package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Funcionalidades.OcultarTeclado;
import es.iesoretania.bdd_navigationdrawer.Objetos.Persona;
import es.iesoretania.bdd_navigationdrawer.R;


public class Fragment_FicharTutor extends Fragment {
    EditText nif,nombre,apellidos,cp,provincia,poblacion,domicilio,telefono,email,observaciones;
    Button btnCancelar,btnAceptar;
    TextView titulo;
    Boolean mod=false;
    Boolean rel = false;
    Persona persona;
    String dniTutelado = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__fichar_tutor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //asociamos los elementos con el layout
        titulo=(TextView)view.findViewById(R.id.tvTutor); 
        nif=(EditText)view.findViewById(R.id.etDniTutor);
        nombre=(EditText)view.findViewById(R.id.etNombreTutor);
        apellidos=(EditText)view.findViewById(R.id.etApellidoTutor);
        cp=(EditText)view.findViewById(R.id.etCpTutor);
        provincia=(EditText)view.findViewById(R.id.etProvinciaTutor);
        poblacion=(EditText)view.findViewById(R.id.etPoblacionTutor);
        domicilio=(EditText)view.findViewById(R.id.etDomicilioTutor);
        telefono=(EditText)view.findViewById(R.id.etTelefonoTutor);
        email=(EditText)view.findViewById(R.id.etEmailTutor);
        observaciones=(EditText)view.findViewById(R.id.etObservacionesTutor);

        btnAceptar=(Button) view.findViewById(R.id.btnAceptarTutor);
        btnCancelar=(Button) view.findViewById(R.id.btnCancelarTutor);
        
        if(getArguments()!=null)
        {
            Fragment_FicharTutorArgs arg= Fragment_FicharTutorArgs.fromBundle(getArguments());
            rel = arg.getRef();
            if(arg.getMod()==true)
            {
                titulo.setText("Modificar Tutor");
                nif.setEnabled(false);
                persona=arg.getPersonaTutor();
                mod=true;
                nif.setText(persona.getDni());
                nombre.setText(persona.getNombre());
                apellidos.setText(persona.getApellidos());
                cp.setText(String.valueOf(persona.getCp()));
                provincia.setText(persona.getProvincia());
                poblacion.setText(persona.getPoblacion());
                domicilio.setText(persona.getDomicilio());
                telefono.setText(persona.getTelefono());
                email.setText(persona.getEmail());
                observaciones.setText(persona.getObservaciones());
            }
            else
            {
                dniTutelado = arg.getDniTutelado();
                persona = arg.getPersonaTutor();
                mod = false;

            }
        }

        nif.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
                SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
                Cursor cursor = baseDeDatos.rawQuery("select * from persona where dni='" + nif.getText().toString() + "'", null);

                if (cursor.moveToFirst()) {
                    nombre.setText(cursor.getString(1));
                    apellidos.setText(cursor.getString(2));
                    if(rel && cursor.getString(3) != null){
                        persona.setDni(nif.getText().toString());
                        cp.setText(cursor.getString(3));
                        provincia.setText(cursor.getString(4));
                        poblacion.setText(cursor.getString(5));
                        domicilio.setText(cursor.getString(6));
                        telefono.setText(cursor.getString(7));
                        email.setText(cursor.getString(8));
                        observaciones.setText(cursor.getString(9));
                        mod = true;
                    }

                }else{
                    nombre.setText("");
                    apellidos.setText("");
                    domicilio.setText("");
                    cp.setText("");
                    poblacion.setText("");
                    provincia.setText("");
                    telefono.setText("");
                    email.setText("");
                    observaciones.setText("");
                    if(rel){
                        mod = false;
                    }
                }

                cursor.close();
                baseDeDatos.close();
                admin.close();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //implementamos onClickListener

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcultarTeclado ocultarTeclado=new OcultarTeclado();
                ocultarTeclado.hideKeyboard(getActivity(),getView());
                Navigation.findNavController(view).navigate(R.id.action_fragment_FicharTutor_to_nav_inicio);
            }
        });




        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nif.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() ||  cp.getText().toString().isEmpty() ||
                   provincia.getText().toString().isEmpty() ||  poblacion.getText().toString().isEmpty() || domicilio.getText().toString().isEmpty() ||
                        telefono.getText().toString().isEmpty() ||  email.getText().toString().isEmpty() ||observaciones.getText().toString().isEmpty())
                {
                    Toast.makeText(v.getContext(), "Debes introducir tus datos.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(mod==false)
                    {
                        inserta();
                    }
                    else
                    {
                        modificar(view);
                    }
                 

                }
            }
        });


    }

    private void modificar(View view) {
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        ContentValues tutor =  new ContentValues();
        tutor.put("dni", persona.getDni());
        tutor.put("nombre",nombre.getText().toString());
        tutor.put("apellidos",apellidos.getText().toString() );
        tutor.put("domicilio", domicilio.getText().toString());
        tutor.put("cp",Integer.valueOf(cp.getText().toString()));
        tutor.put("poblacion",poblacion.getText().toString());
        tutor.put("provincia",provincia.getText().toString());
        tutor.put("telefono",telefono.getText().toString());
        tutor.put("email",email.getText().toString());
        tutor.put("observaciones",observaciones.getText().toString());

        int cantidad2 = baseDeDatos.update("persona", tutor, "dni='" + persona.getDni() + "'", null);
        if( cantidad2 == 1){
            Toast.makeText(view.getContext(), "Registro modificado correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(view.getContext(), "Error al modificar el tutor", Toast.LENGTH_SHORT).show();
        }
        baseDeDatos.close();
        admin.close();
        OcultarTeclado ocultarTeclado=new OcultarTeclado();
        ocultarTeclado.hideKeyboard(getActivity(),getView());
        NavHostFragment.findNavController(Fragment_FicharTutor.this)
                .navigate(Fragment_FicharTutorDirections.actionNavFichaTutorToNavTutor(null));
        
        
    }

    public void inserta(){
        Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        if(compruebaE_S())
        {
            String sql = "insert into persona (dni, nombre,apellidos,domicilio,cp,poblacion,provincia,telefono,email,observaciones) values(?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement insert = baseDeDatos.compileStatement(sql);
            insert.clearBindings();
            insert.bindString(1, nif.getText().toString());
            insert.bindString(2, nombre.getText().toString());
            insert.bindString(3, apellidos.getText().toString());
            insert.bindString(4, domicilio.getText().toString());
            insert.bindString(5, cp.getText().toString());
            insert.bindString(6, poblacion.getText().toString());
            insert.bindString(7, provincia.getText().toString());
            insert.bindString(8, telefono.getText().toString());
            insert.bindString(9, email.getText().toString());
            insert.bindString(10, observaciones.getText().toString());
            insert.executeInsert();
            if(dniTutelado != null){
                String consulta = "insert into rel_tutelados (dni_tutelado, dni_tutor) values (?, ?)";
                SQLiteStatement ins = baseDeDatos.compileStatement(consulta);
                ins.clearBindings();
                ins.bindString(1, dniTutelado);
                ins.bindString(2, nif.getText().toString());
                ins.executeInsert();
            }
        }
        else
        {
            ContentValues usr = new ContentValues();
            usr.put("dni",nif.getText().toString() );
            usr.put("nombre", nombre.getText().toString());
            usr.put("apellidos", apellidos.getText().toString());
            usr.put("domicilio", domicilio.getText().toString());
            usr.put("cp", cp.getText().toString());
            usr.put("poblacion", poblacion.getText().toString());
            usr.put("provincia", provincia.getText().toString());
            usr.put("telefono", telefono.getText().toString());
            usr.put("email", email.getText().toString());
            usr.put("observaciones", observaciones.getText().toString());
            //baseDeDatos.execSQL("update tutors_abriendoCamino set dni='" + tutor.getDni() + "', fecha='" + tutor.getFecha() + "'" + ", imagen=" + tutor.getImagen() + ", fechaSalida='" + tutor.getFechaSalida() + "' where dni='" + tutor.getDni() + "'");
            baseDeDatos.update("persona", usr, "dni='" + nif.getText().toString() + "'", null);

            if(dniTutelado != null){
                String consulta = "insert into rel_tutelados (dni_tutelado, dni_tutor) values (?, ?)";
                SQLiteStatement ins = baseDeDatos.compileStatement(consulta);
                ins.clearBindings();
                ins.bindString(1, dniTutelado);
                ins.bindString(2, nif.getText().toString());
                ins.executeInsert();
            }

        }
        baseDeDatos.close();
        admin.close();
        OcultarTeclado ocultarTeclado=new OcultarTeclado();
        ocultarTeclado.hideKeyboard(getActivity(),getView());
        if(dniTutelado!=null)
        {
            NavHostFragment.findNavController(Fragment_FicharTutor.this).navigate(Fragment_Listado_TutoresDirections.actionNavTutorToNavRelTutelados(dniTutelado));
            Toast.makeText(getContext(), "Registro guardado correctamente", Toast.LENGTH_SHORT).show();

        }
        else {
            NavHostFragment.findNavController(Fragment_FicharTutor.this)
                    .navigate(Fragment_FicharTutorDirections.actionNavFichaTutorToNavTutor(null));
            Toast.makeText(getContext(), "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * @brief función que comprueba si el usuario es nuevo o no
     * @return true si es nuevo; false si no lo es;
     */
    public boolean compruebaE_S(){
        boolean ok=true;
        Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        Cursor comprobador = baseDeDatos.rawQuery("select * from persona where dni='" + nif.getText().toString()+"'" , null);
        while(comprobador.moveToNext()){
            {

                ok=false;
            }
        }
        comprobador.close();
        baseDeDatos.close();
        admin.close();
        return ok;
    }

}