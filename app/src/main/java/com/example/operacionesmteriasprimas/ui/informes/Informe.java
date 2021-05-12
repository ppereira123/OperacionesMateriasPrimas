package com.example.operacionesmteriasprimas.ui.informes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.operacionesmteriasprimas.InformeVista;
import com.example.operacionesmteriasprimas.Modelos.Reporte;
import com.example.operacionesmteriasprimas.Modelos.sumaInformeOperador;
import com.example.operacionesmteriasprimas.Modelos.sumas;
import com.example.operacionesmteriasprimas.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;

import static com.example.operacionesmteriasprimas.InformeVista.GetData;
import static java.lang.Thread.sleep;

public class Informe extends Fragment {
    Button limpiar, buscar;
    TextView name,date;
    String tipoDocumento,fechadesde,fechahasta,tipoInforme;
    EditText editFechadesde,editFechahasta,editSupervisora;
    TextInputLayout edittipoDocumento, textfieldSupervisora, textinputlayoutFechahasta,textinputlayoutFechadesde, edittipoInforme;
    String[] listatipodocumentos,listatipoinforme;
    AutoCompleteTextView autoCompleteTextViewSpinnnerDopcumentos,autoCompleteTextViewSpinnnerInformes;
    List<Reporte> listReportes=new ArrayList<>();


    private FirebaseAuth mAuth;
    public List<String> supervisoreslist;
    List<String> slectSupervisors=new ArrayList<>();

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
        cargardatos();
        context=root.getContext();
        tipoDocumento="";
        fechahasta="";
        fechadesde="";
        limpiar=root.findViewById(R.id.btnLimpiar);
        buscar=root.findViewById(R.id.btnBuscarInforme);
        name=root.findViewById(R.id.txtnameUsuario);
        date=root.findViewById(R.id.txtFechaInforme);
        editSupervisora=root.findViewById(R.id.editSupervisora);
        editFechadesde=root.findViewById(R.id.editFechadesde);
        editFechahasta=root.findViewById(R.id.editFechahasta);
        edittipoDocumento=root.findViewById(R.id.edittipoDocumento);
        textfieldSupervisora=root.findViewById(R.id.textfieldtSupervisora);
        textinputlayoutFechadesde=root.findViewById(R.id.textinputlayoutFechadesde);
        textinputlayoutFechahasta=root.findViewById(R.id.textinputlayoutFechahasta);
        autoCompleteTextViewSpinnnerDopcumentos=root.findViewById(R.id.autocompleteSpinnerDocumentos);
        edittipoInforme=root.findViewById(R.id.edittipodeinforme);
        autoCompleteTextViewSpinnnerInformes=root.findViewById(R.id.autocompleteSpinnertipoinforme);


