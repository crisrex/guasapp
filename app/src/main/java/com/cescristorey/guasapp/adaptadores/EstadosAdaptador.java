package com.cescristorey.guasapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cescristorey.guasapp.modelos.Estado;
import com.cescristorey.guasapp.modelos.EstadosUsuario;
import com.cescristorey.guasapp.R;
import com.cescristorey.guasapp.EstadosActivity;
import com.cescristorey.guasapp.databinding.RowEstadoBinding;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class EstadosAdaptador extends RecyclerView.Adapter<EstadosAdaptador.EstadosViewHolder> {

    Context context;
    ArrayList<EstadosUsuario> estadosUsuarios;

    public EstadosAdaptador(Context context , ArrayList<EstadosUsuario> estadosUsuarios){
        this.context = context;
        this.estadosUsuarios = estadosUsuarios;
    }

    @NonNull
    @Override
    public EstadosViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_estado, parent , false);
        return new EstadosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstadosViewHolder holder, int position) {
        EstadosUsuario estadosUsuario = estadosUsuarios.get(position);

        Estado lastEstado = estadosUsuario.getEstados().get(estadosUsuario.getEstados().size()-1);
        holder.binding.statususername.setText(estadosUsuario.getNombre());
        Glide.with(context).load(lastEstado.getUrlImagen()).into(holder.binding.statusprofile);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> misStories = new ArrayList<>();
                for (Estado estado : estadosUsuario.getEstados()){
                    misStories.add(new MyStory(estado.getUrlImagen()));
                }

                new StoryView.Builder(((EstadosActivity)context).getSupportFragmentManager())
                        .setStoriesList(misStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(estadosUsuario.getNombre()) // Default is Hidden
                        .setSubtitleText("") // Default is Hidden
                        .setTitleLogoUrl(estadosUsuario.getImagenPerfil()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return estadosUsuarios.size();
    }

    public class EstadosViewHolder extends RecyclerView.ViewHolder{
        RowEstadoBinding binding;
        public EstadosViewHolder(View itemView) {
            super(itemView);
            binding = RowEstadoBinding.bind(itemView);
        }
    }
}
