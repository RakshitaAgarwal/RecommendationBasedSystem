package org.cafeteria.server.repositories;

import org.cafeteria.common.model.MenuItemUserVote;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IVotingRepository;
import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class VotingRepository implements IVotingRepository {
    private final Connection connection;

    public VotingRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(MenuItemUserVote item) throws SQLException {
        String query = "INSERT INTO MenuItemUserVote (menuItemId, userId, dateTime) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, item.getMenuItemId());
            statement.setInt(2, item.getUserId());
            statement.setTimestamp(3, dateToTimestamp(item.getDateTime()));
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(MenuItemUserVote item) throws SQLException {
        return false;
    }

    @Override
    public boolean update(MenuItemUserVote item) throws SQLException {
        return false;
    }

    @Override
    public List<MenuItemUserVote> GetAll() throws SQLException {
        return null;
    }

    @Override
    public MenuItemUserVote getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<MenuItemUserVote> getByUserCurrentDate(int userId, String date) throws SQLException {
        String query = "SELECT * FROM MenuItemUserVote WHERE userId = ? AND DATE(dateTime) = ?";
        List<MenuItemUserVote> userVotes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,userId);
            statement.setString(2, date);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int menuItemId = resultSet.getInt("menuItemId");
                Date dateTime = timestampToDate(resultSet.getTimestamp("dateTime"));

                MenuItemUserVote userVote = new MenuItemUserVote();
                userVote.setId(id);
                userVote.setUserId(userId);
                userVote.setMenuItemId(menuItemId);
                userVote.setDateTime(dateTime);

                userVotes.add(userVote);
            }
        }

        return userVotes;
    }
}