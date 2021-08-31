package com.example.operacionesmteriasprimas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Modelos.Voladura;
import com.example.operacionesmteriasprimas.R;

import java.util.ArrayList;

public class adaptadorListVoladuras extends BaseAdapter {
    Context context;
    ArrayList<Voladura> listItems=new ArrayList<>();

    public adaptadorListVoladuras(Context context, ArrayList<Voladura> listItems) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.adaptador_list_voladura, parent,false);
        }
        Voladura currentItem= (Voladura) getItem(position);
        TextView txtcodVoladuraList,txtFechaListVoladura,txtequipoListVoladura;
        txtcodVoladuraList=convertView.findViewById(R.id.txtcodVoladuraList);
        txtFechaListVoladura=convertView.findViewById(R.id.txtFechaListVoladura);
        txtequipoListVoladura=convertView.findViewById(R.id.txtequipoListVoladura);
        txtcodVoladuraList.setText(currentItem.getCodigoVoladura());
        txtequipoListVoladura.setText(currentItem.getEquipo());
        txtFechaListVoladura.setText(currentItem.getFecha());
        return convertView;

    }
}
