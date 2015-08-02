package org.nabsha.ascender;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class ElevatorControllerInterface {

    private final int floor;

    private ElevatorController elevatorController;

    public ElevatorControllerInterface(int floor, ElevatorController elevatorController) {
        this.floor = floor;
        this.elevatorController = elevatorController;
    }

    public void requestElevator() {
        elevatorController.registerRequest(floor);
    }
}
