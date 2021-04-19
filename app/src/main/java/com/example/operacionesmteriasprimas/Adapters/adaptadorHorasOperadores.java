package com.example.operacionesmteriasprimas.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.R;

import java.util.ArrayList;
import java.util.List;

public class adaptadorHorasOperadores extends BaseAdapter {
    Context context;
    List<Operador> lst;

    public adaptadorHorasOperadores(Context context, List<Operador> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView nombre,hora;
       Operador operador= (Operador) getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.adaptador_horas, null);
        nombre= convertView.findViewById(R.id.operador);
        hora= convertView.findViewById(R.id.hora);
        nombre.setText(operador.getNombre());
        Double sumahoras=0.0;
        List<Double> horas=operador.getActividades();
        for (Double i: horas){
            sumahoras=sumahoras+i;
        }
        hora.setText(sumahoras.toString());
        if (sumahoras>=12||sumahoras<=7.5){
            hora.setBackgroundColor(Color.parseColor("#FF5555"));
        }








        return convertView;
    }
}
