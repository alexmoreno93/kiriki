package com.example.alex.proyecto;

import android.content.Intent;
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

public class CrearUsuarioActivity extends AppCompatActivity {

    EditText correo, pass, tipo;
    Button bCrear;
    String url = "http://proyectokiriki.000webhostapp.com/registroApp.php";
    Pattern pattern = Patterns.EMAIL_ADDRESS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_crear_usuario);

        correo = (EditText) findViewById(R.id.correoAdmin);
        pass = (EditText) findViewById(R.id.passAdmin);
        tipo = (EditText) findViewById(R.id.tipoAdmin);
        bCrear = (Button) findViewById(R.id.botonCrearUsuario);

        bCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correoN = correo.getText().toString().trim();
                String passN = pass.getText().toString().trim();
                String tipoN = tipo.getText().toString().trim();
                if(tipoN.equalsIgnoreCase("Administrador")){
                    tipoN = "1";
                }else{
                    tipoN = "0";
                }

                if(correoN.isEmpty() || passN.isEmpty() || tipoN.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Deben estar todos los campos rellenos.", Toast.LENGTH_LONG).show();
                }else if(!pattern.matcher(correo.getText().toString()).matches()) {
                    correo.setError("El correo no es correcto, debe ser según el patrón: ejemplo@ejemplo.com");
                }else{
                    AsyncHttpClient cliente = new AsyncHttpClient();
                    RequestParams rp = new RequestParams();
                    rp.put("correo",correoN);
                    rp.put("password",passN);
                    rp.put("tipo",tipoN);

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

                                //Si es correcta
                                if (res == 1) {
                                    Toast.makeText(getApplicationContext(), "El registro se ha realizado sin problemas.", Toast.LENGTH_LONG).show();

                                    //Una vez iniciada la sesión, nos vamos a una pantalla cualquiera

                                    Intent Principal = new Intent(getApplicationContext(), AdminActivity.class);
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
        });




    }
}
