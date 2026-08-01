package com.example.todolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText etUsuarioLogin, etClaveLogin;
    Button btnIngresar, btnIrRegistro;
    Spinner spinnerRol;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        etUsuarioLogin = findViewById(R.id.etUsuarioLogin);
        etClaveLogin = findViewById(R.id.etClaveLogin);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);
        spinnerRol = findViewById(R.id.spinnerRol);

        // Configurar spinner de roles
        String[] roles = {"admin", "user", "viewer"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapter);

        // Botón para ir a registro
        btnIrRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        // Botón Ingresar
        btnIngresar.setOnClickListener(v -> {
            String usuario = etUsuarioLogin.getText().toString().trim();
            String clave = etClaveLogin.getText().toString().trim();
            String rol = spinnerRol.getSelectedItem().toString();

            if (usuario.isEmpty()) {
                etUsuarioLogin.setError("Ingrese su usuario");
                etUsuarioLogin.requestFocus();
                return;
            }

            if (clave.isEmpty()) {
                etClaveLogin.setError("Ingrese su clave");
                etClaveLogin.requestFocus();
                return;
            }

            // Validar usuario en Firestore
            validarUsuario(usuario, clave, rol);
        });
    }

    private void validarUsuario(String usuario, String clave, String rol) {
        db.collection("usuarios")
                .whereEqualTo("usuario", usuario)
                .whereEqualTo("clave", clave)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Usuario válido, ir a MainActivity
                        Toast.makeText(
                                LoginActivity.this,
                                "¡Bienvenido " + usuario + "!",
                                Toast.LENGTH_SHORT
                        ).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("nombreUsuario", usuario);
                        intent.putExtra("rol", rol);
                        startActivity(intent);
                        finish();
                    } else {
                        // Usuario o clave incorrectos
                        Toast.makeText(
                                LoginActivity.this,
                                "Usuario o clave incorrectos",
                                Toast.LENGTH_SHORT
                        ).show();

                        etClaveLogin.setText("");
                        etClaveLogin.requestFocus();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            LoginActivity.this,
                            "Error al validar: " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}
