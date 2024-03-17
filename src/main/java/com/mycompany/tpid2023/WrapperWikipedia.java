/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tpid2023;

import static com.mycompany.tpid2023.Utils.ESCRITORES_XML_FILE;
import com.mycompany.tpid2023.data_class.Efemeride;
import com.mycompany.tpid2023.data_class.Escritor;
import com.mycompany.tpid2023.data_class.Premio;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;

/**
 *
 * @author AM
 */
public class WrapperWikipedia {
    private static final String AUTOR_FILE = "escritores.txt";
    private static final String AUTOR_LINK = "https://pt.wikipedia.org/wiki/";
    private static final String AUTOR_NOT_FOUND = "(Autor não encontrado)";
    
    public static void Teste(String autor){
        //System.out.println("Hello World!");
        String[] autores = { 
            "Oscar Wilde",
            "Daniel Silva",            
            "Nicholas Sparks",
            "Fernando Pessoa",
            "Paulo Coelho",
            "José Luís Peixoto",
            "Isabel Allende",
"Julian Barnes",
"João Tordo",
"Gabriela Mistral",
"J. K. Rowling",
"Raymond Carver",
"Gore Vidal",
"James Joyce",
"Emma Donoghue",
"Alexandre Herculano",
"José Saramago",
"António Lobo Antunes",
"Antonio Tabucchi",
"Jorge Amado",
"Haruki Murakami",
"Orhan Pamuk",
/*
// -----------------
"Vitor Hugo",
"Antero Quental",
"António Nobre",
"Aquilino Ribeiro",
"Eça de Queirós",
"Camilo Pessanha",
"Camilo Castelo Branco",
"Manuel Maria Barbosa du Bocage",
"Gabriel Garcia Marques",
"Virgínia Woolf",
"Vergilio Ferreira",
"William Shakespeare",
"Charles Dickens",
"Gil Vicente",
"Alexandre Dumas",
"Edgar Poe",
"Cervantes",
"Agatha Christie",
"Mark Twain",
/* para testes finais
"Trindade coelho",
"Almada Negreiros",
"Almeida Garret",
"Ramalho Urtigão",
"Stefan King",
"Miguel Torga",
"Eugénio Andrade",
*/
"Nao existente"
        };
        /*        
        for(int i = 0; i< autores.length;i++)
        {
            //System.out.println("Nome: "+autores[i]);
            //System.out.println("Nome completo: " + WrapperWikipedia.escritor_nomeCompleto(autores[i]) + " >> [" + autores[i] + "]");
            //System.out.println("Nascimento: " + WrapperWikipedia.escritor_dataNascimento(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            //System.out.println("Local Nascimento: " + WrapperWikipedia.escritor_localNascimento(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            //System.out.println("Morte: " + WrapperWikipedia.escritor_dataMorte(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            //System.out.println("Local: " + WrapperWikipedia.escritor_localMorte(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            //System.out.println("Nacionalidade: " + WrapperWikipedia.escritor_nacionalidade(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            //System.out.println("Fotografia: " + WrapperWikipedia.escritor_fotografia(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            //System.out.println("Prémios: " + WrapperWikipedia.escritor_premios(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            //System.out.println("Destaques: " + WrapperWikipedia.escritor_destaques(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            //System.out.println("Género: " + WrapperWikipedia.escritor_generos(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            //System.out.println("Ocupação: " + WrapperWikipedia.escritor_ocupacoes(autores[i]) + " >> [" + i + " " + autores[i] + "]");
            
            if(i == 222)
                i=100;
            
        }*/
        
        
        //System.out.println("Nome: "+autor);
/*        System.out.println("Nome completo: " + WrapperWikipedia.escritor_nomeCompleto(autor));
        System.out.println("Nascimento: " + WrapperWikipedia.escritor_dataNascimento(autor));
        System.out.println("Local Nascimento: " + WrapperWikipedia.escritor_localNascimento(autor));
        System.out.println("Morte: " + WrapperWikipedia.escritor_dataMorte(autor));
        System.out.println("Local: " + WrapperWikipedia.escritor_localMorte(autor));
        System.out.println("Nacionalidade: " + WrapperWikipedia.escritor_nacionalidade(autor));
        System.out.println("Fotografia: " + WrapperWikipedia.escritor_fotografia(autor));
*/
        //System.out.println("Prémios: " + WrapperWikipedia.escritor_premios(autor));
        /*System.out.println("Destaques: " + WrapperWikipedia.escritor_destaques(autor));
        System.out.println("Género: " + WrapperWikipedia.escritor_generos(autor));
        System.out.println("Ocupação: " + WrapperWikipedia.escritor_ocupacoes(autor));*/
    }
    
