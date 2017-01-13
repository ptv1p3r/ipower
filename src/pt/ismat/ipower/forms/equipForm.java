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
        lablesOn();


        /**
         * Metodo que adiciona item listener a combo de escolha de edificios para filtro
         * Item listener porque e a unica forma de captar o click do rato atraves do value change da combo
         */
        cbBuildings.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                apt = cbBuildings.getSelectedItem().toString().split(" - ");

                lstDevicesModel.clear();
                //lsDevices.setModel(lstDevicesModel);
                setApartmentsList(Integer.valueOf(apt[0]));

                setDeviceList(Integer.valueOf(apt[0]), cbApartments.getSelectedIndex()+1000);

                setGuiElementsOff();
                lablesOn();
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

                setDeviceList(Integer.valueOf(selectedBuilding[0]), Integer.valueOf(selectedApartment[0]));

                setGuiElementsOff();
                lablesOn();
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

                    String[] strApartmentId = cbApartments.getSelectedItem().toString().split(" - ");
                    Integer apartmentId = Integer.valueOf(strApartmentId[0]);

                    // vamos buscar o elemento pelo seu index
                    Object selectedDevice = lsDevices.getModel().getElementAt(index);
                    String[] arrDevices = selectedDevice.toString().split(" - ");

                    Devices Device = Devices.loadDevice(buildingId,apartmentId,Integer.valueOf(arrDevices[0].trim()));

                    //mete a info nas lables
                    lblIdData.setText(Device.getDeviceId().toString());
                    txtConsumo.setText(String.valueOf(Device.getConsumo()));
                    cbTipo.setSelectedItem(Device.getDeviceCategory());
                    cbDeviceType.setSelectedItem(Device.getDeviceType());
                    ckbEnable.setSelected(Device.isEnabled());

                    //tranca as lables
                    lablesOff();

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

                //try catch para ver se o consumo e um numero
                try {

                    if (cbTipo.getSelectedItem().toString() != "- nenhum -" && cbDeviceType.getSelectedItem().toString() != "- nenhum -" &&
                            (txtConsumo.getText() != "- nenhum -" || txtConsumo.getText().length()!=0)) {

                        Integer apartmentId = Integer.parseInt(cbApartments.getSelectedItem().toString().replaceAll("\\D+", ""));
                        Integer buildingId = Integer.parseInt(cbBuildings.getSelectedItem().toString().replaceAll("\\D+", ""));

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
                                "Os campos [Tipo] e [Categoria] não podem ser - nenhum -.",
                                "iPower - Criação de Equipamento",
                                JOptionPane.WARNING_MESSAGE);
                    }

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "O campo [Consumo] tem de ser expresso em Watts.",
                            "iPower - Criação de Equipamento",
                            JOptionPane.WARNING_MESSAGE);
                }



            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!lblIdData.isEnabled() && !txtConsumo.isEnabled() && !cbTipo.isEnabled() && !cbDeviceType.isEnabled() && !ckbEnable.isEnabled()){
                    //enable as lables
                    lablesOn();

                } else {

                    //try catch para ver se o consumo e um numero
                    try {

                        //verifica as lables
                        if (cbTipo.getSelectedItem().toString() != "- nenhum -" && cbDeviceType.getSelectedItem().toString() != "- nenhum -" &&
                                (txtConsumo.getText() != "- nenhum -" || txtConsumo.getText().length()!=0)) {

                            int resultado = JOptionPane.showConfirmDialog(
                                    mainFrame,
                                    "Deseja mesmo editar este equipamento?",
                                    "iPower - Edição de Equipamento",
                                    JOptionPane.YES_NO_OPTION);


                            //efetua a edicao
                            if (resultado == 0) {

                                //building e apartment id
                                Integer apartmentId = Integer.parseInt(cbApartments.getSelectedItem().toString().replaceAll("\\D+", ""));
                                Integer buildingId = Integer.parseInt(cbBuildings.getSelectedItem().toString().replaceAll("\\D+", ""));

                                //cria um novo apartamento com o nome novo, ignora os espacos da indentacao da lable.
                                Devices device = new Devices(buildingId, apartmentId, Integer.parseInt(lblIdData.getText()), Integer.parseInt(txtConsumo.getText().trim())
                                        , cbTipo.getSelectedItem().toString(), cbDeviceType.getSelectedItem().toString(), ckbEnable.isSelected());

                                Devices.editDevice(buildingId, apartmentId, device);

                                lstDevicesModel.clear();

                                //volta a preencher a lstApartmentModel com os apartamentos do edificio da combobox atualizados
                                setDeviceList(buildingId, apartmentId);

                                //preenche as casas com a nova informacao do equipamento, como nao muda o ID nao e preciso mexer no id

                                txtConsumo.setText(String.valueOf(device.getConsumo()));
                                cbTipo.setSelectedItem(device.getDeviceCategory());
                                cbDeviceType.setSelectedItem(device.getDeviceType());
                                ckbEnable.setSelected(device.isEnabled());

                                //tranca as lables
                                lablesOff();
                            }
                        } else {
                            JOptionPane.showMessageDialog(mainFrame,
                                    "Os campos [Tipo] e [Categoria] não podem ser - nenhum -.",
                                    "iPower - Criação de Equipamento",
                                    JOptionPane.WARNING_MESSAGE);
                        }

                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(mainFrame,
                                "O campo [Consumo] tem de ser um numero inteiro..",
                                "iPower - Criação de Equipamento",
                                JOptionPane.WARNING_MESSAGE);

                    } catch (NullPointerException npe) {
                        JOptionPane.showMessageDialog(mainFrame,
                                "O campo [Consumo] tem de ser um numero inteiro.",
                                "iPower - Criação de Equipamento",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }

            }
        });

        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!lsDevices.isSelectionEmpty()){

                    int resultado = JOptionPane.showConfirmDialog(
                            mainFrame,
                            "Deseja mesmo remover este equipamento assim como todos os seus dados?",
                            "iPower - Remoção de Equipamento",
                            JOptionPane.YES_NO_OPTION);

                    if (resultado==0){

                        //building e apartment id
                        Integer apartmentId = Integer.parseInt(cbApartments.getSelectedItem().toString().replaceAll("\\D+", ""));
                        Integer buildingId = Integer.parseInt(cbBuildings.getSelectedItem().toString().replaceAll("\\D+", ""));

                        Devices.removeDevice(buildingId,apartmentId, Integer.parseInt(lblIdData.getText())); // remove o equipamento
                        lstDevicesModel.remove(lsDevices.getSelectedIndex()); // remove do model da jlist
                        lsDevices.setModel(lstDevicesModel); // actualiza jlist com model

                        setGuiElementsOff();
                        lablesOn();

                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Nenhum equipamento seleccionado.",
                            "iPower - Remoção de Edificio",
                            JOptionPane.WARNING_MESSAGE);
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

    private void lablesOn() {

        //enable as lables
        lblIdData.setEnabled(true);
        txtConsumo.setEnabled(true);
        cbTipo.setEnabled(true);
        cbDeviceType.setEnabled(true);
        ckbEnable.setEnabled(true);

    }

    private void lablesOff() {

        //disable as lables
        lblIdData.setEnabled(false);
        txtConsumo.setEnabled(false);
        cbTipo.setEnabled(false);
        cbDeviceType.setEnabled(false);
        ckbEnable.setEnabled(false);

    }

}
