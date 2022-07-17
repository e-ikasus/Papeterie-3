package fr.eni.papeterie.bll;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;
import fr.eni.papeterie.dal.ArticleDAO;
import fr.eni.papeterie.dal.DALException;
import fr.eni.papeterie.dal.DAOFactory;

import java.util.List;

public class CatalogueManager
{
	private final String ERROR_GET_ARTICLES = "Impossible de récupérer le catalogue d'articles.\n";
	private final String ERROR_UPDATE_ARTICLE = "Impossible de mettre à jour l'article %d.\n";
	private final String ERROR_DELETE_ARTICLE = "Impossible de supprimer l'article %d.\n";
	private final String ERROR_ADD_ARTICLE = "Impossible d'ajouter l'article ";
	private final String ERROR_GET_ARTICLE = "Impossible de lire l'article %d.\n";

	private final String ERROR_REFERENCE_UNDEFINED = "00 - Référence de l'article non définie ou incorrecte.\n";
	private final String ERROR_MARQUE_UNDEFINED = "01 - Marque de l'article non définie ou incorrecte.\n";
	private final String ERROR_DESIGNATION_UNDEFINED = "02 - Désignation de l'article non définie ou incorrecte.\n";
	private final String ERROR_WRONG_PRICE = "04 - Prix de l'article incorrect.\n";
	private final String ERROR_WRONG_QUANTITY = "03 - Quantité de l'article incorrecte.\n";
	private final String ERROR_WRONG_GRAMMAGE = "Grammage de l'article incorrect.\n";
	private final String ERROR_WRONG_COLOR = "05 - Couleur de l'article non définie.\n";

	private final ArticleDAO daoArticle;
	private static CatalogueManager instance;

	/**
	 * Create a catalogue manager and retrieve the instance of the object used to
	 * communicate with the database.
	 */

	private CatalogueManager()
	{
		daoArticle = DAOFactory.getArticleDAO();
	}

	/**
	 * Get the catalogue manager instance.
	 *
	 * @return instance of the catalogue manager.
	 */

	public static CatalogueManager getInstance() throws BLLException
	{
		if (instance == null) instance = new CatalogueManager();

		return instance;
	}

	/**
	 * Read all the article from the database.
	 *
	 * @return List of articles.
	 *
	 * @throws BLLException In case of problem.
	 */

	public List<Article> getCatalogue() throws BLLException
	{
		try
		{
			return daoArticle.selectAll();
		}
		catch (DALException e)
		{
			throw new BLLException(ERROR_GET_ARTICLES, e);
		}
	}

	/**
	 * read all the articles from the catalogue that correspond to the type
	 * supplied in parameter.
	 *
	 * @param type Type of the articles to read from the database.
	 *
	 * @return List of articles.
	 *
	 * @throws BLLException In case of problem.
	 */

	public List<Article> getCatalogueByType(int type) throws BLLException
	{
		try
		{
			return daoArticle.selectByType(type);
		}
		catch (DALException e)
		{
			throw new BLLException(ERROR_GET_ARTICLES, e);
		}
	}

	/**
	 * Add an article to the database.
	 *
	 * @param article Article to add.
	 *
	 * @throws BLLException In case of problem.
	 */

	public void addArticle(Article article) throws BLLException
	{
		try
		{
			validerArticle(article);

			daoArticle.insert(article);
		}
		catch (DALException e)
		{
			throw new BLLException(ERROR_ADD_ARTICLE + article + "\n", e);
		}
	}

	/**
	 * Update an article from the database. the article must already exist in the
	 * database.
	 *
	 * @param article Article to update.
	 *
	 * @throws BLLException In case of problem.
	 */

	public void updateArticle(Article article) throws BLLException
	{
		try
		{
			validerArticle(article);

			daoArticle.update(article);
		}
		catch (DALException e)
		{
			throw new BLLException(String.format(ERROR_UPDATE_ARTICLE, article.getIdArticle()), e);
		}
	}

	/**
	 * Delete an article from the database.
	 *
	 * @param indexe Identyfier or the artocle to delete.
	 *
	 * @throws BLLException In case of problem.
	 */

	public void removeArticle(int indexe) throws BLLException
	{
		try
		{
			daoArticle.delete(indexe);
		}
		catch (DALException e)
		{
			throw new BLLException(ERROR_DELETE_ARTICLE, e);
		}
	}

	/**
	 * Read an article from the database.
	 *
	 * @param indexe Index of the article to read.
	 *
	 * @return Article requested or null if none exist.
	 *
	 * @throws BLLException In case of problem.
	 */

	public Article getArticle(int indexe) throws BLLException
	{
		try
		{
			return daoArticle.selectById(indexe);
		}
		catch (DALException e)
		{
			throw new BLLException(String.format(ERROR_GET_ARTICLE, indexe), e);
		}
	}

	/**
	 * Validate an article to allow further insertion ou update in the database.
	 *
	 * @param article Article to verify.
	 *
	 * @throws BLLException If something is wrong in the article.
	 */

	public void validerArticle(Article article) throws BLLException
	{
		StringBuilder msg = new StringBuilder();

		if ((article.getReference() == null) || (article.getReference().trim().length() == 0))
			msg.append(ERROR_REFERENCE_UNDEFINED);

		if ((article.getMarque() == null) || (article.getMarque().trim().length() == 0))
			msg.append(ERROR_MARQUE_UNDEFINED);

		if ((article.getDesignation() == null) || (article.getDesignation().trim().length() == 0))
			msg.append(ERROR_DESIGNATION_UNDEFINED);

		if (article.getPrixUnitaire() <= 0) msg.append(ERROR_WRONG_PRICE);

		if (article.getQteStock() <= 0) msg.append(ERROR_WRONG_QUANTITY);

		if ((article instanceof Ramette) && (((Ramette) article).getGrammage() <= 0))
			msg.append(ERROR_WRONG_GRAMMAGE);

		if ((article instanceof Stylo) && (((((Stylo) article).getCouleur()) == null) || ((((Stylo) article).getCouleur().trim().length()) == 0)))
			msg.append(ERROR_WRONG_COLOR);

		if (msg.length() != 0) throw new BLLException(msg.toString());
	}
}

