package pt.ismat.ipower.utils;


import pt.ismat.ipower.forms.mainForm;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Pedro Roldan on 02-01-2017.
 * @version 0.0
 */
public class Counter implements Runnable {
    private Thread t;
    private String threadName;
    private static boolean suspended = false;
    private Date dataInicial, dataFinal;
    private Double dblTotalKwh;
    private Integer intTotalLeituras;


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

        //System.out.println("Creating " +  threadName );
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

    /**
     * Metodo que retorna o total de kw
     * @return Double Total de kw
     */
    public Double getTotalKwh() {
        return dblTotalKwh;
    }

    /**
     * Metodo que retorna o estado da thread, se esta supensa ou nao
     * @return Estado da thread (suspensa)
     */
    public static Boolean isSuspended() {
        return suspended;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    @Override
    public void run()
    {
        try
        {
            //dataInicial = dt.parse(dataInicial.toString());
            // TODO percorrer todos os equipamentos ligados e recolher o seu consumo com time stamp inicial e final e valor kw (kw equipamento * tempo )
            //System.out.println("Running " +  threadName + dataInicial);

            DeviceReading newReading = new DeviceReading();
            Timer timer = new Timer(false); //timer como daemon thread
            timer.scheduleAtFixedRate(newReading, 0, 60*1000); // executa leitura minuto a minuto

            while (!suspended){ // enquanto nao for suspensa

                mainForm.LeiturasTotal.setText(String.valueOf(intTotalLeituras));

                synchronized(this) {
                    while(suspended) {
                        timer.cancel();
                        dataFinal = new Date();

                        System.out.println("Leitura interrompida");
                        wait();

                    }
                }
            }

            dataFinal = new Date();
            System.out.println("Thread " +  threadName + " exiting.");

        }
        catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }
    }

    /**
     * Metodo que inicia a thread
     */
    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    /**
     * Metodo que coloca a thread em suspenso
     */
    public void suspend() {
        suspended = true;
    }

    /**
     * Metodo que retorna a thread e a tira de modo suspensao
     */
    public synchronized void resume() {
        suspended = false;
        notify();
    }

    private class DeviceReading extends TimerTask {

        @Override
        public void run() {
            System.out.println("Reading started at:"+new Date());
            completeReading();
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

                ArrayList arrActiveDevices = Devices.getActiveDevicesList();

                for (int i=0; i < arrActiveDevices.size(); i++ ){
                    String[] arrDevice = arrActiveDevices.get(i).toString().trim().split("#");

                    dblTotalKw = dblTotalKw + Double.valueOf(arrDevice[2])/1000; // conversao w -> kW

                }

                // c = w / 1000 * h = kwh
                Double dblTempoLeitura = Math.round((Double.valueOf(intTotalLeituras)/60)*100D)/100D; // nr leituras (1 minuto ) / 60 minutos
                dblTotalKwh = Math.round((dblTotalKw * dblTempoLeitura) * 100D)/100D;

                //DecimalFormat df=new DecimalFormat("0.000");
                mainForm.CargaTotal.setText(String.valueOf(dblTotalKw) + " kW");
                mainForm.TotalKw.setText(String.valueOf(dblTotalKwh) + " kWh");
                //mainForm.LeiturasTotal.setText(String.valueOf(intTotalLeituras));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
