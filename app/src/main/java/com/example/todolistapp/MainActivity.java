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

    RecyclerView recycler, recyclerPendientes;
    Button btnAgregar, btnCerrarSesion;
    TextView tvNombreUsuario, tvTareasPendientesInfo, tvRolUsuario;
    List<Tarea> lista = new ArrayList<>();
    List<Tarea> listaPendientes = new ArrayList<>();
    TareaAdapter adapter;
    PendientesAdapter adapterPendientes;

    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;
    
    private String nombreUsuarioActual;
    private String rolUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        recycler = findViewById(R.id.recyclerTareas);
        recyclerPendientes = findViewById(R.id.recyclerPendientes);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvTareasPendientesInfo = findViewById(R.id.tvTareasPendientesInfo);
        tvRolUsuario = findViewById(R.id.tvRolUsuario);

        nombreUsuarioActual = getIntent().getStringExtra("nombreUsuario");
        rolUsuario = getIntent().getStringExtra("rol");
        
        if (rolUsuario == null) {
            rolUsuario = "user"; // Por defecto
        }

        if (nombreUsuarioActual != null && !nombreUsuarioActual.isEmpty()) {
            tvNombreUsuario.setText("Bienvenido, " + nombreUsuarioActual);
        } else {
            tvNombreUsuario.setText("Bienvenido");
        }
        
        tvRolUsuario.setText("Rol: " + rolUsuario.toUpperCase());

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recyclerPendientes.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TareaAdapter(lista, this, rolUsuario, nombreUsuarioActual);
        recycler.setAdapter(adapter);

        adapterPendientes = new PendientesAdapter(listaPendientes, this, rolUsuario, nombreUsuarioActual);
        recyclerPendientes.setAdapter(adapterPendientes);

        cargarTareas();

        // Solo Admin y User pueden crear tareas
        if (rolUsuario.equals("admin") || rolUsuario.equals("user")) {
            btnAgregar.setOnClickListener(v -> mostrarDialogo());
            btnAgregar.setVisibility(View.VISIBLE);
        } else {
            btnAgregar.setVisibility(View.GONE);
        }
        
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    private void cargarTareas() {

        listenerRegistration = db.collection("tareas")
                .addSnapshotListener((value, error) -> {

                    if (error != null || value == null) {
                        return;
                    }

                    lista.clear();
                    listaPendientes.clear();

                    for (DocumentSnapshot doc : value.getDocuments()) {

                        Tarea tarea = doc.toObject(Tarea.class);

                        if (tarea != null) {

                            tarea.setId(doc.getId());
                            
                            // Filtrar tareas según el rol
                            boolean mostrarTarea = false;
                            
                            if (rolUsuario.equals("admin")) {
                                // Admin ve todas las tareas
                                mostrarTarea = true;
                            } else if (rolUsuario.equals("user")) {
                                // User ve solo sus propias tareas
                                if (nombreUsuarioActual.equals(tarea.getUsuario())) {
                                    mostrarTarea = true;
                                }
                            } else if (rolUsuario.equals("viewer")) {
                                // Viewer ve todas las tareas pendientes (solo lectura)
                                if (!tarea.isCompletada()) {
                                    mostrarTarea = true;
                                }
                            }

                            if (mostrarTarea) {
                                lista.add(tarea);

                                if (!tarea.isCompletada()) {
                                    listaPendientes.add(tarea);
                                }
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    adapterPendientes.notifyDataSetChanged();
                    actualizarPanelPendientes();
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
                    datos.put("usuario", nombreUsuarioActual);

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
    
    private void actualizarPanelPendientes() {
        int pendientes = 0;
        
        for (Tarea tarea : lista) {
            if (!tarea.isCompletada()) {
                pendientes++;
            }
        }
        
        tvTareasPendientesInfo.setText(pendientes + " tarea" + (pendientes != 1 ? "s" : "") + " pendiente" + (pendientes != 1 ? "s" : ""));
    }
}
