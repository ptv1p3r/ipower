package pt.ismat.ipower.utils;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class xmlParser {
    private static Document documento;

    /**
     * Metodo que le ficheiro xml de edificios e adiciona entradas a uma Building List
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param arrBuildingsList Lista de edificios
     */
    public static void readBuildingXml(String buildingXmlFile,ArrayList arrBuildingsList){

        try {
            documento = xmlHeaderDocument(buildingXmlFile);
            NodeList nlBuildings = documento.getElementsByTagName("building");

            for (int i = 0; i < nlBuildings.getLength(); i++) {

                Node nBuilding = nlBuildings.item(i);

                if (nBuilding.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nBuilding;

                    arrBuildingsList.add(eElement.getAttribute("id") + "#" + eElement.getAttribute("name"));

                }
            }

        } catch (Exception e) {
            // TODO: 30-12-2016 tratar das excepcoes
            e.printStackTrace();
        }
    }

    /**
     * Metodo que le os apartamentos de um determinado edificio e adiciona entradas a uma apartments list
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param arrApartmentsList Lista de apartamentos
     * @param buildingId Identificador de edificio
     */
    public static void readApartmentsXml(String buildingXmlFile,ArrayList arrApartmentsList,Integer buildingId){

        try {
            //buildings.xml
            documento = xmlHeaderDocument(buildingXmlFile);
            NodeList nlBuildings = documento.getElementsByTagName("building");

            //vai a procura do edificio
            for (int i = 0; i < nlBuildings.getLength(); i++) {
                Node nBuilding = nlBuildings.item(i);

                if (nBuilding.getNodeType() == Node.ELEMENT_NODE) {
                    Element eBuilding = (Element) nBuilding;

                    //confirma o edificio
                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        //vai buscar todos os apartamentos do edificio
                        NodeList nlApartments = eBuilding.getElementsByTagName("apartment");

                        //le a informacao de todos os apartamentos
                        for (int k = 0; k < nlApartments.getLength(); k++) {
                            Node nApartments = nlApartments.item(k);

                            if (nApartments.getNodeType() == Node.ELEMENT_NODE) {
                                Element eApartment = (Element) nApartments;

                                arrApartmentsList.add(eApartment.getAttribute("id") + "#" + eApartment.getAttribute("apt"));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            // TODO: 30-12-2016 tratar das excepcoes
            e.printStackTrace();
        }
    }

    /**
     * Metodo que carrega os equipamentos de um apartamento e adiciona a uma device list
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param arrDevicesList Lista de equipamentos
     * @param apartmentId Identificador de apartamento
     */
    public static void readDevicesXml(String buildingXmlFile,ArrayList arrDevicesList, Integer buildingId,Integer apartmentId){

        try {
            documento = xmlHeaderDocument(buildingXmlFile);

            NodeList nlBuildings = documento.getElementsByTagName("building");

            //vai a procura do edificio
            for (int i = 0; i < nlBuildings.getLength(); i++) {
                Node nBuilding = nlBuildings.item(i);

                if (nBuilding.getNodeType() == Node.ELEMENT_NODE) {
                    Element eBuilding = (Element) nBuilding;

                    //confirma o edificio
                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        NodeList nlApartments = eBuilding.getElementsByTagName("apartment"); // retorna no dos apartamentos

                        for (int j = 0; j < nlApartments.getLength(); j++) { // percorre apartamentos
                            Node nApartment = nlApartments.item(j);

                            if (nApartment.getNodeType() == Node.ELEMENT_NODE) { // tipo de no
                                Element eApartment = (Element) nApartment;

                                // valida apartamento correcto
                                if (eApartment.hasAttribute("id") && eApartment.getAttribute("id").equals(apartmentId.toString())) {

                                    NodeList nlDevice = eApartment.getElementsByTagName("device");

                                    for (int k = 0; k < nlDevice.getLength(); k++) {
                                        Node nDevice = nlDevice.item(k);
                                        Element eDevice = (Element) nDevice;

                                        if (eDevice.hasAttribute("id")) {

                                            arrDevicesList.add(eDevice.getAttribute("id")/* + "#" + eDevice.getAttribute("category")*/);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            // TODO: 30-12-2016 tratar das excepcoes
            e.printStackTrace();
        }
    }

    /**
     * Metodo que carrega os equipamentos activos de todos os apartamentos e adiciona a uma device list
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param arrDevicesList Lista de equipamentos
     */
    public static void readActiveDevicesXml(String buildingXmlFile,ArrayList arrDevicesList){

        try {
            documento = xmlHeaderDocument(buildingXmlFile);

            NodeList nlBuildings = documento.getElementsByTagName("building");

            for (int i = 0; i < nlBuildings.getLength(); i++) {
                Node nBuilding = nlBuildings.item(i);

                if (nBuilding.getNodeType() == Node.ELEMENT_NODE) {
                    Element eBuilding = (Element) nBuilding;

                    //confirma o edificio
                    if (eBuilding.hasAttribute("id")) {

                        NodeList nlApartments = eBuilding.getElementsByTagName("apartment"); // retorna no dos apartamentos

                        for (int j = 0; j < nlApartments.getLength(); j++) { // percorre apartamentos
                            Node nApartment = nlApartments.item(j);

                            if (nApartment.getNodeType() == Node.ELEMENT_NODE) { // tipo de no
                                Element eApartment = (Element) nApartment;

                                // valida apartamento correcto
                                if (eApartment.hasAttribute("id")) {

                                    NodeList nlDevice = eApartment.getElementsByTagName("device");

                                    for (int k = 0; k < nlDevice.getLength(); k++) {
                                        Node nDevice = nlDevice.item(k);
                                        Element eDevice = (Element) nDevice;

                                        if (eDevice.hasAttribute("id")) {

                                            if (Boolean.valueOf(eDevice.getElementsByTagName("enable").item(0).getTextContent())) { // devices activos
                                                arrDevicesList.add(eBuilding.getAttribute("id")+eApartment.getAttribute("id")+eDevice.getAttribute("id") + "#" + eDevice.getAttribute("category") + "#" +
                                                        eDevice.getElementsByTagName("euc").item(0).getTextContent() + "#" + eDevice.getAttribute("type"));
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO: 30-12-2016 tratar das excepcoes
            e.printStackTrace();
        }
    }

    /**
     * Metodo que carrega os equipamentos activos de todos os apartamentos e adiciona a uma device list
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param arrDevicesList Lista de equipamentos
     */
    public static void readDevicesTotalXml(String buildingXmlFile,ArrayList arrDevicesList){

        try {
            documento = xmlHeaderDocument(buildingXmlFile);

            NodeList nlDevice = documento.getElementsByTagName("device"); /// apanha todos os devices

            for (int i = 0; i < nlDevice.getLength(); i++) { // percorre devices
                Node nDevice = nlDevice.item(i);

                Element eDevice = (Element) nDevice;

                arrDevicesList.add(eDevice.getAttribute("id") + "#" + eDevice.getAttribute("category") + "#" +
                        eDevice.getElementsByTagName("euc").item(0).getTextContent()); // devices activos

            }

        } catch (Exception e) {
            // TODO: 30-12-2016 tratar das excepcoes
            e.printStackTrace();
        }
    }

    /**
     * Metodo que cria o ficheiro xml de edificios base
     * @param buildingXmlFile Ficheiro xml de edificios
     */
    public static void createBuildingXml(String buildingXmlFile){

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elemento
            documento = docBuilder.newDocument();
            Element root = documento.createElement("buildings");
            documento.appendChild(root);

            xmlWriteDocument(buildingXmlFile);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }

    }

    /**
     * Metodo que cria o ficheiro xml de apartamento base
     * @param apartmentXmlFile Ficheiro xml de edificios
     */
    public static void createApartmentXml(String apartmentXmlFile){

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elemento
            documento = docBuilder.newDocument();
            Element root = documento.createElement("devices");
            documento.appendChild(root);

            xmlWriteDocument(apartmentXmlFile);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    /**
     * Metodo que actualiza o ficheiro de edificios xml
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param Apartment Edificio a ser adicionado
     */
    public static void updateApartmentXml(String buildingXmlFile,Apartments Apartment, Integer buildingId){

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            documento = docBuilder.parse(buildingXmlFile);

            documento.getDocumentElement().normalize();

            NodeList nlBuildingList = documento.getElementsByTagName("building");

            if (nlBuildingList != null && nlBuildingList.getLength() > 0) { //verifica se a nodelist dos edificios foi bem lida

                for (int i = 0; i < nlBuildingList.getLength(); i++) {  //procura pelo edificio com o id inserido

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    //entra quando encontra o edificio
                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        //Vai buscar o node apartments ao node do building
                        NodeList nlApartmentList = eBuilding.getElementsByTagName("apartments");
                        Node nApartments=nlApartmentList.item(0);

                        //cria o node do apartment
                        Element newApartment = documento.createElement("apartment");

                        // atributo id
                        Attr apartmentId = documento.createAttribute("id");
                        apartmentId.setValue(Apartment.getApartmentId().toString());
                        newApartment.setAttributeNode(apartmentId);

                        // apt nome
                        Attr apartmentName = documento.createAttribute("apt");
                        apartmentName.setValue(Apartment.getApartmentName());
                        newApartment.setAttributeNode(apartmentName);

                        nApartments.appendChild(newApartment);

                        //insere um node devices para os equipamentos
                        Element eDevice = documento.createElement("devices");
                        newApartment.appendChild(eDevice);

                        // escreve ficheiro xml
                        DOMSource source = new DOMSource(documento);

                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        StreamResult result = new StreamResult(buildingXmlFile);


                        transformer.transform(source, result);
                    }

                }

            }

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }catch (SAXException sax) {
            sax.printStackTrace();
        }

    }

    /**
     * Metodo que actualiza o ficheiro de edificios xml
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param Building Edificio a ser adicionado
     */
    public static void updateBuildingXml(String buildingXmlFile,Buildings Building){

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //Document document = docBuilder.parse(buildingXmlFile);
            documento = docBuilder.parse(buildingXmlFile);

            // root
            Element root = documento.getDocumentElement();

            Element newBuilding = documento.createElement("building");

            // atributo id
            Attr attrBuildingId = documento.createAttribute("id");
            attrBuildingId.setValue(Building.getBuildingId().toString());
            newBuilding.setAttributeNode(attrBuildingId);

            // nome
            Attr buildingName = documento.createAttribute("name");
            buildingName.setValue(Building.getName());
            newBuilding.setAttributeNode(buildingName);

            // localizacao
            Attr buildingLocation = documento.createAttribute("location");
            buildingLocation.setValue(Building.getLocation());
            newBuilding.setAttributeNode(buildingLocation);

            root.appendChild(newBuilding);

            //abre o node dos apartments e da apend ao node do edificio
            Element eApartment = documento.createElement("apartments");
            newBuilding.appendChild(eApartment);

            xmlWriteDocument(buildingXmlFile);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sax) {
            sax.printStackTrace();
        }

    }

    /**
     * Metodo que remove o edificio do ficheiro buildings.xml atraves do seu id
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param id Identificador de edificio
     */
    public static void removeBuildingXml(String buildingXmlFile,Integer id){
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            documento = docBuilder.parse(buildingXmlFile);

            NodeList nlBuildingList = documento.getElementsByTagName("building");
            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                //procura o node selecionado
                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    //verifica se e o node selecionado
                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(id))) {
                        //remove o node
                        nBuilding.getParentNode().removeChild(nBuilding);
                    }

                }

            }


            // escreve ficheiro xml
            DOMSource source = new DOMSource(documento);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(buildingXmlFile);

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }catch (SAXException sax) {
            sax.printStackTrace();
        }
    }

    /**
     * Metodo que remove o apartamento do ficheiro buildings.xml atraves do seu id
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param buildingId Identificador de edificio
     * @param apartmentId Identificador de apartamento
     */
    public static void removeApartmentXml(String buildingXmlFile,Integer buildingId, Integer apartmentId){
        try {

            documento = xmlHeaderDocument(buildingXmlFile);


            NodeList nlBuildingList = documento.getElementsByTagName("building");

            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        //Vai buscar o node apartments ao node do building
                        NodeList nlApartments = eBuilding.getElementsByTagName("apartments");

                        for (int j=0 ; j<nlApartments.getLength() ; j++ ) {

                            Node nApartments = nlApartments.item(j);
                            Element eApartments = (Element) nApartments;

                            //verifica se existem apartamentos no edificio
                            if (eApartments!=null) {

                                //guarda todos os nodes dos apartamentos
                                NodeList nlApartment = eBuilding.getElementsByTagName("apartment");

                                //procura pelo apartamento selecionado
                                for (int k=0 ; k<nlApartment.getLength() ; k++) {

                                    Node nApartment = nlApartment.item(k);
                                    Element eApartment = (Element) nApartment;

                                    //verifica se e o node selecionado
                                    if (eApartment.hasAttribute("id") && eApartment.getAttribute("id").equals(String.valueOf(apartmentId))) {

                                        //remove o node
                                        nApartment.getParentNode().removeChild(nApartment);
                                    }
                                }
                            }
                        }
                    }
                }
            }


            // escreve ficheiro xml
            DOMSource source = new DOMSource(documento);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(buildingXmlFile);

            transformer.transform(source, result);

        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    /**
     * Metodo que edita o apartamento do ficheiro buildings.xml atraves do seu id
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param buildingId Identificador de edificio
     * @param apartmentId Identificador de apartamento
     */
    public static void editApartmentXml(String buildingXmlFile,Integer buildingId, Integer apartmentId, String apartmentName){
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            documento = docBuilder.parse(buildingXmlFile);

            NodeList nlBuildingList = documento.getElementsByTagName("building");

            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        //Vai buscar o node apartments ao node do building
                        NodeList nlApartments = eBuilding.getElementsByTagName("apartments");

                        for (int j=0 ; j<nlApartments.getLength() ; j++ ) {

                            Node nApartments = nlApartments.item(j);
                            Element eApartments = (Element) nApartments;

                            //verifica se existem apartamentos no edificio
                            if (eApartments!=null) {

                                //guarda todos os nodes dos apartamentos
                                NodeList nlApartment = eBuilding.getElementsByTagName("apartment");

                                for (int k=0 ; k<nlApartment.getLength() ; k++) {

                                    Node nApartment = nlApartment.item(k);
                                    Element eApartment = (Element) nApartment;

                                    //procura pelo apartamento selecionado
                                    if (eApartment.hasAttribute("id") && eApartment.getAttribute("id").equals(String.valueOf(apartmentId))) {

                                        //elimina o primeiro e o ultimo espaco do novo nome para o substituir no xml
                                        eApartment.setAttribute("apt", apartmentName.trim());

                                    }
                                }
                            }
                        }
                    }
                }
            }


            // escreve ficheiro xml
            DOMSource source = new DOMSource(documento);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(buildingXmlFile);

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }catch (SAXException sax) {
            sax.printStackTrace();
        }
    }


    /**
     * Metodo que edita o apartamento do ficheiro buildings.xml atraves do seu id
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param buildingId Identificador de edificio
     */
    public static void editBuildingXml(String buildingXmlFile,String buildingName, String buildingLocation, Integer buildingId){
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            documento = docBuilder.parse(buildingXmlFile);

            NodeList nlBuildingList = documento.getElementsByTagName("building");

            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        eBuilding.setAttribute("location", buildingLocation.trim());
                        eBuilding.setAttribute("name", buildingName.trim());
                    }
                }
            }


            // escreve ficheiro xml
            DOMSource source = new DOMSource(documento);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(buildingXmlFile);

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }catch (SAXException sax) {
            sax.printStackTrace();
        }
    }


    /**
     * Metodo que carrega um edificio baseado nos seu dados do ficheiro xml
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param id Identificador de edificio
     * @return Edificio
     */
    public static Buildings loadBuildingXml(String buildingXmlFile,Integer id){

        Buildings building = null;

        try {
            documento = xmlHeaderDocument(buildingXmlFile);
            NodeList nlBuildingList = documento.getElementsByTagName("building");

            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(id))) {
                        building = new Buildings( eBuilding.getAttribute("name"), eBuilding.getAttribute("location"),
                                Integer.valueOf(eBuilding.getAttribute("id")));
                    }

                }

            }

        } catch (Exception e) {
            // TODO: 30-12-2016 tratar das excepcoes
            e.printStackTrace();
        }

        return building;
    }

    /**
     * Metodo que carrega um eqipamento baseado nos seu dados do ficheiro xml
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param buildingId Identificador de edificio
     * @param apartmentId Identificador de apartamento
     * @param deviceId Identificador de equipamento
     * @return Equipamento
     */
    public static Devices loadDeviceXml(String buildingXmlFile,Integer buildingId, Integer apartmentId, Integer deviceId){

        Devices device = null;

        try {
            //buildings.xml
            documento = xmlHeaderDocument(buildingXmlFile);
            NodeList nlBuildings = documento.getElementsByTagName("building");

            //vai a procura do edificio
            for (int i = 0; i < nlBuildings.getLength(); i++) {
                Node nBuilding = nlBuildings.item(i);

                if (nBuilding.getNodeType() == Node.ELEMENT_NODE) {
                    Element eBuilding = (Element) nBuilding;

                    //confirma o edificio
                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        //vai buscar todos os apartamentos do edificio
                        NodeList nlApartments = eBuilding.getElementsByTagName("apartment");

                        //le a informacao de todos os apartamentos
                        for (int k = 0; k < nlApartments.getLength(); k++) {
                            Node nApartments = nlApartments.item(k);

                            if (nApartments.getNodeType() == Node.ELEMENT_NODE) {
                                Element eApartment = (Element) nApartments;

                                if (eApartment.hasAttribute("id") && eApartment.getAttribute("id").equals(String.valueOf(apartmentId))) {

                                    NodeList nlDevicesList = eApartment.getElementsByTagName("device");

                                    if (nlDevicesList != null && nlDevicesList.getLength() > 0) {

                                        for (int j = 0; j < nlDevicesList.getLength(); j++) {

                                            Node nDevice= nlDevicesList.item(j);
                                            Element eDevice = (Element) nDevice;

                                            if (eDevice.hasAttribute("id") && eDevice.getAttribute("id").equals(String.valueOf(deviceId))) {
                                                device = new Devices(buildingId, apartmentId, deviceId,
                                                        Integer.valueOf(eDevice.getElementsByTagName("euc").item(0).getTextContent()),
                                                        eDevice.getAttribute("category"),
                                                        eDevice.getAttribute("type"),
                                                        Boolean.valueOf(eDevice.getElementsByTagName("enable").item(0).getTextContent()));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            // TODO: 30-12-2016 tratar das excepcoes
            e.printStackTrace();
        }

        return device;
    }

    /**
     * Metodo que adiciona um equipamento ao ficheiro buildings.xml atraves do id do apartamento
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param buildingId Identificador de edificio
     * @param apartmentId Identificador de apartamento
     * @param device Equipamento
     */
    public static void updateDeviceXml(String buildingXmlFile,Integer buildingId, Integer apartmentId, Devices device){
        try {

            documento = xmlHeaderDocument(buildingXmlFile);

            NodeList nlBuildingList = documento.getElementsByTagName("building");

            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        //Vai buscar o node apartments ao node do building
                        NodeList nlApartments = eBuilding.getElementsByTagName("apartments");

                        for (int j=0 ; j<nlApartments.getLength() ; j++ ) {

                            Node nApartments = nlApartments.item(j);
                            Element eApartments = (Element) nApartments;

                            //verifica se existem apartamentos no edificio
                            if (eApartments!=null) {

                                //guarda todos os nodes dos apartamentos
                                NodeList nlApartment = eBuilding.getElementsByTagName("apartment");

                                for (int k=0 ; k<nlApartment.getLength() ; k++) {

                                    Node nApartment = nlApartment.item(k);
                                    Element eApartment = (Element) nApartment;

                                    //procura pelo apartamento selecionado
                                    if (eApartment.hasAttribute("id") && eApartment.getAttribute("id").equals(String.valueOf(apartmentId))) {

                                        //Vai buscar o node devices ao node do apartamento
                                        NodeList nlDevicesList = eApartment.getElementsByTagName("devices");
                                        Node nDevice=nlDevicesList.item(0);

                                        Element newDevice = documento.createElement("device");

                                        // atributo id
                                        Attr attrDeviceId = documento.createAttribute("id");
                                        attrDeviceId.setValue(device.getDeviceId().toString());
                                        newDevice.setAttributeNode(attrDeviceId);

                                        // atributo category
                                        Attr attrDeviceCategory = documento.createAttribute("category");
                                        attrDeviceCategory.setValue(device.getDeviceCategory());
                                        newDevice.setAttributeNode(attrDeviceCategory);

                                        // atributo type
                                        Attr attrDeviceType = documento.createAttribute("type");
                                        attrDeviceType.setValue(device.getDeviceType());
                                        newDevice.setAttributeNode(attrDeviceType);

                                        nDevice.appendChild(newDevice);

                                        //estado do equipamento
                                        Element eStatus = documento.createElement("enable");
                                        eStatus.setTextContent(Boolean.toString(device.isEnabled()));
                                        newDevice.appendChild(eStatus);

                                        //TODO: <auto>0</auto>, what is this?
                                        //Element ? = documento.createElement("auto");
                                        //newDevice.appendChild(?);

                                        //consumo do equipamento
                                        Element eConsume = documento.createElement("euc");
                                        eConsume.setTextContent(Integer.toString(device.getConsumo()));
                                        newDevice.appendChild(eConsume);

                                    }
                                }
                            }
                        }
                    }
                }
            }


            xmlWriteDocument(buildingXmlFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que escreve a leitura de um equipamento no ficheiro xml do apartamento selecionado
     * @param deviceXmlFile Ficheiro xml de apartamento
     * @param DeviceId
     * @param strConsumo
     * @param dtDataInicial
     * @param dtDataFinal
     */
    public static void writeDeviceReading(String deviceXmlFile, Integer DeviceId,String strConsumo, Date dtDataInicial,Date dtDataFinal){
        try{
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            documento = docBuilder.parse(deviceXmlFile);

            NodeList nlDeviceList = documento.getElementsByTagName("device");

            if (nlDeviceList != null && nlDeviceList.getLength() > 0) {

                for (int i=0 ; i<nlDeviceList.getLength() ; i++) {

                    Node nDevice = nlDeviceList.item(i);
                    Element eDevice = (Element) nDevice;

                    if (eDevice.hasAttribute("id") && eDevice.getAttribute("id").equals(String.valueOf(DeviceId))) {

                        Element newReading = documento.createElement("reading");
                        eDevice.appendChild(newReading);

                        //data inicial
                        Element eDataInicial = documento.createElement("start_date_time");
                        eDataInicial.setTextContent(dt.format(dtDataInicial));
                        newReading.appendChild(eDataInicial);

                        //data inicial
                        Element eDataFinal = documento.createElement("end_date_time");
                        eDataFinal.setTextContent(dt.format(dtDataFinal));
                        newReading.appendChild(eDataFinal);

                        //data inicial
                        Element eTotalKw = documento.createElement("energy");
                        eTotalKw.setTextContent(strConsumo);
                        newReading.appendChild(eTotalKw);


                    }
                }
            }

            xmlWriteDocument(deviceXmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que remove um equipamento ao ficheiro buildings.xml atraves do seu id
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param buildingId Identificador de edificio
     * @param apartmentId Identificador de apartamento
     * @param deviceId Identificador de equipamento
     */
    public static void removeDeviceXml(String buildingXmlFile,Integer buildingId, Integer apartmentId, Integer deviceId){
        try {

            documento = xmlHeaderDocument(buildingXmlFile);

            NodeList nlBuildingList = documento.getElementsByTagName("building");

            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        //Vai buscar o node apartments ao node do building
                        NodeList nlApartments = eBuilding.getElementsByTagName("apartments");

                        for (int j=0 ; j<nlApartments.getLength() ; j++ ) {

                            Node nApartments = nlApartments.item(j);
                            Element eApartments = (Element) nApartments;

                            //verifica se existem apartamentos no edificio
                            if (eApartments != null) {

                                //guarda todos os nodes dos apartamentos
                                NodeList nlApartment = eBuilding.getElementsByTagName("apartment");

                                for (int k = 0; k < nlApartment.getLength(); k++) {

                                    Node nApartment = nlApartment.item(k);
                                    Element eApartment = (Element) nApartment;

                                    //procura pelo apartamento selecionado
                                    if (eApartment.hasAttribute("id") && eApartment.getAttribute("id").equals(String.valueOf(apartmentId))) {

                                        //Vai buscar o node devices ao node do apartamento
                                        NodeList nlDevicesList = eApartment.getElementsByTagName("device");
                                        //procura pelo device selecionado
                                        for (int l = 0; l < nlDevicesList.getLength(); l++) {

                                            Node nDevice = nlDevicesList.item(l);
                                            Element eDevice = (Element) nDevice;

                                            //verifica se e o device selecionado
                                            if (eDevice.hasAttribute("id") && eDevice.getAttribute("id").equals(String.valueOf(deviceId))) {

                                                //remove o node
                                                eDevice.getParentNode().removeChild(nDevice);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            xmlWriteDocument(buildingXmlFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que edita um equipamento no ficheiro buildings.xml atraves do seu id
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param buildingId Identificador de edificio
     * @param apartmentId Identificador de apartamento
     * @param device Device a ser editado
     */
    public static void editDeviceXml(String buildingXmlFile,Integer buildingId, Integer apartmentId, Devices device){
        try {

            documento = xmlHeaderDocument(buildingXmlFile);

            NodeList nlBuildingList = documento.getElementsByTagName("building");

            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {

                        //Vai buscar o node apartments ao node do building
                        NodeList nlApartments = eBuilding.getElementsByTagName("apartments");

                        for (int j=0 ; j<nlApartments.getLength() ; j++ ) {

                            Node nApartments = nlApartments.item(j);
                            Element eApartments = (Element) nApartments;

                            //verifica se existem apartamentos no edificio
                            if (eApartments!=null) {

                                //guarda todos os nodes dos apartamentos
                                NodeList nlApartment = eBuilding.getElementsByTagName("apartment");

                                for (int k=0 ; k<nlApartment.getLength() ; k++) {

                                    Node nApartment = nlApartment.item(k);
                                    Element eApartment = (Element) nApartment;

                                    //procura pelo apartamento selecionado
                                    if (eApartment.hasAttribute("id") && eApartment.getAttribute("id").equals(String.valueOf(apartmentId))) {

                                        //Vai buscar o node devices ao node do apartamento
                                        NodeList nlDevicesList = eApartment.getElementsByTagName("device");

                                        //procura pelo device selecionado
                                        for (int l=0 ; l<nlDevicesList.getLength() ; l++) {

                                            Node nDevice = nlDevicesList.item(l);
                                            Element eDevice = (Element) nDevice;

                                            //verifica se e o device selecionado
                                            if (eDevice.hasAttribute("id") && eDevice.getAttribute("id").equals(String.valueOf(device.getDeviceId()))) {

                                                eDevice.setAttribute("category", device.getDeviceCategory().trim());
                                                eDevice.setAttribute("type", device.getDeviceType().trim());

                                                //estado do equipamento
//                                                Element eStatus = documento.createElement("enable");
//                                                eStatus.setTextContent(Boolean.toString(device.isEnabled()));
//                                                eDevice.appendChild(eStatus);
//
//                                                //consumo do equipamento
//                                                Element eConsume = documento.createElement("euc");
//                                                eConsume.setTextContent(Integer.toString(device.getConsumo()));
//                                                eDevice.appendChild(eConsume);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            // escreve ficheiro xml
            xmlWriteDocument(buildingXmlFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que actualiza o ficheiro de xml de um determinado apartamento com um equipamento
     * @param apartmentXmlFile Ficheiro xml do apartamento
     * @param device Equipamento a ser adicionado
     */
    public static void updateApartmentDeviceXml(String apartmentXmlFile,Devices device){

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //Document document = docBuilder.parse(buildingXmlFile);
            documento = docBuilder.parse(apartmentXmlFile);

            // root
            Element root = documento.getDocumentElement();

            Element newDevice = documento.createElement("device");

            // atributo id
            Attr attrDevcieId = documento.createAttribute("id");
            attrDevcieId.setValue(device.getDeviceId().toString());
            newDevice.setAttributeNode(attrDevcieId);

            root.appendChild(newDevice);



            xmlWriteDocument(apartmentXmlFile);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sax) {
            sax.printStackTrace();
        }

    }

    /**
     * Metodo que remove do ficheiro de xml de um determinado apartamento um equipamento selecionado
     * @param apartmentXmlFile Ficheiro xml do apartamento
     * @param deviceId Equipamento a ser removido
     */
    public static void removeApartmentDeviceXml(String apartmentXmlFile,Integer deviceId){

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            documento = docBuilder.parse(apartmentXmlFile);

            //vai buscar todos os equipamentos existentes no ficheiro xml
            NodeList nlDeviceList = documento.getElementsByTagName("device");

            if (nlDeviceList != null && nlDeviceList.getLength() > 0) {

                //percorre todos os equipamentos
                for (int i = 0; i < nlDeviceList.getLength(); i++) {

                    Node nDevice = nlDeviceList.item(i);
                    Element eDevice = (Element) nDevice;

                    //encontra o selecionado
                    if (eDevice.hasAttribute("id") && eDevice.getAttribute("id").equals(String.valueOf(deviceId))) {

                        //remove o selecionado
                        nDevice.getParentNode().removeChild(nDevice);

                    }
                }
            }

            xmlWriteDocument(apartmentXmlFile);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sax) {
            sax.printStackTrace();
        }

    }

    /**
     * Metodo que devolve um documento xml normalizado
     * @param xmlFile Caminho de ficheiro xml
     * @return Document Documento normalizado
     */
    private static Document xmlHeaderDocument(String xmlFile){
        try {

            File fXmlFile = new File(xmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            documento = dBuilder.parse(fXmlFile);

            //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            documento.getDocumentElement().normalize();

        } catch (ParserConfigurationException pce){
            pce.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        } catch (SAXException se){
            se.printStackTrace();
        }

        return documento;
    }

    /**
     * Metodo que escreve o documento normalizado para o ficheiro xml
     * @param buildingXmlFile Caminho de ficheiro xml
     */
    private static void xmlWriteDocument(String buildingXmlFile){

        try {
            // escreve ficheiro xml
            DOMSource source = new DOMSource(documento);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(buildingXmlFile);

            transformer.transform(source, result);

        } catch (TransformerConfigurationException tce){
            tce.printStackTrace();
        } catch (TransformerException te){
            te.printStackTrace();
        }

    }

}
