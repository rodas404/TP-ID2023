/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpid2023;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author footr
 */
public class WrapperBertrand {
    
    
    
   public static List<String> obtem_links(String autor) throws IOException{
       HttpRequestFunctions.httpRequest1("https://www.bertrand.pt/pesquisa/", autor, "livros.txt");
       String er = "<a class=\"title-lnk track\" href=\"([^\"]+)\">";
       
       Pattern p = Pattern.compile(er);
        Matcher m;
        List<String> links = new ArrayList<>();

        try (Scanner ler = new Scanner(new FileInputStream("livros.txt"))) {
            while (ler.hasNextLine()) {
                String linha = ler.nextLine();
                m = p.matcher(linha);
                if (m.find()) {
                    links.add(m.group(1));
                } 
            }
        }
       return links;
   }
   
   public static List<String> obtem_titulos(List<String> links) throws IOException {
    List<String> titulos = new ArrayList<>();
    String er = "<div class=\"right-title-details\"[^>]*>([^<]+)</div>";
    Pattern p = Pattern.compile(er);
    Matcher m;

    for (String link : links) {
        URL url = new URL("https://www.bertrand.pt" + link);
        HttpRequestFunctions.httpRequest1(url.toString(), "", "livro2.txt");

        try (Scanner ler = new Scanner(new FileInputStream("livro2.txt"))) {
            boolean tituloEncontrado = false;
            while (ler.hasNextLine()) {
                String linha = ler.nextLine();
                m = p.matcher(linha);
                if (m.find()) {
                    titulos.add(m.group(1));
                    tituloEncontrado = true;
                    break;
                }
            }
            if (!tituloEncontrado) {
                titulos.add("N/A titulo");
            }
        }
    }

    return titulos;
}





  public static List<String> obtem_autores(List<String> links) throws IOException {
    List<String> autores = new ArrayList<>();
    String er = "<div class=\"right-author\"[^>]*>de <a[^>]*>([^<]+)</a>";
    Pattern p = Pattern.compile(er);
    Matcher m;

    for (String link : links) {
        URL url = new URL("https://www.bertrand.pt" + link);
        HttpRequestFunctions.httpRequest1(url.toString(), "", "livro2.txt");

        try (Scanner ler = new Scanner(new FileInputStream("livro2.txt"))) {
            boolean autorEncontrado = false;
            while (ler.hasNextLine()) {
                String linha = ler.nextLine();
                m = p.matcher(linha);
                if (m.find()) {
                    autores.add(m.group(1));
                    autorEncontrado = true;
                    break;
                }
            }
            if (!autorEncontrado) {
                autores.add("N/A autor");
            }
        }
    }

    return autores;
    }

  
  
  public static List<String> obtem_isbn(List<String> links) throws IOException {
    List<String> isbn = new ArrayList<>();
    String er = "\"isbn\":\"([0-9-]+)\"";
    Pattern p = Pattern.compile(er);
    Matcher m;
    for (String link : links) {
        URL url = new URL("https://www.bertrand.pt" + link);
        HttpRequestFunctions.httpRequest1(url.toString(), "", "livro2.txt");

        try (Scanner ler = new Scanner(new FileInputStream("livro2.txt"))) {
            boolean isbnEncontrado = false;
            while (ler.hasNextLine()) {
                String linha = ler.nextLine();
                m = p.matcher(linha);
                if (m.find()) {
                    isbn.add(m.group(1));
                    isbnEncontrado = true;
                    break;
                }
            }
            if (!isbnEncontrado) {
                isbn.add("N/A isbn");
            }
        }
    }

    return isbn;
}
  
  public static List<String> obtem_editoras(List<String> links) throws IOException {
    List<String> editoras = new ArrayList<>();
    String er = "Editor:\\s*<div class=\"info\">([^<]+)</div>";
    Pattern p = Pattern.compile(er);
    Matcher m;

    for (String link : links) {
        URL url = new URL("https://www.bertrand.pt" + link);
        HttpRequestFunctions.httpRequest1(url.toString(), "", "livro2.txt");

        try (Scanner ler = new Scanner(new FileInputStream("livro2.txt"))) {
            boolean editoraEncontrado = false;
            while (ler.hasNextLine()) {
                String linha = ler.nextLine();
                m = p.matcher(linha);
                if (m.find()) {
                    editoras.add(m.group(1));
                    editoraEncontrado = true;
                    break;
                }
            }
            if (!editoraEncontrado) {
                editoras.add("N/A editora");
            }
        }
    }

    return editoras;
}
  
  public static List<String> obtem_capas(List<String> links) throws IOException {
    List<String> capas = new ArrayList<>();
    String er = "<meta property=\"og:image\" content=\"([^\"]+)\" />";
    Pattern p = Pattern.compile(er);
    Matcher m;

    for (String link : links) {
        URL url = new URL("https://www.bertrand.pt" + link);
        HttpRequestFunctions.httpRequest1(url.toString(), "", "livro2.txt");

        try (Scanner ler = new Scanner(new FileInputStream("livro2.txt"))) {
            boolean capaEncontrado = false;
            while (ler.hasNextLine()) {
                String linha = ler.nextLine();
                m = p.matcher(linha);
                if (m.find()) {
                    
                    capas.add(m.group(1));
                    capaEncontrado = true;
                    break;
                }
            }
            if (!capaEncontrado) {
                capas.add("N/A capa");
            }
        }
    }

    return capas;
}
  
  public static List<String> obtem_precos(List<String> links) throws IOException {
    List<String> preco = new ArrayList<>();
    String er = "<div[^>]*id=\"productPageRightSectionTop-saleAction-price-current\">\\s*([0-9,.]+)€</div>";
    Pattern p = Pattern.compile(er);
    Matcher m;

    for (String link : links) {
        
        URL url = new URL("https://www.bertrand.pt" + link);
        HttpRequestFunctions.httpRequest1(url.toString(), "", "livro2.txt");

        try (Scanner ler = new Scanner(new FileInputStream("livro2.txt"))) {
            boolean precoEncontrado = false;
            while (ler.hasNextLine()) {
                String linha = ler.nextLine();
                m = p.matcher(linha);
                if (m.find()) {
                    
                    preco.add(m.group(1));
                    precoEncontrado = true;
                    break;
                }
            }
            if (!precoEncontrado) {
                preco.add("N/A preco");
            }
        }
    }

    return preco;
}

  
 public static List<Livro> criaLivro(List<String> links, String autorDesejado) throws IOException {
    List<Livro> livros = new ArrayList<>();

    List<String> titulos = obtem_titulos(links);
    List<String> autores = obtem_autores(links);
    List<String> capas = obtem_capas(links);
    List<String> precos = obtem_precos(links);
    List<String> editoras = obtem_editoras(links);
    List<String> isbn = obtem_isbn(links);

    int numLivros = titulos.size();
    int contaLivros = 0; 

    for (int i = 0; i < numLivros; i++) {
        String autor = autores.get(i);

        if (autor.equals(autorDesejado)) {
            String titulo = titulos.get(i);
            String capa = capas.get(i);
            String preco = precos.get(i);
            String editora = editoras.get(i);
            String isbnLivro = isbn.get(i);

            Livro livro = new Livro(titulo, autor, capa, preco, isbnLivro, editora);
            livros.add(livro);

            contaLivros++;

            if (contaLivros >= 5) { 
                break; 
            }
        }
    }

    if (contaLivros <= 1) {
        livros.clear();
        System.out.println("O autor " + autorDesejado + " não tem livros suficientes.");
    }

    return livros;
}




 




}
