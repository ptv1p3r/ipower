package pt.ismat.ipower.utils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Pedro Roldan on 18-01-2017.
 * @version 0.0
 */
public class Images {
    private Integer intHeight=20, intWidth=20;
    private String strPath;

    public Images(String path) {
        this.strPath = path;
    }

    /**
     * Metodo que retorna um ImageIcon redimensionado
     * @param height Int Valor da altura da imagem
     * @param width Int Valor da largura da imagem
     * @return ImageIcon ImagemIcon de retorno
     */
    public ImageIcon resize(Integer height, Integer width){

        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(strPath)); // carrega imagem como image icon
        Image imgImage = imageIcon.getImage(); // Transforma

        Image imgTemp = imgImage.getScaledInstance(height, width,  java.awt.Image.SCALE_SMOOTH); // Faz a escala com smooth
        imageIcon = new ImageIcon(imgTemp);  // Volta a transformar novamente

        return imageIcon;
    }
}
