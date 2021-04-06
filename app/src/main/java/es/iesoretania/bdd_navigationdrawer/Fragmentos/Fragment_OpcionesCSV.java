package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Objetos.Persona;
import es.iesoretania.bdd_navigationdrawer.R;


public class Fragment_OpcionesCSV extends Fragment {
    Button completo,personas,correo,registros;
    ArrayList<Persona> lista;
    File file;
    String nombre_directorio = "AbriendoCaminoCSV";
    String nombre_documento;
    List<String[]> listaEmpleado=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.fragment_opciones_csv,container,false);


        completo=(Button) view.findViewById(R.id.btnComleto);
        personas=(Button) view.findViewById(R.id.btnPersonas);
        correo=(Button) view.findViewById(R.id.btnCorreo);
        registros=(Button) view.findViewById(R.id.btnRegistros);

        completo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override public void onClick(View v) {
            if(ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(view.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
            }
            CSV_Personas(v);
            CSV_Correo(v);
            CSV_Registros(v);

        }});
        personas.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            if(ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(view.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
            }
            CSV_Personas(v);


        }});
        correo.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            if(ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(view.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
            }

            CSV_Correo(v);
        }});
        registros.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override public void onClick(View v) {
            if(ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(view.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
            }
            CSV_Registros(v);

        }});


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void CSV_Registros(View v) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        nombre_documento="RegistrosExportacionCSV_"+formatter.format(new Date())+".csv";
        file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+nombre_directorio);
        nombre_documento=file.toString()+ "/"+nombre_documento;
        boolean isCreate=false;
        if(!file.exists())
        {
            isCreate=file.mkdir();
        }
        try {

            FileWriter fileWriter = new FileWriter(nombre_documento);
            CSVWriter csvWriter= new CSVWriter(fileWriter);
            Administracion_BDD admin = new Administracion_BDD(v.getContext(), "abriendoCamino", null, 1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor cursor = baseDeDatos.rawQuery("select * from empleados_abriendoCamino", null);
            if(cursor.moveToFirst()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    do {

                        listaEmpleado.add(new String[]{cursor.getString(1).toString(), cursor.getString(2).toString(), Base64.getEncoder().encodeToString(cursor.getBlob(3)), cursor.getString(4).toString()});

                    } while (cursor.moveToNext());
                }
                csvWriter.writeAll(listaEmpleado);
                csvWriter.close();
                Toast.makeText(getContext(), "Creado correctamente", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(getContext(), "No hay Registros guardados en la base de datos", Toast.LENGTH_SHORT).show();
            }
            fileWriter.close();
            cursor.close();
            baseDeDatos.close();
            admin.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al crear el fichero", Toast.LENGTH_SHORT).show();
        }



    }

    private void CSV_Correo(View v) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        nombre_documento="CorreoExportacionCSV_"+formatter.format(new Date())+".csv";
        file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+nombre_directorio);
        nombre_documento=file.toString()+ "/"+nombre_documento;
        boolean isCreate=false;
        if(!file.exists())
        {
            isCreate=file.mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(nombre_documento);


            Administracion_BDD admin = new Administracion_BDD(v.getContext(), "abriendoCamino", null, 1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor cursor = baseDeDatos.rawQuery("select * from datos_correos", null);
            if(cursor.moveToFirst()){
                do{
                    //lista.add(new Persona(cursor.getString(0), cursor.getString(1),cursor.getString(2)));
                    fileWriter.append(cursor.getString(0).toString());
                    String s=cursor.getString(0);
                    fileWriter.append(",");
                    fileWriter.append("\n");
                }while(cursor.moveToNext());
                Toast.makeText(getContext(), "Creado correctamente", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "No hay un Correo guardado en la base de datos", Toast.LENGTH_SHORT).show();
            }
            fileWriter.close();
            cursor.close();
            baseDeDatos.close();
            admin.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al crear el fichero", Toast.LENGTH_SHORT).show();
        }


    }

    private void CSV_Personas(View v) {
        //OBTENER HORA DEL SISTEMA
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        nombre_documento="PersonasExportacionCSV_"+formatter.format(new Date())+".csv";

        file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+nombre_directorio);
        nombre_documento=file.toString()+ "/"+nombre_documento;
        boolean isCreate=false;
        if(!file.exists())
        {
            isCreate=file.mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(nombre_documento);


            Administracion_BDD admin = new Administracion_BDD(v.getContext(), "abriendoCamino", null, 1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor cursor = baseDeDatos.rawQuery("select * from persona", null);
            if(cursor.moveToFirst()){
                do{
                    //lista.add(new Persona(cursor.getString(0), cursor.getString(1),cursor.getString(2)));
                    fileWriter.append(cursor.getString(0).toString());
                    String s=cursor.getString(0);
                    fileWriter.append(",");
                    fileWriter.append(cursor.getString(1).toString());
                    fileWriter.append(",");
                    fileWriter.append(cursor.getString(2).toString());
                    fileWriter.append("\n");
                }while(cursor.moveToNext());
                Toast.makeText(getContext(), "Creado correctamente", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "No hay personas guardadas en la base de datos", Toast.LENGTH_SHORT).show();
            }
            fileWriter.close();
            cursor.close();
            baseDeDatos.close();
            admin.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al crear el fichero", Toast.LENGTH_SHORT).show();
        }




    }



}