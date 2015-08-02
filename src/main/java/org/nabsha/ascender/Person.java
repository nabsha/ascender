package org.nabsha.ascender;

/**
 * Created by Nabeel Shaheen on 2/08/2015.
 */
public class Person {
    private int startLevel;
    private int destinationLevel;

    public Person(int startLevel, int destinationLevel) {
        this.startLevel = startLevel;
        this.destinationLevel = destinationLevel;
    }

    public int getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(int startLevel) {
        this.startLevel = startLevel;
    }

    public int getDestinationLevel() {
        return destinationLevel;
    }

    public void setDestinationLevel(int destinationLevel) {
        this.destinationLevel = destinationLevel;
    }
}
