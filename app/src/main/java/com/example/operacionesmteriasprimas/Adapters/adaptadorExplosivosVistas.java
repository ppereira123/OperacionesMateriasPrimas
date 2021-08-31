package com.example.operacionesmteriasprimas.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.example.operacionesmteriasprimas.Modelos.Explosivo;
import com.example.operacionesmteriasprimas.R;
import com.example.operacionesmteriasprimas.ui.Voladura.VistaExplosivos;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class adaptadorExplosivosVistas extends BaseAdapter {
    Context context;
    ArrayList<Explosivo> listItems;
    private LayoutInflater mInflater;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference("Explosivos");

    public adaptadorExplosivosVistas(Context context, ArrayList<Explosivo> listItems) {
        this.context = context;
        this.listItems = listItems;
    }
    @Override
    public int getCount() {
        return listItems.size();
    }
    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.adapter_list_vista_explosivos, parent, false);
        }
        mInflater=LayoutInflater.from(context);
        TextView txtTipoVistaExplosivo=convertView.findViewById(R.id.txtTipoVistaExplosivo);
        TextView editSaldo=convertView.findViewById(R.id.txtsaldoVistaExplosivo);
        TextView editPrecioCaja=convertView.findViewById(R.id.txtpreciounidadVistaExplosivo);
        CardView cvSaldoVistaExplosivo=convertView.findViewById(R.id.cvSaldoVistaExplosivo);
        CardView cvPUVistaExplosivo= convertView.findViewById(R.id.cvPUVistaExplosivo);
        ImageView imgbtnAgregar=convertView.findViewById(R.id.imgbtnAgregar);
        Explosivo currentItem= (Explosivo) getItem(position);
        txtTipoVistaExplosivo.setText(currentItem.getTipo().toString());
        editSaldo.setText(currentItem.getSaldo().toString());
        editPrecioCaja.setText(currentItem.getPreciounidad().toString());



        cvSaldoVistaExplosivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = mInflater.inflate(R.layout.text_input_edit_text, null);
                builder.setTitle("Editar saldo de "+currentItem.getTipo());
                EditText editText=view.findViewById(R.id.editText);
                editText.setHint("Saldo");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!editText.getText().toString().equals("")){
                            DatabaseReference ref2=ref.child(currentItem.getKey()).child("saldo");
                            ref2.setValue(Integer.parseInt(editText.getText().toString()));
                            currentItem.setSaldo(Integer.parseInt(editText.getText().toString()));
                            editSaldo.setText(editText.getText().toString());


                        }else{
                            Toast.makeText(context, "Debe escribir algo en el recuadro de texto", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setView(view);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        cvPUVistaExplosivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = mInflater.inflate(R.layout.text_input_edit_text, null);
                builder.setTitle("Editar Precio de Caja de "+currentItem.getTipo());
                EditText editText=view.findViewById(R.id.editText);
                editText.setHint("Precio de Caja");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!editText.getText().toString().equals("")){
                            DatabaseReference ref2=ref.child(currentItem.getKey()).child("preciounidad");
                            ref2.setValue(Double.parseDouble(editText.getText().toString()));
                            editPrecioCaja.setText(editText.getText().toString());
                            currentItem.setPreciounidad(Double.parseDouble(editText.getText().toString()));
                        }else{
                            Toast.makeText(context, "Debe escribir algo en el recuadro de texto", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setView(view);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        imgbtnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = mInflater.inflate(R.layout.text_input_edit_text, null);
                builder.setTitle("Aumentar saldo de "+currentItem.getTipo());
                EditText editText=view.findViewById(R.id.editText);
                editText.setHint("Ingresar saldo agregado");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!editText.getText().toString().equals("")){
                            DatabaseReference ref2=ref.child(currentItem.getKey()).child("saldo");
                            Integer saldo=Integer.parseInt(editText.getText().toString())+currentItem.getSaldo();
                            ref2.setValue(saldo);
                            currentItem.setSaldo(saldo);
                            editSaldo.setText(saldo.toString());


                        }else{
                            Toast.makeText(context, "Debe escribir algo en el recuadro de texto", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setView(view);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        return convertView;
    }
}
