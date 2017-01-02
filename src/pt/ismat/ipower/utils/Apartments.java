package pt.ismat.ipower.utils;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Pedro Roldan on 02-01-2017.
 * @version 0.0
 */
public class Apartments {

    /**
     * Metodo que retorna uma lista de apartamentos existentes de um edificio pelo seu id
     * @return Apartments List
     */
    public static ArrayList getApartmentList(Integer buildingId){
        ArrayList arrApartmentsList = new ArrayList();

        //validateBuildingsFolder();
        xmlParser.readApartmentsXml(Buildings.strBuildingsXml,arrApartmentsList,buildingId);

        return arrApartmentsList;
    }
}
