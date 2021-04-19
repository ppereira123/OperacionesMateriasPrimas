package com.example.operacionesmteriasprimas.Adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.example.operacionesmteriasprimas.ui.reporte.ListaActividadOperadores;
import com.example.operacionesmteriasprimas.ui.reporte.ListaOperadores;
import com.example.operacionesmteriasprimas.ui.reporte.Nuevoreporte;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class adaptadorlistaOperadores extends BaseAdapter {
    private Context context;
    private List<Operador> listItems;
    View view;
    private String nombre_operador;
    String urlArchivo="";
    ListaActividadOperadores actividadesOperador;
    List actividadesSelecionadas=new ArrayList<String>();
    HashMap<String,Operador> hashOperadores;
    ListaOperadores instance;
    Reporte reporte;


    public adaptadorlistaOperadores(Context context, List<Operador> listItems, Reporte reporte, ListaOperadores instance) {
        this.context = context;
        this.listItems = listItems;
        this.nombre_operador = nombre_operador;
        this.urlArchivo = urlArchivo;
        this.actividadesOperador = actividadesOperador;
        this.hashOperadores = hashOperadores;
        this.reporte=reporte;
        this.instance=instance;
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

        convertView = LayoutInflater.from(context).inflate(R.layout.adaptador_opedador_vista, null);
        view = convertView;
        Operador item = (Operador) getItem(position);
        TextView nombreOperador = convertView.findViewById(R.id.txtNombreOperador);
        ImageView imgCompletado = convertView.findViewById(R.id.imgCompletado);
        nombreOperador.setText(item.getNombre());
        LinearLayout viewOperador = convertView.findViewById(R.id.linearOperador);

        if (item.isCompleto()) {
            imgCompletado.setImageResource(R.mipmap.check);
        } else {
            imgCompletado.setImageResource(R.mipmap.multiply);
        }


        if(item.getNombreActividades().size()<=0){
        viewOperador.setOnClickListener(new View.OnClickListener() {

            String[] actividades = context.getResources().getStringArray(R.array.combo_tiposOperaciones);
            boolean[] checkedItems = new boolean[actividades.length];

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Escoge los operadores");
                builder.setMultiChoiceItems(actividades, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            if (!actividadesSelecionadas.contains(which)) {
                                actividadesSelecionadas.add(actividades[which]);
                            } else {
                                actividadesSelecionadas.remove(which);
                            }
                        }

                        else{
                            actividadesSelecionadas.remove(actividadesSelecionadas.indexOf(actividades[which]));
                        }

                                Toast.makeText(context, String.valueOf(actividadesSelecionadas.size()), Toast.LENGTH_SHORT).show();
                    }

                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(actividadesSelecionadas.size()>0) {
                            item.setNombreActividades(actividadesSelecionadas);
                            List<Double> valores= new ArrayList<>();
                            for(int i=0;i<actividadesSelecionadas.size();i++){
                                valores.add(0.0);
                            }
                            item.setActividades(valores);
                            HashMap<String, Operador> operadorHashMap = reporte.getOperadores();
                            operadorHashMap.put(item.getNombre(), item);
                            reporte.setOperadores(operadorHashMap);
                            try {
                                new InternalStorage().guardarReporte(reporte, context);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(context, ListaActividadOperadores.class);
                            intent.putExtra("operador", (Serializable) item);
                            intent.putExtra("reporte", (Serializable) reporte);
                            instance.startActivityForResult(intent,3);
                        }
                        else {
                            Snackbar snackbar=Snackbar.make(view,"Debe seleccionar al menos una actividad", BaseTransientBottomBar.LENGTH_LONG);
                            snackbar.show();
                        }


                    }
                });
                builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();


            }
        });

    }
        else{
            viewOperador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ListaActividadOperadores.class);
                    intent.putExtra("operador", (Serializable) item);
                    intent.putExtra("reporte", (Serializable) reporte);
                    instance.startActivityForResult(intent,3);
                }
            });

        }

        return convertView;
    }
}
