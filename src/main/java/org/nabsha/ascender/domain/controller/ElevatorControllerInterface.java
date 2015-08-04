package org.nabsha.ascender.domain.controller;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class ElevatorControllerInterface {

    /**
     * Request elevator.
     *
     * @param floor the floor
     */
    public static void requestElevator(int floor) {

        ElevatorController.getInstance().registerRequest(floor);
    }
}
