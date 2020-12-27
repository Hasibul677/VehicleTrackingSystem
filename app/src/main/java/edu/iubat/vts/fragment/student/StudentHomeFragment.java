package edu.iubat.vts.fragment.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import dev.alshakib.rvcompat.viewholder.ViewHolderCompat;
import edu.iubat.vts.R;
import edu.iubat.vts.activity.map.GoogleMapActivity;
import edu.iubat.vts.data.model.Bus;
import edu.iubat.vts.databinding.FragmentStudentHomeBinding;
import edu.iubat.vts.extensions.NavigationExt;

public class StudentHomeFragment extends Fragment implements ViewHolderCompat.OnItemClickListener {
    private static final String LOG_TAG = StudentHomeFragment.class.getSimpleName();

    private FragmentStudentHomeBinding viewBinding;
    private NavController navController;
    private StudentHomeViewModel studentHomeViewModel;

    public StudentHomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentHomeViewModel = new ViewModelProvider(this).get(StudentHomeViewModel.class);
        studentHomeViewModel.getBusListAdapterCompat().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentStudentHomeBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewBinding.recyclerView.setHasFixedSize(true);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewBinding.recyclerView.setAdapter(studentHomeViewModel.getBusListAdapterCompat());
    }

    @Override
    public void onItemClick(@NonNull View v, int viewType, int position) {
        if (v.getId() == R.id.overflow_button) {
            Bus bus = studentHomeViewModel.getBusListAdapterCompat().getCurrentList().get(position);
            if (bus != null) {
                NavigationExt.safeNavigate(navController, R.id.nav_student_home,
                        StudentHomeFragmentDirections.actionNavStudentHomeToNavBusInfoDialog(bus));
            }
        } else {
            Bus bus = studentHomeViewModel.getBusListAdapterCompat().getCurrentList().get(position);
            if (bus != null) {
                Intent intent = new Intent(requireContext(), GoogleMapActivity.class);
                intent.putExtra(GoogleMapActivity.EXTRA_BUS, bus);
                startActivity(intent);
            }
        }
    }
}