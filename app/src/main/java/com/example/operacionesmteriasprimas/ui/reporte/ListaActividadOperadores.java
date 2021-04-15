package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class ListaActividadOperadores extends AppCompatActivity {
    TextView txtNombreOperador,txtFecha,txtActividadesPendientes,txtModificarParametros;
    ChipGroup chipGroup;
    ListView listView;
    Reporte reporte=null;
    Operador operador=null;
    List<String> actividades;
    List<String> actividad;
    Context context=this;


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
        actividad=new ArrayList<>();
        int cont=1;
        for(String s:actividades){
            Chip chip= new Chip(context);
            chip.setTextSize(18);
            chip.setText("Actividad "+cont);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actividad.clear();
                    actividad.add(chip.getText().toString());
                }
            });
            chipGroup.addView(chip);
            cont++;
        }

    }

    private void cargarDatos() {
        txtFecha.setText(reporte.getFecha());
        txtNombreOperador.setText(operador.getNombre());
        txtActividadesPendientes.setText("Actividades por ingresar: "+operador.getActividadesPendientes());

    }
}