/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tpid2023;

import static com.mycompany.tpid2023.Utils.ESCRITORES_XML_FILE;
import static com.mycompany.tpid2023.Utils.OBRAS_XML_FILE;
import static com.mycompany.tpid2023.Utils.GLOBAL_XML_FILE;
import com.mycompany.tpid2023.data_class.Escritor;
import com.mycompany.tpid2023.EscritorModeloXML;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import net.sf.saxon.s9api.SaxonApiException;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 *
 * @author AM
 */
public class TPID2023 {

    public static void main(String[] args) throws FileNotFoundException, IOException, SaxonApiException {
        
        MainFrame app = new MainFrame();
        app.setTitle("TP ID 2022/2023 by AM and RB ");
        app.setExtendedState( app.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        app.setVisible(true);
        
        //wrapper individual wiki
        //WrapperWikipedia.Teste("Haruki Murakami");
        
        //wrappers grupo bertrand
        /*String escritores = "escritores.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(escritores))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String autor = linha.trim();
                List<String> links = WrapperBertrand.obtem_links(autor);
                List<Livro> livros = WrapperBertrand.criaLivro(links,autor);
                System.out.println("Livros encontrados para o autor " + autor + ":");
                    for (Livro livro : livros) {
                        System.out.println("Título: " + livro.getTitulo());
                        System.out.println("Autor: " + livro.getAutor());
                        System.out.println("Capa: " + livro.getCapa());
                        System.out.println("Preço: " + livro.getPreco());
                        System.out.println("ISBN: " + livro.getIsbn());
                        System.out.println("Editora: " + livro.getEditora());
                        System.out.println();
                    }
                System.out.println("----------------------------------------------");
            }
        } */
       
    //wrappers individual bertrand 
    /*String autor = "Daniel Silva";
    try {
        List<String> links = WrapperBertrand.obtem_links(autor);
        List<Livro> livros = WrapperBertrand.criaLivro(links,autor);
        
        for (Livro livro : livros) {
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + livro.getAutor());
            System.out.println("Capa: " + livro.getCapa());
            System.out.println("Preço: " + livro.getPreco());
            System.out.println("ISBN: " + livro.getIsbn());
            System.out.println("Editora: " + livro.getEditora());
            System.out.println();
        }
    } catch (IOException e) {
    }*/
       
    //xml global
    /*Document obrasXML = XMLJDomFunctions.lerDocumentoXML(OBRAS_XML_FILE); 
    Document escritoresXML = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);
    GlobalXML.criaGlobalXML(obrasXML, escritoresXML);*/
    
    //xml individual adiciona autor bertrand
      /*String autor = "Oscar Wilde";

        List<String> links = WrapperBertrand.obtem_links(autor);
        List<Livro> livrosEncontrados = WrapperBertrand.criaLivro(links, autor);

        if (!livrosEncontrados.isEmpty()) {  
            Document obrasXML = XMLJDomFunctions.lerDocumentoXML(OBRAS_XML_FILE); 
            Document escritoresXML = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);

            for (Livro livro : livrosEncontrados) {
                obrasXML = EscritoresXML.adicionaEscritor(livro, obrasXML, escritoresXML );
            }
            
            XMLJDomFunctions.escreverDocumentoParaFicheiro(obrasXML, OBRAS_XML_FILE);
            }*/

       
    
      //xml grupo adiciona autor bertrand
        /*Document documentoXML = null;
        String escritores = "escritores.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(escritores))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String autor = linha.trim();
                List<String> links = WrapperBertrand.obtem_links(autor);
                List<Livro> livrosEncontrados = WrapperBertrand.criaLivro(links, autor); 

        // Verificar se foram encontrados livros do autor 
        if (!livrosEncontrados.isEmpty()) {
            for (Livro livro : livrosEncontrados) {
                documentoXML = EscritoresXML.adicionaEscritor(livro, documentoXML);
            }
         XMLJDomFunctions.escreverDocumentoParaFicheiro(documentoXML, OBRAS_XML_FILE);
        }
       
        }
      }*/
        
    //xml individual remover escritor bertrand  
    /*Document documento = XMLJDomFunctions.lerDocumentoXML("obras.xml");
    String autor = "Oscar Wilde";
    Document documentoAtualizado = EscritoresXML.removeEscritor(autor, documento);

    if (documentoAtualizado != null) {
        XMLJDomFunctions.escreverDocumentoParaFicheiro(documentoAtualizado, "obras.xml");
        System.out.println("Escritor removido com sucesso!");
    }*/
    
    //completar Ids
    /*Document obrasXML = XMLJDomFunctions.lerDocumentoXML(OBRAS_XML_FILE);
    Document escritoresXML = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);
    EscritoresXML.completarIdsEscritores(obrasXML, escritoresXML);
    XMLJDomFunctions.escreverDocumentoParaFicheiro(obrasXML, OBRAS_XML_FILE)*/

    
    //xml escritores individual
    /*String autor = "Orhan Pamuk";
    boolean[] escritorExiste = new boolean[1];
            Escritor escritor = WrapperWikipedia.criaEscritor(autor, false, escritorExiste);
            
            if(escritor != null)
            {
                Document doc = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);
                //Chama a função para adicionar o livro ao XML
                doc = EscritorModeloXML.adicionaEscritor(escritor, doc);
                //grava o ficheiro XML em disco
                XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, ESCRITORES_XML_FILE);
                
     
            }*/
    
    
    // função dtd e xsd
    /*ValidarXML.validarDocumentoDTD("obras.xml", "obras.dtd");
    ValidarXML.validarDocumentoXSD("obras.xml", "obras.xsd");
    ValidarXML.validarDocumentoDTD("escritores.xml", "escritores.dtd");
    ValidarXML.validarDocumentoXSD("escritores.xml", "escritores.xsd");
    ValidarXML.validarDocumentoDTD("global.xml", "global.dtd");
    ValidarXML.validarDocumentoXSD("global.xml", "global.xsd");*/
    
    }
}
        
