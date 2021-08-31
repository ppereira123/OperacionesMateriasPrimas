package com.example.operacionesmteriasprimas.Modelos;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class InternalStorage {

    public InternalStorage(){

    }

    @Override
    public String toString() {
        return "InternalStorage{}";
    }

    public void guardarArchivo(UsersData data, Context context) throws IOException {
        String urlArchivo="admin.txt";
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(urlArchivo, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        os.writeObject(data);
        os.close();
        fos.close();

    }



    public  void  guardarVoladura(Voladura data,Context context) throws IOException {
        String archivos[]=context.fileList();
        String urlArchivo="voladura.txt";
        ArrayList<Voladura> voladuras=new ArrayList<>();
        if(ArchivoExiste(archivos,urlArchivo)){
            voladuras=cargarVoladura(context);
        }
        voladuras.add(data);

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(urlArchivo, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        os.writeObject(voladuras);
        os.close();
        fos.close();

    }
    public  void  guardarExplosivos(ArrayList<Explosivo> data,Context context) throws IOException {
        String urlArchivo="explosivos.txt";
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(urlArchivo, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        os.writeObject(data);
        os.close();
        fos.close();

    }



    public void guardarReporte(Reporte reporte,Context context) throws IOException {
        HashMap<String,Reporte> reportes=cargarReportes(context);
        reportes.put(reporte.getId(),reporte);
        String urlArchivo="reportes.txt";
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(urlArchivo, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        os.writeObject(reportes);
        os.close();
        fos.close();


    }

    public void eliminarReporte(Reporte reporte,Context context) throws IOException {
        HashMap<String,Reporte> reportes=cargarReportes(context);
        reportes.remove(reporte.getId());
        String urlArchivo="reportes.txt";
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(urlArchivo, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        os.writeObject(reportes);
        os.close();
        fos.close();
    }

    public void eliminarVoladura(Voladura voladuraeliminada,Context context) throws IOException {
        ArrayList<Voladura> voladuras=cargarVoladura(context);
        voladuras.remove(voladuraeliminada);
        String urlArchivo="voladuras.txt";
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(urlArchivo, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        os.writeObject(voladuras);
        os.close();
        fos.close();
    }



    public HashMap<String,Reporte> cargarReportes(Context context){
        String urlArchivo="reportes.txt";
        HashMap<String,Reporte> reportes= new HashMap<>();
        String archivos[]=context.fileList();
        try {
            FileInputStream fis = context.openFileInput(urlArchivo);
            ObjectInputStream is = new ObjectInputStream(fis);
            reportes = (HashMap<String, Reporte>) is.readObject();
            is.close();
            fis.close();
            return reportes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return reportes;
    }

    public ArrayList<Voladura> cargarVoladura(Context context){
        String urlArchivo="voladuras.txt";
        ArrayList<Voladura> voladuraArrayList= new ArrayList<>();

        try {

            FileInputStream fis = context.openFileInput(urlArchivo);
            ObjectInputStream is = new ObjectInputStream(fis);
            voladuraArrayList = (ArrayList<Voladura>) is.readObject();
            is.close();
            fis.close();
            return voladuraArrayList;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return voladuraArrayList;
    }
    public ArrayList<Explosivo> cargarExplosivos(Context context){
        String urlArchivo="explosivos.txt";
        ArrayList<Explosivo> explosivoArrayList= new ArrayList<>();

        try {

            FileInputStream fis = context.openFileInput(urlArchivo);
            ObjectInputStream is = new ObjectInputStream(fis);
            explosivoArrayList = (ArrayList<Explosivo>) is.readObject();
            is.close();
            fis.close();
            return explosivoArrayList;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return explosivoArrayList;
    }


    public boolean ArchivoExiste(String[] archivos, String urlArchivo) {
        for(int i=0;i<archivos.length;i++){
            if(urlArchivo.equals(archivos[i])){
                return true;
            }
        }
        return false;
    }


    public  UsersData cargarArchivo(Context context){
        String urlArchivo="admin.txt";
        UsersData data=null;
        String archivos[]=context.fileList();
        try {

            FileInputStream fis = context.openFileInput(urlArchivo);
            ObjectInputStream is = new ObjectInputStream(fis);
            data = (UsersData) is.readObject();
            is.close();
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }



    public void eliminarArchivo(Context context){
        String url="admin.txt";

        String archivos[]=context.fileList();
        if(ArchivoExiste(archivos,url)){
            File dir = context.getFilesDir(); File file = new File(dir, url);


            if(file.delete()){
                Toast.makeText(context, "Archivo borrado correctamente", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context, "No se ha podido borrar el archivo", Toast.LENGTH_SHORT).show();

            }
        }



    }
}
