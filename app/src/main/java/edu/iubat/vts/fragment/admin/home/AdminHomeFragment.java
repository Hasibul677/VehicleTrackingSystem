package edu.iubat.vts.fragment.admin.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import edu.iubat.vts.R;
import edu.iubat.vts.data.repository.DataRepository;
import edu.iubat.vts.databinding.FragmentAdminHomeBinding;
import edu.iubat.vts.extensions.ResourcesExt;
import edu.iubat.vts.fragment.admin.list.BusListFragment;
import edu.iubat.vts.fragment.admin.list.DriverListFragment;
import edu.iubat.vts.fragment.admin.list.StudentListFragment;
import edu.iubat.vts.tab.TabStateAdapter;

public class AdminHomeFragment extends Fragment {
    private static final String LOG_TAG = AdminHomeFragment.class.getSimpleName();

    private FragmentAdminHomeBinding viewBinding;
    private NavController navController;

    private DataRepository dataRepository;

    private TabStateAdapter tabStateAdapter;

    private List<Fragment> fragmentList;
    private List<String> fragmentNameList;

    public AdminHomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataRepository = DataRepository.getInstance(requireContext());
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
            fragmentList.add(BusListFragment.newInstance());
            fragmentList.add(DriverListFragment.newInstance());
            fragmentList.add(StudentListFragment.newInstance());
        }
        if (fragmentNameList == null) {
            fragmentNameList = new ArrayList<>();
            fragmentNameList.add(ResourcesExt.getString(requireContext(), R.string.tab_bus));
            fragmentNameList.add(ResourcesExt.getString(requireContext(), R.string.tab_driver));
            fragmentNameList.add(ResourcesExt.getString(requireContext(), R.string.tab_student));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        tabStateAdapter = new TabStateAdapter(getChildFragmentManager(), getLifecycle(),
                fragmentList);

        viewBinding.viewPager.setAdapter(tabStateAdapter);

        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (!fragmentNameList.isEmpty()) {
                            tab.setText(fragmentNameList.get(position));
                        }
                    }
                }).attach();
    }
}