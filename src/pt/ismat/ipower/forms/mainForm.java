package pt.ismat.ipower.forms;

import pt.ismat.ipower.utils.Apartments;
import pt.ismat.ipower.utils.Buildings;
import pt.ismat.ipower.utils.Counter;
import pt.ismat.ipower.utils.Devices;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

/**
 * Created by v1p3r on 29-12-2016.
 */
public class mainForm {

    private JPanel mainFrame;
    public JTree treeBuildings;
    private JPanel leftFrame;
    private JPanel rightFrame;
    private JPanel bottomFrame;
    private JButton btnLigar;
    private JButton btnDesligar;
    private JProgressBar pbEquipamentos;
    private JProgressBar pbSimulatorStatus;
    private JLabel lblActiveDevices;
    private JLabel lblActiveDevicesTotal;
    private JLabel lblSimStatus;
    private JLabel lblEquipamentosTotalKw;
    private JLabel lblTotalKw;
    private JLabel lblLeituras;
    private JLabel lblLeiturasTotal;
    private JLabel lblCargarTotal;
    private JLabel lblCargaTotalData;
    private JLabel lblStatusBar;
    private JLabel lblStatusBarData;
    public Counter cDevicesCounter;
    public static JLabel LeiturasTotal;
    public static JLabel TotalKw;
    public static JLabel CargaTotal;

    public mainForm() {
        createTree();
        LeiturasTotal = this.lblLeiturasTotal;
        TotalKw = this.lblTotalKw;
        CargaTotal = this.lblCargaTotalData;

        lblActiveDevicesTotal.setText(Devices.getActiveDevices().toString() + "/" + Devices.getDevices().toString());
        pbEquipamentos.setMaximum(Devices.getDevices());
        pbEquipamentos.setValue(Devices.getActiveDevices());


        btnLigar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cDevicesCounter = new Counter("Contador Equipamentos Activos");
                cDevicesCounter.start();

                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");

                lblStatusBarData.setText("Em funcionamento...Iniciado em: " + dt.format(cDevicesCounter.getDataInicial()));
                lblSimStatus.setText("activo");
                btnLigar.setEnabled(false);
                btnDesligar.setEnabled(true);

                pbSimulatorStatus.setIndeterminate(true);
                //pbSimulatorStatus.setVisible(true);
            }
        });

        /**
         * Metodo que associa um action listener ao botao desligar
         */
        btnDesligar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // TODO Criar algoritmo que efetua o registo da leitura dos equipamentos no respectivo apartamento no seu ficheiro xml
                cDevicesCounter.terminate(); // termina o contador
                cDevicesCounter.resetCounter();

                // TODO Metodo que faça update dos valores no gui
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
                lblStatusBarData.setText("Desligado...Iniciado em: " + dt.format(cDevicesCounter.getDataInicial()) + " Terminado em: " + dt.format(cDevicesCounter.getDataFinal()));

                lblLeiturasTotal.setText("0");
                lblTotalKw.setText("0 Kw");
                lblCargaTotalData.setText("0 Kw");
                lblSimStatus.setText("inactivo");
                btnLigar.setEnabled(true);
                btnDesligar.setEnabled(false);
                //pbSimulatorStatus.setVisible(false);
                pbSimulatorStatus.setIndeterminate(false);

            }
        });


    }

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

                JDialog buildFrame = new JDialog(frame,"iPower - Edificios",Dialog.ModalityType.APPLICATION_MODAL); // cria frame em MODAL
                buildFrame.setContentPane(new buildForm().mainFrame); // carrega o main panel feito no gui
                buildFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                buildFrame.setResizable(false);
                buildFrame.setPreferredSize(new Dimension(400, 400));

                buildFrame.pack();
                buildFrame.setLocationRelativeTo(null);

                buildFrame.setVisible(true);

                buildFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        //((DefaultTreeModel)new mainForm().treeBuildings.getModel()).reload();

                    }
                });

            }
        });

        tabelas.add(edificiosItem);

        // Tabelas->Apartamentos
        JMenuItem apartamentosItem = new JMenuItem("Apartamentos...", icon);
        // adiciona o listener para captar evento
        apartamentosItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JDialog apartFrame = new JDialog(frame,"iPower - Apartamentos",Dialog.ModalityType.APPLICATION_MODAL); // cria frame em MODAL
                apartFrame.setContentPane(new apartForm().mainFrame); // carrega o main panel feito no gui
                apartFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                apartFrame.setResizable(false);
                apartFrame.setPreferredSize(new Dimension(400, 400));

                apartFrame.pack();
                apartFrame.setLocationRelativeTo(null);

                apartFrame.setVisible(true);
            }
        });
        tabelas.add(apartamentosItem);

        // Mapas->Equipamentos
        JMenuItem equipamentosItem = new JMenuItem("Equipamentos...", icon);
        // adiciona o listener para captar evento
        equipamentosItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JDialog equipFrame = new JDialog(frame,"iPower - Equipamentos",Dialog.ModalityType.APPLICATION_MODAL); // cria frame em MODAL
                equipFrame.setContentPane(new equipForm().mainFrame); // carrega o main panel feito no gui
                equipFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                equipFrame.setResizable(false);
                equipFrame.setPreferredSize(new Dimension(600, 400));

                equipFrame.pack();
                equipFrame.setLocationRelativeTo(null);

                equipFrame.setVisible(true);
            }
        });
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

    public void createTree(){

        // Cria cada edificio
        ArrayList arrBuildingsList = Buildings.getBuildingsList();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Edificios");

        for (int i = 0; i < arrBuildingsList.size(); i++) {
            String strBuilding = (String) arrBuildingsList.get(i);
            String[] arrBuilding = strBuilding.split("#");

            DefaultMutableTreeNode buildingNode = new DefaultMutableTreeNode(arrBuilding[0] + "-" + arrBuilding[1]);

            // Cria cada apartamento
            ArrayList arrApartmentsList = Apartments.getApartmentList(Integer.valueOf(arrBuilding[0]));

            for (int b = 0; b < arrApartmentsList.size(); b++) {
                String strApartment = (String) arrApartmentsList.get(b);
                String[] arrApartment = strApartment.split("#");

                DefaultMutableTreeNode apartmentNode = new DefaultMutableTreeNode(arrApartment[0] + "-" + arrApartment[1]);
                buildingNode.add(apartmentNode);

                // Cria os equipamentos de cada apartamento
                ArrayList arrDevicesList = Devices.getDevicesList(arrApartment[0]);

                for (int d = 0; d < arrDevicesList.size(); d++) {
                    String strDevice = (String) arrDevicesList.get(d);
                    String[] arrDevice = strDevice.split("#");

                    apartmentNode.add(new DefaultMutableTreeNode(arrDevice[0] + "-" + arrDevice[1]));

                }
            }



            root.add(buildingNode);

        }

        treeBuildings.setModel(new DefaultTreeModel(root));
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
