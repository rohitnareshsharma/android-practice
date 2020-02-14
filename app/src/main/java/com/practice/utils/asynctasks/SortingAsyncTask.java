package com.practice.utils.asynctasks;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.practice.data.models.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * AsyncTask allowing sorting of List<Repository>.
 * This should be memory leak free. Need to test in profiler TODO
 */
public class SortingAsyncTask extends AsyncTask<Comparator<Repository>, Void, List<Repository>> {

    /**
     * LiveData instance to post result onto once done.
     */
    private MutableLiveData<List<Repository>> mTrendingRepoList;

    public SortingAsyncTask(MutableLiveData<List<Repository>> trendingRepoList) {
        mTrendingRepoList = trendingRepoList;
    }

    @SafeVarargs
    @Override
    protected final List<Repository> doInBackground(final Comparator<Repository>... params) {
        List<Repository> repositories = mTrendingRepoList.getValue();
        if(repositories != null) {
            Collections.sort(repositories, params[0]);
        }
        return repositories;
    }

    @Override
    protected void onPostExecute(List<Repository> repositories) {
        super.onPostExecute(repositories);
        mTrendingRepoList.setValue(repositories);
    }
}