package Models;

import java.util.concurrent.atomic.AtomicInteger;

public class Client implements Comparable<Client>{
    private int ID;
    private int arrivalTime;
    private int serviceTime;
    private int waitingPeriod;
    public Client ()
    {

    }
    public Client(int id,int arrTime,int serTime)
    {
        this.ID=id;
        this.arrivalTime=arrTime;
        this.serviceTime=serTime;
        this.waitingPeriod=0;
    }
    public synchronized int getServiceTime()
    {
        return this.serviceTime;
    }
    public int getArrivalTime()
    {
        return  this.arrivalTime;
    }
    public int getWaitingPeriod(){return this.waitingPeriod;}
    public void setWaitingPeriod(int waitingPeriod)
    {
        this.waitingPeriod=waitingPeriod;
    }
    public synchronized void decrementServiceTime() {
        this.serviceTime-=1;
    }
    @Override
    public int compareTo(Client o) {
        if(this.arrivalTime<o.getArrivalTime()) {
            return -1;
        } else if (this.arrivalTime>o.getServiceTime())
            return 1;
            else
                return 0;
    }

    public String toString()
    {
        return "ID="+this.ID+" arrTime="+this.arrivalTime+" serTime="+this.serviceTime;
    }



}
