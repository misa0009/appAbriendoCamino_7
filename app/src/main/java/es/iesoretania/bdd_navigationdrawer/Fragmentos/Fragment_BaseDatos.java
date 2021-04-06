package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.iesoretania.bdd_navigationdrawer.R;


public class Fragment_BaseDatos extends Fragment {
    Button buttoncsv;
    Button buttonborrar;
    Button buttonImportar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.fragment_bdd,container,false);
        buttoncsv=(Button) view.findViewById(R.id.btnCSV);
        buttonborrar=(Button) view.findViewById(R.id.btnBorrar);
        buttonImportar=(Button) view.findViewById(R.id.btnImport);

        if(ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(view.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},1000);
        }

        buttonImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               NavHostFragment.findNavController(Fragment_BaseDatos.this).navigate(R.id.action_fragment_BaseDatos_to_nav_Importar);
            }
        });

        buttonborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Fragment_BaseDatos.this).navigate(R.id.action_fragment_BaseDatos_to_nav_borrarBD);
            }
        });

        buttoncsv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Fragment_BaseDatos.this).navigate(R.id.action_fragment_BaseDatos_to_nav_opciones);
            }
        });

        return  view;
    }
}