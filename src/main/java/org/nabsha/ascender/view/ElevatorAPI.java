package org.nabsha.ascender.view;

import org.nabsha.ascender.domain.actors.Floor;
import org.nabsha.ascender.domain.controller.ElevatorController;
import org.nabsha.ascender.domain.controller.ElevatorControllerInterface;
import org.nabsha.ascender.domain.elevator.Elevator;
import org.nabsha.ascender.domain.elevator.ElevatorModel;
import org.nabsha.ascender.persistence.ElevatorRecorder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Nabeel Shaheen on 3/08/2015.
 */
public class ElevatorAPI {

    /**
     * Rest Api handlers.
     */
    public static void createRESTHandler() {

        get("/view/elevators", (req, res) -> {
            CopyOnWriteArrayList<ElevatorModel> models = new CopyOnWriteArrayList<ElevatorModel>();
            for (Elevator e : ElevatorController.getInstance().getElevatorList()) {
                models.add(e.getElevatorModel());
            }
            return models;
        }, new JsonTransformer());

        get("/view/floors", (req, res) -> {
            return Floor.getInstance();
        }, new JsonTransformer());

        get("/view/requestElevator/:floor", (req, res) -> {
            String floor = req.params(":floor");
            ElevatorControllerInterface.requestElevator(Integer.valueOf(floor));
            return "Elevator request submitted";
        });

        post("/view/addPeopleOnFloor", (req, res) -> {
            Map<String, String> keyValueMap = keyValues(req.body());
            String floorNumber = keyValueMap.get("floorNumber");
            String peopleCount = keyValueMap.get("peopleCount");
            String destinationFloor = keyValueMap.get("destinationFloor");

            if (floorNumber == null || peopleCount == null || destinationFloor == null) {
                return "Invalid request format, POST [url] body:floorNumber=1&peopleCount=10&destinationFloor=5";
            }

            Floor.getInstance().addPersonOnFloor(Integer.valueOf(floorNumber), Integer.valueOf(peopleCount), Integer.valueOf(destinationFloor));
            return Floor.getInstance().getCountOfPeopleWaitingOnFloor(Integer.valueOf(floorNumber));
        }, new JsonTransformer());

        get("/view/events", (req, res) -> {
            return ElevatorRecorder.getAll();
        }, new JsonTransformer());
    }

    private static Map<String, String> keyValues(String body) {

        String[] toks = body.split("&");
        Map<String, String> keyValueMap = new HashMap<>();
        for (String tok : toks) {
            String[] keyValue = tok.split("=");
            keyValueMap.put(keyValue[0], keyValue[1]);
        }
        return keyValueMap;
    }
}
