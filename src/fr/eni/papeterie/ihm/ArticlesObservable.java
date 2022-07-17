package fr.eni.papeterie.ihm;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;

import java.util.List;
import java.util.Observable;

/**
 * This class represent a list of articles that can be observed by observers.
 * Because this class mimics some of Arraylist methods, it can be used
 * transparently, with only a few modifications to the code.
 */

public class ArticlesObservable extends Observable
{
	List<Article> articles;

	/**
	 * Create the observable and attach the supplied list to it.
	 *
	 * @param articles list of articles to attach.
	 */
	public ArticlesObservable(List<Article> articles)
	{
		this.articles = articles;
	}

	/**
	 * Set the list of articles this observable handle.
	 *
	 * @param articles list of article to handle.
	 */
	public void set(List<Article> articles)
	{
		this.articles = articles;

		// Mark the list changed.
		setChanged();

		// And inform the observers that the entire list changed.
		notifyObservers(null);
	}

	/**
	 * Return the list attached to this observable.
	 *
	 * @return List of articles handled
	 */

	public List<Article> list()
	{
		return articles;
	}

	/**
	 * Return the number of articles in the list.
	 *
	 * @return Number of articles.
	 */

	public int size()
	{
		return articles.size();
	}

	/**
	 * Return the article present at the specified index in the list.
	 *
	 * @param index Index of the article to get
	 *
	 * @return Article pointed by the index.
	 */

	public Article get(int index)
	{
		return articles.get(index);
	}

	/**
	 * Add an article to the list and inform observers that the list changed.
	 *
	 * @param article Article to add.
	 */

	public void add(Article article)
	{
		// Add the supplied article to the list.
		articles.add(article);

		// Mark the list changed.
		setChanged();

		// And inform the observers.
		notifyObservers(articles.size() - 1);
	}

	/**
	 * Remove an article to the list and inform observers that the list changed.
	 *
	 * @param index Index of the article top remove.
	 */

	public void remove(int index)
	{
		// Remove the supplied article to the list.
		articles.remove(index);

		// Mark the list changed.
		setChanged();

		// And inform the observers.
		notifyObservers(null);
	}

	/**
	 * Copy the second article to the first whose index is supplied in parameter.
	 * Note that the identifier of the destination article is not modified.
	 *
	 * @param index   Index of the destination article.
	 * @param article Source article.
	 */

	public void clone(int index, Article article)
	{
		Article toArticle = articles.get(index);

		toArticle.setDesignation(article.getDesignation());
		toArticle.setReference(article.getReference());
		toArticle.setMarque(article.getMarque());
		toArticle.setPrixUnitaire(article.getPrixUnitaire());
		toArticle.setQteStock(article.getQteStock());

		if (article instanceof Stylo)
			((Stylo) toArticle).setCouleur(((Stylo) article).getCouleur());
		else
			((Ramette) toArticle).setGrammage(((Ramette) article).getGrammage());

		// Mark the list changed.
		setChanged();

		// And inform the observers.
		notifyObservers(index);
	}
}
