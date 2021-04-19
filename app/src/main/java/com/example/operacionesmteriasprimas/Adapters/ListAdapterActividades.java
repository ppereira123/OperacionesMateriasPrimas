package com.example.operacionesmteriasprimas.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.operacionesmteriasprimas.R;
import com.example.operacionesmteriasprimas.ui.reporte.ListaActividadOperadores;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class ListAdapterActividades extends RecyclerView.Adapter<ListAdapterActividades.ViewHolder> {
    private List<String> mdata;
    private Context context;
    List <Double> valores;
    int posicion;
    private ListaActividadOperadores instance;
    Double horas;
    LayoutInflater mInflater;





    public ListAdapterActividades(List<String> mdata, Context context,List<Double> valores, int posicion, ListaActividadOperadores instance){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mdata =mdata;
        this.posicion=posicion;
        this.instance = instance;
        this.valores = valores;
    }

    public ListAdapterActividades(){

    }
    @NonNull
    @Override
    public ListAdapterActividades
            .ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= mInflater.inflate(R.layout.adaptador_reportes_diarios,null);
        return new ListAdapterActividades.ViewHolder(view);
    }


    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterActividades.ViewHolder holder, int position) {
        holder.binData(mdata.get(position));
    }


    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public List<Double> getValores(){
        return valores;
    }

    public void setValores(List<Double> valores){
        this.valores=valores;
    }






    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEnunciado;
        TextInputEditText tietHoras;
        Button btnSiguient;
        Button btnAnterior;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            txtEnunciado=view.findViewById(R.id.txtNombreActividad);
            tietHoras=view.findViewById(R.id.editTextHoras);
            btnSiguient=view.findViewById(R.id.btnSiguiente);
            btnAnterior=view.findViewById(R.id.btnAnterio);
        }

        public void binData(String item) {
            if(valores.get(posicion)>0.0){
                tietHoras.setText(String.valueOf(valores.get(posicion)));
            }

            else{
                tietHoras.setText("");
            }

            txtEnunciado.setText(item);
            tietHoras.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>0){
                        horas=Double.parseDouble(s.toString());
                    }
                    else{
                        horas=0.0;

                    }
                    if(horas>0.0){
                        valores.remove(posicion);
                        valores.add(posicion,horas);
                        instance.iniciarChips();

                    }
                    else{
                        valores.remove(posicion);
                        valores.add(posicion,0.0);
                        instance.iniciarChips();
                    }
                }
            });

            btnSiguient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean siguiente=instance.siguiente(item);
                    if(siguiente){
                        btnSiguient.setEnabled(true);
                        btnSiguient.setTextColor(context.getColor(R.color.purple_200));
                    }
                    else{
                        btnSiguient.setEnabled(false);
                        btnSiguient.setTextColor(context.getColor(R.color.tab));
                    }
                }
            });

            btnAnterior.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean siguiente=instance.anterior(item);
                    if(siguiente){
                        btnAnterior.setEnabled(true);
                        btnAnterior.setTextColor(context.getColor(R.color.purple_200));
                    }
                    else{
                        btnAnterior.setEnabled(false);
                        btnAnterior.setTextColor(context.getColor(R.color.tab));
                    }
                }
            });

        }

    }
}

