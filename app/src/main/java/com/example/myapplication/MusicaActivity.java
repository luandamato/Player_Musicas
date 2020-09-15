package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tutorialsface.customnotification.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MusicaActivity extends AppCompatActivity implements NotificationService.ServiceCallbacks {

    private Activity oActivity;

    private ImageButton btnVoltar;
    private ImageButton btnPlay;
    private ImageButton btnAvancar;
    private TextView TxtTempoAtual;
    private TextView txtTempoTotal;
    private SeekBar seekTempo;
    private TextView txtArtista;
    private TextView txtMusica;

    private ImageButton btnFechar;
    private Switch swtLoop;
    private Button btnDownload;

    private String fileName;
    private MediaPlayer player;

    private Uri audioSelecionado;
    private String nomeAudio;
    private String statusAudio = "";

    private long startTime = 0;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private Boolean rodando = true;
    private int tempoRestante = 0;
    private int tempoCorrido = 0;
    private int tempoTotal = 0;

    private String downloadAudioPath;
    private String urlDownloadLink = "";

    private NotificationService myService;
    private boolean bound = false;

    @Override
    protected void onStart() {
        super.onStart();
        // bind to Service
        Intent intent = new Intent(this, NotificationService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oActivity = this;
        btnVoltar = findViewById(R.id.btnVoltar);
        btnPlay = findViewById(R.id.btnPlay);
        btnAvancar = findViewById(R.id.btnAvancar);
        TxtTempoAtual = findViewById(R.id.TxtTempoAtual);
        txtTempoTotal = findViewById(R.id.txtTempoTotal);
        seekTempo = findViewById(R.id.seekTempo);
        txtArtista = findViewById(R.id.txtArtista);
        txtMusica = findViewById(R.id.txtMusica);
        btnFechar = findViewById(R.id.btnFechar);
        swtLoop = findViewById(R.id.swtLoop);
        btnDownload = findViewById(R.id.btnDownload);

        obterExtras();

        configurarClicks();

        configurarTimer();

        iniciarMusica();



    }

    @Override
    protected void onDestroy() {
        stopPlaying();
        super.onDestroy();

    }

    private void iniciarMusica(){
        int REQUEST_CODE=1;

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);

        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(downloadAudioPath + File.separator + "voices");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }
        playRecording();
        Intent serviceIntent = new Intent(MusicaActivity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }

    private void obterExtras()
    {
        /*
         * Recupera os itens passados para a próxima tela
         * */

        if(this.getIntent().getExtras() != null && this.getIntent().getExtras().size() > 0)
        {
            if(this.getIntent().getExtras().containsKey("nomeAudio"))
            {
                nomeAudio = this.getIntent().getStringExtra("nomeAudio");
            }
        }
    }

    private void configurarClicks()
    {

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusAudio.isEmpty())
                {
                    audioStart();

                }
                else if(statusAudio.toLowerCase().equals("ouvindo")) {
                    audioPause();
                }
            }
        });

        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioStop();
                finish();
            }
        });

        btnAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avancar();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuar();
            }
        });

        swtLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                player.setLooping(swtLoop.isChecked());
            }
        });

        seekTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TxtTempoAtual.setText(String.valueOf(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //salvarInformacoes();
                player.seekTo(seekTempo.getProgress()*1000);
                tempoCorrido = seekTempo.getProgress();
                tempoRestante = tempoTotal - tempoCorrido;
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });
    }


    public void audioPause(){
        this.statusAudio =  "";
        timerHandler.removeCallbacks(timerRunnable);
        btnPlay.setImageResource(R.drawable.img_play);
        player.pause();
    }



    public void audioStart() {

        this.statusAudio =  "ouvindo";
        btnPlay.setImageResource(R.drawable.img_pause);
        timerHandler.postDelayed(timerRunnable, 1000);
        player.start();
    }

    private void avancar(){
        if (tempoCorrido - 15 >= tempoTotal){
            tempoCorrido = tempoTotal - 1;
        }else{
            tempoCorrido += 15;
        }
        player.seekTo(tempoCorrido*1000);
        seekTempo.setProgress(tempoCorrido);
    }

    private void recuar(){
        if (tempoCorrido - 15 <=0){
            tempoCorrido = 0;
        }else{
            tempoCorrido -= 15;
        }
        player.seekTo(tempoCorrido*1000);
        seekTempo.setProgress(tempoCorrido);
    }

    public void audioStop() {

        this.statusAudio =  "";

        stopPlaying();
    }

    public void playRecording() {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            urlDownloadLink = nomeAudio;
            String filename = extractFilename();
            downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + filename;
            File fileName = new File(downloadAudioPath);
            if (fileName.exists()){
                Toast.makeText(this, "Baixado", Toast.LENGTH_SHORT).show();
                player.setDataSource(downloadAudioPath);
            }else{
                Toast.makeText(this, "Online", Toast.LENGTH_LONG).show();
                player.setDataSource(nomeAudio);
            }



            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    audioStop();
                }
            });

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    audioStart();
                }
            });

            player.prepare();
            player.seekTo(0);
            tempoTotal = player.getDuration()/1000;
            tempoRestante = player.getDuration()/1000;
            seekTempo.setMax(tempoTotal);
            startTime = System.currentTimeMillis();
            int seconds = tempoTotal;
            int minutes = seconds / 60;
            seconds = seconds % 60;


            txtTempoTotal.setText(String.format("%02d:%02d", minutes, seconds));



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }


    }

    private void configurarTimer(){
        timerHandler = new Handler();

        timerRunnable = new Runnable() {


            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = tempoTotal - (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                int secondsCorridos = tempoCorrido;
                int minutesCorridos = secondsCorridos / 60;
                secondsCorridos = secondsCorridos % 60;
                String txt = String.format("%02d:%02d", minutesCorridos, secondsCorridos);
                TxtTempoAtual.setText(txt);
                seekTempo.setProgress(tempoCorrido);
                if (tempoCorrido >= tempoTotal -1){
                    logicasEspeciais();
                }else{
                    tempoCorrido ++;

                    timerHandler.postDelayed(this, 1000);
                }
            }
        };
    }

    private void logicasEspeciais(){
        if (!swtLoop.isChecked()){
            audioStop();
            finish();
        }else{
            tempoRestante = tempoTotal;
            tempoCorrido = 0;
            seekTempo.setProgress(0);
            TxtTempoAtual.setText("00:00");
            timerHandler.postDelayed(timerRunnable, 1000);
        }
    }

    private void startDownload() {
        urlDownloadLink = nomeAudio;
        String filename = extractFilename();
        //downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + filename;
        DownloadFile downloadAudioFile = new DownloadFile();
        downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);

