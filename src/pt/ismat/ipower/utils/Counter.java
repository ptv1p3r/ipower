package pt.ismat.ipower.utils;


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

    public String getName() {
        return threadName;
    }

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

                for(int i = 100; i > 0; i--) {
                    System.out.println("Thread: " + threadName + ", " + i);
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

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    public void suspend() {
        suspended = true;
    }

    public synchronized void resume() {
        suspended = false;
        notify();
    }
}
