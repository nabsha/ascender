package org.nabsha.ascender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nabeel Shaheen on 1/08/2015.
 */
public class Elevator implements Runnable {
    private static final int MAX_NUMBER_OF_PERSONS_IN_ELEVATOR = 20;
    private static final int MAX_ELEVATOR_CAPACITY = 20;
    private static Logger LOG = LoggerFactory.getLogger(Elevator.class);
    private final ElevatorModel elevatorModel = new ElevatorModel();
    private boolean running = true;
    private int minLevel;

    private int maxLevel;

    private LinkedBlockingQueue<Integer> incomingOrderList = new LinkedBlockingQueue<Integer>();

    public Elevator(String elevatorId, int minLevel, int maxLevel) {
        this.elevatorModel.setElevatorId(elevatorId);
        this.elevatorModel.setDirection(Direction.STATIONARY);
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.elevatorModel.setCurrentLevel(this.minLevel);
        this.elevatorModel.setNextStopSet(new TreeSet<Integer>());
        this.elevatorModel.setPersons(new ArrayList<Person>(MAX_NUMBER_OF_PERSONS_IN_ELEVATOR));
    }

    public void addOrder(Integer level) {
        elevatorModel.getNextStopSet().add(level);
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "running=" + running +
                ", elevatorId='" + elevatorModel.getElevatorId() + '\'' +
                ", direction=" + elevatorModel.getDirection() +
                ", currentLevel=" + elevatorModel.getCurrentLevel() +
                ", personsCount=" + elevatorModel.getPersons().size() +
                ", nextStop=" + elevatorModel.getNextStop() +
                ", nextStopSet=" + elevatorModel.getNextStopSet() +
                ", incomingOrderList=" + incomingOrderList +
                '}';
    }

    public void run() {
        while (this.isRunning()) {
            do {
                checkIncomingOrder();
                this.elevatorModel.setNextStop(checkNextStop(this.elevatorModel.getCurrentLevel(), this.elevatorModel.getDirection()));
                System.out.println(this);

                this.elevatorModel.setDirection(calculateDirection(this.elevatorModel.getCurrentLevel(), this.elevatorModel.getNextStop()));

                movingToNextLevel();
                this.elevatorModel.setCurrentLevel(this.elevatorModel.getCurrentLevel() + this.elevatorModel.getDirection().step);
            } while (this.elevatorModel.getNextStop() != this.elevatorModel.getCurrentLevel());

            unloadPeople();

            loadPeople();

            this.elevatorModel.getNextStopSet().remove(this.elevatorModel.getCurrentLevel());
            if (this.elevatorModel.getNextStopSet().isEmpty()) {
                this.elevatorModel.setDirection(Direction.STATIONARY);
                this.elevatorModel.setNextStop(-1);
            }
            System.out.println(this);
        }
    }

    private void loadPeople() {
        List<Person> personFromFloor = Floor.getInstance().takePeopleFromFloor(elevatorModel.getCurrentLevel(), MAX_ELEVATOR_CAPACITY - elevatorModel.getPersons().size());
        elevatorModel.getPersons().addAll(personFromFloor);
        for (Person p : personFromFloor) {
            if (!elevatorModel.getNextStopSet().contains(p.getDestinationLevel())) {
                addOrder(p.getDestinationLevel());
            }
        }
    }


    private void unloadPeople() {
        if (this.getPersons().isEmpty())
            return;

        List<Person> removablePersons = new ArrayList<Person>();
        for (Person person : elevatorModel.getPersons()) {
            if (person.getDestinationLevel() == this.elevatorModel.getCurrentLevel()) {
                removablePersons.add(person);
                Floor.getInstance().incrementCountOfPeopleDelivered(elevatorModel.getCurrentLevel(), 1);
            }
        }
        elevatorModel.getPersons().removeAll(removablePersons);

    }

    private void movingToNextLevel() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Integer checkNextStop(int currentLevel, Direction currDirection) {
        SortedSet<Integer> stopList;
        Integer nextStop = -1;
        switch (currDirection) {
            case UPWARDS: {
                stopList = elevatorModel.getNextStopSet().tailSet(currentLevel);
                if (!stopList.isEmpty())
                    nextStop = stopList.first();
                else
                    nextStop = elevatorModel.getNextStopSet().first();
                break;
            }
            case DOWNWARDS: {
                stopList = elevatorModel.getNextStopSet().headSet(currentLevel);
                if (!stopList.isEmpty())
                    nextStop = stopList.last();
                else
                    nextStop = elevatorModel.getNextStopSet().last();
                break;
            }
            default:
                stopList = elevatorModel.getNextStopSet();
                nextStop = findNearestLevel(stopList, currentLevel);
        }
        return nextStop;
    }

    private Integer findNearestLevel(SortedSet<Integer> stopList, int currentLevel) {
        SortedSet<Integer> lessThanList = stopList.headSet(currentLevel);
        SortedSet<Integer> greaterThanList = stopList.tailSet(currentLevel);

        if (lessThanList.isEmpty() && greaterThanList.isEmpty())
            return currentLevel;

        if (lessThanList.isEmpty())
            return greaterThanList.first();

        if (greaterThanList.isEmpty()) {
            return lessThanList.last();
        }

        return Math.min(currentLevel - lessThanList.last(), greaterThanList.first() - currentLevel);
    }

    private void checkIncomingOrder() {
        Integer order = null;
        if (this.elevatorModel.getNextStopSet().isEmpty()) {
            while (this.isRunning()) {

                try {
                    order = this.incomingOrderList.poll(1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                if (order != null) {
                    addOrder(order);
                    break;
                }
            }
        } else {
            while ((order = this.incomingOrderList.poll()) != null) {
                addOrder(order);
            }
        }

    }

    private Direction calculateDirection(int sourceLevel, int destinationLevel) {
        if (sourceLevel == destinationLevel) {
            return Direction.STATIONARY;
        } else if (sourceLevel > destinationLevel) {
            return Direction.DOWNWARDS;
        } else {
            return Direction.UPWARDS;
        }
    }

    private boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getElevatorId() {
        return elevatorModel.getElevatorId();
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorModel.setElevatorId(elevatorId);
    }

    public Direction getDirection() {
        return elevatorModel.getDirection();
    }

    public void setDirection(Direction direction) {
        this.elevatorModel.setDirection(direction);
    }

    public int getCurrentLevel() {
        return elevatorModel.getCurrentLevel();
    }

    public void setCurrentLevel(int currentLevel) {
        this.elevatorModel.setCurrentLevel(currentLevel);
    }

    public List<Person> getPersons() {
        return elevatorModel.getPersons();
    }

    public void setPersons(List<Person> persons) {
        this.elevatorModel.setPersons(persons);
    }

    public Integer getNextStop() {
        return elevatorModel.getNextStop();
    }

    public void setNextStop(Integer nextStop) {
        this.elevatorModel.setNextStop(nextStop);
    }

    public TreeSet<Integer> getNextStopSet() {
        return elevatorModel.getNextStopSet();
    }

    public void setNextStopSet(TreeSet<Integer> nextStopSet) {
        this.elevatorModel.setNextStopSet(nextStopSet);
    }

    public LinkedBlockingQueue<Integer> getIncomingOrderList() {
        return incomingOrderList;
    }

    public void setIncomingOrderList(LinkedBlockingQueue<Integer> incomingOrderList) {
        this.incomingOrderList = incomingOrderList;
    }
}
