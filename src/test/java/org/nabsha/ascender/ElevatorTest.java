package org.nabsha.ascender;

import org.junit.Test;
import org.nabsha.ascender.domain.actors.Floor;
import org.nabsha.ascender.domain.elevator.Direction;
import org.nabsha.ascender.domain.elevator.Elevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class ElevatorTest {

    /**
     * Test elevator order handling going down.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void testElevatorOrderHandlingGoingDown() throws InterruptedException {

        Elevator e = new Elevator("E1", 1, 10);
        e.setRunning(true);
        e.setCurrentLevel(10);
        e.addOrder(1);

        runElevatorTest(e, 11);

        assertEquals(1, e.getCurrentLevel());

        assertEquals(new Integer(-1), e.getNextStop());

        assertTrue(e.getNextStopSet().isEmpty());

        assertEquals(Direction.STATIONARY, e.getDirection());


    }

    private void runElevatorTest(Elevator e, int loopCount) throws InterruptedException {

        for (int i = 0; i < loopCount; i++) {
            e.processOrder();
            System.out.println(e);
        }
    }

    /**
     * Test elevator order handling going up.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void testElevatorOrderHandlingGoingUp() throws InterruptedException {

        Elevator e = new Elevator("E1", 1, 10);
        e.setRunning(true);
        for (int i = 10; i > 1; i--)
            e.addOrder(i);


        runElevatorTest(e, 10);

        assertEquals(10, e.getCurrentLevel());

        assertEquals(new Integer(-1), e.getNextStop());

        assertTrue(e.getNextStopSet().isEmpty());

        assertEquals(Direction.STATIONARY, e.getDirection());
    }

    /**
     * Test elevator capacity.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void testElevatorCapacity() throws InterruptedException {

        Floor.resetFloor();
        Elevator e = new Elevator("E1", 1, 10);
        e.setRunning(true);
        //for (int i = 10; i > 1; i--)
        e.addOrder(3);

        Floor.getInstance().addPersonOnFloor(3, 10, 4);
        Floor.getInstance().addPersonOnFloor(4, 10, 7);
        Floor.getInstance().addPersonOnFloor(7, 20, 9);
        runElevatorTest(e, 10);
        System.out.println(Floor.getInstance());
        assertEquals(0, Floor.getInstance().getCountOfPeopleWaitingOnFloor(3));
        assertEquals(0, Floor.getInstance().getCountOfPeopleWaitingOnFloor(4));
        assertEquals(0, Floor.getInstance().getCountOfPeopleWaitingOnFloor(7));

    }
}