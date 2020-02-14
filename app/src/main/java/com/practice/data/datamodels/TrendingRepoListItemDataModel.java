package com.practice.data.datamodels;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.practice.BR;
import com.practice.data.models.Repository;

/**
 * ViewModel responsible for individual item in list on main screen.
 */
public class TrendingRepoListItemDataModel extends BaseObservable {

    // Source Data
    private Repository mRepository;

    // Tells if its associated view to be expanded or not.
    private boolean mIsExpanded;

    // Tells if this list item just got selected.
    // This may remain true on multiple item in list
    // as they keep on getting selected. Reason property change
    // is getting observed in adapter which is passing this state to
    // parent view model to preserve it. So that's ok
    private boolean mIsSelected;

    /**
     * Set the data for this ViewModel
     * @param repository
     */
    public void setRepository(@NonNull Repository repository) {
        mRepository = repository;
        notifyChange();
    }

    @Bindable
    public String getAuthor() {
        return mRepository.author;
    }

    @Bindable
    public String getName() {
        return mRepository.name;
    }

    @Bindable
    public String getAvatar() {
        return mRepository.avatar;
    }

    @Bindable
    public String getUrl() {
        return mRepository.url;
    }

    @Bindable
    public String getDescription() {
        return mRepository.description;
    }

    @Bindable
    public String getStars() {
        return mRepository.stars;
    }

    @Bindable
    public String getForks() {
        return mRepository.forks;
    }

    @Bindable
    public String getLanguage() {
        return mRepository.language;
    }

    @Bindable
    public String getLanguageColor() {
        return mRepository.languageColor;
    }

    @Bindable
    public boolean isExpanded() {
        return mIsExpanded;
    }

    @Bindable
    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }

    public Repository getRepository() {
        return mRepository;
    }

}
