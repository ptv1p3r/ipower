package pt.ismat.ipower.utils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pedro Roldan on 02-01-2017.
 * @version 0.0
 */
public class Apartments extends Buildings {

    final static String strBuildingPath = System.getProperty("user.dir") + "/buildings";
    final static String strBuildingsXml = strBuildingsPath + "/buildings.xml";

    private Integer intApartmentId, buildingId;
    private String strApartmentName, apartmentPath;

    public Apartments(Integer id, String strApartmentName) {
        super(id);
        setBuildingId(id);
        setIntApartmentId(getNewApartmentId());

        this.intApartmentId=getApartmentId();
        this.buildingId=getBuildingId();
        this.strApartmentName=strApartmentName;
        this.apartmentPath=strBuildingPath + "/" + (getBuildingId()+1000)+ "/" + this.intApartmentId;
    }

    @Override
    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer id) {
        this.buildingId = id+1000;
    }

    public Integer getApartmentId() {
        return intApartmentId;
    }

    public void setIntApartmentId(Integer intApartmentId) {
        this.intApartmentId = intApartmentId;
    }

    public String getApartmentName() {
        return strApartmentName;
    }

    public String getApartmentPath() {
        return apartmentPath;
    }



    /**
     * Metodo que retorna uma lista de apartamentos existentes de um edificio pelo seu id
     * @return Apartments List
     */
    public static ArrayList getApartmentList(Integer buildingId){
        ArrayList arrApartmentsList = new ArrayList();

        xmlParser.readApartmentsXml(Apartments.strBuildingsXml,arrApartmentsList,buildingId);

        return arrApartmentsList;
    }

    //TODO: Tratar de ir buscar o id atraves do xml
    /**
     * Metodo que gera um novo id disponivel
     * @return Integer Novo id gerado
     */
    private Integer getNewApartmentId(){
        Integer id=1000;
        int i;

        try {
            File apartment = new File(strBuildingPath + "/" + (getBuildingId()+1000) + "/" + id + ".xml");

            if (apartment.exists()) { //verifica se o apartamento ja tem um ficheiro xml correspondente

                File apartmentXML = new File(strBuildingPath + "/" + (getBuildingId()+1000));
                File[] arrApartments = apartmentXML.listFiles();
                Arrays.sort(arrApartments);

                //Cria um array de strings com tamanho do arrBuildings
                String[] idList = new String[arrApartments.length];

                //Transforma o conteudo do arrApartments em Strings
                for (i = 0 ; i < arrApartments.length ; i++) {
                    idList[i]=arrApartments[i].getName();
                }

                try {   //enquanto que o valor do id for igual ao elemento correspondente na string, incrementa ambos
                    for (i=0 ; i<arrApartments.length ; i++) {
                        if (id == Integer.parseInt(idList[i].substring(0, idList[i].lastIndexOf('.')))) {
                            id++;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) { //caso nao exista o valor do id na idList, este obtem o ultimo valor da lista+1
                    id=Integer.parseInt(idList[i])+1;
                }
            }

        } catch (Exception ex) {
            // TODO : validação de erros
            ex.printStackTrace();
        }

        return id;
    }

    /**
     * Metodo que efetua a gravacao do novo apartamento criando a pasta respectiva
     * @param Apartment Apartamento a ser gravado
     */
    public static void saveApartment(Apartments Apartment){
        try {
//            File newApartment = new File(Apartment.getApartmentPath()+".xml");
//
//            if (!newApartment.exists()){ // valida se o apartamento existe
//                xmlParser.updateApartmentXml(strBuildingsXml, Apartment);
//            }
            xmlParser.updateApartmentXml(strBuildingsXml, Apartment, Apartment.getBuildingId());
        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }
    }

    /**
     * Metodo que efetua a remoção do edificio do ficheiro apartamento xml assim como a sua pasta associada
     * @param apartmentId Identificador de apartamento
     * @param buildingId Identificador de edificio
     */
    public static void removeApartment(Integer buildingId, Integer apartmentId){


        try {
            File apartment = new File(strBuildingPath + "/" + buildingId + "/" + apartmentId + ".xml");

            if (apartment.exists()){ // valida se o edificio existe
                apartment.delete();
            }
            //xmlParser.removeBuildingXml(strBuildingsXml,id);
        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }
    }

}
