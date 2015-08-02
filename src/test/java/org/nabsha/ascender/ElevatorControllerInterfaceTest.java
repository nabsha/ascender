package org.nabsha.ascender;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Nabeel Shaheen on 3/08/2015.
 */
public class ElevatorControllerInterfaceTest {

    @Test
    public void testRequestElevatorTest() throws InterruptedException {
        ElevatorControllerInterface eci1 = new ElevatorControllerInterface(1);
        ElevatorControllerInterface eci2 = new ElevatorControllerInterface(2);
        ElevatorControllerInterface eci3 = new ElevatorControllerInterface(3);

        List<Elevator> elevators = new ArrayList<Elevator>();
        Elevator e1 = createElevator("E1", 0, Direction.STATIONARY);
        Elevator e2 = createElevator("E2", 0, Direction.STATIONARY);
        Elevator e3 = createElevator("E3", 0, Direction.STATIONARY);
        Elevator e4 = createElevator("E4", 0, Direction.STATIONARY);

        elevators.add(e1);
        elevators.add(e2);
        elevators.add(e3);
        elevators.add(e4);

        ElevatorController controller = ElevatorController.getInstance();
        controller.setElevatorList(elevators);
        controller.startElevators();

        Floor.getInstance().addPersonOnFloor(1, 10, 7);
        eci1.requestElevator();

        Thread.sleep(2000);
        Floor.getInstance().addPersonOnFloor(2, 10, 8);
        eci2.requestElevator();

        Thread.sleep(2000);
        Floor.getInstance().addPersonOnFloor(3, 10, 10);
        eci3.requestElevator();

        for (Thread t : controller.getElevatorThreads()) {
            while (t.isAlive()) {
                t.join(1000);
                System.out.println(controller.getElevatorList());
                System.out.println(Floor.getInstance().getCountOfPeopleDelivered());
            }
        }
    }

    private Elevator createElevator(String elevatorId, int currentLevel, Direction direction) {
        Elevator e = new Elevator(elevatorId, 1, 10);
        e.setCurrentLevel(currentLevel);
        e.setDirection(direction);
        return e;
    }
}