package org.nabsha.ascender.domain.actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class Floor {

    private static Floor floor = null;

    private Map<Integer, CopyOnWriteArrayList<Person>> floorInfo = new HashMap<Integer, CopyOnWriteArrayList<Person>>();

    private Map<Integer, Integer> countOfPeopleDelivered = new HashMap<Integer, Integer>();

    private Floor() {

    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Floor getInstance() {

        if (floor == null) {
            floor = new Floor();
        }
        return floor;
    }

    /**
     * Gets count of people delivered.
     *
     * @return the count of people delivered
     */
    public Map<Integer, Integer> getCountOfPeopleDelivered() {

        return countOfPeopleDelivered;
    }

    /**
     * Increment count of people delivered.
     *
     * @param floor the floor
     * @param count the count
     */
    public void incrementCountOfPeopleDelivered(int floor, int count) {

        if (countOfPeopleDelivered.get(floor) == null) {
            countOfPeopleDelivered.put(floor, 0);
        }
        countOfPeopleDelivered.put(floor, countOfPeopleDelivered.get(floor) + count);
    }

    /**
     * Gets count of people waiting on floor.
     *
     * @param floor the floor
     * @return the count of people waiting on floor
     */
    public int getCountOfPeopleWaitingOnFloor(Integer floor) {

        return floorInfo.get(floor).size();
    }

    /**
     * Take people from floor.
     *
     * @param floor the floor
     * @param personCount the person count
     * @return the list
     */
    public synchronized List<Person> takePeopleFromFloor(int floor, int personCount) {

        if (floorInfo.get(floor) == null) {
            return new ArrayList<Person>();
        }

        List<Person> persons = new CopyOnWriteArrayList<Person>(floorInfo.get(floor).subList(0, Math.min(floorInfo.get(floor).size(), personCount)));
        for (Person p : persons) {
            floorInfo.get(floor).remove(p);
        }
        return persons;
    }

    /**
     * Add person on floor.
     *
     * @param floor the floor
     * @param personCount the person count
     * @param destinationFloor the destination floor
     */
    public void addPersonOnFloor(int floor, int personCount, int destinationFloor) {

        if (!floorInfo.containsKey(floor)) {
            floorInfo.put(floor, new CopyOnWriteArrayList<Person>());
        }
        for (int i = 0; i < personCount; i++) {
            floorInfo.get(floor).add(new Person(floor, destinationFloor));
        }
    }

    @Override
    public String toString() {

        StringBuilder f = new StringBuilder();
        f.append("People waiting [");
        for (Integer i : floorInfo.keySet()) {
            f.append("{" + i + ":" + floorInfo.get(i).size() + "},");
        }
        f.append("]");

        return "Floor{" +
                "floorInfo=" + f.toString() +
                ", countOfPeopleDelivered=" + countOfPeopleDelivered +
                '}';
    }
}