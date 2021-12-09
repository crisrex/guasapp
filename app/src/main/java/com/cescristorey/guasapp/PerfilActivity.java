package com.cescristorey.guasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cescristorey.guasapp.databinding.ActivityPerfilBinding;
import com.cescristorey.guasapp.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PerfilActivity extends AppCompatActivity {
    ActivityPerfilBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase baseDatos;
    FirebaseStorage almacenamiento;
    Uri imagenSeleccionada;
    ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialogo = new ProgressDialog(this);
        dialogo.setMessage("Updating Profile");
        dialogo.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        almacenamiento = FirebaseStorage.getInstance();
        baseDatos = FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        binding.profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent , 45);
            }
        });

        binding.setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameBox.getText().toString();
                if(name.isEmpty()){
                    binding.nameBox.setError("Please Enter a Name");
                    return;
                }
                dialogo.show();
                if(imagenSeleccionada != null) {
                    StorageReference reference = almacenamiento.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(imagenSeleccionada).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        String uid = auth.getUid();
                                        String phone = auth.getCurrentUser().getPhoneNumber();
                                        String name = binding.nameBox.getText().toString();

                                        Usuario usuario = new Usuario(uid, name, phone, imageUrl);

                                        baseDatos.getReference()
                                                .child("usuarios")
                                                .child(uid)
                                                .setValue(usuario)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialogo.dismiss();
                                                        Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                } else {
                    String uid = auth.getUid();
                    String phone = auth.getCurrentUser().getPhoneNumber();

                    Usuario usuario = new Usuario(uid, name, phone, "No Image");

                    baseDatos.getReference()
                            .child("usuarios")
                            .child(uid)
                            .setValue(usuario)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialogo.dismiss();
                                    Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.profileimage.setImageURI(data.getData());
        imagenSeleccionada = data.getData();

    }
}