package com.example.alex.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class ListaPartidasActivity extends AppCompatActivity {

    String url = "http://proyectokiriki.000webhostapp.com/devolverPartidas.php";
    ListView lista;
    Gson gson;
    AsyncHttpClient cliente;
    Partida partidas;
    AdaptadorPartida adaptador;
    String IdPartida, IdUsuario;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_lista_partidas);


        final String correo = getIntent().getStringExtra("Correo");

        lista = (ListView) findViewById(R.id.listaPartidas);



        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            //Ejecuta tu AsyncTask!
                            cliente = new AsyncHttpClient();
                            RequestParams rp = new RequestParams();
                            cliente.get(url,rp, new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    String valorDevuelto = new String(response.toString());
                                    gson = new Gson();
                                    partidas = gson.fromJson(valorDevuelto, Partida.class);
                                    adaptador = new AdaptadorPartida(ListaPartidasActivity.this,partidas.getPartida());
                                    if(lista == null){
                                        Toast.makeText(getApplicationContext(), "Lista vacia", Toast.LENGTH_LONG).show();

                                    }else {
                                        lista.setAdapter(adaptador);
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }

                                @Override
                                public boolean getUseSynchronousMode() {
                                    return false;
                                }

                            });

                            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Partida.PartidaBean pSelecion = (Partida.PartidaBean) lista.getItemAtPosition(i);
                                    String urlUnirse = "http://proyectokiriki.000webhostapp.com/unirsePartida.php";
                                    IdPartida = pSelecion.getIdPartida();

                                    AsyncHttpClient clienteUnirse = new AsyncHttpClient();
                                    RequestParams rpUnirse = new RequestParams();
                                    rpUnirse.put("correo",correo);
                                    rpUnirse.put("IdPartida",IdPartida);

                                    clienteUnirse.get(urlUnirse, rpUnirse , new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            if (statusCode == 200) {
                                                String respuesta = response.toString();
                                                Gson gson = new Gson();
                                                UnirsePartida partida = gson.fromJson(respuesta, UnirsePartida.class);
                                                IdUsuario = partida.getIdUsuario();
                                                int res = partida.getSuccess();

                                                //Si es correcta
                                                if (res == 1) {
                                                    Toast.makeText(getApplicationContext(), "Unido a la partida.", Toast.LENGTH_LONG).show();
                                                    final ArrayList<Integer> listaFallos = new ArrayList<Integer>();
                                                    listaFallos.add(0);
                                                    listaFallos.add(0);
                                                    listaFallos.add(0);
                                                    Intent Partida = new Intent(getApplicationContext(), SalaEsperaActivity.class);
                                                    Partida.putExtra("correo",correo);
                                                    Partida.putExtra("listaFallos", listaFallos);
                                                    Partida.putExtra("IdPartida",IdPartida);
                                                    Partida.putExtra("IdUsuario",IdUsuario);
                                                    startActivity(Partida);

                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                        }

                                        @Override
                                        public boolean getUseSynchronousMode() {
                                            return false;
                                        }
                                    });

                                }
                            });

                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 3000);



    }
    public void showToast(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}
