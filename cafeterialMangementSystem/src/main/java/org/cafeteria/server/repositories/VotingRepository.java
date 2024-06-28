package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Vote;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IVotingRepository;

import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class VotingRepository implements IVotingRepository {
    private final Connection connection;

    public VotingRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(Vote item) throws SQLException {
        String query = "INSERT INTO Vote (menuItemId, userId, dateTime) VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, item.getMenuItemId());
            statement.setInt(2, item.getUserId());
            statement.setTimestamp(3, dateToTimestamp(item.getDateTime()));
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Vote item) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Vote item) throws SQLException {
        return false;
    }

    @Override
    public List<Vote> GetAll() throws SQLException {
        return null;
    }

    @Override
    public Vote getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Vote> getByUserCurrentDate(int userId, String date) throws SQLException {
        String query = "SELECT * FROM Vote WHERE userId = ? AND DATE(dateTime) = ?";
        List<Vote> userVotes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, date);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int menuItemId = resultSet.getInt("menuItemId");
                Date dateTime = timestampToDate(resultSet.getTimestamp("dateTime"));

                Vote userVote = new Vote();
                userVote.setId(id);
                userVote.setUserId(userId);
                userVote.setMenuItemId(menuItemId);
                userVote.setDateTime(dateTime);

                userVotes.add(userVote);
            }
        }

        return userVotes;
    }

    @Override
    public List<Vote> getAllByDate(String date) throws SQLException {
        String query = "SELECT * FROM Vote WHERE DATE(dateTime) = ?";
        List<Vote> userVotes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("userId");
                int menuItemId = resultSet.getInt("menuItemId");
                Date dateTime = timestampToDate(resultSet.getTimestamp("dateTime"));

                Vote userVote = new Vote();
                userVote.setId(id);
                userVote.setUserId(userId);
                userVote.setMenuItemId(menuItemId);
                userVote.setDateTime(dateTime);

                userVotes.add(userVote);
            }
        }

        return userVotes;
    }

    @Override
    public Map<Integer, Integer> getNextDayMenuOptionsVotes(String date) throws SQLException {
        String query = "SELECT menuItemId, Count(*) As voteCount FROM Vote WHERE DATE(date) = ? GROUP BY menuItemId";
        Map<Integer, Integer> menuItemVotesByDate = new HashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int menuItemId = resultSet.getInt("menuItemId");
                int voteCount = resultSet.getInt("voteCount");
                menuItemVotesByDate.put(menuItemId, voteCount);
            }
        }
        return menuItemVotesByDate;
    }
}