package pt.ismat.ipower.forms;

import pt.ismat.ipower.utils.Apartments;
import pt.ismat.ipower.utils.Buildings;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;


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
    private JButton btnCarregar;
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

        //inicializa a list do primeiro edificio na combobox
        DefaultListModel lstApartmentsModel = new DefaultListModel();

        ArrayList arrApartmentsList = Apartments.getApartmentList(cbBuildings.getSelectedIndex()+1000);

        for (int i = 0; i < arrApartmentsList.size(); i++) {
            String strApartment = (String) arrApartmentsList.get(i);
            String[] arrApartment = strApartment.split("#");
            lstApartmentsModel.addElement(arrApartment[0] + " - " + arrApartment[1]);
        }

        lstApartments.setModel(lstApartmentsModel);

        /**
         * Action Listener do botao de adicionar apartamento
         */
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(txtName.getText().length()!=0){
                    //cria um novo apartamento com a informacao inserida
                    Apartments Apartment = new Apartments(cbBuildings.getSelectedIndex(),txtName.getText().trim());

                    //recebe a lista de apartamentos do edificio selecionado
                    ArrayList arrApartmentsList = Apartments.getApartmentList(cbBuildings.getSelectedIndex()+1000);

                    lstApartmentsModel.clear(); //limpa a lstApartmentModel de modo a que fique limpa do default

                    //volta a preencher a lstApartmentModel com os apartamentos do edificio da combobox
                    for (int i = 0; i < arrApartmentsList.size(); i++) {
                        String strApartment = (String) arrApartmentsList.get(i);
                        String[] arrApartment = strApartment.split("#");
                        lstApartmentsModel.addElement(arrApartment[0] + " - " + arrApartment[1]);
                    }

                    //adiciona o novo elemento a lista, e cria o respetivo xml e atualiza a lista
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
                            "Nenhum apartamento seleccionado.",
                            "iPower - Remoção de Edificio",
                            JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        /**
         * Action Listener do botao editar apartamento
         */
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //caso esteja trancado, ativa
                if (!txtName.isEnabled()) {
                    txtName.setEnabled(true);

                } else { //le o que tem a fazer e tranca as casas

                    //verifica se foi selecionado algum apartamento
                    if (lblIdData.getText() == "- nenhum -") {

                        JOptionPane.showMessageDialog(mainFrame,
                                "Nenhum apartamento seleccionado.",
                                "iPower - Edição de Apartamento",
                                JOptionPane.WARNING_MESSAGE);

                    } else {

                        txtName.setEnabled(false);

                        //janela para confirmacao da edicao
                        int resultado = JOptionPane.showConfirmDialog(
                                mainFrame,
                                "Deseja mesmo editar este apartamento?",
                                "iPower - Edição de Apartamento",
                                JOptionPane.YES_NO_OPTION);

                        //efetua a edicao
                        if (resultado == 0) {

                            //cria um novo apartamento com o nome novo, ignora os espacos da indentacao da lable.
                            Apartments Apartment = new Apartments(cbBuildings.getSelectedIndex() + 1000, Integer.parseInt(lblIdData.getText().trim()),
                                    txtName.getText());

                            //edita o xml do apartamento
                            Apartment.editApartment(Apartment.getBuildingId(), Apartment.getApartmentId(), Apartment.getApartmentName());

                            //recebe a lista de apartamentos do edificio selecionado
                            ArrayList arrApartmentsList = Apartments.getApartmentList(cbBuildings.getSelectedIndex() + 1000);

                            //limpa a lstApartmentModel
                            lstApartmentsModel.clear();

                            //volta a preencher a lstApartmentModel com os apartamentos do edificio da combobox atualizados
                            for (int i = 0; i < arrApartmentsList.size(); i++) {
                                String strApartment = (String) arrApartmentsList.get(i);
                                String[] arrApartment = strApartment.split("#");
                                lstApartmentsModel.addElement(arrApartment[0] + " - " + arrApartment[1]);
                            }

                            lstApartments.setModel(lstApartmentsModel);

                            //preenche as casas com a nova informacao do apartamento
                            lblIdData.setText(Integer.toString(Apartment.getApartmentId()));
                            txtName.setText(Apartment.getApartmentName());
                        }

                    }
                }
            }
        });


        /**
         * Item listener da combobox com o id do edificio
         */
        cbBuildings.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                DefaultListModel lstApartmentsModel = new DefaultListModel();

                lstApartmentsModel.clear();

                //vai buscar a lista de apartamentos do edificio selecionado
                ArrayList arrApartmentsList = Apartments.getApartmentList(cbBuildings.getSelectedIndex()+1000);

                //preenche a lstApartments com os apartamentos existentes do edificio selecionado
                for (int i = 0; i < arrApartmentsList.size(); i++) {
                    String strApartment = (String) arrApartmentsList.get(i);
                    String[] arrApartment = strApartment.split("#");
                    lstApartmentsModel.addElement(arrApartment[0] + " - " + arrApartment[1]);
                }

                lstApartments.setModel(lstApartmentsModel);

                //deixa as lables vazias
                lblIdData.setText("- nenhum -");
                txtName.setText(" ");

                //caso a lable esteja trancada, destranca a mesma
                if (!txtName.isEnabled()) {
                    txtName.setEnabled(true);
                }
            }
        });

        /**
         * Mouse Listener para o evento click na lista de edificios
         */
        lstApartments.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!lstApartments.isSelectionEmpty()){
                    // vamos buscar o index pelo ponto gerado pelo mouse click
                    int index = lstApartments.locationToIndex(e.getPoint());

                    // vamos buscar o elemento pelo seu index
                    Object selectedBuilding = lstApartments.getModel().getElementAt(index);
                    String[] arrBuilding = selectedBuilding.toString().split("-");

                    //Apartments Apartamento = Apartments.loadApartment(Integer.valueOf(arrBuilding[0].trim()));

                    lblIdData.setText(arrBuilding[0]);
                    txtName.setText(arrBuilding[1]);
                } else {
                    lblIdData.setText("- nenhum -");
                    txtName.setText(" ");

                }

                //bloqueia o txtName
                txtName.setEnabled(false);
            }
        });


        /**
         * Action Listener do botao carregar apartamento
         */
        btnCarregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lblIdData.getText() == "- nenhum -") {

                    JOptionPane.showMessageDialog(mainFrame,
                            "Nenhum apartamento seleccionado.",
                            "iPower - Edição de Apartamento",
                            JOptionPane.WARNING_MESSAGE);

                } else {

                    txtName.setEnabled(false);

                    //janela para confirmacao da edicao
                    int resultado = JOptionPane.showConfirmDialog(
                            mainFrame,
                            "Deseja mesmo carregar um relatorio deste apartamento?",
                            "iPower - Carregar relatorio de Apartamento",
                            JOptionPane.YES_NO_OPTION);

                    //efetua a edicao
                    if (resultado == 0) {

                        float total;

                        //cria um novo apartamento com o nome novo, ignora os espacos da indentacao da lable.
                        Apartments Apartment = new Apartments(cbBuildings.getSelectedIndex() + 1000, Integer.parseInt(lblIdData.getText().trim()),
                                txtName.getText());

                        //edita o xml do apartamento
                        total=Apartment.apartmentBill(Apartment.getBuildingId(), Apartment.getApartmentId());

                        //JUST FOR TESTS - para mostrar o total gasto
                        JOptionPane.showConfirmDialog(
                                mainFrame,
                                String.format("Total energy used: " + total, "##.##"),
                                "iPower - Carregar relatorio de apartamento",
                                JOptionPane.YES_NO_OPTION);
                    }

                }
            }

        });
    }
}
