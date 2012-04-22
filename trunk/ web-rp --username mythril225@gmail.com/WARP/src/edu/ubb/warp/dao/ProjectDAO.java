package edu.ubb.warp.dao;

import java.util.ArrayList;

import edu.ubb.warp.exception.DAOException;
import edu.ubb.warp.exception.ProjectNameExistsException;
import edu.ubb.warp.exception.ProjectNotFoundException;
import edu.ubb.warp.model.Project;
import edu.ubb.warp.model.User;

public interface ProjectDAO {
	/**
	 * 
	 * @param projectID
	 * @return Project with requested id
	 * @throws DAOException in case of database access issues
	 * @throws ProjectNotFoundException  in case there is no such project in the database
	 */
	public Project getProjectByProjectID(int projectID) throws DAOException,
			ProjectNotFoundException;

	/**
	 * 
	 * @param projectName
	 * @return Project with requested name
	 * @throws DAOException in case of database access issues
	 * @throws ProjectNotFoundException  in case there is no such project in the database
	 */
	public Project getProjectByProjectName(String projectName)
			throws DAOException, ProjectNotFoundException;

	/**
	 * 
	 * @param project
	 * @throws ProjectNameExistsException in case the projectName is not unique
	 */
	public void insertProject(Project project)
			throws ProjectNameExistsException;

	/**
	 * 
	 * @param project
	 * @throws ProjectNameExistsException in case the projectName is not unique
	 */
	public void updateProject(Project project)
			throws ProjectNameExistsException;

	/**
	 * 
	 * @param project
	 * @throws DAOException in case of database access issues
	 */
	public void deleteProject(Project project) throws DAOException;
	
	/**
	 * 
	 * @param user
	 * @return projects requested by userName
	 * @throws DAOException
	 */
	public ArrayList<Project> getProjectsByUser(User user) throws DAOException;
}
