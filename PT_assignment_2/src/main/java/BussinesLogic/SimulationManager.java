package BussinesLogic;

import Models.Client;
import Models.ServerQueue;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SimulationManager implements  Runnable{
    private int nrClients=5;
    private int nrQueues=5;
    private int maxSimulationTime=60;
    private int minArrTime=2;
    private int maxArrTime=35;
    private int minSerTime=2;
    private int maxSerTime=9;
    private Scheduler scheduler;
    public LinkedList<Client> generatedClients=new LinkedList<>();
    private final Logger logger = Logger.getLogger("MyLog");
    public SimulationManager()
    {
        this.scheduler=new Scheduler(this.nrQueues,this.nrClients);
        this.generateNRandomClients();
        setUpLogger();
    }
    public SimulationManager(int nrClients,int nrQueues,int maxSimulationTime)
    {
        this.nrClients=nrClients;
        this.nrQueues=nrQueues;
        this.maxSimulationTime=maxSimulationTime;
        this.scheduler=new Scheduler(this.nrQueues,this.nrClients);
        this.generateNRandomClients();
        setUpLogger();
    }

    private void setUpLogger() {
        try {
            FileHandler fh = new FileHandler("simulation.txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void generateNRandomClients()
    {
        Random random=new Random();
        int id=0;
        for(int i=0;i<this.nrClients;i++)
        {
            id++;
            int arrTime= random.nextInt(maxArrTime-minArrTime+1)+minArrTime;
            int serTime=random.nextInt(maxSerTime-minSerTime+1)+minSerTime;
            this.generatedClients.add(new Client(id,arrTime,serTime));

        }
        Collections.sort(this.generatedClients);
    }
    public double avgServiceTime()
    {
        double time=0;
        for(int i=0;i<this.generatedClients.size();i++)
            time=time+this.generatedClients.get(i).getServiceTime();
        return time/this.nrClients;
    }
    public boolean verifyEndOfSimulation(List<ServerQueue> serverList) {
        if (generatedClients.size() != 0)
            return false;
        else
            for (ServerQueue queue : serverList) {
                if (queue.getQueueClient().size() != 0)
                    return false;
            }
        return true;
    }
    public void run() {
        AtomicInteger currentTime = new AtomicInteger(0);
        List<ServerQueue>serverQueueList=this.scheduler.getQueues();
        double avgServiceTime = this.avgServiceTime();
        int sumWaitingTime = 0;
        int avgWaitingTime = 0;
        String message = "";
        while (!verifyEndOfSimulation(serverQueueList) && currentTime.intValue()<=maxSimulationTime) {
            Client client = generatedClients.peekFirst();
            while (client != null && client.getArrivalTime() <= currentTime.intValue()) {
                this.scheduler.dispatchClient(client);
                generatedClients.removeFirst();
                if (generatedClients.isEmpty())
                    break;
                else
                 client = generatedClients.peekFirst();
            }
            message = "current time:" + currentTime;
            message += '\n' + scheduler.printQueues();
            logger.info(message);


            currentTime.addAndGet(1);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //message = "current time:" + currentTime;
        //message += '\n' + scheduler.printQueues();
        //logger.info(message);
        //  avgWaitingTime=sumWaitingTime/nrClients;
        message += "avg service time:" + avgServiceTime + "\n";
        //message+="avg waiting time:"+avgWaitingTime+"\n";
        logger.info(message);
    }


    public List<Client> getGeneratedClients() {
        return this.generatedClients;
    }
   // public static void main(String args[])
    //{
      //  SimulationManager sim=new SimulationManager();

        //System.out.println(sim.generatedClients);
        //Thread t=new Thread(sim);
        //t.start();

    //}


}
