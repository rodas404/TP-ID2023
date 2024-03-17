/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpid2023;

/**
 *
 * @author footr
 */
public class Livro {
    String titulo;
    String autor;
    String capa;
    String preco;
    String isbn;
    String editora;
    
    
    public Livro(String titulo, String autor, String capa, String preco, String isbn, String editora) {
        this.titulo = titulo;
        this.autor = autor;
        this.capa = capa;
        this.preco = preco;
        this.isbn = isbn;
        this.editora = editora;
    }

    Livro() {
        this.titulo = "";
        this.autor = "";
        this.capa = "";
        this.preco = "";
        this.isbn = "";
        this.editora = "";
    }
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }
    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
    
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }
}
