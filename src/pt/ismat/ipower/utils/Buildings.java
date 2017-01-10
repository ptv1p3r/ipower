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
     * Construtor de edificios
     * @param Name Nome do edificio
     * @param Location Localização do edificio
     * @param id Identificador do edificio
     */
    public Buildings(String Name, String Location, Integer id) {
        this.strName = Name;
        this.strLocation = Location;
        this.intBuildingId = id;
        this.strPath = strBuildingsPath + "/" + this.intBuildingId;
    }

    /**
     * Construtor de edificios para ser utilizado nos apartamentos
     * @param id Identificador do edificio
     */
    public Buildings(Integer id) {
        this.intBuildingId = id;
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

    /**
     * Metodo que gera um novo id disponivel
     * @return Integer Novo id gerado
     */
    private Integer getNewBuildingId(){
        Integer id=1000;
        int i=0;

        try {
            File building = new File(strBuildingsPath + "/" + id);

            if (!building.exists()){ // valida se o edificio existe
                building.mkdir();
            } else {

                File buildingsFolder = new File(strBuildingsPath);
                File[] arrBuildings = buildingsFolder.listFiles();
                Arrays.sort(arrBuildings);

                //Cria um array de strings com tamanho do arrBuildings
                String[] idList = new String[arrBuildings.length];

                //Transforma o conteudo do arrBuildings em Strings
                for (i = 0 ; i < arrBuildings.length ; i++) {
                    idList[i]=arrBuildings[i].getName();
                }

                try {   //enquanto que o valor do id for igual ao elemento correspondente na string, incrementa ambos
                    for (i=0 ; i<arrBuildings.length ; i++) {
                        if (id == Integer.parseInt(idList[i])) {
                            id++;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) { //caso nao exista o valor do id na idList, este obtem o ultimo valor da lista+1
                    id=Integer.parseInt(idList[i])+1;
                } catch (NumberFormatException n) {
                    //TODO: descobrir o porque deste erro, sempre que e inserido novo building, da este erro no id
                }
            }

        } catch (Exception ex) {
            // TODO : validação de erros
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

    /**
     * Metodo que efetua a remoção do edificio do ficheiro buildings xml assim como a sua pasta associada
     * @param id Identificador de edificio
     */
    public static void removeBuilding(Integer id){

        try {
            File building = new File(strBuildingsPath + "/" + id);

            if (building.exists()){ // valida se o edificio existe
                building.delete();
            }
            xmlParser.removeBuildingXml(strBuildingsXml,id);
        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }
    }

    /**
     * Metodo que retorna um edificio carregado com os seus dados
     * @param id Identificador de edificio
     * @return Edificio
     */
    public static Buildings loadBuilding(Integer id){

        return xmlParser.loadBuildingXml(strBuildingsXml,id);
    }

    /**
     * Metodo que efetua a edição do edificio no ficheiro buildings.xml
     * @param buildingId Identificador de edificio
     */
    public static void editBuilding(String buildingName, String buildingLocation, Integer buildingId) {

        try {
            xmlParser.editBuildingXml(strBuildingsXml, buildingName, buildingLocation, buildingId);    //update ao xml

        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }
    }

    /**
     * Metodo que retorna uma lista de edificios existentes
     * @return Buildings List
     */
    public static ArrayList getBuildingsList(){
        ArrayList arrBuildingsList = new ArrayList();

        validateBuildingsFolder();
        xmlParser.readBuildingXml(strBuildingsXml,arrBuildingsList);

        return arrBuildingsList;
    }

    /**
     * Metodo que valida a existencia da pasta buildings e da pasta dos edificios criados
     */
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
