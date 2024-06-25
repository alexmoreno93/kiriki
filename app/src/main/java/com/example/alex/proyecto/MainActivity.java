package com.example.alex.proyecto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    private Button bJugar;
    private Button bAyuda;
    private Button bRegistro;
    private Button bLogin;
    private Button bRanking;
    private Button bLogout;
    private Button bAdministracion;
    private SQLiteDatabase bd;
    private boolean iniciada , login;
    private int Id;
    private String Correo;
    static final String BBDD = "Usuarios";
    public static boolean Nrepetir = true;
    public static boolean noRepetir = false;
    public static boolean creado = false;

    private Dialog dialogoOnline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);


            iniciada = false;
            //Crear o abrir BD
            bd = openOrCreateDatabase(BBDD,MODE_PRIVATE,null);
            iniciada = getIntent().getBooleanExtra("Iniciada",false);
        if(getIntent().getBooleanExtra("Iniciada",false)!=true){
            //Crear tabla usuario
            bd.execSQL("DROP TABLE IF EXISTS Usuario");

            String sql = "CREATE TABLE IF NOT EXISTS Usuario (\n" +
                    "IdUsuario INTEGER NOT NULL CONSTRAINT PK_USUARIO PRIMARY KEY,\n" +
                    "Correo VARCHAR(50) NOT NULL,\n" +
                    "Password VARCHAR(300) NOT NULL, \n" +
                    "Administrador INTEGER NOT NULL);";
            bd.execSQL(sql);




            //bd.execSQL("DELETE FROM Usuario");

        }




        bJugar = (Button) findViewById(R.id.botonJugar);
        bAyuda = (Button) findViewById(R.id.botonAyuda);
        bRegistro = (Button) findViewById(R.id.botonParaRegistro);
        bLogin = (Button) findViewById(R.id.botonParaLogin);
        bRanking = (Button) findViewById(R.id.botonClas);
        bLogout = (Button) findViewById(R.id.botonParaLogout);
        bAdministracion = (Button) findViewById(R.id.botonParaAdministracion);
        Id = getIntent().getIntExtra("Id",0);
        Correo = getIntent().getStringExtra("Correo");
        login = getIntent().getBooleanExtra("Login",false);
        Log.d("Login", String.valueOf(login));
        Cursor c = bd.rawQuery("SELECT * FROM Usuario", null);
        if(c.moveToFirst()==false){

            bLogin.setVisibility(View.VISIBLE);
            bLogout.setVisibility(View.GONE);

        }else{
            bLogin.setVisibility(View.GONE);
            bLogout.setVisibility(View.VISIBLE);
            bRegistro.setVisibility(View.GONE);
            if(c.getInt(3)==1){
                bAdministracion.setVisibility(View.VISIBLE);
            }else{
                bAdministracion.setVisibility(View.GONE);
            }
        }





        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Logout = new Intent(getApplicationContext(), LogoutActivity.class);
                finish();
                startActivity(Logout);
            }
        });

        bRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Registro = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(Registro);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(Login);
            }
        });

        bJugar.setOnClickListener(new View.OnClickListener() {



            public void onClick(View alertView) {

                if (bLogout.getVisibility() == View.VISIBLE) {
                   /* Intent partida = new Intent(MainActivity.this, CrearPartidaActivity.class);
                    partida.putExtra("Correo",Correo);
                    startActivity(partida);*/
                    dialogoOnline = new Dialog(MainActivity.this);
                    dialogoOnline.setContentView(R.layout.dialogo_jugar);



                    ((Button) dialogoOnline.findViewById(R.id.BCrearPartida)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogoOnline.dismiss();
                            Intent partida = new Intent(MainActivity.this, CrearPartidaActivity.class);
                            partida.putExtra("Correo",Correo);
                            startActivity(partida);

                        }
                    });

                    ((Button) dialogoOnline.findViewById(R.id.BBuscarPartida)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogoOnline.dismiss();
                            Intent lista = new Intent(MainActivity.this, ListaPartidasActivity.class);
                            lista.putExtra("Correo",Correo);
                            startActivity(lista);


                        }
                    });

                    dialogoOnline.show();

                }else {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View formElementsView = inflater.inflate(R.layout.pantalla_cantidad,
                            null, false);


                    final EditText nameEditText = (EditText) formElementsView
                            .findViewById(R.id.respCant);
                    nameEditText.setMinWidth(200);
                    nameEditText.setFilters(new InputFilter[]{new MinMaxFiltro("2", "8")});

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Elija la cantidad de jugadores:");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            String toastString = "";


                            toastString += nameEditText.getText();
                            Intent intent = new Intent(MainActivity.this, JugadoresActivity.class);
                            intent.putExtra("CANTIDAD", toastString);
                            intent.putExtra("correo",Correo);
                            intent.putExtra("Id", Id);
                            intent.putExtra("Login",login);
                            dialog.cancel();
                            startActivity(intent);
                        }

                    });
                    builder.setView(formElementsView);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                            .setEnabled(false);

                    nameEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (TextUtils.isEmpty(editable)) {
                                // Disable ok button
                                ((AlertDialog) dialog).getButton(
                                        AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            } else {
                                // Something into edit text. Enable the button.
                                ((AlertDialog) dialog).getButton(
                                        AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            }
                        }
                    });
               /* new AlertDialog.Builder(MainActivity.this).setView(formElementsView)
                        .setTitle("Elija la cantidad de jugadores:")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                String toastString = "";


                                toastString += nameEditText.getText();
                                Intent intent = new Intent(MainActivity.this, JugadoresActivity.class);
                                intent.putExtra("CANTIDAD",toastString);
                                intent.putExtra("Id",Id);

                                dialog.cancel();
                                startActivity(intent);
                            }

                        }).show();*/
                }
            }
            });






        bAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent Ayuda = new Intent(getApplicationContext(), AyudaActivity.class);
                startActivity(Ayuda);


            }
        });

        bRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Ranking = new Intent(getApplicationContext(), RankingActivity.class);
                startActivity(Ranking);
            }
        });

        bAdministracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Admin = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(Admin);
            }
        });
        }

    public void showToast(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    }


