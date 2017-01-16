package pt.ismat.ipower.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static pt.ismat.ipower.utils.Buildings.strBuildingsXml;

/**
 * @author Pedro Roldan on 02-01-2017.
 * @version 0.0
 */
public class Devices {

    final static String strBuildingsPath = System.getProperty("user.dir") + "/buildings";
    final static String strBuildingsXml = strBuildingsPath + "/buildings.xml";


    private String strDeviceCategory, strDeviceType;
    private Integer intConsumo, intDeviceId, intApartmentId, intBuildingId;
    private Boolean bolEnabled = false ;


    /**
     * Construtor de equipamento
     * @param intBuildingId Identificador de edificio
     * @param intApartmentId Identificador de apartamento
     * @param intConsumo consumo (W)
     * @param strDeviceCategory Categoria do equipamento
     * @param strDeviceType Tipo de equipamento
     * @param bolEnabled Estado do equipamento
     */
    public Devices(Integer intBuildingId, Integer intApartmentId,Integer intConsumo,String strDeviceCategory,String strDeviceType,Boolean bolEnabled) {
        this.intBuildingId=intBuildingId;
        this.intApartmentId=intApartmentId;
        this.intDeviceId = getNewDeviceId(intBuildingId, intApartmentId);
        this.intConsumo = intConsumo;
        this.strDeviceCategory = strDeviceCategory;
        this.bolEnabled = bolEnabled;
        this.strDeviceType = strDeviceType;
    }

    /**
     * construtor para equipamentos
     * @param intBuildingId Identificador de edificio
     * @param intApartmentId Identificador de apartamento
     * @param intDeviceId Identificador de equipamento
     * @param intConsumo consumo (W)
     * @param strDeviceCategory Categoria do equipamento
     * @param strDeviceType Tipo de equipamento
     * @param bolEnabled Estado do equipamento
     */
    public Devices(Integer intBuildingId, Integer intApartmentId,Integer intDeviceId, Integer intConsumo,String strDeviceCategory,String strDeviceType,Boolean bolEnabled) {
        this.intBuildingId=intBuildingId;
        this.intApartmentId=intApartmentId;
        this.intDeviceId = intDeviceId;
        this.intConsumo = intConsumo;
        this.strDeviceCategory = strDeviceCategory;
        this.bolEnabled = bolEnabled;
        this.strDeviceType = strDeviceType;
    }

    /**
     * Metodo que retorna o identificador do equipamento
     * @return Integer Identificador de equipamento
     */
    public Integer getDeviceId() {
        return intDeviceId;
    }

    /**
     * Metodo que retorna o identificador do apartamento
     * @return Integer Identificador de apartamento
     */
    public Integer getApartmentId() {
        return intApartmentId;
    }

    /**
     * Metodo que retorna o identificador do edificio
     * @return Integer Identificador de edificio
     */
    public Integer getBuildingId() {
        return intBuildingId;
    }

    /**
     * Metodo que retorna a categoria de equipamento
     * @return String Equipamento (Automatico,Remoto)
     */
    public String getDeviceCategory() {
        return strDeviceCategory;
    }

    /**
     * Metodo que retorna o tipo de equipamento
     * @return String Tipo (nenhum,frigorifico,aquecedor)
     */
    public String getDeviceType() {
        return strDeviceType;
    }

    /**
     * Metodo que retorna o consumo do equipamento
     * @return Integer Consumo kw
     */
    public Integer getConsumo() {
        return intConsumo;
    }

    /**
     * Metodo que retorna o estado do equipamento
     * @return Boolean activo/inactivo
     */
    public Boolean isEnabled() {
        return bolEnabled;
    }

    /**
     * Metodo que retorna um equipamento carregado com os seus dados
     * @param deviceId Identificador de equipamento
     * @return equipamento
     */
    public static Devices loadDevice(Integer buildingId, Integer apartmentId, Integer deviceId){

        return xmlParser.loadDeviceXml(strBuildingsXml,buildingId, apartmentId, deviceId);
    }

    /**
     * Metodo que retorna uma lista de equipamentos existentes de um apartamento pelo seu id
     * @return Devices List
     */
    public static ArrayList getDevicesList(Integer buildingId, Integer apartmentId){
        ArrayList arrDevicesList = new ArrayList();

        //validateBuildingsFolder();
        xmlParser.readDevicesXml(strBuildingsXml,arrDevicesList, buildingId, apartmentId);

        return arrDevicesList;
    }

