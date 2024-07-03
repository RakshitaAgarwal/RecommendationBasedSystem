package org.cafeteria.client.handlers;

import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.UserProfile;
import java.util.Comparator;
import java.util.Map;

public class MenuItemComparator implements Comparator<Map.Entry<Integer, String>> {

    private final UserProfile userProfile;
    private final Map<Integer, MenuItem> menuItemMap;

    public MenuItemComparator(UserProfile userProfile, Map<Integer, MenuItem> menuItemMap) {
        this.userProfile = userProfile;
        this.menuItemMap = menuItemMap;
    }

    @Override
    public int compare(Map.Entry<Integer, String> entry1, Map.Entry<Integer, String> entry2) {
        MenuItem menuItem1 = menuItemMap.get(entry1.getKey());
        MenuItem menuItem2 = menuItemMap.get(entry2.getKey());

        int comparison = compareDietaryPreference(menuItem1, menuItem2);
        if (comparison != 0) return comparison;

        comparison = compareCuisineType(menuItem1, menuItem2);
        if (comparison != 0) return comparison;

        comparison = compareSpiceLevel(menuItem1, menuItem2);
        if (comparison != 0) return comparison;

        return compareSweetTooth(menuItem1, menuItem2);
    }

    private int compareDietaryPreference(MenuItem menuItem1, MenuItem menuItem2) {
        int menuItem1PreferenceRank = getDietaryPreferenceRank(menuItem1.getMenuItemTypeId());
        int menuItem2PreferenceRank = getDietaryPreferenceRank(menuItem2.getMenuItemTypeId());
        return Integer.compare(menuItem1PreferenceRank, menuItem2PreferenceRank);
    }

    private int getDietaryPreferenceRank(int menuItemTypeId) {
        if (menuItemTypeId == userProfile.getDietaryPreferenceId()) return 0;
        return 1;
    }

    private int compareCuisineType(MenuItem menuItem1, MenuItem menuItem2) {
        if (menuItem1.getCuisineTypeId() == userProfile.getFavCuisineId() &&
                menuItem2.getCuisineTypeId() != userProfile.getFavCuisineId()) return -1;
        if (menuItem1.getCuisineTypeId() != userProfile.getFavCuisineId() &&
                menuItem2.getCuisineTypeId() == userProfile.getFavCuisineId()) return 1;
        return 0;
    }

    private int compareSpiceLevel(MenuItem menuItem1, MenuItem menuItem2) {
        int spiceDifference1 = Math.abs(menuItem1.getSpiceContentLevelId() - userProfile.getSpiceLevelId());
        int spiceDifference2 = Math.abs(menuItem2.getSpiceContentLevelId() - userProfile.getSpiceLevelId());
        return Integer.compare(spiceDifference1, spiceDifference2);
    }

    private int compareSweetTooth(MenuItem menuItem1, MenuItem menuItem2) {
        if (userProfile.isSweetTooth()) {
            return Integer.compare(menuItem2.getSweetContentLevelId(), menuItem1.getSweetContentLevelId());
        }
        return 0;
    }
}