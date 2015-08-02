package org.nabsha.ascender;

/**
 * Created by Nabeel Shaheen on 1/08/2015.
 */
public enum Direction {
    UPWARDS("^", 1), DOWNWARDS("\\/", -1), STATIONARY("-",0);

    String alias;
    int step;
    Direction(String alias, int step) {
        this.alias = alias;
        this.step = step;
    }
}
