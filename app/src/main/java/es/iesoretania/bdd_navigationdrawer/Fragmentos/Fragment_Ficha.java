package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Funcionalidades.OcultarTeclado;
import es.iesoretania.bdd_navigationdrawer.Objetos.CaptureBitmapView;
import es.iesoretania.bdd_navigationdrawer.Objetos.Empleado;
import es.iesoretania.bdd_navigationdrawer.Objetos.Persona;
import es.iesoretania.bdd_navigationdrawer.R;


public class Fragment_Ficha extends Fragment {
    private CaptureBitmapView mSig = null;
    private EditText etNOMBRE, etAPELLIDOS, etDNI;

    private String entrada,fechas;
    private Empleado empleado = null;
    Bitmap bitmap;
    LinearLayout layout;
    ByteArrayOutputStream baos;
    private boolean nuevo = true;

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fichar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        Button btnAceptar = getView().findViewById(R.id.btnAceptar);

        Button btnborrar= getView().findViewById(R.id.btnBorrarFirma);

        etNOMBRE = getView().findViewById(R.id.etNombre);
        etAPELLIDOS = getView().findViewById(R.id.etApellidos);
        etDNI = getView().findViewById(R.id.etDni);
        etDNI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
                SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
                Cursor cursor = baseDeDatos.rawQuery("select * from persona where dni='" + etDNI.getText().toString() + "'", null);

                if (cursor.moveToFirst()) {
                    etNOMBRE.setText(cursor.getString(1));
                    etAPELLIDOS.setText(cursor.getString(2));
                    nuevo = false;

                }else{
                    nuevo=true;
                    etNOMBRE.setText("");
                    etAPELLIDOS.setText("");
                }

                cursor.close();
                baseDeDatos.close();
                admin.close();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        layout = (LinearLayout) view.findViewById(R.id.hlFirma);
        mSig = new CaptureBitmapView(view.getContext(),null);
        layout.addView(mSig, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etDNI.getText().toString().isEmpty() || etNOMBRE.getText().toString().isEmpty() || etAPELLIDOS.getText().toString().isEmpty()){
                    Toast.makeText(v.getContext(), "Debes introducir tus datos. Si has introducido tus datos anteriormente, introduce solo el DNI y la firma.", Toast.LENGTH_SHORT).show();
                }else {
                    registrar(v);
                    OcultarTeclado ocultar=new OcultarTeclado();
                    ocultar.hideKeyboard(getActivity(),getView());
                }
            }
        });

        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSig.ClearCanvas();
            }
        });

    }

    /**
     * @brief Función que comprueba si el usuario está registrado o no y llama a sus correspondientes funciones
     * @param view
     */
    public void registrar(View view) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
        Date now = new Date();
        fechas = formatter.format(now);
        if(!nuevo)
            inserta();
        else
            insertaNuevo();
        nuevo = true;
    }

    /**
     * @brief Función que realiza una captura del cuadro de firmas
     */
    private void takeScreenshot() {
        try {
            View v1 = layout.getChildAt(0);
            v1.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
        }catch (Throwable e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @brief Función que, una vez comprobado si el usuario está entrando o saliendo, registra la entrada/salida en la base de datos
     */
    public void inserta(){
        Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        if(compruebaE_S())
        {
            takeScreenshot();
            baos = new ByteArrayOutputStream(20480);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
            byte[] blob = baos.toByteArray();

            String sql = "insert into empleados_abriendoCamino (dni, fecha, img, fechaSalida) values(?,?,?,?)";
            SQLiteStatement insert = baseDeDatos.compileStatement(sql);
            insert.clearBindings();
            insert.bindString(1, etDNI.getText().toString());
            insert.bindString(2, fechas);
            insert.bindBlob(3, blob);
            insert.bindString(4, "$");
            insert.executeInsert();
        }
        else
        {
            ContentValues emp = new ContentValues();
            emp.put("dni", empleado.getDni());
            emp.put("fecha", empleado.getFecha());
            emp.put("img", empleado.getImagen());
            emp.put("fechaSalida", fechas);
            //baseDeDatos.execSQL("update empleados_abriendoCamino set dni='" + empleado.getDni() + "', fecha='" + empleado.getFecha() + "'" + ", imagen=" + empleado.getImagen() + ", fechaSalida='" + empleado.getFechaSalida() + "' where dni='" + empleado.getDni() + "'");
            baseDeDatos.update("empleados_abriendoCamino", emp, "dni='" + empleado.getDni() + "' and fecha='" + empleado.getFecha() + "'", null);
        }
        baseDeDatos.close();
        admin.close();
        NavHostFragment.findNavController(Fragment_Ficha.this)
                .navigate(R.id.action_nav_matricular_to_nav_inicio);
        Toast.makeText(getContext(), "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    /**
     * @brief Realiza la misma función que la función inserta(), pero incluye una inserción en la base de datos para registrar a la persona, ya que es la primera vez que entra
     */
    public void insertaNuevo(){
        if(compruebaE_S())
        {
            Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
            SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
            takeScreenshot();
            baos = new ByteArrayOutputStream(20480);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
            byte[] blob = baos.toByteArray();

            SQLiteStatement insert2 = baseDeDatos.compileStatement("insert into persona(dni,nombre,apellidos) values(?,?,?)");
            insert2.clearBindings();
            insert2.bindString(1, etDNI.getText().toString());
            insert2.bindString(2, etNOMBRE.getText().toString());
            insert2.bindString(3, etAPELLIDOS.getText().toString());
            insert2.executeInsert();


            String sql = "insert into empleados_abriendoCamino (dni, fecha, img, fechaSalida) values(?,?,?,?)";
            SQLiteStatement insert = baseDeDatos.compileStatement(sql);
            insert.clearBindings();
            insert.bindString(1, etDNI.getText().toString());
            insert.bindString(2, fechas);
            insert.bindBlob(3, blob);
            insert.bindString(4, "$");
            insert.executeInsert();

            baseDeDatos.close();
            admin.close();


            Toast.makeText(getContext(), "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(),"Hay un fallo de congruencia en la base de datos",Toast.LENGTH_SHORT).show();
        }
        NavHostFragment.findNavController(Fragment_Ficha.this)
                .navigate(R.id.action_nav_matricular_to_nav_inicio);
    }

    /**
     * @brief función que comprueba si el usuario está saliendo o entrando de la empresa
     * @return true si está entrando; false si está saliendo
     */
    public boolean compruebaE_S(){
        boolean ok=true;
        Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        Cursor comprobador = baseDeDatos.rawQuery("select * from empleados_abriendoCamino where dni='" + etDNI.getText().toString() + "' and fecha like '" + fechas.substring(0,10) + "%'", null);
        while(comprobador.moveToNext()){
            {
                empleado = new Empleado(comprobador.getString(1), comprobador.getString(2), comprobador.getBlob(3), comprobador.getString(4));
                if(empleado.getFechaSalida().equals("$"))
                    ok=false;
            }
        }
        comprobador.close();
        baseDeDatos.close();
        admin.close();
        return ok;
    }
}