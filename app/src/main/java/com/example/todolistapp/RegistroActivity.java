package com.example.todolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    EditText etNombreUsuario, etCorreo, etClaveRegistro, etConfirmarClave;
    Button btnRegistrar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = FirebaseFirestore.getInstance();

        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etClaveRegistro = findViewById(R.id.etClaveRegistro);
        etConfirmarClave = findViewById(R.id.etConfirmarClave);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String clave = etClaveRegistro.getText().toString().trim();
        String confirmarClave = etConfirmarClave.getText().toString().trim();

        // Validaciones
        if (nombreUsuario.isEmpty()) {
            etNombreUsuario.setError("Ingrese nombre de usuario");
            etNombreUsuario.requestFocus();
            return;
        }

        if (correo.isEmpty()) {
            etCorreo.setError("Ingrese correo electrónico");
            etCorreo.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Ingrese un correo válido");
            etCorreo.requestFocus();
            return;
        }

        if (clave.isEmpty()) {
            etClaveRegistro.setError("Ingrese clave");
            etClaveRegistro.requestFocus();
            return;
        }

        if (clave.length() != 10) {
            etClaveRegistro.setError("La clave debe tener exactamente 10 dígitos");
            etClaveRegistro.requestFocus();
            return;
        }

        if (confirmarClave.isEmpty()) {
            etConfirmarClave.setError("Confirme su clave");
            etConfirmarClave.requestFocus();
            return;
        }

        if (!clave.equals(confirmarClave)) {
            etConfirmarClave.setError("Las claves no coinciden");
            etConfirmarClave.requestFocus();
            return;
        }

        // Registrar en Firestore
        registrarEnFirestore(nombreUsuario, correo, clave);
    }

    private void registrarEnFirestore(String nombreUsuario, String correo, String clave) {
        btnRegistrar.setEnabled(false);

        Map<String, Object> usuario = new HashMap<>();
        usuario.put("usuario", nombreUsuario);
        usuario.put("correo", correo);
        usuario.put("clave", clave);

        db.collection("usuarios")
                .add(usuario)
                .addOnSuccessListener(documentReference -> {
                    btnRegistrar.setEnabled(true);

                    Toast.makeText(
                            RegistroActivity.this,
                            "¡Registro de usuario exitoso!",
                            Toast.LENGTH_LONG
                    ).show();

                    // Limpiar campos
                    etNombreUsuario.setText("");
                    etCorreo.setText("");
                    etClaveRegistro.setText("");
                    etConfirmarClave.setText("");

                    // Ir directamente a la interfaz de tareas (MainActivity)
                    Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                    intent.putExtra("usuario", nombreUsuario);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnRegistrar.setEnabled(true);

                    Toast.makeText(
                            RegistroActivity.this,
                            "Error al registrar: " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}
