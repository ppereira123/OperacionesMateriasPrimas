package com.example.operacionesmteriasprimas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
import com.google.firebase.database.core.Repo;

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
        cargardatos();
        adaptadorVistaHoras adapter = new adaptadorVistaHoras(context,GetData());
        listactividades.setAdapter(adapter);


    }
    void cargardatos(){
        List<Reporte> reportes=new ArrayList<>();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Reportes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    GenericTypeIndicator<Reporte> t = new GenericTypeIndicator<Reporte>() {};
                    Reporte m = ds.getValue(t);
                    reportes.add(m);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        listareportes=reportes;



    }
    private List<sumas> GetData() {
        HashMap<String, Double> actividadeshoras = new HashMap<String, Double>();

        cargardatos();
        for (Reporte reporte:listareportes){
            for(Operador operador:listReporteToHash(reporte.getOperadores())){
                int i=0;
                for (String actividad:operador.getNombreActividades()){
                    if (actividadeshoras.containsKey(actividad)){
                        double horas=actividadeshoras.get(actividad)+operador.getActividades().get(i);
                        actividadeshoras.put(actividad,horas);
                        Log.d("myTag", "si");
                    }else {
                        actividadeshoras.put(actividad,actividadeshoras.get(actividad));
                        Log.d("myTag", "no");
                    }
                    i=0+i;
                }
            }

        }
        return listsumaToHash(actividadeshoras);
    }
    private List<Operador> listReporteToHash(HashMap<String, Operador> hashoperador) {
        List<Operador> lista= new ArrayList<>();
        for(Map.Entry m:hashoperador.entrySet()){
            lista.add((Operador) m.getValue());
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