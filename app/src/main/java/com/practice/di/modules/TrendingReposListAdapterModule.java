package com.practice.di.modules;

import android.view.LayoutInflater;

import com.practice.ui.adapters.TrendingRepoListAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Module responsible for TrendingRepoListAdapter creation via dependency injection
 */
@Module
public class TrendingReposListAdapterModule {

    private LayoutInflater mInflater;

    public TrendingReposListAdapterModule(LayoutInflater inflater) {
        mInflater = inflater;
    }

    @Provides
    public TrendingRepoListAdapter getAdapter() {
        return new TrendingRepoListAdapter(mInflater);
    }

}
