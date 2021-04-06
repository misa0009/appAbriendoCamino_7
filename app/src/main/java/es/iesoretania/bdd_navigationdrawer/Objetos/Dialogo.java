package es.iesoretania.bdd_navigationdrawer.Objetos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import es.iesoretania.bdd_navigationdrawer.R;


public class Dialogo extends DialogFragment {
    Activity actividad;
    //IComunicaFragments iComunicaFragment;
    ImageButton salir;
    CardView cardFacil,cardMedio;

    public Dialogo(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return  CrearDialogo();
    }

    private AlertDialog CrearDialogo() {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v= inflater.inflate(R.layout.cuadro_dialogo,null);
        builder.setView(v);

        salir=v.findViewById(R.id.salirimagen);
        cardFacil=v.findViewById(R.id.tvdatos);
        cardMedio=v.findViewById(R.id.imageViewfirma);
        eventosBotones();
        return builder.create();
    }

    private void eventosBotones() {
        cardFacil.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(actividad, "nivel facil", Toast.LENGTH_SHORT).show();
            }
        });
        cardMedio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(actividad, "nivel Medio", Toast.LENGTH_SHORT).show();
            }
        });
        salir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity)
        {
            this.actividad=(Activity)context;

        }
        else
        {
            throw new RuntimeException(context.toString()+" must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cuadro_dialogo,container,false);
    }
}