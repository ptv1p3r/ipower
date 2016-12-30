package pt.ismat.ipower.forms;

import javax.swing.*;

/**
 * Created by v1p3r on 30-12-2016.
 */
public class buildForm {
    public JPanel mainFrame;
    private JList lstBuildings;
    private JButton button1;

    public buildForm() {
        DefaultListModel lstBuildingsModel = new DefaultListModel();
        lstBuildingsModel.addElement("testet");
        lstBuildings = new JList(lstBuildingsModel);

    }

    public JList getLstBuildings() {
        return lstBuildings;
    }

    public void setLstBuildings(JList lstBuildings) {
        this.lstBuildings = lstBuildings;
    }

}
