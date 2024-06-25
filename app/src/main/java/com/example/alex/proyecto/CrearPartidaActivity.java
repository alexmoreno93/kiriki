package com.example.alex.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
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

public class CrearPartidaActivity extends AppCompatActivity {

    String url = "http://proyectokiriki.000webhostapp.com/crearPartida.php";
    String urlLista = "http://proyectokiriki.000webhostapp.com/comprobarPartida.php";
    String IdUsuario,IdPartida;
    Boolean espera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_crear_partida);

        final String correo = getIntent().getStringExtra("Correo");

        AsyncHttpClient cliente = new AsyncHttpClient();
        RequestParams rp = new RequestParams();
        rp.put("correo",correo);
        cliente.get(url, rp , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    String respuesta = response.toString();
                    Gson gson = new Gson();
                    CrearPartida partida = gson.fromJson(respuesta, CrearPartida.class);
                    IdUsuario = partida.getIdUsuario();
                    Log.d("pruebaparUsuario", IdUsuario);

                    int res = partida.getSuccess();

                    //Si es correcta
                    if (res == 1) {
                        Toast.makeText(getApplicationContext(), "La partida se ha creado.", Toast.LENGTH_LONG).show();


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




        new Handler().postDelayed( new Runnable(){
            public void run(){
/*
final Handler handler = new Handler();
                Timer timer = new Timer();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                try {

                                               } catch (Exception e) {
                                    Log.e("error", e.getMessage());
                                }
                            }
                        });
                    }
                };

                timer.schedule(task, 0, 3000);

 */
                final Handler handler = new Handler();
                Timer timer = new Timer();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    //Ejecuta tu AsyncTask!
                                    AsyncHttpClient clienteLista = new AsyncHttpClient();
                                    RequestParams rp = new RequestParams();
                                    rp.put("IdUsuario",IdUsuario);
                                    clienteLista.get(urlLista,rp, new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            String valorDevuelto = new String(response.toString());
                                            Gson gson = new Gson();
                                            ComprobarPartida comprobar = gson.fromJson(valorDevuelto, ComprobarPartida.class);
                                            IdPartida = comprobar.getIdPartida();
                                            int res = comprobar.getSuccess();

                                            //Si es correcta
                                            if (res == 1) {
                                                final ProgressDialog progressDialog = new ProgressDialog(CrearPartidaActivity.this);

                                                if(Integer.valueOf(comprobar.getCantidad())==1) {

                                                    if(espera==false) {

                                                        progressDialog.setIcon(R.mipmap.ic_launcher);
                                                        progressDialog.setMessage("Esperando a otro jugador...");
                                                        progressDialog.show();
                                                        espera = true;
                                                    }
                                                    //Toast.makeText(getApplicationContext(), "ESPERANDO OTRO JUGADOR.", Toast.LENGTH_LONG).show();
                                                }else{


                                                    progressDialog.dismiss();
                                                    final ArrayList<Integer> listaFallos = new ArrayList<Integer>();
                                                    listaFallos.add(0);
                                                    listaFallos.add(0);
                                                    listaFallos.add(0);
                                                    Intent Partida = new Intent(getApplicationContext(), SalaEsperaActivity.class);
                                                    Partida.putExtra("listaFallos", listaFallos);
                                                    Partida.putExtra("IdPartida",IdPartida);
                                                    Partida.putExtra("IdUsuario",IdUsuario);
                                                    Partida.putExtra("correo",correo);
                                                    finish();
                                                    startActivity(Partida);


                                                    //Toast.makeText(getApplicationContext(), "PARTIDA COMPLETA.", Toast.LENGTH_LONG).show();

                                                }

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



                                } catch (Exception e) {
                                    Log.e("error", e.getMessage());
                                }
                            }
                        });
                    }
                };

                timer.schedule(task, 0, 3000);

            }
        }, 2500);



    }
}
