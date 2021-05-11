package com.example.operacionesmteriasprimas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.BuscadorAdapter;

import com.example.operacionesmteriasprimas.Adapters.adaptadorInformeporOperador;

import com.example.operacionesmteriasprimas.Adapters.adaptadorVistaHoras;
import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.Modelos.sumaInformeOperador;
import com.example.operacionesmteriasprimas.Modelos.sumas;

import com.example.operacionesmteriasprimas.ui.reporte.Nuevoreporte;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.operacionesmteriasprimas.Adapters.adaptadorInformeporOperador.convertDpToPx;
import static com.example.operacionesmteriasprimas.ui.informes.Informe.diferenciaDias;

public class InformeVista extends AppCompatActivity {
    ListView listactividades;
    Context context=this;
    LinearLayout tituloactividades;
    List<Reporte> listareportes;
    Button btnAgregarOperadores;
    TextView txtprincipales,txtextra,txtfechadelreporte,txttotalhoras;
    Double horasextra=0.0;
    Double horasprincipales=0.0;
    String fechadesde,fechahasta,tipoinforme;
    String muestra="";

    List<String> supervisores=new ArrayList<>();
    List<String> operadoresSeleccionados=new ArrayList<>();
    List<String> listaOperadores;
    String[] operadores;
    boolean[] checkedItems;
    AlertDialog dialog;
    BuscadorAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vista Informe");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_vista);
        listactividades=findViewById(R.id.listActividades);
        cargarReportes();
        txtprincipales=findViewById(R.id.txtprincipales);
        txtextra=findViewById(R.id.txtextras);
        txtfechadelreporte=findViewById(R.id.txtinformeporfecha);
        txttotalhoras=findViewById(R.id.txtTotalhoras);

        btnAgregarOperadores=findViewById(R.id.btnAgregarOperadoresInforme);
        tituloactividades=findViewById(R.id.tituloactividades);

