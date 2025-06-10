package com.example.petmeet;

import static android.content.ContentValues.TAG;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TelaActivity2 extends AppCompatActivity {

    EditText edtCadEmail, edtCadSenha1, edtCadSenha2;
    Button btnCriar;
    private FirebaseAuth maUth;
    private FirebaseUser user;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela2);

        edtCadEmail = findViewById(R.id.edtEmail);
        edtCadSenha1 = findViewById(R.id.edtCriarSenha1);
        edtCadSenha2 = findViewById(R.id.edtCriarSenha2);

        btnCriar = findViewById(R.id.btnCriar);

        maUth = FirebaseAuth.getInstance();
    }

    public void cadastrar(View view) {
        String email, senha1, senha2;

        email = edtCadEmail.getText().toString();
        senha1 = edtCadSenha1.getText().toString();
        senha2 = edtCadSenha2.getText().toString();

        if (!email.isEmpty() && !senha1.isEmpty() && !senha2.isEmpty()) {
            if (senha1.equals(senha2)) {
                if (senha1.length() >= 6) {
                    maUth.createUserWithEmailAndPassword(email, senha1)
                            .addOnCompleteListener(TelaActivity2.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Obter o usuário autenticado
                                        user = maUth.getCurrentUser();

                                        if (user != null) {
                                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task1) {
                                                    if (task1.isSuccessful()) {
                                                        Log.d(TAG, "Usuário criado com sucesso!");
                                                        Toast.makeText(TelaActivity2.this, "Para finalizar seu cadastro, por favor verifique seu email.", Toast.LENGTH_LONG).show();
                                                        //criar objeto User com os dados
                                                        User u1 = new User(email);
                                                        // Salvar objeto User no Firebase Realtime Database
                                                        myRef.child(user.getUid()).setValue(u1);

                                                        Intent it = new Intent(TelaActivity2.this, MainActivity.class);
                                                        startActivity(it);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(TelaActivity2.this, task1.getException().toString(), Toast.LENGTH_SHORT).show();
                                                        Log.v("on Complete After Send Email Verification", task1.getException().toString());
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.e(TAG, "Usuário não autenticado.");
                                            Toast.makeText(TelaActivity2.this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        if (task.getException().toString().contains("address is already in use by another account")) {
                                            Toast.makeText(TelaActivity2.this, "Usuário já cadastrado!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(TelaActivity2.this, "Falha de autenticação.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else {
                    Toast.makeText(TelaActivity2.this, "O campo senha precisa conter, pelo menos, 6 caracteres!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Senhas não coincidem, por favor digite novamente.", Toast.LENGTH_SHORT).show();
                edtCadSenha1.setText("");
                edtCadSenha2.setText("");
            }
        } else {
            Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
        }
    }
}