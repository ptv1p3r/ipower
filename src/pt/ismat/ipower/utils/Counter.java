package pt.ismat.ipower.utils;


import pt.ismat.ipower.forms.mainForm;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Pedro Roldan on 02-01-2017.
 * @version 0.0
 */
public class Counter implements Runnable {
    public Thread t;
    private String threadName;
    private static boolean suspended = false;
    private Date dataInicial, dataFinal;
    private Double dblTotalKwh;
    private Integer intTotalLeituras;

    public Counter(String name)
    {
        this.threadName = name;
        this.dataInicial = new Date();
        this.dataFinal = this.dataInicial;
        this.dblTotalKwh = 0.0D;
        this.intTotalLeituras = 0;

        System.out.println("Creating " +  threadName );
    }

    public Integer getTotalLeituras() {
        return intTotalLeituras;
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
            dataInicial = new Date();
            // TODO percorrer todos os equipamentos ligados e recolher o seu consumo com time stamp inicial e final e valor kw (kw equipamento * tempo )
            System.out.println("Running " +  threadName + dataInicial);

            DeviceReading newReading = new DeviceReading();
            Timer timer = new Timer(true); //timer como daemon thread
            timer.scheduleAtFixedRate(newReading, 0, 60*1000); // executa leitura minuto a minuto

            while (!suspended){ // enquanto nao for suspensa

                synchronized(this) {
                    while(suspended) {
                        timer.cancel();
                        System.out.println("Leitura cancelada");
                        wait();
                    }
                }
            }

            System.out.println("Thread " +  threadName + " exiting.");
            dataFinal = new Date();
            System.out.println(getDataInicial().toString() + "#" + getDataFinal().toString());
        }
        catch (InterruptedException e)
        {
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
     * Metodo que retorna a thread e a tira de modo suspenso
     */
    public synchronized void resume() {
        suspended = false;
        notify();
    }

    public class DeviceReading extends TimerTask {

        @Override
        public void run() {
            System.out.println("Reading started at:"+new Date());
            completeReading();
            System.out.println("Reading finished at:"+new Date());
        }

        private void completeReading() {
            try {
                intTotalLeituras++;

                ArrayList arrActiveDevices = Devices.getActiveDevicesList();
                Double dblTempoLeitura = Math.round((Double.valueOf(intTotalLeituras)/60)*100D)/100D; // nr leituras (1 minuto ) / 60 minutos
                Double dblTotalKw = 0.0D;


                for (int i=0; i < arrActiveDevices.size(); i++ ){
                    String[] arrDevice = arrActiveDevices.get(i).toString().trim().split("#");

                    // c = w / 1000 * h = kwh

                    dblTotalKw = dblTotalKw + Double.valueOf(arrDevice[2])/1000; // conversao w -> kW

                }

                dblTotalKwh = Math.round((dblTotalKw * dblTempoLeitura) * 100D)/100D;


                //DecimalFormat df=new DecimalFormat("0.000");
                mainForm.TotalKw.setText(String.valueOf(dblTotalKwh) + " kWh");
                mainForm.LeiturasTotal.setText(String.valueOf(intTotalLeituras));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
