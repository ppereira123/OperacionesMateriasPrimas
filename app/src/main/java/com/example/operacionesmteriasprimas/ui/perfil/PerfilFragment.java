package com.example.operacionesmteriasprimas.ui.perfil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;

import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.R;
import com.example.operacionesmteriasprimas.login.LoginFireBase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {
    View root;
    TextView txt_id, txt_name, txt_email;
    CircleImageView imv_photo;
    Button btn_logout;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    Context context;

    private PerfilViewModel perfilViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        perfilViewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);
        root = inflater.inflate(R.layout.fragment_perfil, container, false);
        txt_id = root.findViewById(R.id.txt_userId);
        txt_name = root.findViewById(R.id.txt_nombre);
        txt_email = root.findViewById(R.id.txt_correo);
        imv_photo = root.findViewById(R.id.imv_foto);
        btn_logout = root.findViewById(R.id.btn_logout);
        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Establecer campos
        txt_id.setText(currentUser.getUid());
        txt_name.setText(currentUser.getDisplayName());
        txt_email.setText(currentUser.getEmail());
        //cargar imagen con glide
        Glide.with(this).load(currentUser.getPhotoUrl()).into(imv_photo);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cerrar sesión con firebase
                mAuth.signOut();
                //Cerrar sesión con google tambien:Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Abrir MainActivity con SigIn button
                        if (task.isSuccessful()) {
                            Intent loginFireBase = new Intent(getActivity(), LoginFireBase.class);
                            startActivity(loginFireBase);
                            InternalStorage storage=new InternalStorage();
                            storage.eliminarArchivo(root.getContext());
                            //PerfilGoogle.finish();
                        } else {
                            Toast.makeText(context, "No se pudo cerrar sesión con google",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return root;
    }
}