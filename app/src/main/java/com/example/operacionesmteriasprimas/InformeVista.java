package com.example.operacionesmteriasprimas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformeVista extends AppCompatActivity {
    ListView listactividades;
    Context context=this;
    List<Reporte> listareportes;
    TextView txtprincipales,txtextra;
    Double horasextra=0.0;
    Double horasprincipales=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_vista);
        listactividades=findViewById(R.id.listActividades);
        cargarReportes();

        txtprincipales=findViewById(R.id.txtprincipales);
        txtextra=findViewById(R.id.txtextras);





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