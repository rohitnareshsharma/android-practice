package com.practice;

import com.practice.utils.comparators.SortByStarsComparator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.practice.data.models.Repository;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SortByStarsComparatorTest {

    @Test
    public void checkSortByStarsComparator() {

        // Create the mock data
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Type type = new TypeToken<ArrayList<Repository>>() {}.getType();
        List<Repository> list = gson.fromJson(TrendingRepoRepositoryTest.getTrendingRepoMockResponseString(), type);

        // Exercise our app code now
        Collections.sort(list, new SortByStarsComparator());

        // Now lets iterate over the list and see stars are in descending order
        int stars = Integer.MAX_VALUE;
        for (Repository repository : list) {
            int repoStar = Integer.parseInt(repository.stars);
            if (repoStar > stars) {
                assert false;
            }
            stars = repoStar;
        }

    }
}
