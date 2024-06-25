package com.example.alex.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdaptadorUsuario extends BaseAdapter {

    Context contexto;
    LayoutInflater inflador;
    List<Usuario.UsuarioBean> listaUsuarios;

    public AdaptadorUsuario(Context contexto, List<Usuario.UsuarioBean> listaUsuarios){
        this.contexto = contexto;
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public int getCount() {
        return listaUsuarios.size();
    }

    @Override
    public Object getItem(int pos) {
        return listaUsuarios.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null){
            view = inflador.inflate(R.layout.pantalla_elemento_lista_usuario,null);
        }

        Usuario.UsuarioBean usuarios = (Usuario.UsuarioBean) getItem(pos);
        TextView correo = view.findViewById(R.id.correoLista);

        correo.setText(usuarios.getCorreo().toString());


        return view;
    }
}
