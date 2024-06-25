package com.example.alex.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdaptadorPartida extends BaseAdapter {

    Context contexto;
    LayoutInflater inflador;
    List<Partida.PartidaBean> listaPartidas;

    public AdaptadorPartida(Context contexto, List<Partida.PartidaBean> listaPartidas){
        this.contexto = contexto;
        this.listaPartidas = listaPartidas;
    }

    @Override
    public int getCount() {
        return listaPartidas.size();
    }

    @Override
    public Object getItem(int pos) {
        return listaPartidas.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null){
            view = inflador.inflate(R.layout.pantalla_elemento_lista_partida,null);
        }

        Partida.PartidaBean partidas = (Partida.PartidaBean) getItem(pos);
        TextView id = view.findViewById(R.id.IdPartidaLista);

        id.setText(partidas.getIdPartida());


        return view;
    }
}
