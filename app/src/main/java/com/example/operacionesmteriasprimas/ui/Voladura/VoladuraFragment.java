package com.example.operacionesmteriasprimas.ui.Voladura;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.adaptadorListVoladuras;
import com.example.operacionesmteriasprimas.Modelos.Explosivo;
import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Voladura;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;


public class VoladuraFragment extends Fragment {
    FloatingActionButton fabNuevoReporteVoladura;
    View root;
    Button btnVerExplosivos;
    ArrayList<Voladura> voladuras;
    ListView listViewVoladura;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_voladura, container, false);
        context=root.getContext();
        listViewVoladura=root.findViewById(R.id.listViewVoladura);
        fabNuevoReporteVoladura=root.findViewById(R.id.fabNuevoReporteVoladura);
        btnVerExplosivos=root.findViewById(R.id.btnVerExplosivos);
        fabNuevoReporteVoladura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(root.getContext(),NuevaVoladura.class);
                startActivity(intent);
            }
        });
        btnVerExplosivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(root.getContext(),VistaExplosivos.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        cargarVoladuras();
        cargarExplosivos();

    }
    void  displayVoladura(){
        adaptadorListVoladuras adaptador_list_voladura=new adaptadorListVoladuras(context,voladuras);
        listViewVoladura.setAdapter(adaptador_list_voladura);

    }
    void  cargarVoladuras(){

        ArrayList<Voladura> voladuraArrayList=new ArrayList<>();
        /*
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Voladuras");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Voladura voladura=dataSnapshot.getValue(Voladura.class);
                        if(!voladuraArrayList.contains(voladura)){
                            voladuraArrayList.add(voladura);
                        }


                    }
                }

                voladuras=voladuraArrayList;
                displayVoladura();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

         */

        voladuraArrayList=new InternalStorage().cargarVoladura(context);
        voladuras=voladuraArrayList;
        displayVoladura();

    }
    void cargarExplosivos(){
        ArrayList<Explosivo> explosivoArrayList= new ArrayList<>();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Explosivos");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Explosivo explosivo=dataSnapshot.getValue(Explosivo.class);
                        if(!explosivoArrayList.contains(explosivo)){
                            explosivoArrayList.add(explosivo);
                        }
                    }
                    try {
                        new InternalStorage().guardarExplosivos(explosivoArrayList,context);
                        Toast.makeText(context, explosivoArrayList.toString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}