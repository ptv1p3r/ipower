package pt.ismat.ipower.forms;

import pt.ismat.ipower.utils.Apartments;
import pt.ismat.ipower.utils.Buildings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pedro Roldan on 31-12-2016.
 * @version 0.0
 */
public class apartForm {
    public JPanel mainFrame;
    private JList lstApartments;
    private JPanel rightFrame;
    private JButton btnAdicionar;
    private JButton btnRemover;
    private JPanel buildingInfoFrame;
    private JLabel lblId;
    private JLabel lblIdData;
    private JTextField txtName;
    private JLabel lblName;
    private JButton button1;
    private JComboBox cbBuildings;
    private JLabel lblBuilding;
    private JPanel leftFrame;
    private JPanel topFrame;
    private JButton btnEditar;
    private String[] apt;

    public apartForm() {

//        setBuildingsList();
//        apt = cbBuildings.getSelectedItem().toString().split("-");
//        setApartmentList(Integer.valueOf(apt[0]));

        DefaultListModel lstApartmentModel = new DefaultListModel();
/*
        ArrayList arrApartmentsList = Apartments.getApartmentList(cbBuildings.setSelectedIndex(0));

        for (int i = 0; i < arrApartmentsList.size(); i++) {
            String strBuilding = (String) arrApartmentsList.get(i);
            String[] arrBuilding = strBuilding.split("#");
            lstApartmentModel.addElement(arrBuilding[0]);
        }

        lstApartments.setModel(lstApartmentModel);
/*

        /**
         * Action Listener do botao de adicionar apartamento
         */
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(txtName.getText().length()!=0 ){
                    Apartments Apartment = new Apartments(Integer.parseInt(txtName.getText().trim()));

                    lstApartmentModel.addElement(Apartment.getBuildingId() + " - " + Apartment.getName());
                    Buildings.saveBuilding(Apartment);
                    lstApartments.setModel(lstApartmentModel);

                    lblIdData.setText("- nenhum -");
                    txtName.setText("");

                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "O campo [Nome] não pode ser vazio.",
                            "iPower - Criação de Apartamento",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

//    private void setApartmentList(Integer buildingId){
//        DefaultListModel lstApartmentsModel = new DefaultListModel();
//
//        ArrayList arrApartmentsList = Apartments.getApartmentList(buildingId);
//
//        for (int i = 0; i < arrApartmentsList.size(); i++) {
//            String strBuilding = (String) arrApartmentsList.get(i);
//            String[] arrBuilding = strBuilding.split("#");
//            lstApartmentsModel.addElement(arrBuilding[0] + "-" + arrBuilding[1]);
//        }
//
//        lstApartments.setModel(lstApartmentsModel);
//    }

    /**
     * Metodo que constroi a combo box com uma lista de edificios existentes
     */
    private void setBuildingsList(){
        final DefaultComboBoxModel cbBuildingsModel = new DefaultComboBoxModel();

        ArrayList arrBuildingsList = Buildings.getBuildingsList();
        for (int i = 0; i < arrBuildingsList.size(); i++) {
            String strBuilding = (String) arrBuildingsList.get(i);
            String[] arrBuilding = strBuilding.split("#");
            cbBuildingsModel.addElement(arrBuilding[0] + "-" + arrBuilding[1]);
        }

        cbBuildings.setModel(cbBuildingsModel);
        cbBuildings.setSelectedIndex(0);
    }

}
