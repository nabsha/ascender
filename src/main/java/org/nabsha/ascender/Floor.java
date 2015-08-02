package org.nabsha.ascender;

import java.util.*;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class Floor {
    private Map<Integer, LinkedList<Person>> floorInfo = new HashMap<Integer, LinkedList<Person>>();
    private Map<Integer, Integer> countOfPeopleDelivered = new HashMap<Integer, Integer>();
    private static Floor floor = null;

    private Floor() {
    }

    public Map<Integer, Integer> getCountOfPeopleDelivered() {
        return countOfPeopleDelivered;
    }

    public void incrementCountOfPeopleDelivered(int floor, int count) {
        if (countOfPeopleDelivered.get(floor) == null) {
            countOfPeopleDelivered.put(floor, 0);
        }
        countOfPeopleDelivered.put(floor, countOfPeopleDelivered.get(floor) + count);
    }

    public void setCountOfPeopleDelivered(Map<Integer, Integer> countOfPeopleDelivered) {
        this.countOfPeopleDelivered = countOfPeopleDelivered;
    }

    public static Floor getInstance() {
        if (floor == null) {
            floor = new Floor();
        }
        return floor;
    }

    public List<Person> takePeopleFromFloor(int floor, int personCount) {
        if (floorInfo.get(floor) == null) {
            return new ArrayList<Person>();
        }

        List<Person> persons = new ArrayList<Person>(floorInfo.get(floor).subList(0, Math.min(floorInfo.get(floor).size(), personCount)));
        for (Person p : persons) {
            floorInfo.get(floor).remove(p);
        }
        return persons;
    }

    public void addPersonOnFloor(int floor, int personCount, int destinationFloor) {
        if (!floorInfo.containsKey(floor)) {
            floorInfo.put(floor, new LinkedList<Person>());
        }
        for (int i = 0; i < personCount; i++) {
            floorInfo.get(floor).add(new Person(floor, destinationFloor));
        }
    }

    @Override
    public String toString() {
        String f = "";
        for (Integer i : floorInfo.keySet()) {
            f += "[" + i + "=" + floorInfo.get(i).size() + "]";
        }
        return "Floor{" +
                "floorInfo=" + f +
                ", countOfPeopleDelivered=" + countOfPeopleDelivered +
                '}';
    }
}