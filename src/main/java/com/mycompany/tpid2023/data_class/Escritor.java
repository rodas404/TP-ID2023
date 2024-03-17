/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpid2023.data_class;

import com.mycompany.tpid2023.Livro;
import java.util.ArrayList;
import java.util.List;
import net.sf.saxon.instruct.ForEach;

/**
 *
 * @author AM
 */
public class Escritor { 

    public Escritor() {
        this.nome = "";
        this.nomeCompleto = "";
        this.nascimento = new Efemeride("", 0, "");
        this.morte = null;
        this.nacionalidade = "";
        this.fotografia = "";
        this.genero = new String[0];
        this.ocupacao = new String[0];
        this.premios = null;
        this.destaques = new String[0];
        this.id = 0;
        this.livros = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Efemeride getNascimento() {
        return nascimento;
    }

    public void setNascimento(Efemeride nascimento) {
        this.nascimento = nascimento;
    }

    public Efemeride getMorte() {
        return morte;
    }

    public void setMorte(Efemeride morte) {
        this.morte = morte;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public String[] getGenero() {
        return genero;
    }

    public void setGenero(String[] genero) {
        this.genero = genero;
    }

    public String[] getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(String[] ocupacao) {
        this.ocupacao = ocupacao;
    }

    public List<Premio> getPremios() {
        return premios;
    }

    public void setPremios(List<Premio> premios) {
        this.premios = premios;
    }

    public String[] getDestaques() {
        return destaques;
    }

    public void setDestaques(String[] destaques) {
        this.destaques = destaques;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
    
    public String[] getPremiosList() {
        if(premios == null)
            return new String[0];
        
        String[] lst = new String[premios.size()];
        for(int i = 0; i < premios.size(); i++)
        {
          lst[i] = Integer.toString(premios.get(i).getAno()) + " - " + premios.get(i).getNome();
        }
        return lst;
    }
    
    public String[] getLivrosList() {
        if(livros == null)
            return new String[0];
        
        String[] lst = new String[livros.size()];
        int i = 0;
        for (Livro liv : livros)
        {
            lst[i] = liv.getTitulo()  + " - " + liv.getEditora() + " - " + liv.getPreco() + " €";// [" + liv.getIsbn() + "]";
            i++;
        }
        return lst;
    }
    
    String nome;
    String nomeCompleto;
    Efemeride nascimento;
    Efemeride morte;
    String nacionalidade;
    String fotografia;
    String[] genero;
    String[] ocupacao;
    String[] destaques;
    List<Premio> premios;
    List<Livro> livros;
    int id;

    public Escritor(int id, String nome, String nomeCompleto, Efemeride nascimento, Efemeride morte, String nacionalidade, String fotografia, 
            String[] genero, String[] ocupacao, String[] destaques, List<Premio> premios) {
        
        this.nome = nome;
        this.nomeCompleto = nomeCompleto;
        this.nascimento = nascimento;
        this.morte = morte;
        this.nacionalidade = nacionalidade;
        this.genero = genero;
        this.ocupacao = ocupacao;
        this.premios = premios;
        this.destaques = destaques;
        this.id = id;
        
        if(fotografia != null && !fotografia.isEmpty() && fotografia.startsWith("//"))
        {
            fotografia = "https:" + fotografia;
        }
        this.fotografia = fotografia;
        
    }
}

