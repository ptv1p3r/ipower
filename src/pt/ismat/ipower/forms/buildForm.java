package pt.ismat.ipower.forms;

import javax.swing.*;
import pt.ismat.ipower.utils.Buildings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            lstBuildingsModel.addElement(arrBuilding[1]);
        }




        //Buildings Edificio2 = new Buildings("teste2","lagos");
        //Buildings Edificio3 = new Buildings("teste3","lagos");
        //lstBuildingsModel.addElement(Edificio1.getName() + " - " + Edificio1.getBuildingId());
        //lstBuildingsModel.addElement(Edificio2.getName() + " - " + Edificio2.getBuildingId());
        lstBuildings.setModel(lstBuildingsModel);

        //lstBuildings = new JList(lstBuildingsModel);
        //lstBuildings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //lstBuildings.getSelectedIndex(-1);
        //lstBuildings.setVisibleRowCount(5);
        //JScrollPane lstBuildingsScrollPane = new JScrollPane(lstBuildings);

        //leftFrame.add(lstBuildingsScrollPane, BorderLayout.CENTER);
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Buildings Edificio = new Buildings("teste","portimao");

                lstBuildingsModel.addElement(Edificio.getName() + " - " + Edificio.getBuildingId());
                Buildings.saveBuilding(Edificio);
                lstBuildings.setModel(lstBuildingsModel);

            }
        });
    }
}
