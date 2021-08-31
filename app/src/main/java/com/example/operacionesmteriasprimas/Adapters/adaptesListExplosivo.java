package com.example.operacionesmteriasprimas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Modelos.Explosivo;
import com.example.operacionesmteriasprimas.Modelos.Voladura;
import com.example.operacionesmteriasprimas.R;

import java.io.Serializable;
import java.util.ArrayList;

public class adaptesListExplosivo extends BaseAdapter implements Serializable {
    Context context;
    ArrayList<Explosivo> listItems;

    public adaptesListExplosivo(Context context, ArrayList<Explosivo> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.adaptes_list_explosivo,parent,false);
        }
        Explosivo explosivo= (Explosivo) getItem(position);
        TextView txtTipoListexplosivo,txtEgresoListexplosivo,txtingresoListexplosivo;
        txtingresoListexplosivo=convertView.findViewById(R.id.txtingresoListexplosivo);
        txtTipoListexplosivo=convertView.findViewById(R.id.txtTipoListexplosivo);
        txtEgresoListexplosivo=convertView.findViewById(R.id.txtEgresoListexplosivo);
        String ingreso="0",egreso="0";
        ingreso=explosivo.getIngreso().toString();
        egreso=explosivo.getEgreso().toString();
        txtEgresoListexplosivo.setText("Egreso: "+egreso);
        txtingresoListexplosivo.setText("Ingreso: "+ingreso);
        txtTipoListexplosivo.setText(explosivo.getTipo().toString());
        return convertView;
    }
}
