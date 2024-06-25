package com.example.alex.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.alex.proyecto.MainActivity.BBDD;

import androidx.appcompat.app.AppCompatActivity;

public class RegistroGanadorActivity extends AppCompatActivity {

    String url = "http://proyectokiriki.000webhostapp.com/registroGanador.php";
    String nombre, correo;
    int turnos, fallos, participantes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_recibirtirada);



                nombre = getIntent().getStringExtra("Nombre");
                turnos = getIntent().getIntExtra("Turnos",0);
                fallos = getIntent().getIntExtra("Fallos",0);
                participantes = getIntent().getIntExtra("Participantes",0);
                int id = getIntent().getIntExtra("Id",0);
                correo = getIntent().getStringExtra("Correo");




                    AsyncHttpClient cliente = new AsyncHttpClient();
                    RequestParams rp = new RequestParams();
                     rp.put("Nombre",nombre);
                     rp.put("Turnos",turnos);
                     rp.put("Fallos",fallos);
                     rp.put("Participantes",participantes);
                     rp.put("Correo",correo);


                    cliente.get(url, rp, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (statusCode == 200) {
                                String respuesta = response.toString();
                                Gson gson = new Gson();
                                RegistroUsuario registro = gson.fromJson(respuesta, RegistroUsuario.class);

                                String mensaje = registro.getMessage();
                                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
                                int res = registro.getSuccess();


                                if (res == 1) {
                                    Toast.makeText(getApplicationContext(), "El registro se ha realizado sin problemas.", Toast.LENGTH_LONG).show();


                                    Intent Principal = new Intent(getApplicationContext(), MainActivity.class);
                                    Principal.putExtra("Iniciada",true);
                                    finish();
                                    startActivity(Principal);

                                }
                            }
                        }
                    });


               }

            }





