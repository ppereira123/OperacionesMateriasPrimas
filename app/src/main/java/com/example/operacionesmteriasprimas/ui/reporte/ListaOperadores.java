package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.BaseAdapterOperadores;
import com.example.operacionesmteriasprimas.Adapters.adaptadorlistaOperadores;
import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaOperadores extends AppCompatActivity {
    TextView txtInspector,txtTurno,txtFecha,txtPendientes;
    Button btnmodificarOperador, btnsubir, btncargar;
    ListView listOperadores;
    Reporte reporte= null;
    List<Operador> operadores;
    HashMap<String,Operador> hashOperadores;
    Context context;
    List<String> operadoresSeleccionados;
    List<String> listAllOperadores;
    String [] arryaOperadores;
    boolean[] checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lista Operadores");
        context=this;
        setContentView(R.layout.activity_lista_operadores);
        txtInspector=findViewById(R.id.txtInspector);
        txtTurno=findViewById(R.id.txtTurno);
        txtFecha=findViewById(R.id.txtFechaoperador);
        txtPendientes=findViewById(R.id.txtOperadoresPendientes);
        listOperadores=findViewById(R.id.listOperadores);
        btnmodificarOperador= findViewById(R.id.btnmodificarOperadores);

        reporte=(Reporte)getIntent().getSerializableExtra("reporte");
        cargarDatos();
        cargarOperadores();
        btnmodificarOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnmodificarOperador();

            }
        });

    }

    void cargarDatos(){
        txtInspector.setText(reporte.getSupervisor());
        txtFecha.setText(reporte.getFecha());
        txtTurno.setText(reporte.getTurno());
        txtPendientes.setText("Operadores incompletos: "+String.valueOf(reporte.getRestantes()));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        guardarOperadores();
    }

    private  void setBtnmodificarOperador(){

         arryaOperadores=getResources().getStringArray(R.array.combo_nombresOperadores);
         checkedItems=new boolean[arryaOperadores.length];
         checkActividades();
         AlertDialog.Builder builder= new AlertDialog.Builder(this);
         builder.setTitle("Escoge los operadores");
         builder.setMultiChoiceItems(arryaOperadores, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which, boolean isChecked) {

             }
         });
         builder.setCancelable(false);
         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 int cont=0;
                 for(boolean seleccionado:checkedItems){
                     if(seleccionado){
                         if(!operadoresSeleccionados.contains(listAllOperadores.get(cont))){
                             Operador operador= new Operador(new HashMap<>(),listAllOperadores.get(cont),false,new ArrayList<>());
                             operadores.add(operador);
                             operadoresSeleccionados.add(listAllOperadores.get(cont));
                             hashOperadores.put(operador.getNombre(),operador);
                         }
                     }
                     else {
                         if(operadoresSeleccionados.contains(listAllOperadores.get(cont))){
                             operadores.remove(operadoresSeleccionados.indexOf(listAllOperadores.get(cont)));
                             hashOperadores.remove(listAllOperadores.get(cont));
                             operadoresSeleccionados.remove(operadoresSeleccionados.indexOf(listAllOperadores.get(cont)));
                         }

                     }
                     cont++;

                 }
                 reporte.setOperadores(hashOperadores);
                 cargarOperadores();
                 cargarDatos();

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
                 }
             }
         });
         AlertDialog dialog=builder.create();
             dialog.show();



     }

    private void checkActividades() {
        listAllOperadores= new ArrayList<>();
        operadoresSeleccionados=new ArrayList<>();
        for(String s: arryaOperadores){
            listAllOperadores.add(s);
        }
        for(Operador operador:operadores){
                int index=listAllOperadores.indexOf(operador.getNombre());
                checkedItems[index]=true;
                operadoresSeleccionados.add(operador.getNombre());
        }
    }

    private void cargarOperadores() {
        hashOperadores=reporte.getOperadores();
        operadores=new ArrayList<>();
        operadores=hashToList(hashOperadores);
        adaptadorlistaOperadores adaptadorlistaOperadores=new adaptadorlistaOperadores(context, operadores,reporte,this);
        listOperadores.setAdapter(adaptadorlistaOperadores);
    }

    public void guardarOperadores(View view){
        reporte.setOperadores(hashOperadores);
        if(reporte.getRestantes()==0){
            reporte.setCompletado(true);
        }

        else{
            reporte.setCompletado(false);
        }
        try {
            new InternalStorage().guardarReporte(reporte,context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setResult(2);
        finish();

    }

    public void subir(View view){
        int cont=0;
        for(Operador operador:operadores){
            if(operador.isCompleto()){
                cont++;
            }
        }
        if(cont==hashOperadores.size()){
            FirebaseDatabase database= FirebaseDatabase.getInstance();
            DatabaseReference refReporte= database.getReference("Reportes").child(reporte.getId());
            reporte.setOperadores(hashOperadores);
            reporte.setSubido(true);
            try {
                new InternalStorage().guardarReporte(reporte,context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            refReporte.setValue(reporte).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Reporte guardado correctamente", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
        else{
            Snackbar snackbar= Snackbar.make(view,"No se puede guardar, de completar todos los operadores", BaseTransientBottomBar.LENGTH_SHORT);
            snackbar.show();
        }


    }

    public void guardarOperadores(){
        if(reporte.getRestantes()==0){
            reporte.setCompletado(true);
        }

        else{
            reporte.setCompletado(false);
        }
        reporte.setOperadores(hashOperadores);
        try {
            new InternalStorage().guardarReporte(reporte,context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setResult(2);

    }

    private List<Operador> hashToList(HashMap<String, Operador> hashOperadores) {
        List<Operador> operadores= new ArrayList<>();
        for(Map.Entry<String,Operador> entry:hashOperadores.entrySet()){
            operadores.add(entry.getValue());
        }
        return operadores;
    }

    private List<String> hashToLisSIMPLEt(HashMap<String, Operador> hashOperadores) {
        List<String> operadores= new ArrayList<>();
        for(Map.Entry<String,Operador> entry:hashOperadores.entrySet()){
            operadores.add(entry.getKey());
        }
        return operadores;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==3){
            reporte= (Reporte) data.getSerializableExtra("reporte");
            cargarOperadores();
            cargarDatos();
        }
    }

    public void eliminarOperador(Operador operador){
        hashOperadores.remove(operador.getNombre());
        reporte.setOperadores(hashOperadores);
        try {
            new InternalStorage().guardarReporte(reporte,context);
            cargarOperadores();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}