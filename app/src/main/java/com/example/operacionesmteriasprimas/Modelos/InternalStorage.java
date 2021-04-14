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
import java.util.HashMap;

public class InternalStorage {

    public InternalStorage(){

    }

    public void guardarArchivo(UsersData data,Context context) throws IOException {
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return reportes;
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
