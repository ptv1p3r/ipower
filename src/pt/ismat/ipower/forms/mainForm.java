package pt.ismat.ipower.forms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.*;
import pt.ismat.ipower.utils.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by v1p3r on 29-12-2016.
 */
public class mainForm {

    public JTree treeBuildings;
    private JPanel mainFrame, leftFrame, rightFrame, bottomFrame;
    private JButton btnLigar, btnDesligar;
    private JProgressBar pbEquipamentos;
    private JProgressBar pbSimulatorStatus;
    private JLabel lblActiveDevices;
    private JLabel lblActiveDevicesTotal,lblSimStatus;
    private JLabel lblEquipamentosTotalKw;
    private JLabel lblTotalKw;
    private JLabel lblLeituras;
    private JLabel lblLeiturasTotal;
    private JLabel lblCargarTotal;
    private JLabel lblCargaTotalData;
    private JLabel lblStatusBar;
    private JLabel lblStatusBarData;
    private JPanel pDataInfo;
    private JLabel lblTeste;
    private JPanel pGrafico;
    private JLabel lblTimer;
    private JLabel lblSemaforo;
    private JButton btnExpandTree;
    private JButton btnColapseTree;
    private JLabel lblDevTipo;
    private JLabel lblDevConsumo;
    private JLabel lblDevPic;
    private JLabel lblBuildPic;
    public Counter cDevicesCounter;

    public static JProgressBar Equipamentos;
    public static JLabel LeiturasTotal,TotalKw,CargaTotal,ActiveDevicesTotal,DevTipo,DevConsumo;
    public static JTree treeBuilding;
    public static JLabel Teste;
    public static TimeSeries series = new TimeSeries("Total Kw Consumidos",Minute.class);