    /******************************************************************
    * 1. Procura por "Nome Completo"
    * 2. Procura nome no inicio do artigo "<p><b>nome"
    * 3. Procura nome junto de "Nascimento"
    ******************************************************************/
    static String escritor_nomeCompleto(String nome)
    {
        String linha;
        String er;
        Pattern p;
        Scanner ler;
        Matcher m;        
        String NomeArtigo = "";
        try {            
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            
            
            // procura nome completo no inicio do artigo
            //</tbody></table>
            //<p><b>Daniel Silva,</b> >> (1632)
            er = "<p><b>([^,<]+),?</b>";
            p = Pattern.compile(er);
            ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                linha = ler.nextLine(); //lê linha a linha
                m = p.matcher(linha);
                if(m.find()){
                    NomeArtigo = m.group(1);// + "(" + line + ") >>>> Nome no artigo ";
                    //System.out.println("Encontrou Nome no artigo " + NomeArtigo);
                    break;
                }
            }
            
            ler = new Scanner(new FileInputStream(AUTOR_FILE));
            
            // Procura Nome completo
            er = ";\">Nome completo";
            p = Pattern.compile(er);
            
            // Procura também o Nascimento
            // Se encontra Nascimento é porqeu não tem Nome Completo
            er = ";\">Nascimento";
            Pattern pAux2 = Pattern.compile(er);
            while ((ler.hasNextLine())) {
                linha = ler.nextLine(); //lê linha a linha
                //System.out.println(linha);
                m = p.matcher(linha);
                if(m.find()){
                    //System.out.println("(Encontrou Nome completo: " + line + ")");
                    er = ";\">([^<]+)";
                    p = Pattern.compile(er);
                    while ((ler.hasNextLine())) {
                        linha = ler.nextLine(); //lê linha a linha
                        m = p.matcher(linha);
                        if(m.find()){
                            ler.close();
                            return m.group(1);// + "(" + line + ") >>>>>>>>>>> no Nome Completo";
                        }
                    }
                }
                else
                {
                    // Procura Nascimento
                    m = pAux2.matcher(linha);
                    if(m.find()){
                        // Se encontrou Nascimento >> Não existe Nome completo
                        // A seguir ao nascimento éapenas 2 linhas                        
                        //System.out.println("(Find 1: " + line + ")");
                        er = ";\">([^0-9<]{5,})";
                        p = Pattern.compile(er);
                        ler.nextLine();
                        linha = ler.nextLine(); //lê linha a linha
                        m = p.matcher(linha);
                        if(m.find()){
                            ler.close();
                            return m.group(1);// + "(" + line + ") >>>>>>>>>>> No Nascimento";
                        }
                        
                        ler.close();
                        // Se não encontrou Devolve o Nome no inicio do artigo
                        return NomeArtigo;
                    }
                }
            }
            ler.close();
            
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.OFF, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        // Se chegou aqui é porque existe página mas ñão encontrou o Nome >> devolve o nome que foi passado na pesquisa
        return nome;
    }
    
    static String escritor_dataNascimento(String nome)
    {
        String nascimento = "";
        try {            
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = ";\">Nascimento";
            Pattern p = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = p.matcher(linha);
                if(m.find()){
                    // a seguir é um td >> avança uma linha
                    ler.nextLine();
                    linha = ler.nextLine();
                    //System.out.println("(Find 1: " + line + ")");
                    er = "#Nascimentos\" title=\"([^\"]+)\"|<br />([^<]+)<br />";
                    p = Pattern.compile(er);
                    m = p.matcher(linha);
                    if(m.find()){
                        if(m.group(1) != null)
                            nascimento = m.group(1) + " ";
                        else                            
                            nascimento = m.group(2) + " ";
                    }
                    //System.out.println(linha);
                    er = "<a href=\"/wiki/([0-9]{4})\"";                    
                    p = Pattern.compile(er);
                    m = p.matcher(linha);
                    if(m.find()){
                        ler.close();
                        if(nascimento.contains(m.group(1)))
                            return nascimento;
                        else
                            return (nascimento + m.group(1));
                    }
                }
            }
            ler.close();
            return nascimento;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }
    
    static String escritor_localNascimento(String nome)
    {
        String local = "";
        try {       
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = ";\">Nascimento";//"#Nascimentos\" title=\"([^\"]+)\"";;
            Pattern p = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = p.matcher(linha);
                if(m.find()){ // encontrou Nascimento
                    // Quando encontra avança 2 >> há uma linha com apenas td
                    ler.nextLine(); 
                    linha = ler.nextLine(); 
                    // ano seguido de local e pais (Isabel)
                    er = "<a [^>]+>[0-9]{4}</a>(<?b?r?\s?/?>?)?(<a[^>]+>([^<]+)</a>,?\s?)?(<a[^>]+>([^<]+)</a>,?\s?)+(<a[^>]+>([^<]+)</a>)"; 
                    // span antes do local e pais (Oscar)
                    er += "|<span class=\"birthplace\">(<a[^>]+>([^<]+)</a>,?\s?)*(<a[^>]+>([^<]+)</a>,?\s?)*(<a[^>]+>([^<]+)</a>)";
                    // sem span e com , antes do local e pais (pessoa)
                    er += "|<br />([^,]+), <a[^>]+>([^<]+)</a>,?\s?<a[^>]+>([^<]+)</a>";
                    // tem uma tag img no emio do local e pais (Julian Barnes + J. K. Rowling)                        
                    er += "|</span><br /><a[^>]+>([^<]+)</a>,?\s?(<img[^>]+>[^<]+)?(<a[^>]+>([^<]+)</a>)*,?<?b?r?\s?/?>?(<a[^>]+>([^<]+)</a>)*";
                    // tem um ) antes do local (João Tordo)
                    er += "|[)]<br /><a[^>]+>([^<]+)</a>,?\s?(<img[^>]+>[^<]+)?(<a[^>]+>([^<]+)</a>)*";
                    // Dumas, Poe
                    er += "|<a [^>]+>[0-9]{4}</a><br /><a[^>]+>([^>]+)</a>[^<]+<a[^>]+>([^>]+)</a>[^<]+<a[^>]+>([^>]+)</a>";

                    p = Pattern.compile(er); 

                    m = p.matcher(linha);
                    if(m.find()){                                    
                        for(int i = 0; i <= m.groupCount(); i++)
                        {
                            if(m.group(i) != null && !m.group(i).contains(("<")))
                            {
                                if(!local.equals(""))
                                    local+= ", ";
                                local += m.group(i);
                            }
                        }
                        ler.close();
                        return local;
                    }                        
                }
            }
            ler.close();
            return local;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }
    
