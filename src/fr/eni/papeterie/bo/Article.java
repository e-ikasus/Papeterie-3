package fr.eni.papeterie.bo;

public abstract class Article
{
	private Integer idArticle;
	private String reference;
	private String marque;
	private String designation;
	private float prixUnitaire;
	private int qteStock;

	public Integer getIdArticle()
	{
		return idArticle;
	}

	public void setIdArticle(Integer idArticle)
	{
		this.idArticle = idArticle;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
	}

	public String getMarque()
	{
		return marque;
	}

	public void setMarque(String marque)
	{
		this.marque = marque;
	}

	public String getDesignation()
	{
		return designation;
	}

	public void setDesignation(String designation)
	{
		this.designation = designation;
	}

	public float getPrixUnitaire()
	{
		return prixUnitaire;
	}

	public void setPrixUnitaire(float prixUnitaire)
	{
		this.prixUnitaire = prixUnitaire;
	}

	public int getQteStock()
	{
		return qteStock;
	}

	public void setQteStock(int qteStock)
	{
		this.qteStock = qteStock;
	}

	public Article(Integer idArticle, String marque, String ref, String designation, float pu, int qte)
	{
		this.idArticle = idArticle;
		this.reference = ref;
		this.marque = marque;
		this.designation = designation;
		this.prixUnitaire = pu;
		this.qteStock = qte;
	}

	public Article(String marque, String ref, String designation, float pu, int qte)
	{
		this.reference = ref;
		this.marque = marque;
		this.designation = designation;
		this.prixUnitaire = pu;
		this.qteStock = qte;
	}

	@Override
	public String toString()
	{
		return "Article{" +
						"idArticle=" + idArticle +
						", reference='" + reference + '\'' +
						", marque='" + marque + '\'' +
						", designation='" + designation + '\'' +
						", prixUnitaire=" + prixUnitaire +
						", qteStock=" + qteStock +
						'}';
	}
}
