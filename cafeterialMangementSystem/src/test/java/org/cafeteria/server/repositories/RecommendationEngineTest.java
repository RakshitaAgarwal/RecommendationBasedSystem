package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Feedback;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.common.model.MenuItemRecommendation;
import org.cafeteria.server.services.interfaces.IFeedbackService;
import org.cafeteria.server.services.interfaces.IMenuService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecommendationEngineTest {

    @Mock
    private IFeedbackService feedbackService;

    @Mock
    private IMenuService menuService;

    @InjectMocks
    private RecommendationEngine recommendationEngine;

    @Before
    public void setUp() {
        recommendationEngine = new RecommendationEngine(feedbackService, menuService);
    }

    @Test
    public void testGetTopRecommendedItems() throws SQLException {
        List<Integer> menuItemIds = Arrays.asList(1, 2, 3);
        MenuItemRecommendation recommendation1 = new MenuItemRecommendation(1, 4.5, "Positive");
        MenuItemRecommendation recommendation2 = new MenuItemRecommendation(2, 3.5, "Neutral");
        MenuItemRecommendation recommendation3 = new MenuItemRecommendation(3, 1.5, "Negative");

        when(menuService.getById(1)).thenReturn(new MenuItem("Pizza", 12.5f, true, 1, 1, 1, 2, 1));
        when(menuService.getById(2)).thenReturn(new MenuItem("Burger", 10.0f, true, 1, 1, 1, 2, 2));
        when(menuService.getById(3)).thenReturn(new MenuItem("Salad", 7.5f, true, 1, 1, 1, 2, 3));

        when(feedbackService.getFeedbackByMenuItemId(1)).thenReturn(Arrays.asList(
                new Feedback("Good", 5),
                new Feedback("Great", 4)
        ));
        when(feedbackService.getFeedbackByMenuItemId(2)).thenReturn(Arrays.asList(
                new Feedback("Okay", 3),
                new Feedback("Fine", 4)
        ));
        when(feedbackService.getFeedbackByMenuItemId(3)).thenReturn(Arrays.asList(
                new Feedback("Bad", 2),
                new Feedback("Terrible", 1)
        ));

        List<MenuItemRecommendation> actualResult = recommendationEngine.getTopRecommendedItems(menuItemIds, 2);
        List<MenuItemRecommendation> expectedResult = List.of(new MenuItemRecommendation[]{recommendation1, recommendation2});

        assertEquals(expectedResult.size(), actualResult.size());
        assertEquals(expectedResult.get(0).getMenuItemId(), actualResult.get(0).getMenuItemId());
        assertEquals(expectedResult.get(1).getMenuItemId(), actualResult.get(1).getMenuItemId());
    }
}