package com.practice.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.RecyclerView;

import com.practice.BR;
import com.practice.R;
import com.practice.data.models.Repository;
import com.practice.data.datamodels.TrendingRepoListItemDataModel;
import com.practice.data.viewmodels.TrendingReposViewModel;
import com.practice.ui.viewholders.TrendingRepoViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * RecyclerView Adapter for displaying the trending git repo.
 */
public class TrendingRepoListAdapter extends RecyclerView.Adapter<TrendingRepoViewHolder> {

    // A reference to parent view model. This is needed to store the selected item state.
    // As adapter is destructible but not the parent viewmodel.
    private TrendingReposViewModel mParentViewModel;

    // Source Data
    private List<Repository> mData = new ArrayList<>();

    // Inflator service for creating views from xml
    private LayoutInflater mInflator;

    // Observer to listen selected item event in the list
    private Observable.OnPropertyChangedCallback selectedItemCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (propertyId == BR.selected) {
                TrendingRepoListItemDataModel dataModel = (TrendingRepoListItemDataModel) sender;
                setSelectedRepository(dataModel.getRepository());
            }
        }
    };

    /**
     * Sets the selected item of this adapter.
     * It communicates back with parent view model
     * to preserve this info.
     *
     * @param repository Selected repository
     */
    private void setSelectedRepository(Repository repository) {
        if (repository.equals(mParentViewModel.getSelectedRepository())) {
            mParentViewModel.setSelectedRepository(null);
        } else {
            mParentViewModel.setSelectedRepository(repository);
        }
        notifyDataSetChanged();
    }

    /**
     * Sets the parent view model of this adapter. For it to communicate back to activity.
     * There should be a better way of this. Will think later
     * @param parentViewModel
     */
    public void setParentViewModel(@NonNull TrendingReposViewModel parentViewModel) {
        mParentViewModel = parentViewModel;
    }

    /*
     * Sets fresh data to this adapter. In case if null is coming.
     * old data if exist will keep on displaying.
     *
     * For a case some results are displayed. Network is gone and now you refresh the list
     * It will still show older results. Instead of retry screen.
     *
     * Don't know for now if this is not the intent.
     *
     */
    public void setData(List<Repository> data) {
        if (data != null) {
            mData = data;
        }
        // Explore Android DiffUtils for better performance.
        notifyDataSetChanged();
    }

    // Constructor
    @Inject
    public TrendingRepoListAdapter(@Named("inflater") LayoutInflater inflater) {
        mInflator = inflater;
    }

    @NonNull
    @Override
    public TrendingRepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrendingRepoViewHolder(DataBindingUtil.inflate(mInflator,
                R.layout.trending_repos_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingRepoViewHolder holder, int position) {
        Repository repository = mData.get(position);
        holder.bind(repository, repository.equals(mParentViewModel.getSelectedRepository()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TrendingRepoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.getDataModel().addOnPropertyChangedCallback(selectedItemCallback);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TrendingRepoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getDataModel().removeOnPropertyChangedCallback(selectedItemCallback);
    }
}


