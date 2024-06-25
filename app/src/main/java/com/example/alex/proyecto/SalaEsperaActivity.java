package com.example.alex.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import static com.example.alex.proyecto.MainActivity.Nrepetir;
import static com.example.alex.proyecto.MainActivity.creado;

import androidx.appcompat.app.AppCompatActivity;

public class SalaEsperaActivity extends AppCompatActivity {

    String urlTurno = "http://proyectokiriki.000webhostapp.com/comprobarTurno.php";
    String url = "http://proyectokiriki.000webhostapp.com/crearPartida.php";
    Intent Partida;
    String correo, turnoJugador, turnoActual, IdPartida, IdUsuario, valorRecibidoReal, valorRecibido;
    Boolean espera = false;
    ArrayList<Integer> listafallos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_crear_partida);

        IdPartida = getIntent().getStringExtra("IdPartida");
        IdUsuario = getIntent().getStringExtra("IdUsuario");
        correo = getIntent().getStringExtra("correo");
        listafallos = (ArrayList<Integer>) getIntent().getSerializableExtra("listaFallos");


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
                    //IdUsuario = partida.getIdUsuario();
                    Log.d("pruebaparUsuario", IdUsuario);

                    int res = partida.getSuccess();

                    //Si es correcta
                    if (res == 1) {


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
        new Handler().postDelayed( new Runnable(){
            public void run(){
                final Handler handler = new Handler();
                final Timer timer = new Timer();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    //Ejecuta tu AsyncTask!
                                    AsyncHttpClient clienteLista = new AsyncHttpClient();
                                    RequestParams rp = new RequestParams();
                                    rp.put("IdPartida",IdPartida);
                                    rp.put("IdUsuario",IdUsuario);

                                    clienteLista.get(urlTurno,rp, new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            String valorDevuelto = new String(response.toString());
                                            Gson gson = new Gson();
                                            ComprobarTurno comprobar = gson.fromJson(valorDevuelto, ComprobarTurno.class);
                                            turnoJugador = comprobar.getPosicion();
                                            turnoActual = comprobar.getTurno();
                                            valorRecibidoReal = comprobar.getValorRecibidoReal();
                                            valorRecibido = comprobar.getValorRecibido();

                                            int res = comprobar.getSuccess();

                                            //Si es correcta
                                            if (res == 1) {
                                                final ProgressDialog progressDialog = new ProgressDialog(SalaEsperaActivity.this);
                                                if(comprobar.getTurno().equalsIgnoreCase("0")) {
                                                    Toast.makeText(getApplicationContext(), "LO SIENTO PERDISTE, INTENTALO DE NUEVO.", Toast.LENGTH_LONG).show();
                                                    Intent Inicio = new Intent(SalaEsperaActivity.this, MainActivity.class);
                                                }else{



                                                    if(comprobar.getPosicion().equalsIgnoreCase(comprobar.getTurno())==false) {

                                                    if(espera==false) {

                                                        progressDialog.setIcon(R.mipmap.ic_launcher);
                                                        progressDialog.setMessage("Esperando mi turno...");
                                                        progressDialog.show();
                                                        espera = true;
                                                    }
                                                    //Toast.makeText(getApplicationContext(), "ESPERANDO OTRO JUGADOR.", Toast.LENGTH_LONG).show();
                                                }else {

                                                    timer.cancel();
                                                    timer.purge();
                                                    progressDialog.dismiss();
                                                    final ArrayList<Integer> listaFallos = new ArrayList<Integer>();
                                                    listaFallos.add(0);
                                                    listaFallos.add(0);
                                                    listaFallos.add(0);
                                                    if (creado == false) {
                                                        Partida = new Intent(SalaEsperaActivity.this, PartidaOnlineActivity.class);
                                                        creado = true;
                                                    }

                                                    Intent Partida = new Intent(SalaEsperaActivity.this, PartidaOnlineActivity.class);
                                                    Partida.putExtra("listaFallos", listaFallos);
                                                    Partida.putExtra("IdPartida", IdPartida);
                                                    Partida.putExtra("IdUsuario", IdUsuario);
                                                    Partida.putExtra("correo", correo);
                                                    Partida.putExtra("turnoActual", turnoActual);
                                                    Partida.putExtra("valorRecibido", valorRecibido);
                                                    Partida.putExtra("valorRecibidoReal", valorRecibidoReal);
                                                    finish();
                                                    startActivity(Partida);


                                                    //Toast.makeText(getApplicationContext(), "PARTIDA COMPLETA.", Toast.LENGTH_LONG).show();
                                                }
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
/*
        final Handler handler = new Handler();
        final Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{


                        AsyncHttpClient clienteTurno = new AsyncHttpClient();
                        RequestParams rpTurno = new RequestParams();
                        rpTurno.put("IdPartida", IdPartida);
                        rpTurno.put("IdUsuario", IdUsuario);


                        clienteTurno.get(urlTurno, rpTurno, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                if (statusCode == 200) {
                                    String respuesta = response.toString();
                                    Gson gson = new Gson();
                                    ComprobarTurno turno = gson.fromJson(respuesta, ComprobarTurno.class);

                                    int res = turno.getSuccess();
                                    Log.d("DEBUG - MENSAJE TURNO", turno.getMessage());
                                    //Si es correcta
                                    if (res == 1) {


                                        turnoJugador = turno.getPosicion();
                                        turnoActual = turno.getTurno();
                                        valorRecibidoReal = turno.getValorRecibidoReal();
                                        valorRecibido = turno.getValorRecibido();

                                        if (!turnoJugador.equalsIgnoreCase(turnoActual)) {

                                            if (espera == false) {
                                                Log.d("DIALOG", "onSuccess: ");

                                                progressDialog.setIcon(R.mipmap.ic_launcher);
                                                progressDialog.setMessage("Esperando mi turno...");
                                                progressDialog.show();
                                                espera = true;
                                            }
                                        } else {

                                            Log.d("INTENT", "onSuccess: ");
                                            progressDialog.dismiss();
                                            final ArrayList<Integer> listaFallos = new ArrayList<Integer>();
                                            listaFallos.add(0);
                                            listaFallos.add(0);
                                            listaFallos.add(0);
                                            Intent Partida = new Intent(getApplicationContext(), PartidaOnlineActivity.class);
                                            Partida.putExtra("listaFallos", listaFallos);
                                            Partida.putExtra("IdPartida", IdPartida);
                                            Partida.putExtra("IdUsuario", IdUsuario);
                                            Partida.putExtra("correo", correo);
                                            finish();
                                            startActivity(Partida);



                                        }

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
                        } catch (Exception e) {
                            Log.e("error",e.getMessage());
                        }
                    }
                });



            }


        };
        timer.schedule(task, 0, 3000);
*/
    }
}
