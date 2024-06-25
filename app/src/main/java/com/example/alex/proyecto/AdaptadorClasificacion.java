package com.example.alex.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdaptadorClasificacion extends BaseAdapter {

    Context contexto;
    LayoutInflater inflador;
    List<Clasificacion.ClasificacionBean> listaClasificacion;

    public AdaptadorClasificacion(Context contexto, List<Clasificacion.ClasificacionBean> listaClasificacion){

        this.contexto = contexto;
        this.listaClasificacion = listaClasificacion;
    }

    @Override
    public int getCount() {
        return listaClasificacion.size();
    }

    @Override
    public Object getItem(int i) {
        return listaClasificacion.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null){
            view = inflador.inflate(R.layout.pantalla_detalle_clasificacion,null);
        }

        Clasificacion.ClasificacionBean clasificacion = (Clasificacion.ClasificacionBean) getItem(i);
        TextView elementoPosicion = view.findViewById(R.id.textoPosicion);
        elementoPosicion.setText(clasificacion.getIdUsuario());
        TextView elementoNombre = view.findViewById(R.id.textoNombre);
        elementoNombre.setText(clasificacion.getNombre());
        TextView elementoTurnos = view.findViewById(R.id.textoTurnos);
        elementoTurnos.setText(clasificacion.getTurnos());
        TextView elementoFallos = view.findViewById(R.id.textoFallos);
        elementoFallos.setText(clasificacion.getFallos());
        TextView elementoJugadores = view.findViewById(R.id.textoJugadores);
        elementoJugadores.setText(clasificacion.getParticipantes());

        return view;
    }
}
