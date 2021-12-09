package com.cescristorey.guasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cescristorey.guasapp.adaptadores.UsuariosAdaptador;
import com.cescristorey.guasapp.modelos.Usuario;
import com.cescristorey.guasapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<Usuario> usuarios;
    UsuariosAdaptador usuariosAdaptador;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = binding.bottomNavigationViewMain;
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        database = FirebaseDatabase.getInstance();
        usuarios = new ArrayList<>();
        usuariosAdaptador = new UsuariosAdaptador(this , usuarios);
        binding.chatsrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.chatsrecyclerview.setAdapter(usuariosAdaptador);



        database.getReference().child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
            usuarios.clear();
            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                Usuario usuario = snapshot1.getValue(Usuario.class);
                usuarios.add(usuario);
            }
            usuariosAdaptador.notifyDataSetChanged();

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
                case R.id.estado:
                    Intent intent = new Intent(MainActivity.this , EstadosActivity.class );
                    startActivity(intent);
                    finish();
                    break;
            }
            return false;
        }
    };




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){


        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu , menu);
        return super.onCreateOptionsMenu(menu);
    }
}