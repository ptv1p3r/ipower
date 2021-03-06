package pt.ismat.ipower.forms;

import javax.swing.*;
import pt.ismat.ipower.utils.Buildings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
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
    private JTextField txtLocation;
    private JLabel lblLocation;
    private JButton btnEditar;
    private JButton btnCarregar;

    public buildForm() {

        btnCarregar.setEnabled(false);

        //inicia a cbBuildings com os ids dos edificios
        DefaultListModel lstBuildingsModel = new DefaultListModel();

        ArrayList arrBuildingsList = Buildings.getBuildingsList();

        for (int i = 0; i < arrBuildingsList.size(); i++) {
            String strBuilding = (String) arrBuildingsList.get(i);
            String[] arrBuilding = strBuilding.split("#");
            lstBuildingsModel.addElement(arrBuilding[0] + " - " + arrBuilding[1]);
        }

        lstBuildings.setModel(lstBuildingsModel);

        /**
         * Action Listener do botao de adicionar edificio
         */
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(txtName.getText().length()!=0 && txtLocation.getText().length()!=0){
                    Buildings Edificio = new Buildings(txtName.getText().trim(),txtLocation.getText().trim());

                    lstBuildingsModel.addElement(Edificio.getBuildingId() + " - " + Edificio.getName());
                    Buildings.saveBuilding(Edificio);
                    lstBuildings.setModel(lstBuildingsModel);

                    lblIdData.setText("- nenhum -");
                    txtName.setText("");
                    txtLocation.setText("");

                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Os campos [Nome] [Localização] não podem ser vazios.",
                            "iPower - Criação de Edificio",
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

                if (!lstBuildings.isSelectionEmpty()){
                    String selectedBuilding = lstBuildings.getSelectedValue().toString();

                    int resultado = JOptionPane.showConfirmDialog(
                            mainFrame,
                            "Deseja mesmo remover este edificio assim como todos os seus dados?",
                            "iPower - Remoção de Edificio",
                            JOptionPane.YES_NO_OPTION);

                    if (resultado==0){
                        String[] arrBuilding = selectedBuilding.split(" - ");

                        Buildings.removeBuilding(Integer.valueOf(arrBuilding[0].trim())); // remove pasta e entrada do xml buildings
                        lstBuildingsModel.remove(lstBuildings.getSelectedIndex()); // remove do model da jlist
                        lstBuildings.setModel(lstBuildingsModel); // actualiza jlist com model

                        lblIdData.setText("- nenhum -");
                        txtName.setText("");
                        txtLocation.setText("");


                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Nenhum edificio seleccionado.",
                            "iPower - Remoção de Edificio",
                            JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        /**
         * Mouse Listener para o evento click na lista de edificios
         */
        lstBuildings.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!lstBuildings.isSelectionEmpty()){
                    // vamos buscar o index pelo ponto gerado pelo mouse click
                    int index = lstBuildings.locationToIndex(e.getPoint());

                    // vamos buscar o elemento pelo seu index
                    Object selectedBuilding = lstBuildings.getModel().getElementAt(index);
                    String[] arrBuilding = selectedBuilding.toString().split(" - ");

                    Buildings Edificio = Buildings.loadBuilding(Integer.valueOf(arrBuilding[0].trim()));

                    lblIdData.setText(Edificio.getBuildingId().toString());
                    txtName.setText(Edificio.getName());
                    txtLocation.setText(Edificio.getLocation());
                    btnCarregar.setEnabled(true);
                } else {
                    lblIdData.setText("- nenhum -");
                    txtName.setText("");
                    txtLocation.setText("");
                }

                //bloqueia o txtLocation e o txtName
                txtLocation.setEnabled(false);
                txtName.setEnabled(false);
            }
        });

        /**
         * Action Listener do botao editar edificio
         */
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //caso esteja trancado, ativa
                if (!txtName.isEnabled() && !txtLocation.isEnabled()) {
                    txtName.setEnabled(true);
                    txtLocation.setEnabled(true);

                } else { //le o que tem a fazer e tranca as lables

                    //verifica se foi selecionado algum apartamento
                    if (lblIdData.getText() == "- nenhum -") {

                        JOptionPane.showMessageDialog(mainFrame,
                                "Nenhum edificio seleccionado.",
                                "iPower - Edição de Edificio",
                                JOptionPane.WARNING_MESSAGE);

                    } else {

                        //janela para confirmacao da edicao
                        int resultado = JOptionPane.showConfirmDialog(
                                mainFrame,
                                "Deseja mesmo editar este edificio ?",
                                "iPower - Edição de Edificio",
                                JOptionPane.YES_NO_OPTION);

                        //efetua a edicao
                        if (resultado == 0) {
                            txtName.setEnabled(false);
                            txtLocation.setEnabled(false);

                            //cria um novo apartamento as novas informacoes, ignora os espacos da indentacao da lable.
                            Buildings Building = new Buildings(txtName.getText().trim(), txtLocation.getText().trim(), Integer.parseInt(lblIdData.getText().trim()));

                            //edita o xml do apartamento
                            Building.editBuilding(Building.getName(), Building.getLocation(), Building.getBuildingId());

                            //recebe a lista de edificios do edificio selecionado
                            ArrayList arrBuildingsList = Buildings.getBuildingsList();

                            //limpa a lstBuildingModel
                            lstBuildingsModel.clear();

                            //volta a preencher a lstApartmentModel com os apartamentos do edificio da combobox atualizados
                            for (int i = 0; i < arrBuildingsList.size(); i++) {
                                String strBuilding = (String) arrBuildingsList.get(i);
                                String[] arrBuilding = strBuilding.split("#");
                                lstBuildingsModel.addElement(arrBuilding[0] + " - " + arrBuilding[1]);
                            }

                            lstBuildings.setModel(lstBuildingsModel);

                            //preenche as casas com a nova informacao do apartamento
                            lblIdData.setText(Building.getBuildingId().toString());
                            txtName.setText(Building.getName());
                            txtLocation.setText(Building.getLocation());
                        }
                    }
                }
            }
        });

        /**
         * Action listener do botao carregar building
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
                            "iPower - Carregar relatorio de edificio",
                            JOptionPane.YES_NO_OPTION);

                    //efetua a edicao
                    if (resultado == 0) {

                        float total;

                        //cria um novo apartamento com o nome novo, ignora os espacos da indentacao da lable.
                        Buildings Building = new Buildings(Integer.parseInt(lblIdData.getText().trim()));

                        //edita o xml do apartamento
                        total=Building.buildingBill(Building.getBuildingId());

                        //JUST FOR TESTS - para mostrar o total gasto
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);

                        JOptionPane.showMessageDialog(
                                mainFrame,
                                String.format("Total de KWh usado: " + df.format(total) +
                                        "\nA pagar: " + df.format(total*0.15) + " €"),
                                "iPower - Carregar relatorio de Edificio",
                                JOptionPane.DEFAULT_OPTION);

                        btnCarregar.setEnabled(false);
                    }

                }
            }
        });
    }
}
