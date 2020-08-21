package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tutorialsface.customnotification.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<ControleValores> slideItens;
    private ViewPager2 viewPager2;
    private Context oContexto;
    private View view;

    public SliderAdapter(List<ControleValores> slideItens, ViewPager2 viewPager2, Context context) {
        this.slideItens = slideItens;
        this.viewPager2 = viewPager2;
        this.oContexto = context;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_carrossel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(slideItens.get(position));
        //montarCelula(slideItens.get(position));
    }

    @Override
    public int getItemCount() {
        return slideItens.size();
    }

    public View montarCelula(ControleValores pObjeto){
        if(pObjeto.getControle().toLowerCase().equals("carrossel"))
        {
            return new CarrosselCell(oContexto, pObjeto).montarCelula();
        }
         return new View(oContexto);
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        private TextView Titulo;
        private TextView Descricao;
        private ImageView imgFoto;
        private Button btnCell;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            Titulo = itemView.findViewById(R.id.Titulo);
            Descricao = itemView.findViewById(R.id.Descricao);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            btnCell = itemView.findViewById(R.id.btnCell);
        }

        public void setImage(ControleValores objetoItem){
//            int resourceId = res.getIdentifier(objetoItem.obterValor("img"), "drawable",
//                    context.getPackageName());//initialize res and context in adapter's contructor
//            imgFoto.setImageResource(resourceId);
            imgFoto.setImageResource(R.drawable.mantra_ganesha);
            Titulo.setText(objetoItem.obterValor("titulo"));
            Descricao.setText(objetoItem.obterValor("descricao"));
            btnCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent oIntent = new Intent(oContexto, MusicaActivity.class);
                    oIntent.putExtra("nomeAudio", "https://ccrma.stanford.edu/~jos/mp3/marimba.mp3");
                    oContexto.startActivity(oIntent);
                }
            });
        }
    }

}
