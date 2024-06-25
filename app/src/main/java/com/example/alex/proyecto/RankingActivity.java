package com.example.alex.proyecto;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class RankingActivity extends AppCompatActivity {

    String url = "http://proyectokiriki.000webhostapp.com/devolverClasificacion.php";
    ListView lista;
    Gson gson;
    AsyncHttpClient cliente;
    Clasificacion clasificacion;
    AdaptadorClasificacion adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_clasificacion);

        lista = (ListView) findViewById(R.id.listaClasificacion);

        cliente = new AsyncHttpClient();
        cliente.get(RankingActivity.this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String valorDevuelto = new String(responseBody);
                gson = new Gson();
                clasificacion = gson.fromJson(valorDevuelto, Clasificacion.class);
                adaptador = new AdaptadorClasificacion(RankingActivity.this,clasificacion.getClasificacion());
                lista.setAdapter(adaptador);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });





    }
}
