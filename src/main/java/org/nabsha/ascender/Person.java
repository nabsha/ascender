package org.nabsha.ascender;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class Person {
    private int startLevel;
    private int destinationLevel;

    /**
     * Instantiates a new Person.
     *
     * @param startLevel the start level
     * @param destinationLevel the destination level
     */
    public Person(int startLevel, int destinationLevel) {
        this.startLevel = startLevel;
        this.destinationLevel = destinationLevel;
    }

    /**
     * Gets start level.
     *
     * @return the start level
     */
    public int getStartLevel() {
        return startLevel;
    }

    /**
     * Sets start level.
     *
     * @param startLevel the start level
     */
    public void setStartLevel(int startLevel) {
        this.startLevel = startLevel;
    }

    /**
     * Gets destination level.
     *
     * @return the destination level
     */
    public int getDestinationLevel() {
        return destinationLevel;
    }

    /**
     * Sets destination level.
     *
     * @param destinationLevel the destination level
     */
    public void setDestinationLevel(int destinationLevel) {
        this.destinationLevel = destinationLevel;
    }
}
