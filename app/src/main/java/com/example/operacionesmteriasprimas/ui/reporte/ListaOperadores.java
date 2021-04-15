package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button btnmodificarOperador, btnsubir, btncargar;
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
        btnmodificarOperador= findViewById(R.id.btnmodificarOperadores);

        reporte=(Reporte)getIntent().getSerializableExtra("reporte");
        txtInspector.setText(reporte.getSupervisor());
        txtFecha.setText(reporte.getFecha());
        txtTurno.setText(reporte.getTurno());
        txtPendientes.setText("Operadores incompletos: "+String.valueOf(reporte.getRestantes()));
        hashOperadores=reporte.getOperadores();
        cargarOperadores();
        btnmodificarOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnmodificarOperador();

            }
        });

    }
     private  void setBtnmodificarOperador(){
         List<String> operadoresSeleccionados= (ArrayList) hashToLisSIMPLEt(hashOperadores);
         String [] operadores=getResources().getStringArray(R.array.combo_nombresOperadores);
         boolean[] checkedItems=new boolean[operadores.length];
         AlertDialog.Builder builder= new AlertDialog.Builder(this);
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

                 for(String s: operadoresSeleccionados){
                     Operador operador= new Operador(new HashMap<Integer,Double>(),s,false,new ArrayList<>());
                     hashOperadores.put(s,operador);
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
                 }
             }
         });
         AlertDialog dialog=builder.create();
             dialog.show();



     }

    private void cargarOperadores() {
        operadores=new ArrayList<>();
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
    private List<String> hashToLisSIMPLEt(HashMap<String, Operador> hashOperadores) {
        List<String> operadores= new ArrayList<>();
        for(Map.Entry<String,Operador> entry:hashOperadores.entrySet()){
            operadores.add(entry.getKey());
        }
        return operadores;
    }
}