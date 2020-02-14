package com.practice.utils.comparators;

import com.practice.data.models.Repository;

import java.util.Comparator;

import javax.inject.Inject;

/**
 * Allows sorting of Repository on number of star basis.
 */
public class SortByStarsComparator implements Comparator<Repository> {

    private static final String TAG = "SortByStarsComparator";

    @Inject
    public SortByStarsComparator() {
        // Default Constructor
    }

    @Override
    public int compare(Repository r1, Repository r2) {
        if (r1 == r2) return 0;
        if (r1 == null) return -1;
        if (r2 == null) return +1;

        int r1Stars;
        int r2Stars;

        try {
            r1Stars = Integer.parseInt(r1.stars);
        } catch (NumberFormatException nfe) {
            return -1;
        }

        try {
            r2Stars = Integer.parseInt(r2.stars);
        } catch (NumberFormatException nfe) {
            return +1;
        }

        // For descending order
        return r2Stars - r1Stars;
    }
}