//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(nomeAudio));
//        request.setDescription("Downloading");
//        request.setTitle("File :");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        }
//        request.setMimeType("audio/MP3");
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "audio.mp3");
//        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        manager.enqueue(request);
    }

    private class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... url) {
            int count;
            try {
                URL urls = new URL(url[0]);
                URLConnection connection = urls.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(urls.openStream());
                OutputStream output = new FileOutputStream(url[1]);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private void dowloadSucesso(){
        Toast.makeText(oActivity, "Download Sucesso", Toast.LENGTH_SHORT).show();
    }
    private void dowloadFalha(){
        Toast.makeText(oActivity, "Falha no Download", Toast.LENGTH_SHORT).show();
    }

    private String extractFilename(){
        if(urlDownloadLink.equals("")){
            return "";
        }
        String newFilename = "";
        if(urlDownloadLink.contains("/")){
            int dotPosition = urlDownloadLink.lastIndexOf("/");
            newFilename = urlDownloadLink.substring(dotPosition + 1, urlDownloadLink.length());
        }
        else{
            newFilename = urlDownloadLink;
        }
        return newFilename;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
            myService = binder.getService();
            bound = true;
            myService.setCallbacks(MusicaActivity.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    /* Defined by ServiceCallbacks interface */
    @Override
    public void pause() {
        audioPause();
    }

    @Override
    public void play() {
        audioStart();
    }

    @Override
    public void avancarN() {
        avancar();
    }

    @Override
    public void voltarN() {
        recuar();
    }

}