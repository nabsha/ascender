package org.nabsha.ascender.domain.elevator;

/**
 * Created by Nabeel Shaheen on 1/08/2015.
 */
public enum Direction {
    /**
     * The UPWARDS.
     */
    UPWARDS("^", 1), /**
     * The DOWNWARDS.
     */
    DOWNWARDS("\\/", -1), /**
     * The STATIONARY.
     */
    STATIONARY("-",0);

    /**
     * The Alias.
     */
    String alias;

    /**
     * The Step.
     */
    int step;

    /**
     * Instantiates a new Direction.
     *
     * @param alias the alias
     * @param step the step
     */
    Direction(String alias, int step) {
        this.alias = alias;
        this.step = step;
    }
}
