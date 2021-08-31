package com.example.operacionesmteriasprimas.ui.Voladura;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.adaptesListExplosivo;
import com.example.operacionesmteriasprimas.Modelos.Explosivo;
import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Utility;
import com.example.operacionesmteriasprimas.Modelos.Voladura;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class NuevaVoladura extends AppCompatActivity {
    TextInputEditText editLongitudPerforacionVoladura, editFechaNuevaVoladura,editEdipoVoladura,
            editEspaciamientoVoladura,editAlturaBancoVoladura,editSobrePerforacionVoladura,editCodigoVoladura,
            editDiametroVoladura,editNumeroPerforacionesVoladura,editNumeroFilasVoladura,editPerforacionesxFilaVoladura;
    AutoCompleteTextView autocompleteExplosivoVoladura;
    ListView listExplosivosVoladura;
    Context context=this;
    Resources res;
    private LayoutInflater mInflater;
    String[] nombre_explosivos;
    ArrayList<Explosivo> explosivos=new ArrayList<>();
    String codigoVoladura="";
    TextInputLayout textInputlayoutCVoladura;
    TextView txtenunciadoList;
    Button btnGuardar;
    LinearLayout nuevaVoladura;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_voladura);
        nuevaVoladura=findViewById(R.id.nuevaVoladura);
        txtenunciadoList=findViewById(R.id.txtenunciadoList);
        textInputlayoutCVoladura=findViewById(R.id.textInputlayoutCVoladura);
        editLongitudPerforacionVoladura=findViewById(R.id.editLongitudPerforacionVoladura);
        editFechaNuevaVoladura=findViewById(R.id.editFechaNuevaVoladura);
        editEdipoVoladura=findViewById(R.id.editEdipoVoladura);
        editEspaciamientoVoladura=findViewById(R.id. editEspaciamientoVoladura);
        editAlturaBancoVoladura=findViewById(R.id.editAlturaBancoVoladura);
        editSobrePerforacionVoladura=findViewById(R.id.editSobrePerforacionVoladura);
        editCodigoVoladura=findViewById(R.id.editCodigoVoladura);
        editCodigoVoladura.setKeyListener(null);
        editDiametroVoladura=findViewById(R.id.editDiametroVoladura);
        editNumeroPerforacionesVoladura=findViewById(R.id.editNumeroPerforacionesVoladura);
        editNumeroFilasVoladura=findViewById(R.id.editNumeroFilasVoladura);
        editPerforacionesxFilaVoladura=findViewById(R.id.editPerforacionesxFilaVoladura);
        autocompleteExplosivoVoladura=findViewById(R.id.autocompleteExplosivoVoladura);
        btnGuardar=findViewById(R.id.btnGuardar);
        listExplosivosVoladura=findViewById(R.id.listExplosivosVoladura);
        mInflater=LayoutInflater.from(context);
        configFecha(editFechaNuevaVoladura);
        autocompleteExplosivoVoladura.setKeyListener(null);
        res = context.getResources();
        nombre_explosivos = res.getStringArray(R.array.combo_explosivos);
        ArrayList<Explosivo> explosivoArrayList=new InternalStorage().cargarExplosivos(context);
        ArrayList<String> listanombreExplosivos=new ArrayList<>();
        for(Explosivo explosivo:explosivoArrayList){
            listanombreExplosivos.add(explosivo.getTipo().toString());
        }
        ArrayAdapter<String> adaptertipoExplosivos=new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item,listanombreExplosivos);
        autocompleteExplosivoVoladura.setAdapter(adaptertipoExplosivos);
        editCodigoVoladura.clearFocus();
        configCodigoVoladura();
        editCodigoVoladura.clearFocus();
        autocompleteExplosivoVoladura.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                añadirExplosivo(position);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!comprobar()){
                    String autor=new InternalStorage().cargarArchivo(context).getName();
                    Double longitudPerforacion=Double.parseDouble(editLongitudPerforacionVoladura.getText().toString());
                    Double espaciamiento=Double.parseDouble(editEspaciamientoVoladura.getText().toString());
                    Double alturabanco=Double.parseDouble(editAlturaBancoVoladura.getText().toString());
                    Double sobreperforacion=Double.parseDouble(editSobrePerforacionVoladura.getText().toString());
                    String fecha=editFechaNuevaVoladura.getText().toString();
                    String codigo=editCodigoVoladura.getText().toString();
                    String equipo=editEdipoVoladura.getText().toString();
                    Integer numeroperforacion=Integer.parseInt(editNumeroPerforacionesVoladura.getText().toString());
                    Integer numerofila=Integer.parseInt(editNumeroFilasVoladura.getText().toString());
                    Integer perforacionesporfila=Integer.parseInt(editPerforacionesxFilaVoladura.getText().toString());
                    Double diametro=Double.parseDouble(editDiametroVoladura.getText().toString());
                    Voladura voladura=new Voladura(autor,codigo,fecha,equipo,diametro,espaciamiento,alturabanco,
                            sobreperforacion,longitudPerforacion,numeroperforacion,numerofila,perforacionesporfila,explosivos,false);
                    try {
                        new InternalStorage().guardarVoladura(voladura,context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });

    }
    void hayexplosivos(){
        if(explosivos.size()>0){
            txtenunciadoList.setVisibility(View.VISIBLE);
        }
    }
    void configCodigoVoladura(){
        editCodigoVoladura.setKeyListener(null);


        editCodigoVoladura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCodigoVoladura.clearFocus();
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Escriba codigo de voladura");
                View view= LayoutInflater.from(context).inflate(R.layout.dialog_codigo_voladura,null);
                EditText editcodigoV1,editcodigoV2,editcodigoV3;
                editcodigoV1=view.findViewById(R.id.editcodigoV1);
                editcodigoV2=view.findViewById(R.id.editcodigoV2);
                editcodigoV3=view.findViewById(R.id.editcodigoV3);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String error="",codigo1="",codigo2="",codigo3="";
                        codigo1=editcodigoV1.getText().toString();
                        codigo2=editcodigoV2.getText().toString();
                        codigo3=editcodigoV3.getText().toString();
                        if(codigo1.equals("")){
                            error=error+" codigo 1";
                            int color= editcodigoV1.getDrawingCacheBackgroundColor();
                            editcodigoV1.setBackgroundColor(Color.parseColor("#FFCDD2"));
                            TimerTask tarea= new TimerTask() {
                                @Override
                                public void run() {
                                    editcodigoV1.setBackgroundColor(Color.parseColor("#FFFFFF"));

                                }
                            };
                            Timer tiempo= new Timer();
                            tiempo.schedule(tarea,1000);
                        }
                        if(codigo2.equals("")){
                            error=error+" codigo 2";
                            int color= editcodigoV2.getDrawingCacheBackgroundColor();
                            editcodigoV2.setBackgroundColor(Color.parseColor("#FFCDD2"));
                            TimerTask tarea= new TimerTask() {
                                @Override
                                public void run() {
                                    editcodigoV2.setBackgroundColor(Color.parseColor("#FFFFFF"));

                                }
                            };
                            Timer tiempo= new Timer();
                            tiempo.schedule(tarea,1000);
                        }
                        if(codigo3.equals("")){
                            error=error+" codigo 3";
                            int color= editcodigoV2.getDrawingCacheBackgroundColor();
                            editcodigoV2.setBackgroundColor(Color.parseColor("#FFCDD2"));
                            TimerTask tarea= new TimerTask() {
                                @Override
                                public void run() {
                                    editcodigoV2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                }
                            };
                            Timer tiempo= new Timer();
                            tiempo.schedule(tarea,1000);
                        }
                        if(error.equals("")){
                            codigoVoladura=codigo1+"-"+codigo2+"-"+codigo3;
                            editCodigoVoladura.setText(codigoVoladura);

                        }else {
                            Toast.makeText(context, "Falta de completar: "+error, Toast.LENGTH_SHORT).show();
                        }
                        editCodigoVoladura.clearFocus();
                        textInputlayoutCVoladura.clearFocus();
                    }
                });
                builder.setView(view);
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
                editCodigoVoladura.clearFocus();
                textInputlayoutCVoladura.clearFocus();
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
                        tietFecha.setText(date);
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
    void  displayExplosivo(ArrayList<Explosivo> list){
        adaptesListExplosivo adaptesListExplosivo=new adaptesListExplosivo(context,list);
        listExplosivosVoladura.setAdapter(adaptesListExplosivo);
        Utility.setListViewHeightBasedOnChildren(listExplosivosVoladura);
        hayexplosivos();
    }
    void añadirExplosivo(int posicion){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = mInflater.inflate(R.layout.adapter_dialog_explosivo, null);
        builder.setTitle(nombre_explosivos[posicion]);
        EditText editTextEgreso,editTextingreso;
        editTextEgreso=view.findViewById(R.id.editTextEgreso);
        editTextingreso=view.findViewById(R.id.editTextingreso);
        Button btnGuardarDialogExplosivo=view.findViewById(R.id.btnGuardarDialogExplosivo);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
        btnGuardarDialogExplosivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEgreso.getText().toString().equals("")){
                    Toast.makeText(context, "No ha ingresado ningun valor en egreso", Toast.LENGTH_SHORT).show();
                }else {
                    Integer ingreso=0;
                    Integer egreso=0;
                    if(!editTextingreso.getText().toString().equals("")){
                        ingreso=Integer.parseInt(editTextingreso.getText().toString());
                    }
                    if(!editTextEgreso.getText().toString().equals("")){
                        egreso=Integer.parseInt(editTextEgreso.getText().toString());
                    }
                    Explosivo explosivo=new Explosivo(nombre_explosivos[posicion],egreso, ingreso);
                    explosivos.add(explosivo);
                    displayExplosivo(explosivos);
                    alert.dismiss();
                }

            }
        });
    }
    Boolean comprobar(){
        Boolean falta=false;
        String numeroperforacion="",numerofila="",perforacionesporfila="", espaciamiento="",
                alturabanco="", sobreperforacion="",longitudPerforacion="", diametro;
        String codigo="",fecha="",equipo="",error="";
        diametro=editDiametroVoladura.getText().toString();
        longitudPerforacion=editLongitudPerforacionVoladura.getText().toString();
        espaciamiento=editEspaciamientoVoladura.getText().toString();
        alturabanco=editAlturaBancoVoladura.getText().toString();
        sobreperforacion=editSobrePerforacionVoladura.getText().toString();
        fecha=editFechaNuevaVoladura.getText().toString();
        codigo=editCodigoVoladura.getText().toString();
        equipo=editEdipoVoladura.getText().toString();
        numeroperforacion=editNumeroPerforacionesVoladura.getText().toString();
        numerofila=editNumeroFilasVoladura.getText().toString();
        perforacionesporfila=editPerforacionesxFilaVoladura.getText().toString();

        if(diametro.equals("")){

            if(error.equals("")){
                error="diametro";

            }else {
                error=error+", diametro";
            }
/*
            editLongitudPerforacionVoladura.setBackgroundColor(Color.parseColor("#FFCDD2"));
            TimerTask tarea= new TimerTask() {
                @Override
                public void run() {
                    editLongitudPerforacionVoladura.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            };
            Timer tiempo= new Timer();
            tiempo.schedule(tarea,1000);
 */
        }
        if(longitudPerforacion.equals("")){

            if(error.equals("")){
                error="longitud de perforación";

            }else {
                error=error+", longitud de perforación";
            }
/*
            editLongitudPerforacionVoladura.setBackgroundColor(Color.parseColor("#FFCDD2"));
            TimerTask tarea= new TimerTask() {
                @Override
                public void run() {
                    editLongitudPerforacionVoladura.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            };
            Timer tiempo= new Timer();
            tiempo.schedule(tarea,1000);
 */
        }
        if(sobreperforacion.equals("")){
            if(error.equals("")){
                error="sobre perforación";
            }else {
                error=error+", sobre perforación";
            }
        }
        if(alturabanco.equals("")){

            if(error.equals("")){
                error="altura de banco";

            }else {
                error=error+", altura de banco";
            }

        }
        if(espaciamiento.equals("")){

            if(error.equals("")){
                error="espaciamiento";

            }else {
                error=error+", espaciamiento";
            }

        }
        if(perforacionesporfila.equals("")){

            if(error.equals("")){
                error="perforaciones por fila";

            }else {
                error=error+", perforaciones por fila";
            }

        }
        if(numerofila.equals("")){

            if(error.equals("")){
                error="número de fila";

            }else {
                error=error+", número de fila";
            }

        }

        if(numeroperforacion.equals("")){

            if(error.equals("")){
                error="número de perforación";

            }else {
                error=error+", número de perforación";
            }

        }
        if(equipo.equals("")){

            if(error.equals("")){
                error="equipo";

            }else {
                error=error+", equipo";
            }

        }
        if(fecha.equals("")){

            if(error.equals("")){
                error="fecha";

            }else {
                error=error+", fecha";
            }

        }
        if(codigo.equals("")){

            if(error.equals("")){
                error="codigo";

            }else {
                error=error+", codigo";
            }

        }
        if(explosivos.size()<=0){
            if(error.equals("")){
                error="explosivos";

            }else {
                error=error+", explosivos";
            }
        }
        if(!error.equals("")){
            Snackbar snackbar = Snackbar.make(nuevaVoladura,"Falta de completar: "+error, Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
            falta=true;
        }

        return falta;





    }
}