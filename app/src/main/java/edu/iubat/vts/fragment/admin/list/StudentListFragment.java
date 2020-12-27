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
import edu.iubat.vts.data.model.User;
import edu.iubat.vts.databinding.FragmentStudentListBinding;
import edu.iubat.vts.extensions.NavigationExt;
import edu.iubat.vts.fragment.admin.home.AdminHomeFragmentDirections;

public class StudentListFragment extends Fragment implements ViewHolderCompat.OnItemClickListener {
    private static final String LOG_TAG = StudentListFragment.class.getSimpleName();

    private FragmentStudentListBinding viewBinding;
    private NavController navController;

    private StudentListViewModel studentListViewModel;

    public StudentListFragment() { }

    public static StudentListFragment newInstance() {
        return new StudentListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentListViewModel = new ViewModelProvider(requireActivity()).get(StudentListViewModel.class);
        studentListViewModel.getUserListAdapterCompat().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentStudentListBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewBinding.recyclerView.setHasFixedSize(true);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewBinding.recyclerView.setAdapter(studentListViewModel.getUserListAdapterCompat());
    }

    @Override
    public void onItemClick(@NonNull View v, int viewType, int position) {
        User user = studentListViewModel.getUserListAdapterCompat().getCurrentList().get(position);
        if (user != null) {
            if (v.getId() == R.id.overflow_button) {
                PopupMenu popup = new PopupMenu(requireContext(), v);
                popup.getMenuInflater().inflate(R.menu.user_list_popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_edit) {
                            NavigationExt.safeNavigate(navController, R.id.nav_admin_home,
                                    AdminHomeFragmentDirections.actionNavAdminHomeToNavEditStudent(user));
                            return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        }
    }
}