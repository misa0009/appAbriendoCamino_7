package es.iesoretania.bdd_navigationdrawer.Objetos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.util.List;

import es.iesoretania.bdd_navigationdrawer.BaseDeDatos.Administracion_BDD;
import es.iesoretania.bdd_navigationdrawer.R;

public class Adaptador_Persona extends ArrayAdapter {
    Context ctx;
    private int layoutTemplate;
    private List<Empleado> personaList;


    public Adaptador_Persona(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        ctx = context;
        layoutTemplate = resource;
        personaList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate,parent,false);
        Empleado elementoActual = personaList.get(position);
        TextView tvNombre = (TextView) v.findViewById(R.id.tvNombre);
        TextView tvApellido = (TextView) v.findViewById(R.id.tvApellido);
        TextView tvFecha = (TextView) v.findViewById(R.id.tvFecha);
        ImageView foto = (ImageView) v.findViewById(R.id.ivFoto);
        TextView tvFechaSalida=(TextView)v.findViewById(R.id.tvFechaSalida);
        if(!elementoActual.getFechaSalida().equals("$"))
        {
            tvFechaSalida.setText(elementoActual.getFechaSalida());
        }
        else
        {
            tvFechaSalida.setText("Vacío");
        }
        Administracion_BDD admin = new Administracion_BDD(getContext(), "abriendoCamino", null, 1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        Cursor cursor = baseDeDatos.rawQuery("select * from persona where dni='"+ elementoActual.getDni() + "'", null);
        if(cursor.moveToFirst())
        {
            tvNombre.setText(cursor.getString(1));
            tvApellido.setText(cursor.getString(2));
            tvFecha.setText(elementoActual.getFecha());

            byte[] blob = elementoActual.getImagen();
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            Bitmap b = BitmapFactory.decodeStream(bais);
            foto.setImageBitmap(b);
        }
        else
        {
            Toast.makeText(v.getContext(), "Error: incongruencia en la base de datos", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        baseDeDatos.close();
        admin.close();

        return v;
    }
}
