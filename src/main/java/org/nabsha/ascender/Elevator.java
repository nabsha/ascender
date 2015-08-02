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
    private static Logger LOG = LoggerFactory.getLogger(Elevator.class);

    private static final int MAX_NUMBER_OF_PERSONS_IN_ELEVATOR = 20;

    private boolean running = true;

    private static final int MAX_ELEVATOR_CAPACITY = 20;

    private String elevatorId;

    private Direction direction;

    private int currentLevel;

    private int minLevel;

    private int maxLevel;

    private List<Person> persons;

    private Integer nextStop;

    private TreeSet<Integer> nextStopSet;

    private LinkedBlockingQueue<Integer> incomingOrderList = new LinkedBlockingQueue<Integer>();

    public Elevator(String elevatorId, int minLevel, int maxLevel) {
        this.elevatorId = elevatorId;
        this.direction = Direction.STATIONARY;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.currentLevel = this.minLevel;
        this.nextStopSet = new TreeSet<Integer>();
        this.persons = new ArrayList<Person>(MAX_NUMBER_OF_PERSONS_IN_ELEVATOR);
    }

    public void addOrder(Integer level) {
        nextStopSet.add(level);
    }

    @Override
    public String toString() {
        return "Elevator{" +
                "running=" + running +
                ", elevatorId='" + elevatorId + '\'' +
                ", direction=" + direction +
                ", currentLevel=" + currentLevel +
                ", personsCount=" + persons.size() +
                ", nextStop=" + nextStop +
                ", nextStopSet=" + nextStopSet +
                ", incomingOrderList=" + incomingOrderList +
                '}';
    }

    public void run() {
        while (this.isRunning()) {
            do {
                checkIncomingOrder();
                this.nextStop = checkNextStop(this.currentLevel, this.direction);
                System.out.println(this);

                this.direction = calculateDirection(this.currentLevel, this.nextStop);

                movingToNextLevel();
                this.currentLevel += this.direction.step;
            } while (this.nextStop != this.currentLevel);

            unloadPeople();

            loadPeople();

            this.nextStopSet.remove(this.currentLevel);
            if (this.nextStopSet.isEmpty()) {
                this.direction = Direction.STATIONARY;
                this.nextStop = -1;
            }
            System.out.println(this);
        }
    }

    private void loadPeople() {
        List<Person> personFromFloor = Floor.getInstance().takePeopleFromFloor(currentLevel, MAX_ELEVATOR_CAPACITY - persons.size());
        persons.addAll(personFromFloor);
        for (Person p : personFromFloor) {
            if (!nextStopSet.contains(p.getDestinationLevel())) {
                addOrder(p.getDestinationLevel());
            }
        }
    }


    private void unloadPeople() {
        if (this.getPersons().isEmpty())
            return;

        List<Person> removablePersons = new ArrayList<Person>();
        for (Person person : persons) {
            if (person.getDestinationLevel() == this.currentLevel) {
                removablePersons.add(person);
                Floor.getInstance().incrementCountOfPeopleDelivered(currentLevel, 1);
            }
        }
        persons.removeAll(removablePersons);

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
                stopList = nextStopSet.tailSet(currentLevel);
                if (!stopList.isEmpty())
                    nextStop = stopList.first();
                else
                    nextStop = nextStopSet.first();
                break;
            }
            case DOWNWARDS: {
                stopList = nextStopSet.headSet(currentLevel);
                if (!stopList.isEmpty())
                    nextStop = stopList.last();
                else
                    nextStop = nextStopSet.last();
                break;
            }
            default:
                stopList = nextStopSet;
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
        if (this.nextStopSet.isEmpty()) {
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
        return elevatorId;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public Integer getNextStop() {
        return nextStop;
    }

    public void setNextStop(Integer nextStop) {
        this.nextStop = nextStop;
    }

    public TreeSet<Integer> getNextStopSet() {
        return nextStopSet;
    }

    public void setNextStopSet(TreeSet<Integer> nextStopSet) {
        this.nextStopSet = nextStopSet;
    }

    public LinkedBlockingQueue<Integer> getIncomingOrderList() {
        return incomingOrderList;
    }

    public void setIncomingOrderList(LinkedBlockingQueue<Integer> incomingOrderList) {
        this.incomingOrderList = incomingOrderList;
    }
}