    /**
     * Metodo que gera um novo id disponivel
     * @return Integer Novo id gerado
     */
    private Integer getNewDeviceId(Integer buildingId, Integer apartmentId){
        Integer id=1000;
        int i;

        try {
            ArrayList devicesList = getDevicesList(buildingId, apartmentId);

            Integer[] deviceId = new Integer[devicesList.size()];

            for (i = 0; i < devicesList.size(); i++) {
                String strDevices = (String) devicesList.get(i);

                if (id == Integer.parseInt(strDevices)) {
                    id++;
                }
            }

        } catch (Exception ex) {
            // TODO : validação de erros
            ex.printStackTrace();
        }

        return id;
    }

    /**
     * Metodo que efetua a gravacao do novo equipamento nos respetivos ficheiros xml
     * @param buildingId identificador do edificio
     * @param apartmentId identificador do apartamanto
     * @param device equipamento a ser gravado
     */
    public static void saveDevice(Integer buildingId, Integer apartmentId, Devices device){
        try {

            xmlParser.updateDeviceXml(strBuildingsXml, buildingId, apartmentId, device);
            xmlParser.updateApartmentDeviceXml((strBuildingsPath+"/"+buildingId+"/"+apartmentId+".xml"), device);


        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }
    }

    /**
     * Metodo que efetua a remoção do apartamento do ficheiro buildings.xml assim como o seu xml associado
     * @param apartmentId Identificador de apartamento
     * @param buildingId Identificador de edificio
     */
    public static void removeDevice(Integer buildingId, Integer apartmentId, Integer deviceId){


        try {
            xmlParser.removeDeviceXml(strBuildingsXml, buildingId, apartmentId, deviceId);
            xmlParser.removeApartmentDeviceXml((strBuildingsPath + "/" + buildingId + "/" + apartmentId + ".xml"), deviceId);

        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }
    }

    /**
     * Metodo que efetua a edição do apartamento no ficheiro buildings.xml
     * @param buildingId Identificador de edificio
     * @param apartmentId Identificador de apartamento
     * @param device equipamento a ser editado
     */
    public static void editDevice(Integer buildingId, Integer apartmentId, Devices device) {

        try {
            xmlParser.editDeviceXml((strBuildingsPath+"/"+buildingId+"/"+apartmentId+".xml"), buildingId, apartmentId, device.getDeviceId(),
                    device.getDeviceCategory(), device.getDeviceType());    //update ao xml

        } catch (Exception ex) {
            //TODO : validação de erros
            ex.printStackTrace();
        }
    }

    /**
     * Metodo que retorna uma lista de equipamentos activos existentes de todos os apartamentos
     * @return Devices List
     */
    public static ArrayList getActiveDevicesList(){
        ArrayList arrDevicesList = new ArrayList();

        //validateBuildingsFolder();
        xmlParser.readActiveDevicesXml(strBuildingsXml,arrDevicesList);

        return arrDevicesList;
    }

    /**
     * Metodo que retorna um valor total de equipamentos activos existentes em todos os apartamentos
     * @return Integer Devices Count
     */
    public static Integer getActiveDevices(){
        ArrayList arrDevicesList = new ArrayList();

        //validateBuildingsFolder();
        xmlParser.readActiveDevicesXml(strBuildingsXml,arrDevicesList);

        return arrDevicesList.size();
    }

    /**
     * Metodo que retorna um valor total de equipamentos activos existentes em todos os apartamentos
     * @return Integer Devices Count
     */
    public static Integer getDevices(){
        ArrayList arrDevicesList = new ArrayList();

        //validateBuildingsFolder();
        xmlParser.readDevicesTotalXml(strBuildingsXml,arrDevicesList);

        return arrDevicesList.size();
    }

    /**
     * Metodo que retorna um valor total de equipamentos activos existentes em todos os apartamentos
     * @return Integer Devices Count
     */
    public static Integer getDeviceReading(Integer intDeviceId){
        ArrayList arrDevicesList = new ArrayList();

        //validateBuildingsFolder();
        xmlParser.readDevicesTotalXml(strBuildingsXml,arrDevicesList);

        return arrDevicesList.size();
    }

    public static void setDeviceReading(String strDeviceId, String strConsumo){
        Integer intDevice, intBuild, intApart;

        String edificio = strDeviceId.substring(0,4);
        String apartamento = strDeviceId.substring(4,8);
        String equipamento = strDeviceId.substring(8,12);

        String strDeviceXml = strBuildingsPath + "/" + edificio + "/" + apartamento +".xml";

        xmlParser.writeDeviceReading(strDeviceXml,Integer.valueOf(equipamento),strConsumo);
    }
}
