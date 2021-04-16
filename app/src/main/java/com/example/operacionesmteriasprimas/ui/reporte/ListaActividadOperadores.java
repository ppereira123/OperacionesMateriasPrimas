package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.AdapterRecyclerActividades;
import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaActividadOperadores extends AppCompatActivity {
    TextView txtNombreOperador,txtFecha,txtActividadesPendientes,txtModificarParametros;
    ChipGroup chipGroup;
    ListView listView;
    Reporte reporte=null;
    Operador operador=null;
    LinearLayout lLModificar,layout;
    List<String> actividades;
    List<String> actividad;
    int cont;

    List actividadesSelecionadas=new ArrayList<String>();
    HashMap<String,Double> valores;


    Context context=this;
    AdapterRecyclerActividades adapter;
    View root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividad_operadores);

        txtNombreOperador=findViewById(R.id.txtoperador);
        txtFecha=findViewById(R.id.txtFechaoperador);
        txtActividadesPendientes=findViewById(R.id.txtOperadoresPendientes);
        txtModificarParametros=findViewById(R.id.txtAgregarParametro);
        lLModificar=findViewById(R.id.lLModificarParametros);
        layout=findViewById(R.id.llactividadoperadores);
        listView=findViewById(R.id.listActividades);
        chipGroup=findViewById(R.id.chipGroupActividades);
        reporte=(Reporte)getIntent().getSerializableExtra("reporte");
        operador=(Operador)getIntent().getSerializableExtra("operador");
        actividades=operador.getNombreActividades();
        cargarDatos();
        generarChips();
        iniciarChips();


        lLModificar.setOnClickListener(new View.OnClickListener() {
            String[] actividadesM = context.getResources().getStringArray(R.array.combo_tiposOperaciones);
            boolean[] checkedItems = new boolean[actividadesM.length];
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Escoge los operadores");
                builder.setMultiChoiceItems(actividadesM, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            if (!actividadesSelecionadas.contains(which)) {
                                actividadesSelecionadas.add(actividadesM[which]);
                            } else {
                                actividadesSelecionadas.remove(which);
                            }
                        }

                        else{
                            actividadesSelecionadas.remove(actividadesSelecionadas.indexOf(actividadesM[which]));
                        }

                        Toast.makeText(context, String.valueOf(actividadesSelecionadas.size()), Toast.LENGTH_SHORT).show();
                    }

                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(actividadesSelecionadas.size()>0) {
                            operador.setNombreActividades(actividadesSelecionadas);
                            HashMap<String, Operador> operadorHashMap = reporte.getOperadores();
                            operadorHashMap.put(operador.getNombre(), operador);
                            reporte.setOperadores(operadorHashMap);
                            try {
                                new InternalStorage().guardarReporte(reporte, context);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            Snackbar sna=Snackbar.make(context,layout,"Debe tener al menos una actividad", BaseTransientBottomBar.LENGTH_LONG);
                            sna.show();
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

    private void iniciarChips() {
        for(Map.Entry<String,Double>entry:valores.entrySet()){
            marcarChips(Integer.parseInt(entry.getKey()));
        }
    }

    public void guardar(View view){
        //Obtengo las actividades realizadas
        valores=adapter.getValores();
        //Agrego las actividades al operador
        operador.setActividades(valores);
        if(operador.getActividadesPendientes()<=0){
            operador.setCompleto(true);
        }
        else{
            operador.setCompleto(false);
        }
        //Actualizo el operador en el reporte
        HashMap<String,Operador> operadorHashMap=reporte.getOperadores();
        operadorHashMap.put(operador.getNombre(),operador);
        reporte.setOperadores(operadorHashMap);
        try {
            new InternalStorage().guardarReporte(reporte,context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent= new Intent();
        intent.putExtra("reporte",reporte);
        setResult(3,intent);
        finish();
    }

    private void generarChips() {

        cont=1;
        for(String s:actividades){
            Chip chip= new Chip(context);
            chip.setTextSize(18);
            chip.setText("Actividad "+cont);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actividad.clear();
                    String nombreChip=chip.getText().toString();
                    String[] sep=nombreChip.split(" ");
                    int posicion=Integer.parseInt(sep[1])-1;
                    actividad.add(actividades.get(posicion));
                    valores=adapter.getValores();
                    cargarLista();
                }
            });
            chipGroup.addView(chip);
            cont++;
        }

    }

    public void marcarChips(Integer posicion){
        valores=adapter.getValores();
        Chip chip=(Chip) chipGroup.getChildAt(posicion);
        chip.setChipBackgroundColorResource(R.color.purple_200);
    }

    public int getPosicionChip(Chip chip){
        String nombreChip=chip.getText().toString();
        String[] sep=nombreChip.split(" ");
        int posicion=Integer.parseInt(sep[1])-1;
        return posicion;
    }

    private void cargarDatos() {
        actividad=new ArrayList<>();
        txtFecha.setText(reporte.getFecha());
        txtNombreOperador.setText(operador.getNombre());
        txtActividadesPendientes.setText("Actividades por ingresar: "+operador.getActividadesPendientes());
        actividad.add(actividades.get(0));
        valores=operador.getActividades();
        cargarLista();

    }

    void cargarLista(){
        adapter= new AdapterRecyclerActividades(actividad,context,valores,actividades.indexOf(actividad.get(0)),this);
        listView.setAdapter(adapter);
    }

    public void eliminarDeLista(String actividad){

    }

    public boolean anterior(String item){
        int index=actividades.indexOf(item)-1;
        if(index>=0){
            actividad.clear();
            actividad.add(actividades.get(index));
            cargarLista();
            return true;
        }
        else{return false; }
    }

    public boolean siguiente(String item){
        int maxIndex=actividades.size()-1;
        int index=actividades.indexOf(item)+1;
        if(index<=maxIndex){
            actividad.clear();
            actividad.add(actividades.get(index));
            cargarLista();
            return  true;
        }
        else{
            return false;
        }
    }


}