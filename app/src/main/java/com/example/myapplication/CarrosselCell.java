package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tutorialsface.customnotification.R;

/**
 * Created by Andreia on 05/07/18.
 */

public class CarrosselCell {

    private Resources res = null;
    private Context oContexto;
    private ControleValores objetoItem;

    //private Object objetoItem;

    //Passo 1 - Criar declaração controles
    private View viewPrincipal;

    private TextView Titulo;
    private TextView Descricao;
    private ImageView imgFoto;




    //Passo 2 - Alterar nome do construtor
    public CarrosselCell(Context pContexto, ControleValores pObjeto)
    {
        oContexto = pContexto;
        this.objetoItem = pObjeto;
    }

    public View montarCelula()
    {
        LayoutInflater li = (LayoutInflater) oContexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Passo 3 - Alterar nome do layout
        viewPrincipal = (View) li.inflate(R.layout.cell_carrossel, null);

        obterControles();

        preencherControles();

        return viewPrincipal;
    }

    private void obterControles()
    {
        viewPrincipal = (View)viewPrincipal.findViewById(R.id.viewPrincipal);

        Titulo = (TextView) viewPrincipal.findViewById(R.id.Titulo);
        Descricao = (TextView) viewPrincipal.findViewById(R.id.Descricao);
        imgFoto = (ImageView) viewPrincipal.findViewById(R.id.imgFoto);
    }

    private void preencherControles()
    {
        preencherLabels();

        preencherButton();

        preencherImages();

        preencherClicks();

        logicasEspecias();
    }

    private void preencherLabels()
    {
        String t = objetoItem.obterValor("titulo");
        Titulo.setText(objetoItem.obterValor("titulo"));
        Descricao.setText(objetoItem.obterValor("descricao"));
    }

    private void preencherButton()
    {

    }

    private void preencherImages()
    {
        //imgFoto.imageFromUrl(objetoItem.obterValor("img"), false, true);
//        int resourceId = res.getIdentifier(objetoItem.obterValor("img"), "drawable",
//                oContexto.getPackageName());//initialize res and context in adapter's contructor
//        imgFoto.setImageResource(resourceId);
    }

    private void preencherClicks()
    {

    }

    private void logicasEspecias()
    {

    }
}
