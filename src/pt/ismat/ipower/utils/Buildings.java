package pt.ismat.ipower.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

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
        this.strPath = setNewBuildingPath(this.intBuildingId);

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

                long lastMod = Long.MIN_VALUE;
                File selectedBuilding = null;

                // retorna a ultima pasta pela data de modificacao
                for (File newBuilding : arrBuildings) { // percorre as pastas e compara as datas de modificacao
                    if (newBuilding.lastModified() > lastMod) {
                        selectedBuilding = newBuilding;
                        lastMod = newBuilding.lastModified();
                    }
                }
                id = Integer.valueOf(selectedBuilding.getName())+1; // cria novo id para edificio
            }

        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }

        return id;
    }

    private String setNewBuildingPath(Integer id){
        String newPath = strBuildingsPath + "/" + id;

        try {
            File newBuilding = new File(newPath);

            if (!newBuilding.exists()){ // valida se o edificio existe
                newBuilding.mkdir();

            }

        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }

        return newPath;
    }

    public static ArrayList getBuildingsList(){
        ArrayList arrBuildingsList = new ArrayList();

        validateBuildingsFolder();

        arrBuildingsList.add(strBuildingsPath);
        arrBuildingsList.add("A");
        arrBuildingsList.add("E");
        arrBuildingsList.add("B");
        arrBuildingsList.add("D");
        arrBuildingsList.add("F");

        return arrBuildingsList;
    }

    private static void validateBuildingsFolder(){

        try {
            File buildingsFolder = new File(strBuildingsPath);

            if (!buildingsFolder.exists()){ // valida se a pasta buildings existe e cria
                buildingsFolder.mkdir();
            }

        } catch (Exception ex) {

        }

    }
}
