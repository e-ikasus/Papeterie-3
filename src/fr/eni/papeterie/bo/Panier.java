package fr.eni.papeterie.bo;

import java.util.ArrayList;
import java.util.List;

public class Panier
{
	private float montant;
	private List<Ligne> lignesPanier = new ArrayList<>();

	public float getMontant()
	{
		return montant;
	}

	public Ligne getLigne(int index)
	{
		return lignesPanier.get(index);
	}

	public List<Ligne> getLignes()
	{
		return lignesPanier;
	}

	public void addLigne(Article article, int qte)
	{
		montant += article.getPrixUnitaire() * qte;

		lignesPanier.add(new Ligne(article, qte));
	}

	public void updateLigne(int index, int newQte)
	{
		Ligne ligneToUpdate = lignesPanier.get(index);
		float prix = ligneToUpdate.getArticle().getPrixUnitaire();

		montant -= ligneToUpdate.getPrix();

		lignesPanier.get(index).setQte(newQte);

		montant += ligneToUpdate.getPrix();

	}

	public void removeLigne(int index)
	{
		montant -= lignesPanier.get(index).getQte() * lignesPanier.get(index).getArticle().getPrixUnitaire();

		lignesPanier.remove(index);
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();

		str.append("Panier:\n\n");

		for (int i = 0; i < lignesPanier.size(); i++) str.append(String.format("Ligne %d:    %s\n", i, lignesPanier.get(i)));

		str.append(String.format("%n%nValeur du panier : %.1f", montant));

		return str.toString();
	}
}
