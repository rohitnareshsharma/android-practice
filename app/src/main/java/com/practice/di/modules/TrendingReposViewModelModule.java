package com.practice.di.modules;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.practice.data.viewmodels.TrendingReposViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Module responsible for TrendingReposViewModel creation via dependency injection.
 */
@Module
public class TrendingReposViewModelModule {

    private ViewModelStoreOwner mOwner;

    private String mBaseUrl;

    public TrendingReposViewModelModule(ViewModelStoreOwner owner, String baseUrl) {
        mOwner = owner;
        mBaseUrl = baseUrl;
    }

    @Provides
    TrendingReposViewModel providesModel() {
        return new ViewModelProvider(mOwner,
               new TrendingRepoViewModelFactory(mBaseUrl)).get(TrendingReposViewModel.class);
    }

    public static class TrendingRepoViewModelFactory implements ViewModelProvider.Factory {

        private String mBaseUrl;

        TrendingRepoViewModelFactory(String baseUrl) {
            mBaseUrl = baseUrl;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new TrendingReposViewModel(mBaseUrl);
        }
    }

}
