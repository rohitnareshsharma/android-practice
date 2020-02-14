package com.practice.ui.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.data.models.Repository;
import com.practice.data.datamodels.TrendingRepoListItemDataModel;
import com.practice.databinding.TrendingReposListItemBinding;

/**
 * Trending Repo List Item View Holder. Because of binding it is doing very less amount of work.
 */
public class TrendingRepoViewHolder extends RecyclerView.ViewHolder {

    private TrendingReposListItemBinding mBinding;

    private TrendingRepoListItemDataModel mDataModel;

    public TrendingRepoViewHolder(@NonNull TrendingReposListItemBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        mBinding.setViewModel(mDataModel = new TrendingRepoListItemDataModel());
    }

    public TrendingRepoListItemDataModel getDataModel() {
        return mDataModel;
    }

    public void bind(Repository repository, boolean isExpanded) {
        mBinding.getViewModel().setRepository(repository);
        mBinding.getViewModel().setExpanded(isExpanded);
        mBinding.executePendingBindings();
    }
}