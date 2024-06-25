package com.example.alex.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ListaActivity extends AppCompatActivity {

    String url = "http://proyectokiriki.000webhostapp.com/devolverUsuarios.php";
    ListView lista;
    Gson gson;
    AsyncHttpClient cliente;
    Usuario usuarios;
    AdaptadorUsuario adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_lista_usuarios);


        lista = (ListView) findViewById(R.id.listaUsuarios);

        RequestParams rp = new RequestParams();

        cliente = new AsyncHttpClient();
        cliente.get(url,rp, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String valorDevuelto = new String(response.toString());
                gson = new Gson();
                usuarios = gson.fromJson(valorDevuelto, Usuario.class);
                adaptador = new AdaptadorUsuario(ListaActivity.this,usuarios.getUsuario());
                lista.setAdapter(adaptador);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String Correo = usuarios.getUsuario().get(i).getCorreo();
                String TipoUsuario = usuarios.getUsuario().get(i).getAdministrador();

                Intent intent = new Intent(ListaActivity.this, DetalleUsuario.class);
                intent.putExtra("CORREO", Correo);
                intent.putExtra("TIPO", TipoUsuario);

                startActivity(intent);
            }
        });
    }
}
