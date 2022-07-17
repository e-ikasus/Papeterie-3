package fr.eni.papeterie.ihm;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class StockUI extends JFrame implements Observer
{
	private static final String FORM_TITLE = "Liste des articles";
	private final ArticlesController articlesController;
	private final JPanel panel;
	private final StockUIRow stockUIRow;

	public StockUI()
	{
		articlesController = ArticlesController.getInstance();

		panel = (JPanel) getContentPane();

		// Close application when the form is closed.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// The table will take all the width.
		new BoxLayout(this, BoxLayout.LINE_AXIS);

		// Create the instance of a row.
		stockUIRow = new StockUIRow(articlesController.getArticles());

		// Create the table.
		JTable jTable = new JTable(stockUIRow);

		// Set the height of all the rows.
		jTable.setRowHeight(30);

		// The table occupies the entire height.
		jTable.setFillsViewportHeight(true);

		panel.add(jTable);
		panel.add(new JScrollPane(jTable));

		// Form is centered on the screen now, but will be shifted later.
		setLocationRelativeTo(null);

		setTitle(FORM_TITLE);
		setSize(new Dimension(450, 400));

		setVisible(true);
	}

	/**
	 * Update the table display to reflect changes in the articles list.
	 *
	 * @param o   The article list.
	 * @param arg The article index changed or null if the entire list.
	 */

	@Override
	public void update(Observable o, Object arg)
	{
		// Not really necessary for now, because the ArrayList instance is always
		// the same.
		stockUIRow.setArticles(((ArticlesObservable) o).articles);

		// Redraw the table on only one row.
		if (arg == null) stockUIRow.fireTableDataChanged();
		else stockUIRow.fireTableRowsUpdated((Integer) arg, (Integer) arg);
	}
}
