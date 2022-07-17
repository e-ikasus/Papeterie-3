package fr.eni.papeterie.ihm;

import fr.eni.papeterie.bll.BLLException;
import fr.eni.papeterie.bll.CatalogueManager;
import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class ArticlesController implements ActionButtonsObserver
{
	private static ArticlesController instance;

	private CatalogueManager catalogueManager;

	private ArticlesObservable articles;

	private ArticlesUI articlesUI;

	private StockUI stockUI;

	private int currentDrawn;

	private boolean createMode;

	private ArticlesController()
	{
	}

	/**
	 * Get the instance of the articles' controller. If it is not already
	 * instantiate, do this.
	 *
	 * @return instance of the articles' controller.
	 */

	public static ArticlesController getInstance()
	{
		if (instance == null) instance = new ArticlesController();

		return instance;
	}

	/**
	 * initialise the ArticleController, read the list of articles present in the
	 * database and display the form. The first article in the list is displayed.
	 * If the list is empty, the creation mode is entered.
	 */

	public void start()
	{
		if (instance != null)
		{
			try
			{
				// Retrieve the CatalogueManager.
				catalogueManager = CatalogueManager.getInstance();

				// Retrieve the articles from the database.
				articles = new ArticlesObservable(catalogueManager.getCatalogue());

				// Display the GUI.
				articlesUI = new ArticlesUI();
				stockUI = new StockUI();

				// Register the controller to the action buttons.
				articlesUI.ActionButtons().register(this);

				// Add the list form to the articles list observers.
				articles.addObserver(stockUI);

				// First article displayed.
				currentDrawn = 0;

				// Update the current article if exist or prepare it to create one.
				updateForm((articles.size() == 0), false);
			}
			catch (BLLException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return
	 */

	public List<Article> getArticles()
	{
		return articles.list();

	}

	/**
	 * Display the next article. If the last is already displayed, then display
	 * the first in the list.
	 */

	private void nextArticle()
	{
		// If there is zero or one article in the list, do nothing.
		if (articles.size() <= 1) return;

		// Determine the next index.
		if (++currentDrawn >= articles.size()) currentDrawn = 0;

		// And display the next article then.
		updateForm(false, false);
	}

	/**
	 * Display the previous article. If the first is already displayed, then
	 * display the last in the list.
	 */

	private void previousArticle()
	{
		// If there is zero or one article in the list, do nothing.
		if (articles.size() <= 1) return;

		// Determine the previous index.
		if (--currentDrawn < 0) currentDrawn = articles.size() - 1;

		// And display the previous article then.
		updateForm(false, false);
	}

	/**
	 * Delete the current article. After that, the next article is displayed. If
	 * it is the last that was deleted, the first is then displayed. If there is
	 * no article available, then the creation mode is entered.
	 */

	private void deleteArticle()
	{
		// In creation mode, it is impossible to delete one article.
		if (createMode) return;

		try
		{
			// Retrieve the article to delete.
			Article toDelete = articles.get(currentDrawn);

			// Remove it from the database.
			catalogueManager.removeArticle(toDelete.getIdArticle());

			// Remove it from the list.
			articles.remove(currentDrawn);

			// Check the next to display.
			if (currentDrawn >= articles.size()) currentDrawn = 0;

			// And display it.
			updateForm(false, true);
		}
		catch (BLLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Add or update an article to the database. If errors occurred during process
	 * that correspond to wrong data entered by the user, the interface will
	 * display them.
	 */

	private void addArticle()
	{
		List<Integer> errors = new ArrayList<>();

		// Retrieve information from user interface.
		Article article = articlesUI.prepareToSave();

		try
		{
			// Check the validity to the article to add/update.
			catalogueManager.validerArticle(article);
		}
		catch (BLLException e)
		{
			Scanner scan = new Scanner(e.getMessage());

			while (scan.hasNextLine())
				errors.add(Integer.valueOf(scan.nextLine().substring(0, 2)));

			scan.close();

			e.printStackTrace();
		}

		// If there is no error in the user data entered, try to add it to the db.
		if (errors.size() == 0)
		{
			try
			{
				if (createMode)
				{
					// Add the article to the database.
					catalogueManager.addArticle(article);

					// And now to the list or articles.
					articles.add(article);

					// The newly added article is always the last.
					currentDrawn = articles.size() - 1;
				}
				else
				{
					// Retrieve the id article to update.
					article.setIdArticle(articles.get(currentDrawn).getIdArticle());

					// And update it.
					catalogueManager.updateArticle(article);

					// update now the articles list.
					articles.clone(currentDrawn, article);
				}

				// And update the form.
				updateForm(false, true);
			}
			catch (BLLException e)
			{
				e.getMessage();
			}
		}

		// Display possible errors.
		articlesUI.showErrors(errors);
	}

	/**
	 * Enter the creation mode to add a new  article and prepare the form.
	 */

	private void newArticle()
	{
		updateForm(true, false);
	}

	/**
	 * Retrieve the colors list from the database used by pencil articles.
	 *
	 * @return List of colors.
	 */

	public Vector<String> getColorsList()
	{
		Vector<String> colorsList = new Vector<>();
		String color;
		int i;

		// For each article (pencil) red from the database.
		for (Article article : articles.list())
		{
			// Only take care about pencils.
			if (!(article instanceof Stylo)) continue;

			// Get the color attribute.
			color = ((Stylo) article).getCouleur();

			// Search if the color name is already in the list.
			for (i = 0; i < colorsList.size(); i++)
				if (colorsList.get(i).equals(color)) break;

			// If not in the list, add it.
			if (i >= colorsList.size()) colorsList.add(color);
		}

		// Return the list of colors.
		return colorsList;
	}

	/**
	 * Update the form according to the view mode passed as parameter and the
	 * current article to display.
	 *
	 * @param mode New mode to enter.
	 */

	private void updateForm(boolean mode, boolean listChanged)
	{
		// Change the display mode.
		createMode = (mode) | (articles.size() == 0);

		// Update the current article if exist or prepare to create one.
		if (!createMode)
			articlesUI.updateCurrent(articles.get(currentDrawn), listChanged);
		else articlesUI.prepareForNew(true);
	}

	/**
	 * Catch events on action buttons and perform appropriate action.
	 *
	 * @param action Action asked by the user.
	 */

	@Override
	public void notifyAction(ActionButtonEnum action)
	{
		if (action == ActionButtonEnum.NEXT) nextArticle();
		else if (action == ActionButtonEnum.PREVIOUS) previousArticle();
		else if (action == ActionButtonEnum.NEW) newArticle();
		else if (action == ActionButtonEnum.DELETE) deleteArticle();
		else addArticle();
	}
}
