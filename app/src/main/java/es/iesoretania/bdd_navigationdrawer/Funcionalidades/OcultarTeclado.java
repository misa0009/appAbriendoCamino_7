package es.iesoretania.bdd_navigationdrawer.Funcionalidades;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.FragmentActivity;

public class OcultarTeclado {


    public void hideKeyboard(FragmentActivity activity, View v) {
        v.clearFocus();
        if(v!=null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
