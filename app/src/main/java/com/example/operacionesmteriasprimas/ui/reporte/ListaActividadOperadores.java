package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.ListAdapterActividades;
import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListaActividadOperadores extends AppCompatActivity {
    TextView txtNombreOperador,txtFecha,txtActividadesPendientes;
    Button btnAgregarActividad;
    ChipGroup chipGroup;
    RecyclerView rvActividades;
    Reporte reporte=null;
    Operador operador=null;
    LinearLayout lLModificar,layout;
    //Actividades del objeto operador
    List<String> actividades;
    //Actividad que se muestra en adapter
    List<String> actividad;
    int cont;
    //Lista de todas las actividades
    String[] ActividadesNoSeleccionadas;

    List<Double> valores;
    Context context=this;
    ListAdapterActividades adapter;
    boolean[] checkedItems;
    List<String> allActividades;
    View root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividad_operadores);

        txtNombreOperador=findViewById(R.id.txtoperador);
        txtFecha=findViewById(R.id.txtFechaoperador);
        txtActividadesPendientes=findViewById(R.id.txtOperadoresPendientes);
        btnAgregarActividad=findViewById(R.id.btnAgregarActividad);
        lLModificar=findViewById(R.id.lLModificarParametros);
        layout=findViewById(R.id.llactividadoperadores);
        rvActividades=findViewById(R.id.rvActividades);
        chipGroup=findViewById(R.id.chipGroupActividades);
        reporte=(Reporte)getIntent().getSerializableExtra("reporte");
        operador=(Operador)getIntent().getSerializableExtra("operador");
        cargarDatos();
        generarChips();
        iniciarChips();


        btnAgregarActividad.setOnClickListener(new View.OnClickListener() {

            String[] actividadesM = context.getResources().getStringArray(R.array.combo_tiposOperaciones);

            @Override
            public void onClick(View v) {
                obtenerNoEscogidos(actividadesM);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Escoge los operadores");
                builder.setMultiChoiceItems(ActividadesNoSeleccionadas, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
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
                            String estaActividad= ActividadesNoSeleccionadas[cont];
                            if(seleccionado){
                                if(!actividades.contains(estaActividad)){
                                    actividades.add(estaActividad);
                                    valores.add(0.0);
                                }
                            }
                            else{
                                if(actividades.contains(estaActividad)){
                                    valores.remove(String.valueOf(actividades.indexOf(estaActividad)));
                                    actividades.remove(actividades.indexOf(estaActividad));

                                    Toast.makeText(ListaActividadOperadores.this, String.valueOf(valores.size()), Toast.LENGTH_SHORT).show();

                                }
                            }
                            cont++;
                        }
                        operador.setNombreActividades(actividades);
                        operador.setActividades(valores);
                        cargarLista();
                        generarChips();
                        iniciarChips();

                    }
                });
                builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();





            }
        });

    }

    void obtenerNoEscogidos(String[] actividadesM){
        List<String> llenar= new ArrayList<>();
        int cont=0;
        for(String s:actividadesM){
            if(!actividades.contains(s)){
            llenar.add(s);}
        }
        ActividadesNoSeleccionadas= new String[llenar.size()];
        ActividadesNoSeleccionadas= llenar.toArray(ActividadesNoSeleccionadas);
         checkedItems = new boolean[llenar.size()];
    }

    public void iniciarChips() {
        int cont=0;
        for(int i=0;i<valores.size();i++){
            if(valores.get(i)>0){
                marcarChips(cont);
            }
            else{
                desmarcarChips(cont);
            }

            cont++;
        }
    }

    public void guardar(View view){
        //Obtengo las actividades realizadas
        valores=adapter.getValores();
        //Agrego las actividades al operador
        operador.setActividades(valores);
        operador.setNombreActividades(actividades);
        if(operador.getActividadesPendientes()<=0){
            if(!(valores.size()<=0)) {
                operador.setCompleto(true);
            }
            else{
                operador.setCompleto(false);
            }
        }
        else{
            operador.setCompleto(false);
        }
        //Actualizo el operador en el reporte
        HashMap<String,Operador> operadorHashMap=reporte.getOperadores();
        operadorHashMap.put(operador.getNombre(),operador);
        reporte.setOperadores(operadorHashMap);
        try {
            new InternalStorage().guardarReporte(reporte,context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent= new Intent();
        intent.putExtra("reporte",reporte);
        setResult(3,intent);
        finish();
    }

    private void generarChips() {

        cont=1;
        chipGroup.removeAllViews();
        for(String s:actividades){
            Chip chip= new Chip(context);
            chip.setTextSize(18);
            chip.setText("Actividad "+cont);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String valor=chip.getText().toString();
                    String[] separado=valor.split(" ");
                    int num=Integer.parseInt(separado[1]);
                    num=num-1;
                    actividades.remove(num);
                    valores.remove(num);
                    generarChips();
                    iniciarChips();
                }
            });
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actividad.clear();
                    String nombreChip=chip.getText().toString();
                    String[] sep=nombreChip.split(" ");
                    int posicion=Integer.parseInt(sep[1])-1;
                    actividad.add(actividades.get(posicion));
                    valores=adapter.getValores();
                    cargarLista();
                }
            });
            chipGroup.addView(chip);
            cont++;
        }

    }

    public void marcarChips(Integer posicion){
        valores=adapter.getValores();
        Chip chip=(Chip) chipGroup.getChildAt(posicion);
        chip.setChipBackgroundColorResource(R.color.purple_200);
    }

    public void desmarcarChips(Integer posicion){
        valores=adapter.getValores();
        Chip chip=(Chip) chipGroup.getChildAt(posicion);
        chip.setChipBackgroundColorResource(R.color.tab);
    }

    public int getPosicionChip(Chip chip){
        String nombreChip=chip.getText().toString();
        String[] sep=nombreChip.split(" ");
        int posicion=Integer.parseInt(sep[1])-1;
        return posicion;
    }

    private void cargarDatos() {
        actividades=operador.getNombreActividades();
        actividad=new ArrayList<>();
        txtFecha.setText(reporte.getFecha());
        txtNombreOperador.setText(operador.getNombre());
        txtActividadesPendientes.setText("Actividades por ingresar: "+operador.getActividadesPendientes());
        actividad.add(actividades.get(0));
        valores=operador.getActividades();
        cargarLista();

    }

    void cargarLista(){
        adapter= new ListAdapterActividades(actividad,context,valores,actividades.indexOf(actividad.get(0)),this);
        rvActividades.setHasFixedSize(true);
        rvActividades.setLayoutManager(new LinearLayoutManager(context));
        rvActividades.setAdapter(adapter);
    }

    public void eliminarDeLista(String actividad){

    }

    public boolean anterior(String item){
        int index=actividades.indexOf(item)-1;
        if(index>=0){
            actividad.clear();
            actividad.add(actividades.get(index));
            cargarLista();
            return true;
        }
        else{return false; }
    }

    public boolean siguiente(String item){
        int maxIndex=actividades.size()-1;
        int index=actividades.indexOf(item)+1;
        if(index<=maxIndex){
            actividad.clear();
            actividad.add(actividades.get(index));
            cargarLista();
            return  true;
        }
        else{
            return false;
        }
    }


}