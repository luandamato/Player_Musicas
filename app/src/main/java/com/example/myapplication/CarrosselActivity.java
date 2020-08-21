package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tutorialsface.customnotification.R;

import java.util.ArrayList;
import java.util.List;

public class CarrosselActivity extends AppCompatActivity {

    private ViewPager2 viewpager2;
    private List<ControleValores> objetosItem;
    private Activity oActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrossel);

        viewpager2 = findViewById(R.id.viewPagerImageSlider);
        oActivity = this;
        preencherItens();

        configurarClicks();
    }

    private void preencherItens(){

        ControleValores objeto1 = new ControleValores("carrossel");
        objeto1.adicionarValor("img", "mantra_esferas");
        objeto1.adicionarValor("titulo", "Celula 1");
        objeto1.adicionarValor("descricao", "Descricao da Celula 1");

        ControleValores objeto2 = new ControleValores("carrossel");
        objeto2.adicionarValor("img", "mantra_ganesha");
        objeto2.adicionarValor("titulo", "Celula 2");
        objeto2.adicionarValor("descricao", "Descricao da Celula 2");

        ControleValores objeto3 = new ControleValores("carrossel");
        objeto3.adicionarValor("img", "mantra_olho");
        objeto3.adicionarValor("titulo", "Celula 3");
        objeto3.adicionarValor("descricao", "Descricao da Celula 3");

        ControleValores objeto4 = new ControleValores("carrossel");
        objeto4.adicionarValor("img", "mantra_esferas");
        objeto4.adicionarValor("titulo", "Celula 4");
        objeto4.adicionarValor("descricao", "Descricao da Celula 4");

        ControleValores objeto5 = new ControleValores("carrossel");
        objeto5.adicionarValor("img", "mantra_om");
        objeto5.adicionarValor("titulo", "Celula 5");
        objeto5.adicionarValor("descricao", "Descricao da Celula 5");

        ControleValores objeto6 = new ControleValores("carrossel");
        objeto6.adicionarValor("img", "mantra_om");
        objeto6.adicionarValor("titulo", "Celula 6");
        objeto6.adicionarValor("descricao", "Descricao da Celula 6");
        objetosItem = new ArrayList<ControleValores>();
        objetosItem.add(objeto1);
        objetosItem.add(objeto2);
        objetosItem.add(objeto3);
        objetosItem.add(objeto4);
        objetosItem.add(objeto5);
        objetosItem.add(objeto6);

        viewpager2.setAdapter(new SliderAdapter(objetosItem, viewpager2, oActivity));
        viewpager2.setClipToPadding(false);
        viewpager2.setClipChildren(false);
        viewpager2.setOffscreenPageLimit(3);
        viewpager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer composite = new CompositePageTransformer();
        composite.addTransformer(new MarginPageTransformer(40));
        composite.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r *0.15f);
            }
        });

        viewpager2.setPageTransformer(composite);
    }

    private void configurarClicks(){
        viewpager2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oIntent = new Intent(oActivity, MusicaActivity.class);
                oIntent.putExtra("nomeAudio", "https://ccrma.stanford.edu/~jos/mp3/marimba.mp3");
                startActivity(oIntent);
            }
        });
    }
}
