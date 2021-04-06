package es.iesoretania.bdd_navigationdrawer.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.iesoretania.bdd_navigationdrawer.R;

public class Fragment_inicio extends Fragment {

    private Button fichar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view=inflater.inflate(R.layout.fragment_inicio,container,false);

        fichar=(Button)view.findViewById(R.id.btnFichar);

        fichar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Fragment_inicio.this)
                        .navigate(R.id.action_nav_inicio_to_nav_matricular);
            }
        });


        return view;
    }
}