package org.nabsha.ascender;

import java.util.List;
import java.util.TreeSet;

public class ElevatorModel {
    private String elevatorId;
    private Direction direction;
    private int currentLevel;
    private List<Person> persons;
    private Integer nextStop;
    private TreeSet<Integer> nextStopSet;

    public ElevatorModel() {
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
}