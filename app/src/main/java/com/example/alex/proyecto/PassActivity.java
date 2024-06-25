package com.example.alex.proyecto;

import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PassActivity extends AppCompatActivity {

    int codigoEnviar = 0;
    EditText textoCorreo, codigoRecibir;
    Button bEnviar,bVerificar;
    String correo, pass, correoN = "";
    Session sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_pass);

        textoCorreo = (EditText) findViewById(R.id.textoCorreo);
        codigoRecibir = (EditText) findViewById(R.id.textoCodigo);
        bEnviar = (Button) findViewById(R.id.botonCorreo);
        bVerificar = (Button) findViewById(R.id.botonVerificar);


        correo = "kirikiproyecto@gmail.com";
        pass = "kiriki93";

        bEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigoEnviar = (int) (Math.random() * 100000) + 1;
                correoN = textoCorreo.getText().toString().trim();

                if (correoN.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debes introducir un correo.", Toast.LENGTH_LONG).show();
                } else {


                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Properties properties = new Properties();
                    properties.put("mail.smtp.host", "smtp.googlemail.com");
                    properties.put("mail.smtp.socketFactory.port", "465");
                    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.port", "465");


                    try {

                        sesion = Session.getDefaultInstance(properties, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(correo, pass);
                            }
                        });

                        if (sesion != null) {
                            javax.mail.Message message = new MimeMessage(sesion);
                            message.setFrom(new InternetAddress(correo));
                            message.setSubject("Código para recuperar contraseña.");
                            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(correoN));
                            message.setContent("Introduzca este código para acceder al cambio de contraseña:  " + codigoEnviar, "text/html; charset=utf-8");
                            Transport.send(message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        bVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigoN = codigoRecibir.getText().toString().trim();

                if (codigoN.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Debes introducir un código.", Toast.LENGTH_LONG).show();
                } else if(codigoN.equalsIgnoreCase(String.valueOf(codigoEnviar))==false){
                    Toast.makeText(getApplicationContext(), "El código no coincide, revise el correo por favor.", Toast.LENGTH_LONG).show();
                } else {
                    Intent pass = new Intent(getApplicationContext(), CambioPassActivity.class);
                    pass.putExtra("correo",correoN);
                    finish();
                    startActivity(pass);


                }
            }
        });



    }
}