    static String escritor_dataMorte(String nome)
    {
        String morte = "";
        try {            
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = ";\">Morte";
            Pattern p = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = p.matcher(linha);
                if(m.find()){
                    ler.nextLine(); 
                    linha = ler.nextLine(); 
                   // er = "\"><a href=\"/wiki/[^\"]+\" title=\"([^\"]+)\"";
                    er = "<td[^>]+>([0-3][^(]+)[(]";
                    p = Pattern.compile(er);
                    m = p.matcher(linha);
                    if(m.find()){
                        morte = m.group(1) + " ";
                    }  
                    
                    if(morte.equals(""))
                    {
                        er = "<a href=\"/wiki/[^\"]+\" title=\"([^\"]+)\"";
                        p = Pattern.compile(er);
                        m = p.matcher(linha);
                        if(m.find()){
                            morte = m.group(1) + " ";
                        }
                    }
                    //System.out.println(linha);
                    er = "<a href=\"/wiki/([0-9]{4})\"";                    
                    p = Pattern.compile(er);
                    m = p.matcher(linha);
                    if(m.find()){
                        ler.close();
                        if(m.group(1) == null || morte.contains(m.group(1)))
                            return morte;                                   
                        return (morte + m.group(1));
                    }                    
                }
            }
            ler.close();
            return morte;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }
    
    static String escritor_localMorte(String nome)
    {
        String local = "";
        try {            
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = ";\">Morte";
            Pattern p = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = p.matcher(linha);
                if(m.find()){ // encontrou Nascimento
                    ler.nextLine(); 
                    linha = ler.nextLine(); 
                    er = "([0-9]{4})";                    
                    p = Pattern.compile(er);
                    m = p.matcher(linha);
                    if(m.find()){ // encontrou o ano    
                        //System.out.println("asdasd");
                        er = "(<a[^>]+>([^<]+)</a>,\s)*(<a[^>]+>([^<]+)</a>,\s)+(<a[^>]+>([^<]+)</a>)";
                        er += "|[)]</span><br />([^<]+)<a[^>]+>([^<]+)</a>,?\s?<a[^>]+>([^<]+)</a>,?\s?<a[^>]+>([^<]+)</a>";
                        er += "|[)]</span><br />([^<]+)<a[^>]+>([^<]+)</a>,?\s?([^>]+)";
                        p = Pattern.compile(er); 
                        m = p.matcher(linha);
                        if(m.find()){                                    
                            ler.close();
                            for(int i = 0; i <= m.groupCount(); i++)
                            {
                                if(m.group(i) != null && !m.group(i).contains("<") )
                                {
                                    if(!local.equals("") && !local.endsWith(", "))
                                        local+= ", ";
                                    local += m.group(i);
                                }
                            }
                            return local;
                        }
                        er = "</span><br /><a[^>]+>([^<]+)</a>,?\s?([^>]+)";
                        er += "|<span class=\"deathplace\"><a[^>]+>([^<]+)</a>,\s<a[^>]+>([^<]+)</a></span>";
                        er += "|</span><br /><span class=\"deathplace\"><a[^>]+>([^<]+)</a>";
                        er += "|[)]</span><br /><a[^>]+>([^<]+)</a>";
                        er += "|[)]<br /><span class=\"deathplace\"><a[^>]+>([^<]+)</a>(\s[(]<a[^>]+>([^<]+)</a>[)])?</span>";                        
                        p = Pattern.compile(er); 
                        m = p.matcher(linha);
                        if(m.find()){                                    
                            ler.close();
                            for(int i = 0; i <= m.groupCount(); i++)
                            {
                                if(m.group(i) != null && !m.group(i).contains("<") )
                                {
                                    if(!local.equals("") && !local.endsWith(", "))
                                        local+= ", ";
                                    local += m.group(i);
                                }
                            }
                            return local;
                        }
                    }
                }
            }
            ler.close();
            return local;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }
    
    static String escritor_nacionalidade(String nome)
    {
        try {            
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = ";\">Nacionalidade|;\">Cidadania";
            Pattern p = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = p.matcher(linha);
                if(m.find()){ // encontrou Nascimento
                    ler.nextLine();
                    linha = ler.nextLine();
                    er = "<td [^>]+><a href=\"/wiki/[^\"]+\" title=\"[^\"]+\">([^<]+)</a>";
                    er += "|<td [^>]+><a[^>]+><img [^>]+></a> <a href=\"/wiki/[^\"]+\" title=\"[^\"]+\">([^<]+)</a>";
                    er += "|<td [^>]+>([^<\"]+)$";
                    p = Pattern.compile(er); 
                    m = p.matcher(linha);
                    if(m.find()){                                    
                        ler.close();
                        //System.out.println(m.groupCount()); 
                        if(m.group(1) != null)
                            return m.group(1);
                        if(m.group(2) != null)
                            return m.group(2);
                        if(m.group(3) != null)
                            return m.group(3);
                        return "";
                    }                       
                }
            }
            ler.close();
            return null;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }
    
