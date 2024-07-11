package org.cafeteria.client.handlers;

import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuItemComparatorTest {
    @Mock
    private UserProfile userProfile;
    @Mock
    private Map<Integer, MenuItem> menuItemMap;
    @InjectMocks
    private MenuItemComparator menuItemComparator;

    public void setUp(MenuItem menuItem1, MenuItem menuItem2) {
        userProfile = new UserProfile(3061, 1,2,1,false);
        menuItemMap = new HashMap<>();
        menuItemMap.put(1, menuItem1);
        menuItemMap.put(2, menuItem2);
        menuItemComparator = new MenuItemComparator(userProfile, menuItemMap);
    }
    @Test
    public void testCompareDietaryPreference() {
        MenuItem menuItem1 = new MenuItem("Egg Omelette", 45.0f, true, 2, 3, 2, 2, 1);
        MenuItem menuItem2 = new MenuItem("Burger", 45.0f, true, 2, 1, 3, 2, 2);

        setUp(menuItem1, menuItem2);

        Map.Entry<Integer, String> entry1 = new AbstractMap.SimpleEntry<>(1, "Menu Item Name: " + menuItem1.getName() + " | Score: " + 23 + " | Key Phrases: tasty" );
        Map.Entry<Integer, String> entry2 = new AbstractMap.SimpleEntry<>(2, "Menu Item Name: " + menuItem2.getName() + " | Score: " + 24 + " | Key Phrases: yummy");

        int actualResult = menuItemComparator.compare(entry1, entry2);
        int expectedResult = 1;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCompareCuisineType() {
        MenuItem menuItem1 = new MenuItem("Egg Omelette", 45.0f, true, 2, 1, 2, 2, 1);
        MenuItem menuItem2 = new MenuItem("Uttapam", 45.0f, true, 2, 1, 3, 2, 2);

        setUp(menuItem1, menuItem2);

        Map.Entry<Integer, String> entry1 = new AbstractMap.SimpleEntry<>(1, "Menu Item Name: " + menuItem1.getName() + " | Score: " + 23 + " | Key Phrases: tasty" );
        Map.Entry<Integer, String> entry2 = new AbstractMap.SimpleEntry<>(2, "Menu Item Name: " + menuItem2.getName() + " | Score: " + 24 + " | Key Phrases: yummy");

        int actualResult = menuItemComparator.compare(entry1, entry2);
        int expectedResult = -1;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCompare_SpiceLevel() {
        MenuItem menuItem1 = new MenuItem("Egg Omelette", 45.0f, true, 2, 1, 2, 2, 1);
        MenuItem menuItem2 = new MenuItem("Pan Cakes", 45.0f, true, 2, 1, 3, 1, 1);

        setUp(menuItem1, menuItem2);

        Map.Entry<Integer, String> entry1 = new AbstractMap.SimpleEntry<>(1, "Menu Item Name: " + menuItem1.getName() + " | Score: " + 23 + " | Key Phrases: tasty" );
        Map.Entry<Integer, String> entry2 = new AbstractMap.SimpleEntry<>(2, "Menu Item Name: " + menuItem2.getName() + " | Score: " + 24 + " | Key Phrases: yummy");

        int actualResult = menuItemComparator.compare(entry1, entry2);
        int expectedResult = -1;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCompare_SweetTooth() {
        MenuItem menuItem1 = new MenuItem("Egg Omelette", 45.0f, true, 2, 3, 2, 2, 1);
        MenuItem menuItem2 = new MenuItem("Pan Cakes", 45.0f, true, 2, 1, 3, 2, 2);

        setUp(menuItem1, menuItem2);

        Map.Entry<Integer, String> entry1 = new AbstractMap.SimpleEntry<>(1, "Menu Item Name: " + menuItem1.getName() + " | Score: " + 23 + " | Key Phrases: tasty" );
        Map.Entry<Integer, String> entry2 = new AbstractMap.SimpleEntry<>(2, "Menu Item Name: " + menuItem2.getName() + " | Score: " + 24 + " | Key Phrases: yummy");

        int actualResult = menuItemComparator.compare(entry1, entry2);
        int expectedResult = 1;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCompare_EqualItems() {
        MenuItem menuItem1 = new MenuItem("Dosa", 45.0f, true, 2, 1, 1, 2, 2);
        MenuItem menuItem2 = new MenuItem("Uttapam", 45.0f, true, 2, 1, 1, 2, 2);

        setUp(menuItem1, menuItem2);

        Map.Entry<Integer, String> entry1 = new AbstractMap.SimpleEntry<>(1, "Menu Item Name: " + menuItem1.getName() + " | Score: " + 23 + " | Key Phrases: tasty" );
        Map.Entry<Integer, String> entry2 = new AbstractMap.SimpleEntry<>(2, "Menu Item Name: " + menuItem2.getName() + " | Score: " + 24 + " | Key Phrases: yummy");

        int actualResult = menuItemComparator.compare(entry1, entry2);
        int expectedResult = 0;
        assertEquals(expectedResult, actualResult);
    }
}