package pt.ismat.ipower.utils;


import pt.ismat.ipower.forms.mainForm;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Pedro Roldan on 02-01-2017.
 * @version 0.0
 */
public class Counter implements Runnable {
    private Thread t;
    private String threadName;
    private Date dataInicial, dataFinal;
    private Double dblTotalKwh;
    private Integer intTotalLeituras;
    private Timer timer;
    private Map mapActiveDevices = new HashMap();

    /**
     * Metodo construtor do contador
     * @param name Nome do contador
     */
    public Counter(String name)
    {
        this.threadName = name;
        this.dataInicial = new Date();
        this.dataFinal = this.dataInicial;
        this.dblTotalKwh = 0.0D;
        this.intTotalLeituras = 0;
    }

    /**
     * Metodo que efetua o reset as variaveis do contador
     */
    public void resetCounter() {
        this.intTotalLeituras = 0;
        this.dblTotalKwh = 0.0D;
        this.dataInicial = new Date();
        this.dataFinal = this.dataInicial;
    }

    /**
     * Metodo que retorna o nome da thread
     * @return String O nome da thread
     */
    public String getName() {
        return threadName;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    @Override
    public void run(){
        // TODO percorrer todos os equipamentos ligados e recolher o seu consumo com time stamp inicial e final e valor kw (kw equipamento * tempo )

        DeviceReading newReading = new DeviceReading();
        timer = new Timer(false); //timer como daemon thread
        timer.scheduleAtFixedRate(newReading, 0, 60*1000); // executa leitura minuto a minuto

        // TODO em testes
        if (t.isInterrupted()){
            System.out.println("Thread " +  threadName + " exiting.");
        }
    }

    /**
     * Metodo que inicia a thread
     */
    public void start () {
        // TODO Retirar prints de consola
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    /**
     * Metodo que termina a thread e timer
     */
    public void terminate() {
        timer.cancel();
        t.interrupt();
        saveReadings();
    }

    /**
     * Metodo que grava as leituras obtidas dos equipamentos activos e os coloca em ficheiro
     */
    private void saveReadings(){
        // TODO Terminar algoritmo que guarda leituras em ficheiro
        // Definir o dataset do mapa
        Set sDataSet = mapActiveDevices.entrySet();

        // Definir um iterador
        Iterator i = sDataSet.iterator();

        // Varrimento do iterador
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
           // System.out.print(me.getKey() + ": ");
           // System.out.println(me.getValue());
            Devices.setDeviceReading(me.getKey().toString(),me.getValue().toString(), dataInicial, dataFinal);
        }
    }

    private class DeviceReading extends TimerTask {

        @Override
        public void run() {
            // TODO Retirar prints de consola
            System.out.println("Reading started at:"+new Date());
            completeReading();
            dataFinal = new Date();
            System.out.println("Reading finished at:"+new Date());
        }

        /**
         * Metodo que efetua uma leitura a todos os dispositivos
         */
        private void completeReading() {
            try {
                // TODO Terminar algoritmo de recolha e gravacao das leituras
                intTotalLeituras++;
                Double dblTotalKw = 0.0D;
                Double dblDeviceKw = 0.0D;

                Double dblTempoLeitura = Math.round((Double.valueOf(intTotalLeituras)/60)*100D)/100D; // nr leituras (1 minuto ) / 60 minutos

                // varrimento de todos os equipamentos activos
                for (int i=0; i < Devices.getActiveDevicesList().size(); i++ ){
                    String[] arrDevice = Devices.getActiveDevicesList().get(i).toString().trim().split("#");

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
                    String dateInString = "22-05-2016";
                    //Dates datas = new Dates(new Date());
                    Dates datas = new Dates(sdf.parse(dateInString));

                    System.out.println(datas.getSeasonName());


                    dblDeviceKw = Double.valueOf(arrDevice[2])/1000;
                    dblDeviceKw = Math.round((dblDeviceKw * dblTempoLeitura) * 100D)/100D;

                    // adiciona equipamento ao mapa de equipamentos activos
                    mapActiveDevices.put(arrDevice[0],dblDeviceKw);


                    dblTotalKw = dblTotalKw + Double.valueOf(arrDevice[2])/1000; // conversao w -> kW

                }

                // c = w / 1000 * h = kwh
                //Double dblTempoLeitura = Math.round((Double.valueOf(intTotalLeituras)/60)*100D)/100D; // nr leituras (1 minuto ) / 60 minutos
                dblTotalKwh = Math.round((dblTotalKw * dblTempoLeitura) * 100D)/100D;

                //DecimalFormat df=new DecimalFormat("0.000");
                mainForm.CargaTotal.setText(String.valueOf(dblTotalKw) + " kW");
                mainForm.TotalKw.setText(String.valueOf(dblTotalKwh) + " kWh");
                mainForm.LeiturasTotal.setText(String.valueOf(intTotalLeituras));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
