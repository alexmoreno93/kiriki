package com.example.alex.proyecto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class JugadoresActivity extends AppCompatActivity {

    private String dato, correo;
    private int turno;
    private int cant;
    private int Id;
    private Button bEmpezar;
    private boolean login;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_jugadores);


        dato = getIntent().getStringExtra("CANTIDAD");
        correo = getIntent().getStringExtra("correo");
        Id = getIntent().getIntExtra("Id",0);
        login = getIntent().getBooleanExtra("Login",false);
        Log.d("Login", String.valueOf(login));

        turno = 0;
        cant = Integer.parseInt(dato);
        bEmpezar = (Button) findViewById(R.id.botonEmpezar);


        ScrollView layout = (ScrollView) findViewById(R.id.linearJugadores);
        layout.removeView(bEmpezar);
        LinearLayout contenedor = new LinearLayout(this);
        contenedor.setOrientation(LinearLayout.VERTICAL);
        contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        final ArrayList<String> milista = new ArrayList<String>();
        final ArrayList<EditText> listaEdit = new ArrayList<EditText>();
        final ArrayList<Integer> listaFallos = new ArrayList<Integer>();
        for (int j = 1; j <= cant; j++ ){
            EditText edit = new EditText(this);
            TextView text = new TextView(this);
            edit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            edit.setId(j);
            edit.setMinWidth(200);
            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            text.setText("\nJugador " + j);
            text.setId(j);
            contenedor.addView(text);
            contenedor.addView(edit);
            listaEdit.add(edit);

        }
         /* Button bEmpezar = new Button(this);
          bEmpezar.setText("Empezar partida");
          bEmpezar.setBackgroundColor(R.color.colorNaranja);
          bEmpezar.setTextColor(R.color.colorBlanco);
*/          bEmpezar.setGravity(Gravity.CENTER);
          contenedor.addView(bEmpezar);

        bEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean vacio = false;
                for(int cont = 0;cont<listaEdit.size();cont++){
                    if(listaEdit.get(cont).getText().toString().trim().isEmpty()){
                        vacio = true;
                    }
                }



                if(vacio){
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View formElementsView = inflater.inflate(R.layout.pantalla_jugadores,
                            null, false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(JugadoresActivity.this);
                            builder.setTitle("Debe completar todos los nombres de los jugadores.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setView(formElementsView);
                            final AlertDialog dialog = builder.create();
                            dialog.show();
                }else {

                    Intent Partida = new Intent(getApplicationContext(), PartidaActivity.class);
                    for (int j = 0; j < listaEdit.size(); j++) {
                        milista.add(listaEdit.get(j).getText().toString());
                        listaFallos.add(0);
                    }
                    Partida.putExtra("Id", Id);
                    Partida.putExtra("correo",correo);
                    Partida.putExtra("Participantes", cant);
                    Partida.putExtra("turno", turno);
                    Partida.putExtra("miLista", milista);
                    Partida.putExtra("valor", "");
                    Partida.putExtra("listaFallos", listaFallos);
                    Partida.putExtra("Login",login);
                    finish();
                    startActivity(Partida);
                }
            }
        });
        layout.addView(contenedor);
       // layout.addView(bEmpezar);


    }


}
