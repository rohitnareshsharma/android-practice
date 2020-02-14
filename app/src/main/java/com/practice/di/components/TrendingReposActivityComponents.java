package com.practice.di.components;

import com.practice.di.modules.TrendingReposListAdapterModule;
import com.practice.di.modules.TrendingReposViewModelModule;
import com.practice.ui.activities.TrendingReposActivity;

import dagger.Component;

/**
 * Interface responsible for initialising TrendingReposActivity
 *
 * All @Inject annotations will be initialised.
 */
@Component(modules = {TrendingReposViewModelModule.class, TrendingReposListAdapterModule.class})
public interface TrendingReposActivityComponents {

    void inject(TrendingReposActivity trendingReposActivity);

}
