package com.practice.utils.comparators;

import com.practice.data.models.Repository;

import java.util.Comparator;

import javax.inject.Inject;

/**
 * Allows sorting of Repository on name basis.
 */
public class SortByNameComparator implements Comparator<Repository> {

    private static final String TAG = "SortByNameComparator";

    @Inject
    public SortByNameComparator() {
        // Default Constructor
    }

    @Override
    public int compare(Repository r1, Repository r2) {
        if (r1 == r2) return 0;
        if (r1 == null || r1.name == null) return -1;
        if (r2 == null || r2.name == null) return 1;

        return r1.name.compareTo(r2.name);
    }
}
