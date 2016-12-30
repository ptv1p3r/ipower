package pt.ismat.ipower.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pedro Roldan on 30-12-2016.
 * @version 0.0
 */
public class Buildings {

    final static String strBuildingsPath = System.getProperty("user.dir") + "/buildings";
    final static String strBuildingsXml = strBuildingsPath + "/buildings.xml";

    private String strLocation, strPath, strName;
    private Integer intBuildingId;

    /**
     * Construtor de edificios
     * @param Name Nome do edificio
     * @param Location Localizacao do edificio
     */
    public Buildings(String Name, String Location) {
        this.strName = Name;
        this.strLocation = Location;
        this.intBuildingId = getNewBuildingId();
        this.strPath = strBuildingsPath + "/" + this.intBuildingId;
    }

    /**
     * Retorna o nome do edificio
     * @return String
     */
    public String getName() {
        return strName;
    }

    /**
     * Retorna o caminho para a pasta do edificio
     * @return String
     */
    public String getPath() {
        return strPath;
    }

    /**
     * Retorna a localizacao do edificio
     * @return String
     */
    public String getLocation() {
        return strLocation;
    }

    /**
     * Retorna o identificador do edificio
     * @return Integer
     */
    public Integer getBuildingId() {
        return intBuildingId;
    }

    private Integer getNewBuildingId(){
        Integer id=1000;

        try {
            File building = new File(strBuildingsPath + "/" + id);

            if (!building.exists()){ // valida se o edificio existe
                building.mkdir();
            } else {
                File buildingsFolder = new File(strBuildingsPath);
                File[] arrBuildings = buildingsFolder.listFiles();
                Arrays.sort(arrBuildings);
                id = Integer.valueOf(arrBuildings.length -1)+id; // cria novo id para edificio
            }

        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }

        return id;
    }

    /**
     * Metodo que efetua a gravacao do novo edificio criando a pasta respectiva assim como a entrada no ficheiro de edificios xml
     * @param Building Edificio a ser gravado
     */
    public static void saveBuilding(Buildings Building){
        try {
            File newBuilding = new File(Building.strPath);

            if (!newBuilding.exists()){ // valida se o edificio existe
                newBuilding.mkdir();
            }
            xmlParser.updateBuildingXml(strBuildingsXml,Building);
        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }
    }

    public static ArrayList getBuildingsList(){
        ArrayList arrBuildingsList = new ArrayList();

        validateBuildingsFolder();
        xmlParser.readBuildingXml(strBuildingsXml,arrBuildingsList);

        return arrBuildingsList;
    }

    private static void validateBuildingsFolder(){

        try {
            File buildingsFolder = new File(strBuildingsPath);

            if (!buildingsFolder.exists()){ // valida se a pasta buildings existe e cria
                buildingsFolder.mkdir();
            }

            File buildingsFile = new File(strBuildingsXml);
            if (!buildingsFile.exists()){ // valida se buildingsfile existe e cria
                xmlParser.createBuildingXml(strBuildingsXml);
            }

        } catch (Exception ex) {
            // TODO: 30-12-2016 tratar das excepcoes
            ex.printStackTrace();
        }

    }
}
