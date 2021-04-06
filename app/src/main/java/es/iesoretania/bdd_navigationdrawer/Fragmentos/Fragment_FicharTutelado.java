package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Funcionalidades.OcultarTeclado;
import es.iesoretania.bdd_navigationdrawer.Objetos.Tutelado;
import es.iesoretania.bdd_navigationdrawer.R;

public class Fragment_FicharTutelado extends Fragment {



    EditText etDni, etNombre, etApellidos, etEdad, etDomicilio, etCp, etPoblacion, etProvincia, etTelefono, etEmail, etObservaciones;
    TextView tvTitulo;
    Button btnAceptar, btnCancelar,B;
    Boolean mod=false;
    Tutelado tutelado = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__fichar_tutelado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        etDni = view.findViewById(R.id.etDniTutelado);
        etNombre = view.findViewById(R.id.etNombreTutelado);
        etApellidos = view.findViewById(R.id.etApellidosTutelado);
        etEdad = view.findViewById(R.id.etEdadTutelado);
        etDomicilio = view.findViewById(R.id.etDomicilioTutelado);
        etCp = view.findViewById(R.id.etCpTutelado);
        etPoblacion = view.findViewById(R.id.etPoblacionTutelado);
        etProvincia = view.findViewById(R.id.etProvinciaTutelado);
        etTelefono = view.findViewById(R.id.etTelefonoTutelado);
        etEmail = view.findViewById(R.id.etEmailTutelado);
        etObservaciones = view.findViewById(R.id.etObservacionesTutelado);

        tvTitulo = view.findViewById(R.id.tvFicharTutelado);

        btnAceptar = view.findViewById(R.id.btnAceptarTutelado);
        btnCancelar = view.findViewById(R.id.btnCancelarTutelado);


        if(getArguments() != null){
            Fragment_FicharTuteladoArgs args = Fragment_FicharTuteladoArgs.fromBundle(getArguments());
            if(args.getMod() == true){
                etDni.setEnabled(false);
                tutelado = args.getTutelado();
                mod = true;
                tvTitulo.setText("Modificar Tutelado");
                etDni.setText(tutelado.getDni());
                etNombre.setText(tutelado.getNombre());
                etApellidos.setText(tutelado.getApellidos());
                etEdad.setText(String.valueOf( tutelado.getEdad()));
                etDomicilio.setText(tutelado.getDomicilio());
                etCp.setText(String.valueOf( tutelado.getCp()));
                etPoblacion.setText(tutelado.getPoblacion());
                etProvincia.setText(tutelado.getProvincia());
                etTelefono.setText(tutelado.getTelefono());
                etEmail.setText(tutelado.getEmail());
                etObservaciones.setText(tutelado.getObservaciones());
            }
            else{
                mod = false;
            }
        }




        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OcultarTeclado ocultarTeclado=new OcultarTeclado();
                ocultarTeclado.hideKeyboard(getActivity(),getView());
                navController.navigate(Fragment_FicharTuteladoDirections.actionNavFichaTuteladoToNavInicio());
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etDni.getText().toString().isEmpty() || etNombre.getText().toString().isEmpty() || etApellidos.getText().toString().isEmpty() || etEdad.getText().toString().isEmpty() || etDomicilio.getText().toString().isEmpty() || etCp.getText().toString().isEmpty() || etPoblacion.getText().toString().isEmpty() || etProvincia.getText().toString().isEmpty() || etTelefono.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty() || etObservaciones.getText().toString().isEmpty()){
                    Toast.makeText(v.getContext(), "Debes introducir todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    if(!mod){
                        insertar();
                    }else{
                        modificar(view);
                    }
                }
            }
        });
    }
    public void insertar(){
        Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        String sql = "insert into tutelado(dni, nombre, apellidos, edad, domicilio, cp, poblacion, provincia, telefono, email, observaciones) values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement insert = baseDeDatos.compileStatement(sql);
        insert.clearBindings();
        insert.bindString(1, etDni.getText().toString());
        insert.bindString(2, etNombre.getText().toString());
        insert.bindString(3, etApellidos.getText().toString());
        insert.bindString(4, etEdad.getText().toString());
        insert.bindString(5, etDomicilio.getText().toString());
        insert.bindString(6, etCp.getText().toString());
        insert.bindString(7, etPoblacion.getText().toString());
        insert.bindString(8, etProvincia.getText().toString());
        insert.bindString(9, etTelefono.getText().toString());
        insert.bindString(10, etEmail.getText().toString());
        insert.bindString(11, etObservaciones.getText().toString());
        insert.executeInsert();

        baseDeDatos.close();
        admin.close();
        OcultarTeclado ocultarTeclado=new OcultarTeclado();
        ocultarTeclado.hideKeyboard(getActivity(),getView());
        NavHostFragment.findNavController(Fragment_FicharTutelado.this).navigate(Fragment_FicharTuteladoDirections.actionNavFichaTuteladoToNavTutelado());
        Toast.makeText(getContext(), "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    private void modificar(View view) {
        Administracion_BDD admin;
        admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        ContentValues tutelado =  new ContentValues();
        tutelado.put("dni", this.tutelado.getDni());
        tutelado.put("nombre", etNombre.getText().toString());
        tutelado.put("apellidos", etApellidos.getText().toString() );
        tutelado.put("edad", Integer.valueOf(etEdad.getText().toString()));
        tutelado.put("domicilio", etDomicilio.getText().toString());
        tutelado.put("cp", Integer.valueOf(etCp.getText().toString()));
        tutelado.put("poblacion", etPoblacion.getText().toString());
        tutelado.put("provincia", etProvincia.getText().toString());
        tutelado.put("telefono", etTelefono.getText().toString());
        tutelado.put("email", etEmail.getText().toString());
        tutelado.put("observaciones", etObservaciones.getText().toString());

        int cantidad2 = baseDeDatos.update("tutelado", tutelado, "dni='" + this.tutelado.getDni() + "'", null);
        if( cantidad2 == 1){
            Toast.makeText(view.getContext(), "Registro modificado correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(view.getContext(), "Error al modificar el tutelado", Toast.LENGTH_SHORT).show();
        }
        baseDeDatos.close();
        admin.close();
        OcultarTeclado ocultarTeclado=new OcultarTeclado();
        ocultarTeclado.hideKeyboard(getActivity(),getView());
        NavHostFragment.findNavController(Fragment_FicharTutelado.this)
                .navigate(Fragment_FicharTuteladoDirections.actionNavFichaTuteladoToNavTutelado());
    }
}