package pt.ismat.ipower.forms;

import javax.swing.*;
import pt.ismat.ipower.utils.Buildings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by v1p3r on 30-12-2016.
 */
public class buildForm {
    public JPanel mainFrame;
    private JList lstBuildings;
    private JButton btnAdicionar;
    private JPanel leftFrame;
    private JPanel rightFrame;
    private JButton btnRemover;
    private JPanel buildingInfoFrame;
    private JTextField txtName;
    private JLabel lblId;
    private JLabel lblIdData;
    private JLabel lblName;
    private JTextField textField1;
    private JLabel lblLocation;
    private JButton button1;

    public buildForm() {
        DefaultListModel lstBuildingsModel = new DefaultListModel();

        ArrayList arrBuildingsList = Buildings.getBuildingsList();

        for (int i = 0; i < arrBuildingsList.size(); i++) {
            String strBuilding = (String) arrBuildingsList.get(i);
            String[] arrBuilding = strBuilding.split("#");
            lstBuildingsModel.addElement(arrBuilding[0] + "-" + arrBuilding[1]);
        }

        lstBuildings.setModel(lstBuildingsModel);

        //lstBuildings = new JList(lstBuildingsModel);
        //lstBuildings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //lstBuildings.getSelectedIndex(-1);
        //lstBuildings.setVisibleRowCount(5);
        //JScrollPane lstBuildingsScrollPane = new JScrollPane(lstBuildings);

        //leftFrame.add(lstBuildingsScrollPane, BorderLayout.CENTER);

        /**
         * Action Listener do botao de adicionar edificio
         */
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Buildings Edificio = new Buildings("teste","portimao");

                lstBuildingsModel.addElement(Edificio.getName() + " - " + Edificio.getBuildingId());
                Buildings.saveBuilding(Edificio);
                lstBuildings.setModel(lstBuildingsModel);

            }
        });

        /**
         * Action Listener do botao remover edificio
         */
        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBuilding = lstBuildings.getSelectedValue().toString();
                System.out.println("on: " + selectedBuilding);
            }
        });

        /**
         * Mouse Listener para o evento click na lista de edificios
         */
        lstBuildings.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // vamos buscar o index pelo ponto gerado pelo mouse click
                int index = lstBuildings.locationToIndex(e.getPoint());
                // vamos buscar o elemento pelo seu index
                Object o = lstBuildings.getModel().getElementAt(index);

                System.out.println("Double-clicked on: " + o.toString());
            }
        });

    }
}
