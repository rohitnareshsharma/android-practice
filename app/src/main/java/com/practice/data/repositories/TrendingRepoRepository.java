package com.practice.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.easyvolley.Callback;
import com.easyvolley.EasyVolleyError;
import com.easyvolley.EasyVolleyResponse;
import com.easyvolley.NetworkClient;
import com.easyvolley.NetworkPolicy;
import com.practice.BuildConfig;
import com.practice.data.models.Repository;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

/**
 * Component responsible for fetching trending repo data from
 * network. It provides live data object for observing the events
 * or loading situations.
 */
public class TrendingRepoRepository {

    // For logging purpose
    private static final String TAG = "TrendingRepoRepository";

    // key in intents to pass base url
    public static String EXTRA_BASE_URL = "EXTRA_BASE_URL";

    // Trending repo endpoint
    private static final String TRENDING_REPO_ENDPOINT = "/repositories?";

    // Baseurl of network request. Use TrendingRepoRepository(String baseUrl)
    // in case you want to override the default base url.
    private String mBaseUrl = BuildConfig.DEFAULT_BASE_URL;

    public MutableLiveData<List<Repository>> mTrendingRepoList = new MutableLiveData<>();

    public MutableLiveData<Boolean> mIsLoading = new MutableLiveData<Boolean>(){{
        setValue(false);
    }};

    /**
     * Construct TrendingRepoRepository object with overriding the
     * base url. This is critical for Unit testing to use this exclusively.
     * @param baseUrl
     */
    @Inject
    public TrendingRepoRepository(String baseUrl) {
        if(baseUrl != null) {
            mBaseUrl = baseUrl;
        }
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    /**
     * Fetch trending repos from network.
     *
     * @param isForced flag to ignore disk cache read. Result will be cached
     */
    public void fetchTrendingRepo(boolean isForced) {
        fetchTrendingRepo(isForced, null);
    }

    /**
     *
     * @param isForced flag to ignore disk cache read. Result will be cached
     * @param latch CountdownLatch object to observe operation completion.
     */
    public void fetchTrendingRepo(boolean isForced, CountDownLatch latch) {
        mIsLoading.setValue(true);
        NetworkClient.get(mBaseUrl + TRENDING_REPO_ENDPOINT)
                .setMaxNumRetries(3) // Let the retry be there in case of network issues
                .setNetworkPolicy(isForced ? NetworkPolicy.IGNORE_READ_BUT_WRITE_CACHE : NetworkPolicy.DEFAULT)
                .setCallback(new Callback<List<Repository>>() {
                    @Override
                    public void onSuccess(List<Repository> repositories, EasyVolleyResponse response) {
                        Log.d(TAG, "Network call to fetch trending repo is successful");
                        mTrendingRepoList.setValue(repositories);
                        mIsLoading.setValue(false);

                        if(latch != null) {
                            latch.countDown();
                        }
                    }

                    @Override
                    public void onError(EasyVolleyError error) {
                        Log.e(TAG, "Network call to fetch trending repo failed");
                        mIsLoading.setValue(false);

                        if(latch != null) {
                            latch.countDown();
                        }
                    }
                }).execute();
    }
}
