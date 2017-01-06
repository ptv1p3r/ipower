package pt.ismat.ipower.forms;

import pt.ismat.ipower.utils.Apartments;
import pt.ismat.ipower.utils.Buildings;
import pt.ismat.ipower.utils.Devices;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author Pedro Roldan on 31-12-2016.
 * @version 0.0
 */
public class equipForm {
    public JPanel mainFrame;
    private JLabel lblBuilding;
    private JComboBox cbBuildings;
    private JList lsDevices;
    private JList evelsDevices;
    private JPanel rightFrame;
    private JButton btnAdicionar;
    private JButton btnRemover;
    private JPanel buildingInfoFrame;
    private JLabel lblId;
    private JLabel lblIdData;
    private JLabel lblTipo;
    private JButton btnCarregarLeituras;
    private JPanel topFrame;
    private JPanel leftFrame;
    private JLabel lblApartment;
    private JComboBox cbApartments;
    private JLabel lblConsumo;
    private JTextField txtConsumo;
    private JComboBox cbTipo;
    private JLabel lblLeituras;
    private JList lstLeituras;
    private JButton btnEditar;
    private JCheckBox ckbEnable;
    private JLabel lblType;
    private JComboBox cbDeviceType;
    private String[] apt;
    private DefaultListModel lstDevicesModel = new DefaultListModel();

    public equipForm() {
        setBuildingsList();
        apt = cbBuildings.getSelectedItem().toString().split("-");
        setApartmentsList(Integer.valueOf(apt[0]));
        String[] selectedItem = cbApartments.getSelectedItem().toString().split("-");
        setDeviceList(selectedItem[0]);




        /**
         * Metodo que adiciona item listener a combo de escolha de edificios para filtro
         * Item listener porque e a unica forma de captar o click do rato atraves do value change da combo
         */
        cbBuildings.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                apt = cbBuildings.getSelectedItem().toString().split("-");

                lstDevicesModel.clear();
                lsDevices.setModel(lstDevicesModel);
                setApartmentsList(Integer.valueOf(apt[0]));

            }
        });

        /**
         * Metodo que adiciona item listener na combo de escolha de apartamentos para filtro
         */
        cbApartments.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                DefaultListModel lstDevicesModel = new DefaultListModel();

                String[] selectedItem = cbApartments.getSelectedItem().toString().split("-");

                ArrayList arrDevicesList = Devices.getDevicesList(selectedItem[0]);

                for (int i = 0; i < arrDevicesList.size(); i++) {
                    String strBuilding = (String) arrDevicesList.get(i);
                    String[] arrBuilding = strBuilding.split("#");
                    lstDevicesModel.addElement(arrBuilding[0] + "-" + arrBuilding[1]);
                }

                lsDevices.setModel(lstDevicesModel);

                setGuiElementsOff();
            }
        });


        /**
         * Metodo que cria um mouse listener - mouse click - na lista de devices disponiveis
         */
        lsDevices.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!lsDevices.isSelectionEmpty()){
                    // vamos buscar o index pelo ponto gerado pelo mouse click
                    int index = lsDevices.locationToIndex(e.getPoint());

                    // vamos buscar o elemento pelo seu index
                    Object selectedDevice = lsDevices.getModel().getElementAt(index);
                    String[] arrDevices = selectedDevice.toString().split("-");

                    Devices Device = Devices.loadDevice(arrDevices[0].trim());

                    lblIdData.setText(Device.getDeviceId().toString());
                    txtConsumo.setText(String.valueOf(Device.getConsumo()));
                    cbTipo.setSelectedItem(Device.getDeviceCategory());
                    cbDeviceType.setSelectedIndex(Device.getDeviceType());
                    ckbEnable.setSelected(Device.isEnabled());

                } else {
                    setGuiElementsOff();
                    ckbEnable.setSelected(false);
                }

            }
        });
    }

    // TODO alterar e talvez combinar as funcoes com codigo identico
    private void setBuildingsList(){
        final DefaultComboBoxModel cbBuildingsModel = new DefaultComboBoxModel();

        ArrayList arrBuildingsList = Buildings.getBuildingsList();
        for (int i = 0; i < arrBuildingsList.size(); i++) {
            String strBuilding = (String) arrBuildingsList.get(i);
            String[] arrBuilding = strBuilding.split("#");
            cbBuildingsModel.addElement(arrBuilding[0] + "-" + arrBuilding[1]);
        }

        cbBuildings.setModel(cbBuildingsModel);
    }

    // TODO alterar e talvez combinar as funcoes com codigo identico
    private void setApartmentsList(Integer buildingId){
        final DefaultComboBoxModel cbApartmentModel = new DefaultComboBoxModel();

        ArrayList arrApartmentsList = Apartments.getApartmentList(buildingId);
        for (int i = 0; i < arrApartmentsList.size(); i++) {
            String strApartment = (String) arrApartmentsList.get(i);
            String[] arrApartment= strApartment.split("#");
            cbApartmentModel.addElement(arrApartment[0] + "-" + arrApartment[1]);
        }
        cbApartments.setModel(cbApartmentModel);

    }

    private void setDeviceList(String apartmentId){
        ArrayList arrDevicesList = Devices.getDevicesList(apartmentId);

        for (int i = 0; i < arrDevicesList.size(); i++) {
            String strBuilding = (String) arrDevicesList.get(i);
            String[] arrBuilding = strBuilding.split("#");
            lstDevicesModel.addElement(arrBuilding[0] + "-" + arrBuilding[1]);
        }

        lsDevices.setModel(lstDevicesModel);

    }

    private void setGuiElementsOff(){
        lblIdData.setText("- nenhum -");
        txtConsumo.setText("0");
        cbTipo.setSelectedItem("- nenhum -");
        cbDeviceType.setSelectedItem("- nenhum -");
    }

}
