package fr.eni.papeterie.ihm;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Stylo;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class StockUIRow extends AbstractTableModel
{
	String[] headers = {"Type", "Reference", "Marque", "Désignation", "Prix Unitaire", "Quantité"};

	private final static String PENCIL_ICON = "pencil.gif";
	private final static String PAPER_ICON = "ramette.gif";

	List<Article> articles;

	private ImageIcon pencilImg;
	private ImageIcon paperImg;

	public StockUIRow(List<Article> articles)
	{
		super();

		this.articles = articles;

		pencilImg = Utils.createIcon(PENCIL_ICON);
		paperImg = Utils.createIcon(PAPER_ICON);
	}

	public List<Article> getArticles()
	{
		return articles;
	}

	public void setArticles(List<Article> articles)
	{
		this.articles = articles;
	}

	@Override
	public int getRowCount()
	{
		return articles.size();
	}

	@Override
	public String getColumnName(int column)
	{
		return headers[column];
	}

	@Override
	public int getColumnCount()
	{
		return headers.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Article article = articles.get(rowIndex);

		if (columnIndex == 0) return (article instanceof Stylo) ? (pencilImg) : (paperImg);
		else if (columnIndex == 1) return article.getReference();
		else if (columnIndex == 2) return article.getMarque();
		else if (columnIndex == 3) return article.getDesignation();
		else if (columnIndex == 4) return article.getPrixUnitaire();
		else if (columnIndex == 5) return article.getQteStock();
		else return "";
	}

	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}
