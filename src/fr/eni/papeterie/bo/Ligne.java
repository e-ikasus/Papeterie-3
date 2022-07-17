package fr.eni.papeterie.bo;

public class Ligne
{
	private int qte;

	private Article article;

	public int getQte()
	{
		return qte;
	}

	public void setQte(int qte)
	{
		this.qte = qte;
	}

	public Article getArticle()
	{
		return article;
	}

	public void setArticle(Article article)
	{
		this.article = article;
	}

	public float getPrix()
	{
		return qte * article.getPrixUnitaire();
	}

	public Ligne(Article article, int qte)
	{
		this.qte = qte;
		this.article = article;
	}

	@Override
	public String toString()
	{
		return "Ligne{" +
						"qte=" + qte +
						", prix=" + article.getPrixUnitaire() +
						", Article=" + article +
						'}';
	}
}
