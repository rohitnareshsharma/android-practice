package com.practice.di.modules;

import com.practice.data.repositories.TrendingRepoRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Module responsible for TrendingRepoRepository creation via dependency injection.
 */
@Module
public class TrendingRepoRepositoryModule {

    private String mBaseUrl;

    public TrendingRepoRepositoryModule(String baseURL) {
        mBaseUrl = baseURL;
    }

    @Provides
    public TrendingRepoRepository getRepository() {
       return new TrendingRepoRepository(mBaseUrl);
    }
}
