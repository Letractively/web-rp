package edu.ubb.warp.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.ubb.warp.dao.GroupDAO;
import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.GroupExistsException;
import edu.ubb.warp.exception.GroupNotFoundException;
import edu.ubb.warp.model.Group;

public class GroupJdbcDAO implements GroupDAO {
	public Group getGroupByGroupID(int groupID) throws DAOException,
			GroupNotFoundException {
		Group group = null;
		try {
			String command = "SELECT * FROM `Groups` WHERE `GroupID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, groupID);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				group = getGroupFromResult(result);
			} else {
				throw new GroupNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return group;
	}

	public Group getGroupByGroupName(String groupName) throws DAOException,
			GroupNotFoundException {
		Group group = null;
		try {
			String command = "SELECT * FROM `Groups` WHERE `GroupName` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, groupName);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				group = getGroupFromResult(result);
			} else {
				throw new GroupNotFoundException();
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return group;
	}

	public void insertGroup(Group group) throws GroupExistsException {
		try {
			String command = "INSERT INTO `Groups`(groupName) VALUES (?);";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, group.getGroupName());
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();
			result.next();
			group.setGroupID(result.getInt(1));
		} catch (SQLException e) {
			throw new GroupExistsException();
		}
	}

	public void updateGroup(Group group) throws GroupExistsException {
		try {
			String command = "UPDATE `Groups` SET `groupName` = ? WHERE `groupID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setString(1, group.getGroupName());
			statement.setInt(2, group.getGroupID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GroupExistsException();
		}
	}

	public void deleteGroup(Group group) throws DAOException {
		try {
			String command = "DELETE FROM `Groups` WHERE `groupID` = ?";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			statement.setInt(1, group.getGroupID());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException();
		}
	}

	private Group getGroupFromResult(ResultSet result) throws SQLException {
		Group group = new Group();
		group.setGroupID(result.getInt("GroupID"));
		group.setGroupName(result.getString("GroupName"));
		return group;
	}

	public ArrayList<Group> getAllGroups() throws DAOException {
		ArrayList<Group> groups = new ArrayList<Group>();
		try {
			String command = "SELECT * FROM Groups";
			PreparedStatement statement = JdbcConnection.getConnection()
					.prepareStatement(command);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				groups.add(getGroupFromResult(result));
			}
		} catch (SQLException e) {
			throw new DAOException();
		}
		return groups;
	}

}
