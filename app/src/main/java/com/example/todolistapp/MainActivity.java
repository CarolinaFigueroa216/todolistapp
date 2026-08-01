package com.example.todolistapp;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    Button btnAgregar, btnCerrarSesion;
    TextView tvNombreUsuario;
    List<Tarea> lista = new ArrayList<>();
    TareaAdapter adapter;

    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        recycler = findViewById(R.id.recyclerTareas);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);

        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            tvNombreUsuario.setText("Bienvenido, " + nombreUsuario);
        } else {
            tvNombreUsuario.setText("Bienvenido");
        }

        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TareaAdapter(lista, this);
        recycler.setAdapter(adapter);

        cargarTareas();

        btnAgregar.setOnClickListener(v -> mostrarDialogo());
        
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    private void cargarTareas() {

        listenerRegistration = db.collection("tareas")
                .addSnapshotListener((value, error) -> {

                    if (error != null || value == null) {
                        return;
                    }

                    lista.clear();

                    for (DocumentSnapshot doc : value.getDocuments()) {

                        Tarea tarea = doc.toObject(Tarea.class);

                        if (tarea != null) {

                            tarea.setId(doc.getId());

                            lista.add(tarea);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    private void mostrarDialogo() {

        View view = getLayoutInflater()
                .inflate(R.layout.dialog_tarea, null);

        EditText etTitulo =
                view.findViewById(R.id.etTitulo);

        EditText etDescripcion =
                view.findViewById(R.id.etDescripcion);

        etTitulo.addTextChangedListener(
                new android.text.TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {
                    }

                    @Override
                    public void afterTextChanged(
                            android.text.Editable s) {

                        if (s.length() < 3) {
                            etTitulo.setError(
                                    "Mínimo 3 caracteres");
                        }
                    }
                });

        new AlertDialog.Builder(this)
                .setTitle("Nueva tarea")
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {

                    String titulo =
                            etTitulo.getText()
                                    .toString()
                                    .trim();

                    String descripcion =
                            etDescripcion.getText()
                                    .toString()
                                    .trim();

                    if (titulo.isEmpty()) {

                        Toast.makeText(
                                this,
                                "Ingrese un título",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    if (descripcion.isEmpty()) {

                        Toast.makeText(
                                this,
                                "Ingrese una descripción",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    btnAgregar.setEnabled(false);

                    Map<String, Object> datos =
                            new HashMap<>();

                    datos.put("titulo", titulo);
                    datos.put("descripcion", descripcion);
                    datos.put("completada", false);

                    db.collection("tareas")
                            .add(datos)
                            .addOnSuccessListener(
                                    documentReference -> {

                                        btnAgregar.setEnabled(true);

                                        Toast.makeText(
                                                this,
                                                "Tarea guardada",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    })
                            .addOnFailureListener(e -> {

                                btnAgregar.setEnabled(true);

                                Toast.makeText(
                                        this,
                                        "Error al guardar",
                                        Toast.LENGTH_SHORT
                                ).show();
                            });

                })
                .setNegativeButton(
                        "Cancelar",
                        null)
                .show();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
    
    private void cerrarSesion() {
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        finish();
    }
}
