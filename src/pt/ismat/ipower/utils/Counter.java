package pt.ismat.ipower.utils;


import pt.ismat.ipower.forms.mainForm;

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
    private Integer intTotalKw;
    private Integer intTotalLeituras;

    public Counter(String name)
    {
        this.threadName = name;
        this.dataInicial = new Date();
        this.dataFinal = this.dataInicial;
        this.intTotalKw = 0;
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
     * @return Integer Total de kw
     */
    public Integer getTotalKw() {
        return intTotalKw;
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
            timer.scheduleAtFixedRate(newReading, 0, 60*1000);

            while (!suspended){

                //System.out.println("Reading started");
                //System.out.println(intTotalKw);

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
                ArrayList arrActiveDevices = Devices.getActiveDevicesList();

                for (int i=0; i < arrActiveDevices.size(); i++ ){
                    String[] arrDevice = arrActiveDevices.get(i).toString().trim().split("#");

                    intTotalKw = intTotalKw + Integer.valueOf(arrDevice[2]);
                }

                intTotalLeituras++;
                mainForm.TotalKw.setText(String.valueOf(intTotalKw) + " Kw");
                mainForm.LeiturasTotal.setText(String.valueOf(intTotalLeituras));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
