package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;

import android.widget.Spinner;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Nuevoreporte extends AppCompatActivity {

    SearchView buscar;
    ListView lvNombreOperadores;
    String[] operadores;
    boolean[] checkedItems;
    List<String> operadoresSeleccionados;
    TextInputEditText tietFecha,tietOperadores;
    String muestra="";
    TextView txtTurno;
    Spinner spinner;
    Context context=this;
    String turno="";
    String fecha="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ingreso de Administrador");
        setContentView(R.layout.activity_nuevoreporte);
        tietFecha=findViewById(R.id.tietFechaNuevoReporte);
        spinner=findViewById(R.id.spinnerTurno);
        txtTurno=findViewById(R.id.txtEscogerTurno);
        tietOperadores=findViewById(R.id.tietOperadores);

        configFecha(tietFecha);
        llenarSpinner(spinner,txtTurno);
        llenarLista();





    }

    void llenarSpinner(Spinner spinner,TextView txtTurno){
        String[] turnos=getResources().getStringArray(R.array.combo_turnos);
        List<String> lturnos=new ArrayList<>();
        lturnos.add("Selecciones turno");
        for(String s:turnos){
            lturnos.add(s);
        }
        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,lturnos));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                turno= (String) parent.getItemAtPosition(position);
                txtTurno.setText("Escoger turno: "+turno);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void configFecha(TextInputEditText tietFecha) {
        Calendar calendar= Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        tietFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date= day+"/"+month+"/"+year;
                        fecha=date;
                        tietFecha.setText(fecha);
                        tietFecha.clearFocus();

                    }
                },year,month,day);



                if(hasFocus){
                    datePickerDialog.show();
                }
                else{
                    datePickerDialog.dismiss();
                }

            }
        });
    }


    void llenarLista(){
        operadoresSeleccionados=new ArrayList<>();
        operadores=getResources().getStringArray(R.array.combo_nombresOperadores);
        checkedItems=new boolean[operadores.length];
        tietOperadores.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(Nuevoreporte.this);
                builder.setTitle("Escoge los operadores");
                builder.setMultiChoiceItems(operadores, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            if(!operadoresSeleccionados.contains(which)){
                                operadoresSeleccionados.add(operadores[which]);
                            }
                            else{
                                operadoresSeleccionados.remove(which);
                            }
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                         for(String s:operadoresSeleccionados){
                             muestra=muestra+s+"\n";
                             tietOperadores.setText(muestra);
                             tietOperadores.clearFocus();
                         }
                    }
                });
                builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Reiniciar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     for(int i=0;i<checkedItems.length;i++){
                         checkedItems[i]=false;
                         operadoresSeleccionados.clear();
                         muestra="";
                     }
                    }
                });
                AlertDialog dialog=builder.create();

                if(hasFocus){
                    dialog.show();
                }
                else{
                    dialog.dismiss();
                }

            }
        });



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