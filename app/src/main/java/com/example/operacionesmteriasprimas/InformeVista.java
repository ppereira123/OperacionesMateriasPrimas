package com.example.operacionesmteriasprimas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformeVista extends AppCompatActivity {
    ListView listactividades;
    Context context=this;
    List<Reporte> listareportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_vista);
        listactividades=findViewById(R.id.listActividades);
        cargarReportes();



    }

    void cargarReportes(){
        List<String> valores= new ArrayList<>();
        listareportes=new ArrayList<>();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Reportes");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        GenericTypeIndicator<Reporte> t = new GenericTypeIndicator<Reporte>() {};
                        Reporte m = ds.getValue(t);
                        if(!listareportes.contains(m)){
                            listareportes.add(m);
                        }
                    }
                    Toast.makeText(context, listareportes.toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "No existe referencia", Toast.LENGTH_SHORT).show();
                }

                adaptadorVistaHoras adapter = new adaptadorVistaHoras(context,GetData(listareportes));
                listactividades.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    void cargardatos(){
        List<Reporte> reportes=new ArrayList<>();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Reportes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Log.d("myTag", "entra a la base de datos");
                    GenericTypeIndicator<Reporte> t1 = new GenericTypeIndicator<Reporte>() {};
                    Reporte m = ds.getValue(t1);
                    Toast.makeText(context, m.toString(), Toast.LENGTH_SHORT).show();
                    reportes.add(m);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        Log.d("myTag", reportes.toString());

        listareportes=reportes;

    }
private List<sumas> GetData(List<Reporte> listareportes) {
        //HashMap<String, Double> actividadeshoras = new HashMap<String, Double>();
        List<sumas> lista=new ArrayList<sumas>();
        for (Reporte reporte:listareportes){
            for(Operador operador:listReporteToHash(reporte.getOperadores())){
                int i=0;
                for (String actividad:operador.getNombreActividades()){
                    if(existeactividad(lista,actividad)==-1){
                        sumas suma=new sumas(actividad,operador.getActividades().get(i));
                        lista.add(suma);

                    }
                    else {

                        Double horas= (Double) (operador.getActividades().get(i)+lista.get(existeactividad(lista,actividad)).getHoras());
                        sumas suma=new sumas(actividad,horas);
                        lista.add(suma);

                    }
                    i=i++;


                }



                /*
                int i=0;
                for (String actividad:operador.getNombreActividades()){
                    if (actividadeshoras.containsKey(actividad)){
                        double horas=actividadeshoras.get(actividad)+operador.getActividades().get(i);
                        actividadeshoras.put(actividad,horas);

                    }else {

                        actividadeshoras.put(actividad, operador.getActividades().get(i));

                    }
                    i=1+i;
                }

                 */
            }


        }



        return lista;
    }
    private int existeactividad(List<sumas> list, String actividad){
        int posicion=-1;
        int i=0;
        for(sumas x:list){
            if(x.getActividad().equals(actividad)){
                posicion=i;
            }
            i=i++;
        }
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