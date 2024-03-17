/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpid2023;

import static com.mycompany.tpid2023.Utils.ESCRITORES_XML_FILE;
import static com.mycompany.tpid2023.Utils.OBRAS_XML_FILE;
import static com.mycompany.tpid2023.WrapperWikipedia.GetEfemeride;
import static com.mycompany.tpid2023.WrapperWikipedia.GetPremiosList;
import static com.mycompany.tpid2023.WrapperWikipedia.GetStringArray;
import com.mycompany.tpid2023.XPathFunctions;
import com.mycompany.tpid2023.data_class.Efemeride;
import com.mycompany.tpid2023.data_class.Escritor;
import com.mycompany.tpid2023.data_class.Premio;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import net.sf.saxon.exslt.Date;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;
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
    
    public static Escritor getEscritor(String autor, Document doc, Document docObras) throws SaxonApiException
    {
        Escritor oEscritor = null;
        String xp = "//escritor[@nome='" + autor + "']";
        XdmValue res = null;
        res = XPathFunctions.executaXpath(xp, ESCRITORES_XML_FILE);
        if (res == null || res.size() == 0 || doc == null) { //Escritor não existe ou força Update
            return oEscritor;
        }        
        else
        {
            Element raiz = doc.getRootElement();            
            Element eEscritor = encontrarEscritorByNome(raiz, autor);
            if(eEscritor == null)
            {
                return oEscritor;
            }
            
            Element eAux = eEscritor.getChild("nascimento");
            Efemeride efNascimento = null;
            if(eAux != null)
            {
                int auxInt = 0;
                if(!eAux.getChildText("ano").isEmpty())
                {
                    auxInt = Integer.parseInt(eAux.getChildText("ano"));
                }
                efNascimento = GetEfemeride(eAux.getChildText("data"), eAux.getChildText("local"));
                efNascimento.setAno( auxInt);
            }    
            
            eAux = eEscritor.getChild("morte");
            Efemeride efMorte = null;
            if(eAux != null)
            {
                int auxInt = 0;
                if(!eAux.getChildText("ano").isEmpty())
                {
                    auxInt = Integer.parseInt(eAux.getChildText("ano"));
                }
                efMorte = GetEfemeride(eAux.getChildText("data"), eAux.getChildText("local"));
                efMorte.setAno( auxInt);
            }
            
            List auxList = eEscritor.getChild("generos").getChildren("genero");
            Element p;
            List<String> generoLst = new ArrayList<String>();
            for(int j=0; j<auxList.size();j++)
            {      
                 p = (Element)auxList.get(j);
                 generoLst.add(p.getText());
            }
            
            auxList = eEscritor.getChild("ocupacoes").getChildren("ocupacao");
            List<String> ocupacoLst = new ArrayList<String>();
            for(int j=0; j<auxList.size();j++)
            {      
                 p = (Element)auxList.get(j);
                 ocupacoLst.add(p.getText());
            }
            
            auxList = eEscritor.getChild("obrasEmDestaque").getChildren("obra");
            List<String> destaqueLst = new ArrayList<String>();
            for(int j=0; j<auxList.size();j++)
            {      
                 p = (Element)auxList.get(j);
                 destaqueLst.add(p.getText());
            }
            
            List<Premio> premiosLst = null;
            eAux = eEscritor.getChild("premios");
            if(eAux != null)
            {
                auxList = eAux.getChildren("premio");
                if(auxList != null)
                {
                    premiosLst = new ArrayList<Premio>();
                    for(int j=0; j<auxList.size();j++)
                    {      
                         p = (Element)auxList.get(j);                 
                         Premio premioObj = new Premio();
                         premioObj.setNome(p.getText());
                         premioObj.setAno(Integer.parseInt(p.getAttributeValue("ano")));

                         premiosLst.add(premioObj);
                    }
                }
            }
            
            oEscritor = new Escritor(
                    Integer.parseInt(eEscritor.getAttributeValue("id")),
                    autor,
                    eEscritor.getChildText("nomeCompleto"), efNascimento, efMorte,
                    eEscritor.getChildText("nacionalidade"), 
                    eEscritor.getChildText("fotografia"),
                    generoLst.toArray(new String[0]),
                    ocupacoLst.toArray(new String[0]),
                    destaqueLst.toArray(new String[0]), 
                    premiosLst);

            // obtem Livros
            xp = "//escritor[@id='" + Integer.toString(oEscritor.getId()) + "']";
            res = XPathFunctions.executaXpath(xp, OBRAS_XML_FILE);
            if (res == null || res.size() == 0 || docObras == null) { //Escritor não existe ou força Update
                return oEscritor;
            }
            
            List<Livro> livrosLst = new ArrayList<Livro>();
            Element obrasRoot = docObras.getRootElement();
            List<Element> obrasList = obrasRoot.getChildren("escritor");
            
            for (Element obra : obrasList) {
                String obraId = obra.getAttributeValue("id");
            
                //verifica se está no sitio correto
                if (obraId.equals(Integer.toString(oEscritor.getId()))) {
                    List<Element> livrosList = obra.getChildren("livro");
                    for (Element livro : livrosList) {
                        Livro oLivro = new Livro(
                                livro.getChildText("titulo"), 
                                livro.getChildText("autor"),
                                livro.getChildText("capa"),
                                livro.getChildText("preco"),
                                livro.getAttributeValue("isbn"), 
                                livro.getChildText("editora") 
                        );
                        livrosLst.add(oLivro);
                    }
                    
                    oEscritor.setLivros(livrosLst);
                    return oEscritor;  
                }
            }
        }
        return oEscritor; 
    }
    
    private static Element encontrarEscritorByNome(Element raiz, String autor) {
    List<Element> escritores = raiz.getChildren("escritor"); 
    for (Element escritor : escritores) {
        if (escritor.getAttributeValue("nome").equals(autor)) {
            return escritor;
        }
    }
    return null;
}
    private static Element encontrarEscritorById(Element raiz, int id) {
    List<Element> escritores = raiz.getChildren("escritor"); 
    for (Element escritor : escritores) {
        if (escritor.getAttributeValue("id").equals(Integer.toString(id))) {
            return escritor;
        }
    }
    return null;
}
    
    static Document removeEscritorById(int id, Document doc) {
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
                
        List<Element> todosEscritores = new ArrayList<>(raiz.getChildren("escritor"));

        boolean found = false;
        for (Element escritor : todosEscritores) {
            int idEscritor = Integer.parseInt(escritor.getAttributeValue("id"));
            if(idEscritor == id) {
                escritor.getParent().removeContent(escritor);
                //System.out.println("Escritor removido com sucesso nos Escritores!");
                found = true;
            }
        }
        
        if(!found){
            //System.out.println("Autor " + autorNome + " não foi encontrado");
            return null;
        }
        return doc;       
    }
    
    public static Document removeEscritor (String autorNome, Document doc)
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
                
        List<Element> todosEscritores = new ArrayList<>(raiz.getChildren("escritor"));

        boolean found = false;
        for (Element escritor : todosEscritores) {
            String nome = escritor.getAttributeValue("nome");
            if (nome != null && nome.equals(autorNome)) {
                escritor.getParent().removeContent(escritor);
                //System.out.println("Escritor removido com sucesso nos Escritores!");
                found = true;
            }
        }
        
        if(!found){
            //System.out.println("Autor " + autorNome + " não foi encontrado");
            return null;
        }
        return doc;       
    }
    
    public static Document removeLivros (int autorID, Document doc)
    {        
        Element raiz;
        if(doc == null)
        {
            //System.out.print("Ficheiro não existe - nao dá para remover informação.");
            return doc;
        }
        else
        {
            raiz = doc.getRootElement();
        }
                
        List<Element> todosEscritores = new ArrayList<>(raiz.getChildren("escritor"));

        boolean found = false;
        for (Element escritor : todosEscritores) {
            String id = escritor.getAttributeValue("id");
            if (id != null && id.equals(Integer.toString(autorID))) {
                escritor.getParent().removeContent(escritor);
                //System.out.println("Escritor removido com sucesso nos livros!");
                found = true;
            }
        }
        
        if(!found){
            //System.out.println("Autor " + autorNome + " não foi encontrado");
            return null;
        }
        return doc;       
    }

    public static Document adicionaLivros(int id, Document doc, Livro liv, String nome) {
        Element raiz;
        if (doc == null) {
            raiz = new Element("obras");
            doc = new Document(raiz);
        } else {
            raiz = doc.getRootElement();
        }

        // verifica se o escritor já existe no documento
        Element escritor = encontrarEscritorById(raiz, id);

        if (escritor == null) {
            // Se não existir, cria
            escritor = new Element("escritor");
            escritor.setAttribute("nome", nome);

            escritor.setAttribute("id", Integer.toString(id));

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
    
    public static Document removeEscritorBut (String autorNome, Document doc)
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
                
        List<Element> todosEscritores = new ArrayList<>(raiz.getChildren("escritor"));

        boolean found = false;
        for (Element escritor : todosEscritores) {
            String nome = escritor.getAttributeValue("nome");
            if (nome != null && !nome.equals(autorNome)) {
                escritor.getParent().removeContent(escritor);
            }
        }
        
        return doc;       
    }
    
    /*
    public static Document resetRoot(Document doc, String rootNome) {
        Element raiz = new Element(rootNome);
        doc = new Document(raiz);

        return doc;
    }*/
    
}
