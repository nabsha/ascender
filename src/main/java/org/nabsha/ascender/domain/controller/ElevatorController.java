package org.nabsha.ascender.domain.controller;

import org.nabsha.ascender.domain.elevator.Direction;
import org.nabsha.ascender.domain.elevator.Elevator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class ElevatorController {

    private static ElevatorController elevatorController = null;
    private List<Elevator> elevatorList;
    private List<ElevatorControllerInterface> elevatorControllerInterfaceList;
    private List<Thread> elevatorThreads = new CopyOnWriteArrayList<Thread>();

    private ElevatorController() {}

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ElevatorController getInstance() {
        if (elevatorController == null)
            elevatorController = new ElevatorController();

        return elevatorController;
    }

    /**
     * Gets elevator threads.
     *
     * @return the elevator threads
     */
    public List<Thread> getElevatorThreads() {
        return elevatorThreads;
    }

    /**
     * Gets elevator list.
     *
     * @return the elevator list
     */
    public List<Elevator> getElevatorList() {
        return elevatorList;
    }

    /**
     * Sets elevator list.
     *
     * @param elevatorList the elevator list
     */
    public void setElevatorList(List<Elevator> elevatorList) {
        this.elevatorList = elevatorList;
    }

    /**
     * Gets elevator controller interface list.
     *
     * @return the elevator controller interface list
     */
    public List<ElevatorControllerInterface> getElevatorControllerInterfaceList() {
        return elevatorControllerInterfaceList;
    }

    /**
     * Sets elevator controller interface list.
     *
     * @param elevatorControllerInterfaceList the elevator controller interface list
     */
    public void setElevatorControllerInterfaceList(List<ElevatorControllerInterface> elevatorControllerInterfaceList) {
        this.elevatorControllerInterfaceList = elevatorControllerInterfaceList;
    }

    /**
     * Start elevators.
     */
    public void startElevators() {
        for (Elevator elevator : elevatorList) {
            Thread t = new Thread(elevator);
            elevatorThreads.add(t);
            t.start();
        }
    }

    /**
     * Register request.
     *
     * @param floor the floor
     */
    public synchronized void registerRequest(int floor) {
        selectElevator(floor).getElevatorRequestHandler().getIncomingOrderList().add(floor);
    }

    /**
     * Select elevator.
     *
     * @param requestFloor the request floor
     * @return the elevator
     */
    public Elevator selectElevator(int requestFloor) {
        int minScor = 100;
        Elevator selectedElevator = null;
        for (Elevator elevator : elevatorList) {
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

    public void shutDownElevators() {

        for (Elevator e : elevatorList) {
            e.setRunning(false);
        }
    }
}
