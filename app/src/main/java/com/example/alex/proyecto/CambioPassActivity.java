package com.example.alex.proyecto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import cz.msebera.android.httpclient.Header;

public class CambioPassActivity extends AppCompatActivity {

    EditText pass,pass2;
    Button bCambiar;
    String correoN;
    String url = "http://proyectokiriki.000webhostapp.com/cambiarPass.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_cambiopass);


        pass = (EditText) findViewById(R.id.textoPass);
        pass2 = (EditText) findViewById(R.id.textoPass2);
        bCambiar = (Button) findViewById(R.id.botonCambioPass);
        correoN = getIntent().getStringExtra("correo");

        bCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pass.getText().toString().trim().equalsIgnoreCase("")||pass2.getText().toString().trim().equalsIgnoreCase("")){
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View formElementsView = inflater.inflate(R.layout.pantalla_estado,
                            null, false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(CambioPassActivity.this);
                    builder.setTitle("Deben estar todos los campos rellenos.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    builder.setView(formElementsView);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    if(pass.getText().toString().trim().equalsIgnoreCase(pass2.getText().toString().trim())==true) {
                        AsyncHttpClient cliente = new AsyncHttpClient();
                        RequestParams rp = new RequestParams();

                        rp.put("correo", correoN);
                        rp.put("password", pass.getText().toString().trim());
                        cliente.get(url, rp, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                if (statusCode == 200) {
                                    String respuesta = response.toString();
                                    Gson gson = new Gson();

                                    UpdatePass update = gson.fromJson(respuesta, UpdatePass.class);

                                    String mensaje = update.getMessage();
                                    Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
                                    int res = update.getSuccess();

                                    //Si es correcta
                                    if (res == 1) {
                                        Toast.makeText(getApplicationContext(), "Contraseña cambiada", Toast.LENGTH_LONG).show();
                                        //Una vez iniciada la sesión, nos vamos a una pantalla cualquiera

                                        Intent Principal = new Intent(getApplicationContext(), MainActivity.class);
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

    }
}
