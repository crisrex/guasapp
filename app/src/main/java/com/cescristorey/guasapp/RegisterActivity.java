package com.cescristorey.guasapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import com.cescristorey.guasapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        auth = FirebaseAuth.getInstance();

        binding.txtLogin.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity( new Intent( RegisterActivity.this, LoginActivity.class ) );
            }
        } );

        binding.register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String str_username = binding.txtLogin.getText().toString();
                String str_username = binding.username.getText().toString();
                //String str_fullname = binding.fullname.getText().toString();
                String str_email = binding.email.getText().toString();
                String str_password = binding.password.getText().toString();

                if (TextUtils.isEmpty( str_username ) /*|| TextUtils.isEmpty( str_fullname )*/ ||
                        TextUtils.isEmpty( str_email ) || TextUtils.isEmpty( str_password )) {
                    Toast.makeText( RegisterActivity.this,getText(R.string.fields_required),Toast.LENGTH_SHORT ).show();
                } else if (str_password.length()<6) {
                    Toast.makeText( RegisterActivity.this, getString(R.string.password_length),Toast.LENGTH_SHORT ).show();
                } else {
                    pd = new ProgressDialog( RegisterActivity.this );
                    pd.setMessage( getText(R.string.wait) );
                    pd.show();
                    register(str_username,"",str_email,str_password);
                }
            }
        } );
    }

    private void register(final String username, final String info, String email, String password){
        auth.createUserWithEmailAndPassword( email,password )
                .addOnCompleteListener( RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child( "usuarios" ).child( userid );

                            HashMap<String,Object> hashMap = new HashMap<>(  );
                            hashMap.put( "id",userid );
                            hashMap.put( "nombre",username );
                            hashMap.put( "info",info );
                            //hashMap.put( "bio","" );
                            //hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/instagram-clone-69e41.appspot.com/o/placeholder.png?alt=media&token=150b0541-3150-43a8-8792-7bb0fb61c2a8");

                            reference.setValue( hashMap ).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                                        startActivity( intent );
                                    }
                                }
                            } );
                        }else{
                            pd.dismiss();
                            Toast.makeText( RegisterActivity.this, getText(R.string.no_register), Toast.LENGTH_SHORT).show();
                        }
                    }
                } );
    }

}