    static String escritor_fotografia(String nome)
    {
        try {            
            //HttpRequestFunctions.httpRequest2(AUTOR_LINK, nome, AUTOR_FILE);            
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = "<a href=\"/wiki/Ficheiro:[^\"]+\" class=\"image\" title=\"[^\"]+\"><img alt=\"\" src=\"([^\"]+)\" ";
            Pattern p = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = p.matcher(linha);
                if(m.find()){                     
                    ler.close();
                    return m.group(1);
                }
            }
            ler.close();
            return null;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }
               
    static String escritor_premios(String nome)
    {
        int line = 0;
        //int lineCount = 0;
        String premio = "";
        try {            
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = "<td scope=\"row\" style=\"vertical-align: top; text-align: left; font-weight:bold;\">Pr[éê]mios";
            Pattern pPremio = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                line++;
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = pPremio.matcher(linha);
                if(m.find()){ 
                    //System.out.println("Find 1: "+ line);
                    
                    // avança 1: há uma linha apenas com </td>
                    ler.nextLine();
                    
                    er = "<td [^>]+><div [^>]+><ul><li>([^(]+)[(].*([0-9]{4})[)]";
                    er += "|<td [^>]+><a [^>]+>([^<]+)</a>\s*[(]([0-9]{4})[)]<br />\s<a [^>]+>([^<]+)</a>\s*[(]([0-9]{4})[)]";
                    er += "|<td [^>]+>([^(]+)[(]([0-9]{4})[)],<sup[^>]+><a [^>]+>.+</a></sup> <a [^>]+>([^<]+)</a>\s*[(]([0-9]{4})[)]";
                    
                    // Lobo Antunes
                    er += "|<td [^>]+><a [^>]+>([^<]+)</a>,?\s?[(]([0-9]{4})[)]<br /><a [^>]+>([^<]+)</a>,?\s?[(]([0-9]{4})[)]<br />"
                            + "([^(]+),?\s?[(]([0-9]{4})[)]<br /><a [^>]+>([^<]+)</a>,?\s*[(]([0-9]{4})[)]<br /><a [^>]+>([^<]+)</a>,?\s*[(]([0-9]{4})[)]<br />"
                            + "<a [^>]+>([^<]+)</a>,?\s*[(]([0-9]{4})[)]<br /><a [^>]+>([^<]+)</a>,?\s*[(]([0-9]{4})[)]<br /><a [^>]+>([^<]+)</a>,?\s*[(]([0-9]{4})[)]<br />"
                            + "<a [^>]+>([^<]+),?\s*([0-9]{4})</a><br /><a [^>]+>([^<]+),?\s*([0-9]{4})</a>";
                    
                    Pattern p = Pattern.compile(er); 
                    while ((ler.hasNextLine())) {                        
                        linha = ler.nextLine(); //lê linha a linha
                        m = p.matcher(linha);                        
                        if(m.find()){                                    
                            ler.close();
                            for(int i = 1; i <= m.groupCount(); i++)
                            {
                                if(m.group(i) != null && !m.group(i).contains("<"))
                                {
                                    if(Character.isDigit(m.group(i).charAt(0)))
                                            premio += m.group(i) + ",";
                                        else
                                            premio += m.group(i) + " - ";
                                }
                            }
                            return premio;
                        }
                    }
                    
                    // Prémios em lista
                    ler = new Scanner(new FileInputStream(AUTOR_FILE));
                    while ((ler.hasNextLine())) {                        
                        linha = ler.nextLine(); //lê linha a linha
                        m = pPremio.matcher(linha);
                        if(m.find())
                        {
                            // avança a linha com apenas TD
                            ler.nextLine(); 
                            linha = ler.nextLine();
                            //System.out.println(linha);                        
                            er = "<td [^>]+><a href=\"([^\"]+)\" [^>]+>Ver página</a>";                            
                            p = Pattern.compile(er); 
                            m = p.matcher(linha);
                            if(m.find())
                            {
                                if(m.group(1) != null)
                                {
                                    premio = PaginaDePremios(m.group(1));
                                    if(!premio.equals(""))
                                    {
                                        ler.close();
                                        return premio;
                                    }
                                    return premio;
                                }
                            }
                            
                            er = "<td [^>]+><div class=\"NavFrame collapsed\" [^>]+>";
                            p = Pattern.compile(er); 
                            m = p.matcher(linha);
                            if(m.find())
                            {
                                //System.out.println("Procura local111:");                        
                                // Lista colapsada                                
                                // Saramago 11
                                er = "<a href=\"/wiki/[^\"]+\" title=\"[^\"]+\">([^<]+)</a><?/?s?p?a?n?>?\s*[(]([0-9]{4},?\s?)*([0-9]{4})[)]";
                                er += "<br /><a href=\"/wiki/[^\"]+\" class=\"mw-redirect\" title=\"[^\"]+\">([^<]+)</a><?/?s?p?a?n?>?\s*[(]([0-9]{4},?\s?)*([0-9]{4})[)]<br />";                            
                                er += "|<a href=\"/wiki/[^\"]+\" title=\"[^\"]+\">([^<]+)</a><?/?s?p?a?n?>?\s*[(]([0-9]{4},?\s?)*([0-9]{4})[)]";
                                er += "|<a href=\"/wiki/[^\"]+\" class=\"mw-redirect\" title=\"[^\"]+\">([^<]+)</a><?/?s?p?a?n?>?\s*[(]([0-9]{4},?\s?)*([0-9]{4})[)]";
                            }
                            else
                            {                           
                                //System.out.println("Procura local33:");                        
                                // Lista não colapsada
                                er = "<a href=\"/w[^\"]+\" title=\"[^\"]+\">([^<]+)</a>\s?[(]([0-9]{4})[)]<br /><a href=\"/w[^\"]+\" title=\"[^\"]+\">([^<]+)</a>\s?-[^(]+[(]([0-9]{4})[)]";
                                // Luis Peixoto 7
                                er += "|<a href=\"[^\"]+\"[^.]+title=\"[^\"]+\">([^<]+)</a>\s?[(]([0-9]{4})[)]\s?<br />\s?<a[^>]+>([^<]+)</a>\s?[(]([0-9]{4})[)]\s?<br />(\s?<a[^>]+>([^<]+)</a>\s?[(]([0-9]{4})[)]\s?<br />)+\s?<a[^>]+>([^<]+)</a>\s?[(]([0-9]{4})[)]\s?<br />";
                                er += "|<p><a href=\"[^\"]+\"[^.]+title=\"[^\"]+\">([^<]+)</a>\s?[(]([0-9]{4})[)]\s?<br />";
                                er += "|<a href=\"/w[^\"]+\"[^.]+title=\"[^\"]+\">([^<]+)</a>\s?[(]([0-9]{4})[)]";
                                // Pamuk
                                er += "|<span [^>]+><a href=\"/w[^\"]+\"[^.]+title=\"[^\"]+\">([^<]+)\s?[(]([0-9]{4})[)]</a></span>";                                
                            }
                            
                            p = Pattern.compile(er); 
                            // Verifica a linha já carregada
                            m = p.matcher(linha);
                            if(m.find()){
                                for(int i = 1; i <= m.groupCount(); i++)
                                {   
                                    if(m.group(i) != null && !m.group(i).contains("<"))
                                    {
                                        if(Character.isDigit(m.group(i).charAt(0)))
                                            premio += m.group(i) + ",";
                                        else
                                            premio += m.group(i) + " - ";
                                    }
                                }
                            }
                            // Identifica o fim da lista de premios                                
                            String erFim = "</td></tr>";
                            pPremio = Pattern.compile(erFim); 
                            while ((ler.hasNextLine())) {                        
                                linha = ler.nextLine(); //lê linha a linha
                                m = p.matcher(linha);                        
                                if(m.find()){                                    
                                    for(int i = 1; i <= m.groupCount(); i++)
                                    {   
                                        if(m.group(i) != null && !m.group(i).contains("<"))
                                        {
                                            if(Character.isDigit(m.group(i).charAt(0)))
                                                premio += m.group(i) + ",";
                                            else
                                                premio += m.group(i) + " - ";
                                        }
                                    }
                                    //return premio;
                                }
                                // se encontrou o final da tabela de premios sai do ciclo
                                m = pPremio.matcher(linha);
                                if(m.find())
                                   break;                                
                            }   
                            if(!premio.equals(""))
                            {
                                ler.close();
                                return premio;
                            }
                        }
                    }
                }
            }
            ler.close();
            return null;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }
            
