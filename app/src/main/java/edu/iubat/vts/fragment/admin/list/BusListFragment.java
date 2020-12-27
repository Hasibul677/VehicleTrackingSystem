package edu.iubat.vts.fragment.admin.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import dev.alshakib.rvcompat.viewholder.ViewHolderCompat;
import edu.iubat.vts.R;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.databinding.FragmentBusListBinding;
import edu.iubat.vts.extensions.NavigationExt;
import edu.iubat.vts.fragment.admin.home.AdminHomeFragmentDirections;

public class BusListFragment extends Fragment implements ViewHolderCompat.OnItemClickListener {
    private static final String LOG_TAG = BusListFragment.class.getSimpleName();

    private FragmentBusListBinding viewBinding;
    private NavController navController;

    private BusListViewModel busListViewModel;

    public BusListFragment() { }

    public static BusListFragment newInstance() {
        return new BusListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        busListViewModel = new ViewModelProvider(requireActivity()).get(BusListViewModel.class);
        busListViewModel.getAdminBusListAdapterCompat().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentBusListBinding.inflate(inflater, container,false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewBinding.recyclerView.setHasFixedSize(true);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewBinding.recyclerView.setAdapter(busListViewModel.getAdminBusListAdapterCompat());
    }

    @Override
    public void onItemClick(@NonNull View v, int viewType, int position) {
        Bus bus = busListViewModel.getAdminBusListAdapterCompat().getCurrentList().get(position);
        if (bus != null) {
            if (v.getId() == R.id.overflow_button) {
                PopupMenu popup = new PopupMenu(requireContext(), v);
                popup.getMenuInflater().inflate(R.menu.bus_list_popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_add_bus_driver) {
                            NavigationExt.safeNavigate(navController, R.id.nav_admin_home,
                                    AdminHomeFragmentDirections.actionNavAdminHomeToNavAddBusDriver(bus));
                            return true;
                        } else if (item.getItemId() == R.id.action_add_bus_conductor) {
                            NavigationExt.safeNavigate(navController, R.id.nav_admin_home,
                                    AdminHomeFragmentDirections.actionNavAdminHomeToNavAddBusConductor(bus));
                            return true;
                        } else if (item.getItemId() == R.id.action_edit) {
                            NavigationExt.safeNavigate(navController,R.id.nav_admin_home,
                                    AdminHomeFragmentDirections.actionNavAdminHomeToNavEditBus(bus));
                        } else if (item.getItemId() == R.id.action_delete) {
                            busListViewModel.delete(bus);
                        }
                        return false;
                    }
                });
                popup.show();
            } else {
                NavigationExt.safeNavigate(navController, R.id.nav_admin_home,
                        AdminHomeFragmentDirections.actionNavAdminHomeToNavBusDetailsDialog(bus));
            }
        }
    }
}