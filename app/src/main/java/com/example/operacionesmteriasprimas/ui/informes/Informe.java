package com.example.operacionesmteriasprimas.ui.informes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.textfield.TextInputEditText;
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
    TextView name,date;
    EditText editSupervisora,editFechadesde,editFechahasta,edittipoDocumento;
    private FirebaseAuth mAuth;
    List<String> supervisoreslist=new ArrayList<>();
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
        name=root.findViewById(R.id.txtnameUsuario);
        date=root.findViewById(R.id.txtFechaInforme);
        editSupervisora=root.findViewById(R.id.editSupervisora);
        editFechadesde=root.findViewById(R.id.editFechadesde);
        editFechahasta=root.findViewById(R.id.editFechahasta);
        edittipoDocumento=root.findViewById(R.id.edittipoDocumento);
        editSupervisora.setKeyListener(null);
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
        supervisoreslist.add("Todos");
        editSupervisora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseDatabase database= FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("Reportes");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot ds:snapshot.child("supervisor").getChildren()){
                                if(ds.exists()){
                                    Toast.makeText(root.getContext(), ds.getValue().toString(), Toast.LENGTH_SHORT).show();
                                    if(supervisoreslist.contains(ds.getValue().toString())==false){
                                        supervisoreslist.add(ds.getValue().toString());
                                        Toast.makeText(root.getContext(), ds.getValue().toString(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else {
                                    Toast.makeText(root.getContext(), "no existe", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                        else{
                            Toast.makeText(root.getContext(), "No existe referencia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                supervisoreslist.add("todo");

                checkedItems=new boolean[1];
                arryaSupervisores=new String[1];
                checkedItems[0]=false;
                arryaSupervisores[0]="todo";

                /*
                int i = 0;
                while (i < supervisoreslist.size() - 1)
                {
                    arryaSupervisores[i]=supervisoreslist.get(i);
                    i++;
                }
*/



                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                builder.setTitle("Escoger supervisores para el informe");
                builder.setMultiChoiceItems(arryaSupervisores, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });





        return root;
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