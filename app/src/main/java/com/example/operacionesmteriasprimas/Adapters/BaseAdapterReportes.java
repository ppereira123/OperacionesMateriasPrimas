package com.example.operacionesmteriasprimas.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.example.operacionesmteriasprimas.ui.reporte.ListaOperadores;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAdapterReportes extends BaseAdapter {
    List<Reporte> listReportes;
    Context context;
    Fragment fragment;

    public BaseAdapterReportes(List<Reporte> listReportes, Context contex, Fragment fragment) {
        this.listReportes=listReportes;
        this.context=contex;
        this.fragment=fragment;
    }

    @Override
    public int getCount() {
        return listReportes.size();
    }

    @Override
    public Reporte getItem(int position) {
        return listReportes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_element_reportes, null);
        Reporte item=(Reporte)getItem(position);
        TextView txtInspector,txtTurno,txtFecha,txtOperadoresRestantes;
        ImageView imgCompleto,imgSubido;

        txtInspector=convertView.findViewById(R.id.txtInspectorReporte);
        txtFecha=convertView.findViewById(R.id.txtFechaReporte);
        txtTurno=convertView.findViewById(R.id.txtTurnoReporte);
        txtOperadoresRestantes=convertView.findViewById(R.id.txtOperadoresRestantes);
        imgCompleto=convertView.findViewById(R.id.imgReporteCompletada);
        imgSubido=convertView.findViewById(R.id.imgSubidoReporte);

        LinearLayout llReportes=convertView.findViewById(R.id.llReportes);

        llReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ListaOperadores.class);
                intent.putExtra("reporte",item);
                fragment.startActivityForResult(intent,2);

            }
        });

        txtInspector.setText(item.getSupervisor());
        txtFecha.setText(item.getFecha());
        txtTurno.setText(item.getTurno());

        int restantes=item.getRestantes();
        txtOperadoresRestantes.setText(String.valueOf(restantes));

        if(item.isCompletado()){
            imgCompleto.setImageResource(R.drawable.check);
        }
        else{
            imgCompleto.setImageResource(R.drawable.multiply);
        }

        if(item.isSubido()){
            imgSubido.setVisibility(View.VISIBLE);
        }
        else{
            imgSubido.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
