package com.practice.data.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.practice.di.components.DaggerTrendingReposViewModelComponents;
import com.practice.di.modules.TrendingRepoRepositoryModule;
import com.practice.utils.asynctasks.SortingAsyncTask;
import com.practice.utils.comparators.SortByNameComparator;
import com.practice.utils.comparators.SortByStarsComparator;
import com.practice.data.models.Repository;
import com.practice.data.repositories.TrendingRepoRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * View model responsible for ui data over TrendingReposActivity.
 * It is currently housing TrendingRepoRepository which is doing
 * the heavy lifting of Network operation.
 */
public class TrendingReposViewModel extends ViewModel {

    private static final String TAG = "TrendingReposViewModel";

    // Data provider for trending repos.
    @Inject TrendingRepoRepository mRepository;

    // Comparator for Star based sorting
    @Inject SortByStarsComparator sortByStars;

    // Comparator for name based sorting
    @Inject SortByNameComparator sortByName;

    // Currently selected repository reference.
    // It is getting set via clicking over list item.
    // It gets unset once you tap on same item again.
    private Repository mSelectedRepository;

    // Constructor
    @Inject
    public TrendingReposViewModel(String baseUrl) {

        // Inject dependencies
        DaggerTrendingReposViewModelComponents
                .builder()
                .trendingRepoRepositoryModule(new TrendingRepoRepositoryModule(baseUrl))
                .build()
                .inject(this);

        // Fetch the data as soon as possible.
        fetchTrendingRepo(false);
    }

    /**
     * Get the livedata instance carrying the trending repo list.
     */
    public MutableLiveData<List<Repository>> getTrendingRepos() {
        return mRepository.mTrendingRepoList;
    }

    /**
     * Return selected Repository reference.
     */
    public Repository getSelectedRepository() {
        return mSelectedRepository;
    }

    /**
     * Sets the current selected repository.
     * @param mSelectedRepository to be selected Repository
     */
    public void setSelectedRepository(Repository mSelectedRepository) {
        this.mSelectedRepository = mSelectedRepository;
    }

    /**
     * Tells if doing background operation of loading data.
     * @return tells if this model is loading results.
     */
    public MutableLiveData<Boolean> isLoading() {
        return mRepository.mIsLoading;
    }

    /**
     * Load trending repos from origin/cloud.
     * It can return cached response in case cache-control header is set.
     * use ifForced to bypass that.
     * @param isForced Ignore disk cache
     */
    public void fetchTrendingRepo(boolean isForced) {
        mRepository.fetchTrendingRepo(isForced);
    }

    /**
     * Sorts the list data on star count basis for each repo.
     */
    public void sortRepoListByStars() {
        new SortingAsyncTask(getTrendingRepos()).execute(sortByStars);
    }

    /**
     * Sorts the list data on name basis for each repo.
     */
    public void sortRepoListByName() {
        new SortingAsyncTask(getTrendingRepos()).execute(sortByName);
    }

}
