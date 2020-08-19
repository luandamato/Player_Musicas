package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tutorialsface.customnotification.R;

import java.util.Locale;

public class InicialActivity extends AppCompatActivity {

    private Activity oActivity;
    private Button musica1;
    private Button musica2;
    private Button musica3;
    private Button musica4;
    private Button lingua;
    private Button carrossel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        musica1 = findViewById(R.id.musica1);
        musica2 = findViewById(R.id.musica2);
        musica3 = findViewById(R.id.musica3);
        musica4 = findViewById(R.id.musica4);
        lingua = findViewById(R.id.lingua);
        carrossel = findViewById(R.id.carrossel);
        oActivity = this;

        musica1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oIntent = new Intent(oActivity, MusicaActivity.class);
                //oIntent.putExtra("nomeAudio", "https://dl.espressif.com/dl/audio/ff-16b-2c-44100hz.mp4");
                oIntent.putExtra("nomeAudio", "https://ccrma.stanford.edu/~jos/mp3/gtr-jazz-3.mp3");
                startActivity(oIntent);
            }
        });

        musica2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oIntent = new Intent(oActivity, MusicaActivity.class);
                oIntent.putExtra("nomeAudio", "https://ccrma.stanford.edu/~jos/mp3/marimba.mp3");
                startActivity(oIntent);
            }
        });

        musica3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oIntent = new Intent(oActivity, MusicaActivity.class);
                oIntent.putExtra("nomeAudio", "https://ccrma.stanford.edu/~jos/mp3/pno-cs.mp3");
                startActivity(oIntent);
            }
        });

        musica4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oIntent = new Intent(oActivity, MusicaActivity.class);
                oIntent.putExtra("nomeAudio", "https://ccrma.stanford.edu/~jos/mp3/vln-lin-cs.mp3");
                startActivity(oIntent);
            }
        });

        carrossel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oIntent = new Intent(oActivity, CarrosselActivity.class);
                oIntent.putExtra("nomeAudio", "https://ccrma.stanford.edu/~jos/mp3/vln-lin-cs.mp3");
                startActivity(oIntent);
            }
        });

        lingua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvLinguas();
            }
        });
    }

    private void pvLinguas(){
        final String[] linguas = {"Português", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(oActivity);
        mBuilder.setTitle(R.string.escolhaLingua);
        mBuilder.setSingleChoiceItems(linguas, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i ==0){
                    setLocal("pt");
                    recreate();
                }
                else if (i ==1){
                    setLocal("en");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocal(String lang) {
        Locale local = new Locale(lang);
        Locale.setDefault(local);

        Configuration config = new Configuration();
        config.setLocale(local);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("my_lang", lang);
        editor.apply();
    }

    private void getLocal(){
        SharedPreferences pref = getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = pref.getString("my_lang", "");
        setLocal(lang);
    }
}
