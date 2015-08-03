package org.nabsha.ascender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Nabeel Shaheen on 1/08/2015.
 */
public class Elevator implements Runnable {

    /**
     * The constant MAX_ELEVATOR_CAPACITY.
     */
    public static final int MAX_ELEVATOR_CAPACITY = 20;

    private static Logger LOG = LoggerFactory.getLogger(Elevator.class);

    private final ElevatorModel elevatorModel = new ElevatorModel();

    private Thread elevatorRequestHandlerThread;

    private boolean running = true;

    private int minLevel;

    private ElevatorRequestHandler elevatorRequestHandler;

    private int maxLevel;

    /**
     * Instantiates a new Elevator.
     *
     * @param elevatorId the elevator id
     * @param minLevel the min level
     * @param maxLevel the max level
     */
    public Elevator(String elevatorId, int minLevel, int maxLevel) {

        this.elevatorModel.setElevatorId(elevatorId);
        this.elevatorModel.setDirection(Direction.STATIONARY);
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.elevatorModel.setCurrentLevel(this.minLevel);
        this.elevatorModel.setNextStopSet(new TreeSet<Integer>());
        this.elevatorModel.setPersons(new CopyOnWriteArrayList<Person>());
        this.elevatorModel.setNextStop(-1);
        elevatorRequestHandler = new ElevatorRequestHandler(this);
        elevatorRequestHandlerThread = new Thread(elevatorRequestHandler);
        elevatorRequestHandlerThread.start();
    }

    /**
     * Gets elevator model.
     *
     * @return the elevator model
     */
    public ElevatorModel getElevatorModel() {

        return elevatorModel;
    }

    /**
     * Gets elevator request handler.
     *
     * @return the elevator request handler
     */
    public ElevatorRequestHandler getElevatorRequestHandler() {

        return elevatorRequestHandler;
    }

    /**
     * Add order.
     *
     * @param level the level
     */
    public synchronized void addOrder(Integer level) {

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
                '}';
    }

    public void run() {

        while (this.isRunning()) {
            if (this.elevatorModel.getNextStopSet().isEmpty()) {
                waitForOrders(100);
                continue;
            }

            synchronized (elevatorModel) {
                while (this.elevatorModel.getNextStop() != this.elevatorModel.getCurrentLevel()) {
                    this.elevatorModel.setNextStop(checkNextStop(this.elevatorModel.getCurrentLevel(), this.elevatorModel.getDirection()));
                    System.out.println(this);
                    this.elevatorModel.setDirection(calculateDirection(this.elevatorModel.getCurrentLevel(), this.elevatorModel.getNextStop()));

                    movingToNextLevel();
                    this.elevatorModel.setCurrentLevel(this.elevatorModel.getCurrentLevel() + this.elevatorModel.getDirection().step);
                }
            }
            unloadPeople();

            loadPeople();

            removeVisitedStop();
        }

        //stopping request handler thread
        elevatorRequestHandler.setRunning(false);
    }

    private void removeVisitedStop() {

        synchronized (elevatorModel) {
            this.elevatorModel.getNextStopSet().remove(this.elevatorModel.getCurrentLevel());
            if (this.elevatorModel.getNextStopSet().isEmpty()) {
                this.elevatorModel.setDirection(Direction.STATIONARY);
                this.elevatorModel.setNextStop(-1);
            }
        }
    }

    private void waitForOrders(int timeout) {

        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadPeople() {

        synchronized (elevatorModel) {
            List<Person> personFromFloor = Floor.getInstance().takePeopleFromFloor(elevatorModel.getCurrentLevel(), MAX_ELEVATOR_CAPACITY - elevatorModel.getPersons().size());
            elevatorModel.getPersons().addAll(personFromFloor);
            for (Person p : personFromFloor) {
                if (!elevatorModel.getNextStopSet().contains(p.getDestinationLevel())) {
                    addOrder(p.getDestinationLevel());
                }
            }
        }
    }


    private void unloadPeople() {

        if (this.getPersons().isEmpty())
            return;

        synchronized (elevatorModel) {
            List<Person> removablePersons = new CopyOnWriteArrayList<Person>();
            for (Person person : elevatorModel.getPersons()) {
                if (person.getDestinationLevel() == this.elevatorModel.getCurrentLevel()) {
                    removablePersons.add(person);
                    Floor.getInstance().incrementCountOfPeopleDelivered(elevatorModel.getCurrentLevel(), 1);
                }
            }
            elevatorModel.getPersons().removeAll(removablePersons);
        }
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
        synchronized (elevatorModel) {
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

    /**
     * Sets running.
     *
     * @param running the running
     */
    public void setRunning(boolean running) {

        this.running = running;
    }

    /**
     * Gets elevator id.
     *
     * @return the elevator id
     */
    public String getElevatorId() {

        return elevatorModel.getElevatorId();
    }

    /**
     * Sets elevator id.
     *
     * @param elevatorId the elevator id
     */
    public void setElevatorId(String elevatorId) {

        this.elevatorModel.setElevatorId(elevatorId);
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public Direction getDirection() {

        return elevatorModel.getDirection();
    }

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    public void setDirection(Direction direction) {

        this.elevatorModel.setDirection(direction);
    }

    /**
     * Gets current level.
     *
     * @return the current level
     */
    public int getCurrentLevel() {

        return elevatorModel.getCurrentLevel();
    }

    /**
     * Sets current level.
     *
     * @param currentLevel the current level
     */
    public void setCurrentLevel(int currentLevel) {

        this.elevatorModel.setCurrentLevel(currentLevel);
    }

    /**
     * Gets persons.
     *
     * @return the persons
     */
    public List<Person> getPersons() {

        return elevatorModel.getPersons();
    }

    /**
     * Sets persons.
     *
     * @param persons the persons
     */
    public void setPersons(List<Person> persons) {

        this.elevatorModel.setPersons(persons);
    }

    /**
     * Gets next stop.
     *
     * @return the next stop
     */
    public Integer getNextStop() {

        return elevatorModel.getNextStop();
    }

    /**
     * Sets next stop.
     *
     * @param nextStop the next stop
     */
    public void setNextStop(Integer nextStop) {

        this.elevatorModel.setNextStop(nextStop);
    }

    /**
     * Gets next stop set.
     *
     * @return the next stop set
     */
    public TreeSet<Integer> getNextStopSet() {

        return elevatorModel.getNextStopSet();
    }

    /**
     * Sets next stop set.
     *
     * @param nextStopSet the next stop set
     */
    public void setNextStopSet(TreeSet<Integer> nextStopSet) {

        this.elevatorModel.setNextStopSet(nextStopSet);
    }


}
