package com.example.alex.proyecto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class DetalleUsuario extends AppCompatActivity {

    Button bEditar,bEliminar;
    EditText CorreoTextView,TipoUsuarioTextView;
    String Correo, TipoUsuario,Tipo;
    String url = "http://proyectokiriki.000webhostapp.com/updateUsuario.php";
    String url2 = "http://proyectokiriki.000webhostapp.com/eliminarUsuario.php";
    Boolean primeraVez = false;
    Pattern pattern = Patterns.EMAIL_ADDRESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_detalle_usuario);

        bEditar = (Button) findViewById(R.id.botonEditarUsuario);
        bEliminar = (Button) findViewById(R.id.botonEliminarUsuario);


        CorreoTextView = (EditText) findViewById(R.id.CorreoUsuario);
        TipoUsuarioTextView = (EditText) findViewById(R.id.TipoUsuario);

        Correo = getIntent().getStringExtra("CORREO");
        TipoUsuario = getIntent().getStringExtra("TIPO");

        if (TipoUsuario.equals("0")){
            TipoUsuario = "Normal";
            Tipo = "0";
        }else{
            TipoUsuario= "Administrador";
            Tipo = "1";
        }

        CorreoTextView.setText(Correo);
        TipoUsuarioTextView.setText(TipoUsuario);

        CorreoTextView.setEnabled(false);
        TipoUsuarioTextView.setEnabled(false);


        bEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (primeraVez == false) {
                    primeraVez = true;
                    CorreoTextView.setEnabled(true);
                    TipoUsuarioTextView.setEnabled(true);
                    bEliminar.setEnabled(false);
                    bEliminar.setTextColor(Color.parseColor("#9E9E9E"));
                } else {
                    if (CorreoTextView.getText().toString().equals(Correo)  && TipoUsuarioTextView.getText().toString().equals(TipoUsuario)) {
                        Toast.makeText(getApplicationContext(), "No se ha cambiado nada, por lo que no habrá edición.", Toast.LENGTH_LONG).show();
                    } else if (!TipoUsuarioTextView.getText().toString().equalsIgnoreCase("Administrador") && !TipoUsuarioTextView.getText().toString().equalsIgnoreCase("Normal")) {
                        Toast.makeText(getApplicationContext(), "El tipo debe ser administrador o normal.", Toast.LENGTH_LONG).show();
                    } else if(!pattern.matcher(CorreoTextView.getText().toString()).matches()) {
                        CorreoTextView.setError("El correo no es correcto, debe ser según el patrón: ejemplo@ejemplo.com");
                    }else{
                        AsyncHttpClient cliente = new AsyncHttpClient();
                        RequestParams rp = new RequestParams();

                        rp.put("correo", Correo);
                        rp.put("nuevo", CorreoTextView.getText().toString().trim());
                        rp.put("tipo", Tipo);
                        cliente.get(url, rp, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject
                                    response) {
                                if (statusCode == 200) {
                                    String respuesta = response.toString();
                                    Gson gson = new Gson();

                                    UpdateUsuario update = gson.fromJson(respuesta, UpdateUsuario.class);

                                    String mensaje = update.getMessage();
                                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
                                    int res = update.getSuccess();

                                    //Si es correcta
                                    if (res == 1) {
                                        Toast.makeText(getApplicationContext(), "Datos cambiados.", Toast.LENGTH_LONG).show();
                                        //Una vez iniciada la sesión, nos vamos a una pantalla cualquiera

                                        Intent Principal = new Intent(getApplicationContext(), MainActivity.class);
                                        Principal.putExtra("Iniciada", true);
                                        finish();
                                        startActivity(Principal);

                                    }
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            }
                        });
                    }
                }
            }
        });

        bEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AsyncHttpClient clienteEliminar = new AsyncHttpClient();
                    RequestParams rpEliminar = new RequestParams();

                    rpEliminar.put("correo", CorreoTextView.getText().toString().trim());
                    clienteEliminar.get(url2, rpEliminar, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (statusCode == 200) {
                                String respuesta = response.toString();
                                Gson gson = new Gson();

                                BorradoUsuario update = gson.fromJson(respuesta, BorradoUsuario.class);

                                String mensaje = update.getMessage();
                                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
                                int res = update.getSuccess();

                                //Si es correcta
                                if (res == 1) {
                                    Toast.makeText(getApplicationContext(), "Usuario eliminado.", Toast.LENGTH_LONG).show();
                                    //Una vez iniciada la sesión, nos vamos a una pantalla cualquiera

                                    Intent Principal = new Intent(getApplicationContext(), MainActivity.class);
                                    Principal.putExtra("Iniciada", true);
                                    finish();
                                    startActivity(Principal);

                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });

            };
        });





    }
}