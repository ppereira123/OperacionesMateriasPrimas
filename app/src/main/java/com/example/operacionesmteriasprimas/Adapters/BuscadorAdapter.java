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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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



public class BuscadorAdapter extends RecyclerView.Adapter<BuscadorAdapter.ViewHolder> {
    private List<String> mdata;
    private Context context;
    LayoutInflater mInflater;
    boolean [] checkedItems;
    List<String> allOperadores;

    public BuscadorAdapter(List<String> mdata, Context context,boolean[] checkedItems,List<String> allOperadores){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mdata =mdata;
        this.checkedItems=checkedItems;
        this.allOperadores=allOperadores;
    }

    public BuscadorAdapter(){

    }

    public boolean[] getCheckedItems() {
        return checkedItems;
    }

    @NonNull
    @Override
    public BuscadorAdapter
            .ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= mInflater.inflate(R.layout.adaptador_nombre_operadores,null);
        return new BuscadorAdapter.ViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull BuscadorAdapter.ViewHolder holder, int position) {
        holder.binData(mdata.get(position),position);
    }


    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            checkBox=view.findViewById(R.id.checkBoxOperador);
        }

        public void binData(String item,int posicion) {
            checkBox.setText(item);
            int index=allOperadores.indexOf(item);
            boolean valor=checkedItems[index];
            checkBox.setChecked(valor);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkedItems[index]=!valor;
                }
            });

        }

    }
}

