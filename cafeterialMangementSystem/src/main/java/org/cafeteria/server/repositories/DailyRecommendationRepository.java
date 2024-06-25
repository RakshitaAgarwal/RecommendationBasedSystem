package org.cafeteria.server.repositories;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.common.model.MenuItem;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IDailyRecommendationRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

public class DailyRecommendationRepository implements IDailyRecommendationRepository {
    private Connection connection;

    public DailyRecommendationRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(DailyRecommendation dailyRecommendation) throws SQLException {
        String query = "INSERT INTO DailyRecommendation (menuItemId, votes, rolledOutDate) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, dailyRecommendation.getMenuItemId());
            statement.setInt(2, dailyRecommendation.getVotes());
            statement.setTimestamp(3, dateToTimestamp(dailyRecommendation.getRolledOutDate()));
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(DailyRecommendation item) {

        return false;
    }

    @Override
    public boolean update(DailyRecommendation item) {

        return false;
    }

    @Override
    public List<DailyRecommendation> GetAll() {
        return null;
    }

    @Override
    public DailyRecommendation getById(int id) {
        return null;
    }

    @Override
    public List<DailyRecommendation> getByDate(String recommendationDate) throws SQLException {
        String query = "SELECT * FROM DailyRecommendation WHERE DATE(rolledOutDate) = ?";
        List<DailyRecommendation> recommendations;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, recommendationDate);
            ResultSet resultSet = statement.executeQuery();
            recommendations = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int menuItemId = resultSet.getInt("menuItemId");
                int votes = resultSet.getInt("votes");
                Date date = timestampToDate(resultSet.getTimestamp("rolledOutDate"));

                DailyRecommendation recommendation = new DailyRecommendation();
                recommendation.setId(id);
                recommendation.setMenuItemId(menuItemId);
                recommendation.setVotes(votes);
                recommendation.setRolledOutDate(date);

                recommendations.add(recommendation);
            }
        }
        return recommendations;
    }
}