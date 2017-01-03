package pt.ismat.ipower.utils;

import java.util.ArrayList;

import static pt.ismat.ipower.utils.Buildings.strBuildingsXml;

/**
 * @author Pedro Roldan on 02-01-2017.
 * @version 0.0
 */
public class Devices {

    private String strDeviceId, strDeviceType;
    private Integer intConsumo;
    private Boolean bolEnabled = false ;

    /**
     * Construtor para equipamentos
     * @param strDeviceId Identificador de equipamento
     */
    public Devices(String strDeviceId,Integer intConsumo,String strDeviceType,Boolean bolEnabled) {
        this.strDeviceId = strDeviceId;
        this.intConsumo = intConsumo;
        this.strDeviceType = strDeviceType;
        this.bolEnabled = bolEnabled;
    }

    /**
     * Metodo que retorna o identificador do equipamento
     * @return String Identificador de equipamento
     */
    public String getDeviceId() {
        return strDeviceId;
    }

    /**
     * Metodo que retorna o tipo de equipamento
     * @return String Equipamento (Automatico,Remoto)
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
    public static Devices loadDevice(String deviceId){

        return xmlParser.loadDeviceXml(strBuildingsXml,deviceId);
    }

    /**
     * Metodo que retorna uma lista de equipamentos existentes de um apartamento pelo seu id
     * @return Devices List
     */
    public static ArrayList getDevicesList(String deviceId){
        ArrayList arrDevicesList = new ArrayList();

        //validateBuildingsFolder();
        xmlParser.readDevicesXml(strBuildingsXml,arrDevicesList,deviceId);

        return arrDevicesList;
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
}
