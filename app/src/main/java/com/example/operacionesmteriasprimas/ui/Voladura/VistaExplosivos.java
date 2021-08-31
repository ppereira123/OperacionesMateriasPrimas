package com.example.operacionesmteriasprimas.ui.Voladura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Adapters.adaptadorExplosivosVistas;
import com.example.operacionesmteriasprimas.Modelos.Explosivo;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VistaExplosivos extends AppCompatActivity {
    FloatingActionButton fabNuevoExplosivo;
    ListView listExplosivos;
    Context context=this;
    private LayoutInflater mInflater;
    String tipo="",precio="",saldo="";
    ArrayList<Explosivo> listaexplosivos=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_explosivos);
        mInflater= LayoutInflater.from(context);
        fabNuevoExplosivo=findViewById(R.id.fabNuevoExplosivo);
        listExplosivos=findViewById(R.id.listExposivos);
        fabNuevoExplosivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                añadirExplosivo();

            }
        });
        cargarExplosivos();
        listExplosivos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("¿Desea eliminar este tipo de explosivo?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database= FirebaseDatabase.getInstance();
                        DatabaseReference ref=database.getReference("Explosivos");
                        DatabaseReference ref2=ref.child(listaexplosivos.get(position).getKey());
                        ref2.keepSynced(true);
                        ref2.removeValue();
                        cargarExplosivos();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

    }

    void  cargarExplosivos(){

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Explosivos");
        ref.keepSynced(true);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<Explosivo> listItems=new ArrayList<>();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Explosivo explosivo=ds.getValue(Explosivo.class);
                    explosivo.setKey(ds.getKey());
                        listItems.add(explosivo);

                }
                listaexplosivos=listItems;
                display(listItems);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    void display(ArrayList<Explosivo> listItems){
        adaptadorExplosivosVistas adaptador= new adaptadorExplosivosVistas(context,listItems);
        listExplosivos.setAdapter(adaptador);
    }
    void añadirExplosivo(){


        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Explosivos").push();
        ref.keepSynced(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Agregar un nuevo exploivo");
        builder.setCancelable(false);
        View view = mInflater.inflate(R.layout.adaptador_dialog_vista_explosivo, null);
        EditText editTipoDialog,editSaldoDialog,editPrecioCajaDialog;
        editTipoDialog=view.findViewById(R.id.editTipoDialog);
        editSaldoDialog=view.findViewById(R.id.editSaldoDialog);
        editPrecioCajaDialog=view.findViewById(R.id.editPrecioCajaDialog);
        Button btnSubirDialogExplosivo,btnCancelarDialogExplosivo;
        btnSubirDialogExplosivo=view.findViewById(R.id.btnSubirDialogExplosivo);
        btnCancelarDialogExplosivo=view.findViewById(R.id.btnCancelarDialogExplosivo);
        builder.setView(view);
        AlertDialog alert = builder.create();
        alert.show();
        btnSubirDialogExplosivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo=editTipoDialog.getText().toString();
                precio=editPrecioCajaDialog.getText().toString();
                saldo=editSaldoDialog.getText().toString();
                Boolean existe=false;
                for(Explosivo explosivo:listaexplosivos){
                    if(explosivo.getTipo().equals(tipo)){
                        existe=true;
                    }
                }
                if(existe){
                    Toast.makeText(context, "Ya existe este tipo de explosivo", Toast.LENGTH_SHORT).show();
                }else {
                    if(tipo.equals("")||precio.equals("")||saldo.equals("")){
                        Toast.makeText(context, "Debe llenar todos los datos", Toast.LENGTH_SHORT).show();
                    }else {

                        Explosivo explosivo= new Explosivo(
                                ref.getKey(),
                                tipo,
                                Double.parseDouble(precio),
                                Integer.parseInt(saldo));
                        ref.setValue(explosivo);
                        cargarExplosivos();
                        alert.dismiss();
                    }
                }

            }
        });
        btnCancelarDialogExplosivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }

        });
    }

}