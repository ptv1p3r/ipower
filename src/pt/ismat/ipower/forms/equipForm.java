package pt.ismat.ipower.forms;

import pt.ismat.ipower.utils.Apartments;
import pt.ismat.ipower.utils.Buildings;
import pt.ismat.ipower.utils.Devices;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringJoiner;

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
        apt = cbBuildings.getSelectedItem().toString().split(" - ");
        setApartmentsList(Integer.valueOf(apt[0]));
        String[] selectedItem = cbApartments.getSelectedItem().toString().split(" - ");
        setDeviceList(Integer.valueOf(apt[0]), Integer.valueOf(selectedItem[0]));



        /**
         * Metodo que adiciona item listener a combo de escolha de edificios para filtro
         * Item listener porque e a unica forma de captar o click do rato atraves do value change da combo
         */
        cbBuildings.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                apt = cbBuildings.getSelectedItem().toString().split(" - ");

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

                String[] selectedBuilding = cbBuildings.getSelectedItem().toString().split(" - ");
                String[] selectedApartment = cbApartments.getSelectedItem().toString().split(" - ");

                ArrayList arrDevicesList = Devices.getDevicesList(Integer.parseInt(selectedBuilding[0]), Integer.parseInt(selectedApartment[0]));

                for (int i = 0; i < arrDevicesList.size(); i++) {
                    String strBuilding = (String) arrDevicesList.get(i);
                    String[] arrBuilding = strBuilding.split("#");
                    lstDevicesModel.addElement(arrBuilding[0]);
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

                    String[] strBuildingId = cbBuildings.getSelectedItem().toString().split(" - ");
                    Integer buildingId = Integer.valueOf(strBuildingId[0]);

                    String[] strApartmentId = cbBuildings.getSelectedItem().toString().split(" - ");
                    Integer apartmentId = Integer.valueOf(strApartmentId[0]);

                    // vamos buscar o elemento pelo seu index
                    Object selectedDevice = lsDevices.getModel().getElementAt(index);
                    String[] arrDevices = selectedDevice.toString().split(" - ");

                    Devices Device = Devices.loadDevice(buildingId,apartmentId,Integer.valueOf(arrDevices[0].trim()));

                    lblIdData.setText(Device.getDeviceId().toString());
                    txtConsumo.setText(String.valueOf(Device.getConsumo()));
                    cbTipo.setSelectedItem(Device.getDeviceCategory());
                    cbDeviceType.setSelectedItem(Device.getDeviceType());
                    ckbEnable.setSelected(Device.isEnabled());

                } else {
                    setGuiElementsOff();
                    ckbEnable.setSelected(false);
                }

            }
        });

        //Testing, not ready
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (cbTipo.getSelectedItem().toString() != "- nenhum -" && cbDeviceType.getSelectedItem().toString() != "- nenhum -" &&
                        (txtConsumo.getText() != "- nenhum -" || txtConsumo.getText().length()!=0)) {

                    Integer apartmentId = Integer.parseInt(cbApartments.getSelectedItem().toString().replaceAll("\\D+", ""));
                    Integer buildingId = Integer.parseInt(cbApartments.getSelectedItem().toString().replaceAll("\\D+", ""));

                    Integer consumo = Integer.parseInt(txtConsumo.getText().trim());

                    //mudar a recepcao do id para como o dos outros.
                    Devices device = new Devices(buildingId, apartmentId, consumo, cbTipo.getSelectedItem().toString(),
                            cbDeviceType.getSelectedItem().toString(), ckbEnable.isSelected());


                    ArrayList arrDevicesList = Devices.getDevicesList(buildingId, apartmentId);

                    lstDevicesModel.clear();

                    //volta a preencher a lstDevicesModel com os apartamentos do edificio da combobox
                    for (int i = 0; i < arrDevicesList.size(); i++) {
                        String strDevices = (String) arrDevicesList.get(i);
                        String[] arrDevices = strDevices.split("#");
                        lstDevicesModel.addElement(arrDevices[0]);
                    }

                    //adiciona o novo elemento a lista, e cria o respetivo xml e atualiza a lista
                    lstDevicesModel.addElement(device.getDeviceId());
                    Devices.saveDevice(buildingId, apartmentId, device);
                    lsDevices.setModel(lstDevicesModel);

                    setGuiElementsOff();

                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "O campo [Nome] não pode ser vazio.",
                            "iPower - Criação de Apartamento",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
            cbBuildingsModel.addElement(arrBuilding[0] + " - " + arrBuilding[1]);
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
            cbApartmentModel.addElement(arrApartment[0] + " - " + arrApartment[1]);
        }
        cbApartments.setModel(cbApartmentModel);

    }

    private void setDeviceList(Integer buildingId, Integer apartmentId){

        ArrayList arrDevicesList = Devices.getDevicesList(buildingId, apartmentId);

        lstDevicesModel.clear();

        for (int i = 0; i < arrDevicesList.size(); i++) {
            String strDevice = (String) arrDevicesList.get(i);
            String[] arrDevice = strDevice.split("#");
            lstDevicesModel.addElement(arrDevice[0]);
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
