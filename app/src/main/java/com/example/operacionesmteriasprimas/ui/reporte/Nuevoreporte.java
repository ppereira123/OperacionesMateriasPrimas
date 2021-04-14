package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;

import android.widget.Spinner;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.BaseAdapterOperadores;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Nuevoreporte extends AppCompatActivity {

    SearchView buscar;
    ListView lvNombreOperadores;
    Context context=this;
    String turno="";
    String fecha="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ingreso de Administrador");
        setContentView(R.layout.activity_nuevoreporte);

        buscar=findViewById(R.id.buscarOperadores);
        lvNombreOperadores=findViewById(R.id.lvNombreOperadores);


        llenarLista();
    }






    void llenarLista(){
        String[] operadores=getResources().getStringArray(R.array.combo_nombresOperadores);
        List<String> loperadores=new ArrayList<>();
        for(String s: operadores ){
            loperadores.add(s);
        }
        BaseAdapterOperadores baseAdapterOperadores= new BaseAdapterOperadores(loperadores,context);
        lvNombreOperadores.setAdapter(baseAdapterOperadores);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        setResult(1,intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}