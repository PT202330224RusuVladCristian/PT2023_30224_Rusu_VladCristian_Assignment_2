package BussinesLogic;

import Models.Client;
import Models.ServerQueue;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {
    private List<ServerQueue> listQueues;
    private int maxNrQueues;
    private int maxClientsPerQueue;
    public Scheduler(int maxNrQueues,int maxClientsPerQueue)
    {
        listQueues=new ArrayList<ServerQueue>();
        this.maxNrQueues=maxNrQueues;
        this.maxClientsPerQueue=maxClientsPerQueue;
        for(int i=0;i<maxNrQueues;i++)
        {
            ServerQueue newQueue=new ServerQueue();
            listQueues.add(newQueue);
            Thread newThread=new Thread(newQueue);
            newThread.start();
        }
    }
    public synchronized void dispatchClient(Client c)
    {
        ServerQueue addToThis=new ServerQueue();
        addToThis=Collections.min(listQueues);
        c.setWaitingPeriod(addToThis.getWaitingPeriod().intValue());
        addToThis.addClients(c);
    }
    public void leaveQueue(Client c)
    {
        for(int i=0;i<this.listQueues.size();i++)
        {
            if(this.listQueues.get(i).getQueueClient().contains(c))
                this.listQueues.get(i).removeClients(c);
        }
    }
    public List<ServerQueue> getQueues()
    {
        return this.listQueues;
    }

    public int queueTime(Client c)
    {
        int time=0;
        for(int i=0;i<this.listQueues.size();i++)
        {
            if(this.listQueues.get(i).getQueueClient().contains(c)) {
                time = this.listQueues.get(i).getWaitingPeriod().intValue();
                break;
            }
        }
        return time;
    }

    public synchronized String printQueues()
    {
        String cozi = "";
        for(int i=0;i<this.maxNrQueues;i++)
        {
            //System.out.println("coada:"+i+" "+listQueues.get(i));
            cozi+="coada"+i+listQueues.get(i)+'\n';
        }
        return cozi;
    }
    public void decrementTimes()
    {
        for(int i=0;i<this.listQueues.size();i++)
        {
            if(this.listQueues.get(i).getWaitingPeriod().intValue()!=0)
                this.listQueues.get(i).decrementWaiting();
        }
    }


}
