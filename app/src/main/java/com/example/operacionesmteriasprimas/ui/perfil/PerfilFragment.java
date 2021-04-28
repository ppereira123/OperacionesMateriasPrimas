package com.example.operacionesmteriasprimas.ui.perfil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.operacionesmteriasprimas.Adapters.BuscadorAdapter;
import com.example.operacionesmteriasprimas.Modelos.InternalStorage;
import com.example.operacionesmteriasprimas.Modelos.Operador;
import com.example.operacionesmteriasprimas.Modelos.UsersData;
import com.example.operacionesmteriasprimas.R;
import com.example.operacionesmteriasprimas.login.LoginFireBase;
import com.example.operacionesmteriasprimas.ui.reporte.Nuevoreporte;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {
    View root;
    TextView txt_id, txt_name, txt_email;
    TextInputEditText tietOperadores;
    CircleImageView imv_photo;
    Button btn_logout;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    Context context;
    private boolean[] checkedItems;
    private String[] operadores;
    List<String> operadoresSeleccionados;
    List<String> listaOperadores;
    BuscadorAdapter adapter;
    String muestra;
    UsersData usersData;
    List<String> listAllOperadores;

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
        tietOperadores=root.findViewById(R.id.tietOperadoresPerfil);
        usersData=new InternalStorage().cargarArchivo(root.getContext());
        tietOperadores.clearFocus();
        seleccionarOperadores(tietOperadores);
        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Establecer campos
        txt_id.setText(usersData.getId());
        txt_name.setText(usersData.getName());
        txt_email.setText(usersData.getEmail());
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

    private void seleccionarOperadores(TextInputEditText tietOperadores) {
        String muestra="";
        for(String s:usersData.getOperadores()){
            muestra=muestra+s+"\n";
        }
        muestra = muestra.substring(0, muestra.length() - 1);
        tietOperadores.setText(muestra);
        operadoresSeleccionados=new ArrayList<>();
        operadores=getResources().getStringArray(R.array.combo_nombresOperadores);
        listaOperadores=new ArrayList<>();
        for(String s: operadores){
            listaOperadores.add(s);
        }
        checkedItems=new boolean[operadores.length];
        checkActividades();
        tietOperadores.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                escogerOperadores(hasFocus);
            }

        });
    }

    private void checkActividades() {
        listAllOperadores= new ArrayList<>();
        operadoresSeleccionados=new ArrayList<>();
        for(String s: operadores){
            listAllOperadores.add(s);
        }
        for(String s:usersData.getOperadores()){
            int index=listAllOperadores.indexOf(s);
            checkedItems[index]=true;
        }
    }

    private void escogerOperadores(boolean mostrar) {
        AlertDialog.Builder builder= new AlertDialog.Builder(root.getContext());
        builder.setTitle("Escoge los operadores");

        LayoutInflater mInflate= LayoutInflater.from(root.getContext());
        View view= mInflate.inflate(R.layout.res_buscador,null);
        final SearchView searchView= view.findViewById(R.id.searchRes);
        RecyclerView recyclerView = view.findViewById(R.id.rvRes);
        adapter= new BuscadorAdapter(listaOperadores,root.getContext(),checkedItems,listaOperadores);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarOperador(newText,recyclerView);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter= new BuscadorAdapter(listaOperadores,root.getContext(),checkedItems,listaOperadores);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
                tietOperadores.clearFocus();
                return false;
            }
        });
        builder.setView(view);
        builder.setCancelable(false);

        //builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItems=adapter.getCheckedItems();
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


                }
                muestra = muestra.substring(0, muestra.length() - 1);
                tietOperadores.setText(muestra);
                usersData.setOperadores(operadoresSeleccionados);
                try {
                    new InternalStorage().guardarArchivo(usersData,root.getContext());
                } catch (IOException e) {
                    e.printStackTrace();
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
        if(mostrar) {
            dialog.show();
            tietOperadores.clearFocus();
        }
        else{
            dialog.dismiss();
        }
    }

    public void buscarOperador(String s,RecyclerView rvRes) {
        ArrayList<String> milista = new ArrayList<>();
        for (String obj : operadores) {
            if (obj.toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);
            }
        }
        BuscadorAdapter adapter= new BuscadorAdapter(milista,root.getContext(),checkedItems,listaOperadores);
        rvRes.setHasFixedSize(true);
        rvRes.setAdapter(adapter);
        rvRes.setLayoutManager(new LinearLayoutManager(root.getContext()));
    }

}