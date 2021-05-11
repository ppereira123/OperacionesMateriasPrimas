package com.example.operacionesmteriasprimas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.login.LoginFireBase;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    Context context= this;
    InternalStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        storage=new InternalStorage();

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.desplaza_arriba);
        Animation animation2= AnimationUtils.loadAnimation(this,R.anim.desplaza_abajo);

        ImageView imgSplash=findViewById(R.id.logo);
        TextView txtLogo= findViewById(R.id.txtlogo);
        imgSplash.setAnimation(animation);
        txtLogo.setAnimation(animation2);
        TextView txtSlpash=findViewById(R.id.version);
        txtSlpash.setAnimation(animation2);


        TextView txtversion=findViewById(R.id.version);
        txtversion.setText("Version "+getVersionName(context));
        txtversion.setAnimation(animation);


        TimerTask tarea= new TimerTask() {
            @Override
            public void run() {
                String archivos[]=context.fileList();
                if (storage.ArchivoExiste(archivos,"admin.txt")){
                    Intent intent=new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(Splash.this, LoginFireBase.class);
                    startActivity(intent);
                    finish();
                }


            }
        };

        Timer tiempo= new Timer();
        tiempo.schedule(tarea,2000);

    }
    public String getVersionName(Context ctx){
        try {
            return ctx.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}