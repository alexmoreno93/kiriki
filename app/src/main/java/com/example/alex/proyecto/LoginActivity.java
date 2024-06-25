package com.example.alex.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class LoginActivity extends AppCompatActivity {

    EditText correo, password;
    Button botonLogin,botonPass;
    String url = "http://proyectokiriki.000webhostapp.com/loginApp.php";
    SQLiteDatabase bd;
    private int id=0;
    Pattern pattern = Patterns.EMAIL_ADDRESS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_login);


        correo = (EditText) findViewById(R.id.correoLogin);
        password = (EditText) findViewById(R.id.passLogin);
        botonLogin = (Button) findViewById(R.id.botonLogin);
        botonPass = (Button) findViewById(R.id.botonPass);


        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String correoN = correo.getText().toString().trim();
                String passN = password.getText().toString().trim();

                if (correoN.isEmpty() || passN.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Deben estar todos los campos rellenos.", Toast.LENGTH_LONG).show();
                } else if(!pattern.matcher(correo.getText().toString().trim()).matches()) {
                    correo.setError("El correo no es correcto, debe ser según el patrón: ejemplo@ejemplo.com");
                }else{

                    AsyncHttpClient cliente = new AsyncHttpClient();
                    RequestParams rp = new RequestParams();
                    rp.put("correo", correoN);
                    rp.put("password", passN);

                    cliente.get(url, rp, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (statusCode == 200) {
                                String respuesta = response.toString();
                                Gson gson = new Gson();
                                LoginUsuario usuarios = gson.fromJson(respuesta, LoginUsuario.class);

                                String mensaje = usuarios.getMessage();
                                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                                int res = usuarios.getSuccess();

                                //Si es correcta
                                if (res == 1) {
                                    LoginUsuario.UsuarioBean usuario = usuarios.getUsuario().get(0);

                                    // Preparamos los datos para la inserción en BD interna.
                                    String IdUsuario = usuario.getIdUsuario();
                                    String Correo = usuario.getCorreo();
                                    String Password = usuario.getPassword();
                                    String Administrador = usuario.getAdministrador();


                                    //Abrimos BD.
                                    bd = getApplication().openOrCreateDatabase(MainActivity.BBDD,MODE_PRIVATE,null);


                                    //Borramos sesión anterior (si hay).
                                    String sqlLimpiarBD = "DELETE FROM Usuario";
                                    bd.execSQL(sqlLimpiarBD);

                                    //Creamos sentencia preparada.
                                    String sqlInsercion = "INSERT INTO Usuario (IdUsuario, Correo, Password, Administrador) " +
                                            "VALUES (?, ?, ?, ?)";

                                    //Insertamos usuario que inicia sesión.
                                    bd.execSQL(sqlInsercion, new Object[]{IdUsuario, Correo, Password, Administrador});

                                    // Consulta
                                    String sqlSelect = "SELECT * FROM Usuario";
                                    Cursor cursor = bd.rawQuery(sqlSelect, null);

                                    if (cursor.moveToFirst()) {
                                        id = cursor.getInt(0);
                                        System.out.println("Id: " + cursor.getInt(0));
                                        System.out.println("Correo: " + cursor.getString(1));
                                        System.out.println("Password: " + cursor.getString(2));
                                        System.out.println("Administrador: " + cursor.getInt(3));



                                    }

                                    //Una vez iniciada la sesión, nos vamos a una pantalla cualquiera
                                    Intent Principal = new Intent(getApplicationContext(), MainActivity.class);
                                    Principal.putExtra("Iniciada",true);
                                    Principal.putExtra("Id",id);
                                    Principal.putExtra("Correo",correo.getText().toString());
                                    Principal.putExtra("Login",true);
                                    finish();
                                    startActivity(Principal);
                               }
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }
                    });
                }

            }
        });

        botonPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent pass = new Intent(getApplicationContext(),CambioPassActivity.class);
                Intent pass = new Intent(getApplicationContext(),PassActivity.class);
                finish();
                startActivity(pass);
            }
        });

    }
}
