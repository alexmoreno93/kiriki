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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class PartidaActivity extends AppCompatActivity {

    private Button bAyuda;
    private Button bTirar;
    private Button bEstado;
    private ImageView dado;
    private ImageView dado2;
    private SoundPool sp;
    private int sonidoDado = 0, sonidoAcierto = 0, sonidoError = 0;
    private TextView nombreJugador;
    private int turno, turnosTotales = 0, participantes;
    private String valor, valorRecibido, valorReal, valorRecibidoReal, correo;
    private Spinner spinnerValores;
    private ArrayList<Integer> listaFallosVariable;
    private boolean eliminacion,tirada = false,login;
    private int Id, espera = 3000;
    private AnimationDrawable animacion;
    private AnimationDrawable animacion2;
    private int valorDado = 0;
    private int valorDado2 = 0;

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

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sonidoDado = sp.load(getApplicationContext(), R.raw.dado, 0);
        sonidoAcierto = sp.load(getApplicationContext(), R.raw.acierto, 0);
        sonidoError = sp.load(getApplicationContext(), R.raw.error, 0);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearPartida);
        dado.setImageDrawable(animacion);
        dado2.setImageDrawable(animacion2);

        final ArrayList<String> lista = (ArrayList<String>) getIntent().getSerializableExtra("miLista");
        final ArrayList<Integer> listaFallos = (ArrayList<Integer>) getIntent().getSerializableExtra("listaFallos");
        listaFallosVariable = listaFallos;
        turno = getIntent().getIntExtra("turno", 0);
        nombreJugador.setText(lista.get(turno));
        participantes = getIntent().getIntExtra("Participantes", 0);
        valorRecibido = getIntent().getStringExtra("valor");
        valorRecibidoReal = getIntent().getStringExtra("valorReal");
        correo = getIntent().getStringExtra("correo");
        Id = getIntent().getIntExtra("Id", Id);
        login = getIntent().getBooleanExtra("Login",false);
        Log.d("Login", String.valueOf(login));

        eliminacion = false;
        LinearLayout contenedor = new LinearLayout(this);
        contenedor.setOrientation(LinearLayout.HORIZONTAL);
        contenedor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //COMPROBACIÓN DE QUE SEA LA PRIMERA VEZ O LUEGO DE UNA ELIMINACIÓN
        if (!valorRecibido.equalsIgnoreCase("")) {
            //SI NO LO ES SE COMPRUEBA QUE NO SEA UN KIRIKI EL VALOR ENVIADO
            if (valorRecibido.equalsIgnoreCase("Kiriki")) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.pantalla_recibirtirada,
                        null, false);
                new AlertDialog.Builder(PartidaActivity.this).setView(formElementsView)
                        .setTitle("Recibiste kiriki, se te sumara un punto y se te pasara de turno. ")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                int fallo = listaFallosVariable.get(turno) + 1;
                                listaFallosVariable.set(turno, fallo);
                                if (listaFallosVariable.get(turno) == 3) {
                                    lista.remove(turno);
                                    listaFallosVariable.remove(turno);
                                    eliminacion = true;
                                }

                                if (eliminacion == true) {
                                    if (lista.size() == 1) {
                                        Intent Final = new Intent(getApplicationContext(), FinalActivity.class);
                                        Final.putExtra("NombreGanador", lista.get(0));
                                        Final.putExtra("Turnos", turnosTotales);
                                        Final.putExtra("Fallos", listaFallosVariable.get(0));
                                        Final.putExtra("Participantes", participantes);
                                        Final.putExtra("Id", Id);
                                        Final.putExtra("Login",login);
                                        Final.putExtra("correo",correo);
                                        finish();
                                        startActivity(Final);
                                    } else {
                                        Intent Partida = new Intent(getApplicationContext(), PartidaActivity.class);
                                        Partida.putExtra("miLista", lista);
                                        turnosTotales++;
                                        Partida.putExtra("Id", Id);
                                        Partida.putExtra("Participantes", participantes);
                                        Partida.putExtra("Turnos", turnosTotales);
                                        Partida.putExtra("valorReal", "");
                                        Partida.putExtra("valor", "");
                                        Partida.putExtra("turno", turno);
                                        Partida.putExtra("listaFallos", listaFallosVariable);
                                        Partida.putExtra("Login",login);
                                        Partida.putExtra("correo",correo);

                                        dialog.cancel();
                                        finish();
                                        startActivity(Partida);
                                    }
                                } else {
                                    valor = "";
                                    valorReal = "";
                                    Intent Partida = new Intent(getApplicationContext(), PartidaActivity.class);
                                    Partida.putExtra("miLista", lista);

                                    if (turno + 1 == lista.size()) {
                                        turno = 0;
                                    } else {
                                        turno++;
                                    }
                                    turnosTotales++;
                                    Partida.putExtra("Id", Id);
                                    Partida.putExtra("Participantes", participantes);
                                    Partida.putExtra("Turnos", turnosTotales);
                                    Partida.putExtra("valorReal", valorReal);
                                    Partida.putExtra("valor", valor);
                                    Partida.putExtra("turno", turno);
                                    Partida.putExtra("listaFallos", listaFallosVariable);
                                    Partida.putExtra("Login",login);
                                    Partida.putExtra("correo",correo);

                                    dialog.cancel();
                                    finish();
                                    startActivity(Partida);
                                }
                            }


                        }).show();
            } else {
            //PARA CUANDO EL VALOR NO SEA KIRIKI, COMPROBACIONES
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.pantalla_recibirtirada,
                        null, false);
                new AlertDialog.Builder(PartidaActivity.this).setView(formElementsView)
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
                                    int fallo = listaFallosVariable.get(turno) + 1;
                                    listaFallosVariable.set(turno, fallo);
                                    Toast.makeText(getApplicationContext(), "Punto para ti, estaba diciendo la verdad!", Toast.LENGTH_LONG).show();
                                    sp.play(sonidoError, 1, 1, 1, 0, 1);
                                    if (listaFallosVariable.get(turno) == 3) {
                                        lista.remove(turno);
                                        listaFallosVariable.remove(turno);
                                        eliminacion = true;
                                    }

                                    if (eliminacion == true) {
                                        if (lista.size() == 1) {
                                            Intent Final = new Intent(getApplicationContext(), FinalActivity.class);
                                            Final.putExtra("NombreGanador", lista.get(0));
                                            Final.putExtra("Turnos", turnosTotales);
                                            Final.putExtra("Fallos", listaFallosVariable.get(0));
                                            Final.putExtra("Participantes", participantes);
                                            Final.putExtra("Id", Id);
                                            Final.putExtra("Login",login);
                                            Final.putExtra("correo",correo);
                                            finish();
                                            startActivity(Final);
                                        } else {
                                            Intent Partida = new Intent(getApplicationContext(), PartidaActivity.class);
                                            Partida.putExtra("miLista", lista);
                                            turnosTotales++;
                                            Partida.putExtra("Id", Id);
                                            Partida.putExtra("Participantes", participantes);
                                            Partida.putExtra("Turnos", turnosTotales);
                                            Partida.putExtra("valorReal", "");
                                            Partida.putExtra("Login",login);
                                            Partida.putExtra("valor", "");
                                            Partida.putExtra("turno", turno);
                                            Partida.putExtra("listaFallos", listaFallosVariable);
                                            Partida.putExtra("correo",correo);
                                            finish();
                                            startActivity(Partida);

                                        }
                                    } else {
                                        valorReal = "";
                                        valor = "";
                                        valorRecibido = "";
                                        valorRecibidoReal = "";
                                    }

                                } else {
                                    if (turno == 0) {
                                        int fallo = listaFallosVariable.get(listaFallosVariable.size() - 1) + 1;
                                        listaFallosVariable.set((listaFallosVariable.size() - 1), fallo);
                                        Toast.makeText(getApplicationContext(), "Te intentó engañar pero lo cogiste, punto para el!", Toast.LENGTH_LONG).show();
                                        sp.play(sonidoAcierto, 1, 1, 1, 0, 1);
                                        if (listaFallosVariable.get(listaFallosVariable.size() - 1) == 3) {
                                            lista.remove(listaFallosVariable.size() - 1);
                                            listaFallosVariable.remove(listaFallosVariable.size() - 1);
                                            eliminacion = true;
                                        }

                                        if (eliminacion == true) {
                                            if (lista.size() == 1) {
                                                Intent Final = new Intent(getApplicationContext(), FinalActivity.class);
                                                Final.putExtra("NombreGanador", lista.get(0));
                                                Final.putExtra("Turnos", turnosTotales);
                                                Final.putExtra("Fallos", listaFallosVariable.get(0));
                                                Final.putExtra("Participantes", participantes);
                                                Final.putExtra("Id", Id);
                                                Final.putExtra("Login",login);
                                                Final.putExtra("correo",correo);
                                                finish();
                                                startActivity(Final);
                                            } else {
                                                Intent Partida = new Intent(getApplicationContext(), PartidaActivity.class);
                                                Partida.putExtra("miLista", lista);
                                                turnosTotales++;
                                                Partida.putExtra("Id", Id);
                                                Partida.putExtra("Participantes", participantes);
                                                Partida.putExtra("Turnos", turnosTotales);
                                                Partida.putExtra("valorReal", "");
                                                Partida.putExtra("valor", "");
                                                Partida.putExtra("turno", turno);
                                                Partida.putExtra("listaFallos", listaFallosVariable);
                                                Partida.putExtra("Login",login);
                                                Partida.putExtra("correo",correo);
                                                finish();
                                                startActivity(Partida);
                                            }
                                        } else {
                                            valorReal = "";
                                            valor = "";
                                            valorRecibido = "";
                                            valorRecibidoReal = "";
                                        }
                                    } else {
                                        int fallo = listaFallosVariable.get(turno - 1) + 1;
                                        listaFallosVariable.set(turno - 1, fallo);
                                        Toast.makeText(getApplicationContext(), "Te intentó engañar pero lo cogiste, punto para el!", Toast.LENGTH_LONG).show();
                                        sp.play(sonidoAcierto, 1, 1, 1, 0, 1);
                                        if (listaFallosVariable.get(turno - 1) == 3) {
                                            lista.remove(turno - 1);
                                            listaFallosVariable.remove(turno - 1);
                                            eliminacion = true;
                                        }

                                        if (eliminacion == true) {
                                            if (lista.size() == 1) {
                                                Intent Final = new Intent(getApplicationContext(), FinalActivity.class);
                                                Final.putExtra("NombreGanador", lista.get(0));
                                                Final.putExtra("Turnos", turnosTotales);
                                                Final.putExtra("Fallos", listaFallosVariable.get(0));
                                                Final.putExtra("Participantes", participantes);
                                                Final.putExtra("Id", Id);
                                                Final.putExtra("Login",login);
                                                Final.putExtra("correo",correo);
                                                finish();
                                                startActivity(Final);
                                            } else {
                                                Intent Partida = new Intent(getApplicationContext(), PartidaActivity.class);
                                                Partida.putExtra("miLista", lista);
                                                turnosTotales++;
                                                Partida.putExtra("Id", Id);
                                                Partida.putExtra("Participantes", participantes);
                                                Partida.putExtra("Turnos", turnosTotales);
                                                Partida.putExtra("valorReal", "");
                                                Partida.putExtra("valor", "");
                                                Partida.putExtra("turno", turno);
                                                Partida.putExtra("listaFallos", listaFallosVariable);
                                                Partida.putExtra("Login",login);
                                                Partida.putExtra("correo",correo);
                                                finish();
                                                startActivity(Partida);
                                            }
                                        } else {
                                            valorReal = "";
                                            valor = "";
                                            valorRecibido = "";
                                            valorRecibidoReal = "";
                                        }
                                    }
                                }
                            }
                        }).show();
            }
        }


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
                                Intent Partida = new Intent(getApplicationContext(), PartidaActivity.class);
                                Partida.putExtra("miLista", lista);

                                if (turno + 1 == lista.size()) {
                                    turno = 0;
                                } else {
                                    turno++;
                                }
                                turnosTotales++;
                                Partida.putExtra("Id", Id);
                                Partida.putExtra("Participantes", participantes);
                                Partida.putExtra("Turnos", turnosTotales);
                                Partida.putExtra("valorReal", valorReal);
                                Partida.putExtra("valor", valor);
                                Partida.putExtra("turno", turno);
                                Partida.putExtra("listaFallos", listaFallosVariable);
                                Partida.putExtra("Login",login);
                                Partida.putExtra("correo",correo);
                                finish();
                                startActivity(Partida);
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

                if (!((Activity) PartidaActivity.this).isFinishing()) {

                    if (tirada == true){
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

                            int contBorrar = 0;
                            Log.d("VALOR RECIBIDO", valorRecibido);
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
                            ArrayAdapter<Valor> adaptador = new ArrayAdapter<Valor>(PartidaActivity.this, android.R.layout.simple_spinner_item, listaValores);
                            adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerValores.setAdapter(adaptador);


                            new AlertDialog.Builder(PartidaActivity.this).setView(formElementsView)
                                    .setTitle("Indique la cantidad que quiere enviar:")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int id) {


                                            valor = spinnerValores.getSelectedItem().toString();
                                            Intent Partida = new Intent(getApplicationContext(), PartidaActivity.class);
                                            Partida.putExtra("miLista", lista);

                                            if (turno + 1 == lista.size()) {
                                                turno = 0;
                                            } else {
                                                turno++;
                                            }
                                            turnosTotales++;
                                            Partida.putExtra("Id", Id);
                                            Partida.putExtra("Participantes", participantes);
                                            Partida.putExtra("Turnos", turnosTotales);
                                            Partida.putExtra("valorReal", valorReal);
                                            Partida.putExtra("valor", valor);
                                            Partida.putExtra("turno", turno);
                                            Partida.putExtra("listaFallos", listaFallosVariable);
                                            Partida.putExtra("Login",login);
                                            Partida.putExtra("correo",correo);
                                            dialog.cancel();
                                            finish();
                                            startActivity(Partida);
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


        layout.addView(contenedor);
    }


    private Bitmap getBitmapFromAssets(PartidaActivity partidaActivity, String filepath) {
        AssetManager assetManager = partidaActivity.getAssets();
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
