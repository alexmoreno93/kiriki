package com.example.alex.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.alex.proyecto.MainActivity.BBDD;

import androidx.appcompat.app.AppCompatActivity;

public class FinalActivity extends AppCompatActivity {

    private TextView nombreGanador;
    private Button bVolver,bRegistrarGanador;
    private SoundPool sp;
    private int sonidoCampeon=0;
    private SQLiteDatabase bd;
    private int Id, turnos, fallos, participantes;
    private String nombre, correo;
    private boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_final);

        bd = openOrCreateDatabase(BBDD,MODE_PRIVATE,null);

          nombre = getIntent().getStringExtra("NombreGanador");
          turnos = getIntent().getIntExtra("Turnos",0);
          fallos = getIntent().getIntExtra("Fallos",0);
          participantes = getIntent().getIntExtra("Participantes",0);
          Id = getIntent().getIntExtra("Id",0);
          login = getIntent().getBooleanExtra("Login",false);
          correo = getIntent().getStringExtra("correo");


        Log.d("Login", String.valueOf(login));


        nombreGanador = (TextView) findViewById(R.id.NombreGanador);
        bVolver = (Button) findViewById(R.id.botonMenu);
        bRegistrarGanador = (Button) findViewById(R.id.botonRegistraGanador);

        Toast.makeText(getApplicationContext(), nombre + turnos + fallos + participantes + Id, Toast.LENGTH_LONG).show();


        nombreGanador.setText(getIntent().getStringExtra("NombreGanador"));

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        sonidoCampeon = sp.load(getApplicationContext(),R.raw.campeon,0);
        sp.play(sonidoCampeon,1,1,1,0,1);

        Cursor c = bd.rawQuery("SELECT * FROM Usuario", null);

        if(c.moveToFirst()==false){
            bRegistrarGanador.setVisibility(View.GONE);
        }else{
            bRegistrarGanador.setVisibility(View.VISIBLE);
        }

        bVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Principal = new Intent(getApplicationContext(), MainActivity.class);
                Principal.putExtra("Iniciada",true);
                finish();
                startActivity(Principal);
            }
        });

        bRegistrarGanador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Ganador = new Intent(getApplicationContext(), RegistroGanadorActivity.class);
                Ganador.putExtra("Id",Id);
                Ganador.putExtra("Nombre",nombre);
                Ganador.putExtra("Turnos",turnos);
                Ganador.putExtra("Fallos",fallos);
                Ganador.putExtra("Participantes", participantes);
                Ganador.putExtra("Correo",correo);
                finish();
                startActivity(Ganador);
            }
        });
    }
}
