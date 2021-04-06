package es.iesoretania.bdd_navigationdrawer.Objetos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import es.iesoretania.bdd_navigationdrawer.R;

public class Adaptador_Rutas extends ArrayAdapter {
    Context ctx;
    private int layoutTemplate;
    List<String> listaRuta;
    private TextView rut;

    public Adaptador_Rutas(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        ctx = context;
        layoutTemplate = resource;
        listaRuta= objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate,parent,false);
        String elementoActual = listaRuta.get(position);
        rut=(TextView) v.findViewById(R.id.listruta);
        rut.setText(elementoActual);
        return v;

    }
}
