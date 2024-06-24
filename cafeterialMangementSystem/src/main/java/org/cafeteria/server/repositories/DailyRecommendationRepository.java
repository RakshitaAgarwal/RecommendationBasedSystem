package org.cafeteria.server.repositories;

import org.cafeteria.common.model.DailyRecommendation;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IDailyRecommendationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.cafeteria.common.util.Utils.dateToTimestamp;

public class DailyRecommendationRepository implements IDailyRecommendationRepository {
    private Connection connection;
    public DailyRecommendationRepository() {
        connection = JdbcConnection.getConnection();
    }
    @Override
    public boolean add(DailyRecommendation dailyRecommendation) throws SQLException {
        String query = "INSERT INTO DailyRecommendation (menuItemId, votes, dateTime) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, dailyRecommendation.getMenuItemId());
            statement.setInt(2, dailyRecommendation.getVotes());
            statement.setTimestamp(3, dateToTimestamp(dailyRecommendation.getDateTime()));
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
}