package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.Modelos.UsersData;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Nuevoreporte extends AppCompatActivity {

    SearchView buscar;
    ListView lvNombreOperadores;
    String[] operadores;
    boolean[] checkedItems;
    List<String> operadoresSeleccionados;
    TextInputEditText tietFecha,tietOperadores;
    String muestra="";
    TextView txtTurno;
    Spinner spinner;
    Context context=this;
    String turno="";
    String fecha="";
    LinearLayout llreport;
    UsersData user;
    String id="";
    final int REQUEST_CODE=2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nuevo Reporte");
        setContentView(R.layout.activity_nuevoreporte);
        tietFecha=findViewById(R.id.tietFechaNuevoReporte);
        llreport=findViewById(R.id.llReportes);
        spinner=findViewById(R.id.spinnerTurno);
        txtTurno=findViewById(R.id.txtEscogerTurno);
        tietOperadores=findViewById(R.id.tietOperadores);
        getUser();
        configFecha(tietFecha);
        llenarSpinner(spinner,txtTurno);
        llenarLista();

    }

    private void getUser() {
        InternalStorage internalStorage= new InternalStorage();
        user=internalStorage.cargarArchivo(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void siguiente(View view) throws IOException {
        String error="";
        if(fecha.equals("")){
            error=error+"/Fecha";
        }

        if(turno.equals("Seleccione el turno")){
            error=error+"/Turno";
        }

        if(operadoresSeleccionados.size()<1){
            error=error+"/Seleccionar operadores";
        }

        if(error.equals("")){
            obtenerId();
            HashMap<String, Operador> operadorHashMap =generarOperadores();
            Reporte reporte= new Reporte(fecha,user.getName(),id,turno,false,false,operadorHashMap);
            Intent intent= new Intent(context,ListaOperadores.class);
            intent.putExtra("reporte",reporte);
            startActivityForResult(intent,REQUEST_CODE);
            new InternalStorage().guardarReporte(reporte,context);
            Toast.makeText(context, "Reporte creado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }

        else{
            String[] separado=error.split("/");
            String[] errores=error.split("/");

            List<String>muestra=new ArrayList<>();
            for(int i=1;i<errores.length;i++){
                muestra.add(errores[i]);
            }
            String dialogErrores=String.join(",",muestra);

            Snackbar snackbar=Snackbar.make(findViewById(R.id.viewNuevoReporte),"Falta completar:"+dialogErrores, BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void obtenerId() {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Reportes").child(user.getName());
        DatabaseReference refId=reference.push();
        id=refId.getKey();
    }

    private HashMap<String, Operador> generarOperadores() {
        HashMap<String,Operador> operadorHashMap= new HashMap<>();
        for(String s: operadoresSeleccionados){
            Operador operador= new Operador(new ArrayList<>(),s,false,new ArrayList<>());
            operadorHashMap.put(s,operador);
        }
        return operadorHashMap;
    }

    void llenarSpinner(Spinner spinner,TextView txtTurno){
        String[] turnos=getResources().getStringArray(R.array.combo_turnos);
        List<String> lturnos=new ArrayList<>();
        lturnos.add("Seleccione el turno");
        for(String s:turnos){
            lturnos.add(s);
        }
        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,lturnos));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                turno= (String) parent.getItemAtPosition(position);
                txtTurno.setText("Escoger turno: "+turno);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void configFecha(TextInputEditText tietFecha) {
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


    void llenarLista(){
        operadoresSeleccionados=new ArrayList<>();
        operadores=getResources().getStringArray(R.array.combo_nombresOperadores);
        checkedItems=new boolean[operadores.length];
        tietOperadores.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AlertDialog.Builder builder= new AlertDialog.Builder(Nuevoreporte.this);
                builder.setTitle("Escoge los operadores");

                final SearchView searchView= new SearchView(Nuevoreporte.this);

                builder.setView(searchView);
                builder.setCancelable(false);


                builder.setMultiChoiceItems(operadores, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });
                //builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        operadoresSeleccionados=new ArrayList<>();
                        for(int i=0;i<checkedItems.length;i++){
                            boolean b=checkedItems[i];
                            if(b) {
                                if (!operadoresSeleccionados.contains(operadores[i])){
                                    operadoresSeleccionados.add(operadores[i]);
                            }
                                else{
                                    operadoresSeleccionados.remove(i);
                                }
                            }
                        }
                            muestra="";
                         for(String s:operadoresSeleccionados){
                             muestra=muestra+s+"\n";
                             tietOperadores.setText(muestra);

                         }
                        tietOperadores.clearFocus();
                    }
                });
                builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        tietOperadores.clearFocus();
                    }
                });

                builder.setNeutralButton("Reiniciar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        muestra="";
                        tietOperadores.setText(muestra);
                        operadoresSeleccionados.clear();
                     for(int i=0;i<checkedItems.length;i++){
                         checkedItems[i]=false;
                         tietOperadores.clearFocus();

                     }
                    }
                });




                AlertDialog dialog=builder.create();




                if(hasFocus){
                    dialog.show();
                   ListView listView=dialog.getListView();
                    ViewGroup.MarginLayoutParams lp= (ViewGroup.MarginLayoutParams) listView.getLayoutParams();
                    lp.height=800;
                    listView.setLayoutParams(lp);
                }
                else{
                    dialog.dismiss();
                }


            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        setResult(1,intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private static final class ListItemWithIndex {
        public final int index;
        public final String value;

        public ListItemWithIndex(final int index, final String value) {
            super();
            this.index = index;
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static void showMultiChoiceDialogWithSearchFilterUI(final Activity _activity, final Object[] _optionsList,
                                                               final int _titleResId, final DialogInterface.OnClickListener _itemSelectionListener) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(_activity);

        final List<ListItemWithIndex> allItems = new ArrayList<ListItemWithIndex>();
        final List<ListItemWithIndex> filteredItems = new ArrayList<ListItemWithIndex>();

        for (int i = 0; i < _optionsList.length; i++) {
            final Object obj = _optionsList[i];
            final ListItemWithIndex listItemWithIndex = new ListItemWithIndex(i, obj.toString());
            allItems.add(listItemWithIndex);
            filteredItems.add(listItemWithIndex);
        }

        dialogBuilder.setTitle(_titleResId);
        final ArrayAdapter<ListItemWithIndex> objectsAdapter = new ArrayAdapter<ListItemWithIndex>(_activity,
                android.R.layout.simple_list_item_1, filteredItems) {
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void publishResults(final CharSequence constraint, final FilterResults results) {
                        filteredItems.clear();
                        filteredItems.addAll((List<ListItemWithIndex>) results.values);
                        notifyDataSetChanged();
                    }

                    @Override
                    protected FilterResults performFiltering(final CharSequence constraint) {
                        final FilterResults results = new FilterResults();

                        final String filterString = constraint.toString();
                        final ArrayList<ListItemWithIndex> list = new ArrayList<ListItemWithIndex>();
                        for (final ListItemWithIndex obj : allItems) {
                            final String objStr = obj.toString();
                            if ("".equals(filterString)
                                    || objStr.toLowerCase(Locale.getDefault()).contains(
                                    filterString.toLowerCase(Locale.getDefault()))) {
                                list.add(obj);
                            }
                        }

                        results.values = list;
                        results.count = list.size();
                        return results;
                    }
                };
            }
        };

        final EditText searchEditText = new EditText(_activity);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence arg0, final int arg1, final int arg2, final int arg3) {
            }

            @Override
            public void beforeTextChanged(final CharSequence arg0, final int arg1, final int arg2, final int arg3) {
            }

            @Override
            public void afterTextChanged(final Editable arg0) {
                objectsAdapter.getFilter().filter(searchEditText.getText());
            }
        });

        final ListView listView = new ListView(_activity);
        listView.setAdapter(objectsAdapter);
        final LinearLayout linearLayout = new LinearLayout(_activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(searchEditText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
        linearLayout.addView(listView, layoutParams);
        dialogBuilder.setView(linearLayout);

        final AlertDialog dialog = dialogBuilder.create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                dialog.dismiss();
                _itemSelectionListener.onClick(null, filteredItems.get(position).index);
            }
        });
        dialog.show();
    }



}