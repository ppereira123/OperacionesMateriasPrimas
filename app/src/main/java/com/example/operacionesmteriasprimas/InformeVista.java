package com.example.operacionesmteriasprimas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.adaptadorVistaHoras;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.Modelos.sumas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.operacionesmteriasprimas.ui.informes.Informe.diferenciaDias;

public class InformeVista extends AppCompatActivity {
    ListView listactividades;
    Context context=this;
    List<Reporte> listareportes;
    TextView txtprincipales,txtextra,txtfechadelreporte,txttotalhoras;
    Double horasextra=0.0;
    Double horasprincipales=0.0;
    String fechadesde,fechahasta;
    List<String> supervisores=new ArrayList<>();



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
        fechadesde=getIntent().getStringExtra("Fechadesde");
        fechahasta=getIntent().getStringExtra("Fechahasta");
        supervisores= (List<String>) getIntent().getSerializableExtra("Supervisores");
        txtfechadelreporte.setText(fechadesde+"-"+fechahasta);




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
                    Toast.makeText(context, listareportes.toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "No existe referencia", Toast.LENGTH_SHORT).show();
                }

                adaptadorVistaHoras adapter = new adaptadorVistaHoras(context,GetData(listareportes));
                listactividades.setAdapter(adapter);
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

private List<sumas> GetData(List<Reporte> listareportes) {
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
    private int existeactividad(List<sumas> list, String actividad){
        List<String> contenido=new ArrayList<>();


        for(sumas x:list){
            contenido.add(x.getActividad());
        }
        int posicion=contenido.indexOf(actividad);
        return posicion;


    }
    private List<Operador> listReporteToHash(HashMap<String, Operador> hashoperador) {
        List<Operador> lista= new ArrayList<>();
        for(Map.Entry<String,Operador> m:hashoperador.entrySet()){
            lista.add(m.getValue());
        }
        return lista;
    }
    private List<sumas> listsumaToHash(HashMap<String, Double> hashhoras) {
        List<sumas> lista= new ArrayList<>();
        for(Map.Entry<String,Double> m:hashhoras.entrySet()){

            sumas suma=new sumas(m.getKey(),m.getValue());
            lista.add(suma);
        }
        return lista;
    }

}