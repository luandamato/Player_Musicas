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
    private List<SlideItem> slideItems;
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
        slideItems = new ArrayList<SlideItem>();
        slideItems.add(new SlideItem(R.drawable.mantra_ganesha));
        slideItems.add(new SlideItem(R.drawable.mantra_ganesha));
        slideItems.add(new SlideItem(R.drawable.mantra_ganesha));
        slideItems.add(new SlideItem(R.drawable.mantra_ganesha));

        viewpager2.setAdapter(new SliderAdapter(slideItems, viewpager2));
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
