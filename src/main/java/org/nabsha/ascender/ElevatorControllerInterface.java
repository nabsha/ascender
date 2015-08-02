package org.nabsha.ascender;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class ElevatorControllerInterface {

    private final int floor;

    public ElevatorControllerInterface(int floor) {
        this.floor = floor;
    }

    public void requestElevator() {
        ElevatorController.getInstance().registerRequest(floor);
    }
}
