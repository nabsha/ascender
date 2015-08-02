package org.nabsha.ascender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class ElevatorController implements Runnable {

    private List<Elevator> elevatorList;
    private List<ElevatorControllerInterface> elevatorControllerInterfaceList;

    private List<Thread> elevatorThreads = new ArrayList<Thread>();

    public ElevatorController(List<Elevator> elevatorList) {
        this.elevatorList = elevatorList;
    }

    public void run() {
        startElevators();


    }

    private void startElevators() {
        for (Elevator elevator : elevatorList) {
            Thread t = new Thread(elevator);
            elevatorThreads.add(t);
            t.start();
        }
    }
}
