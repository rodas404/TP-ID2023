/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpid2023.data_class;

import java.util.Date;

/**
 *
 * @author AM
 */
public class Efemeride {

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    /*
    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
*/
    public Efemeride(String data, int ano, String local) {
        this.data = data;
        this.ano = ano;
        this.local = local;
        //this.pais = pais;
    }
    String data;
    int ano;    
    String local;
    //String pais;
}
