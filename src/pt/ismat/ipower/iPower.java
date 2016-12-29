package pt.ismat.ipower;
import pt.ismat.ipower.forms.mainForm;

/**
 * Created by v1p3r on 29-12-2016.
 */
public class iPower {

    public static void main(String[] args){

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainForm.createGUI();
            }
        });

    }
}
