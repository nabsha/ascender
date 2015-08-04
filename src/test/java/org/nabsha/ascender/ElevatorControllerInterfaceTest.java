package org.nabsha.ascender;

import org.junit.Test;
import org.nabsha.ascender.domain.actors.Floor;
import org.nabsha.ascender.domain.controller.ElevatorController;
import org.nabsha.ascender.domain.controller.ElevatorControllerInterface;
import org.nabsha.ascender.domain.elevator.Direction;
import org.nabsha.ascender.domain.elevator.Elevator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nabeel Shaheen on 3/08/2015.
 */
public class ElevatorControllerInterfaceTest {

    /**
     * Test request elevator test.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void testRequestElevatorTest() throws InterruptedException {

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
        ElevatorControllerInterface.requestElevator(1);

        Thread.sleep(2000);
        Floor.getInstance().addPersonOnFloor(2, 10, 8);
        ElevatorControllerInterface.requestElevator(2);

        Thread.sleep(2000);
        Floor.getInstance().addPersonOnFloor(3, 10, 10);
        ElevatorControllerInterface.requestElevator(3);

        Thread.sleep(10000);
        System.out.println(controller.getElevatorList());
        controller.shutDownElevators();
    }

    private Elevator createElevator(String elevatorId, int currentLevel, Direction direction) {
        Elevator e = new Elevator(elevatorId, 1, 10);
        e.setCurrentLevel(currentLevel);
        e.setDirection(direction);
        return e;
    }
}