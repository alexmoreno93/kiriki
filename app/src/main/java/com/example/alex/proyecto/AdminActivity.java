package com.example.alex.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AdminActivity extends AppCompatActivity {


    Button bLista,bRegistrarUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_admin);

        bLista = (Button) findViewById(R.id.botonListarUsuario);
        bRegistrarUsuario = (Button) findViewById(R.id.botonRegistrarUsuario);


        bLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lista = new Intent(AdminActivity.this, ListaActivity.class);
                startActivity(lista);
            }
        });


        bRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lista = new Intent(AdminActivity.this, CrearUsuarioActivity.class);
                startActivity(lista);
            }
        });
    }
}
