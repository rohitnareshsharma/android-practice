package com.practice;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.practice.data.viewmodels.TrendingReposViewModel;
import com.practice.di.components.DaggerTrendingReposViewModelComponents;
import com.practice.di.modules.TrendingRepoRepositoryModule;
import com.practice.di.modules.TrendingReposViewModelModule;

import dagger.Module;
import dagger.Provides;

/**
 * This is not used. I need to debug few di pieces before i can proceed
 * using this.
 */
@Module
public class MockTrendingReposViewModelModule extends TrendingReposViewModelModule {

    private ViewModelStoreOwner mOwner;

    private String mMockBaseUrl;

    public MockTrendingReposViewModelModule(ViewModelStoreOwner owner, String mockBaseUrl) {
        super(owner, mockBaseUrl);
        mOwner = owner;
        mMockBaseUrl = mockBaseUrl;
    }

    @Provides
    TrendingReposViewModel providesModel() {

        TrendingReposViewModel model = new ViewModelProvider(mOwner).get(TrendingReposViewModel.class);

        // Lets inject our mock repository to it.
        DaggerTrendingReposViewModelComponents.builder()
                .trendingRepoRepositoryModule(new TrendingRepoRepositoryModule(mMockBaseUrl))
                .build()
                .inject(model);

        return model;

    }
}
