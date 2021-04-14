package com.example.operacionesmteriasprimas.ui.reporte;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReporteFragment extends Fragment {

    private ReporteModel galleryViewModel;
    private ListView listReporte;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipe;
    private InternalStorage storage;
    private View root;
    String fecha="";
    String turno="";
    Context context;

    HashMap<String, Reporte> hashReportes;
    List<Reporte> resportes;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(ReporteModel.class);
        root = inflater.inflate(R.layout.fragment_reporte, container, false);
        listReporte=root.findViewById(R.id.listReporte);
        fab=root.findViewById(R.id.fabNuevoReporte);
        swipe=root.findViewById(R.id.swipeReporte);
        storage=new InternalStorage();
        cargarReportes();
        configFab();
        context=root.getContext();





        return root;
    }

    private void cargarReportes(){
        Toast.makeText(root.getContext(), "Cargando", Toast.LENGTH_SHORT).show();
    }

    private void configFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater mInflater=LayoutInflater.from(root.getContext());;
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                builder.setTitle("Nuevo Reporte");
                View view = mInflater.inflate(R.layout.res_nuevo_reporte, null);
                TextInputEditText tietFecha,tietTurno;
                Spinner spinner;
                tietFecha=view.findViewById(R.id.tietFechaNuevoReporte);
                tietTurno=view.findViewById(R.id.tietFechaNuevoReporte2);
                spinner=view.findViewById(R.id.spinnerTurno);
                configFecha(tietFecha);
                llenarSpinner(spinner,tietTurno);

                tietFecha.setText(fecha);
                spinner.setSelection(0);

                builder.setView(view);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String error="";

                        if (fecha.equals("")) {
                            error=error+"/Fecha";
                        }

                        if(turno.equals("Seleccione turno")){
                            error=error+"/Turno";
                        }



                        if(error.equals("")){
                            Intent intent=new Intent(root.getContext(),Nuevoreporte.class);
                            startActivityForResult(intent,1);
                        }


                        else {
                            String[] errores=error.split("/");

                            List<String>muestra=new ArrayList<>();
                            for(int i=1;i<errores.length;i++){
                                muestra.add(errores[i]);
                            }
                            String dialogErrores=String.join(",",muestra);
                            Snackbar snackbar = Snackbar
                                    .make(root, "Falta ingresar: "+dialogErrores, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }
                });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();




            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cargarReportes();
    }

    void llenarSpinner(Spinner spinner,TextInputEditText tiet){
        String[] turnos=getResources().getStringArray(R.array.combo_turnos);
        List<String> lturnos=new ArrayList<>();
        lturnos.add("Selecciones turno");
        for(String s:turnos){
            lturnos.add(s);
        }
        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,lturnos));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                turno= (String) parent.getItemAtPosition(position);
                tiet.setText(turno);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        fecha=date;
                        tietFecha.setText(fecha);

                    }
                },year,month,day);



                if(hasFocus){
                    datePickerDialog.show();
                }
                else{
                    datePickerDialog.dismiss();
                }

            }
        });
    }
}