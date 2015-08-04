package org.nabsha.ascender;

import org.nabsha.ascender.domain.controller.ElevatorController;
import org.nabsha.ascender.domain.elevator.Elevator;
import org.nabsha.ascender.view.ElevatorAPI;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Nabeel Shaheen on 3/08/2015.
 */
public class Main {

    public static void main(String[] args) {

        createEverything();

        ElevatorAPI.createRESTHandler();
    }

    private static void createEverything() {

        List<Elevator> elevators = new CopyOnWriteArrayList<Elevator>();
        Elevator e1 = new Elevator("Elevator 1", 1, 10);
        Elevator e2 = new Elevator("Elevator 2", 1, 10);
        Elevator e3 = new Elevator("Elevator 3", 1, 10);
        Elevator e4 = new Elevator("Elevator 4", 1, 10);

        elevators.add(e1);
        elevators.add(e2);
        elevators.add(e3);
        elevators.add(e4);

        ElevatorController controller = ElevatorController.getInstance();
        controller.setElevatorList(elevators);
        controller.startElevators();
    }
}
