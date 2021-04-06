package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Objetos.Adaptador_Rutas;
import es.iesoretania.bdd_navigationdrawer.R;


public class Fragment_Importar extends Fragment {
    private List<String> listaNombresArchivos;
    private List<String> listaRutasArchivos;
    private String directorioRaiz;
    private TextView carpetaActual;
    private ListView listaRut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.fragment_importar,container,false);
       carpetaActual = (TextView) view.findViewById(R.id.tvRuta);

       directorioRaiz = Environment.getExternalStorageDirectory().getPath();
        listaRut=(ListView) view.findViewById(R.id.lvRutas);

       verArchivosDirectorio(directorioRaiz);

        return view;
    }

    /**
     * @brief Función que muestra desde la raíz un gestor de archivos para buscar el CSV a importar
     * @param rutaDirectorio
     */
    private void verArchivosDirectorio(String rutaDirectorio) {
        carpetaActual.setText("Estas en: " + rutaDirectorio);
        listaNombresArchivos = new ArrayList<String>();
        listaRutasArchivos = new ArrayList<String>();
        File directorioActual = new File(rutaDirectorio);
        File[] listaArchivos = directorioActual.listFiles();

        int x = 0;

        // Si no es nuestro directorio raiz creamos un elemento que nos
       //  permita volver al directorio padre del directorio actual
        if (!rutaDirectorio.equals(directorioRaiz)) {
            listaNombresArchivos.add("../");
            listaRutasArchivos.add(directorioActual.getParent());
            x = 1;
        }

        // Almacenamos las rutas de todos los archivos y carpetas del directorio
        for (File archivo : listaArchivos) {
            listaRutasArchivos.add(archivo.getPath());
        }

        // Ordenamos la lista de archivos para que se muestren en orden alfabetico
        Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);


        // Recorredos la lista de archivos ordenada para crear la lista de los nombres
        // de los archivos que mostraremos en el listView
        for (int i = x; i < listaRutasArchivos.size(); i++){
            File archivo = new File(listaRutasArchivos.get(i));
            if (archivo.isFile()) {
                listaNombresArchivos.add(archivo.getName());
            } else {
                listaNombresArchivos.add("/" + archivo.getName());
            }
        }

        // Si no hay ningun archivo en el directorio lo indicamos
        if (listaArchivos.length < 1) {
            listaNombresArchivos.add("No hay ningun archivo");
            listaRutasArchivos.add(rutaDirectorio);
        }


        // Creamos el adaptador y le asignamos la lista de los nombres de los
        // archivos y el layout para los elementos de la lista
        Adaptador_Rutas adaptadorrut = new Adaptador_Rutas(getContext(), R.layout.adaptador_rutas, listaNombresArchivos);
        listaRut.setAdapter(adaptadorrut);
        listaRut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtenemos la ruta del archivo en el que hemos hecho click en el
                // listView
                File archivo = new File(listaRutasArchivos.get(position));

                // Si es un archivo se muestra un Toast con su nombre y si es un directorio
                // se cargan los archivos que contiene en el listView
                if (archivo.isFile()) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                    builder.setMessage("¿Estas seguro que quieres importar el archivo "+archivo.getName()+"?").setTitle("Alerta de Importación");
                    builder.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            importacionCSV(view, archivo.getPath());

                        }


                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.create().show();
                } else {
                    // Si no es un directorio mostramos todos los archivos que contiene
                    verArchivosDirectorio(listaRutasArchivos.get(position));
                }

            }


        });
    }

    /**
     * @brief Función que realiza la importación del CSV
     * @param view
     * @param ruta
     */
    private void importacionCSV(View view, String ruta)
    {
        try {
            FileReader archCSV;
            archCSV = new FileReader(ruta);
            CSVReader csvReader = new CSVReader(archCSV);
            String[] fila = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
                SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                while ((fila = csvReader.readNext()) != null) {
                    String sql = "insert into empleados_abriendoCamino (dni, fecha, img, modo) values(?,?,?,?)";
                    SQLiteStatement insert = baseDeDatos.compileStatement(sql);
                    insert.clearBindings();
                    insert.bindString(1, fila[0]);
                    insert.bindString(2, fila[1]);
                    insert.bindBlob(3, Base64.getDecoder().decode(fila[2]));
                    insert.bindString(4, fila[3]);
                    insert.executeInsert();
                }
                baseDeDatos.close();
                admin.close();
                archCSV.close();
                csvReader.close();
            }
        }catch(Exception e){
            Toast.makeText(view.getContext(), "Se ha producido un error", Toast.LENGTH_SHORT).show();
        }
    }

}