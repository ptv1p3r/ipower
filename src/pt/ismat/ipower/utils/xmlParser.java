package pt.ismat.ipower.utils;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by v1p3r on 29-12-2016.
 */
public class xmlParser {

    /**
     * Metodo que le ficheiro xml de edificios e adiciona entradas a uma Building List
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param arrBuildingsList Lista de edificios
     */
    public static void readBuildingXml(String buildingXmlFile,ArrayList arrBuildingsList){

        try {
            File fXmlFile = new File(buildingXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nlBuildings = doc.getElementsByTagName("building");

            for (int i = 0; i < nlBuildings.getLength(); i++) {

                Node nBuilding = nlBuildings.item(i);

                if (nBuilding.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nBuilding;

                    arrBuildingsList.add(eElement.getAttribute("id") + "#" + eElement.getElementsByTagName("name").item(0).getTextContent());

                }
            }

        } catch (Exception e) {
            // TODO: 30-12-2016 tratar das excepcoes
            e.printStackTrace();
        }
    }

    /**
     * Metodo que le os apatamentos de um determinado edificio e adiciona entradas a uma apartments list
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param arrApartmentsList Lista de apartamentos
     * @param buildingId Identificador de edificio
     */
    public static void readApartmentsXml(String buildingXmlFile,ArrayList arrApartmentsList,Integer buildingId){

        try {
            File fXmlFile = new File(buildingXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nlBuildings = doc.getElementsByTagName("building");

            for (int i = 0; i < nlBuildings.getLength(); i++) {
                Node nBuilding = nlBuildings.item(i);

                if (nBuilding.getNodeType() == Node.ELEMENT_NODE) {
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(buildingId))) {
                     /*   NodeList nlApartments = doc.getElementsByTagName("apartments");

                        for (int a = 0; a < nlApartments.getLength(); a++) {
                            Node nApartment = nlApartments.item(a);

                            if (nApartment.getNodeType() == Node.ELEMENT_NODE) {
                                Element eApartment = (Element) nApartment;
                                arrApartmentsList.add(eApartment.getAttribute("id") + "#" + eApartment.getElementsByTagName("apt").item(0).getTextContent());
                            }
                        }*/
                     NodeList childList = nlBuildings.item(i).getChildNodes();
                        for (int j = 0; j < childList.getLength(); j++) {
                            Node childNode = childList.item(j);

                            if ("apartments".equals(childNode.getNodeName())) {

                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eApartment = (Element) childNode;

                                    arrApartmentsList.add(eApartment.getAttribute("id") + "#" + eApartment.getElementsByTagName("apt").item(0).getTextContent());
                                }
                                //System.out.println(childList.item(j).getTextContent().trim());
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
     * Metodo que carrega os equipamentos de um apatamento e adiciona a uma device list
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param arrDevicesList Lista de equipamentos
     * @param ApartmentId Identificador de apartamento
     */
    public static void readDevicesXml(String buildingXmlFile,ArrayList arrDevicesList,String ApartmentId){

        try {
            File fXmlFile = new File(buildingXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nlApartments = doc.getElementsByTagName("apartments"); // retorna no dos apartamentos

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
     * Metodo que cria o ficheiro xml de edificios base
     * @param buildingXmlFile Ficheiro xml de edificios
     */
    public static void createBuildingXml(String buildingXmlFile){

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elemento
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("buildings");
            doc.appendChild(root);

            // escreve ficheiro xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(buildingXmlFile));

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
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

            Document document = docBuilder.parse(buildingXmlFile);

            // root
            Element root = document.getDocumentElement();

            Element newBuilding = document.createElement("building");

            // atributo id
            Attr attr = document.createAttribute("id");
            attr.setValue(Building.getBuildingId().toString());
            newBuilding.setAttributeNode(attr);

            // nome
            Element buildingName = document.createElement("name");
            buildingName.appendChild(document.createTextNode(Building.getName()));
            newBuilding.appendChild(buildingName);

            // localizacao
            Element buildingLocation = document.createElement("location");
            buildingLocation.appendChild(document.createTextNode(Building.getLocation()));
            newBuilding.appendChild(buildingLocation);

            root.appendChild(newBuilding);


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
     * Metodo que carrega um edificio baseado nos seu dados do ficheiro xml
     * @param buildingXmlFile Ficheiro xml de edificios
     * @param id Identificador de edificio
     * @return Edificio
     */
    public static Buildings loadBuildingXml(String buildingXmlFile,Integer id){

        Buildings building = null;

        try {

            File fXmlFile = new File(buildingXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nlBuildingList = doc.getElementsByTagName("building");

            if (nlBuildingList != null && nlBuildingList.getLength() > 0) {

                for (int i = 0; i < nlBuildingList.getLength(); i++) {

                    Node nBuilding= nlBuildingList.item(i);
                    Element eBuilding = (Element) nBuilding;

                    if (eBuilding.hasAttribute("id") && eBuilding.getAttribute("id").equals(String.valueOf(id))) {
                        building = new Buildings( eBuilding.getElementsByTagName("name").item(0).getTextContent(), eBuilding.getElementsByTagName("location").item(0).getTextContent(),Integer.valueOf(eBuilding.getAttribute("id")));
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
                        device = new Devices( eDevice.getAttribute("id"),Integer.valueOf(eDevice.getElementsByTagName("euc").item(0).getTextContent()),eDevice.getAttribute("category"));
                    }

                }

            }

        } catch (Exception e) {
            // TODO: 30-12-2016 tratar das excepcoes
            e.printStackTrace();
        }

        return device;
    }

}
