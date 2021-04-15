package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.R;

public class ListaActividadOperadores extends AppCompatActivity {
    TextView txtNombreOperador,txtFecha,txtActividadesPendientes,txtModificarParametros;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividad_operadores);
        txtNombreOperador=findViewById(R.id.txtoperador);
        txtFecha=findViewById(R.id.txtFechaoperador);
        txtActividadesPendientes=findViewById(R.id.txtOperadoresPendientes);
        txtModificarParametros=findViewById(R.id.txtAgregarParametro);
        listView=findViewById(R.id.listActividades);


    }
}