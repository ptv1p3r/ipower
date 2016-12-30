package pt.ismat.ipower.forms;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Created by v1p3r on 29-12-2016.
 */
public class mainForm {

    private JPanel mainFrame;



    /**
     * Cria a barra de menu com todos os items e eventos associados
     * @param frame Frame a ser passada para construcao do menu
     */
    private static void createMenuBar(final JFrame frame) {

        JMenuBar menubar = new JMenuBar();
        ImageIcon icon = new ImageIcon("exit.png");

        // Ficheiros
        JMenu file = new JMenu("Ficheiros");
        file.setMnemonic(KeyEvent.VK_F); // adiciona tecla

        // Ficheiros->Importar
        JMenuItem importarItem = new JMenuItem("Importar...", icon);
        importarItem.setMnemonic(KeyEvent.VK_I);
        // adiciona o listener para captar evento
        importarItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Filtros de ficheiros
                FileFilter excel2003 = new FileNameExtensionFilter("ficheiros Excel 2003 ou inf. (.xls)", new String[] {"xls"});
                FileFilter excel2007 = new FileNameExtensionFilter("ficheiros Excel 2007 ou sup. (.xlsx)", new String[] {"xlsx"});

                JFileChooser fileChooser = new JFileChooser(); // Adiciona o file chooser para indicar o ficheiro a importar

                // Aplica filtros
                fileChooser.addChoosableFileFilter(excel2007); // opcao para filtro
                fileChooser.setFileFilter(excel2003); // filtro selected
                fileChooser.setAcceptAllFileFilterUsed(false); // desliga all files do filtro

                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); // path para localizar ficheiro
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    //System.out.println("Selected file: " + selectedFile.getAbsolutePath());

//                    Parser excelFile = new Parser(selectedFile);
//                    excelFile.exportToPdf();
                }
            }
        });
        file.add(importarItem);

        file.addSeparator();

        // Ficheiros->Sair
        JMenuItem eMenuItem = new JMenuItem("Sair", icon);
        eMenuItem.setMnemonic(KeyEvent.VK_S);
        eMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        frame,"Têm a certeza que deseja fechar a aplicação?","Sair de iPower",JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION){
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    System.exit(0);
                }
            }
        });
        file.add(eMenuItem);

        // Tabelas
        JMenu tabelas = new JMenu("Tabelas");
        tabelas.setMnemonic(KeyEvent.VK_T);

        // Tabelas->Edificios
        JMenuItem edificiosItem = new JMenuItem("Edificios...", icon);

        // adiciona o listener para captar evento
        edificiosItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JDialog buildFrame = new JDialog(frame,"iMaps - Edificios",Dialog.ModalityType.APPLICATION_MODAL); // cria frame em MODAL
                buildFrame.setContentPane(new buildForm().mainFrame); // carrega o main panel feito no gui
                buildFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                buildFrame.setResizable(false);
                buildFrame.setPreferredSize(new Dimension(400, 400));

                buildFrame.pack();
                buildFrame.setLocationRelativeTo(null);

                buildFrame.setVisible(true);
            }
        });
        tabelas.add(edificiosItem);

        // Tabelas->Apartamentos
        JMenuItem apartamentosItem = new JMenuItem("Apartamentos...", icon);
        tabelas.add(apartamentosItem);

        // Mapas->Equipamentos
        JMenuItem equipamentosItem = new JMenuItem("Equipamentos...", icon);
        tabelas.add(equipamentosItem);

        // Preferencias
        JMenu preferencias = new JMenu("Preferencias");
        preferencias.setMnemonic(KeyEvent.VK_P);

        // Preferencias->Configuracao
        JMenuItem configuracaoItem = new JMenuItem("Configuração...", icon);
        // adiciona o listener para captar evento
        configuracaoItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JDialog configFrame = new JDialog(frame,"iPower - Configuração",Dialog.ModalityType.APPLICATION_MODAL); // cria frame em MODAL
                configFrame.setContentPane(new confForm().mainFrame); // carrega o main panel feito no gui
                configFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                configFrame.setResizable(false);
                configFrame.setPreferredSize(new Dimension(400, 400));

                configFrame.pack();
                configFrame.setLocationRelativeTo(null);

                configFrame.setVisible(true);

            }
        });
        preferencias.add(configuracaoItem);

        // Ajuda
        JMenu ajuda = new JMenu("Ajuda");
        ajuda.setMnemonic(KeyEvent.VK_A);

        // Ajuda->Sobre
        JMenuItem sobreItem = new JMenuItem("Sobre", icon);
        sobreItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,"Autores: Pedro Roldan 21501217\nLeandro Moreira 21501217\nVersão: 0.1 alpha\nISMAT POO 2016","Sobre iPower...",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ajuda.add(sobreItem);

        // adiciona os menus a menubar
        menubar.add(file);
        menubar.add(tabelas);
        menubar.add(preferencias);
        //menubar.add(Box.createHorizontalGlue()); // horizontal glue
        menubar.add(ajuda);

        frame.setJMenuBar(menubar);
    }

    /**
     * Inicializa o gui
     */
    public static void createGUI() {
        JFrame mainFrame = new JFrame("iPower");

        mainFrame.setContentPane(new mainForm().mainFrame); // carrega o main panel feito no gui
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setResizable(false);
        mainFrame.setPreferredSize(new Dimension(800, 600));
        createMenuBar(mainFrame);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