    static String PaginaDePremios(String link)
    {
        String premio = "";
        String premiosFile = "Premios_"+AUTOR_FILE;
        int lista = 0;
        try { 
            HttpRequestFunctions.httpRequest2(AUTOR_LINK+link.replace("/wiki/", ""), "",premiosFile);

            String er = "<dl><dt>Prêmio[^<]+</dt></dl>";            
            Pattern pLista = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(premiosFile));
            while ((ler.hasNextLine())) {
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = pLista.matcher(linha);
                if(m.find()){  
                    lista++;
                    //System.out.println(linha);                        
                    String erPremio = "<?u?l?>?<li><a [^>]+>([^<]+)</a>\s?[(][^0-9]*([0-9]{4})[)]";
                    erPremio += "|<li>([^<]+)<a [^<]+>([^<]+)</a>([^(]+)[(][^0-9]*([0-9]{4})[)]";
                    erPremio += "|<li><a [^<]+>([^<]+)</a>([^(]+)[(][^0-9]*([0-9]{4})[)]";
                    erPremio += "|<li>([^(]+)[(][^0-9]*([0-9]{4})[)]";
                    
                    Pattern p = Pattern.compile(erPremio); 
                    while ((ler.hasNextLine())) {                        
                        linha = ler.nextLine(); //lê linha a linha
                        m = p.matcher(linha);                        
                        //System.out.println(linha);
                        //System.out.println("Procura local:");                        
                        //m = p.matcher(linha);
                        if(m.find()){
                            for(int i = 1; i <= m.groupCount(); i++)
                            {   
                                if(m.group(i) != null && !m.group(i).isEmpty() && !m.group(i).contains("<"))
                                {
                                    if(Character.isDigit(m.group(i).charAt(0)))
                                        premio += " - " + m.group(i) + "§";
                                    else
                                        premio += m.group(i) + "";
                                }
                            }                                                        
                        }
                        
                        if(linha.startsWith("<dl><dt>") )
                        {
                            // as duas listas de premios estão seguidas
                            // se o <dl><dt> não tem premio termina
                            m = pLista.matcher(linha);
                            if(!m.find())
                                break;
                        }
                    }                    
                }                                
            }
            ler.close();
            return premio;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return "";
    }
    
