package com.example.todolistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PendientesAdapter extends RecyclerView.Adapter<PendientesAdapter.ViewHolder> {

    private List<Tarea> lista;
    private Context context;

    public PendientesAdapter(List<Tarea> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView descripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tvTituloPendiente);
            descripcion = itemView.findViewById(R.id.tvDescripcionPendiente);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_pendiente, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Tarea tarea = lista.get(position);

        holder.titulo.setText(tarea.getTitulo());
        holder.descripcion.setText(tarea.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
