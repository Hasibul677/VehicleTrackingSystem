package edu.iubat.vts.extensions;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

public final class NavigationExt {

    public static void safeNavigate(@NonNull NavController navController, @IdRes int currentDestinationId, @NonNull NavDirections navDirections) {
        if (navController.getCurrentDestination() != null && navController.getCurrentDestination().getId() == currentDestinationId) {
            navController.navigate(navDirections);
        }
    }

    public static void safeNavigateUp(@NonNull NavController navController, @IdRes int currentDestinationId) {
        if (navController.getCurrentDestination() != null && navController.getCurrentDestination().getId() == currentDestinationId) {
            navController.navigateUp();
        }
    }
}
