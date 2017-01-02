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

    /**
     * Construtor para equipamentos
     * @param strDeviceId Identificador de equipamento
     */
    public Devices(String strDeviceId,Integer intConsumo,String strDeviceType) {
        this.strDeviceId = strDeviceId;
        this.intConsumo = intConsumo;
        this.strDeviceType = strDeviceType;
    }

    public String getDeviceId() {
        return strDeviceId;
    }

    public String getDeviceType() {
        return strDeviceType;
    }

    public Integer getConsumo() {
        return intConsumo;
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
     * Metodo que retorna uma lista de apartamentos existentes de um edificio pelo seu id
     * @return Apartments List
     */
    public static ArrayList getDevicesList(String deviceId){
        ArrayList arrDevicesList = new ArrayList();

        //validateBuildingsFolder();
        xmlParser.readDevicesXml(strBuildingsXml,arrDevicesList,deviceId);

        return arrDevicesList;
    }
}
