package Models;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class ServerQueue implements Runnable,Comparable<ServerQueue>{
    private BlockingQueue<Client> queueClient;
    private AtomicInteger waitingPeriod;
    public ServerQueue()
    {
        queueClient=new LinkedBlockingQueue<>();
        this.waitingPeriod=new AtomicInteger(0);
    }
    public synchronized void addClients(Client newClient)
    {
        queueClient.add(newClient);
       // newClient.setWaitingPeriod(this.waitingPeriod.intValue());
        this.waitingPeriod.addAndGet(newClient.getServiceTime());

    }
    public void removeClients(Client removeClient)
    {
        queueClient.remove(removeClient);
        waitingPeriod.addAndGet(-removeClient.getServiceTime());
    }
    public void decrementWaiting()
    {
        waitingPeriod.addAndGet(-1);
    }

    public void run() {
        while (true) {
            Client extracted = queueClient.peek();
            if (extracted != null) {
                try {
                    sleep(1000);
                    extracted.decrementServiceTime();
                    if (extracted.getServiceTime() == 0)
                        queueClient.poll();
                    waitingPeriod.decrementAndGet();
                } catch (InterruptedException e) {
                    System.out.println("Problem with thread sleeping");
                }
            }
        }
    }
    public AtomicInteger getWaitingPeriod()
    {
        return this.waitingPeriod;
    }

    public BlockingQueue<Client> getQueueClient() {
        return this.queueClient;
    }

    @Override
    public int compareTo(ServerQueue o) {
        if(this.waitingPeriod.intValue()<o.getWaitingPeriod().intValue()) {
            return -1;
        } else if (this.waitingPeriod.intValue()>o.getWaitingPeriod().intValue())
            return 1;
        else
            return 0;
    }
    public String toString()
    {
        return ""+this.queueClient;
    }

}
