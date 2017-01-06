package pt.ismat.ipower;
import pt.ismat.ipower.forms.mainForm;

import javax.swing.*;
import javax.swing.UIManager.*;

/**
 * Created by v1p3r on 29-12-2016.
 */
public class iPower {

    public static void main(String[] args){
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                // Altera o look and feel se disponivel
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                } else {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                }

            }
        } catch (Exception e) {

        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainForm.createGUI();
            }
        });

    }
}
