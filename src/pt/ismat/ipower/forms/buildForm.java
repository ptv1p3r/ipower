package pt.ismat.ipower.forms;

import javax.swing.*;
import pt.ismat.ipower.utils.Buildings;

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

    public buildForm() {
        DefaultListModel lstBuildingsModel = new DefaultListModel();

        ArrayList arrBuildingsList = Buildings.getBuildingsList();

        for (int i = 0; i < arrBuildingsList.size(); i++) {
            String s = (String) arrBuildingsList.get(i);
            lstBuildingsModel.addElement(s);
        }



        Buildings Edificio1 = new Buildings("teste","portimao");
        Buildings Edificio2 = new Buildings("teste2","lagos");
        Buildings Edificio3 = new Buildings("teste3","lagos");
        //lstBuildingsModel.addElement(Edificio1.getName() + " - " + Edificio1.getBuildingId());
        //lstBuildingsModel.addElement(Edificio2.getName() + " - " + Edificio2.getBuildingId());
        lstBuildings.setModel(lstBuildingsModel);

        //lstBuildings = new JList(lstBuildingsModel);
        //lstBuildings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //lstBuildings.getSelectedIndex(-1);
        //lstBuildings.setVisibleRowCount(5);
        //JScrollPane lstBuildingsScrollPane = new JScrollPane(lstBuildings);

        //leftFrame.add(lstBuildingsScrollPane, BorderLayout.CENTER);
    }
}