        autoCompleteTextViewSpinnnerInformes.setKeyListener(null);
        autoCompleteTextViewSpinnnerDopcumentos.setKeyListener(null);
        editSupervisora.setKeyListener(null);
        editFechahasta.setKeyListener(null);
        editFechadesde.setKeyListener(null);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Establecer campos
        name.setText(currentUser.getDisplayName());
        Date d=new Date();
        SimpleDateFormat fecc=new SimpleDateFormat("d/MM/yyyy");
        String fechacComplString = fecc.format(d);
        date.setText(fechacComplString);
        configFecha(editFechadesde,editFechahasta,false,textinputlayoutFechadesde);
        configFecha(editFechahasta,editFechadesde,true,textinputlayoutFechahasta);
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar();
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });




        editSupervisora.setOnFocusChangeListener(new View.OnFocusChangeListener() {


            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                    List<String> supervisoresSeleccionados=new ArrayList<>();
                    supervisoreslist=new ArrayList<>();
                    supervisoreslist.add("Todos");
                    FirebaseDatabase database= FirebaseDatabase.getInstance();
                    DatabaseReference ref=database.getReference("Reportes");
                    ref.keepSynced(true);
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

                                for(int i=0;i<supervisoreslist.size();i=i+1){
                                    arryaSupervisores[i]=supervisoreslist.get(i);
                                    checkedItems[i]=false;
                                }


                                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                                builder.setTitle("Escoger supervisores para el informe");
                                builder.setMultiChoiceItems(arryaSupervisores, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if(arryaSupervisores[which].equals("Todos")){

                                            final AlertDialog alertDialog = (AlertDialog) dialog;
                                            final ListView alertDialogList = alertDialog.getListView();

                                            for (int position = 0; position < alertDialogList.getChildCount(); position++)
                                            {
                                                if (position != which) {
                                                    if(isChecked){
                                                        alertDialogList.getChildAt(position).setVisibility(View.GONE);
                                                        supervisoresSeleccionados.clear();
                                                        supervisoresSeleccionados.add(arryaSupervisores[which]);
                                                    }else {
                                                        supervisoresSeleccionados.clear();
                                                        alertDialogList.getChildAt(position).setVisibility(View.VISIBLE);
                                                    }



                                                }
                                            }
                                        }else {
                                            if(supervisoresSeleccionados.contains("Todos")){
                                                supervisoresSeleccionados.remove(supervisoresSeleccionados.indexOf("Todos"));
                                            }

                                            if(supervisoresSeleccionados.contains(arryaSupervisores[which])){
                                                supervisoresSeleccionados.remove(supervisoresSeleccionados.indexOf(arryaSupervisores[which]));

                                            }
                                            else {
                                                supervisoresSeleccionados.add(arryaSupervisores[which]);
                                            }

                                        }




                                    }
                                });
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        slectSupervisors.clear();
                                        editSupervisora.clearFocus();

                                        if(!supervisoresSeleccionados.contains("Todos")){
                                            int cont=0;

                                            for(boolean seleccionado:checkedItems){
                                                if(seleccionado){
                                                    slectSupervisors.add(arryaSupervisores[cont]);

                                                }
                                                cont++;
                                            }

                                        }else {
                                            slectSupervisors.add("Todos");


                                        }
                                        String muestra="";
                                        for(String s:slectSupervisors){
                                            muestra=muestra+s+"\n";
                                        }
                                        editSupervisora.setText(muestra);


                                    }
                                });
                                builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        editSupervisora.clearFocus();
                                    }
                                });

                                AlertDialog dialog=builder.create();
                                dialog.show();
                            }
                            else{
                                Toast.makeText(root.getContext(), "No existe referencia", Toast.LENGTH_SHORT).show();
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    editSupervisora.clearFocus();
                }
            }
        });

        //spinner tipo documentos llenado
        listatipodocumentos=getResources().getStringArray(R.array.combo_tiposDocumentos);
        ArrayAdapter<String> adapterTipoDocumento=new ArrayAdapter<>(context, R.layout.dropdow_item,listatipodocumentos);
        autoCompleteTextViewSpinnnerDopcumentos.setAdapter(adapterTipoDocumento);
        tipoDocumento=autoCompleteTextViewSpinnnerDopcumentos.getText().toString();
        //spinner tipo informe llenado
        listatipoinforme=getResources().getStringArray(R.array.combo_tipoInforme);
        ArrayAdapter<String> adapterTipoinforme=new ArrayAdapter<>(context, R.layout.dropdow_item,listatipoinforme);
        autoCompleteTextViewSpinnnerInformes.setAdapter(adapterTipoinforme);
        tipoInforme=autoCompleteTextViewSpinnnerInformes.getText().toString();

        return root;
    }


    public static int diferenciaDias(String fecha1,String fecha2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date1= format.parse(fecha1);
        Date date2=format.parse(fecha2);
        int dias = (int) ((date1.getTime() - date2.getTime()) / 86400000);
        return  dias;

    }


    private void limpiar(){
        autoCompleteTextViewSpinnnerDopcumentos.setText("");
        editFechadesde.setText("");
        editFechahasta.setText("");
        editSupervisora.setText("");
        autoCompleteTextViewSpinnnerInformes.setText("");
    }
    private  void buscar(){
        tipoDocumento=autoCompleteTextViewSpinnnerDopcumentos.getText().toString();
        fechadesde=editFechadesde.getText().toString();
        fechahasta=editFechahasta.getText().toString();
        tipoInforme=autoCompleteTextViewSpinnnerInformes.getText().toString();



        if(tipoDocumento.equals("")||fechahasta.equals("")||fechadesde.equals("")||slectSupervisors.size()<1||tipoInforme.equals("")){
            Toast.makeText(context, "Falta completar parametro", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Comenzando busqueda", Toast.LENGTH_SHORT).show();
            if(tipoDocumento.equals("Visual")){
                Intent intent=new Intent(context, InformeVista.class);
                intent.putExtra("Fechadesde",fechadesde);
                intent.putExtra("Fechahasta",fechahasta);
                intent.putExtra("TipoInforme",tipoInforme);
                intent.putExtra("Supervisores", (Serializable) slectSupervisors);
                intent.putExtra("TipoInforme",tipoInforme);
                startActivityForResult(intent,1);
            }else if(tipoDocumento.equals("Excel")){

                Toast.makeText(context, listReportes.toString(), Toast.LENGTH_SHORT).show();

                Toast.makeText(context, listReportes.toString(), Toast.LENGTH_SHORT).show();
                crearExcelactividadesGenerales(filtrarDatos(fechadesde,fechahasta,listReportes));
            }
        }

    }



    private void configFecha(EditText tietFecha, EditText tietFecha2,boolean isfechahasta,TextInputLayout textinput) {


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
                        String monthstring,yearstring,daystring;
                        monthstring=String.valueOf(month);
                        String date= day+"/"+month+"/"+year;
                        fecha=date;


                        ParsePosition pos = new ParsePosition(0);
                        String fecha2= tietFecha2.getText().toString();
                        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");

                        if(!fecha2.equals("")){

                            if(isfechahasta){
                                try {
                                    if(diferenciaDias(fecha,fecha2)>=0){
                                         tietFecha.setText(fecha);
                                     }
                                     else {
                                         Toast.makeText(context, "Error! la fecha debe ser mayor a la de Fecha desde", Toast.LENGTH_SHORT).show();
                                         tietFecha.setText("");
                                         int color= tietFecha.getDrawingCacheBackgroundColor();
                                        textinput.setBoxBackgroundColor(Color.parseColor("#FFCDD2"));
                                        TimerTask tarea= new TimerTask() {
                                            @Override
                                            public void run() {
                                                textinput.setBoxBackgroundColor(Color.parseColor("#FFFFFF"));



                                            }
                                        };

                                        Timer tiempo= new Timer();
                                        tiempo.schedule(tarea,1000);
                                     }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else {
                                try {
                                    if(diferenciaDias(fecha,fecha2)<=0){
                                        tietFecha.setText(fecha);
                                    }
                                    else {
                                        Toast.makeText(context, "Error! la fecha debe ser menor a la de Fecha hasta", Toast.LENGTH_SHORT).show();
                                        tietFecha.setText("");
                                        int color= tietFecha.getDrawingCacheBackgroundColor();
                                        textinput.setBoxBackgroundColor(Color.parseColor("#FFCDD2"));
                                        TimerTask tarea= new TimerTask() {
                                            @Override
                                            public void run() {
                                                textinput.setBoxBackgroundColor(Color.parseColor("#FFFFFF"));
                                            }
                                        };

                                        Timer tiempo= new Timer();
                                        tiempo.schedule(tarea,1000);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else {
                            tietFecha.setText(fecha);
                        }


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

    public void cargardatos(){

        List<Reporte> list=new ArrayList<>();
        list.clear();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Reportes");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        GenericTypeIndicator<Reporte> t = new GenericTypeIndicator<Reporte>() {};
                        Reporte m = dataSnapshot.getValue(t);
                        list.add(m);



                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        listReportes=list;
    }

    public void crearExcelactividadesGenerales(List<Reporte> listReportes){
        Workbook wb= new HSSFWorkbook();
        Map<String, CellStyle> styles= createStyles(wb);
        Sheet sheet= wb.createSheet("Informe");
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);

        //title row
        String rango;
        int nRow=0;
        int nCell=0;
        Row titleRow = sheet.createRow(nRow);
        titleRow.setHeightInPoints(20);
        Cell titleCell = titleRow.createCell(nCell);
        titleCell.setCellValue("Informe General ");
        titleCell.setCellStyle(styles.get("title"));
        rango="$A$"+(nRow+1)+":$K$"+(nRow+1);

        sheet.addMergedRegion(CellRangeAddress.valueOf(rango));
        //Datos
        //nombre de la empresa
        nRow=nRow+1;
        Row row = sheet.createRow(nRow);
        Cell cell = row.createCell(nCell);
        cell.setCellValue("Empresa: ");
        cell.setCellStyle(styles.get("item_left1"));
        rango="$A$"+(nRow+1)+":$B$"+(nRow+1);
        sheet.addMergedRegion(CellRangeAddress.valueOf(rango));
        nRow=nRow+1;
        cell = row.createCell(nRow);
        cell.setCellValue("Holcim Ecuador");
        cell.setCellStyle(styles.get("item_left"));

        //fecha
        row = sheet.createRow(nRow);
        cell = row.createCell(nCell);
        cell.setCellValue("Fecha del reporte: ");
        cell.setCellStyle(styles.get("item_left1"));


        cell = row.createCell(nRow);
        cell.setCellValue(editFechadesde.getText().toString()+" - "+editFechahasta.getText().toString());
        cell.setCellStyle(styles.get("item_left"));


        //Titulo actividades
        nRow=nRow+3;
        nCell=nCell+1;

        row = sheet.createRow(nRow);
        cell = row.createCell(nCell);
        cell.setCellValue("Actividades");
        cell.setCellStyle(styles.get("header"));

        cell = row.createCell(nCell+6);
        cell.setCellValue("Total");
        cell.setCellStyle(styles.get("header"));

        sheet.addMergedRegion(new CellRangeAddress(
                nRow, //first row (0-based)
                nRow, //last row  (0-based)
                1, //first column (0-based)
                6 //last column  (0-based)
        ));
        sheet.addMergedRegion(new CellRangeAddress(
                nRow, //first row (0-based)
                nRow, //last row  (0-based)
                7, //first column (0-based)
                8 //last column  (0-based)
        ));
        //actividades
        List<sumas> lista=GetData(listReportes);
        for(sumas suma:lista){
            nRow=nRow+1;
            row = sheet.createRow(nRow);
            cell = row.createCell(nCell);
            cell.setCellValue(suma.getActividad());
            cell.setCellStyle(styles.get("cell"));
            cell = row.createCell(nCell+6);
            double hora=suma.getHoras();
            BigDecimal bd = new BigDecimal(hora).setScale(0, RoundingMode.HALF_UP);
            int val1 = (int) bd.doubleValue();
            cell.setCellValue(val1);
            cell.setCellStyle(styles.get("cell"));
            cell = row.createCell(nCell+7);
            cell.setCellValue("");
            cell.setCellStyle(styles.get("cell"));


            for (int n = (nCell+1); n < (nCell+6); n++) {
                cell = row.createCell(n);
                cell.setCellStyle(styles.get("cell"));
            }


            //merge de las celdas


            sheet.addMergedRegion(new CellRangeAddress(
                    nRow, //first row (0-based)
                    nRow, //last row  (0-based)
                    1, //first column (0-based)
                    6 //last column  (0-based)
            ));
            sheet.addMergedRegion(new CellRangeAddress(
                    nRow, //first row (0-based)
                    nRow, //last row  (0-based)
                    7, //first column (0-based)
                    8 //last column  (0-based)
            ));

        }


        /*
        rango="$C$"+(nRow)+":$F$"+(nRow);
        sheet.addMergedRegion(CellRangeAddress.valueOf(rango));

         */








        //creacion del documento
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        String nombreFile="InformeGeneral.xls";
        File file = new File(context.getExternalFilesDir(null),nombreFile);
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            Toast.makeText(context.getApplicationContext(),"Reporte generado correctamente",Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();

            Toast.makeText(context.getApplicationContext(),"NO OK",Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }






    }
    public void crearExelPorOperador(sumaInformeOperador data){


    }

    private static Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)18);
        titleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font itemFont = wb.createFont();
        itemFont.setFontHeightInPoints((short)12);
        itemFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(titleFont);
        style.setFont(itemFont);
        styles.put("item_left", style);

        Font itemresFont = wb.createFont();
        itemresFont.setFontHeightInPoints((short)12);
        itemresFont.setFontName("Trebuchet MS");
        itemresFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(itemresFont);
        styles.put("item_left1", style);



        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short)12);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        Font cellFont = wb.createFont();
        cellFont.setFontHeightInPoints((short)12);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(cellFont);
        style.setWrapText(true);
        styles.put("cell", style);
        return styles;
    }
    public List<Reporte> filtrarDatos(String fechadesde, String fechahasta, List<Reporte> list){
        List<Reporte> lista=new ArrayList<>();
        try {
            for(Reporte m:list){
                if(diferenciaDias(m.getFecha(),fechadesde)>=0&&diferenciaDias(m.getFecha(),fechahasta)<=0){
                    lista.add(m);

                    if(slectSupervisors.contains("Todos")){
                        lista.add(m);

                    }else {
                        if(slectSupervisors.contains(m.getSupervisor())){
                            lista.add(m);
                        }

                    }


                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lista;
    }


}