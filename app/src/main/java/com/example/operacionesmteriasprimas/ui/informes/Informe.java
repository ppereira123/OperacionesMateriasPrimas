package com.example.operacionesmteriasprimas.ui.informes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.R;
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
import java.util.Date;
import java.util.List;

public class Informe extends Fragment {
    TextView name,date;
    EditText editSupervisora,editFechadesde,editFechahasta,edittipoDocumento;
    private FirebaseAuth mAuth;
    public List<String> supervisoreslist;
    Context context;
    String [] arryaSupervisores;
    boolean[] checkedItems;

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_informe, container, false);
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

        editSupervisora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            checkedItems[0]=false;
                            arryaSupervisores[0]="todo";

                            for(int i=1;i<supervisoreslist.size();i++){
                                arryaSupervisores[i]=supervisoreslist.get(i);
                            }







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

        return root;
    }
}