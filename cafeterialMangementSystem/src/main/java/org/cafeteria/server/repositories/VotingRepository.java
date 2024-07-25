package org.cafeteria.server.repositories;

import org.cafeteria.common.model.Vote;
import org.cafeteria.server.network.JdbcConnection;
import org.cafeteria.server.repositories.interfaces.IVotingRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cafeteria.common.util.Utils.dateToTimestamp;
import static org.cafeteria.common.util.Utils.timestampToDate;

public class VotingRepository implements IVotingRepository {
    private final Connection connection;
    private static final String TABLE_VOTE = "Vote";
    private static final String COLUMN_PK_ID = "id";
    private static final String COLUMN_FK_MENU_ITEM_ID = "menuItemId";
    private static final String COLUMN_FK_USER_ID = "userId";
    private static final String COLUMN_DATE_TIME = "dateTime";

    public VotingRepository() {
        connection = JdbcConnection.getConnection();
    }

    @Override
    public boolean add(Vote item) throws SQLException {
        String query = "INSERT INTO " + TABLE_VOTE + " (" + COLUMN_FK_MENU_ITEM_ID + ", " + COLUMN_FK_USER_ID + ", " + COLUMN_DATE_TIME + ") VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, item.getMenuItemId());
            statement.setInt(2, item.getUserId());
            statement.setTimestamp(3, dateToTimestamp(item.getDateTime()));
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean addBatch(List<Vote> items) throws SQLException {
        return false;
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
    public List<Vote> getAll() throws SQLException {
        return null;
    }

    @Override
    public Vote getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Vote> getByUserCurrentDate(int userId, String date) throws SQLException {
        String query = "SELECT * FROM " + TABLE_VOTE + " WHERE " + COLUMN_FK_USER_ID + " = ? AND DATE(" + COLUMN_DATE_TIME + ") = ?";
        List<Vote> userVotes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, date);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Vote userVote = new Vote();
                userVote.setId(resultSet.getInt(COLUMN_PK_ID));
                userVote.setUserId(userId);
                userVote.setMenuItemId(resultSet.getInt(COLUMN_FK_MENU_ITEM_ID));
                userVote.setDateTime(timestampToDate(resultSet.getTimestamp(COLUMN_DATE_TIME)));

                userVotes.add(userVote);
            }
        }

        return userVotes;
    }

    @Override
    public List<Vote> getAllByDate(String date) throws SQLException {
        String query = "SELECT * FROM " + TABLE_VOTE + " WHERE DATE(" + COLUMN_DATE_TIME + ") = ?";
        List<Vote> userVotes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Vote userVote = new Vote();
                userVote.setId(resultSet.getInt(COLUMN_PK_ID));
                userVote.setUserId(resultSet.getInt(COLUMN_FK_USER_ID));
                userVote.setMenuItemId(resultSet.getInt(COLUMN_FK_MENU_ITEM_ID));
                userVote.setDateTime(timestampToDate(resultSet.getTimestamp(COLUMN_DATE_TIME)));

                userVotes.add(userVote);
            }
        }

        return userVotes;
    }

    @Override
    public Map<Integer, Integer> getNextDayMenuOptionsVotes(String date) throws SQLException {
        String query = "SELECT " + COLUMN_FK_MENU_ITEM_ID + ", Count(*) As voteCount FROM " + TABLE_VOTE + " WHERE DATE(" + COLUMN_DATE_TIME + ") = ? GROUP BY " + COLUMN_FK_MENU_ITEM_ID;
        Map<Integer, Integer> menuItemVotesByDate = new HashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, date);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int menuItemId = resultSet.getInt(COLUMN_FK_MENU_ITEM_ID);
                int voteCount = resultSet.getInt("voteCount");
                menuItemVotesByDate.put(menuItemId, voteCount);
            }
        }
        return menuItemVotesByDate;
    }
}