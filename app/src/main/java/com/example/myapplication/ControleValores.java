package com.example.myapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ControleValores implements Serializable {

    private String controle;
    private List<ItemValor> valores;

    public ControleValores()
    {}

    public ControleValores(String pControle)
    {
        this.controle = pControle;
        this.valores = new ArrayList<>();
    }

    public String getControle() {
        return controle;
    }

    public void setControle(String controle) {
        this.controle = controle;
    }

    public List<ItemValor> getValores() {
        return valores;
    }

    public void setValores(List<ItemValor> valores) {
        this.valores = valores;
    }


    public boolean isControle(String pControle) {
        return this.controle != null && this.controle.toLowerCase().equals(pControle.toLowerCase());
    }

    public void limparValores() {
        this.valores = new ArrayList<>();
    }

    public void adicionarValor(String pItem, String pValor) {
        if(this.valores == null)
            this.valores = new ArrayList<>();

        this.valores.add(new ItemValor(pItem, pValor));
    }

    public String obterValor(String pItem) {
        if(this.valores != null && this.valores.size() > 0)
        {
            for (ItemValor item: this.valores) {

                if(item.getItem().toLowerCase().equals(pItem.toLowerCase()))
                {
                    return (item.getValor()!=null)?item.getValor():"";
                }
            }
        }

        return "";
    }

    public int obterValorInt(String pItem){
        String valor = this.obterValor(pItem);

        if(valor != null && !valor.isEmpty()) {
            return Integer.valueOf(valor);
        }

        return 0;
    }

    public ItemValor obterItemValor(String pItem) {
        if(this.valores != null && this.valores.size() > 0)
        {
            for (ItemValor item: this.valores) {

                if(item.getItem().toLowerCase().equals(pItem.toLowerCase()))
                {
                    return item;
                }
            }
        }

        return new ItemValor();
    }
}
