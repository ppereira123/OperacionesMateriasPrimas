package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.BuscadorAdapter;
import com.example.operacionesmteriasprimas.Adapters.adaptadorHorasOperadores;
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
    List<String> listaOperadores;

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
        hashOperadores=reporte.getOperadores();
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
         listaOperadores=new ArrayList<String>();
         for(String s: arryaOperadores){
             listaOperadores.add(s);
         }
         checkedItems=new boolean[arryaOperadores.length];
         checkActividades();
         escogerOperadores(true);



     }

    private void escogerOperadores(boolean mostrar) {
        AlertDialog.Builder builder= new AlertDialog.Builder(ListaOperadores.this);
        builder.setTitle("Escoge los operadores");

        LayoutInflater mInflate= LayoutInflater.from(context);
        View view= mInflate.inflate(R.layout.res_buscador,null);
        final SearchView searchView= view.findViewById(R.id.searchRes);
        RecyclerView recyclerView = view.findViewById(R.id.rvRes);
        BuscadorAdapter adapter= new BuscadorAdapter(listaOperadores,context,checkedItems,listAllOperadores);
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
                BuscadorAdapter adapter= new BuscadorAdapter(listaOperadores,context,checkedItems,listAllOperadores);
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
                        if (!operadoresSeleccionados.contains(arryaOperadores[i])){
                            operadoresSeleccionados.add(arryaOperadores[i]);
                        }
                        else{
                            operadoresSeleccionados.remove(i);
                        }
                    }
                }
                HashMap<String,Operador> newOperadores= new HashMap<>();
                for(String s: operadoresSeleccionados){
                    Operador operador=null;
                    if(!hashOperadores.containsKey(s)){
                        operador = new Operador(new ArrayList<Double>(), s, false, new ArrayList<>());
                    }
                    else{
                        operador= hashOperadores.get(s);
                    }
                    newOperadores.put(s, operador);
                }
                hashOperadores=newOperadores;
                cargarOperadores();

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
                operadoresSeleccionados.clear();
                for(int i=0;i<checkedItems.length;i++){
                    checkedItems[i]=false;
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

    public void buscarOperador(String s,RecyclerView rvRes) {
        ArrayList<String> milista = new ArrayList<>();
        for (String obj : arryaOperadores) {
            if (obj.toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }
        }
        BuscadorAdapter adapter= new BuscadorAdapter(milista,context,checkedItems,listAllOperadores);
        rvRes.setHasFixedSize(true);
        rvRes.setAdapter(adapter);
        rvRes.setLayoutManager(new LinearLayoutManager(context));
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
            LayoutInflater mInflater= LayoutInflater.from(context);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View viewdialog = mInflater.inflate(R.layout.dialogo_adaptador_horas_actividades, null);
            ListView listahoras= viewdialog.findViewById(R.id.listHoras);
            adaptadorHorasOperadores adapter = new adaptadorHorasOperadores(context,operadores);
            listahoras.setAdapter(adapter);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
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

                        }
                    });
                    finish();


                }
            });
            builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setView(viewdialog);

            AlertDialog dialog = builder.create();

            dialog.show();












        }
        else{
            Snackbar snackbar= Snackbar.make(view,"No se puede guardar sin completar todos los operadores", BaseTransientBottomBar.LENGTH_SHORT);
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