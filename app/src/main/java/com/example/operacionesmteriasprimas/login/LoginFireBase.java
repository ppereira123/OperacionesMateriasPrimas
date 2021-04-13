package com.example.operacionesmteriasprimas.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.operacionesmteriasprimas.MainActivity;
import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.UsersData;
import com.example.operacionesmteriasprimas.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;

public class LoginFireBase extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    static final int GOOGLE_SIGN_IN = 123;
    FirebaseAuth mAuth;
    ImageButton imgIniciar;
    ImageView imgHolcim,imgLogo;
    TextView txtIncio;
    Context context= this;
    InternalStorage storage;
    boolean admin;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fire_base);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Iniciando Sesion...");
        LayoutInflater inflater=LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.res_carga,null);
        builder.setView(view);
        alert = builder.create();
        storage=new InternalStorage();

        mAuth= FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        if(msg != null){
            if(msg.equals("cerrarSesion")){
                cerrarSesion();
            }
        }


    }
    private void cerrarSesion() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }
    public void iniciarSesion(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        /*if (account != null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        alert.show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent( data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Fallo el inicio de sesión con google.", e);
            }
        }

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);

                        FirebaseDatabase database= FirebaseDatabase.getInstance();
                        String uid= user.getUid();
                        DatabaseReference myRef= database.getReference("admins").child(uid);
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    admin=true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        try {
                            UsersData data= new UsersData(admin,user.getUid(),user.getDisplayName(), user.getPhotoUrl().toString(),user.getEmail());
                            storage.guardarArchivo(data ,this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(this, MainActivity.class);
                        alert.dismiss();
                        startActivity(intent);
                        finish();


                    } else {
                        System.out.println("error");
                        updateUI(null);
                    }

                });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            HashMap<String, String> info_user = new HashMap<String, String>();
            info_user.put("user_name", user.getDisplayName());
            info_user.put("user_email", user.getEmail());
            info_user.put("user_photo", String.valueOf(user.getPhotoUrl()));
            info_user.put("user_id", user.getUid());
            finish();
        } else {
            System.out.println("sin registrarse");
        }
    }

}