        fechadesde=getIntent().getStringExtra("Fechadesde");
        fechahasta=getIntent().getStringExtra("Fechahasta");
        supervisores= (List<String>) getIntent().getSerializableExtra("Supervisores");
        tipoinforme=getIntent().getStringExtra("TipoInforme");
        txtfechadelreporte.setText(fechadesde+"-"+fechahasta);
        if(tipoinforme.equals("Por operador")){

            btnAgregarOperadores.setVisibility(View.VISIBLE);
            tituloactividades.setVisibility(View.GONE);
        }






    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        return false;
    }

    void cargarReportes(){
        List<String> valores= new ArrayList<>();
        listareportes=new ArrayList<>();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Reportes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        GenericTypeIndicator<Reporte> t = new GenericTypeIndicator<Reporte>() {};
                        Reporte m = ds.getValue(t);


                        if(supervisores.contains("Todos")){
                            try {
                                if(diferenciaDias(m.getFecha(),fechadesde)>=0&&diferenciaDias(m.getFecha(),fechahasta)<=0){
                                    if(!listareportes.contains(m)){
                                        listareportes.add(m);
                                    }

                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }else {
                            if(supervisores.contains(m.getSupervisor())){
                                try {
                                    if(diferenciaDias(m.getFecha(),fechadesde)>=0&&diferenciaDias(m.getFecha(),fechahasta)<=0){
                                        if(!listareportes.contains(m)){
                                            listareportes.add(m);
                                        }

                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                }
                else{
                    Toast.makeText(context, "No existe referencia", Toast.LENGTH_SHORT).show();
                }

                if(tipoinforme.equals("Por operador")){

                    btnAgregarOperadores.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            llenarLista();


                        }
                    });

                }else if(tipoinforme.equals("Generar por actividad")){
                    List<sumas> lista= (List<sumas>) GetData(listareportes);
                    adaptadorVistaHoras adapter = new adaptadorVistaHoras(context,lista);

                    listactividades.setAdapter(adapter);
                    listactividades.setDividerHeight(4);
                    listactividades.setBackgroundColor(getResources().getColor(R.color.white));
                    

                    for(sumas suma:GetData(listareportes)){
                        if (suma.getActividad().equals("Extracción")||suma.getActividad().equals("Esteril")){
                            horasprincipales=horasprincipales+suma.getHoras();
                        }
                        else {
                            horasextra=horasextra+suma.getHoras();
                        }
                    }
                    txtprincipales.setText(String.valueOf(horasprincipales));
                    txtextra.setText(String.valueOf(horasextra));
                    txttotalhoras.setText(String.valueOf(horasextra+horasprincipales));
                }else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private static List<sumas> GetData(List<Reporte> listareportes) {
        //HashMap<String, Double> actividadeshoras = new HashMap<String, Double>();
        List<sumas> lista=new ArrayList<sumas>();
        for (Reporte reporte:listareportes){
            for(Operador operador:listReporteToHash(reporte.getOperadores())){

                for (String actividad:operador.getNombreActividades()){
                    if(existeactividad(lista,actividad)==-1) {


                        sumas suma = new sumas(actividad, operador.getActividades().get(operador.getNombreActividades().indexOf(actividad)));
                        lista.add(suma);
                    }else {
                        double sumar=operador.getActividades().get(operador.getNombreActividades().indexOf(actividad))+lista.get(existeactividad(lista,actividad)).getHoras();
                        sumas suma = new sumas(actividad, sumar);

                        lista.set(existeactividad(lista,actividad),suma);

                    }
                }
            }
        }
        return lista;
    }

    public static   List<sumaInformeOperador> GetDataInformeporoperador(List<Reporte> listareportes, List<String> operadoresSeleccionados){

        List<sumaInformeOperador> lista=new ArrayList<>();
        for(String operador: operadoresSeleccionados){
            List<sumas> listasuma=new ArrayList<sumas>();
            for (Reporte reporte: listareportes){
                List<Operador> operadores=listReporteToHash(reporte.getOperadores());
                int existe=existeOperadorenList(operadores,operador);
                if (!(existe==-1)){
                    for (String actividad:operadores.get(existe).getNombreActividades()){
                        if(existeactividad(listasuma,actividad)==-1) {
                            sumas suma = new sumas(actividad, operadores.get(existe).getActividades().get(operadores.get(existe).getNombreActividades().indexOf(actividad)));
                            listasuma.add(suma);
                        }else {
                            double sumar=operadores.get(existe).getActividades().get(operadores.get(existe).getNombreActividades().indexOf(actividad))+listasuma.get(existeactividad(listasuma,actividad)).getHoras();
                            sumas suma = new sumas(actividad, sumar);

                            listasuma.set(existeactividad(listasuma,actividad),suma);
                        }
                    }

                }

            }
            sumaInformeOperador info= new sumaInformeOperador(operador,listasuma);
            lista.add(info);
        }
        return lista;
    }

    public static int existeOperadorenList(List<Operador> operadores, String operador){
        List<String> contenido=new ArrayList<>();

        for(Operador x:operadores){
            contenido.add(x.getNombre());
        }
        int posicion=contenido.indexOf(operador);
        return posicion;
    }


    void llenarLista(){

        operadores=getResources().getStringArray(R.array.combo_nombresOperadores);
        listaOperadores=new ArrayList<>();
        for(String s: operadores){
            listaOperadores.add(s);
        }
        checkedItems=new boolean[operadores.length];
        checkActividades();
        escogerOperadores();


    }

    private void checkActividades() {
        listaOperadores = new ArrayList<>();

        for (String s : operadores) {
            listaOperadores.add(s);
        }
        for (String s : operadoresSeleccionados) {
            int index = listaOperadores.indexOf(s);
            checkedItems[index] = true;
        }
    }



        private void escogerOperadores (){
        AlertDialog.Builder builder = new AlertDialog.Builder(InformeVista.this);
        builder.setTitle("Escoge los operadores");

        LayoutInflater mInflate = LayoutInflater.from(context);
        View view = mInflate.inflate(R.layout.res_buscador, null);
        final SearchView searchView = view.findViewById(R.id.searchRes);
        RecyclerView recyclerView = view.findViewById(R.id.rvRes);
        adapter = new BuscadorAdapter(listaOperadores, context, checkedItems, listaOperadores);
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
                buscarOperador(newText, recyclerView);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter = new BuscadorAdapter(listaOperadores, context, checkedItems, listaOperadores);
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
                checkedItems = adapter.getCheckedItems();
                operadoresSeleccionados = new ArrayList<>();
                for (int i = 0; i < checkedItems.length; i++) {
                    boolean b = checkedItems[i];
                    if (b) {
                        if (!operadoresSeleccionados.contains(operadores[i])) {
                            operadoresSeleccionados.add(operadores[i]);
                        } else {
                            operadoresSeleccionados.remove(i);
                        }
                    }
                }
                muestra = "";
                for (String s : operadoresSeleccionados) {
                    muestra = muestra + s + "\n";
                }
                adaptadorInformeporOperador adapterporOperador = new adaptadorInformeporOperador(context, GetDataInformeporoperador(listareportes, operadoresSeleccionados));
                listactividades.setAdapter(adapterporOperador);
                ColorDrawable sage = new ColorDrawable(Color.parseColor("#FFFFFF"));
                listactividades.setDivider(sage);
                for (sumaInformeOperador sumaporoperador : GetDataInformeporoperador(listareportes, operadoresSeleccionados)) {
                    for (sumas suma : sumaporoperador.getListaactividades()) {
                        if (suma.getActividad().equals("Extracción") || suma.getActividad().equals("Esteril")) {
                            horasprincipales = horasprincipales + suma.getHoras();
                        } else {
                            horasextra = horasextra + suma.getHoras();
                        }
                    }

                    txtprincipales.setText(String.valueOf(horasprincipales));
                    txtextra.setText(String.valueOf(horasextra));
                    txttotalhoras.setText(String.valueOf(horasextra + horasprincipales));

                    BigDecimal bd = new BigDecimal(horasprincipales).setScale(0, RoundingMode.HALF_UP);
                    double val1 = bd.doubleValue();
                    BigDecimal bd2 = new BigDecimal(horasextra).setScale(0, RoundingMode.HALF_UP);
                    double val2 = bd2.doubleValue();
                    double total=horasextra + horasprincipales;
                    BigDecimal bd3 = new BigDecimal(total).setScale(0, RoundingMode.HALF_UP);
                    double val3 = bd3.doubleValue();
                    txtprincipales.setText(String.valueOf(val1));
                    txtextra.setText(String.valueOf(val2));
                    txttotalhoras.setText(String.valueOf(val3));

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
                muestra = "";

                operadoresSeleccionados.clear();
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;


                }
            }
        });
        AlertDialog dialog = builder.create();

            dialog.show();

    }



        public void buscarOperador (String s, RecyclerView rvRes){
        ArrayList<String> milista = new ArrayList<>();
        for (String obj : operadores) {
            if (obj.toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }
        }
        BuscadorAdapter adapter = new BuscadorAdapter(milista, context, checkedItems, listaOperadores);
        rvRes.setHasFixedSize(true);
        rvRes.setAdapter(adapter);
        rvRes.setLayoutManager(new LinearLayoutManager(context));
    }


        public static int existeactividad (List < sumas > list, String actividad){
        List<String> contenido = new ArrayList<>();


        for (sumas x : list) {
            contenido.add(x.getActividad());
        }
        int posicion = contenido.indexOf(actividad);
        return posicion;


    }

        public static List<Operador> listReporteToHash (HashMap < String, Operador > hashoperador){
        List<Operador> lista = new ArrayList<>();
        for (Map.Entry<String, Operador> m : hashoperador.entrySet()) {
            lista.add(m.getValue());
        }
        return lista;
    }

        private  List<sumas> listsumaToHash (HashMap < String, Double > hashhoras){
        List<sumas> lista = new ArrayList<>();
        for (Map.Entry<String, Double> m : hashhoras.entrySet()) {

            sumas suma = new sumas(m.getKey(), m.getValue());
            lista.add(suma);
        }
        return lista;
    }



        }