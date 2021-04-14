package com.example.operacionesmteriasprimas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseAdapterOperadores extends BaseAdapter {
    List<String> listOperadoses;
    HashMap<Integer,String> operadoresSeleccionados=new HashMap<>();
    Context context;

    public BaseAdapterOperadores( List<String> listOperadores,Context contex) {
        this.listOperadoses=listOperadores;
        this.context=contex;
    }

    @Override
    public int getCount() {
        return listOperadoses.size();
    }

    @Override
    public String getItem(int position) {
        return listOperadoses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.adaptador_nombre_operadores, null);
        String item=(String)getItem(position);
        TextView txtNombre=convertView.findViewById(R.id.txtnombreOperadorLv);
        CheckBox checkBox=convertView.findViewById(R.id.checkBoxOperador);
        LinearLayout llEscoger=convertView.findViewById(R.id.llEscoger);
        txtNombre.setText(item);


     checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if(isChecked){
                 operadoresSeleccionados.put(position,item);
             }
             else{
                 operadoresSeleccionados.remove(position);
             }
         }
     });
        return convertView;
    }
}
