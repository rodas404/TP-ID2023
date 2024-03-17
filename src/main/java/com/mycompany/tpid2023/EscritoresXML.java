/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpid2023;

import static com.mycompany.tpid2023.Utils.ESCRITORES_XML_FILE;
import com.mycompany.tpid2023.data_class.Escritor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 *
 * @author footr
 */
public class EscritoresXML {
    
    public static Document adicionaEscritor(Livro liv, Document doc, Document escritoresDoc) {
    Element raiz;
    if (doc == null) {
        raiz = new Element("obras");
        doc = new Document(raiz);
    } else {
        raiz = doc.getRootElement();
    }

    // verifica se o escritor já existe no documento
    Element escritor = encontrarEscritor(raiz, liv.getAutor());

    if (escritor == null) {
        // Se não existir, cria
        escritor = new Element("escritor");
        escritor.setAttribute("nome", liv.getAutor());

        // encontra o identificador no outro documento xml
        String idEscritor = obterIdEscritor(escritoresDoc.getRootElement(), liv.getAutor());
        if (idEscritor != null) {
            escritor.setAttribute("id", idEscritor);
        }
        else{
            escritor.setAttribute("id", "N/A");
            
        }

        raiz.addContent(escritor);
    }

    // Cria o elemento livro e adiciona os atributos e elementos filho
    Element livro = new Element("livro");
    livro.setAttribute("isbn", liv.getIsbn());
    
    Element titulo = new Element("titulo").addContent(liv.getTitulo());
    Element preco = new Element("preco").addContent(liv.getPreco());
    Element editora = new Element("editora").addContent(liv.getEditora());
    Element capa = new Element("capa").addContent(liv.getCapa());
    
    livro.addContent(titulo);
    livro.addContent(preco);
    livro.addContent(editora);
    livro.addContent(capa);

    escritor.addContent(livro);

    return doc;
}

private static Element encontrarEscritor(Element raiz, String autor) {
    List<Element> escritores = raiz.getChildren("escritor"); 
    for (Element escritor : escritores) {
        if (escritor.getAttributeValue("nome").equals(autor)) {
            return escritor;
        }
    }
    return null;
}

public static void completarIdsEscritores(Document obrasXML, Document escritoresXML) {
    Element raizObras = obrasXML.getRootElement();
    Element raizEscritores = escritoresXML.getRootElement();
    List<Element> escritores = raizObras.getChildren("escritor");

    for (Element escritor : escritores) {
        String id = escritor.getAttributeValue("id");

        if (id.equals("incompleto")) {
            String autor = escritor.getAttributeValue("nome");
            String novoId = obterIdEscritor(raizEscritores, autor);

            if (novoId != null) {
                escritor.getAttribute("id").setValue(novoId);
            }
        }
    }
}



private static String obterIdEscritor(Element raiz, String autor) {
    List<Element> escritores = raiz.getChildren("escritor");
    for (Element escritor : escritores) {
        String nome = escritor.getAttributeValue("nome");
        if (nome != null && nome.equals(autor)) {
            return escritor.getAttributeValue("id");
        }
    }
    return null;
}



public static Document removeEscritor(String procura, Document doc) {
    Element raiz;
    if (doc == null) {
        System.out.println("Não existe ficheiro XML");
        return null;
    } else {
        raiz = doc.getRootElement();
    }
    List<Element> todosEscritores = new ArrayList<>(raiz.getChildren("escritor"));

    boolean found = false;
    for (Element escritor : todosEscritores) {
        String nome = escritor.getAttributeValue("nome");
    if (nome != null && nome.contains(procura)) {
        escritor.getParent().removeContent(escritor);
        System.out.println("Escritor removido com sucesso!");
        found = true;
        }
    }

    if (!found) {
    System.out.println("Autor " + procura + " não foi encontrado");
    return null;
    }

    return doc;

    }

}