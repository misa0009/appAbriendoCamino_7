package es.iesoretania.bdd_navigationdrawer.Objetos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;
import es.iesoretania.bdd_navigationdrawer.R;

public class Adaptador_Tutores extends ArrayAdapter {

    Context ctx;
    private int layoutTemplate;
    private List<Persona> personaList;


    public Adaptador_Tutores(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        ctx = context;
        layoutTemplate = resource;
        personaList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate, parent,false);
        Persona elementoActual = personaList.get(position);

        TextView dni = (TextView) v.findViewById(R.id.tvDniTutor);
        TextView nTutor = (TextView) v.findViewById(R.id.tvNombreTutor);
        TextView aTutor = (TextView) v.findViewById(R.id.tvApellidosTutor);

        dni.setText(elementoActual.getDni());
        nTutor.setText(elementoActual.getNombre());
        aTutor.setText(elementoActual.getApellidos());
        return v;
    }
}
