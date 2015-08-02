package org.nabsha.ascender;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class ElevatorTest {

    @Test
    public void testElevatorOrderHandlingGoingDown() throws InterruptedException {
        Elevator e = new Elevator("E1", 1, 10);
        e.setRunning(true);
        e.setCurrentLevel(10);
        for (int i = 1; i < 10; i++)
            e.addOrder(i);


        Thread t = runElevatorTest(e);

        t.join();

        assertEquals(1, e.getCurrentLevel());

        assertEquals(new Integer(-1), e.getNextStop());

        assertTrue(e.getNextStopSet().isEmpty());

        assertEquals(Direction.STATIONARY, e.getDirection());


    }

    private Thread runElevatorTest(Elevator e) throws InterruptedException {
        Thread t = new Thread(e);
        t.start();


        Thread.sleep(2000);
        while (!e.getDirection().equals(Direction.STATIONARY)) {
            System.out.println(Floor.getInstance());
            Thread.sleep(1000);
        }
        e.setRunning(false);
        return t;
    }

    @Test
    public void testElevatorOrderHandlingGoingUp() throws InterruptedException {
        Elevator e = new Elevator("E1", 1, 10);
        e.setRunning(true);
        for (int i = 10; i > 1; i--)
            e.addOrder(i);


        Thread t = runElevatorTest(e);

        t.join();

        assertEquals(10, e.getCurrentLevel());

        assertEquals(new Integer(-1), e.getNextStop());

        assertTrue(e.getNextStopSet().isEmpty());

        assertEquals(Direction.STATIONARY, e.getDirection());
    }

    @Test
    public void testElevatorCapacity() throws InterruptedException {

        Elevator e = new Elevator("E1", 1, 10);
        e.setRunning(true);
        for (int i = 10; i > 1; i--)
            e.addOrder(i);

        Floor.getInstance().addPersonOnFloor(3, 10, 7);
        Floor.getInstance().addPersonOnFloor(4, 10, 7);
        Floor.getInstance().addPersonOnFloor(5, 20, 7);
        Thread t = runElevatorTest(e);

        t.join();

    }
}