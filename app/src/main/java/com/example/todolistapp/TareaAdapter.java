package com.example.todolistapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.ViewHolder> {

    private List<Tarea> lista;
    private Context context;
    private FirebaseFirestore db;
    private String rolUsuario;
    private String usuarioActual;

    public TareaAdapter(List<Tarea> lista, Context context, String rolUsuario, String usuarioActual) {
        this.lista = lista;
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.rolUsuario = rolUsuario;
        this.usuarioActual = usuarioActual;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView descripcion;
        CheckBox estado;

        Button btnEditar;
        Button btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tvTitulo);
            descripcion = itemView.findViewById(R.id.tvDescripcion);
            estado = itemView.findViewById(R.id.checkEstado);

            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_tarea, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Tarea tarea = lista.get(position);

        holder.titulo.setText(tarea.getTitulo());
        holder.descripcion.setText(tarea.getDescripcion());
        holder.estado.setChecked(tarea.isCompletada());

        // Controlar visibilidad de botones según el rol
        boolean puedeEditar = rolUsuario.equals("admin");
        boolean puedeEliminar = rolUsuario.equals("admin") || 
                               (rolUsuario.equals("user") && usuarioActual.equals(tarea.getUsuario()) && !tarea.isCompletada());

        if (puedeEditar) {
            holder.btnEditar.setVisibility(View.VISIBLE);
            holder.btnEditar.setOnClickListener(v -> {

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(context);

                View view = LayoutInflater.from(context)
                        .inflate(R.layout.dialog_tarea, null);

                EditText etTitulo =
                        view.findViewById(R.id.etTitulo);

                EditText etDescripcion =
                        view.findViewById(R.id.etDescripcion);

                etTitulo.setText(tarea.getTitulo());
                etDescripcion.setText(tarea.getDescripcion());

                builder.setTitle("Editar tarea");
                builder.setView(view);

                builder.setPositiveButton(
                        "Actualizar",
                        (dialog, which) -> {

                            String nuevoTitulo =
                                    etTitulo.getText()
                                            .toString()
                                            .trim();

                            String nuevaDescripcion =
                                    etDescripcion.getText()
                                            .toString()
                                            .trim();

                            if (nuevoTitulo.isEmpty()
                                    || nuevaDescripcion.isEmpty()) {
                                return;
                            }

                            db.collection("tareas")
                                    .document(tarea.getId())
                                    .update(
                                            "titulo",
                                            nuevoTitulo,
                                            "descripcion",
                                            nuevaDescripcion
                                    );
                        });

                builder.setNegativeButton(
                        "Cancelar",
                        null);

                builder.show();
            });
        } else {
            holder.btnEditar.setVisibility(View.GONE);
        }

        if (puedeEliminar) {
            holder.btnEliminar.setVisibility(View.VISIBLE);
            holder.btnEliminar.setOnClickListener(v -> {

                db.collection("tareas")
                        .document(tarea.getId())
                        .delete();
            });
        } else {
            holder.btnEliminar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}