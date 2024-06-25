package com.example.alex.proyecto;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EstadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_estado);

        ScrollView layout = (ScrollView) findViewById(R.id.linearEstado);

        LinearLayout contenedor = new LinearLayout(this);
        contenedor.setOrientation(LinearLayout.VERTICAL);
        contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        final ArrayList<String> lista = (ArrayList<String>) getIntent().getSerializableExtra("miLista");
        final ArrayList<Integer> listaFallos = (ArrayList<Integer>) getIntent().getSerializableExtra("listaFallos");

        for (int j = 0; j < lista.size(); j++ ){
            LinearLayout contenedorInterior = new LinearLayout(this);
            contenedor.setOrientation(LinearLayout.VERTICAL);
            contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            String fallo = listaFallos.get(j).toString();
            TextView nombre = new TextView(this);
            nombre.setTextSize(20);
            nombre.setTypeface(nombre.getTypeface(), Typeface.BOLD);
            nombre.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            nombre.setId(j);
            if (Integer.parseInt(fallo) == 1) {
                nombre.setText("El jugador " + lista.get(j) + " tiene " + fallo + " fallo");
            }else{
                nombre.setText("El jugador " + lista.get(j) + " tiene " + fallo + " fallos");
            }
            contenedorInterior.addView(nombre);
            contenedor.addView(contenedorInterior);

        }
        layout.addView(contenedor);
    }
}
