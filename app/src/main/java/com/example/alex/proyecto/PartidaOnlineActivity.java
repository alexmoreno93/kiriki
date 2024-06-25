package com.example.alex.proyecto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;

import static com.example.alex.proyecto.MainActivity.creado;
import static com.example.alex.proyecto.MainActivity.noRepetir;
import static com.example.alex.proyecto.MainActivity.Nrepetir;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PartidaOnlineActivity extends AppCompatActivity {

    private Button bAyuda;
    private Button bTirar;
    private Button bEstado;
    private ImageView dado;
    private ImageView dado2;
    private SoundPool sp;
    private int sonidoDado = 0, sonidoAcierto = 0, sonidoError = 0;
    private TextView nombreJugador;
    private int turno;
    private String valor, valorRecibido, valorReal, valorRecibidoReal;
    private Spinner spinnerValores;
    private ArrayList<Integer> listaFallosVariable;
    private boolean eliminacion, tirada = false;
    private int Id, espera = 3000, turnoOtro;
    private AnimationDrawable animacion;
    private AnimationDrawable animacion2;
    private int valorDado = 0;
    private int valorDado2 = 0;
    private ArrayList<String> lista;
    private String turnoJugador, turnoActual, IdPartida, IdUsuario;
    private boolean dialogo = false;
    private LinearLayout layout;
    private String correo;



    //private String urlTurno = "http://proyectokiriki.000webhostapp.com/comprobarTurno.php";
    private String urlCorreos = "http://proyectokiriki.000webhostapp.com/correosPartida.php";
    private String urlFallos = "http://proyectokiriki.000webhostapp.com/fallosPartida.php";
    private String urlEliminacion = "http://proyectokiriki.000webhostapp.com/eliminarParticipante.php";
    private String urlActualizarValores = "http://proyectokiriki.000webhostapp.com/updateValores.php";
    private String urlOtroEliminacion = "http://proyectokiriki.000webhostapp.com/eliminarOtroParticipante.php";
    private String urlActualizarFallos = "http://proyectokiriki.000webhostapp.com/updateFallos.php";
    private String urlActualizarOtroFallos = "http://proyectokiriki.000webhostapp.com/updateOtroFallos.php";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("¡Cuidado!")
                    .setMessage("¿Desea salir de la partida?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            creado = false;
                            Intent Principal = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(Principal);
                        }
                    })
                    .show();
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_partida);

        bAyuda = (Button) findViewById(R.id.botonAyudaPartida);
        bTirar = (Button) findViewById(R.id.botonTirar);
        bEstado = (Button) findViewById(R.id.botonEstadoPartida);
        dado = (ImageView) findViewById(R.id.Dado);
        dado2 = (ImageView) findViewById(R.id.Dado2);
        nombreJugador = (TextView) findViewById(R.id.nombreJugador);
        animacion = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.animacion);
        animacion2 = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.animacion);
        lista = new ArrayList<String>();
        listaFallosVariable = new ArrayList<Integer>();

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sonidoDado = sp.load(getApplicationContext(), R.raw.dado, 0);
        sonidoAcierto = sp.load(getApplicationContext(), R.raw.acierto, 0);
        sonidoError = sp.load(getApplicationContext(), R.raw.error, 0);
        layout = (LinearLayout) findViewById(R.id.linearPartida);
        dado.setImageDrawable(animacion);
        dado2.setImageDrawable(animacion2);
        IdPartida = getIntent().getStringExtra("IdPartida");
        IdUsuario = getIntent().getStringExtra("IdUsuario");
        correo = getIntent().getStringExtra("correo");
        turnoActual = getIntent().getStringExtra("turnoActual");
        turnoJugador = getIntent().getStringExtra("turnoJugador");
        valorRecibido = getIntent().getStringExtra("valorRecibido");
        valorRecibidoReal = getIntent().getStringExtra("valorRecibidoReal");
        turno = Integer.parseInt(turnoActual);
        nombreJugador.setText(correo);
        if(turno == 1){
            turnoOtro = 2;
        }else{
            turnoOtro = 1;
        }

        //LISTA DE CORREOS, SE COGERA CON GSON
        AsyncHttpClient clienteBorrado = new AsyncHttpClient();
        RequestParams rpBorrado = new RequestParams();
        rpBorrado.put("IdPartida", IdPartida);
        rpBorrado.put("IdUsuario",IdUsuario);

        clienteBorrado.get("https://proyectokiriki.000webhostapp.com/eliminarPartidas.php?", rpBorrado, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    String respuesta = response.toString();
                    Gson gson = new Gson();
                    BorradoPartidas correos = gson.fromJson(respuesta, BorradoPartidas.class);
                    int res = correos.getSuccess();



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





                    AsyncHttpClient clienteCorreos = new AsyncHttpClient();
                    RequestParams rpCorreos = new RequestParams();
                    rpCorreos.put("IdPartida", IdPartida);

                    clienteCorreos.get(urlCorreos, rpCorreos, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (statusCode == 200) {
                                String respuesta = response.toString();
                                Gson gson = new Gson();
                                CorreosPartida correos = gson.fromJson(respuesta, CorreosPartida.class);
                                int res = correos.getSuccess();


                                lista.addAll(correos.getCorreos());
;


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


                    AsyncHttpClient clienteFallos = new AsyncHttpClient();
                    RequestParams rpFallos = new RequestParams();
                    rpFallos.put("IdPartida", IdPartida);

                    clienteFallos.get(urlFallos, rpFallos, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (statusCode == 200) {
                                String respuesta = response.toString();
                                Gson gson = new Gson();
                                FallosPartida fallos = gson.fromJson(respuesta, FallosPartida.class);

                                for (String s : fallos.getFallos()) {
                                    listaFallosVariable.add(Integer.valueOf(s));

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







        //ESTE TURNO SE COGERA DE LA WEB EN VEZ DE UN INTENT, PRIMERO SE COGE EL TURNO Y LUEGO SE COMPRUEBA SI COINCIDE CON EL DEL JUGADOR.
      /*  handler = new Handler();
        timer = new Timer();
        Log.d("REPETIR", String.valueOf(Nrepetir));
        // ESTE ES EL UNICO LOG QUE MUESTRA, REPITIENDOLO
        //if (Nrepetir == true) { // PRIMERA COMPROBACIÓN QUE COGE TODOS LOS MÉTODOS
            Log.d("REPETIR", "ENTRAif ");

            task = new TimerTask() {
                @Override
                public void run() {
                    Log.d("REPETIR", "ENTRAtask ");
                    Nrepetir = false;

                    //handler.post(new Runnable() {
                    //    public void run() {
                    //try {
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

                                    //Si es correcta

                                    turnoJugador = turno.getPosicion();
                                    turnoActual = turno.getTurno();
                                    valorRecibidoReal = turno.getValorRecibidoReal();
                                    valorRecibido = turno.getValorRecibido();


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
*/
        /*

        new Handler().postDelayed( new Runnable(){
            public void run(){
      }
        }, 2500);
         */

                       // final ProgressDialog progressDialog = new ProgressDialog(PartidaOnlineActivity.this);


                      /*  new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Log.d("REPETIRRUN2", "Entrarun");

                                if (Nrepetir == true && turnoActual.equalsIgnoreCase(turnoJugador)) { // SEGUNDA PARA CUANDO COINCIDE CON EL TURNO COMPARAR VALORES...

                                    Log.d("ENTRA1", String.valueOf(Nrepetir));
                                    Nrepetir = false;
                                    Log.d("ENTRA1", String.valueOf(Nrepetir));

                                    //progressDialog.dismiss();


                                    AsyncHttpClient clienteCorreos = new AsyncHttpClient();
                                    RequestParams rpCorreos = new RequestParams();
                                    rpCorreos.put("IdPartida", IdPartida);

                                    clienteCorreos.get(urlCorreos, rpCorreos, new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            if (statusCode == 200) {
                                                String respuesta = response.toString();
                                                Gson gson = new Gson();
                                                CorreosPartida correos = gson.fromJson(respuesta, CorreosPartida.class);
                                                int res = correos.getSuccess();


                                                lista.addAll(correos.getCorreos());


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

                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {

                                            turno = Integer.parseInt(turnoActual);

                                            //SE COGERA DE LA LISTA DE CORREOS

                                            AsyncHttpClient clienteFallos = new AsyncHttpClient();
                                            RequestParams rpFallos = new RequestParams();
                                            rpFallos.put("IdPartida", IdPartida);

                                            clienteFallos.get(urlFallos, rpFallos, new JsonHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                    if (statusCode == 200) {
                                                        String respuesta = response.toString();
                                                        Gson gson = new Gson();
                                                        FallosPartida fallos = gson.fromJson(respuesta, FallosPartida.class);
                                                        for (String s : fallos.getFallos())
                                                            listaFallosVariable.add(Integer.valueOf(s));

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

                                            // final ArrayList<Integer> listaFallos = (ArrayList<Integer>) getIntent().getSerializableExtra("listaFallos");
                                            // listaFallosVariable = listaFallos;

                                            turno = getIntent().getIntExtra("turno", 0);

//        participantes = getIntent().getIntExtra("Participantes", 0);
                                            //       valorRecibido = getIntent().getStringExtra("valor");
                                            //     valorRecibidoReal = getIntent().getStringExtra("valorReal");
                                            //Id = getIntent().getIntExtra("Id", Id);
                           */                 eliminacion = false;
                                            LinearLayout contenedor = new LinearLayout(getApplicationContext());
                                            contenedor.setOrientation(LinearLayout.HORIZONTAL);
                                            contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                            //COMPROBACIÓN DE QUE SEA LA PRIMERA VEZ O LUEGO DE UNA ELIMINACIÓN
                                            if (!valorRecibido.equalsIgnoreCase("0")) {
                                                //SI NO LO ES SE COMPRUEBA QUE NO SEA UN KIRIKI EL VALOR ENVIADO
                                                if (valorRecibido.equalsIgnoreCase("Kiriki")) {
                                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                    final View formElementsView = inflater.inflate(R.layout.pantalla_recibirtirada,
                                                            null, false);
                                                    new AlertDialog.Builder(PartidaOnlineActivity.this).setView(formElementsView)
                                                            .setTitle("Recibiste kiriki, se te sumara un punto y se te pasara de turno. ")
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    int fallo = listaFallosVariable.get(turno - 1) + 1;
                                                                    listaFallosVariable.set(turno - 1, fallo);
                                                                    if (listaFallosVariable.get(turno - 1) == 3) {
                                                                        lista.remove(turno - 1);
                                                                        listaFallosVariable.remove(turno - 1);
                                                                        AsyncHttpClient clienteEliminacion = new AsyncHttpClient();
                                                                        RequestParams rpEliminacion = new RequestParams();
                                                                        rpEliminacion.put("IdPartida", IdPartida);
                                                                        rpEliminacion.put("IdUsuario", IdUsuario);

                                                                        clienteEliminacion.get(urlEliminacion, rpEliminacion, new JsonHttpResponseHandler() {
                                                                            @Override
                                                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                super.onSuccess(statusCode, headers, response);
                                                                                if (statusCode == 200) {
                                                                                    String respuesta = response.toString();
                                                                                    Gson gson = new Gson();
                                                                                    BorradoParticipante borrado = gson.fromJson(respuesta, BorradoParticipante.class);
                                                                                    Toast.makeText(getApplicationContext(), "Participante eliminado", Toast.LENGTH_LONG).show();


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
                                                                        eliminacion = true;
                                                                    }

                                                                    if (eliminacion == true) {
                                                                        AsyncHttpClient clienteBorradomia = new AsyncHttpClient();
                                                                        RequestParams rpBorradomia = new RequestParams();
                                                                        rpBorradomia.put("IdPartida", IdPartida);
                                                                        rpBorradomia.put("IdUsuario",IdUsuario);

                                                                        clienteBorradomia.get("https://proyectokiriki.000webhostapp.com/eliminarPartida.php?", rpBorradomia, new JsonHttpResponseHandler() {
                                                                            @Override
                                                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                if (statusCode == 200) {
                                                                                    String respuesta = response.toString();
                                                                                    Gson gson = new Gson();
                                                                                    BorradoPartidas correos = gson.fromJson(respuesta, BorradoPartidas.class);
                                                                                    int res = correos.getSuccess();



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


                                                                        //if (lista.size() == 1) {
                                                                        Intent Final = new Intent(getApplicationContext(), FinalActivity.class);
                                                                        Final.putExtra("NombreGanador", lista.get(0));
                                                                        creado = false;
                                                                        finish();
                                                                        startActivity(Final);

                                                                    } else {
                                                                        Log.d("KIRIKI", "NO ES ELIMINADO");
                                                                        valor = "0";
                                                                        valorReal = "0";
                                                                        AsyncHttpClient clienteFallos = new AsyncHttpClient();
                                                                        RequestParams rpFallos = new RequestParams();
                                                                        rpFallos.put("IdPartida", IdPartida);
                                                                        rpFallos.put("IdUsuario", IdUsuario);
                                                                        rpFallos.put("Fallos",listaFallosVariable.get(turno-1));

                                                                        clienteFallos.get(urlActualizarFallos, rpFallos, new JsonHttpResponseHandler() {
                                                                            @Override
                                                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                super.onSuccess(statusCode, headers, response);
                                                                                if (statusCode == 200) {
                                                                                    String respuesta = response.toString();
                                                                                    Gson gson = new Gson();
                                                                                    UpdateFallos updateFallos = gson.fromJson(respuesta, UpdateFallos.class);


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


                                                                        AsyncHttpClient clienteValores = new AsyncHttpClient();
                                                                        RequestParams rpValores = new RequestParams();
                                                                        rpValores.put("IdPartida", IdPartida);
                                                                        rpValores.put("IdUsuario", IdUsuario);
                                                                        rpValores.put("ValorRecibido", valor);
                                                                        rpValores.put("ValorRecibidoReal", valorReal);

                                                                        clienteValores.get(urlActualizarValores, rpValores, new JsonHttpResponseHandler() {
                                                                            @Override
                                                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                super.onSuccess(statusCode, headers, response);
                                                                                if (statusCode == 200) {
                                                                                    String respuesta = response.toString();
                                                                                    Gson gson = new Gson();
                                                                                    UpdateValores updateValores = gson.fromJson(respuesta, UpdateValores.class);
                                                                                    Log.d("KIRIKI valores", updateValores.getMessage());


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


                                                                        AsyncHttpClient clienteTurno = new AsyncHttpClient();
                                                                        RequestParams rpTurno = new RequestParams();
                                                                        rpTurno.put("IdPartida", IdPartida);
                                                                        rpTurno.put("Turno", turnoActual);
                                                                        

                                                                        clienteTurno.get("http://proyectokiriki.000webhostapp.com/cambiarTurno.php", rpTurno, new JsonHttpResponseHandler() {
                                                                            @Override
                                                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                super.onSuccess(statusCode, headers, response);
                                                                                if (statusCode == 200) {
                                                                                    String respuesta = response.toString();
                                                                                    Gson gson = new Gson();
                                                                                    CambioTurno cambio = gson.fromJson(respuesta, CambioTurno.class);
                                                                                    Intent Partida = new Intent(getApplicationContext(), SalaEsperaActivity.class);
                                                                                    Partida.putExtra("listaFallos", listaFallosVariable);
                                                                                    Partida.putExtra("IdPartida",IdPartida);
                                                                                    Partida.putExtra("IdUsuario",IdUsuario);
                                                                                    Partida.putExtra("correo",correo);

                                                                                    finish();
                                                                                    startActivity(Partida);

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
                                                                }


                                                            }).show();
                                                } else {
                                                    //PARA CUANDO EL VALOR NO SEA KIRIKI, COMPROBACIONES
                                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                    final View formElementsView = inflater.inflate(R.layout.pantalla_recibirtirada,
                                                            null, false);
                                                    new AlertDialog.Builder(PartidaOnlineActivity.this).setView(formElementsView)
                                                            .setTitle("El jugador anterior le envia " + valorRecibido)
                                                            .setPositiveButton("Creer", new DialogInterface.OnClickListener() {

                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    Toast.makeText(getApplicationContext(), "Te lo creiste, debes superar o igualar " + valorRecibido, Toast.LENGTH_LONG).show();

                                                                }

                                                            })
                                                            .setNegativeButton("No creer", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    if (valorRecibido.equalsIgnoreCase(valorRecibidoReal)) {
                                                                        int fallo = listaFallosVariable.get(turno - 1) + 1;
                                                                        listaFallosVariable.set(turno - 1, fallo);
                                                                        Toast.makeText(getApplicationContext(), "Punto para ti, estaba diciendo la verdad!", Toast.LENGTH_LONG).show();
                                                                        sp.play(sonidoError, 1, 1, 1, 0, 1);
                                                                        if (listaFallosVariable.get(turno - 1) == 3) {
                                                                            lista.remove(turno - 1);
                                                                            listaFallosVariable.remove(turno - 1);

                                                                            AsyncHttpClient clienteEliminacion = new AsyncHttpClient();
                                                                            RequestParams rpEliminacion = new RequestParams();
                                                                            rpEliminacion.put("IdPartida", IdPartida);
                                                                            rpEliminacion.put("IdUsuario", IdUsuario);

                                                                            clienteEliminacion.get(urlEliminacion, rpEliminacion, new JsonHttpResponseHandler() {
                                                                                @Override
                                                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                    super.onSuccess(statusCode, headers, response);
                                                                                    if (statusCode == 200) {
                                                                                        String respuesta = response.toString();
                                                                                        Gson gson = new Gson();
                                                                                        BorradoParticipante borrado = gson.fromJson(respuesta, BorradoParticipante.class);
                                                                                        Toast.makeText(getApplicationContext(), "Participante eliminado", Toast.LENGTH_LONG).show();


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
                                                                            eliminacion = true;
                                                                        }

                                                                        if (eliminacion == true) {
                                                                            AsyncHttpClient clienteBorradomia = new AsyncHttpClient();
                                                                            RequestParams rpBorradomia = new RequestParams();
                                                                            rpBorradomia.put("IdPartida", IdPartida);
                                                                            rpBorradomia.put("IdUsuario",IdUsuario);

                                                                            clienteBorradomia.get("https://proyectokiriki.000webhostapp.com/eliminarPartida.php?", rpBorradomia, new JsonHttpResponseHandler() {
                                                                                @Override
                                                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                    if (statusCode == 200) {
                                                                                        String respuesta = response.toString();
                                                                                        Gson gson = new Gson();
                                                                                        BorradoPartidas correos = gson.fromJson(respuesta, BorradoPartidas.class);
                                                                                        int res = correos.getSuccess();



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

                                                                            // if (lista.size() == 1) {
                                                                            Intent Final = new Intent(getApplicationContext(), FinalActivity.class);
                                                                            Final.putExtra("NombreGanador", lista.get(0));
                                                                            creado = false;

                                                                            finish();
                                                                            startActivity(Final);

                                                                        } else {
                                                                            valorReal = "";
                                                                            valor = "";
                                                                            valorRecibido = "";
                                                                            valorRecibidoReal = "";

                                                                            AsyncHttpClient clienteFallos = new AsyncHttpClient();
                                                                            RequestParams rpFallos = new RequestParams();
                                                                            rpFallos.put("IdPartida", IdPartida);
                                                                            rpFallos.put("IdUsuario", IdUsuario);
                                                                            rpFallos.put("Fallos",listaFallosVariable.get(turno-1));
                                                                            Log.d(" FALLO MIO", lista.get(turno-1));

                                                                            clienteFallos.get(urlActualizarFallos, rpFallos, new JsonHttpResponseHandler() {
                                                                                @Override
                                                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                    super.onSuccess(statusCode, headers, response);
                                                                                    if (statusCode == 200) {
                                                                                        String respuesta = response.toString();
                                                                                        Gson gson = new Gson();
                                                                                        UpdateFallos updateFallos = gson.fromJson(respuesta, UpdateFallos.class);
                                                                                        Log.d(" FALLO MIO", updateFallos.getMessage());


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

                                                                    } else {

                                                                        if (turnoActual.equalsIgnoreCase("1")) {
                                                                            Log.d("TURNO1", String.valueOf(listaFallosVariable.get(1)));
                                                                            int fallo = listaFallosVariable.get(1) + 1;
                                                                            Log.d("TURNO1", String.valueOf(listaFallosVariable.get(1)));
                                                                            listaFallosVariable.set(1, fallo);
                                                                            Log.d("TURNO1", String.valueOf(listaFallosVariable.get(1)));
                                                                            Toast.makeText(getApplicationContext(), "Te intentó engañar pero lo cogiste, punto para el!", Toast.LENGTH_LONG).show();
                                                                            sp.play(sonidoAcierto, 1, 1, 1, 0, 1);
                                                                            if (listaFallosVariable.get(1) == 3) {
                                                                                lista.remove(1);
                                                                                listaFallosVariable.remove(1);
                                                                                eliminacion = true;
                                                                                AsyncHttpClient clienteOtroEliminacion = new AsyncHttpClient();
                                                                                RequestParams rpOtroEliminacion = new RequestParams();
                                                                                rpOtroEliminacion.put("IdPartida", IdPartida);
                                                                                rpOtroEliminacion.put("IdUsuario", IdUsuario);

                                                                                clienteOtroEliminacion.get(urlOtroEliminacion, rpOtroEliminacion, new JsonHttpResponseHandler() {
                                                                                    @Override
                                                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                        super.onSuccess(statusCode, headers, response);
                                                                                        if (statusCode == 200) {
                                                                                            String respuesta = response.toString();
                                                                                            Gson gson = new Gson();
                                                                                            BorradoOtroParticipante borrado = gson.fromJson(respuesta, BorradoOtroParticipante.class);
                                                                                            Toast.makeText(getApplicationContext(), "Participante eliminado", Toast.LENGTH_LONG).show();


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

                                                                                AsyncHttpClient clienteTurno = new AsyncHttpClient();
                                                                                RequestParams rpTurno = new RequestParams();
                                                                                rpTurno.put("IdPartida", IdPartida);
                                                                                rpTurno.put("Turno", "0");

                                                                                clienteTurno.get("http://proyectokiriki.000webhostapp.com/cambiarTurno.php", rpTurno, new JsonHttpResponseHandler() {
                                                                                    @Override
                                                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                        super.onSuccess(statusCode, headers, response);
                                                                                        if (statusCode == 200) {
                                                                                            String respuesta = response.toString();
                                                                                            Gson gson = new Gson();
                                                                                            CambioTurno cambio = gson.fromJson(respuesta, CambioTurno.class);
                                                                                            Intent Partida = new Intent(getApplicationContext(), SalaEsperaActivity.class);
                                                                                            Partida.putExtra("listaFallos", listaFallosVariable);
                                                                                            Partida.putExtra("IdPartida", IdPartida);
                                                                                            Partida.putExtra("IdUsuario", IdUsuario);
                                                                                            Partida.putExtra("correo", correo);
                                                                                            Log.d("KIRIKI DADO", "onSuccess: ");
                                                                                            finish();
                                                                                            startActivity(Partida);


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
                                                                                if (eliminacion == true) {
                                                                                    AsyncHttpClient clienteBorradomia = new AsyncHttpClient();
                                                                                    RequestParams rpBorradomia = new RequestParams();
                                                                                    rpBorradomia.put("IdPartida", IdPartida);
                                                                                    rpBorradomia.put("IdUsuario",IdUsuario);

                                                                                    clienteBorradomia.get("https://proyectokiriki.000webhostapp.com/eliminarPartida.php?", rpBorradomia, new JsonHttpResponseHandler() {
                                                                                        @Override
                                                                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                            if (statusCode == 200) {
                                                                                                String respuesta = response.toString();
                                                                                                Gson gson = new Gson();
                                                                                                BorradoPartidas correos = gson.fromJson(respuesta, BorradoPartidas.class);
                                                                                                int res = correos.getSuccess();



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

                                                                                    Intent Final = new Intent(getApplicationContext(), FinalActivity.class);
                                                                                    Final.putExtra("NombreGanador", lista.get(0));

                                                                                    Final.putExtra("Id", Id);
                                                                                    creado = false;

                                                                                    finish();
                                                                                    startActivity(Final);
                                                                                }


                                                                            }else{
                                                                                AsyncHttpClient clienteFallos = new AsyncHttpClient();
                                                                                RequestParams rpFallos = new RequestParams();
                                                                                rpFallos.put("IdPartida", IdPartida);
                                                                                rpFallos.put("IdUsuario", IdUsuario);

                                                                                rpFallos.put("Fallos",listaFallosVariable.get(1));

                                                                                clienteFallos.get(urlActualizarOtroFallos, rpFallos, new JsonHttpResponseHandler() {
                                                                                    @Override
                                                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                        super.onSuccess(statusCode, headers, response);
                                                                                        if (statusCode == 200) {
                                                                                            String respuesta = response.toString();
                                                                                            Gson gson = new Gson();
                                                                                            UpdateOtroFallos updateOtroFallos = gson.fromJson(respuesta, UpdateOtroFallos.class);
                                                                                            Log.d("KIRIKI FALLOS  otro", updateOtroFallos.getMessage());


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
                                                                        } else {
                                                                            int fallo = listaFallosVariable.get(0) + 1;
                                                                            listaFallosVariable.set(0, fallo);
                                                                            Toast.makeText(getApplicationContext(), "Te intentó engañar pero lo cogiste, punto para el!", Toast.LENGTH_LONG).show();
                                                                            sp.play(sonidoAcierto, 1, 1, 1, 0, 1);
                                                                            if (listaFallosVariable.get(0) == 3) {
                                                                                lista.remove(0);
                                                                                listaFallosVariable.remove(0);
                                                                                eliminacion = true;
                                                                                AsyncHttpClient clienteOtroEliminacion = new AsyncHttpClient();
                                                                                RequestParams rpOtroEliminacion = new RequestParams();
                                                                                rpOtroEliminacion.put("IdPartida", IdPartida);
                                                                                rpOtroEliminacion.put("IdUsuario", IdUsuario);

                                                                                clienteOtroEliminacion.get(urlOtroEliminacion, rpOtroEliminacion, new JsonHttpResponseHandler() {
                                                                                    @Override
                                                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                        super.onSuccess(statusCode, headers, response);
                                                                                        if (statusCode == 200) {
                                                                                            String respuesta = response.toString();
                                                                                            Gson gson = new Gson();
                                                                                            BorradoOtroParticipante borrado = gson.fromJson(respuesta, BorradoOtroParticipante.class);
                                                                                            Toast.makeText(getApplicationContext(), "Participante eliminado", Toast.LENGTH_LONG).show();


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
                                                                                if (eliminacion == true) {
                                                                                    AsyncHttpClient clienteBorradomia = new AsyncHttpClient();
                                                                                    RequestParams rpBorradomia = new RequestParams();
                                                                                    rpBorradomia.put("IdPartida", IdPartida);
                                                                                    rpBorradomia.put("IdUsuario",IdUsuario);

                                                                                    clienteBorradomia.get("https://proyectokiriki.000webhostapp.com/eliminarPartida.php?", rpBorradomia, new JsonHttpResponseHandler() {
                                                                                        @Override
                                                                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                            if (statusCode == 200) {
                                                                                                String respuesta = response.toString();
                                                                                                Gson gson = new Gson();
                                                                                                BorradoPartidas correos = gson.fromJson(respuesta, BorradoPartidas.class);
                                                                                                int res = correos.getSuccess();



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

                                                                                    Intent Final = new Intent(getApplicationContext(), FinalActivity.class);
                                                                                    Final.putExtra("NombreGanador", lista.get(0));

                                                                                    Final.putExtra("Id", Id);
                                                                                    creado = false;

                                                                                    finish();
                                                                                    startActivity(Final);
                                                                                }
                                                                            }else{
                                                                                AsyncHttpClient clienteFallos = new AsyncHttpClient();
                                                                                RequestParams rpFallos = new RequestParams();
                                                                                rpFallos.put("IdPartida", IdPartida);
                                                                                rpFallos.put("IdUsuario", IdUsuario);
                                                                                rpFallos.put("Fallos",listaFallosVariable.get(0));

                                                                                clienteFallos.get(urlActualizarOtroFallos, rpFallos, new JsonHttpResponseHandler() {
                                                                                    @Override
                                                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                                                        super.onSuccess(statusCode, headers, response);
                                                                                        if (statusCode == 200) {
                                                                                            String respuesta = response.toString();
                                                                                            Gson gson = new Gson();
                                                                                            UpdateFallos updateFallos = gson.fromJson(respuesta, UpdateFallos.class);
                                                                                            Log.d("KIRIKI FALLOS otro", updateFallos.getMessage());


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
                                                                        }

                                                                      /*  if (eliminacion == true) {
                                                                            Intent Final = new Intent(getApplicationContext(), FinalActivity.class);
                                                                            Final.putExtra("NombreGanador", lista.get(0));

                                                                            Final.putExtra("Id", Id);
                                                                            creado = false;

                                                                            finish();
                                                                            startActivity(Final);

                                                                        } else {
                                                                            valorReal = "";
                                                                            valor = "";
                                                                            valorRecibido = "";
                                                                            valorRecibidoReal = "";
                                                                        }*/

                                                                    }
                                                                }
                                                            }).show();
                                                }
                                            }
                                            layout.addView(contenedor);
                                       /* }
                                    }, 2500);


                                } else {
                                    timer.purge();
                                    timer.cancel();
                                    task.cancel();
                                    /*if (dialogo == false) {
                                        progressDialog.setIcon(R.mipmap.ic_launcher);
                                        progressDialog.setMessage("Esperando que el otro jugador tire...");
                                        progressDialog.show();
                                        dialogo = true;

                                    }
                                }


                            }

                        }, 2000); */

                   /* } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                    //}
                    //  });

                   /* if (Nrepetir == true && turnoActual.equalsIgnoreCase(turnoJugador)) {

                    } else {   // IF ELSE COMPARANDO EL BOOLEAN
                        Log.d("ENTRA", "onCreate: ");
                        timer.purge();
                        timer.cancel();
                        task.cancel();
                    }

                } // FIN DEL RUN
            };    // FIN DEL TIMER TASK

*/


/*else{
            timer.cancel();
            timer.purge();
            task.cancel();
        }*/


        bAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Ayuda = new Intent(getApplicationContext(), AyudaActivity.class);


                startActivity(Ayuda);

            }
        });


        bTirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tirada == false) {
                    sp.play(sonidoDado, 1, 1, 1, 0, 1);


                    animacion.start();
                    animacion2.start();

                    valorDado = (int) Math.floor(Math.random() * 6 + 1);

                    valorDado2 = (int) Math.floor(Math.random() * 6 + 1);


                    if (valorDado == valorDado2) {
                        valorReal = "Pareja";
                    } else {
                        int sumaDados = valorDado + valorDado2;
                        switch (sumaDados) {
                            case 3:
                                valorReal = "Kiriki";
                                valor = valorReal;
                                Log.d("KIRIKI DADO FUERA", "onSuccess: ");
                                Toast.makeText(getApplicationContext(), "Kiriki enviado.", Toast.LENGTH_LONG).show();

                                AsyncHttpClient clienteValores = new AsyncHttpClient();
                                RequestParams rpValores = new RequestParams();
                                rpValores.put("IdPartida", IdPartida);
                                rpValores.put("IdUsuario", IdUsuario);
                                rpValores.put("ValorRecibido", valor);
                                rpValores.put("ValorRecibidoReal", valorReal);

                                clienteValores.get(urlActualizarValores, rpValores, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        if (statusCode == 200) {
                                            String respuesta = response.toString();
                                            Gson gson = new Gson();
                                            UpdateValores updateValores = gson.fromJson(respuesta, UpdateValores.class);
                                            Log.d("KIRIKI valores", updateValores.getMessage());


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
                                AsyncHttpClient clienteTurno = new AsyncHttpClient();
                                RequestParams rpTurno = new RequestParams();
                                rpTurno.put("IdPartida", IdPartida);
                                rpTurno.put("Turno", turno);

                                clienteTurno.get("http://proyectokiriki.000webhostapp.com/cambiarTurno.php", rpTurno, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        if (statusCode == 200) {
                                            String respuesta = response.toString();
                                            Gson gson = new Gson();
                                            CambioTurno cambio = gson.fromJson(respuesta, CambioTurno.class);
                                            Intent Partida = new Intent(getApplicationContext(), SalaEsperaActivity.class);
                                            Partida.putExtra("listaFallos", listaFallosVariable);
                                            Partida.putExtra("IdPartida", IdPartida);
                                            Partida.putExtra("IdUsuario", IdUsuario);
                                            Partida.putExtra("correo", correo);
                                            Log.d("KIRIKI DADO", "onSuccess: ");
                                            finish();
                                            startActivity(Partida);


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

                                break;
                            case 4:
                                valorReal = "4";
                                break;
                            case 5:
                                valorReal = "5";
                                break;
                            case 6:
                                valorReal = "6";
                                break;
                            case 7:
                                valorReal = "7";
                                break;
                            case 8:
                                valorReal = "8";
                                break;
                            case 9:
                                valorReal = "9";
                                break;
                            case 10:
                                valorReal = "10";
                                break;
                            case 11:
                                valorReal = "Ladrillo";
                                break;
                        }
                    }


                }
                Log.d("KIRIKI LUEGO", "onSuccess: ");

                if(!valorReal.equalsIgnoreCase("KIRIKI")){
                if (!((Activity) PartidaOnlineActivity.this).isFinishing()) {
                    Log.d("KIRIKI NO DEBERIA", "onSuccess: ");

                    if (tirada == true) {
                        espera = 0;
                    }
                    tirada = true;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            animacion.stop();
                            switch (valorDado) {
                                case 1:
                                    dado.setImageResource(R.drawable.dice1);
                                    break;
                                case 2:
                                    dado.setImageResource(R.drawable.dice2);
                                    break;
                                case 3:
                                    dado.setImageResource(R.drawable.dice3);
                                    break;
                                case 4:
                                    dado.setImageResource(R.drawable.dice4);
                                    break;
                                case 5:
                                    dado.setImageResource(R.drawable.dice5);
                                    break;
                                case 6:
                                    dado.setImageResource(R.drawable.dice6);
                                    break;
                            }
                            animacion2.stop();
                            switch (valorDado2) {
                                case 1:
                                    dado2.setImageResource(R.drawable.dice1);
                                    break;
                                case 2:
                                    dado2.setImageResource(R.drawable.dice2);
                                    break;
                                case 3:
                                    dado2.setImageResource(R.drawable.dice3);
                                    break;
                                case 4:
                                    dado2.setImageResource(R.drawable.dice4);
                                    break;
                                case 5:
                                    dado2.setImageResource(R.drawable.dice5);
                                    break;
                                case 6:
                                    dado2.setImageResource(R.drawable.dice6);
                                    break;
                            }
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View formElementsView = inflater.inflate(R.layout.pantalla_enviartirada,
                                    null, false);

                            spinnerValores = (Spinner) formElementsView.findViewById(R.id.eleccionTirada);

                            ArrayList<Valor> listaValores = new ArrayList<Valor>();

                            listaValores.add(new Valor(2, "Ladrillo"));
                            listaValores.add(new Valor(3, "Pareja"));
                            listaValores.add(new Valor(4, "10"));
                            listaValores.add(new Valor(5, "9"));
                            listaValores.add(new Valor(6, "8"));
                            listaValores.add(new Valor(7, "7"));
                            listaValores.add(new Valor(8, "6"));
                            listaValores.add(new Valor(9, "5"));
                            listaValores.add(new Valor(10, "4"));

                            if (!valorRecibido.equalsIgnoreCase("")) {

                                Iterator iterator = listaValores.iterator();
                                while (iterator.hasNext()) {


                                    String value = iterator.next().toString();
                                    if (value.equalsIgnoreCase("Ladrillo") || value.equalsIgnoreCase("Pareja")) {
                                        value = "11";
                                    }

                                    if (valorRecibido.equalsIgnoreCase("Ladrillo") || valorRecibido.equalsIgnoreCase("Pareja")) {
                                        int valueInt = Integer.parseInt(value);
                                        if (valueInt < 11) {
                                            iterator.remove();
                                        }
                                    } else {
                                        int valueInt = Integer.parseInt(value);
                                        if (valueInt < Integer.parseInt(valorRecibido)) {
                                            iterator.remove();
                                        }
                                    }
                                }

                            }

                            bTirar.setText("Elegir resultado");
                            ArrayAdapter<Valor> adaptador = new ArrayAdapter<Valor>(PartidaOnlineActivity.this, android.R.layout.simple_spinner_item, listaValores);
                            adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerValores.setAdapter(adaptador);


                            new AlertDialog.Builder(PartidaOnlineActivity.this).setView(formElementsView)
                                    .setTitle("Indique la cantidad que quiere enviar:")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int id) {


                                            valor = spinnerValores.getSelectedItem().toString();

                                            AsyncHttpClient clienteValores = new AsyncHttpClient();
                                            RequestParams rpValores = new RequestParams();
                                            rpValores.put("IdPartida", IdPartida);
                                            rpValores.put("IdUsuario", IdUsuario);
                                            rpValores.put("ValorRecibido", valor);
                                            rpValores.put("ValorRecibidoReal", valorReal);

                                            Log.d("VALORES", IdPartida);
                                            Log.d("VALORES", IdUsuario);
                                            Log.d("VALORES", valor);
                                            Log.d("VALORES", valorReal);

                                            clienteValores.get(urlActualizarValores, rpValores, new JsonHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                    super.onSuccess(statusCode, headers, response);
                                                    if (statusCode == 200) {
                                                        String respuesta = response.toString();
                                                        Gson gson = new Gson();
                                                        UpdateValores updateValores = gson.fromJson(respuesta, UpdateValores.class);
                                                        Log.d("VALORES", updateValores.getMessage());


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

                                            AsyncHttpClient clienteTurno = new AsyncHttpClient();
                                            RequestParams rpTurno = new RequestParams();
                                            rpTurno.put("IdPartida", IdPartida);
                                            rpTurno.put("Turno", turnoActual);

                                            //Log.d("IdPartida", "onClick: ");

                                            clienteTurno.get("http://proyectokiriki.000webhostapp.com/cambiarTurno.php", rpTurno, new JsonHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                    super.onSuccess(statusCode, headers, response);
                                                    if (statusCode == 200) {
                                                        String respuesta = response.toString();
                                                        Gson gson = new Gson();
                                                        CambioTurno cambio = gson.fromJson(respuesta, CambioTurno.class);
                                                        Intent Partida = new Intent(getApplicationContext(), SalaEsperaActivity.class);
                                                        Partida.putExtra("listaFallos", listaFallosVariable);
                                                        Partida.putExtra("IdPartida", IdPartida);
                                                        Partida.putExtra("IdUsuario", IdUsuario);
                                                        Partida.putExtra("correo", correo);
                                                        Log.d("VALORES", cambio.getMessage());

                                                        finish();
                                                        startActivity(Partida);


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

                                    })
                                    .setNegativeButton("Ayuda", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent Ayuda = new Intent(getApplicationContext(), AyudaActivity.class);
                                            startActivity(Ayuda);
                                        }
                                    }).show();
                        }
                    }, espera);
                }
            }


            }
        });

        bEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Estado = new Intent(getApplicationContext(), EstadoActivity.class);
                Estado.putExtra("miLista", lista);
                Estado.putExtra("listaFallos", listaFallosVariable);
                startActivity(Estado);
            }
        });

    }


    private Bitmap getBitmapFromAssets(PartidaOnlineActivity partidaOnlineActivity, String filepath) {
        AssetManager assetManager = partidaOnlineActivity.getAssets();
        InputStream istr = null;
        Bitmap bitmap = null;

        try {
            istr = assetManager.open(filepath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException ioe) {
            // manage exception
        } finally {
            if (istr != null) {
                try {
                    istr.close();
                } catch (IOException e) {
                }
            }
        }

        return bitmap;
    }
}
