package com.example.operacionesmteriasprimas.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.R;
import com.example.operacionesmteriasprimas.ui.reporte.ListaActividadOperadores;
import com.example.operacionesmteriasprimas.ui.reporte.Nuevoreporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class adaptadorlistaOperadores extends BaseAdapter {
    private Context context;
    private List<Operador> listItems;
    View convertView;
    private String nombre_operador;
    String urlArchivo="";
    ListaActividadOperadores actividadesOperador;
    List actividadesSelecionadas=new ArrayList<String>();
    HashMap<String,Operador> hashOperadores;

    public adaptadorlistaOperadores(Context context, List<Operador> listItems, String nombre_operador, String urlArchivo, ListaActividadOperadores actividadesOperador, HashMap<String, Operador> hashOperadores) {
        this.context = context;
        this.listItems = listItems;
        this.nombre_operador = nombre_operador;
        this.urlArchivo = urlArchivo;
        this.actividadesOperador = actividadesOperador;
        this.hashOperadores = hashOperadores;
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
        this.convertView=convertView;
        convertView = LayoutInflater.from(context).inflate(R.layout.adaptador_opedador_vista, null);
        Operador item= (Operador) getItem(position);
        TextView nombreOperador= convertView.findViewById(R.id.txtNombreOperador);
        ImageView imgCompletado=convertView.findViewById(R.id.imgCompletado);
        nombreOperador.setText(item.getNombre());
        LinearLayout viewOperador= convertView.findViewById(R.id.viewOperador);
        /*
        if(item.isCompleto()){
            imgCompletado.setImageResource(R.mipmap.check);
        }
        else {
            imgCompletado.setImageResource(R.mipmap.multiply);
        }
*/

        viewOperador.setOnClickListener(new View.OnClickListener() {

            String[] actividades=context.getResources().getStringArray(R.array.combo_tiposOperaciones);
            boolean[] checkedItems=new boolean[actividades.length];

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("Escoge los operadores");
                builder.setMultiChoiceItems(actividades, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            if(!actividadesSelecionadas.contains(which)){
                                actividadesSelecionadas.add(actividades[which]);
                            }
                            else{
                                actividadesSelecionadas.remove(which);
                            }
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(context, ListaActividadOperadores.class);
                        intent.putExtra("actividades", (Parcelable) actividadesSelecionadas);
                        context.startActivity(intent);


                    }
                });
                builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog=builder.create();

                dialog.show();







            }
        });

        return convertView;
    }
}
