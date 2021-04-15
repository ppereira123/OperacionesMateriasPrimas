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

import com.example.operacionesmteriasprimas.Adapters.BaseAdapterReportes;
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
import java.util.Map;

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
    List<Reporte> reportes;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(ReporteModel.class);
        root = inflater.inflate(R.layout.fragment_reporte, container, false);
        listReporte=root.findViewById(R.id.listReporte);
        fab=root.findViewById(R.id.fabNuevoReporte);
        swipe=root.findViewById(R.id.swipeReporte);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarReportes();
            }
        });
        storage=new InternalStorage();
        cargarReportes();
        configFab();
        context=root.getContext();
        return root;
    }

    private void cargarReportes(){
        swipe.setRefreshing(true);
        hashReportes=storage.cargarReportes(root.getContext());
        reportes=listToHash(hashReportes);
        Toast.makeText(root.getContext(), "Cargando", Toast.LENGTH_SHORT).show();
        BaseAdapterReportes baseAdapterReportes= new BaseAdapterReportes(reportes,root.getContext(),this);
        listReporte.setAdapter(baseAdapterReportes);
        swipe.setRefreshing(false);
    }

    private List<Reporte >listToHash(HashMap<String, Reporte> hashReportes) {
        List<Reporte> reportes=new ArrayList<>();
        for(Map.Entry<String,Reporte> entry:hashReportes.entrySet()){
            reportes.add(entry.getValue());
        }
        return reportes;
    }

    private void configFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(root.getContext(),Nuevoreporte.class);
                startActivityForResult(intent,1);


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cargarReportes();
    }




}