    static String escritor_destaques(String nome)
    {
        String destaque = "";
        try {           
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = ";\">Nacionalidade|;\">Nascimento";            
            Pattern p = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = p.matcher(linha);
                if(m.find()){                     
                    er = "\">Principais trabalhos|\">Obras destacadas|\">Magnum opus</a>";
                    p = Pattern.compile(er); 
                    while ((ler.hasNextLine())) {                        
                        linha = ler.nextLine(); //lê linha a linha
                        m = p.matcher(linha);
                        if(m.find()){
                            er = "<td style=\"[^\"]+\"><i><a [^>]+>([^<]+)</a></i>.+<i><a [^>]+>([^<]+)</a></i>.+<i><a [^>]+>([^<]+)</a>\", </i>"
                                    + "<a [^>]+>([^<]+)</a><i>, </i><a [^>]+>([^<]+)</a><i>, </i><a [^>]+>([^<]+)</a><i>, </i><a [^>]+>([^<]+)</a><i>, </i>"
                                    + "<a [^>]+>([^<]+)</a><i>, </i><a [^>]+>([^<]+)</a><i></i>";
                            
                            er += "|<td style=\"[^\"]+\"><i><a href=\"/wiki/[^\"]+\" class=\"mw-redirect\" title=\"[^\"]+\">([^<]+)</a></i>"
                                    + "|<td style=\"[^\"]+\"><i><a href=\"/wiki/[^\"]+\" title=\"[^\"]+\">([^<]+)</a></i>[^>]+><i><a href=\"/wiki/[^\"]+\" title=\"[^\"]+\">([^<]+)</a></i>"
                                    + "|<td style=\"[^\"]+\"><i><a href=\"/wiki/[^\"]+\" title=\"[^\"]+\">([^<]+)</a></i>.+<i><a href=\"/wiki/[^\"]+\" title=\"[^\"]+\">([^<]+)</a></i>"
                                    + "|<td style=\"[^\"]+\"><i><a href=\"/wiki/[^\"]+\" .+ title=\"[^\"]+\">([^<]+)</a></i>";
                            er += "|<td style=\"[^\"]+\"><i><a href=\"/wiki/[^\"]+\" title=\"[^\"]+\">([^<]+)</a></i>"
                                    + "|<td style=\"[^\"]+\"><i>([^<]+)</i>[^<]+<br /><i>([^<]+)</i>[^<]+<br /><i>([^<]+)</i>[^<]+<br /><i>([^<]+)</i>[^<]+<i>"
                                    + "|<td style=\"[^\"]+\"><i>([^<]+)</i>"
                                    + "|<td style=\"[^\"]+\">[^<]+<i><a [^>]+>([^<]+)</a></i>"
                                    + "|<td style=\"[^\"]+\"><div [^>]+><i><a [^>]+>([^<]+)</a></i>[^<]+<br /><i><a [^>]+>([^<]+)</a></i>"
                                    + "[^<]+<br />\s+<i><a [^>]+>([^<]+)</a></i>";
                                    
                            p = Pattern.compile(er); 
                            while ((ler.hasNextLine())) {                        
                                linha = ler.nextLine(); //lê linha a linha
                                m = p.matcher(linha);                        
                                while(m.find()){
                                    ler.close();
                                    for(int i = 1; i <= m.groupCount(); i++)
                                    {
                                        if(m.group(i) != null && !m.group(i).contains("<"))
                                        {
                                            if(!destaque.equals(""))
                                                destaque += ",";
                                            destaque += m.group(i);
                                        }
                                    }
                                    //er = "<td style=\"[^\"]+\">(<i><a href=\"/wiki/[^\"]+\"[^>]+>([^<]+)</a></i><?b?r?\s?/?>?)*<i><a href=\"/wiki/[^\"]+\"[^>]+>([^<]+)</a></i>";
                                    er = "(<i><a\s+[^>]+>([^<]+)</a></i><?b?r?\s?/?>?)*<i><a href=\"/wiki/[^\"]+\"[^>]+>([^<]+)</a></i>";
                                    p = Pattern.compile(er);
                                    m = p.matcher(linha);                        
                                    if(m.find()){
                                        for(int i = 2; i <= m.groupCount(); i++)
                                        {
                                            if(m.group(i) != null && !destaque.contains(m.group(i)))
                                            {
                                                if(!destaque.equals(""))
                                                    destaque += ",";
                                                destaque += m.group(i);
                                            }
                                        }
                                    }
                                    return destaque;   
                                }                                
                            }                            
                        }       
                    }
                }
            }
            ler.close();
            return null;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }
    
