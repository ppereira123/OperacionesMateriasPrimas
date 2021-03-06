package com.example.operacionesmteriasprimas.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Modelos.sumaInformeOperador;
import com.example.operacionesmteriasprimas.Modelos.sumas;
import com.example.operacionesmteriasprimas.R;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class adaptadorInformeporOperador extends BaseAdapter {
    private Context context;
    private List<sumaInformeOperador> listItems;

    public adaptadorInformeporOperador(Context context, List<sumaInformeOperador> listItems) {
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
                    inflate(R.layout.listadaptadorinformeoperador, parent, false);
        }
        double horasprincipales,horasextra,total;
        horasprincipales=0.0;
        horasextra=0.0;
        sumaInformeOperador datosInforme= (sumaInformeOperador) getItem(position);
        TextView txtNombreOperadorInforme, txthorasporoperador,txthorasactprinc,txthorasactextra;
        ListView listActividadesoperadores;
        txtNombreOperadorInforme= convertView.findViewById(R.id.txtNombreOperadorInforme);
        txthorasporoperador=convertView.findViewById(R.id.txthorasporoperador);

        txthorasactprinc=convertView.findViewById(R.id.txthorasactprinc);

        txthorasactextra=convertView.findViewById(R.id.txthorasactextra);
        listActividadesoperadores=convertView.findViewById(R.id.listActividadesoperadores);
        txtNombreOperadorInforme.setText(datosInforme.getNombreOperador());
        adaptadorVistaHoras adapter = new adaptadorVistaHoras(context, datosInforme.getListaactividades());
        listActividadesoperadores.setAdapter(adapter);

        listActividadesoperadores.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, (int) convertDpToPx(context,56*datosInforme.getListaactividades().size())));
        for(sumas suma: datosInforme.getListaactividades()){
            if (suma.getActividad().equals("Extracci??n")||suma.getActividad().equals("Esteril")){
                horasprincipales=horasprincipales+suma.getHoras();
            }
            else {
                horasextra=horasextra+suma.getHoras();
            }
        }
        total=horasprincipales+horasextra;
        BigDecimal bd = new BigDecimal(total).setScale(0, RoundingMode.HALF_UP);
        int val1 = (int) bd.doubleValue();
        BigDecimal bd2 = new BigDecimal(horasextra).setScale(0, RoundingMode.HALF_UP);
        int val2 = (int) bd2.doubleValue();
        BigDecimal bd3 = new BigDecimal(horasprincipales).setScale(0, RoundingMode.HALF_UP);
        int val3 = (int) bd3.doubleValue();

        txthorasporoperador.setText(String.valueOf(val1));
        txthorasactextra.setText(String.valueOf(val2));
        txthorasactprinc.setText(String.valueOf(val3));

        return convertView;
    }
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
