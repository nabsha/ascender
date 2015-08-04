package org.nabsha.ascender;

import org.junit.Test;
import org.nabsha.ascender.domain.controller.ElevatorController;
import org.nabsha.ascender.domain.elevator.Direction;
import org.nabsha.ascender.domain.elevator.Elevator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class ElevatorControllerTest {

    /**
     * Test elevator selection.
     */
    @Test
    public void testElevatorSelection() {
        Elevator e1 = createElevator("E1", 7, Direction.DOWNWARDS);
        Elevator e2 = createElevator("E2", 5, Direction.UPWARDS);
        Elevator e3 = createElevator("E3", 3, Direction.DOWNWARDS);
        Elevator e4 = createElevator("E4", 1, Direction.UPWARDS);

        List<Elevator> elevators = new ArrayList<Elevator>();
        elevators.add(e1);
        elevators.add(e2);
        elevators.add(e3);
        elevators.add(e4);
        ElevatorController controller = ElevatorController.getInstance();
        controller.setElevatorList(elevators);
        assertEquals("E2", controller.selectElevator(5).getElevatorId());
        assertEquals("E3", controller.selectElevator(3).getElevatorId());
        assertEquals("E1", controller.selectElevator(7).getElevatorId());
        assertEquals("E2", controller.selectElevator(10).getElevatorId());
    }

    private Elevator createElevator(String elevatorId, int currentLevel, Direction direction) {
        Elevator e = new Elevator(elevatorId, 1, 10);
        e.setCurrentLevel(currentLevel);
        e.setDirection(direction);
        return e;
    }
}