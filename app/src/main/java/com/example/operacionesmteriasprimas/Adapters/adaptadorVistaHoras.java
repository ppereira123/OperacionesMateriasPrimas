package com.example.operacionesmteriasprimas.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Modelos.sumas;
import com.example.operacionesmteriasprimas.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class adaptadorVistaHoras extends BaseAdapter {
    private Context context;
    Activity activity;
    private List<sumas> listItems;

    public adaptadorVistaHoras(Context context, List<sumas> listItems) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.adapter_vista_actividades, parent, false);
        }
        sumas currentItem = (sumas) getItem(position);
        TextView actividad,horas;
        actividad=convertView.findViewById(R.id.txtactividadview);
        horas=convertView.findViewById(R.id.txthorasview);
        actividad.setText(currentItem.getActividad());
        double hora=currentItem.getHoras();
        BigDecimal bd = new BigDecimal(hora).setScale(0, RoundingMode.HALF_UP);
        int val1 = (int) bd.doubleValue();
        horas.setText(String.valueOf(val1));
        return convertView;
    }
}
