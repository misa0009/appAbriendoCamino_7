package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.Funcionalidades.OcultarTeclado;
import es.iesoretania.bdd_navigationdrawer.Objetos.Empleado;
import es.iesoretania.bdd_navigationdrawer.R;
import harmony.java.awt.Color;

public class Fragment_Exportar extends Fragment {
    EditText etPlannedDate;
    EditText etDeadline;
    Button Exportar;
    ArrayList<Empleado> lista;
    String nombre_directorio = "AbriendoCamino";
    String nombre_documento;
    String fechainiForm;
    String fechafinForm;
    File file;
    String aux;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view =inflater.inflate(R.layout.fragment_exportar,container,false);

        if(ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(view.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
        }

        etPlannedDate = (EditText) view.findViewById(R.id.etPlannedDate);

        etPlannedDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    OcultarTeclado teclado= new OcultarTeclado();
                    teclado.hideKeyboard(getActivity(),getView());
                    showDatePickerDialog(etPlannedDate);
                }

            }
        });

        etDeadline = (EditText) view.findViewById(R.id.etDeadline);

        etDeadline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    OcultarTeclado teclado= new OcultarTeclado();
                    teclado.hideKeyboard(getActivity(),getView());
                    showDatePickerDialog(etDeadline);
                }

            }
        });

        Exportar=(Button)view.findViewById(R.id.btnExportar);

        Exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exportacion(v);
            }
        });

        return view;
    }

    /**
     * @brief Crea un documento PDF con los registros de entrada y salida entre las dos fechas especificadas (Para ello se usa la variable lista que tiene una lista de Empleados
     * @param view
     */
    private void crearPDF(View view) {
        Document documento = new Document(PageSize.LETTER, 0, 0, 0, 0);
        boolean fallo = false;
        FileOutputStream ficheroPDF;
        try{
            file = crearFichero(nombre_documento);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ficheroPDF = new FileOutputStream(file.getAbsolutePath());
            }else{
                ficheroPDF = getContext().openFileOutput(nombre_documento, Context.MODE_PRIVATE);
            }
            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPDF);

            documento.open();
            Paragraph titulo = new Paragraph("REGISTRO\n\n");
            titulo.setFont(FontFactory.getFont("Times New Roman", 20, Font.BOLD));
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            documento.add(titulo);

            Table tabla = new Table(6);
            tabla.setWidth(90);

            int fila = 0;
            int columna = 0;
            Cell dni = new Cell(" \nDNI\n ");
            dni.setBackgroundColor(Color.GREEN);
            dni.setHorizontalAlignment(1);
            tabla.addCell(dni);

            Cell nombre = new Cell(" \nNombre\n ");
            nombre.setBackgroundColor(Color.GREEN);
            nombre.setHorizontalAlignment(1);
            tabla.addCell(nombre);

            Cell apellido = new Cell(" \nApellidos\n ");
            apellido.setBackgroundColor(Color.GREEN);
            apellido.setHorizontalAlignment(1);
            tabla.addCell(apellido);

            Cell fecha = new Cell(" \nFecha\n ");
            fecha.setBackgroundColor(Color.GREEN);
            fecha.setHorizontalAlignment(1);
            tabla.addCell(fecha);

            Cell modo = new Cell(" \nFecha Salida\n ");
            modo.setBackgroundColor(Color.GREEN);
            modo.setHorizontalAlignment(1);
            tabla.addCell(modo);

            Cell img = new Cell(" \nImagen\n ");
            img.setHorizontalAlignment(1);
            img.setBackgroundColor(Color.GREEN);
            img.setHorizontalAlignment(1);

            tabla.addCell(img);
            tabla.endHeaders();
            //FIN DE LAS CABECERAS
            for(Empleado e:lista)
            {
                Cell dni2 = new Cell(" \n"+e.getDni()+"\n ");
                dni2.setHorizontalAlignment(1);
                tabla.addCell(dni2);

                Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
                SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
                Cursor cursor = baseDeDatos.rawQuery("select * from persona where dni='" + e.getDni() + "'", null);
                if(cursor.moveToFirst())
                {

                      Cell nombre2 = new Cell(" \n"+cursor.getString(1)+"\n ");
                      nombre2.setHorizontalAlignment(1);
                      tabla.addCell(nombre2);

                      Cell apellidos2 = new Cell(" \n"+cursor.getString(2)+"\n ");
                      apellidos2.setHorizontalAlignment(1);
                      tabla.addCell(apellidos2);

                      cursor.close();
                      baseDeDatos.close();
                      admin.close();

                      Image imagen = Image.getInstance(e.getImagen());
                      imagen.scaleToFit(70,70);

                      Cell fecha2 = new Cell(" \n"+e.getFecha()+"\n ");
                      fecha2.setHorizontalAlignment(1);
                      tabla.addCell(fecha2);

                      if(!e.getFechaSalida().equals("$")){
                          Cell modo2 = new Cell(" \n" + e.getFechaSalida() + "\n ");
                          modo2.setHorizontalAlignment(1);
                          tabla.addCell(modo2);
                      }else{

                          Cell modo2 = new Cell(" \n"+"Vacío"+"\n ");
                          modo2.setHorizontalAlignment(1);
                          tabla.addCell(modo2);
                      }
                      Cell imagen2 = new Cell(imagen);
                      imagen2.setVerticalAlignment(5);
                      imagen2.setHorizontalAlignment(1);
                      tabla.addCell(imagen2);
                }


            }
            tabla.setBorder(5);
            tabla.setCellsFitPage(true);
            //tabla.setBackgroundColor(Color.LIGHT_GRAY);
            documento.add(tabla);
        }catch(DocumentException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Problema al crear el documento", Toast.LENGTH_SHORT).show();
            fallo = true;
        }catch(IOException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Problema al crear el archivo", Toast.LENGTH_SHORT).show();
            fallo = true;
        }finally{
            documento.close();
            if(!fallo) {
                Toast.makeText(getContext(), "Transacción finalizada", Toast.LENGTH_SHORT).show();
                enviarCorreo(view);
            }

        }
    }

    /**
     * @brief Inicia el intent de enviar un correo. Para ello le pasa el PDF creado anteriormente
     * @param v
     */
    private void enviarCorreo(View v) {
        Administracion_BDD admin = new Administracion_BDD(v.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        Cursor cursor = baseDeDatos.rawQuery("select * from datos_correos", null);
        if(cursor.moveToFirst()) {
            Uri uri = null;
            aux=cursor.getString(0);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                uri = FileProvider.getUriForFile(getContext(), "AUTORIDAD.fileProvider", file);
            }else{
                uri = Uri.fromFile(new File(getContext().getExternalCacheDir(),nombre_documento));
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {aux});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Exportacion de firmas desde " + fechainiForm + " hasta " + fechafinForm);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Enviar mensaje con"));
        }
        else
        {
            Toast.makeText(getContext(), "No existe ningun Mail", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        baseDeDatos.close();
        admin.close();
    }

    /**
     * @brief Función que gestiona el lugar de almacenamiento del PDF
     * @param nombreFichero
     * @return El fichero creado
     */
    public File crearFichero(String nombreFichero) {
        File ruta = getRuta();
        File fichero = null;
        if(ruta != null) {
            fichero = new File(ruta + File.separator + nombreFichero);
            if (fichero.exists())
                fichero.delete();
            fichero = new File(ruta, nombreFichero);
        }
        return fichero;
    }

    /**
     * @brief Función que comprueba la ruta de almacenamiento interno del móvil (Comprueba la versión del dispositivo para utilizar una ruta u otra)
     * @return
     */
    public File getRuta(){
        File ruta = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                ruta = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nombre_directorio);
                if (ruta != null) {
                    if (!ruta.mkdirs()) {
                        if (!ruta.exists()) {
                            return null;
                        }
                    }
                }
            }
        }else{
            ruta = new File(getContext().getExternalCacheDir(),nombre_directorio);
        }
        return ruta;
    }

    /**
     * @brief Función que obtiene todos los registros de entrada/salida entre dos fechas y los guarda en la variable lista
     * @param view
     */
    public void exportacion(View view) {
        Administracion_BDD admin = new Administracion_BDD(view.getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String[] str = etPlannedDate.getText().toString().split("/");
        Date f_ini = new Date(Integer.parseInt(str[2]) - 1900, Integer.parseInt(str[1])-1, Integer.parseInt(str[0]));
        fechainiForm = formatter.format(f_ini);
        String[] str2 = etDeadline.getText().toString().split("/");
        Date f_fin = new Date(Integer.parseInt(str2[2]) - 1900, Integer.parseInt(str2[1])-1, Integer.parseInt(str2[0]));
        fechafinForm = formatter.format(f_fin);
        String fechainiFormNom = fechainiForm.replace("/", "-");
        String fechafinFormNom = fechafinForm.replace("/", "-");
        nombre_documento = "Exportacion_" + fechainiFormNom + "_" + fechafinFormNom + ".pdf";
        lista = new ArrayList<>();
        Cursor cursor = baseDeDatos.rawQuery("select * from empleados_abriendoCamino where fecha between '" + fechainiForm +"' and '" + fechafinForm + "'  ORDER BY fecha", null);
        if(cursor.moveToFirst()){
            do{
                lista.add(new Empleado(cursor.getString(1), cursor.getString(2), cursor.getBlob(3),cursor.getString(4)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        baseDeDatos.close();
        admin.close();
        crearPDF(view);
    }

    /**
     * @brief Función que crea un DatePicker para seleccionar la fecha de inicio y fin de la exportación
     * @param editText
     */
    private void showDatePickerDialog(final EditText editText) {
        OcultarTeclado ocultarTeclado=new OcultarTeclado();
        ocultarTeclado.hideKeyboard(getActivity(),getView());

        Fragment_DatePicker newFragment = Fragment_DatePicker.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editText.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    /**
     * @brief Función que recibe un numero y lo modifica para que tenga dos dígitos
     * @param n : El número
     * @return Un string con el número en dos dígitos
     */
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
}

