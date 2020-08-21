package com.example.myapplication;

import java.io.Serializable;

public class ItemValor implements Serializable {

    private String item;
    private String valor;

    public ItemValor()
    {
    }

    public ItemValor(String pItem, String pValor)
    {
        this.item = pItem;
        this.valor = pValor;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String controle) {
        this.item = controle;
    }

    public String getValor() {
        return this.valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
