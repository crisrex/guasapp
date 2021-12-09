package com.cescristorey.guasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cescristorey.guasapp.databinding.ActivityNumeroTelefonoBinding;
import com.google.firebase.auth.FirebaseAuth;

public class NumeroTelefonoActivity extends AppCompatActivity {

    ActivityNumeroTelefonoBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNumeroTelefonoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser()!=null){
            Intent intent = new Intent(NumeroTelefonoActivity.this , MainActivity.class) ;
            startActivity(intent);
            finishAffinity();
        }

        else{
            getSupportActionBar().hide();
            binding.phoneBox.requestFocus();
            binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NumeroTelefonoActivity.this , SMSActivity.class);
                    intent.putExtra("numeroTelefono" , binding.phoneBox.getText().toString());
                    startActivity(intent);
                }
            });

        }
    }
}