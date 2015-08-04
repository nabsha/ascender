package org.nabsha.ascender.domain.elevator;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nabeel Shaheen on 4/08/2015.
 */
public class ElevatorRequestHandler implements Runnable {

    private LinkedBlockingQueue<Integer> incomingOrderList = new LinkedBlockingQueue<Integer>();

    private Elevator elevator;

    private boolean running = true;

    /**
     * Instantiates a new Elevator request handler.
     *
     * @param elevator the elevator
     */
    public ElevatorRequestHandler(Elevator elevator) {

        this.elevator = elevator;
    }

    /**
     * Gets incoming order list.
     *
     * @return the incoming order list
     */
    public LinkedBlockingQueue<Integer> getIncomingOrderList() {

        return incomingOrderList;
    }

    @Override
    public void run() {

        while (isRunning()) {
            checkIncomingOrder(1000);
        }
    }


    private synchronized boolean checkIncomingOrder(int timeout) {

        Integer order = null;

        try {
            order = this.incomingOrderList.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (order == null)
            return false;

        this.elevator.addOrder(order);
        return true;
    }

    /**
     * Is running.
     *
     * @return the boolean
     */
    public boolean isRunning() {

        return running;
    }

    /**
     * Sets running.
     *
     * @param running the running
     */
    public void setRunning(boolean running) {

        this.running = running;
    }
}
