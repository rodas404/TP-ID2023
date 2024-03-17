/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpid2023;

import static com.mycompany.tpid2023.Utils.GLOBAL_XML_FILE;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 *
 * @author footr
 */
public class GlobalXML {
    
    public static Document criaGlobalXML(Document obras, Document escritores) {
    Element globalRoot = new Element("global");

    Element obrasRoot = obras.getRootElement();
    Element escritoresRoot = escritores.getRootElement();

    // percorre a lista de escritores no escritores.xml
    List<Element> escritoresList = escritoresRoot.getChildren("escritor");
    for (Element escritor : escritoresList) {
        String nome = escritor.getAttributeValue("nome");
        String nomeCompleto = escritor.getChildText("nomeCompleto");
        String id = escritor.getAttributeValue("id");
        String nacionalidade = escritor.getChildText("nacionalidade");
        String fotografia = escritor.getChildText("fotografia");

        // cria o elemento escritor
        Element escritorElement = new Element("escritor");
        escritorElement.setAttribute("nome", nome);
        escritorElement.setAttribute("id", id);
        
        // adiciona as informações do escritor
        Element auxElement = new Element("nomeCompleto");
        auxElement.setText(nomeCompleto);
        escritorElement.addContent(auxElement);

        // adiciona as informações do escritor
        Element nacionalidadeElement = new Element("nacionalidade");
        nacionalidadeElement.setText(nacionalidade);
        escritorElement.addContent(nacionalidadeElement);

        Element fotografiaElement = new Element("fotografia");
        fotografiaElement.setText(fotografia);
        escritorElement.addContent(fotografiaElement);

        auxElement = new Element("nascimento");
        Element oldElement = escritor.getChild("nascimento");
        if(oldElement != null)
        {
            Element elemAux2 = new Element("local");
            elemAux2.setText(oldElement.getChildText("local"));            
            auxElement.addContent(elemAux2);
            
            elemAux2 = new Element("data");
            elemAux2.setText(oldElement.getChildText("data"));
            auxElement.addContent(elemAux2);
            
            elemAux2 = new Element("ano");
            elemAux2.setText(oldElement.getChildText("ano"));
            auxElement.addContent(elemAux2);            
        }
        escritorElement.addContent(auxElement);
                
        auxElement = new Element("morte");
        oldElement = escritor.getChild("morte");
        if(oldElement != null)
        {
            Element elemAux2 = new Element("local");
            elemAux2.setText(oldElement.getChildText("local"));            
            auxElement.addContent(elemAux2);
            
            elemAux2 = new Element("data");
            elemAux2.setText(oldElement.getChildText("data"));
            auxElement.addContent(elemAux2);
            
            elemAux2 = new Element("ano");
            elemAux2.setText(oldElement.getChildText("ano"));
            auxElement.addContent(elemAux2);            
        }
        escritorElement.addContent(auxElement);
        
        //percorre a lista de escritores no obras.xml
        List<Element> obrasList = obrasRoot.getChildren("escritor");
        for (Element obra : obrasList) {
            String obraNome = obra.getAttributeValue("nome");
            String obraId = obra.getAttributeValue("id");
            
            //verifica se está no sitio correto
            if (nome.equals(obraNome) && id.equals(obraId)) {
                // cria o elemento livros para armazenar cada um dos livros
                Element livrosElement = new Element("livros");
                List<Element> livrosList = obra.getChildren("livro");
                for (Element livro : livrosList) {
                    String isbn = livro.getAttributeValue("isbn");
                    String titulo = livro.getChildText("titulo");
                    String preco = livro.getChildText("preco");
                    String editora = livro.getChildText("editora");
                    String capa = livro.getChildText("capa");

                    //cria o elemento livro com as suas informações
                    Element livroElement = new Element("livro");
                    livroElement.setAttribute("isbn", isbn);
                    
                    Element tituloElement = new Element("titulo");
                    tituloElement.setText(titulo);
                    livroElement.addContent(tituloElement);

                    Element precoElement = new Element("preco");
                    precoElement.setText(preco);
                    livroElement.addContent(precoElement);

                    Element editoraElement = new Element("editora");
                    editoraElement.setText(editora);
                    livroElement.addContent(editoraElement);

                    Element capaElement = new Element("capa");
                    capaElement.setText(capa);
                    livroElement.addContent(capaElement);

                    livrosElement.addContent(livroElement);
                }

                escritorElement.addContent(livrosElement);
            }
        }

        // verifica se o escritor tem prémios
        Element premiosElement = escritor.getChild("premios");
        if (premiosElement != null) {
            List<Element> premioList = premiosElement.getChildren("premio");
            Element gPremiosElement = new Element("premios");
            if(!premioList.isEmpty())
            {
                for (Element premio : premioList) {
                    String ano = premio.getAttributeValue("ano");
                    String descricao = premio.getText();

                    // cria o elemento premio
                    Element premioElement = new Element("premio");
                    premioElement.setAttribute("ano", ano);
                    premioElement.setText(descricao);

                    gPremiosElement.addContent(premioElement);
                }
            }
            escritorElement.addContent(gPremiosElement);
        }

        globalRoot.addContent(escritorElement);
    }

    // cria e guarda o documento
    Document globalDoc = new Document(globalRoot);
    XMLJDomFunctions.escreverDocumentoParaFicheiro(globalDoc, GLOBAL_XML_FILE);

    return globalDoc;
    }
   
}
       
              
