package es.iesoretania.bdd_navigationdrawer.Objetos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import es.iesoretania.bdd_navigationdrawer.R;

public class Cuadro_Tutelados {
    @SuppressLint("ResourceAsColor")
    public Cuadro_Tutelados(Context contexto, String s)
    {

        final Dialog dialogo= new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialogo.setContentView(R.layout.cuadro_tutores);

        //A partir de aqui cambiar
        final TextView datosTutores=(TextView) dialogo.findViewById(R.id.tvTutor);
        datosTutores.setBackgroundColor(Color.rgb(124,200,110));
        String[] data = s.split("#");
        datosTutores.setText("\n\tDNI: "+ data[0] + "\n\n\tNombre: " + data[1] + " " + data[2] +"\n\n\tEdad: "+data[3]+ "\n\n\tDomicilio: " + data[4] + "\n\n\tCódigo postal: " + data[5] + "\n\n\tPoblación: " + data[6] + "\n\n\tProvincia: " + data[7] + "\n\n\tTeléfono: " + data[8] + "\n\n\tEmail: " + data[9] + "\n\n\tObservaciones: " + data[10]);
        ImageView salir= (ImageView) dialogo.findViewById(R.id.salirimagen);

        salir.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         dialogo.dismiss();
                                     }
                                 }
        );
        dialogo.show();
    }
}