    static String escritor_generos(String nome)
    {
        String result = "";
        try {            
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = ";\">Nacionalidade|;\">Nascimento";      
            Pattern p = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            while ((ler.hasNextLine())) {
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = p.matcher(linha);
                if(m.find()){                     
                    er = "\">Género literário|\">Gênero literário</a>";
                    p = Pattern.compile(er); 
                    while ((ler.hasNextLine())) {                        
                        linha = ler.nextLine(); //lê linha a linha
                        m = p.matcher(linha);                        
                        if(m.find()){
                            
                            ler.nextLine(); // avança 1: tem um td sozinho
                            //System.out.println(linha);  
                                    
                            er = "<td style=\"[^\"]+\"><a [^>]+>([^<]+)</a>,\s<a [^>]+>([^<]+)</a>,\s<a [^>]+>([^<]+)</a>,\s<a [^>]+>([^<]+)</a>"
                                    + "|<td style=\"[^\"]+\"><a [^>]+>([^<]+)</a>,\s<a [^>]+>([^<]+)</a>,\s<a [^>]+>([^<]+)</a>"
                                    + "|<td style=\"[^\"]+\"><a [^>]+>([^<]+)</a>,\s<a [^>]+>([^<]+)</a>"
                                    + "|<td style=\"[^\"]+\"><a [^>]+>([^<]+)</a>,\s([^<]+)";
                            p = Pattern.compile(er); 
                            while ((ler.hasNextLine())) {                        
                                linha = ler.nextLine(); //lê linha a linha
                                m = p.matcher(linha);                        
                                if(m.find()){
                                    ler.close();
                                    for(int i = 1; i <= m.groupCount(); i++)
                                    {
                                        if(m.group(i) != null)
                                        {
                                            if(!result.equals(""))
                                                result += ",";
                                            result += m.group(i);
                                        }
                                    }
                                    return result;   
                                }
                                
                                er = "<li><a [^>]+>([^<]+)</a></li>";
                                p = Pattern.compile(er); 
                                while ((ler.hasNextLine())) {                        
                                    linha = ler.nextLine(); //lê linha a linha
                                    m = p.matcher(linha);                        
                                    if(m.find()){
                                        for(int i = 1; i <= m.groupCount(); i++)
                                        {
                                            if(m.group(i) != null)
                                            {
                                                if(!result.equals(""))
                                                    result += ",";
                                                result += m.group(i);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        ler.close();
                                        return result;
                                    }
                                }
                            }
                        }       
                    }
                }
            }
            ler.close();
            return null;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }
    
    static String escritor_ocupacoes(String nome)
    {
        int line = 0;
        String ocupacao = "";
        try {            
            verificaDesambiguacao(AUTOR_LINK, nome, AUTOR_FILE);            

            String er = "<td [^>]+>Ocupação";
            Pattern p = Pattern.compile(er);
            Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));
            
            while ((ler.hasNextLine())) {
                line++;
                String linha = ler.nextLine(); //lê linha a linha
                Matcher m = p.matcher(linha);
                if(m.find()){ 
                    //System.out.println("(Find 1: "+ line +")");
                    
                    // tem um td sozinho, avança 2 linhas (Exemplo: Oscar)
                    ler.nextLine();
                    linha = ler.nextLine();
                    
                    // lista colapsada
                    er = "<td [^>]+><div class=\"NavFrame collapsed\"";
                    er += "|<td [^>]+><div class=\"hlist\"";
                    p = Pattern.compile(er); 
                    m = p.matcher(linha);
                    if(m.find()){
                        // Ocupacao em lista colapsada
                        //ler = new Scanner(new FileInputStream(AUTOR_FILE));
                        er = "<li><a[^>]+>([^<]+)</a></li>";
                        p = Pattern.compile(er); 
                        while ((ler.hasNextLine())) {                        
                            linha = ler.nextLine(); //lê linha a linha
                            m = p.matcher(linha);                        
                            if(m.find()){                                    
                                for(int i = 1; i <= m.groupCount(); i++)
                                {
                                    if(!ocupacao.equals(""))
                                        ocupacao += ",";
                                    ocupacao += m.group(i);                                
                                }
                            } 
                            else if(!ocupacao.equals(""))
                            {
                                ler.close();
                                return ocupacao;
                            }
                        }
                    }
                    else{
                        er = "<a\s+[^>]+>([^<]+)</a>,\s([^<]+)<a\s+[^>]+>([^<]+)</a>,\s([^<]+)<a\s+[^>]+>([^<]+)</a>,?\s*<a\s+[^>]+>([^<]+)</a>,?\s*<a\s+[^>]+>([^<]+)</a>,\s([^<]+),\s([^<]+)<a\s+[^>]+>([^<]+)</a>,\s(.+)";
                        er += "|<a [^>]+>([^<]+)</a>, <a [^>]+>([^<]+)</a>, <a [^>]+>([^<]+)</a>, ([^<]+)";
                        er += "|<a [^>]+>([^<]+)</a>, <a [^>]+>([^<]+)</a>\s?[,e]\s<a [^>]+>([^<]+)</a>";
                        er += "|<a [^>]+>([^<]+)</a>\s?[,e]\s?<a [^>]+>([^<]+)</a>";
                        er += "|<td [^>]+>([^<]+)";
                        er += "|<a [^>]+>([^<]+)</a>";
                        ///er = "(<a\s[^>]+>([^<]+)</a>,\s){1,}([^<]+)";
                        p = Pattern.compile(er); 
                        m = p.matcher(linha);                        
                        if(m.find()){                                    
                            ler.close();
                            for(int i = 1; i <= m.groupCount(); i++)
                            {   
                                if(m.group(i) == null || m.group(i).contains("<"))
                                    continue;
                                if(!ocupacao.equals(""))
                                    ocupacao += ",";
                                ocupacao += m.group(i);
                            }
                            return ocupacao;
                        } 
                    }
                }
            }
            ler.close();
            return null;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(TPID2023.class.getName()).log(Level.SEVERE, null, ex);
            return AUTOR_NOT_FOUND;
        } catch (IOException ex) { 
            Logger.getLogger(WrapperWikipedia.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return null;
    }    
    
    private static void verificaDesambiguacao(String link, String pesquisa, String file) throws IOException{
        
        int line = 0;
        HttpRequestFunctions.httpRequest2(link, pesquisa, file);
        
        String er = "Desambiguação";
        Pattern p = Pattern.compile(er);
        Scanner ler = new Scanner(new FileInputStream(AUTOR_FILE));

        // avança até à linha 5
        while (ler.hasNextLine() && line < 5)
        {
            ler.nextLine();
            line++;
        }

        while (ler.hasNextLine() && line < 20) {
            line++;
            String linha = ler.nextLine(); //lê linha a linha
            Matcher m = p.matcher(linha);
            if(m.find()){ 
                HttpRequestFunctions.httpRequest2(link, pesquisa+" (escritor)", file);        
            }
        }  
                
    }
    
    public static Escritor criaEscritor(String autorNome, boolean forceUpdate, boolean[] escritorExiste ) throws IOException, SaxonApiException {

        //System.out.println("AAA");
        //Teste(autorNome);
        
        String xp = "//escritor[@nome='" + autorNome + "']";
        XdmValue res = null;
        res = XPathFunctions.executaXpath(xp, ESCRITORES_XML_FILE);
        
        escritorExiste[0] = false;
        if (res != null && res.size() > 0)
        {
            escritorExiste[0] = true;
        }
        
        if (res == null || res.size() == 0 || forceUpdate) { //Escritor não existe ou força Update
            
            xp = "max(//escritor/@id)";
            res = XPathFunctions.executaXpath(xp, ESCRITORES_XML_FILE);
            
            int id = 1;
            if (res != null && res.size() > 0)
            {
                id = id + Integer.parseInt(res.itemAt(0).getStringValue());
            }
            //System.out.println(autorNome);
            String nomeCompleto = WrapperWikipedia.escritor_nomeCompleto(autorNome);
            if(nomeCompleto != AUTOR_NOT_FOUND)
            {
                String dataNascimento = WrapperWikipedia.escritor_dataNascimento(autorNome);                
                String localNascimento = WrapperWikipedia.escritor_localNascimento(autorNome);
                String dataMorte = WrapperWikipedia.escritor_dataMorte(autorNome);
                String localMorte = WrapperWikipedia.escritor_localMorte(autorNome);
                String nacionalidade = WrapperWikipedia.escritor_nacionalidade(autorNome);
                String fotografia = WrapperWikipedia.escritor_fotografia(autorNome);
                String destaques = WrapperWikipedia.escritor_destaques(autorNome);
                String generos = WrapperWikipedia.escritor_generos(autorNome);
                String ocupacoes = WrapperWikipedia.escritor_ocupacoes(autorNome);
                String premios = WrapperWikipedia.escritor_premios(autorNome);
                
                Efemeride efNascimento = GetEfemeride(dataNascimento, localNascimento);                
                Efemeride efMorte = GetEfemeride(dataMorte, localMorte);
                
                Escritor escritor = new Escritor(id,autorNome, nomeCompleto, efNascimento, efMorte, nacionalidade, fotografia,
                        GetStringArray(generos),GetStringArray(ocupacoes), GetStringArray(destaques), 
                        GetPremiosList(premios));
                
                return escritor;                
            }
        }
            
        return null;
    }
    
    public static Efemeride GetEfemeride(String data, String local)
    {
        Efemeride ef = null;
        if((data != null && !data.isEmpty()) || (local != null && !local.isEmpty() ))
        {
            int ano = 0;
            try
            {
                ano = Integer.parseInt(data.substring(data.length() - 4));                        
            }
            catch(Exception ex)
            {}
            
            ef = new Efemeride(data, ano, local);
        }
        else
        {
            ef = new Efemeride("", 0, "");
        }
        
        return ef;
    }
    
    public static String[] GetStringArray(String values)
    {
        if(values != null && !values.isEmpty())
            return values.split(",");
        else
            return new String[0];
    }
    
    public static List<Premio> GetPremiosList(String premios)
    {
        List<Premio> premiosLst = null;
        if(premios != null && !premios.isEmpty())
        {
            //System.out.println(premios);
            
            premiosLst = new ArrayList<Premio>();
            
            String[] premioStr;
            if(premios.contains("§"))
            {
                premioStr = premios.split("§");
            }
            else
            {
                premioStr = premios.split(",");
            }
            
            for(int i = 0; i < premioStr.length; i++)
            {
                String[] premioTermos = premioStr[i].split(" - ");
                if(premioTermos.length == 2)
                {
                    Premio premioObj = new Premio();
                    premioObj.setNome(premioTermos[0]);
                    premioObj.setAno(Integer.parseInt(premioTermos[1]));
                    
                    premiosLst.add(premioObj);
                }
                if(premioTermos.length == 1 && Character.isDigit(premioTermos[0].charAt(0))
                        && i-2 >=0)
                {
                    Premio premioObj = new Premio();
                    premioObj.setNome(premioStr[i-2].split(" - ")[0]);
                    premioObj.setAno(Integer.parseInt(premioTermos[0]));
                    
                    premiosLst.add(premioObj);
                }
            }
        }
        return premiosLst;
    }
}