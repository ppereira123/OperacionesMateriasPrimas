package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Adapters.AdapterRecyclerActividades;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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
    List<String> actividades;
    List<String> actividad;
    int cont;
    HashMap<Integer,Double> valores;
    Context context=this;
    AdapterRecyclerActividades adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividad_operadores);
        txtNombreOperador=findViewById(R.id.txtoperador);
        txtFecha=findViewById(R.id.txtFechaoperador);
        txtActividadesPendientes=findViewById(R.id.txtOperadoresPendientes);
        txtModificarParametros=findViewById(R.id.txtAgregarParametro);
        listView=findViewById(R.id.listActividades);
        chipGroup=findViewById(R.id.chipGroupActividades);
        reporte=(Reporte)getIntent().getSerializableExtra("reporte");
        operador=(Operador)getIntent().getSerializableExtra("operador");
        actividades=operador.getNombreActividades();
        cargarDatos();
        generarChips();


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