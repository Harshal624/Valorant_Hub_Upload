package tfws.mobileapps.valoranthubupload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText email,password;
    private Button signin;
    public static final String SAVECRED = "SAVECRED";
    public static final String EmailShared = "Email";
    public static final String passShared = "Password";
    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        signin = (Button)findViewById(R.id.signin);
        sharedPreferences = getSharedPreferences(SAVECRED, Context.MODE_PRIVATE);
        String emailPut = sharedPreferences.getString(EmailShared,"");
        String passPut = sharedPreferences.getString(passShared,"");
        if(!emailPut.equals(null) || !emailPut.equals("")){
            email.setText(emailPut);
        }
        if(!passPut.equals(null) || !passPut.equals("")){
            password.setText(passPut);
        }


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().equals("") && password.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                }
                else if(email.getText().toString().equals("") && !password.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                }
                else if(!email.getText().toString().equals("") && password.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else{
                    String email1 = email.getText().toString().trim();
                    String password1 = password.getText().toString().trim();
                    sharedPreferences = getSharedPreferences(SAVECRED, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(EmailShared,email1);
                    editor.putString(passShared,password1);
                    editor.commit();
                    firebaseAuth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(),UploadActivity.class));

                            }
                            else{
                                Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(MainActivity.this, "Unable to sign in", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
