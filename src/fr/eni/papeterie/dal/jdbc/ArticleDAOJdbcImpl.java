package fr.eni.papeterie.dal.jdbc;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;
import fr.eni.papeterie.dal.ArticleDAO;
import fr.eni.papeterie.dal.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAOJdbcImpl implements ArticleDAO
{
	/* Error messages. */

	private final static String READ_ERROR = "Error while reading from the database.";
	private final static String WRITE_ERROR = "Error while writing to the database.";
	private final static String DELETE_ERROR = "Error while deleting an article in the database.";

	/* Queries. */

	private final static String INSERT_QUERY = "INSERT INTO articles" +
					" (reference, marque, designation, prixunitaire, qteStock, grammage, couleur, type)" +
					" VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	private final static String SELECT_BY_ID_QUERY = "SELECT" +
					" idArticle, reference, marque, designation, prixunitaire, qteStock, grammage, couleur, type" +
					" FROM articles" +
					" WHERE idArticle=";

	private final static String SELECT_ALL_QUERY = "SELECT" +
					" idArticle, reference, marque, designation, prixUnitaire, qteStock, grammage, couleur, type" +
					" FROM articles";

	private final static String SELECT_BY_TYPE = "SELECT" +
					" idArticle, reference, marque, designation, prixunitaire, qteStock, grammage, couleur, type" +
					" FROM articles" +
					" WHERE type =";

	private final static String UPDATE_QUERY = "UPDATE articles SET" +
					" reference=?, marque=?, designation=?, prixunitaire=?, qteStock=?, grammage=?, couleur=?, type=?" +
					" WHERE idarticle=?";

	private final static String DELETE_QUERY = "DELETE FROM Articles WHERE idArticle=";

	/* Other constants */

	private final static String PAPER_STRING = "ramette";
	private final static String PENCIL_STRING = "stylo";

	/**
	 * Insert an article into the database.
	 *
	 * @param article Article to insert
	 *
	 * @throws DALException In case of problem.
	 */

	public void insert(Article article) throws DALException
	{
		putArticle(article, INSERT_QUERY, true);
	}

	/**
	 * Search an article in the database according to his identifier.
	 *
	 * @param idArticle Identifier of the article to search for.
	 *
	 * @return Article found or null if not found.
	 *
	 * @throws DALException In case of problem.
	 */

	public Article selectById(int idArticle) throws DALException
	{
		List<Article> articles = getArticle(SELECT_BY_ID_QUERY + idArticle);

		return (articles.size() != 0) ? (articles.get(0)) : (null);
	}

	/**
	 * Read all the articles present in the database.
	 *
	 * @return List of articles.
	 *
	 * @throws DALException in case of problem.
	 */

	public List<Article> selectAll() throws DALException
	{
		return getArticle(SELECT_ALL_QUERY);
	}

	/**
	 * Read all the articles corresponding to the supplied type.
	 *
	 * @param type Type of article to retrieve.
	 *
	 * @return List of articles.
	 *
	 * @throws DALException in case of problem.
	 */

	public List<Article> selectByType(int type) throws DALException
	{
		return getArticle(SELECT_BY_TYPE + "'" + ((type == TYPE_PAPER) ? (PAPER_STRING) : (PENCIL_STRING)) + "'");
	}

	/**
	 * Modify an article form the database. This article must already exist to do
	 * the update.
	 *
	 * @param article Article containing the new information.
	 *
	 * @throws DALException In case of problem.
	 */

	public void update(Article article) throws DALException
	{
		putArticle(article, UPDATE_QUERY, false);
	}

	/**
	 * Delete an article from the database using its identifier.
	 *
	 * @param idArticle Identifier of the article to delete.
	 *
	 * @throws DALException In case of problem.
	 */

	public void delete(int idArticle) throws DALException
	{
		Connection connection = null;
		Statement statement = null;

		try
		{
			// Open the database.
			connection = JDBCTools.getConnection();

			// Create object to build the query.
			statement = connection.createStatement();

			// Retrieve data from database
			statement.executeUpdate(DELETE_QUERY + idArticle);
		}
		catch (SQLException | ClassNotFoundException e)
		{
			// try to close the connection.
			closeConnection(statement, connection, DELETE_ERROR, e);
		}

		// try to close the connection.
		closeConnection(statement, connection, DELETE_ERROR, null);
	}

	/**
	 * Read a list of articles from the database according to the query string
	 * supplied in parameter.
	 *
	 * @param query Query to execute to retrieve articles.
	 *
	 * @return A list of articles matching query criteria.
	 *
	 * @throws DALException In case of problem.
	 */

	private List<Article> getArticle(String query) throws DALException
	{
		List<Article> articles = new ArrayList<>();

		int idArticle;
		String reference;
		String marque;
		String designation;
		float prixUnitaire;
		int qteStock;
		int grammage;
		String couleur;
		String type;

		Connection connection = null;
		Statement statement = null;

		try
		{
			// Open the database.
			connection = JDBCTools.getConnection();

			// Create object to build the query.
			statement = connection.createStatement();

			// Retrieve data from database
			ResultSet resultSet = statement.executeQuery(query);

			// Retrieve required articles form the database.
			while (resultSet.next())
			{
				idArticle = resultSet.getInt(1);
				reference = resultSet.getString(2).trim();
				marque = resultSet.getString(3);
				designation = resultSet.getString(4);
				prixUnitaire = resultSet.getInt(5);
				qteStock = resultSet.getInt(6);
				grammage = resultSet.getInt(7);
				couleur = resultSet.getString(8);
				type = resultSet.getString(9).trim();

				if (type.equals(PENCIL_STRING))
					articles.add(new Stylo(idArticle, marque, reference, designation, prixUnitaire, qteStock, couleur));
				else
					articles.add(new Ramette(idArticle, marque, reference, designation, prixUnitaire, qteStock, grammage));
			}
		}
		catch (SQLException | ClassNotFoundException e)
		{
			// try to close the connection.
			closeConnection(statement, connection, READ_ERROR, e);
		}

		// try to close the connection.
		closeConnection(statement, connection, READ_ERROR, null);

		// Return the list articles.
		return articles;
	}

	/**
	 * Save an article info the database using the supplied query string. The
	 * operation performed (insert or update) depends on the insert parameter.
	 *
	 * @param article Article to update/insert.
	 * @param query   Query used to do the operation.
	 * @param insert  Type of operation.
	 *
	 * @throws DALException In case of problem.
	 */

	private void putArticle(Article article, String query, boolean insert) throws DALException
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet generatedKeys;

		Stylo stylo;
		Ramette ramette;

		try
		{
			// Open the database.
			connection = JDBCTools.getConnection();

			// Create object to build the query.
			statement = (insert) ? (connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) : (connection.prepareStatement(query));

			statement.setString(1, article.getReference());
			statement.setString(2, article.getMarque());
			statement.setString(3, article.getDesignation());
			statement.setFloat(4, article.getPrixUnitaire());
			statement.setInt(5, article.getQteStock());

			if (article instanceof Stylo)
			{
				stylo = (Stylo) article;
				statement.setNull(6, Types.INTEGER);
				statement.setString(7, stylo.getCouleur());
				statement.setString(8, PENCIL_STRING);
			}
			else
			{
				ramette = (Ramette) article;
				statement.setInt(6, ramette.getGrammage());
				statement.setNull(7, Types.NVARCHAR);
				statement.setString(8, PAPER_STRING);
			}

			if (!insert) statement.setInt(9, article.getIdArticle());

			// Update or insert data from database
			statement.executeUpdate();

			if (insert)
			{
				generatedKeys = statement.getGeneratedKeys();

				if (generatedKeys.next()) article.setIdArticle(generatedKeys.getInt(1));
			}
		}
		catch (SQLException | ClassNotFoundException e)
		{
			closeConnection(statement, connection, WRITE_ERROR, e);
		}

		closeConnection(statement, connection, WRITE_ERROR, null);
	}

	/**
	 * Close the connection requirements that was needed to open it.
	 *
	 * @param statement  Statement to close or null if none.
	 * @param connection Connection to close or null if none.
	 * @param errorMsg   Message to add to the eventual exception.
	 * @param e1         Exception previously generated.
	 *
	 * @throws DALException In case of problem.
	 */

	private void closeConnection(Statement statement, Connection connection, String errorMsg, Throwable e1) throws DALException
	{
		StringBuilder str = new StringBuilder();

		// Try to close the statement if opened.
		if (statement != null)
		{
			try
			{
				statement.close();
			}
			catch (SQLException e2)
			{
				str.append(e2.getMessage()).append("\n");
			}
		}

		// Try to close the connection if opened.
		if (connection != null)
		{
			try
			{
				JDBCTools.closeConnexion();
			}
			catch (SQLException e3)
			{
				str.append(e3.getMessage()).append("\n");
			}
		}

		// A none empty string means there was an error previously generated.
		if (str.length() != 0)
		{
			// Add an eventually previous error message.
			if (e1 != null) str.append(e1.getMessage()).append("\n");

			throw new DALException(str.append(errorMsg).append("\n").toString());
		}
	}
}

