package com.cescristorey.guasapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cescristorey.guasapp.modelos.Mensaje;
import com.cescristorey.guasapp.R;
import com.cescristorey.guasapp.databinding.ItemRecibidoBinding;
import com.cescristorey.guasapp.databinding.ItemEnviadoBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MensajesAdaptador extends RecyclerView.Adapter{
    Context contexto;
    ArrayList<Mensaje> mensajes;

    final int ITEM_ENVIADO = 1;
    final int ITEM_RECIBIDO = 2;
    public MensajesAdaptador(Context contexto, ArrayList<Mensaje> mensajes){
        this.contexto = contexto;
        this.mensajes = mensajes;
    }

    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        if(viewType== ITEM_ENVIADO){
            View view = LayoutInflater.from(contexto).inflate(R.layout.item_enviado, parent , false);
            return new enviadoViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(contexto).inflate(R.layout.item_recibido, parent , false);
            return new recibidoViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje = mensajes.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(mensaje.getEmisorId())){
            return ITEM_ENVIADO;
        }
        else{
            return ITEM_RECIBIDO;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);
        if(holder.getClass()==(enviadoViewHolder.class)){
            enviadoViewHolder viewHolder = (enviadoViewHolder) holder;

            if(mensaje.getTextoMensaje().equals("photo")){
                viewHolder.binding.chatImage.setVisibility(View.VISIBLE);
                viewHolder.binding.mensaje.setVisibility(View.GONE);

                Glide.with(contexto).load(mensaje.getUrlImagen()).into(viewHolder.binding.chatImage);
            }
            viewHolder.binding.mensaje.setText(mensaje.getTextoMensaje());
        }
        else{

            recibidoViewHolder viewHolder = (recibidoViewHolder) holder;
            if(mensaje.getTextoMensaje().equals("photo")){
                viewHolder.binding.chatImage.setVisibility(View.VISIBLE);
                viewHolder.binding.mensaje.setVisibility(View.GONE);

                Glide.with(contexto).load(mensaje.getUrlImagen()).into(viewHolder.binding.chatImage);
            }
            viewHolder.binding.mensaje.setText(mensaje.getTextoMensaje());
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public class enviadoViewHolder extends RecyclerView.ViewHolder{
        ItemEnviadoBinding binding;

        public enviadoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemEnviadoBinding.bind(itemView);
        }
    }

    public class recibidoViewHolder extends RecyclerView.ViewHolder{
        ItemRecibidoBinding binding ;

        public recibidoViewHolder(@NonNull  View itemView) {
            super(itemView);
            binding = ItemRecibidoBinding.bind(itemView);
        }
    }
}
