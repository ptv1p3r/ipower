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
import java.util.ArrayList;

/**
 * Created by v1p3r on 29-12-2016.
 */
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
     * @param ApartmentId Identificador de apartamento
     */
    public static void readDevicesXml(String buildingXmlFile,ArrayList arrDevicesList,String ApartmentId){

        try {
            documento = xmlHeaderDocument(buildingXmlFile);
            NodeList nlApartments = documento.getElementsByTagName("apartments"); // retorna no dos apartamentos

            for (int i = 0; i < nlApartments.getLength(); i++) { // percorre apartamentos
                Node nApartment= nlApartments.item(i);

                if (nApartment.getNodeType() == Node.ELEMENT_NODE) { // tipo de no
                    Element eApartment = (Element) nApartment;

                    if (eApartment.hasAttribute("id") && eApartment.getAttribute("id").equals(ApartmentId.toString())) { // valida apartamento correcto

                        NodeList childList = nlApartments.item(i).getChildNodes();
                        for (int j = 0; j < childList.getLength(); j++) {
                            Node childNode = childList.item(j);

                            if ("devices".equals(childNode.getNodeName())) {
                                Element eDevice = (Element) childNode;
                                arrDevicesList.add(eDevice.getAttribute("id") + "#" + eDevice.getAttribute("category"));

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
            NodeList nlApartments = documento.getElementsByTagName("apartments"); // retorna no dos apartamentos

            for (int i = 0; i < nlApartments.getLength(); i++) { // percorre apartamentos
                Node nApartment= nlApartments.item(i);

                if (nApartment.getNodeType() == Node.ELEMENT_NODE) { // tipo de no

                    NodeList childList = nlApartments.item(i).getChildNodes();

                    for (int j = 0; j < childList.getLength(); j++) {
                        Node childNode = childList.item(j);

                        if ("devices".equals(childNode.getNodeName())) {
                            Element eDevice = (Element) childNode;

                            if (Boolean.valueOf(eDevice.getElementsByTagName("enable").item(0).getTextContent())) { // equipamentos activos
                                arrDevicesList.add(eDevice.getAttribute("id") + "#" + eDevice.getAttribute("category") + "#" + eDevice.getElementsByTagName("euc").item(0).getTextContent());
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
            NodeList nlApartments = documento.getElementsByTagName("apartments"); // retorna no dos apartamentos

            for (int i = 0; i < nlApartments.getLength(); i++) { // percorre apartamentos
                Node nApartment= nlApartments.item(i);

                if (nApartment.getNodeType() == Node.ELEMENT_NODE) { // tipo de no

                    NodeList childList = nlApartments.item(i).getChildNodes();
                    for (int j = 0; j < childList.getLength(); j++) {
                        Node childNode = childList.item(j);

                        if ("devices".equals(childNode.getNodeName())) {
                            Element eDevice = (Element) childNode;
                                arrDevicesList.add(eDevice.getAttribute("id") + "#" + eDevice.getAttribute("category") + "#" + eDevice.getElementsByTagName("euc").item(0).getTextContent());
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
            Element root = documento.createElement("device");
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

            Document document = docBuilder.parse(buildingXmlFile);

            document.getDocumentElement().normalize();

            NodeList nlBuildingList = document.getElementsByTagName("building");

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
                        Element newApartment = document.createElement("apartment");

                        // atributo id
                        Attr apartmentId = document.createAttribute("id");
                        apartmentId.setValue(Apartment.getApartmentId().toString());
                        newApartment.setAttributeNode(apartmentId);

                        // apt nome
                        Attr apartmentName = document.createAttribute("apt");
                        apartmentName.setValue(Apartment.getApartmentName());
                        newApartment.setAttributeNode(apartmentName);

                        nApartments.appendChild(newApartment);


                        // escreve ficheiro xml
                        DOMSource source = new DOMSource(document);

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
            Element eBuilding = documento.createElement("apartments");
            newBuilding.appendChild(eBuilding);

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

            Document document = docBuilder.parse(buildingXmlFile);

            NodeList nlBuildingList = document.getElementsByTagName("building");
            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(id))) {
                        nBuilding.getParentNode().removeChild(nBuilding);
                    }

                }

            }


            // escreve ficheiro xml
            DOMSource source = new DOMSource(document);

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

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document document = docBuilder.parse(buildingXmlFile);

            NodeList nlBuildingList = document.getElementsByTagName("building");

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
            DOMSource source = new DOMSource(document);

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
                        building = new Buildings( eBuilding.getAttribute("name"), eBuilding.getAttribute("location"),Integer.valueOf(eBuilding.getAttribute("id")));
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
     * @param id Identificador de equipamento
     * @return Equipamento
     */
    public static Devices loadDeviceXml(String buildingXmlFile,String id){

        Devices device = null;

        try {

            File fXmlFile = new File(buildingXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nlDevicesList = doc.getElementsByTagName("devices");

            if (nlDevicesList != null && nlDevicesList.getLength() > 0) {

                for (int i = 0; i < nlDevicesList.getLength(); i++) {

                    Node nDevice= nlDevicesList.item(i);
                    Element eDevice = (Element) nDevice;

                    if (eDevice.hasAttribute("id") && eDevice.getAttribute("id").equals(id)) {
                        device = new Devices( eDevice.getAttribute("id"),Integer.valueOf(eDevice.getElementsByTagName("euc").item(0).getTextContent()),eDevice.getAttribute("category"),Integer.valueOf(eDevice.getAttribute("type")),Boolean.valueOf(eDevice.getElementsByTagName("enable").item(0).getTextContent()));
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
