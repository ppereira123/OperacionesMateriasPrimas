package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Path;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Adapters.BaseAdapterOperadores;
import com.example.operacionesmteriasprimas.Adapters.adaptadorlistaOperadores;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaOperadores extends AppCompatActivity {
    TextView txtInspector,txtTurno,txtFecha,txtPendientes;
    ListView listOperadores;
    Reporte reporte= null;
    List<Operador> operadores;
    HashMap<String,Operador> hashOperadores;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_lista_operadores);
        txtInspector=findViewById(R.id.txtInspector);
        txtTurno=findViewById(R.id.txtTurno);
        txtFecha=findViewById(R.id.txtFechaoperador);
        txtPendientes=findViewById(R.id.txtOperadoresPendientes);
        listOperadores=findViewById(R.id.listOperadores);

        reporte=(Reporte)getIntent().getSerializableExtra("reporte");
        txtInspector.setText(reporte.getSupervisor());
        txtFecha.setText(reporte.getFecha());
        txtTurno.setText(reporte.getTurno());
        txtPendientes.setText("Operadores incompletos: "+String.valueOf(reporte.getRestantes()));
        cargarOperadores();

    }

    private void cargarOperadores() {
        operadores=new ArrayList<>();
        hashOperadores=reporte.getOperadores();
        operadores=hashToList(hashOperadores);
        adaptadorlistaOperadores adaptadorlistaOperadores=new adaptadorlistaOperadores(context, operadores,reporte);
        listOperadores.setAdapter(adaptadorlistaOperadores);
    }

    private List<Operador> hashToList(HashMap<String, Operador> hashOperadores) {
        List<Operador> operadores= new ArrayList<>();
        for(Map.Entry<String,Operador> entry:hashOperadores.entrySet()){
            operadores.add(entry.getValue());
        }
        return operadores;
    }
}