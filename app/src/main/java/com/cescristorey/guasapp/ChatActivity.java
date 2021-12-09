package com.cescristorey.guasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cescristorey.guasapp.adaptadores.MensajesAdaptador;
import com.cescristorey.guasapp.modelos.Mensaje;
import com.cescristorey.guasapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    MensajesAdaptador adaptadorMensajes;
    ArrayList<Mensaje> mensajes;
    FirebaseDatabase baseDatos;
    FirebaseStorage almacenamiento;

    String emisorUId;
    String receptorUId;

    String chatEmisor, chatReceptor;

    ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        baseDatos = FirebaseDatabase.getInstance();
        almacenamiento =FirebaseStorage.getInstance();
        dialogo = new ProgressDialog(this);
        dialogo.setMessage(getString(R.string.uploading));
        dialogo.setCancelable(false);
        mensajes = new ArrayList<>();
        adaptadorMensajes = new MensajesAdaptador(this , mensajes);
        binding.messsagerecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.messsagerecyclerView.setAdapter(adaptadorMensajes);

        getSupportActionBar().hide();
        setContentView(binding.getRoot());

        String nombre = getIntent().getStringExtra("name");
        receptorUId = getIntent().getStringExtra("uid");
        String imagenPerfil = getIntent().getStringExtra("profileImage");
        emisorUId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        chatEmisor = emisorUId + receptorUId;
        chatReceptor = receptorUId + emisorUId;
        binding.activename.setText(nombre);
        Glide.with(this).load(imagenPerfil).into(binding.profiledp);

        baseDatos.getReference().child("chats")
                    .child(chatEmisor)
                    .child("messages")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            mensajes.clear();
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                Mensaje mensaje = snapshot1.getValue(Mensaje.class);
                                mensajes.add(mensaje);
                            }
                            adaptadorMensajes.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });

        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent , 25);

            }
        });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMensaje = binding.messageBox.getText().toString();
                Date fecha = new Date();
                Mensaje mensaje = new Mensaje(textoMensaje , emisorUId, fecha.getTime());
                binding.messageBox.setText("");
                baseDatos.getReference().child("chats")
                        .child(chatEmisor)
                        .child("messages")
                        .push()
                        .setValue(mensaje).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        baseDatos.getReference().child("chats")
                                .child(chatReceptor)
                                .child("messages")
                                .push()
                                .setValue(mensaje).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==25){
            if(data!=null){
                if(data.getData()!=null){
                    Uri imagenSeleccionada = data.getData();
                    Calendar calendario = Calendar.getInstance();
                    StorageReference reference = almacenamiento.getReference().child("chats").child(calendario.getTimeInMillis()+"");
                    dialogo.show();
                    reference.putFile(imagenSeleccionada).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull  Task<UploadTask.TaskSnapshot> task) {
                            dialogo.dismiss();
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                     String rutaFichero = uri.toString();

                                        String messageTxt = binding.messageBox.getText().toString();
                                        Date fecha = new Date();
                                        Mensaje mensaje = new Mensaje(messageTxt , emisorUId, fecha.getTime());
                                        mensaje.setUrlImagen(rutaFichero);
                                        mensaje.setTextoMensaje("photo");
                                        binding.messageBox.setText("");
                                        baseDatos.getReference().child("chats")
                                                .child(chatEmisor)
                                                .child("messages")
                                                .push()
                                                .setValue(mensaje).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                baseDatos.getReference().child("chats")
                                                        .child(chatReceptor)
                                                        .child("messages")
                                                        .push()
                                                        .setValue(mensaje).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                });

                                            }
                                        });

                                        Toast.makeText(ChatActivity.this, rutaFichero, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });

                }
            }
        }
    }
}