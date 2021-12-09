package com.cescristorey.guasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cescristorey.guasapp.databinding.ActivitySmsactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SMSActivity extends AppCompatActivity {

    ActivitySmsactivityBinding binding;
    FirebaseAuth auth;
    String verificacionID;
    String numeroTelefono;
    ProgressDialog dialogo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySmsactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        numeroTelefono = getIntent().getStringExtra("numeroTelefono");
        dialogo = new ProgressDialog(this);
        dialogo.setMessage(getString(R.string.verify_otp));
        dialogo.setCancelable(false);

        getSupportActionBar().hide();

        binding.phoneLbl2.setText(getString(R.string.verify_text) + numeroTelefono);
        PhoneAuthOptions opciones = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(numeroTelefono)
                .setTimeout(60L , TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                    auth.signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull  FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull  String verifyId, @NonNull  PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyId, forceResendingToken);
                        verificacionID = verifyId;
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(opciones);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = binding.otpbox.getText().toString();
                if(codigo.isEmpty()){
                    Toast.makeText(SMSActivity.this, getString(R.string.please_enter_otp), Toast.LENGTH_SHORT).show();
                }
                else{
                    dialogo.show();
                    PhoneAuthCredential credencial = PhoneAuthProvider.getCredential(verificacionID, codigo);
                    auth.signInWithCredential(credencial).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                dialogo.dismiss();
                                Intent intent = new Intent(SMSActivity.this, PerfilActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(SMSActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}