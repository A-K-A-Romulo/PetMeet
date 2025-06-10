package com.example.petmeet;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edEmail, edSenha;
    Button btnLogin;
    TextView txtCadastro;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edEmail = findViewById(R.id.edtEmail);
        edSenha = findViewById(R.id.edtSenha);
        txtCadastro = findViewById(R.id.txtCriar);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
    }

        public void logar(View view) {
            String usuario, senha;
            usuario = edEmail.getText().toString();
            senha = edSenha.getText().toString();

            if (!usuario.isEmpty() && !senha.isEmpty()) {
                mAuth.signInWithEmailAndPassword(usuario, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Obter o usuário autenticado

                            user = mAuth.getCurrentUser();

                            if (user != null && user.isEmailVerified()) {
                                // Caso tenha sucesso no login, envia para a página de Welcome
                                Log.d(TAG, "Usuário logado!");
                                Intent it = new Intent(MainActivity.this, CriarActivity2.class);
                                startActivity(it);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Verifique seu e-mail antes de logar!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Usuário e/ou senha inválidos, tente novamente.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Preencha os dados corretamente!", Toast.LENGTH_SHORT).show();
            }
        }

    public void cadastro(View view) {
        Intent it = new Intent(MainActivity.this, TelaActivity2.class);
        startActivity(it);
        finish();
    }
}
