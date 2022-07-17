package fr.eni.papeterie.dal.jdbc;

import fr.eni.papeterie.config.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCTools
{
	private static Connection connection;

	private JDBCTools(){};

	/**
	 * Open a connection to the database.
	 *
	 * @return connection o this database.
	 *
	 * @throws SQLException in case of problem.
	 */

	public static Connection getConnection() throws SQLException, ClassNotFoundException
	{
		// If the connexion is already opened.
		if (connection != null) return connection;

		// Retrieve properties
		String url = Settings.getProperties("url");
		String user = Settings.getProperties("user");
		String passwd = Settings.getProperties("password");
		String driverName = Settings.getProperties("driver");

		//Class.forName(driverName);

		// open the connexion to the database.
		connection = DriverManager.getConnection(url, user, passwd);

		// And return the result.
		return connection;
	}

	/**
	 * Close the connection to the database.
	 *
	 * @throws SQLException In case of problem.
	 */

	public static void closeConnexion() throws SQLException
	{
		// If the database is opened, close-it.
		if (connection != null) connection.close();

		// Database closed.
		connection = null;
	}
}
