package com.example.operacionesmteriasprimas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.ui.reporte.ListaActividadOperadores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterRecyclerActividades extends RecyclerView.Adapter<AdapterRecyclerActividades.ViewHolder> {
    private List<String> mdata;
    private LayoutInflater mInflater;
    private Context context;
    HashMap<String, Operador> valores;
    private ArrayList<Integer> mSectionPositions;
    private ListaActividadOperadores instance;

    public AdapterRecyclerActividades(List<String> mdata, LayoutInflater mInflater, Context context, HashMap<String, Operador> valores, ArrayList<Integer> mSectionPositions, ListaActividadOperadores instance) {
        this.mdata = mdata;
        this.mInflater = mInflater;
        this.context = context;
        this.valores = valores;
        this.mSectionPositions = mSectionPositions;
        this.instance = instance;
    }

    @NonNull
    @Override
    public AdapterRecyclerActividades.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerActividades.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
