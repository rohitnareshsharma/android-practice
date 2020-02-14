package com.practice.di.components;

import com.practice.data.viewmodels.TrendingReposViewModel;
import com.practice.di.modules.TrendingRepoRepositoryModule;

import dagger.Component;

/**
 * Interface responsible for initialising dependencies of TrendingReposViewModel
 * Especially TrendingRepoRepository
 *
 * All @Inject annotations will be initialised.
 */
@Component(modules = TrendingRepoRepositoryModule.class)
public interface TrendingReposViewModelComponents {

    void inject(TrendingReposViewModel model);

}
