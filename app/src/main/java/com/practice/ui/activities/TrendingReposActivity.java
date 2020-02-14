package com.practice.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.practice.R;
import com.practice.data.repositories.TrendingRepoRepository;
import com.practice.di.components.DaggerTrendingReposActivityComponents;
import com.practice.di.modules.TrendingReposListAdapterModule;
import com.practice.di.modules.TrendingReposViewModelModule;
import com.practice.ui.adapters.TrendingRepoListAdapter;
import com.practice.databinding.ActivityTrendingReposBinding;
import com.practice.data.models.Repository;
import com.practice.data.viewmodels.TrendingReposViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Launcher activity displaying trending repos.
 */
public class TrendingReposActivity extends AppCompatActivity {

    // Binding carrying all the views for this screen
    private ActivityTrendingReposBinding mBinding;

    // Main viewmodel associated with this activity
    @Inject
    public TrendingReposViewModel mViewModel;

    // Adapter for trending repo list
    @Inject
    public TrendingRepoListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViewAndModel();
        initObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mViewModel.isLoading().getValue()) {
            mBinding.loadingContainer.slLoadingContainer.startShimmer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.loadingContainer.slLoadingContainer.stopShimmer();
    }

    //Initialise viewmodel and other UI components here.
    private void initViewAndModel() {
        LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Get the layout binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trending_repos);

        DaggerTrendingReposActivityComponents.builder()
                .trendingReposViewModelModule(new TrendingReposViewModelModule(this,
                        getIntent().getStringExtra(TrendingRepoRepository.EXTRA_BASE_URL)))
                .trendingReposListAdapterModule(new TrendingReposListAdapterModule(inflator))
                .build()
                .inject(this);

        mAdapter.setParentViewModel(mViewModel);

        // Associate the binding with ViewModel
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        mBinding.setAdapter(mAdapter); // This allows layout to bind it directly to recycler view
        mBinding.executePendingBindings();

    }

    // Init observers
    private void initObservers() {
        mViewModel.getTrendingRepos().observe(this, this::setAdapterData);
        mViewModel.isLoading().observe(this, this::toggleLoadingViewVisibility);
    }

    private void setAdapterData(List<Repository> repositories) {
        mAdapter.setData(repositories);
    }

    private void toggleLoadingViewVisibility(boolean isLoading) {
        if(isLoading) {
            mBinding.loadingContainer.slLoadingContainer.startShimmer();
        } else {
            mBinding.loadingContainer.slLoadingContainer.stopShimmer();
        }
    }

    public void onCreatePopupMenu(View view) {
        //Creating the instance of PopupMenu and Inflating the Popup using xml file
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.trending_repo_list_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.mi_sort_by_name :
                    mViewModel.sortRepoListByName();
                    break;
                case R.id.mi_sort_by_stars :
                    mViewModel.sortRepoListByStars();
                    break;
            }
            return true;
        });

        popup.show(); //showing popup menu
    }



}
