package org.nabsha.ascender.domain.elevator;


import org.nabsha.ascender.domain.actors.Person;

import javax.persistence.*;
import java.util.List;
import java.util.TreeSet;

/**
 * The type Elevator model.
 */
@Entity
@Table(name = "ELEVATOR_EVENTS")
public class ElevatorModel {

    @Id
    @GeneratedValue
    @Column(name = "EVENT_ID")
    private long uuid;

    @Column(name = "ELEVATOR_ID")
    private String elevatorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "DIRECTION")
    private Direction direction;

    @Column(name = "CURRENT_LEVEL")
    private int currentLevel;

    @Transient
    private List<Person> persons;

    @Column(name = "NEXT_STOP")
    private Integer nextStop = -1;

    @Transient
    private TreeSet<Integer> nextStopSet;

    /**
     * Instantiates a new Elevator model.
     */
    public ElevatorModel() {

    }

    public long getUuid() {

        return uuid;
    }

    public void setUuid(long uuid) {

        this.uuid = uuid;
    }

    /**
     * Gets elevator id.
     *
     * @return the elevator id
     */
    public String getElevatorId() {

        return elevatorId;
    }

    /**
     * Sets elevator id.
     *
     * @param elevatorId the elevator id
     */
    public void setElevatorId(String elevatorId) {

        this.elevatorId = elevatorId;
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */

    public Direction getDirection() {

        return direction;
    }

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    public void setDirection(Direction direction) {

        this.direction = direction;
    }

    /**
     * Gets current level.
     *
     * @return the current level
     */

    public int getCurrentLevel() {

        return currentLevel;
    }

    /**
     * Sets current level.
     *
     * @param currentLevel the current level
     */
    public void setCurrentLevel(int currentLevel) {

        this.currentLevel = currentLevel;
    }

    /**
     * Gets persons.
     *
     * @return the persons
     */
    public List<Person> getPersons() {

        return persons;
    }

    /**
     * Sets persons.
     *
     * @param persons the persons
     */
    public void setPersons(List<Person> persons) {

        this.persons = persons;
    }

    /**
     * Gets next stop.
     *
     * @return the next stop
     */

    public Integer getNextStop() {

        return nextStop;
    }

    /**
     * Sets next stop.
     *
     * @param nextStop the next stop
     */
    public void setNextStop(Integer nextStop) {

        this.nextStop = nextStop;
    }

    public String getNextStopSetString() {

        return nextStopSet.toString();
    }

    /**
     * Gets next stop set.
     *
     * @return the next stop set
     */
    public TreeSet<Integer> getNextStopSet() {

        return nextStopSet;
    }

    /**
     * Sets next stop set.
     *
     * @param nextStopSet the next stop set
     */
    public void setNextStopSet(TreeSet<Integer> nextStopSet) {

        this.nextStopSet = nextStopSet;
    }

    @Override
    public synchronized String toString() {

        return "ElevatorModel{" +
                "elevatorId='" + elevatorId + '\'' +
                ", direction=" + direction +
                ", currentLevel=" + currentLevel +
                ", persons=" + persons +
                ", nextStop=" + nextStop +
                ", nextStopSet=" + nextStopSet +
                '}';
    }
}