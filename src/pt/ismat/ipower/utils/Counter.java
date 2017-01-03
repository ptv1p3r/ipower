package pt.ismat.ipower.utils;


import java.util.ArrayList;
import java.util.Date;

/**
 * @author Pedro Roldan on 02-01-2017.
 * @version 0.0
 */
public class Counter implements Runnable {
    public Thread t;
    private String threadName;
    private static boolean suspended = false;
    private Date dataInicial, dataFinal;

    public Counter(String name)
    {
        this.threadName = name;
        this.dataInicial = new Date();
        this.dataFinal = this.dataInicial;
        System.out.println("Creating " +  threadName );
    }

    /**
     * Metodo que retorna o nome da thread
     * @return String O nome da thread
     */
    public String getName() {
        return threadName;
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
            System.out.println("Running " +  threadName );

            while (!suspended){
                System.out.println("Thread: " + threadName);

                ArrayList arrActiveDevices = Devices.getActiveDevicesList();
                System.out.println(arrActiveDevices.size());

                for (int i = 0; i < arrActiveDevices.size()-1; i++){

                }



                // Let the thread sleep for a while.
                Thread.sleep(300);

                synchronized(this) {
                    while(suspended) {
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
}
