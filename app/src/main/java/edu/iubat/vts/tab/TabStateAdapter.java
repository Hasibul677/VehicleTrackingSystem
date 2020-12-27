package edu.iubat.vts.tab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class TabStateAdapter extends FragmentStateAdapter {
    private static final String LOG_TAG = TabStateAdapter.class.getSimpleName();

    private final List<Fragment> fragmentList;

    public TabStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,
                           @NonNull List<Fragment> fragmentList) {
        super(fragmentManager, lifecycle);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
