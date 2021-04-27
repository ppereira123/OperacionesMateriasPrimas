package com.example.operacionesmteriasprimas.ui.informes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Informe extends Fragment {
    Button limpiar, buscar;
    TextView name,date;
    String tipoDocumento,fechadesde,fechahasta;
    EditText editFechadesde,editFechahasta,editSupervisora;
    TextInputLayout edittipoDocumento, textfieldSupervisora;
    String[] listatipodocumentos;
    AutoCompleteTextView autoCompleteTextViewSpinnnerDopcumentos;
    String fechaview="";

    private FirebaseAuth mAuth;
    public List<String> supervisoreslist;
    List<String> slectSupervisors=new ArrayList<>();

    Context context;
    String [] arryaSupervisores;
    boolean[] checkedItems;
    String fecha="";

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_informe, container, false);
        context=root.getContext();
        tipoDocumento="";
        fechahasta="";
        fechadesde="";
        limpiar=root.findViewById(R.id.btnLimpiar);
        buscar=root.findViewById(R.id.btnBuscarInforme);
        name=root.findViewById(R.id.txtnameUsuario);
        date=root.findViewById(R.id.txtFechaInforme);
        editSupervisora=root.findViewById(R.id.editSupervisora);
        editFechadesde=root.findViewById(R.id.editFechadesde);
        editFechahasta=root.findViewById(R.id.editFechahasta);
        edittipoDocumento=root.findViewById(R.id.edittipoDocumento);
        textfieldSupervisora=root.findViewById(R.id.textfieldtSupervisora);
        autoCompleteTextViewSpinnnerDopcumentos=root.findViewById(R.id.autocompleteSpinnerDocumentos);

        editSupervisora.setKeyListener(null);
        editFechahasta.setKeyListener(null);
        editFechadesde.setKeyListener(null);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Establecer campos
        name.setText(currentUser.getDisplayName());
        Date d=new Date();
        SimpleDateFormat fecc=new SimpleDateFormat("d/MM/yyyy");
        String fechacComplString = fecc.format(d);
        date.setText(fechacComplString);
        configFecha(editFechadesde);
        configFecha(editFechahasta);
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar();
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });



        editSupervisora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> supervisoresSeleccionados=new ArrayList<>();
                supervisoreslist=new ArrayList<>();
                supervisoreslist.add("Todos");
                FirebaseDatabase database= FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("Reportes");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                DataSnapshot ds=dataSnapshot.child("supervisor");
                                if(ds.exists()){
                                    String supervisor=ds.getValue().toString();
                                    if(!supervisoreslist.contains(supervisor)){
                                        supervisoreslist.add(supervisor);
                                    }
                                }
                            }
                            checkedItems=new boolean[supervisoreslist.size()];
                            arryaSupervisores=new String[supervisoreslist.size()];

                            for(int i=0;i<supervisoreslist.size();i=i+1){
                                arryaSupervisores[i]=supervisoreslist.get(i);
                                checkedItems[i]=false;
                            }


                            AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                            builder.setTitle("Escoger supervisores para el informe");
                            builder.setMultiChoiceItems(arryaSupervisores, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if(arryaSupervisores[which].equals("Todos")){

                                        final AlertDialog alertDialog = (AlertDialog) dialog;
                                        final ListView alertDialogList = alertDialog.getListView();

                                        for (int position = 0; position < alertDialogList.getChildCount(); position++)
                                        {
                                            if (position != which) {
                                                if(isChecked){
                                                    alertDialogList.getChildAt(position).setVisibility(View.GONE);
                                                    supervisoresSeleccionados.clear();
                                                    supervisoresSeleccionados.add(arryaSupervisores[which]);
                                                }else {
                                                    supervisoresSeleccionados.clear();
                                                    alertDialogList.getChildAt(position).setVisibility(View.VISIBLE);
                                                }



                                            }
                                        }
                                    }else {
                                        if(supervisoresSeleccionados.contains("Todos")){
                                            supervisoresSeleccionados.remove(supervisoresSeleccionados.indexOf("Todos"));
                                        }

                                        if(supervisoresSeleccionados.contains(arryaSupervisores[which])){
                                            supervisoresSeleccionados.remove(supervisoresSeleccionados.indexOf(arryaSupervisores[which]));

                                        }
                                        else {
                                            supervisoresSeleccionados.add(arryaSupervisores[which]);
                                        }

                                    }




                                }
                            });
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    slectSupervisors.clear();

                                    if(!supervisoresSeleccionados.contains("Todos")){
                                        int cont=0;

                                        for(boolean seleccionado:checkedItems){
                                            if(seleccionado){
                                                slectSupervisors.add(arryaSupervisores[cont]);

                                            }
                                            cont++;
                                        }

                                    }else {
                                        slectSupervisors.add("Todos");


                                    }
                                    String muestra="";
                                    for(String s:slectSupervisors){
                                        muestra=muestra+s+"\n";
                                    }
                                    editSupervisora.setText(muestra);


                                }
                            });
                            builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog=builder.create();
                            dialog.show();
                        }
                        else{
                            Toast.makeText(root.getContext(), "No existe referencia", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(root.getContext(), String.valueOf(supervisoreslist.size()), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        listatipodocumentos=getResources().getStringArray(R.array.combo_tiposDocumentos);
        ArrayAdapter<String> adapterTipos=new ArrayAdapter<>(context, R.layout.dropdow_item,listatipodocumentos);
        autoCompleteTextViewSpinnnerDopcumentos.setAdapter(adapterTipos);
        tipoDocumento=autoCompleteTextViewSpinnnerDopcumentos.getText().toString();







        return root;
    }

    private void limpiar(){
        autoCompleteTextViewSpinnnerDopcumentos.setText("");
        editFechadesde.setText("");
        editFechahasta.setText("");
        editSupervisora.setText("");
    }
    private  void buscar(){
        tipoDocumento=autoCompleteTextViewSpinnnerDopcumentos.getText().toString();
        fechadesde=editFechadesde.getText().toString();
        fechahasta=editFechahasta.getText().toString();

        if(tipoDocumento.equals("")||fechahasta.equals("")||fechadesde.equals("")||slectSupervisors.size()<1){
            Toast.makeText(context, "Falta completar parametro", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "comenzando busqueda", Toast.LENGTH_SHORT).show();
            if(tipoDocumento.equals("Visual")){
                Intent intent= new Intent();
            }
        }

    }



    private void configFecha(EditText tietFecha) {



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
                        fechaview=fecha;

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
}