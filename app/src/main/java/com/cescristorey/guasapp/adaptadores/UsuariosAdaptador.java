package com.cescristorey.guasapp.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cescristorey.guasapp.ChatActivity;
import com.cescristorey.guasapp.modelos.Usuario;
import com.cescristorey.guasapp.R;
import com.cescristorey.guasapp.databinding.RowConversacionBinding;

import java.util.ArrayList;

public class UsuariosAdaptador extends RecyclerView.Adapter<UsuariosAdaptador.UsuarioViewHolder>{

    Context contexto;
    ArrayList<Usuario> usuarios;


    public UsuariosAdaptador(Context contexto, ArrayList<Usuario> usuarios) {
        this.contexto = contexto;
        this.usuarios = usuarios;
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.row_conversacion, parent , false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        holder.binding.username.setText(usuario.getNombre());
        Glide.with(contexto).load(usuario.getImagenPerfil())
                            .placeholder(R.drawable.avatar)
                            .into(holder.binding.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto, ChatActivity.class);
                intent.putExtra("name" , usuario.getNombre());
                intent.putExtra("uid" , usuario.getUid());
                intent.putExtra("profileImage" , usuario.getImagenPerfil());
                contexto.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder{
        RowConversacionBinding binding;

        public UsuarioViewHolder(@NonNull  View itemView) {

            super(itemView);
            binding = RowConversacionBinding.bind(itemView);
        }
    }
}
