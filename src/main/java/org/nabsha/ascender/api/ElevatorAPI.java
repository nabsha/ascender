package org.nabsha.ascender.api;

import org.nabsha.ascender.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Nabeel Shaheen on 3/08/2015.
 */
public class ElevatorAPI {

    /**
     * Rest Api handlers.
     */
    public static void apiHandlers() {

        get("/api/elevators", (req, res) -> {
            CopyOnWriteArrayList<ElevatorModel> models = new CopyOnWriteArrayList<ElevatorModel>();
            for (Elevator e : ElevatorController.getInstance().getElevatorList()) {
                models.add(e.getElevatorModel());
            }
            return models;
        }, new JsonTransformer());

        get("/api/floors", (req, res) -> {
            return Floor.getInstance();
        }, new JsonTransformer());

        get("/api/requestElevator/:floor", (req, res) -> {
            String floor = req.params(":floor");
            ElevatorControllerInterface.requestElevator(Integer.valueOf(floor));
            return "Elevator request submitted";
        });

        post("/api/addPeopleOnFloor", (req, res) -> {
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
