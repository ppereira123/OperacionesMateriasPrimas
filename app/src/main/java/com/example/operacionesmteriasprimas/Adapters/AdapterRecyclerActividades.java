package com.example.operacionesmteriasprimas.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.R;
import com.example.operacionesmteriasprimas.ui.reporte.ListaActividadOperadores;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterRecyclerActividades extends BaseAdapter {
    private List<String> mdata;
    private Context context;
    HashMap<String , Double> valores;
    int posicion;
    private ListaActividadOperadores instance;
    Double horas;

    public AdapterRecyclerActividades(List<String> mdata,  Context context, HashMap<String, Double> valores, int posicion, ListaActividadOperadores instance) {
        this.mdata = mdata;
        this.context = context;
        this.valores = valores;
        this.posicion=posicion;
        this.instance = instance;
    }


    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public HashMap<String,Double> getValores(){
        return valores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.adaptador_reportes_diarios, null);
        String item=(String)getItem(position);
        TextView txtEnunciado=convertView.findViewById(R.id.txtNombreActividad);
        TextInputEditText tietHoras=convertView.findViewById(R.id.editTextHoras);
        Button btnSiguient=convertView.findViewById(R.id.btnSiguiente);
        Button btnAnterior=convertView.findViewById(R.id.btnAnterio);

        if(valores.containsKey(posicion)){
            tietHoras.setText(String.valueOf(valores.get(posicion)));
        }

        else{
            tietHoras.setText("");
        }

        txtEnunciado.setText(item);
        tietHoras.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    horas=Double.parseDouble(s.toString());
                }
                else{
                    horas=0.0;

                }
                if(horas>0.0){
                    valores.put(String.valueOf(posicion),horas);
                    instance.marcarChips(posicion);
                }
                else{
                   valores.remove(posicion);
                }
            }
        });

        btnSiguient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean siguiente=instance.siguiente(item);
                if(siguiente){
                    btnSiguient.setEnabled(true);
                    btnSiguient.setTextColor(context.getColor(R.color.purple_200));
                }
                else{
                    btnSiguient.setEnabled(false);
                    btnSiguient.setTextColor(context.getColor(R.color.tab));
                }
            }
        });

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean siguiente=instance.anterior(item);
                if(siguiente){
                    btnAnterior.setEnabled(true);
                    btnAnterior.setTextColor(context.getColor(R.color.purple_200));
                }
                else{
                    btnAnterior.setEnabled(false);
                    btnAnterior.setTextColor(context.getColor(R.color.tab));
                }
            }
        });
        return convertView;
    }
}