    public mainForm() {
        // passa objectos por referencia
        Equipamentos = pbEquipamentos;
        Teste = lblTeste;
        treeBuilding = this.treeBuildings;
        LeiturasTotal = this.lblLeiturasTotal;
        TotalKw = this.lblTotalKw;
        CargaTotal = this.lblCargaTotalData;
        ActiveDevicesTotal = this.lblActiveDevicesTotal;
        DevTipo = lblDevTipo;
        DevConsumo = lblDevConsumo;

        createTree(treeBuilding); // cria tree inicial

        Images imgRed = new Images("images/red.png");
        lblSemaforo.setIcon(imgRed.resize(20,20));

        Images imgDev = new Images("images/equip.png");
        lblDevPic.setIcon(imgDev.resize(80,80));

        Images imgBuild = new Images("images/build.png");
        lblBuildPic.setIcon(imgBuild.resize(80,80));


        lblActiveDevicesTotal.setText(Devices.getActiveDevices().toString() + "/" + Devices.getDevices().toString());
        pbEquipamentos.setMaximum(Devices.getDevices());

        displayTimer(true); // efetua o display das horas

        TimeSeriesCollection dataset = new TimeSeriesCollection(series); //inicia um dataset para receber series

        JFreeChart chart = ChartFactory.createTimeSeriesChart("Consumo","Minutos","Kw",dataset,true,true,false);
        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setDomainZoomable(true);
        chartpanel.setPreferredSize(new Dimension(200, 270));

        // carrega chartpanel para o painel final
        pGrafico.setLayout(new BorderLayout());
        pGrafico.add(chartpanel, BorderLayout.NORTH);

        /**
         * Metodo que associa um action listener ao botao ligar
         */
        btnLigar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cDevicesCounter = new Counter("");
                cDevicesCounter.start();

                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");

                lblStatusBarData.setText("Em funcionamento...Iniciado em: " + dt.format(cDevicesCounter.getDataInicial()));

                Images imgGreen = new Images("images/green.png");
                lblSemaforo.setIcon(imgGreen.resize(20,20));

                lblSimStatus.setText("activo");
                btnLigar.setEnabled(false);
                btnDesligar.setEnabled(true);

                pbSimulatorStatus.setIndeterminate(true);

                expandAllNodes(treeBuildings, 0, treeBuildings.getRowCount());
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

                // TODO Metodo que faça update dos valores no gui
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
                lblStatusBarData.setText("Desligado...Iniciado em: " + dt.format(cDevicesCounter.getDataInicial()) + " Terminado em: " + dt.format(cDevicesCounter.getDataFinal()));

                lblSemaforo.setIcon(imgRed.resize(20,20));
                series.clear(); // limpa as series do grafico
                createTree(treeBuilding);

                lblLeiturasTotal.setText("0");
                lblTotalKw.setText("0 Kw");
                lblCargaTotalData.setText("0 Kw");
                lblSimStatus.setText("inactivo");
                btnLigar.setEnabled(true);
                btnDesligar.setEnabled(false);
                pbSimulatorStatus.setIndeterminate(false);

            }
        });

        /**
         * Metodo que associa Mouse Listener ao botao expand
         */
        btnExpandTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                expandAllNodes(treeBuildings, 0, treeBuildings.getRowCount());
            }
        });

        /**
         * Metodo que associa Mouse Listener ao botao colapse
         */
        btnColapseTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                colapseAllNodes(treeBuildings, 1, treeBuildings.getRowCount());
            }
        });
    }

    /**
     * Metodo que liga/desliga o mostrador do relogio
     * @param estado Boolean a definir o estado
     */
    private void displayTimer(Boolean estado){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        if(estado){
            ActionListener timerListener = new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    lblTimer.setText(timeFormat.format(new Date()));
                }
            };

            Timer timer = new Timer(1000, timerListener);

            // altera delay para nao ficar 1 seg à espera de iniciar
            timer.setInitialDelay(0);
            timer.start();

        } else {
            lblTimer.setText(timeFormat.format(new Date()));
        }

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

                // adiciona window listener ao frame
                buildFrame.addWindowListener(new WindowAdapter(){
                    // Evento window closed
                    public void windowClosed(WindowEvent e){
                        createTree(treeBuilding);
                    }

                    /*// Evento window closing
                    public void windowClosing(WindowEvent e){}*/
                });

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
                apartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // adiciona window listener ao frame
                apartFrame.addWindowListener(new WindowAdapter(){
                    // Evento window closed
                    public void windowClosed(WindowEvent e){
                        createTree(treeBuilding);
                    }

                    /*// Evento window closing
                    public void windowClosing(WindowEvent e){}*/
                });

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
                equipFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // adiciona window listener ao frame
                equipFrame.addWindowListener(new WindowAdapter(){
                     // Evento window closed
                    public void windowClosed(WindowEvent e){
                        createTree(treeBuilding);
                    }

                    // Evento window closing
                    /*public void windowClosing(WindowEvent e){}*/
                });

                equipFrame.setResizable(false);
                equipFrame.setPreferredSize(new Dimension(600, 400));

                equipFrame.pack();
                equipFrame.setLocationRelativeTo(null);

                equipFrame.setVisible(true);
            }
        });

        tabelas.add(equipamentosItem);

        // Ajuda
        JMenu ajuda = new JMenu("Ajuda");
        ajuda.setMnemonic(KeyEvent.VK_A);

        // Ajuda->Sobre
        JMenuItem sobreItem = new JMenuItem("Sobre", icon);
        sobreItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,"Autores: Pedro Roldan 21501217\nLeandro Moreira 21601599\nVersão: 0.1 alpha\nISMAT POO 2016/2017",
                        "Sobre iPower...",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        ajuda.add(sobreItem);

        // adiciona os menus a menubar
        menubar.add(file);
        menubar.add(tabelas);
        //menubar.add(Box.createHorizontalGlue()); // horizontal glue
        menubar.add(ajuda);

        frame.setJMenuBar(menubar);
    }

    /**
     * Metodo que actualiza o estado de cada equipamento na tree
     * @param deviceId String com id do equipamento
     * @param status Boolean Estado a actualizar
     */
    public static void setEquipmentTreeStatus(String deviceId, Boolean status){
        //String deviceId = "100010001000";
        String BuildingId = deviceId.substring(0,4);
        String ApartmentId = deviceId.substring(4,8);
        String EquipId = deviceId.substring(8,12);

        DefaultTreeModel model = (DefaultTreeModel) treeBuilding.getModel();
        TreeNode rootNode  = (TreeNode) treeBuilding.getModel().getRoot();
        TreePath path = new TreePath(rootNode);

        for(int i=0; i<rootNode.getChildCount(); i++) { // todos os nos
            TreeNode nodeEdificios = rootNode.getChildAt(i);

            if (nodeEdificios.toString().substring(0,4).equals(BuildingId)){ // valida edificio

                for(int a=0; a<nodeEdificios.getChildCount(); a++) {
                    TreeNode nodeApartamentos = nodeEdificios.getChildAt(a);

                    if (nodeApartamentos.toString().substring(0,4).equals(ApartmentId)){ // valida apartamento

                        if(nodeApartamentos.getChildCount()>0){

                            for(int d=0; d<nodeApartamentos.getChildCount(); d++) {
                                DefaultMutableTreeNode nodeDevices = (DefaultMutableTreeNode) nodeApartamentos.getChildAt(d);

                                if (nodeDevices.toString().substring(0,4).equals(EquipId)) {
                                    if (status){
                                        nodeDevices.setUserObject(EquipId + " - " + "[On]");
                                    } else {
                                        nodeDevices.setUserObject(EquipId + " - " + "[Off]");
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        treeBuilding.setModel(new DefaultTreeModel(rootNode));
        expandAllNodes(treeBuilding,0,treeBuilding.getRowCount());
    }

    /**
     * Metodo que cria a tree com edificios, apartamentos e equipamentos
     * @param treeBuildings Objecto Jtree a ser criado
     */
    public static void createTree(JTree treeBuildings){

        // Cria cada edificio
        ArrayList arrBuildingsList = Buildings.getBuildingsList();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Edificios");
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) treeBuildings.getCellRenderer();

        for (int i = 0; i < arrBuildingsList.size(); i++) {
            String strBuilding = (String) arrBuildingsList.get(i);
            String[] arrBuilding = strBuilding.split("#");

            DefaultMutableTreeNode buildingNode = new DefaultMutableTreeNode(arrBuilding[0] + " - " + arrBuilding[1]);


            // Cria cada apartamento
            ArrayList arrApartmentsList = Apartments.getApartmentList(Integer.valueOf(arrBuilding[0]));

            for (int b = 0; b < arrApartmentsList.size(); b++) {
                String strApartment = (String) arrApartmentsList.get(b);
                String[] arrApartment = strApartment.split("#");

                DefaultMutableTreeNode apartmentNode = new DefaultMutableTreeNode(arrApartment[0] + " - " + arrApartment[1]);
                buildingNode.add(apartmentNode);

                // Cria os equipamentos de cada apartamento
                ArrayList arrDevicesList = Devices.getDevicesList(Integer.valueOf(arrBuilding[0]), Integer.valueOf(arrApartment[0]));



                for (int d = 0; d < arrDevicesList.size(); d++) {
                    String strDevice = (String) arrDevicesList.get(d);
                    String[] arrDevice = strDevice.split("#");

                    if(!Boolean.valueOf(arrDevice[3])){
                        apartmentNode.add(new DefaultMutableTreeNode(arrDevice[0] + " - [Off]"));
                    } else {
                        apartmentNode.add(new DefaultMutableTreeNode(arrDevice[0]));
                    }


                }
            }

            // altera imagens default da tree
            Images imgBuild = new Images("images/build.png");
            Icon icon = imgBuild.resize(20,20);
            renderer.setClosedIcon(icon);
            renderer.setOpenIcon(icon);

            Images imgEquip = new Images("images/equip.png");
            Icon icone = imgEquip.resize(20,20);
            renderer.setLeafIcon(icone);

            root.add(buildingNode);

        }

        treeBuildings.setModel(new DefaultTreeModel(root));

        // adiciona mouse listener à tree para capturar o click do rato
        treeBuildings.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                getTreeData(me);
            }
        });
    }

    /**
     * Metodo que resolve a localizacao na tree atraves do click do rato
     * @param me Evento click do rato
     */
    static void getTreeData(MouseEvent me) {
        TreePath tp = treeBuilding.getPathForLocation(me.getX(), me.getY());
        // TODO Terminar o algoritmo que tras a informacao da tree e coloca os dados relevantes no painel de informacao
        if (tp != null) {

            String[] dados = tp.toString().split(",");
            Integer buildid = Integer.valueOf(dados[1].trim().substring(0,4));
            Integer aptid = Integer.valueOf(dados[2].trim().substring(0,4));
            Integer devid = Integer.valueOf(dados[3].trim().substring(0,4));

            Teste.setText(dados[1]);
            Devices equipamento = Devices.loadDevice(buildid, aptid, devid);
            DevTipo.setText(equipamento.getDeviceType());
            DevConsumo.setText(String.valueOf(equipamento.getConsumo()) + " W");
        } else {
            Teste.setText("");
        }
    }

    /**
     * Metodo que faz o expand de todos os nos de uma arvore
     * @param tree Arvore a expandir
     * @param startIndex Numero inicial da tree
     * @param rowCount Numero total de linhas
     */
    private static void expandAllNodes(JTree tree, int startIndex, int rowCount){
        for(int i=startIndex;i<rowCount;++i){
            tree.expandRow(i);
        }

        if(tree.getRowCount()!=rowCount){
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    /**
     * Metodo que faz o colapse de todos os nos de uma arvore
     * @param tree Arvore a fechar
     * @param startIndex Numero inicial da tree
     * @param rowCount Numero total de linhas
     */
    private void colapseAllNodes(JTree tree, int startIndex, int rowCount){
        for(int i=startIndex;i<rowCount;++i){
            tree.collapseRow(i);
        }

        if(tree.getRowCount()!=rowCount){
            colapseAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    /**
     * Inicializa o gui
     */
    public static void createGUI() {
        JFrame mainFrame = new JFrame("iPower");

        mainFrame.setContentPane(new mainForm().mainFrame); // carrega o main panel feito no gui
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setResizable(false);
        mainFrame.setPreferredSize(new Dimension(1000, 600));
        createMenuBar(mainFrame);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
