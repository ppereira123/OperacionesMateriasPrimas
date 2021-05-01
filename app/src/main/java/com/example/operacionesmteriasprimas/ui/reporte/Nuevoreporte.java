package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.BuscadorAdapter;
import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.Modelos.UsersData;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    LinearLayout llreport;
    UsersData user;
    String id="";
    List<String> listaOperadores;
    final int REQUEST_CODE=2;
    AlertDialog dialog;
    BuscadorAdapter adapter;
    UsersData usersData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nuevo Reporte");
        setContentView(R.layout.activity_nuevoreporte);
        tietFecha=findViewById(R.id.tietFechaNuevoReporte);
        llreport=findViewById(R.id.llReportes);
        spinner=findViewById(R.id.spinnerTurno);
        txtTurno=findViewById(R.id.txtEscogerTurno);
        tietOperadores=findViewById(R.id.tietOperadores);

        usersData=new InternalStorage().cargarArchivo(context);
        getUser();
        configFecha(tietFecha);
        llenarSpinner(spinner,txtTurno);
        llenarLista();

    }

    private void getUser() {
        InternalStorage internalStorage= new InternalStorage();
        user=internalStorage.cargarArchivo(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void siguiente(View view) throws IOException {
        String error="";
        if(fecha.equals("")){
            error=error+"/Fecha";
        }

        if(turno.equals("Seleccione el turno")){
            error=error+"/Turno";
        }

        if(operadoresSeleccionados.size()<1){
            error=error+"/Seleccionar operadores";
        }

        if(error.equals("")){
            obtenerId();
            HashMap<String, Operador> operadorHashMap =generarOperadores();
            Reporte reporte= new Reporte(fecha,user.getName(),id,turno,false,false,operadorHashMap);
            Intent intent= new Intent(context,ListaOperadores.class);
            intent.putExtra("reporte",reporte);
            startActivityForResult(intent,REQUEST_CODE);
            new InternalStorage().guardarReporte(reporte,context);
            Toast.makeText(context, "Reporte creado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }

        else{
            String[] separado=error.split("/");
            String[] errores=error.split("/");

            List<String>muestra=new ArrayList<>();
            for(int i=1;i<errores.length;i++){
                muestra.add(errores[i]);
            }
            String dialogErrores=String.join(",",muestra);

            Snackbar snackbar=Snackbar.make(findViewById(R.id.viewNuevoReporte),"Falta completar:"+dialogErrores, BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void obtenerId() {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Reportes").child(user.getName());
        DatabaseReference refId=reference.push();
        id=refId.getKey();
    }

    private HashMap<String, Operador> generarOperadores() {
        HashMap<String,Operador> operadorHashMap= new HashMap<>();
        for(String s: operadoresSeleccionados){
            Operador operador= new Operador(new ArrayList<>(),s,false,new ArrayList<>());
            operadorHashMap.put(s,operador);
        }
        return operadorHashMap;
    }

    void llenarSpinner(Spinner spinner,TextView txtTurno){
        String[] turnos=getResources().getStringArray(R.array.combo_turnos);
        List<String> lturnos=new ArrayList<>();
        lturnos.add("Seleccione el turno");
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
                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.dismiss();
                            tietFecha.clearFocus();
                
                        }

                    }
                });

                if(hasFocus){
                    datePickerDialog.show();
                }
                else{
                    datePickerDialog.dismiss();
                    tietFecha.clearFocus();
                }

            }
        });
    }

    void llenarLista(){

        operadores=getResources().getStringArray(R.array.combo_nombresOperadores);
        listaOperadores=new ArrayList<>();
        for(String s: operadores){
        listaOperadores.add(s);
        }
        checkedItems=new boolean[operadores.length];
        checkActividades();
        tietOperadores.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                escogerOperadores(hasFocus);
            }

        });

    }

    private void checkActividades() {
        listaOperadores= new ArrayList<>();
        operadoresSeleccionados=usersData.getOperadores();

        for(String s: operadores){
            listaOperadores.add(s);
        }
        for(String s:operadoresSeleccionados){
            int index=listaOperadores.indexOf(s);
            checkedItems[index]=true;
        }

        for(String s:operadoresSeleccionados){
            muestra=muestra+s+"\n";
        }
        if(muestra.length()>0){
            muestra = muestra.substring(0, muestra.length() - 1);
        }
        tietOperadores.setText(muestra);
    }

    private void escogerOperadores(boolean mostrar) {
        AlertDialog.Builder builder= new AlertDialog.Builder(Nuevoreporte.this);
        builder.setTitle("Escoge los operadores");

        LayoutInflater mInflate= LayoutInflater.from(context);
        View view= mInflate.inflate(R.layout.res_buscador,null);
        final SearchView searchView= view.findViewById(R.id.searchRes);
        RecyclerView recyclerView = view.findViewById(R.id.rvRes);
        adapter= new BuscadorAdapter(listaOperadores,context,checkedItems,listaOperadores);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarOperador(newText,recyclerView);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter= new BuscadorAdapter(listaOperadores,context,checkedItems,listaOperadores);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
                return false;
            }
        });
        builder.setView(view);
        builder.setCancelable(false);

        //builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItems=adapter.getCheckedItems();
                operadoresSeleccionados=new ArrayList<>();
                for(int i=0;i<checkedItems.length;i++){
                    boolean b=checkedItems[i];
                    if(b) {
                        if (!operadoresSeleccionados.contains(operadores[i])){
                            operadoresSeleccionados.add(operadores[i]);
                        }
                        else{
                            operadoresSeleccionados.remove(i);
                        }
                    }
                }
                muestra="";
                for(String s:operadoresSeleccionados){
                    muestra=muestra+s+"\n";
                }
                tietOperadores.setText(muestra);
                tietOperadores.clearFocus();
            }
        });
        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                tietOperadores.clearFocus();
            }
        });

        builder.setNeutralButton("Reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                muestra="";
                tietOperadores.setText(muestra);
                operadoresSeleccionados.clear();
                for(int i=0;i<checkedItems.length;i++){
                    checkedItems[i]=false;
                    tietOperadores.clearFocus();

                }
            }
        });
        AlertDialog dialog=builder.create();
        if(mostrar) {
            dialog.show();
        }
        else{
            dialog.dismiss();
        }
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


    public void buscarOperador(String s,RecyclerView rvRes) {
        ArrayList<String> milista = new ArrayList<>();
        for (String obj : operadores) {
            if (obj.toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }
        }
        BuscadorAdapter adapter= new BuscadorAdapter(milista,context,checkedItems,listaOperadores);
        rvRes.setHasFixedSize(true);
        rvRes.setAdapter(adapter);
        rvRes.setLayoutManager(new LinearLayoutManager(context));
    }

}