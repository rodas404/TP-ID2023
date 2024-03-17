/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tpid2023;

import static com.mycompany.tpid2023.Utils.ESCRITORES_XML_FILE;
import static com.mycompany.tpid2023.Utils.OBRAS_XML_FILE;
import static com.mycompany.tpid2023.Utils.GLOBAL_XML_FILE;
import com.mycompany.tpid2023.data_class.Escritor;
import com.mycompany.tpid2023.EscritorModeloXML;
import com.mycompany.tpid2023.data_class.Efemeride;
import com.mycompany.tpid2023.data_class.Premio;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.trans.XPathException;
import org.jdom2.Document;

/**
 *
 * @author AM
 */
public class MainFrame extends javax.swing.JFrame {

    private void GeraHTMComObras(String autor) {
        // TODO add your handling code here:
        Document doc = XMLJDomFunctions.lerDocumentoXML("global.xml"); 
        Document doc2 = EscritorModeloXML.removeEscritorBut(autor, doc);
                
        if (doc != null) {
            try {
                Document novo = JDOMFunctions_XSLT.transformaDocumento(doc2, "global.xml", "transf4.xsl"); 
                XMLJDomFunctions.escreverDocumentoParaFicheiro(novo, "transf4.html");
                doc = XMLJDomFunctions.lerDocumentoXML("transf4.html");
                String t = XMLJDomFunctions.escreverDocumentoString(doc);
            
                lblFileName.setText("Resultado transformação XSL.");
            
                jTextAreaFile.setText(t);
                JOptionPane.showMessageDialog(this,
                        "Transformação feita com sucesso... a abrir browser...",
                        "XSLT para HTML", JOptionPane.INFORMATION_MESSAGE);
                String url = "transf4.html";
                File htmlFile = new File(url);
                Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    enum enumEscritorAction
    {
        Recolher,
        Eliminar,
        Mostrar,
        Editar,
        Novo,
        Indefinido,
        HTMcomObras
    }
    
    private enumEscritorAction eEscritorAction;

    private Escritor oGlobalEscritor = null;
    
    private Escritor LoadEscritor(String autor) {
        Escritor escritor = null;
        try 
        {
            if(!autor.equals(""))
            {
                Document doc = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);
                if(doc == null)
                {
                    JOptionPane.showMessageDialog(this,
                            "Ficheiro de escritores " + ESCRITORES_XML_FILE + " não foi encontrado!",
                            "Carregar dados de escritor",
                            JOptionPane.ERROR_MESSAGE);
                }            

                Document docObras = XMLJDomFunctions.lerDocumentoXML(OBRAS_XML_FILE); 
                escritor = EscritorModeloXML.getEscritor(autor, doc, docObras);

                if(escritor == null)
                {
                    JOptionPane.showMessageDialog(this,
                            "Não foi encontrado o escrito " + autor + "!",
                            "Carregar dados de escritor",
                            JOptionPane.ERROR_MESSAGE);           
                }
            }
            else
            {
                escritor = new Escritor();
            }
            
            if(escritor != null)
            {
                jDialogEscritor.setTitle(jDialogEscritor.getTitle() + " - " + Integer.toString(escritor.getId()));
                jTextNome.setText(escritor.getNome());
                jTextNomeCompleto.setText(escritor.getNomeCompleto());
                jTextNacionalidade.setText(escritor.getNacionalidade());
                //if(escritor.getNascimento().getData() != null)
                jTextLocalNascimento.setText(escritor.getNascimento().getLocal());
                jTextDataNascimento.setText(escritor.getNascimento().getData() );
                jTextAnoNascimento.setText(Integer.toString(escritor.getNascimento().getAno()));
                if(jTextAnoNascimento.getText().equals("0"))
                {
                    jTextAnoNascimento.setText("");
                }
                jTextFotografia.setText(escritor.getFotografia());                
                
                if(escritor.getMorte() != null)
                {
                  //  if(escritor.getMorte().getData() != null) 
                        jTextDataMorte.setText(escritor.getMorte().getData());
                    //if(escritor.getMorte().getLocal()!= null) 
                        jTextLocalMorte.setText(escritor.getMorte().getLocal());
                    //if(escritor.getMorte().getAno() > 0) 
                        jTextAnoMorte.setText(Integer.toString(escritor.getMorte().getAno()));
                        if(jTextAnoMorte.getText().equals("0"))
                        {
                            jTextAnoMorte.setText("");
                        }
                }
                else
                {
                    jTextDataMorte.setText("");
                    jTextLocalMorte.setText("");
                    jTextAnoMorte.setText("");
                }
                
                jListGeneros.removeAll();
                jListGeneros.setListData(escritor.getGenero());
                
                jListOcupacoes.removeAll();
                jListOcupacoes.setListData(escritor.getOcupacao());
                
                jListDestaques.removeAll();
                jListDestaques.setListData(escritor.getDestaques());
                
                jListPremios.removeAll();
                jListPremios.setListData(escritor.getPremiosList());
                
                jListLivros.removeAll();
                jListLivros.setListData(escritor.getLivrosList());
                
                ImageIcon imageIcon = getImageIcon(escritor.getFotografia());
                if(imageIcon!= null)
                {
                    jLabelFotografia.setIcon( imageIcon);
                }                
            }
                        
        } catch (SaxonApiException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return escritor;
    }

    private ImageIcon getImageIcon(String url)
    {
        Image image = null;
        try {
            if(!url.equals(""))                    
            {
                if(url.startsWith("//"))
                {
                    url = "https:" + url;
                }
                URL link = new URL(url);
                image = ImageIO.read(link);
                
                Image newimg = image.getScaledInstance(200, 300,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                return  new ImageIcon(newimg);            
            } 
            
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(image == null)
        {
            try {

                    File sourceimage = new File("imagemNaoDisponivel.png");
                    image = ImageIO.read(sourceimage);
                    return new ImageIcon(image);

            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);                    
            }
        }
        return null;
    }
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        jLabelAviso.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogRecolher = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        jTextAutorNome = new javax.swing.JTextField();
        jButtonOK = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jLabelAviso = new javax.swing.JLabel();
        jDialogMSG = new javax.swing.JDialog();
        jLabelMSG = new javax.swing.JLabel();
        jButtonSim = new javax.swing.JButton();
        jButtonNao = new javax.swing.JButton();
        jButtonOkMsg = new javax.swing.JButton();
        jOptionPane1 = new javax.swing.JOptionPane();
        jDialogEscritor = new javax.swing.JDialog();
        jButtonFechar = new javax.swing.JButton();
        jButtonEditaEscritor = new javax.swing.JButton();
        jButtonGravaEscritor = new javax.swing.JButton();
        jButtonApagaEscritor = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextNome = new javax.swing.JTextField();
        jTextNomeCompleto = new javax.swing.JTextField();
        jTextNacionalidade = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextDataNascimento = new javax.swing.JTextField();
        jTextAnoNascimento = new javax.swing.JTextField();
        jTextLocalNascimento = new javax.swing.JTextField();
        jTextLocalMorte = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextAnoMorte = new javax.swing.JTextField();
        jTextDataMorte = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextOcupacao = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListOcupacoes = new javax.swing.JList<>();
        jButtonRemoveOcupacao = new javax.swing.JButton();
        jButtonAddOcupacao = new javax.swing.JButton();
        jLabelFotografia = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextGenero = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListGeneros = new javax.swing.JList<>();
        jButtonRemoveGenero = new javax.swing.JButton();
        jButtonAddGenero = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextDestaque = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListDestaques = new javax.swing.JList<>();
        jButtonRemoveDestaque = new javax.swing.JButton();
        jButtonAddDestaque = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jTextFotografia = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextPremio = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jListPremios = new javax.swing.JList<>();
        jButtonRemovePremio = new javax.swing.JButton();
        jButtonAddPremio = new javax.swing.JButton();
        jTextAno = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jTextTitulo = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        jListLivros = new javax.swing.JList<>();
        jButtonRemovePremio1 = new javax.swing.JButton();
        jButtonAddPremio1 = new javax.swing.JButton();
        jTextPreco = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTextEditora = new javax.swing.JTextField();
        jLabelMostraCapa = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jTextURLCapa = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jTextISBN = new javax.swing.JTextField();
        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jDialogXPathMenu = new javax.swing.JDialog();
        jLabel24 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jDialogXPathMenu2 = new javax.swing.JDialog();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextFieldEditora = new javax.swing.JTextField();
        jTextFieldPreco = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jPanelMostraFicheiro = new javax.swing.JPanel();
        lblFileName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaFile = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuRecolherDados = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem23 = new javax.swing.JMenuItem();

        jDialogRecolher.setTitle("Recolher dados de um Autor.");
        jDialogRecolher.setAlwaysOnTop(true);
        jDialogRecolher.setModal(true);

        jLabel2.setText("Nome:");

        jTextAutorNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAutorNomeKeyPressed(evt);
            }
        });

        jButtonOK.setText("OK");
        jButtonOK.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jLabelAviso.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabelAviso.setForeground(new java.awt.Color(153, 0, 51));
        jLabelAviso.setText("Tem que indicar um nome.");

        javax.swing.GroupLayout jDialogRecolherLayout = new javax.swing.GroupLayout(jDialogRecolher.getContentPane());
        jDialogRecolher.getContentPane().setLayout(jDialogRecolherLayout);
        jDialogRecolherLayout.setHorizontalGroup(
            jDialogRecolherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogRecolherLayout.createSequentialGroup()
                .addGroup(jDialogRecolherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jDialogRecolherLayout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jButtonOK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCancelar))
                    .addGroup(jDialogRecolherLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jDialogRecolherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelAviso)
                            .addComponent(jTextAutorNome, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jDialogRecolherLayout.setVerticalGroup(
            jDialogRecolherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogRecolherLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jDialogRecolherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextAutorNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabelAviso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jDialogRecolherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonOK)
                    .addComponent(jButtonCancelar))
                .addGap(43, 43, 43))
        );

        jButtonSim.setText("Sim");

        jButtonNao.setText("Não");

        jButtonOkMsg.setText("Ok");
        jButtonOkMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkMsgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogMSGLayout = new javax.swing.GroupLayout(jDialogMSG.getContentPane());
        jDialogMSG.getContentPane().setLayout(jDialogMSGLayout);
        jDialogMSGLayout.setHorizontalGroup(
            jDialogMSGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogMSGLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jDialogMSGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogMSGLayout.createSequentialGroup()
                        .addComponent(jLabelMSG)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jDialogMSGLayout.createSequentialGroup()
                        .addComponent(jButtonSim)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonOkMsg)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonNao)
                        .addGap(81, 81, 81))))
        );
        jDialogMSGLayout.setVerticalGroup(
            jDialogMSGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogMSGLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelMSG)
                .addGap(99, 99, 99)
                .addGroup(jDialogMSGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSim)
                    .addComponent(jButtonNao)
                    .addComponent(jButtonOkMsg))
                .addGap(41, 41, 41))
        );

        jDialogEscritor.setModal(true);

        jButtonFechar.setText("Fechar");
        jButtonFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFecharActionPerformed(evt);
            }
        });

        jButtonEditaEscritor.setText("Editar");
        jButtonEditaEscritor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditaEscritorActionPerformed(evt);
            }
        });

        jButtonGravaEscritor.setText("Gravar");
        jButtonGravaEscritor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGravaEscritorActionPerformed(evt);
            }
        });

        jButtonApagaEscritor.setForeground(new java.awt.Color(204, 0, 51));
        jButtonApagaEscritor.setText("Apagar");
        jButtonApagaEscritor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApagaEscritorActionPerformed(evt);
            }
        });

        jTabbedPane1.setName("Geral"); // NOI18N

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 600));

        jLabel3.setText("Nome completo:");

        jLabel4.setText("Nacionalidade");

        jLabel5.setText("Nome:");

        jLabel6.setText("Data nascimento");

        jLabel7.setText("Ano nascimento:");

        jLabel8.setText("Local nascimento:");

        jLabel9.setText("Local morte:");

        jLabel10.setText("Ano morte::");

        jLabel11.setText("Data morte:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 283, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );

        jLabel1.setText("Ocupações:");

        jListOcupacoes.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListOcupacoes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListOcupacoes.setFixedCellWidth(45);
        jScrollPane2.setViewportView(jListOcupacoes);

        jButtonRemoveOcupacao.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonRemoveOcupacao.setForeground(new java.awt.Color(204, 0, 51));
        jButtonRemoveOcupacao.setText("x");
        jButtonRemoveOcupacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveOcupacaoActionPerformed(evt);
            }
        });

        jButtonAddOcupacao.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAddOcupacao.setForeground(new java.awt.Color(0, 102, 0));
        jButtonAddOcupacao.setText("+");
        jButtonAddOcupacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddOcupacaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextOcupacao, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                            .addComponent(jScrollPane2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonAddOcupacao)
                            .addComponent(jButtonRemoveOcupacao))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonRemoveOcupacao)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextOcupacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAddOcupacao))))
                .addGap(44, 44, 44))
        );

        jLabel12.setText("Generos:");

        jListGeneros.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListGeneros.setFixedCellWidth(45);
        jScrollPane3.setViewportView(jListGeneros);

        jButtonRemoveGenero.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonRemoveGenero.setForeground(new java.awt.Color(204, 0, 51));
        jButtonRemoveGenero.setText("x");
        jButtonRemoveGenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveGeneroActionPerformed(evt);
            }
        });

        jButtonAddGenero.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAddGenero.setForeground(new java.awt.Color(0, 102, 0));
        jButtonAddGenero.setText("+");
        jButtonAddGenero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddGeneroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3)
                            .addComponent(jTextGenero, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonRemoveGenero)
                            .addComponent(jButtonAddGenero))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonRemoveGenero)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAddGenero))))
                .addGap(44, 44, 44))
        );

        jLabel13.setText("Obras em destaque:");

        jListDestaques.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListDestaques.setFixedCellWidth(45);
        jScrollPane4.setViewportView(jListDestaques);

        jButtonRemoveDestaque.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonRemoveDestaque.setForeground(new java.awt.Color(204, 0, 51));
        jButtonRemoveDestaque.setText("x");
        jButtonRemoveDestaque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveDestaqueActionPerformed(evt);
            }
        });

        jButtonAddDestaque.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAddDestaque.setForeground(new java.awt.Color(0, 102, 0));
        jButtonAddDestaque.setText("+");
        jButtonAddDestaque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddDestaqueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextDestaque, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(jScrollPane4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonAddDestaque)
                            .addComponent(jButtonRemoveDestaque))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonRemoveDestaque)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextDestaque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAddDestaque))))
                .addGap(44, 44, 44))
        );

        jLabel28.setText("Fotografia:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextNome)
                            .addComponent(jTextNomeCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextNacionalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextDataNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextAnoNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextLocalNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextDataMorte, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextAnoMorte, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextLocalMorte, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFotografia, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelFotografia, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(182, 182, 182))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelFotografia, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextNomeCompleto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextNacionalidade))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextDataNascimento)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextAnoNascimento)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextLocalNascimento)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextDataMorte)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextAnoMorte)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextLocalMorte)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFotografia)
                            .addComponent(jLabel28))
                        .addGap(7, 7, 7)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1035, 1035, 1035))
        );

        jTabbedPane1.addTab("Geral", jPanel1);

        jLabel14.setText("Adicionar prémio:");

        jListPremios.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jListPremios);

        jButtonRemovePremio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonRemovePremio.setForeground(new java.awt.Color(204, 0, 51));
        jButtonRemovePremio.setText("Remover");
        jButtonRemovePremio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemovePremioActionPerformed(evt);
            }
        });

        jButtonAddPremio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAddPremio.setForeground(new java.awt.Color(0, 102, 0));
        jButtonAddPremio.setText("Adicionar");
        jButtonAddPremio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPremioActionPerformed(evt);
            }
        });

        jLabel15.setText("Nome:");

        jLabel16.setText("Ano:");

        jLabel17.setText("Lista de prémios:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRemovePremio))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextPremio, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jTextAno, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(404, 404, 404)
                                .addComponent(jButtonAddPremio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(170, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextPremio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAddPremio)
                    .addComponent(jLabel16))
                .addGap(42, 42, 42)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRemovePremio))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Prémios", jPanel3);

        jLabel18.setText("Adicionar livro:");

        jListLivros.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListLivros.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListLivros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListLivrosMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jListLivros);

        jButtonRemovePremio1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonRemovePremio1.setForeground(new java.awt.Color(204, 0, 51));
        jButtonRemovePremio1.setText("Remover");
        jButtonRemovePremio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemovePremio1ActionPerformed(evt);
            }
        });

        jButtonAddPremio1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonAddPremio1.setForeground(new java.awt.Color(0, 102, 0));
        jButtonAddPremio1.setText("Adicionar");
        jButtonAddPremio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPremio1ActionPerformed(evt);
            }
        });

        jLabel19.setText("Titulo:");

        jLabel20.setText("Preço:");

        jLabel21.setText("Lista de livros:");

        jLabel22.setText("Editora:");

        jLabelMostraCapa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel23.setText("Capa URL:");

        jLabel27.setText("ISBN:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextTitulo, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextEditora, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jTextPreco, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextURLCapa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addComponent(jButtonAddPremio1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonRemovePremio1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addComponent(jLabelMostraCapa, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel27)
                    .addComponent(jTextISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextEditora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jTextPreco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextURLCapa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jButtonAddPremio1))
                .addGap(30, 30, 30)
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonRemovePremio1))
                    .addComponent(jLabelMostraCapa, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Livros", jPanel8);

        javax.swing.GroupLayout jDialogEscritorLayout = new javax.swing.GroupLayout(jDialogEscritor.getContentPane());
        jDialogEscritor.getContentPane().setLayout(jDialogEscritorLayout);
        jDialogEscritorLayout.setHorizontalGroup(
            jDialogEscritorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogEscritorLayout.createSequentialGroup()
                .addGroup(jDialogEscritorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogEscritorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonApagaEscritor)
                        .addGap(48, 48, 48)
                        .addComponent(jButtonEditaEscritor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGravaEscritor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 508, Short.MAX_VALUE)
                        .addComponent(jButtonFechar))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialogEscritorLayout.setVerticalGroup(
            jDialogEscritorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogEscritorLayout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialogEscritorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonFechar)
                    .addComponent(jButtonEditaEscritor)
                    .addComponent(jButtonGravaEscritor)
                    .addComponent(jButtonApagaEscritor))
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jDialogXPathMenu.setModal(true);

        jLabel24.setText("Palavra de Pesquisa:");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton4.setText("Pesquisa XPath");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogXPathMenuLayout = new javax.swing.GroupLayout(jDialogXPathMenu.getContentPane());
        jDialogXPathMenu.getContentPane().setLayout(jDialogXPathMenuLayout);
        jDialogXPathMenuLayout.setHorizontalGroup(
            jDialogXPathMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogXPathMenuLayout.createSequentialGroup()
                .addGroup(jDialogXPathMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogXPathMenuLayout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jButton4))
                    .addGroup(jDialogXPathMenuLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jDialogXPathMenuLayout.setVerticalGroup(
            jDialogXPathMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogXPathMenuLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(jDialogXPathMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(35, 35, 35))
        );

        jLabel25.setText("Nome da Editora:");

        jLabel26.setText("Preço mínimo:");

        jTextFieldEditora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEditoraActionPerformed(evt);
            }
        });

        jButton5.setText("Pesquisa XPath");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogXPathMenu2Layout = new javax.swing.GroupLayout(jDialogXPathMenu2.getContentPane());
        jDialogXPathMenu2.getContentPane().setLayout(jDialogXPathMenu2Layout);
        jDialogXPathMenu2Layout.setHorizontalGroup(
            jDialogXPathMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogXPathMenu2Layout.createSequentialGroup()
                .addGroup(jDialogXPathMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogXPathMenu2Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(jDialogXPathMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jDialogXPathMenu2Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addGap(46, 46, 46)
                                .addComponent(jTextFieldEditora, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDialogXPathMenu2Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addGap(62, 62, 62)
                                .addComponent(jTextFieldPreco))))
                    .addGroup(jDialogXPathMenu2Layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(jButton5)))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jDialogXPathMenu2Layout.setVerticalGroup(
            jDialogXPathMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogXPathMenu2Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jDialogXPathMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jTextFieldEditora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialogXPathMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jTextFieldPreco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(69, 69, 69)
                .addComponent(jButton5)
                .addContainerGap(81, Short.MAX_VALUE))
        );

        jMenu6.setText("File");
        jMenuBar2.add(jMenu6);

        jMenu7.setText("Edit");
        jMenuBar2.add(jMenu7);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblFileName.setText("Conteúdo:");

        jTextAreaFile.setColumns(20);
        jTextAreaFile.setRows(5);
        jScrollPane1.setViewportView(jTextAreaFile);

        javax.swing.GroupLayout jPanelMostraFicheiroLayout = new javax.swing.GroupLayout(jPanelMostraFicheiro);
        jPanelMostraFicheiro.setLayout(jPanelMostraFicheiroLayout);
        jPanelMostraFicheiroLayout.setHorizontalGroup(
            jPanelMostraFicheiroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMostraFicheiroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMostraFicheiroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMostraFicheiroLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 885, Short.MAX_VALUE))
                    .addGroup(jPanelMostraFicheiroLayout.createSequentialGroup()
                        .addComponent(lblFileName)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelMostraFicheiroLayout.setVerticalGroup(
            jPanelMostraFicheiroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMostraFicheiroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFileName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Principal");

        jMenuItem1.setText("Sair");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Escritor");

        menuRecolherDados.setText("Recolher dados de um Escritor");
        menuRecolherDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRecolherDadosActionPerformed(evt);
            }
        });
        jMenu2.add(menuRecolherDados);

        jMenuItem2.setText("Mostrar dados de um Escritor");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Acrescentar um Escritor");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Editar dados de um Escritor");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem14.setText("Eliminar um Escritor");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Estuturas XML");

        jMenuItem5.setText("Mostrar XML de Escritores");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setText("Validação XSD de XML de Escritores");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem7.setText("Validação DTD de XML de Escritores");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);
        jMenu3.add(jSeparator1);

        jMenuItem8.setText("Mostrar XML de Obras");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuItem9.setText("Validação XSD de XML de Obras");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem9);

        jMenuItem10.setText("Validação DTD de XML de Obras");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);
        jMenu3.add(jSeparator2);

        jMenuItem11.setText("Mostrar XML Global");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        jMenuItem12.setText("Validação XSD de XML Global");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem12);

        jMenuItem13.setText("Validação DTD de XML Global");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem13);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Pesquisas XPath");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jMenuItem15.setText("Pesquisa autor");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem15);

        jMenuItem16.setText("Pesquisa nacionalidade");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem16);

        jMenuItem17.setText("Pesquisa obras de um autor");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem17);

        jMenuItem18.setText("Pesquisa escritor mais premiado");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem18);

        jMenuItem19.setText("Pesquisa livros de uma editora acima de um preço");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem19);

        jMenuItem20.setText("Pesquisar autores com um determinado prémios");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem20);

        jMenuItem21.setText("Pesquisar livros pelo titulo");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem21);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Exportacões XSLT");

        jMenuItem22.setText("HTML com nome e fotografia dos escritores");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem22);

        jMenuItem24.setText("XML com os 5 livros mais caros");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem24);

        jMenuItem25.setText("HTML com as obras de um autor");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem25);

        jMenuItem26.setText("XML com autores e obras");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem26);

        jMenuItem27.setText("HTML nascidos depois de 1950");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem27);

        jMenuItem28.setText("HTML com autores falecidos");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem28);

        jMenuItem29.setText("HTML vencedores de Nobel");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem29);

        jMenuBar1.add(jMenu5);

        jMenu8.setText("Exportações XQuery");

        jMenuItem23.setText("Lista de Autores ");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem23);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMostraFicheiro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMostraFicheiro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menuRecolherDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRecolherDadosActionPerformed
        // TODO add your handling code here:
        eEscritorAction = enumEscritorAction.Recolher;
        
        jDialogRecolher.setTitle("Recolher de escritor");
        jDialogRecolher.setSize(400, 250);
        jDialogRecolher.setLocation(200, 200);
        jDialogRecolher.setVisible(true);        
    }//GEN-LAST:event_menuRecolherDadosActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        // TODO add your handling code here:
        //System.out.println(jTextField1.getText());
        
        String autor = jTextAutorNome.getText();
        
        if(autor == null || autor.isEmpty())
        {
           jLabelAviso.setVisible(true);
        }
        else        
        {            
            jTextAutorNome.setText("");
            jLabelAviso.setVisible(false);
            jDialogRecolher.setVisible(false);

            switch(eEscritorAction)
            {
                case Recolher:
                    RecolheDadosDeEscritor(autor);
                    break;
                case Eliminar:
                    EliminarEscritor(autor);
                    break;
                case Editar:
                    EditarEscritor(autor);
                    break;
                case Mostrar:
                    MostraEscritor(autor);
                    break;                    
                case HTMcomObras:
                    GeraHTMComObras(autor);
                    break;
            }
        }
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void EliminarEscritor(String autor) throws HeadlessException {
        
        oGlobalEscritor = LoadEscritor(autor);
        if(oGlobalEscritor != null)
        {
            jDialogEscritor.setTitle("Eliminar escritor: " + autor);
        
            jButtonApagaEscritor.setVisible(true);
            jButtonEditaEscritor.setVisible(false);
            jButtonGravaEscritor.setVisible(false);

            try {
                File sourceimage = new File("imagemNaoDisponivel.png");
                Image image = ImageIO.read(sourceimage);
                //jLabelFotografia.setIcon( new ImageIcon(image));            
                //jLabelMostraCapa.setIcon( new ImageIcon(image));
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            jTabbedPane1.setSelectedIndex(0);
            jDialogEscritor.setSize(900, 650);
            jDialogEscritor.setLocation(200, 200);
            jDialogEscritor.setVisible(true);
        }
    }
    private void EditarEscritor(String autor) throws HeadlessException {
        
        jDialogEscritor.setTitle("Editar escritor: " + autor);
        
        oGlobalEscritor = LoadEscritor(autor);
        
        if(oGlobalEscritor == null)
            return;
        
        jButtonApagaEscritor.setVisible(false);
        jButtonEditaEscritor.setVisible(false);
        jButtonGravaEscritor.setEnabled(true);
        jButtonGravaEscritor.setVisible(true);
        
        /*try {
            File sourceimage = new File("imagemNaoDisponivel.png");
            Image image = ImageIO.read(sourceimage);
            //jLabelFotografia.setIcon( new ImageIcon(image));            
            //jLabelMostraCapa.setIcon( new ImageIcon(image));
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        jTabbedPane1.setSelectedIndex(0);
        jDialogEscritor.setSize(900, 650);
        jDialogEscritor.setLocation(200, 200);
        jDialogEscritor.setVisible(true);
    }
    
    private void MostraEscritor(String autor) throws HeadlessException {
        
        jDialogEscritor.setTitle("Mostrar escritor: " + autor);

        oGlobalEscritor = LoadEscritor(autor);
        
        if(oGlobalEscritor == null)
            return;
        
        jButtonApagaEscritor.setVisible(false);
        jButtonEditaEscritor.setVisible(true);
        jButtonGravaEscritor.setEnabled(false);
        jButtonGravaEscritor.setVisible(true);
        
        jTabbedPane1.setSelectedIndex(0);
        jDialogEscritor.setSize(900, 650);
        jDialogEscritor.setLocation(200, 200);
        jDialogEscritor.setVisible(true);
    }
    
    private void RecolheDadosDeEscritor(String autor) throws HeadlessException {
        try {
            // TODO:
            // 1. Verificar se o escritor já existe no XML escritores
            // 2. Se já existir pergutar se pretende pesquisar novamente e substituir dados
            boolean[] escritorExiste = new boolean[1];
            Escritor escritor = WrapperWikipedia.criaEscritor(autor, false, escritorExiste);
            
            if(escritor != null)
            {
                Document doc = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);
                //Chama a função para adicionar o livro ao XML
                doc = EscritorModeloXML.adicionaEscritor(escritor, doc);
                //grava o ficheiro XML em disco
                XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, ESCRITORES_XML_FILE);
                
                JOptionPane.showMessageDialog(this,"O autor '" + autor + "' foi adicionado com sucesso!");
                
                List<String> links = WrapperBertrand.obtem_links(autor);
                List<Livro> livrosEncontrados = WrapperBertrand.criaLivro(links, autor);

                if (!livrosEncontrados.isEmpty()) {  
                    Document obrasXML = XMLJDomFunctions.lerDocumentoXML(OBRAS_XML_FILE); 
                    //Document escritoresXML = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);

                    for (Livro livro : livrosEncontrados) {
                        obrasXML = EscritoresXML.adicionaEscritor(livro, obrasXML, doc );
                    }

                    XMLJDomFunctions.escreverDocumentoParaFicheiro(obrasXML, OBRAS_XML_FILE);
                }
            }
            else
            {
                if(escritorExiste[0])
                {
                    JOptionPane.showMessageDialog(this,"O autor '" + autor + "' já se encontra adicionado!");
                }
                else
                {
                    JOptionPane.showMessageDialog(this,"O autor '" + autor + "' não foi encontrado!");
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SaxonApiException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        // TODO add your handling code here:
        jTextAutorNome.setText("");
        jLabelAviso.setVisible(false);
        jDialogRecolher.setVisible(false);
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jButtonOkMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkMsgActionPerformed
        // TODO add your handling code here:
        jDialogMSG.setVisible(false);
    }//GEN-LAST:event_jButtonOkMsgActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        ShowXMLFile(ESCRITORES_XML_FILE);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        ShowXMLFile(OBRAS_XML_FILE);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here::
        ShowXMLFile(GLOBAL_XML_FILE);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // TODO add your handling code here:
        
        eEscritorAction = enumEscritorAction.Eliminar;
        
        jDialogRecolher.setTitle("Eliminar um escritor");
        jDialogRecolher.setSize(400, 250);
        jDialogRecolher.setLocation(200, 200);
        jDialogRecolher.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        
        eEscritorAction = enumEscritorAction.Mostrar;
        
        jDialogRecolher.setTitle("Mostrar dados de escritor");
        jDialogRecolher.setSize(400, 250);
        jDialogRecolher.setLocation(200, 200);
        jDialogRecolher.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        eEscritorAction = enumEscritorAction.Novo;        
        oGlobalEscritor = LoadEscritor("");   
        
        jButtonApagaEscritor.setVisible(false);
        jButtonEditaEscritor.setVisible(false);
        jButtonGravaEscritor.setEnabled(true);
        jButtonGravaEscritor.setVisible(true);
        
        try {
            File sourceimage = new File("imagemNaoDisponivel.png");
            Image image = ImageIO.read(sourceimage);
            jLabelFotografia.setIcon( new ImageIcon(image));            
            jLabelMostraCapa.setIcon( new ImageIcon(image));
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        jDialogEscritor.setTitle("Novo escritor");
        jTabbedPane1.setSelectedIndex(0);        
        jDialogEscritor.setSize(900, 650);
        jDialogEscritor.setLocation(200, 200);
        jDialogEscritor.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        
        eEscritorAction = enumEscritorAction.Editar;
        
        jDialogRecolher.setTitle("Editar dados de escritor");
        jDialogRecolher.setSize(400, 250);
        jDialogRecolher.setLocation(200, 200);
        jDialogRecolher.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButtonApagaEscritorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApagaEscritorActionPerformed
        // TODO add your handling code here:
        //remover livros
        //remover autor
        if(oGlobalEscritor != null)
        {
            Document doc = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);  
            Document docAtualizado = EscritorModeloXML.removeEscritor(oGlobalEscritor.getNome(), doc);
            
            Document docObras = XMLJDomFunctions.lerDocumentoXML(OBRAS_XML_FILE);
            Document docObrasAtualizado = EscritorModeloXML.removeLivros(oGlobalEscritor.getId(), docObras);
            
            if (docAtualizado != null) {
                
                if (docObrasAtualizado != null) {
                    XMLJDomFunctions.escreverDocumentoParaFicheiro(docObrasAtualizado, OBRAS_XML_FILE);
                    //System.out.println("Escritor removido com sucesso!");
                }

                XMLJDomFunctions.escreverDocumentoParaFicheiro(docAtualizado, ESCRITORES_XML_FILE);
                //System.out.println("Escritor removido com sucesso!");
                
                JOptionPane.showMessageDialog(this,
                    "O escritor " + oGlobalEscritor.getNome() + " foi removido com sucesso!",
                    "Remover escritor",
                    JOptionPane.INFORMATION_MESSAGE);
                
                jDialogEscritor.setVisible(false);
                oGlobalEscritor = null;
            }
            else
            {    JOptionPane.showMessageDialog(this,
                    "O escritor " + oGlobalEscritor.getNome() + " não foi encontrado!",
                    "Remover escritor",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Não foi possível remover o escritor !",
                "Remover escritor",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonApagaEscritorActionPerformed

    private void jButtonEditaEscritorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditaEscritorActionPerformed
        // TODO add your handling code here:
        eEscritorAction = enumEscritorAction.Editar;
        
        //jButtonEditaEscritor.setEnabled(false);
        jButtonGravaEscritor.setEnabled(true);
    }//GEN-LAST:event_jButtonEditaEscritorActionPerformed

    private void jButtonFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFecharActionPerformed
        // TODO add your handling code here:
        jDialogEscritor.setVisible(false);
        eEscritorAction = enumEscritorAction.Indefinido;
        oGlobalEscritor = null;
    }//GEN-LAST:event_jButtonFecharActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // TODO add your handling code here:
        jDialogXPathMenu.setSize(400, 250);
        jDialogXPathMenu.setLocation(200, 200);
        jDialogXPathMenu.setTitle("Pesquisa por autor");
        jTextField1.setText("");
        jDialogXPathMenu.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        int res = ValidarXML.validarDocumentoXSD("escritores.xml", "escritores.xsd");
        if (res == 1) {
            JOptionPane.showMessageDialog(this,
                    "Ficheiro válido por XSD",
                    "Validação XSD",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        if (res == -1) {
            JOptionPane.showMessageDialog(this,
                    "Ficheiro inválido por XSD",
                    "Validação XSD",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

       try {
            // TODO add your handling code here:
            int res = ValidarXML.validarDocumentoDTD("escritores.xml", "escritores.dtd");
            if (res == 1) {
                JOptionPane.showMessageDialog(this,
                        "Ficheiro válido por DTD",
                        "Validação DTD",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            if (res == -1) {
                JOptionPane.showMessageDialog(this,
                        "Ficheiro inválido por DTD",
                        "Validação DTD",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Verifique se os ficheiros XML e DTD existem",
                    "Validação DTD",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        int res = ValidarXML.validarDocumentoXSD("obras.xml", "obras.xsd");
        if (res == 1) {
            JOptionPane.showMessageDialog(this,
                    "Ficheiro válido por XSD",
                    "Validação XSD",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        if (res == -1) {
            JOptionPane.showMessageDialog(this,
                    "Ficheiro inválido por XSD",
                    "Validação XSD",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            int res = ValidarXML.validarDocumentoDTD("obras.xml", "obras.dtd");
            if (res == 1) {
                JOptionPane.showMessageDialog(this,
                        "Ficheiro válido por DTD",
                        "Validação DTD",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            if (res == -1) {
                JOptionPane.showMessageDialog(this,
                        "Ficheiro inválido por DTD",
                        "Validação DTD",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Verifique se os ficheiros XML e DTD existem",
                    "Validação DTD",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu4ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
        try {
            
            Path file = Paths.get(GLOBAL_XML_FILE);
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            file = Paths.get(ESCRITORES_XML_FILE);
            BasicFileAttributes attrEs = Files.readAttributes(file, BasicFileAttributes.class);
            System.out.println(attrEs.lastModifiedTime());
            System.out.println(attrEs.lastModifiedTime().to(TimeUnit.MINUTES));
            if(attrEs.lastModifiedTime().to(TimeUnit.MINUTES) > attr.lastModifiedTime().to(TimeUnit.MINUTES))
            {
                Document obrasXML = XMLJDomFunctions.lerDocumentoXML(OBRAS_XML_FILE); 
                Document escritoresXML = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);
                GlobalXML.criaGlobalXML(obrasXML, escritoresXML);
            }
            
            
            String xp = null;
            String pesquisa = jTextField1.getText();
            lblFileName.setText("Pesquisa por '"+pesquisa+"'.");
            // TODO add your handling code here:
            if (jDialogXPathMenu.getTitle().equals("Pesquisa por autor")) {
                //xp = "//livro[contains(autor, '" + pesquisa + "')]/titulo"; // por fazer
                xp = "//escritor[contains(lower-case(@nome), '" + pesquisa.toLowerCase() + "')]/@nome"
                        + " | //escritor[contains(lower-case(@nome), '" + pesquisa.toLowerCase() + "')]/nomeCompleto"
                         + " | //escritor[contains(lower-case(@nome), '" + pesquisa.toLowerCase() + "')]/nascimento";                
            }
            if (jDialogXPathMenu.getTitle().equals("Pesquisa por nacionalidade")) {
                xp = "//escritor[contains(lower-case(nacionalidade),'"+ pesquisa.toLowerCase() +"')]/@nome";                
            }
            if (jDialogXPathMenu.getTitle().equals("Pesquisa obras do autor")) {
                xp = "//escritor[contains(lower-case(@nome),'" + pesquisa.toLowerCase() + "')]//livro/titulo/text()";                
            }
            if (jDialogXPathMenu.getTitle().equals("Pesquisa por prémio")) {
                //xp = "//escritor/premios/premio[contains(lower-case(premio),'" + pesquisa.toLowerCase() + "')]/../../nomeCompleto";
                xp = "//escritor/premios/premio[contains(lower-case(text()),'" + pesquisa.toLowerCase() + "')]/../../nomeCompleto";
            }               
            if (jDialogXPathMenu.getTitle().equals("Pesquisa por título")) {
                /*xp = "//escritor/livros/livro[contains(lower-case(titulo),'"+pesquisa.toLowerCase()+"')]/../../nomeCompleto"
                        + " | //escritor/livros/livro[lower-case(titulo)='"+pesquisa.toLowerCase()+"']/titulo"
                        + " | //escritor/livros/livro[lower-case(titulo)='"+pesquisa.toLowerCase()+"']/editora"
                        + " | //escritor/livros/livro[lower-case(titulo)='"+pesquisa.toLowerCase()+"']/preco";
                */
                xp = "//escritor/livros/livro[contains(lower-case(titulo),'"+pesquisa.toLowerCase()+"')]/../../nomeCompleto"
                + " | //escritor/livros/livro[contains(lower-case(titulo),'"+pesquisa.toLowerCase()+"')]";                
            }
            XdmValue res = XPathFunctions.executaXpath(xp, "global.xml");  
            String s = XPathFunctions.listaResultado(res);
            jDialogXPathMenu.setVisible(false);
            if (res == null) {
                jTextAreaFile.setText("Ficheiro XML não existe");
            } else if (res.size() == 0) {
                jTextAreaFile.setText("Sem resultados.");
            } else {
                jTextAreaFile.setText(s);
            }
        } catch (SaxonApiException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // TODO add your handling code here:
        jDialogXPathMenu.setSize(400, 250);
        jDialogXPathMenu.setLocation(200, 200);
        jDialogXPathMenu.setTitle("Pesquisa por nacionalidade");
        jTextField1.setText("");
        jDialogXPathMenu.setVisible(true);
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        // TODO add your handling code here:
        jDialogXPathMenu.setSize(400, 250);
        jDialogXPathMenu.setLocation(200, 200);
        jDialogXPathMenu.setTitle("Pesquisa obras do autor");
        jTextField1.setText("");
        jDialogXPathMenu.setVisible(true);
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        // TODO add your handling code here:
        try {
            //String xp = "//escritor[count(premios/premio) = max(//escritor/count(premios/premio))]/@nome";
            String xp = "//escritor[count(premios/premio) = max(//escritor/count(premios/premio))]/nomeCompleto/text()"
                    + " | //escritor[count(premios/premio) = max(//escritor/count(premios/premio))]/premios/premio/text()";
            XdmValue res = XPathFunctions.executaXpath(xp, "global.xml");
            
            lblFileName.setText("Pesquisa 'Prémios do escritor com mais prémios'.");
            
            String s = XPathFunctions.listaResultado(res);
            jDialogXPathMenu.setVisible(false);
            if (res == null) {
                jTextAreaFile.setText("Ficheiro XML não existe");
            } else if (res.size() == 0) {
                jTextAreaFile.setText("Sem resultados.");
            } else {
                jTextAreaFile.setText("O Escritor com mais prémios é " + s);
            }
        } catch (SaxonApiException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jTextFieldEditoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEditoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEditoraActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        // TODO add your handling code here:
        jDialogXPathMenu2.setSize(500, 300);
        jDialogXPathMenu2.setLocation(200, 200);
        jDialogXPathMenu2.setTitle("Pesquisa editora com preco limite");
        jDialogXPathMenu2.setVisible(true);
        
      
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        try {
            
            String pesquisaEditora = jTextFieldEditora.getText();
            String pesquisaPreco = jTextFieldPreco.getText();
            
            String xp = "//livro[lower-case(editora) = '" + pesquisaEditora.toLowerCase() + "' and number(translate(preco, ',', '.')) > " 
                    + pesquisaPreco +"]/titulo/text()";

            XdmValue res = XPathFunctions.executaXpath(xp, "obras.xml");
            
            lblFileName.setText("Pesquisa Editora por '"+pesquisaEditora+"' e Preço '"+ pesquisaPreco +"'.");
              
            String s = XPathFunctions.listaResultado(res);
            jDialogXPathMenu2.setVisible(false);
            if (res == null) {
                jTextAreaFile.setText("Ficheiro XML não existe");
            } else if (res.size() == 0) {
                jTextAreaFile.setText("Sem resultados.");
            } else {
                jTextAreaFile.setText(s);
            }
        } catch (SaxonApiException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        // TODO add your handling code here:
        Document doc = XMLJDomFunctions.lerDocumentoXML("escritores.xml"); 
        if (doc != null) {
            try {
                Document novo = JDOMFunctions_XSLT.transformaDocumento(doc, "escritores.xml", "transf1.xsl"); 
                XMLJDomFunctions.escreverDocumentoParaFicheiro(novo, "transf1.html");
                doc = XMLJDomFunctions.lerDocumentoXML("transf1.html");
                String t = XMLJDomFunctions.escreverDocumentoString(doc);
            
                lblFileName.setText("Resultado transformação XSL.");
            
                jTextAreaFile.setText(t);
                JOptionPane.showMessageDialog(this,
                        "Transformação feita com sucesso... a abrir browser...",
                        "XSLT para HTML", JOptionPane.INFORMATION_MESSAGE);
                String url = "transf1.html";
                File htmlFile = new File(url);
                Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        int res = ValidarXML.validarDocumentoXSD("global.xml", "global.xsd");
        if (res == 1) {
            JOptionPane.showMessageDialog(this,
                    "Ficheiro válido por XSD",
                    "Validação XSD",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        if (res == -1) {
            JOptionPane.showMessageDialog(this,
                    "Ficheiro inválido por XSD",
                    "Validação XSD",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            int res = ValidarXML.validarDocumentoDTD("global.xml", "global.dtd");
            if (res == 1) {
                JOptionPane.showMessageDialog(this,
                        "Ficheiro válido por DTD",
                        "Validação DTD",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            if (res == -1) {
                JOptionPane.showMessageDialog(this,
                        "Ficheiro inválido por DTD",
                        "Validação DTD",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Verifique se os ficheiros XML e DTD existem",
                    "Validação DTD",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        // TODO add your handling code here:
        try {
                SaxonFunctions_XQuery.xQueryToText("query1.txt", "query1.xql");
                StringBuilder texto;
            try (Scanner ler = new Scanner (new FileInputStream("query1.txt"))) {
                texto = new StringBuilder();
                String linha;
                while(ler.hasNextLine()){
                    linha = ler.nextLine();
                    texto = texto.append(linha).append("\n");
                }
            }
            lblFileName.setText("Resultado de query XQL.");
                jTextAreaFile.setText(texto.toString());
            } catch (XPathException | IOException ex) {
                Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
            }

        JOptionPane.showMessageDialog(this,
                "Query feita com sucesso... ficheiro TXT criado! ",
                "XQuery",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        // TODO add your handling code here:
        Document doc = XMLJDomFunctions.lerDocumentoXML("global.xml");
            if (doc != null) {
                Document novo = JDOMFunctions_XSLT.transformaDocumento(doc, "global.xml", "transf2.xsl");
                XMLJDomFunctions.escreverDocumentoParaFicheiro(novo, "transf2.xml");
                doc = XMLJDomFunctions.lerDocumentoXML("transf2.xml");
                String t = XMLJDomFunctions.escreverDocumentoString(doc);
                lblFileName.setText("Resultado transformação XSL.");
                jTextAreaFile.setText(t);
            }
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jTextAutorNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAutorNomeKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10)
        {
            jButtonOKActionPerformed(null);
        }
    }//GEN-LAST:event_jTextAutorNomeKeyPressed

    private void jListLivrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListLivrosMouseClicked
        // TODO add your handling code here:
        //String selected = jListLivros.getSelectedValue();
        //System.out.println("sadasds " + selected);
        
        int i =  jListLivros.getSelectedIndex();
        //System.out.println("sadasds " + Integer.toString(i));
        
        if(oGlobalEscritor != null && oGlobalEscritor.getLivros() != null)
        {
            Livro oLivro = oGlobalEscritor.getLivros().get(i);
            if(oLivro != null)
            {
                ImageIcon imgIcon = getImageIcon(oLivro.getCapa());
                if(imgIcon != null)
                {
                    jLabelMostraCapa.setIcon(imgIcon);
                }
            }
        }
    }//GEN-LAST:event_jListLivrosMouseClicked

    private void jButtonGravaEscritorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGravaEscritorActionPerformed
        // TODO add your handling code here:
        if(oGlobalEscritor != null && (eEscritorAction == enumEscritorAction.Editar || eEscritorAction == enumEscritorAction.Novo))
        {
            oGlobalEscritor.setNome(jTextNome.getText());
                
            if(oGlobalEscritor.getNome() == null || oGlobalEscritor.getNome().equals(""))
            {
                JOptionPane.showMessageDialog(this,
                        "Tem que indicar o nome! ",
                        "Gravar escritor",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            oGlobalEscritor.setNomeCompleto(jTextNomeCompleto.getText());
            oGlobalEscritor.setNacionalidade(jTextNacionalidade.getText());
            oGlobalEscritor.setFotografia(jTextFotografia.getText());

            int intAux = 0;
            if(!jTextAnoNascimento.getText().isEmpty())
                intAux =Integer.parseInt(jTextAnoNascimento.getText());

            oGlobalEscritor.setNascimento(
                    new Efemeride(jTextDataNascimento.getText(),intAux,jTextLocalNascimento.getText()));

            if(jTextDataMorte.getText() != null || jTextLocalMorte.getText() != null || jTextAnoMorte.getText() != null)
            {
                intAux = 0;
                if(!jTextAnoMorte.getText().isEmpty())
                    intAux =Integer.parseInt(jTextAnoMorte.getText());

                oGlobalEscritor.setMorte(
                        new Efemeride(jTextDataMorte.getText(), intAux,jTextLocalMorte.getText()));
            }
            else
            {
                oGlobalEscritor.setMorte(null);
            }

            Document doc = null;
            Document docObras = null;
            if(eEscritorAction == enumEscritorAction.Editar && oGlobalEscritor.getId() > 0)
            {
                // apaga escritor
                doc = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);  
                Document docAtualizado1 = EscritorModeloXML.removeEscritorById(oGlobalEscritor.getId(), doc);

                docObras = XMLJDomFunctions.lerDocumentoXML(OBRAS_XML_FILE);
                Document docObrasAtualizado1 = EscritorModeloXML.removeLivros(oGlobalEscritor.getId(), docObras);
                /*    
                if (docAtualizado1 != null) {

                    if (docObrasAtualizado1 != null) {
                        XMLJDomFunctions.escreverDocumentoParaFicheiro(docObrasAtualizado1, OBRAS_XML_FILE);
                        //System.out.println("Escritor removido com sucesso!");
                    }

                    XMLJDomFunctions.escreverDocumentoParaFicheiro(docAtualizado1, ESCRITORES_XML_FILE);
                }*/
                doc = docAtualizado1;
                docObras = docObrasAtualizado1;
            }
            else
            {  
                try {
                    String xp = "max(//escritor/@id)";
                    XdmValue res = XPathFunctions.executaXpath(xp, ESCRITORES_XML_FILE);
                    if (res != null && res.size() > 0)
                    {
                        oGlobalEscritor.setId(Integer.parseInt(res.itemAt(0).getStringValue())+1);                
                    }   } catch (SaxonApiException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(doc == null)
            {
                doc = XMLJDomFunctions.lerDocumentoXML(ESCRITORES_XML_FILE);
            }
            //Chama a função para adicionar o livro ao XML
            doc = EscritorModeloXML.adicionaEscritor(oGlobalEscritor, doc);
            //grava o ficheiro XML em disco
            XMLJDomFunctions.escreverDocumentoParaFicheiro(doc, ESCRITORES_XML_FILE);

            List<Livro> livrosList  = oGlobalEscritor.getLivros();
            if( livrosList == null || livrosList.isEmpty())
            {
                try {
                    List<String> links = WrapperBertrand.obtem_links(oGlobalEscritor.getNome());
                    livrosList = WrapperBertrand.criaLivro(links, oGlobalEscritor.getNome());
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (!livrosList.isEmpty()) {
                if(docObras == null)
                {
                    docObras = XMLJDomFunctions.lerDocumentoXML(OBRAS_XML_FILE); 
                }
                
                for (Livro livro : livrosList) {
                    docObras = EscritorModeloXML.adicionaLivros(oGlobalEscritor.getId(), docObras,livro, oGlobalEscritor.getNome());
                }

                XMLJDomFunctions.escreverDocumentoParaFicheiro(docObras, OBRAS_XML_FILE);
            } 
            
            jDialogEscritor.setVisible(false);
            if(eEscritorAction == enumEscritorAction.Editar && oGlobalEscritor.getId() > 0)
            {
                JOptionPane.showMessageDialog(this,"O autor '" + oGlobalEscritor.getNome() + "' foi atualizado com sucesso!");                
            }
            else
            {
                JOptionPane.showMessageDialog(this,"O autor '" + oGlobalEscritor.getNome() + "' foi adicionado com sucesso!");                
            }            

        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Ocorreu um erro e não é possível gravar escritor! ",
                "Gravar escritor",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonGravaEscritorActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        // TODO add your handling code here:
        jDialogXPathMenu.setSize(400, 250);
        jDialogXPathMenu.setLocation(200, 200);
        jDialogXPathMenu.setTitle("Pesquisa por prémio");
        jTextField1.setText("");
        jDialogXPathMenu.setVisible(true);
        /*try {
            String xp = "//escritor[count(premios/premio) = max(//escritor/count(premios/premio))]/nomeCompleto/text()"
                    + " | //escritor[count(premios/premio) = max(//escritor/count(premios/premio))]/premios/premio/text()";
            XdmValue res = XPathFunctions.executaXpath(xp, "global.xml");
            
            String s = XPathFunctions.listaResultado(res);
            jDialogXPathMenu.setVisible(false);
            if (res == null) {
                jTextAreaFile.setText("Ficheiro XML não existe");
            } else if (res.size() == 0) {
                jTextAreaFile.setText("Sem resultados.");
            } else {
                jTextAreaFile.setText("O Escritor com mais prémios é " + s);
            }
        } catch (SaxonApiException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        // TODO add your handling code here:
        jDialogXPathMenu.setSize(400, 250);
        jDialogXPathMenu.setLocation(200, 200);
        jDialogXPathMenu.setTitle("Pesquisa por título");
        jTextField1.setText("");
        jDialogXPathMenu.setVisible(true);
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10)
        {
            jButton4ActionPerformed(null);
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        // TODO add your handling code here:
        eEscritorAction = enumEscritorAction.HTMcomObras;
        
        jDialogRecolher.setTitle("Recolher de obras do escritor");
        jDialogRecolher.setSize(400, 250);
        jDialogRecolher.setLocation(200, 200);
        jDialogRecolher.setVisible(true);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        // TODO add your handling code here:
        Document doc = XMLJDomFunctions.lerDocumentoXML("global.xml");
        if (doc != null) {
            Document novo = JDOMFunctions_XSLT.transformaDocumento(doc, "global.xml", "transf3.xsl");
            XMLJDomFunctions.escreverDocumentoParaFicheiro(novo, "transf3.xml");
            doc = XMLJDomFunctions.lerDocumentoXML("transf3.xml");
            String t = XMLJDomFunctions.escreverDocumentoString(doc);
            lblFileName.setText("Resultado transformação XSL.");
            jTextAreaFile.setText(t);
        }
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        // TODO add your handling code here:
        Document doc = XMLJDomFunctions.lerDocumentoXML("global.xml"); 
        if (doc != null) {
            try {
                Document novo = JDOMFunctions_XSLT.transformaDocumento(doc, "global.xml", "transf5.xsl"); 
                XMLJDomFunctions.escreverDocumentoParaFicheiro(novo, "transf5.html");
                doc = XMLJDomFunctions.lerDocumentoXML("transf5.html");
                String t = XMLJDomFunctions.escreverDocumentoString(doc);
            
                lblFileName.setText("Resultado transformação XSL.");
            
                jTextAreaFile.setText(t);
                JOptionPane.showMessageDialog(this,
                        "Transformação feita com sucesso... a abrir browser...",
                        "XSLT para HTML", JOptionPane.INFORMATION_MESSAGE);
                String url = "transf5.html";
                File htmlFile = new File(url);
                Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        // TODO add your handling code here:
        Document doc = XMLJDomFunctions.lerDocumentoXML("global.xml"); 
                
        if (doc != null) {
            try {
                Document novo = JDOMFunctions_XSLT.transformaDocumento(doc, "global.xml", "transf6.xsl"); 
                XMLJDomFunctions.escreverDocumentoParaFicheiro(novo, "transf6.html");
                doc = XMLJDomFunctions.lerDocumentoXML("transf6.html");
                String t = XMLJDomFunctions.escreverDocumentoString(doc);
            
                lblFileName.setText("Resultado transformação XSL.");
            
                jTextAreaFile.setText(t);
                JOptionPane.showMessageDialog(this,
                        "Transformação feita com sucesso... a abrir browser...",
                        "XSLT para HTML", JOptionPane.INFORMATION_MESSAGE);
                String url = "transf6.html";
                File htmlFile = new File(url);
                Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        // TODO add your handling code here:
        Document doc = XMLJDomFunctions.lerDocumentoXML("global.xml"); 
        if (doc != null) {
            try {
                Document novo = JDOMFunctions_XSLT.transformaDocumento(doc, "global.xml", "transf7.xsl"); 
                XMLJDomFunctions.escreverDocumentoParaFicheiro(novo, "transf7.html");
                doc = XMLJDomFunctions.lerDocumentoXML("transf7.html");
                String t = XMLJDomFunctions.escreverDocumentoString(doc);
            
                lblFileName.setText("Resultado transformação XSL.");
            
                jTextAreaFile.setText(t);
                JOptionPane.showMessageDialog(this,
                        "Transformação feita com sucesso... a abrir browser...",
                        "XSLT para HTML", JOptionPane.INFORMATION_MESSAGE);
                String url = "transf7.html";
                File htmlFile = new File(url);
                Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jButtonAddOcupacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddOcupacaoActionPerformed
        // TODO add your handling code here:
        if(!jTextOcupacao.getText().equals("") && oGlobalEscritor != null)
        {
            String[] oldArray = oGlobalEscritor.getOcupacao();
            String[] newArray = new String[oGlobalEscritor.getOcupacao().length+1];
            int i;
            for (i = 0; i < oldArray.length; i++)
            {
                newArray[i] = oldArray[i];
            }
            newArray[i] = jTextOcupacao.getText();
            
            oGlobalEscritor.setOcupacao(newArray);

            jListOcupacoes.removeAll();
            jListOcupacoes.setListData(oGlobalEscritor.getOcupacao());
        }
    }//GEN-LAST:event_jButtonAddOcupacaoActionPerformed

    private void jButtonRemoveOcupacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveOcupacaoActionPerformed
        // TODO add your handling code here:
        int j =  jListOcupacoes.getSelectedIndex();
        
        if(j>=0 && oGlobalEscritor != null)
        {
            String[] oldArray = oGlobalEscritor.getOcupacao();
            String[] newArray = new String[oGlobalEscritor.getOcupacao().length-1];
            int i;
            for (i = 0; i < oldArray.length; i++)
            {
                if( i < j )
                    newArray[i] = oldArray[i];
                else if( i > j)
                    newArray[i-1] = oldArray[i];
            }
            oGlobalEscritor.setOcupacao(newArray);

            jListOcupacoes.removeAll();
            jListOcupacoes.setListData(oGlobalEscritor.getOcupacao());
        }
        
    }//GEN-LAST:event_jButtonRemoveOcupacaoActionPerformed

    private void jButtonAddGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddGeneroActionPerformed
        // TODO add your handling code here:
        if(!jTextGenero.getText().equals("") && oGlobalEscritor != null)
        {
            String[] oldArray = oGlobalEscritor.getGenero();
            String[] newArray = new String[oGlobalEscritor.getGenero().length+1];
            int i;
            for (i = 0; i < oldArray.length; i++)
            {
                newArray[i] = oldArray[i];
            }
            newArray[i] = jTextGenero.getText();
            
            oGlobalEscritor.setGenero(newArray);

            jListGeneros.removeAll();
            jListGeneros.setListData(oGlobalEscritor.getGenero());
        }

    }//GEN-LAST:event_jButtonAddGeneroActionPerformed

    private void jButtonAddDestaqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddDestaqueActionPerformed
        // TODO add your handling code here:
        if(!jTextDestaque.getText().equals("") && oGlobalEscritor != null)
        {
            String[] oldArray = oGlobalEscritor.getDestaques();
            String[] newArray = new String[oGlobalEscritor.getDestaques().length+1];
            int i;
            for (i = 0; i < oldArray.length; i++)
            {
                newArray[i] = oldArray[i];
            }
            newArray[i] = jTextDestaque.getText();
            
            oGlobalEscritor.setDestaques(newArray);

            jListDestaques.removeAll();
            jListDestaques.setListData(oGlobalEscritor.getDestaques());
        }
    }//GEN-LAST:event_jButtonAddDestaqueActionPerformed

    private void jButtonRemoveGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveGeneroActionPerformed
        // TODO add your handling code here:
        int j =  jListGeneros.getSelectedIndex();
        
        if(j>=0 && oGlobalEscritor != null)
        {
            String[] oldArray = oGlobalEscritor.getGenero();
            String[] newArray = new String[oGlobalEscritor.getGenero().length-1];
            int i;
            for (i = 0; i < oldArray.length; i++)
            {
                if( i < j )
                    newArray[i] = oldArray[i];
                else if( i > j)
                    newArray[i-1] = oldArray[i];
            }
            oGlobalEscritor.setGenero(newArray);

            jListGeneros.removeAll();
            jListGeneros.setListData(oGlobalEscritor.getGenero());
        }
    }//GEN-LAST:event_jButtonRemoveGeneroActionPerformed

    private void jButtonRemoveDestaqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveDestaqueActionPerformed
        // TODO add your handling code here:
        int j =  jListDestaques.getSelectedIndex();
        
        if(j>=0 && oGlobalEscritor != null)
        {
            String[] oldArray = oGlobalEscritor.getDestaques();
            String[] newArray = new String[oGlobalEscritor.getDestaques().length-1];
            int i;
            for (i = 0; i < oldArray.length; i++)
            {
                if( i < j )
                    newArray[i] = oldArray[i];
                else if( i > j)
                    newArray[i-1] = oldArray[i];
            }
            oGlobalEscritor.setDestaques(newArray);

            jListDestaques.removeAll();
            jListDestaques.setListData(oGlobalEscritor.getDestaques());
        }
    }//GEN-LAST:event_jButtonRemoveDestaqueActionPerformed

    private void jButtonRemovePremioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemovePremioActionPerformed
        // TODO add your handling code here:
        int j =  jListPremios.getSelectedIndex();
        
        if(j>=0 && oGlobalEscritor != null)
        {
            List<Premio> lst = oGlobalEscritor.getPremios();
            lst.remove(j);            
            oGlobalEscritor.setPremios(lst);

            jListPremios.removeAll();
            jListPremios.setListData(oGlobalEscritor.getPremiosList());
        }
    }//GEN-LAST:event_jButtonRemovePremioActionPerformed

    private void jButtonRemovePremio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemovePremio1ActionPerformed
        // TODO add your handling code here:
        int j =  jListLivros.getSelectedIndex();
        
        if(j>=0 && oGlobalEscritor != null)
        {
            List<Livro> lst = oGlobalEscritor.getLivros();
            lst.remove(j);            
            oGlobalEscritor.setLivros(lst);

            jListLivros.removeAll();
            jListLivros.setListData(oGlobalEscritor.getLivrosList());
        }
    }//GEN-LAST:event_jButtonRemovePremio1ActionPerformed

    private void jButtonAddPremioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPremioActionPerformed
        // TODO add your handling code here:
                // TODO add your handling code here:
        if(!jTextPremio.getText().equals("") && !jTextAno.getText().equals("") 
                && Integer.parseInt(jTextAno.getText()) > 0 && oGlobalEscritor != null)
        {
            List<Premio> lst;
            if(oGlobalEscritor.getPremios() != null)
            {
                lst = oGlobalEscritor.getPremios();
            }
            else
            {       
                lst = new ArrayList<Premio>() ;
            }
            
            Premio pr = new Premio();
            pr.setNome(jTextPremio.getText());
            pr.setAno(Integer.parseInt(jTextAno.getText()));
            lst.add(pr);
            
            oGlobalEscritor.setPremios(lst);

            jListPremios.removeAll();
            jListPremios.setListData(oGlobalEscritor.getPremiosList());
        }
    }//GEN-LAST:event_jButtonAddPremioActionPerformed

    private void jButtonAddPremio1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPremio1ActionPerformed
        // TODO add your handling code here:
                if(!jTextTitulo.getText().equals("") && oGlobalEscritor != null)
        {
            
            List<Livro> lst;
            if(oGlobalEscritor.getLivros() != null)
            {
                lst = oGlobalEscritor.getLivros();
            }
            else
            {       
                lst = new ArrayList<Livro>() ;
            }            
            
            Livro pr = new Livro();
            
            pr.setTitulo(jTextTitulo.getText());
            pr.setIsbn(jTextISBN.getText());
            pr.setAutor(oGlobalEscritor.getNome());
            pr.setEditora(jTextEditora.getText());
            pr.setCapa(jTextURLCapa.getText());
            pr.setPreco(jTextPreco.getText());
            lst.add(pr);
            
            oGlobalEscritor.setLivros(lst);

            jListLivros.removeAll();
            jListLivros.setListData(oGlobalEscritor.getLivrosList());
        }
    }//GEN-LAST:event_jButtonAddPremio1ActionPerformed

    private void ShowXMLFile(String fileName)
    {
        Document doc = XMLJDomFunctions.lerDocumentoXML(fileName);
        lblFileName.setText("Conteúdo do ficheiro: " + fileName);
        String t = XMLJDomFunctions.escreverDocumentoString(doc);
        
        jTextAreaFile.setText(t);
        lblFileName.setVisible(true);
        jTextAreaFile.setVisible(true);
    }
    
    private void ShowMessage(String msg)
    {
        jDialogMSG.setTitle("Informação");
        jLabelMSG.setText(msg);
        jButtonNao.setVisible(false);
        jButtonSim.setVisible(false);
        jButtonOkMsg.setLocation(150, 50);
        jDialogMSG.setSize(400, 250);
        jDialogMSG.setLocation(200, 200);        
        jDialogMSG.setVisible(true);
    }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonAddDestaque;
    private javax.swing.JButton jButtonAddGenero;
    private javax.swing.JButton jButtonAddOcupacao;
    private javax.swing.JButton jButtonAddPremio;
    private javax.swing.JButton jButtonAddPremio1;
    private javax.swing.JButton jButtonApagaEscritor;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonEditaEscritor;
    private javax.swing.JButton jButtonFechar;
    private javax.swing.JButton jButtonGravaEscritor;
    private javax.swing.JButton jButtonNao;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JButton jButtonOkMsg;
    private javax.swing.JButton jButtonRemoveDestaque;
    private javax.swing.JButton jButtonRemoveGenero;
    private javax.swing.JButton jButtonRemoveOcupacao;
    private javax.swing.JButton jButtonRemovePremio;
    private javax.swing.JButton jButtonRemovePremio1;
    private javax.swing.JButton jButtonSim;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialogEscritor;
    private javax.swing.JDialog jDialogMSG;
    private javax.swing.JDialog jDialogRecolher;
    private javax.swing.JDialog jDialogXPathMenu;
    private javax.swing.JDialog jDialogXPathMenu2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAviso;
    private javax.swing.JLabel jLabelFotografia;
    private javax.swing.JLabel jLabelMSG;
    private javax.swing.JLabel jLabelMostraCapa;
    private javax.swing.JList<String> jListDestaques;
    private javax.swing.JList<String> jListGeneros;
    private javax.swing.JList<String> jListLivros;
    private javax.swing.JList<String> jListOcupacoes;
    private javax.swing.JList<String> jListPremios;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelMostraFicheiro;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextAno;
    private javax.swing.JTextField jTextAnoMorte;
    private javax.swing.JTextField jTextAnoNascimento;
    private javax.swing.JTextArea jTextAreaFile;
    private javax.swing.JTextField jTextAutorNome;
    private javax.swing.JTextField jTextDataMorte;
    private javax.swing.JTextField jTextDataNascimento;
    private javax.swing.JTextField jTextDestaque;
    private javax.swing.JTextField jTextEditora;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextFieldEditora;
    private javax.swing.JTextField jTextFieldPreco;
    private javax.swing.JTextField jTextFotografia;
    private javax.swing.JTextField jTextGenero;
    private javax.swing.JTextField jTextISBN;
    private javax.swing.JTextField jTextLocalMorte;
    private javax.swing.JTextField jTextLocalNascimento;
    private javax.swing.JTextField jTextNacionalidade;
    private javax.swing.JTextField jTextNome;
    private javax.swing.JTextField jTextNomeCompleto;
    private javax.swing.JTextField jTextOcupacao;
    private javax.swing.JTextField jTextPreco;
    private javax.swing.JTextField jTextPremio;
    private javax.swing.JTextField jTextTitulo;
    private javax.swing.JTextField jTextURLCapa;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JMenuItem menuRecolherDados;
    // End of variables declaration//GEN-END:variables
}
