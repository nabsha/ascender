package org.nabsha.ascender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void registerRequest(int floor) {
        selectElevator(floor).addOrder(floor);
    }

    public Elevator selectElevator(int requestFloor) {
        Map<String, Integer> elevatorSelectionScore = new HashMap<String, Integer>();
        int minScor = 100;
        Elevator selectedElevator = null;
        for (Elevator elevator:elevatorList) {
            int elevationScore;
            if (elevator.getDirection().equals(Direction.DOWNWARDS)) {
                elevationScore = elevator.getCurrentLevel() - requestFloor;
            } else if (elevator.getDirection().equals(Direction.UPWARDS)) {
                elevationScore = requestFloor - elevator.getCurrentLevel();
            } else {
                elevationScore = Math.abs(requestFloor - elevator.getCurrentLevel());
            }

            if (elevationScore < 0) {
                elevationScore = -elevationScore * 2;
            }
            if (minScor > elevationScore) {
                minScor = elevationScore;
                selectedElevator = elevator;
            }
        }

        return selectedElevator;
    }
}
