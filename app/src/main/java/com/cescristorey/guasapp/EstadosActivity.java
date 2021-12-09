package com.cescristorey.guasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.cescristorey.guasapp.adaptadores.EstadosAdaptador;
import com.cescristorey.guasapp.databinding.ActivityEstadosBinding;
import com.cescristorey.guasapp.modelos.Estado;
import com.cescristorey.guasapp.modelos.Usuario;
import com.cescristorey.guasapp.modelos.EstadosUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EstadosActivity extends AppCompatActivity {

    ActivityEstadosBinding binding;
    FirebaseDatabase database;
    EstadosAdaptador adaptador;
    ArrayList<EstadosUsuario> estadosUsuario;
    ProgressDialog dialogo;
    Usuario usuario;
    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEstadosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        estadosUsuario = new ArrayList<>();
        dialogo = new ProgressDialog(this);
        dialogo.setMessage(getString(R.string.uploading_status));
        dialogo.setCancelable(false);
        database = FirebaseDatabase.getInstance();
        navigationView = binding.bottomNavigationViewStatus;

        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        navigationView.getMenu().findItem(R.id.estado).setChecked(true);

        database.getReference().child("usuarios").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        usuario = snapshot.getValue(Usuario.class);
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });

        adaptador = new EstadosAdaptador(this, estadosUsuario);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.statuslist.setLayoutManager(layoutManager);
        binding.statuslist.setAdapter(adaptador);

        binding.addtostatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent , 75);

            }
        });

        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if(snapshot.exists()){
                    estadosUsuario.clear();
                    for(DataSnapshot storySnapshot :snapshot.getChildren()){
                        EstadosUsuario estadoUsuario = new EstadosUsuario();
                        estadoUsuario.setNombre(storySnapshot.child("name").getValue(String.class));
                        estadoUsuario.setImagenPerfil(storySnapshot.child("profileImage").getValue(String.class));
                        estadoUsuario.setUltimaActualizacion(storySnapshot.child("lastupdated").getValue(Long.class));

                        ArrayList<Estado> estados = new ArrayList<>();
                        for(DataSnapshot statusSnapshot : storySnapshot.child("statuses").getChildren()){
                            Estado sampleEstado = statusSnapshot.getValue(Estado.class);
                            estados.add(sampleEstado);
                        }
                        estadoUsuario.setEstados(estados);
                        estadosUsuario.add(estadoUsuario);

                    }
                    adaptador.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
            switch (item.getItemId()){
                case R.id.chat:
                    Intent intent = new Intent(EstadosActivity.this , MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null){
            if(data.getData()!=null){
                dialogo.show();
                FirebaseStorage almacenamiento = FirebaseStorage.getInstance();
                Date fecha = new Date();

                StorageReference reference = almacenamiento.getReference().child("status").child(fecha.getTime()+"");
                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    EstadosUsuario estadoUsuario = new EstadosUsuario();
                                    estadoUsuario.setNombre(usuario.getNombre());
                                    estadoUsuario.setImagenPerfil(usuario.getImagenPerfil());
                                    estadoUsuario.setUltimaActualizacion(fecha.getTime());

                                    HashMap<String , Object> aux = new HashMap<>();
                                    aux.put("name" , estadoUsuario.getNombre());
                                    aux.put("profileImage"  , estadoUsuario.getImagenPerfil());
                                    aux.put("lastupdated" , estadoUsuario.getUltimaActualizacion());

                                    String urlImagen = uri.toString();
                                    Estado estado = new Estado(urlImagen , estadoUsuario.getUltimaActualizacion());

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(aux);

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(estado);

                                    dialogo.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }


}