package org.nabsha.ascender.api;

import org.nabsha.ascender.Elevator;
import org.nabsha.ascender.ElevatorController;
import org.nabsha.ascender.ElevatorControllerInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Nabeel Shaheen on 3/08/2015.
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws SQLException the sQL exception
     * @throws ClassNotFoundException the class not found exception
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

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

        ElevatorAPI.apiHandlers();
    }
}
