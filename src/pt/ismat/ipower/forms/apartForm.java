package pt.ismat.ipower.forms;

import pt.ismat.ipower.utils.Apartments;
import pt.ismat.ipower.utils.Buildings;
import pt.ismat.ipower.utils.Devices;

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
    private JComboBox cbBuildings;
    private JLabel lblBuilding;
    private JPanel leftFrame;
    private JPanel topFrame;
    private JButton btnEditar;
    private String[] apt;

    public apartForm() {

        //inicia a cbBuildings com os ids dos edificios
        DefaultListModel lstBuildigModel = new DefaultListModel();

        ArrayList arrBuildingsList = Buildings.getBuildingsList();

        for (int i = 0; i < arrBuildingsList.size(); i++) {
            String strBuilding = (String) arrBuildingsList.get(i);
            String[] arrBuilding = strBuilding.split("#");
            lstBuildigModel.addElement(arrBuilding[0]);
            cbBuildings.addItem(arrBuilding[0]);
        }

        //
        DefaultListModel lstApartmentsModel = new DefaultListModel();

        ArrayList arrApartmentsList = Apartments.getApartmentList(cbBuildings.getSelectedIndex()+1000);

        for (int i = 0; i < arrApartmentsList.size(); i++) {
            String strApartment = (String) arrApartmentsList.get(i);
            String[] arrApartment = strApartment.split("#");
            lstApartmentsModel.addElement(arrApartment[0]);
        }

        lstApartments.setModel(lstApartmentsModel);

        /**
         * Action Listener do botao de adicionar apartamento
         */
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(txtName.getText().length()!=0){
                    Apartments Apartment = new Apartments(cbBuildings.getSelectedIndex(),txtName.getText().trim());

                    Apartment.setBuildingId(cbBuildings.getSelectedIndex());
                    lstApartmentsModel.addElement(Apartment.getApartmentId() + " - " + Apartment.getApartmentName());
                    Apartment.saveApartment(Apartment);
                    lstApartments.setModel(lstApartmentsModel);

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

        /**
         * Action Listener do botao remover apartamento
         */
        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!lstApartments.isSelectionEmpty()){
                    String selectedApartment = lstApartments.getSelectedValue().toString();

                    int resultado = JOptionPane.showConfirmDialog(
                            mainFrame,
                            "Deseja mesmo remover este apartamento assim como todos os seus dados?",
                            "iPower - Remoção de Apartamento",
                            JOptionPane.YES_NO_OPTION);

                    if (resultado==0){
                        String[] arrApartment = selectedApartment.split("-");

                        Apartments.removeApartment(cbBuildings.getSelectedIndex()+1000,Integer.valueOf(arrApartment[0].trim())); // remove pasta e entrada do xml buildings
                        lstApartmentsModel.remove(lstApartments.getSelectedIndex()); // remove do model da jlist
                        lstApartments.setModel(lstApartmentsModel); // actualiza jlist com model

                        lblIdData.setText("- nenhum -");
                        txtName.setText("");

                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Nenhum edificio seleccionado.",
                            "iPower - Remoção de Edificio",
                            JOptionPane.WARNING_MESSAGE);
                }

            }
        });


        cbBuildings.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                DefaultListModel lstApartmentsModel = new DefaultListModel();

                String[] selectedItem = cbBuildings.getSelectedItem().toString().split("");

                ArrayList arrApartmentList = Apartments.getApartmentList(cbBuildings.getSelectedIndex()+1000);

                for (int i = 0; i < arrApartmentList.size(); i++) {
                    String strBuilding = (String) arrApartmentList.get(i);
                    String[] arrBuilding = strBuilding.split("#");
                    lstApartmentsModel.addElement(arrBuilding[0] + "-" + arrBuilding[1]);
                }

                lstApartmentsModel.clear();
                setApartmentList(cbBuildings.getSelectedIndex()+1000);
                //lstApartments.setModel(lstApartmentsModel);

                lblIdData.setText("- nenhum -");

            }
        });
    }


    //TODO: os apts so sao apts bem, apenas o numero, na inicializacao. Tratar disso e faze lo ler so os apts do edificio
    private void setApartmentList(Integer buildingId){
        DefaultListModel lstApartmentsModel = new DefaultListModel();

        ArrayList arrApartmentsList = Apartments.getApartmentList(buildingId);

        for (int i = 0; i < arrApartmentsList.size(); i++) {
            String strBuilding = (String) arrApartmentsList.get(i);
            String[] arrBuilding = strBuilding.split("#");
            lstApartmentsModel.addElement(arrBuilding[0] + "-" + arrBuilding[1]);
        }

        lstApartments.setModel(lstApartmentsModel);
    }

}
