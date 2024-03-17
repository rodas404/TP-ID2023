/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpid2023.xml_class;

import com.mycompany.tpid2023.data_class.Escritor;
import com.mycompany.tpid2023.data_class.Premio;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import net.sf.saxon.exslt.Date;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 *
 * @author AM
 */
public class EscritorModeloXML {
    
    public static Document adicionaEscritor (Escritor escritorObj, Document doc)
    {
        Element raiz;
        if(doc == null)
        {
            raiz = new Element("escritores");
            doc = new Document(raiz);
        }
        else
        {
            raiz = doc.getRootElement();
        }
        
        Element eEscritor = new Element("escritor");
        
        Attribute nome = new Attribute("nome", escritorObj.getNome());
        Attribute id = new Attribute("id", Integer.toString(escritorObj.getId()));

        eEscritor.setAttribute(nome);
        eEscritor.setAttribute(id);
        
        Element filho = new Element("nomeCompleto").addContent(escritorObj.getNomeCompleto());
        eEscritor.addContent(filho);
        
        if(escritorObj.getNacionalidade()!= null)
        {
            filho = new Element("nacionalidade").addContent(escritorObj.getNacionalidade());
            eEscritor.addContent(filho);
        }
        
        if(escritorObj.getFotografia()!= null)
        {
            filho = new Element("fotografia").addContent(escritorObj.getFotografia());
            eEscritor.addContent(filho);
        }
        
        // Nascimento
        Element filho2;
        filho = new Element("nascimento");
        if(escritorObj.getNascimento().getData() != null)
        {
            filho2 = new Element("data").addContent(escritorObj.getNascimento().getData());
            filho.addContent(filho2);
        }
        filho2 = new Element("ano").addContent(Integer.toString(escritorObj.getNascimento().getAno()));
        filho.addContent(filho2);
        filho2 = new Element("local").addContent(escritorObj.getNascimento().getLocal());
        filho.addContent(filho2);
        eEscritor.addContent(filho);
        
        // Morte
        if(escritorObj.getMorte() != null)
        {
            filho = new Element("morte");
            if(escritorObj.getMorte().getData() != null)
            {
                filho2 = new Element("data").addContent(escritorObj.getMorte().getData());
                filho.addContent(filho2);
            }
            filho2 = new Element("ano").addContent(Integer.toString(escritorObj.getMorte().getAno()));
            filho.addContent(filho2);
            filho2 = new Element("local").addContent(escritorObj.getMorte().getLocal());
            filho.addContent(filho2);
            eEscritor.addContent(filho);
        }
        
        // Generos
        String[] coll = escritorObj.getGenero();
        if(coll != null)
        {
            filho = new Element("generos");
            System.out.println(coll.length);
            for(int i = 0; i < coll.length; i++)
            {
                if(coll[i] != null && !coll[i].isEmpty())
                {
                    filho2 = new Element("genero").addContent(coll[i]);
                    filho.addContent(filho2);
                }
            }
            eEscritor.addContent(filho);            
        }
        
        // Ocupacoes
        if(escritorObj.getOcupacao()!= null)
        {
            filho = new Element("ocupacoes");        
            for(int i = 0; i<escritorObj.getOcupacao().length; i++)
            {
                String value = escritorObj.getOcupacao()[i];
                if( value != null && !value.isEmpty()
                   && value != "" && !value.isBlank() && !value.equals(""))
                {
                    filho2 = new Element("ocupacao").addContent(value);
                    filho.addContent(filho2);
                }
            }
            eEscritor.addContent(filho);            
        }
        
        // Destaques
        if(escritorObj.getDestaques()!= null)
        {
            filho = new Element("obrasEmDestaque");        
            for(int i = 0; i<escritorObj.getDestaques().length; i++)
            {
                if(escritorObj.getDestaques()[i] != null && !escritorObj.getDestaques()[i].isEmpty())
                {
                    filho2 = new Element("obra").addContent(escritorObj.getDestaques()[i]);
                    filho.addContent(filho2);
                }
            }
            eEscritor.addContent(filho);            
        }
        
        // Premios
        if(escritorObj.getPremios() != null)
        {
            filho = new Element("premios"); 
            for(int i = 0; i< escritorObj.getPremios().size(); i++)
            {
                Premio prem = escritorObj.getPremios().get(i);                
                Element filho3 = new Element("premio");
                Attribute ano = new Attribute("ano", Integer.toString(prem.getAno()));  
                filho3.setAttribute(ano);
                filho3.addContent(prem.getNome());
                filho.addContent(filho3);
            }
            eEscritor.addContent(filho); 
        }   
        
        raiz.addContent(eEscritor);
        
        return doc;
    }
    
    public static Document removeEscritor (String autorNome, Document doc)
    {        
        /*Element raiz;
        if(doc == null)
        {
            System.out.print("Ficheiro não existe - nao dá para remover informação.");
            return doc;
        }
        else
        {
            raiz = doc.getRootElement();
        }
                
        List todosLivros = raiz.getChildren("livro");
        
        boolean found = false;
        for(int i=0; i<todosLivros.size();i++){
            Element livro = (Element)todosLivros.get(i); //obtem livro i da Lista
            if (livro.getChild("autor").getText().contains(autorNome)){
                livro.getParent().removeContent(livro);
                System.out.println("Livro removido com sucesso!");
                found = true;
            }
        }
        
        if(!found){
            System.out.println("Autor " + autorNome + " não foi encontrado");
            return null;
        }
        */
        return doc;       
    }
    /*
    public static Document removeLivroISBN(String isbn, Document doc)
    {
                Element raiz;
        if(doc == null)
        {
            System.out.print("Ficheiro não existe - nao dá para remover informação.");
            return doc;
        }
        else
        {
            raiz = doc.getRootElement();
        }
        
        List todosLivros = raiz.getChildren("livro");
        
        boolean found = false;
        for(int i=0; i<todosLivros.size();i++){
            Element livro = (Element)todosLivros.get(i); //obtem livro i da Lista
            if (livro.getChild("autor").getText().contains(isbn)){
                livro.getParent().removeContent(livro);
                System.out.println("Livro removido com sucesso!");
                found = true;
            }
        }
        
        if(!found){
            System.out.println("Autor " + isbn + " não foi encontrado");
            return null;
        }
        
        return doc;      
    }   
    
    
    public static Document alteraPrecoLivro (String isbn, double novoPreco, String loja, Document doc)
    {        
        Element raiz;
        if(doc == null)
        {
            System.out.print("Ficheiro não existe - nao dá para remover informação.");
            return doc;
        }
        else
        {
            raiz = doc.getRootElement();
        }
        
        List todosLivros = raiz.getChildren("livro");
        
        boolean found = false;
        for(int i=0; i<todosLivros.size();i++){
            Element livro = (Element)todosLivros.get(i); //obtem livro i da Lista
            //System.out.println(livro.getAttributeValue("isbn") + " <> isbn: " + isbn);
            if (livro.getAttributeValue("isbn").equals(isbn)){
                String titulo = livro.getChildText("titulo");
                System.out.println("Titulo: " + titulo);
                
                List precos = livro.getChildren("preco");
                for(int j=0; j<precos.size();j++){
                    Element p = (Element)precos.get(j);
                    if(p.getAttributeValue("store").equals(loja) ){  
                        found = true;
                        System.out.println("Preco: " + p.getValue()); 
                        p.setText( String.valueOf(novoPreco));
                    }
                }
            }
        }
        
        if(!found){
            System.out.println("O livro com ISBN  " + isbn + " não foi encontrado");
            return null;
        }
        
        return doc;        
    }*/
    
}
