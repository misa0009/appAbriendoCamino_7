package es.iesoretania.bdd_navigationdrawer.Objetos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import es.iesoretania.bdd_navigationdrawer.R;

public class CuadroDialogo  {

    @SuppressLint("ResourceAsColor")
    public CuadroDialogo(Context contexto, Drawable d,String s)
    {

        final Dialog dialogo= new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialogo.setContentView(R.layout.cuadro_dialogo);
        final TextView datos=(TextView) dialogo.findViewById(R.id.tvdatos);
        datos.setBackgroundColor(Color.rgb(124,200,110));
        String[] data = s.split("#");
        datos.setText("\n\tNombre: " + data[3] + " " + data[4] + "\n\n\tDNI: " + data[0] + "\n\n\tFecha: " + data[1] + "\n\n\tFecha Salida: " + data[2]);
        ImageView salir= (ImageView) dialogo.findViewById(R.id.salirimagen);
        ImageView firma=(ImageView) dialogo.findViewById(R.id.imageViewfirma);
        firma.setImageDrawable(d);


        salir.setOnClickListener(new View.OnClickListener()
             {
                 @Override
                 public void onClick(View v) {
                     dialogo.dismiss();
                 }
             }
        );
        dialogo.show();
    }




}
