package fr.eni.papeterie.ihm;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.util.Vector;

public class ArticlesUI extends JFrame
{
	// Text headers.
	private final static String TITLE_APP = "Papeterie : ";
	private final static String REFERENCE_HEADER = "Référence : ";
	private final static String DESIGNATION_HEADER = "Désignation : ";
	private final static String MANUFACTURER_HEADER = "Marque : ";
	private final static String QUANTITY_HEADER = "Stock : ";
	private final static String PRICE_HEADER = "Prix : ";
	private final static String TYPE_HEADER = "Type";
	private final static String WEIGHT_HEADER = "Grammage";
	private final static String COLOR_HEADER = "Couleur";
	private final static String WEIGHT80_HEADER = "80 Grammes";
	private final static String WEIGHT100_HEADER = "100 Grammes";
	private final static String PAPER_RB_HEADER = "Ramette";
	private final static String PENCIL_RB_HEADER = "Stylo";
	private final static String ERROR = "ERREUR";
	private final static String ERROR_EMPTY = "      ";

	// Attributes.
	private final static int JUSTIFY_LEFT = GridBagConstraints.LINE_START;

	// Number of visible chars per text field.
	private final static int VISIBLE_CHARS = 30;

	// The root panel objects.
	private final JPanel panel;

	// Text fields.
	private JTextField fldReference;
	private JTextField fldDesignation;
	private JTextField fldManufacturer;
	private JFormattedTextField fldQuantity;
	private JFormattedTextField fldPrice;

	// Radio buttons.
	private JRadioButton rbtnPaper;
	private JRadioButton rbtnPencil;

	// CheckBox.
	private JCheckBox chkWeight80;
	private JCheckBox chkWeight100;

	// Groups of UI objects.
	private final ButtonGroup btnGroupType = new ButtonGroup();
	private final ButtonGroup btnGroupWeight = new ButtonGroup();
	private JPanel actionsGroup;

	// List of colors.
	private Vector<String> colorsList;
	private JComboBox<String> colorSelector;

	// Instance of the controller.
	private final ArticlesController articlesController;

	// Instance of the action buttons component.
	private final ActionButtons actionButtons;

	// Error labels staff.
	private final JLabel[] errorLabels = new JLabel[7];

	/**
	 * Handler used to listen objects that obtain focus to clear the error label
	 * for indicating that the user modify that object to correct its content.
	 */

	FocusListener focusListener = new FocusListener()
	{
		@Override
		public void focusGained(FocusEvent e)
		{
			if (e.getSource() == fldReference) setErrorLabelState(0, false);
			else if (e.getSource() == fldDesignation) setErrorLabelState(1, false);
			else if (e.getSource() == fldManufacturer) setErrorLabelState(2, false);
			else if (e.getSource() == fldQuantity) setErrorLabelState(3, false);
			else if (e.getSource() == fldPrice) setErrorLabelState(4, false);
			else if (e.getSource() == colorSelector) setErrorLabelState(5, false);
		}

		@Override
		public void focusLost(FocusEvent e)
		{

		}
	};

	/**
	 * Get the instance of the ActionButtons component used by this interface.
	 */
		public ActionButtons ActionButtons() { return actionButtons; }

	/**
	 * Create a form that will be displayed later and containing al the necessary
	 * staff for creating, editing or viewing articles.
	 */

	public ArticlesUI()
	{
		int indexErrorLabel = 0;

		articlesController = ArticlesController.getInstance();

		actionButtons = new ActionButtons();

		panel = (JPanel) getContentPane();

		// Close application when the form is closed.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set the layout manager for the pane
		panel.setLayout(new GridBagLayout());

		setTitle(TITLE_APP);
		setSize(new Dimension(450, 400));

		// Form is centered on the screen.
		setLocationRelativeTo(null);

		addComponent(new JLabel(REFERENCE_HEADER), 0, 0, 1, 1, JUSTIFY_LEFT);
		addComponent(getFldReference(), 1, 0, 1);
		addComponent(getErrorLabel(indexErrorLabel++), 1, 1, 1, 1, JUSTIFY_LEFT);

		addComponent(new JLabel(DESIGNATION_HEADER), 0, 2, 1, 1, JUSTIFY_LEFT);
		addComponent(getFldDesignation(), 1, 2, 1);
		addComponent(getErrorLabel(indexErrorLabel++), 1, 3, 1, 1, JUSTIFY_LEFT);

		addComponent(new JLabel(MANUFACTURER_HEADER), 0, 4, 1, 1, JUSTIFY_LEFT);
		addComponent(getFldManufacturer(), 1, 4, 1);
		addComponent(getErrorLabel(indexErrorLabel++), 1, 5, 1, 1, JUSTIFY_LEFT);

		addComponent(new JLabel(QUANTITY_HEADER), 0, 6, 1, 1, JUSTIFY_LEFT);
		addComponent(getFldQuantity(), 1, 6, 1);
		addComponent(getErrorLabel(indexErrorLabel++), 1, 7, 1, 1, JUSTIFY_LEFT);

		addComponent(new JLabel(PRICE_HEADER), 0, 8, 1, 1, JUSTIFY_LEFT);
		addComponent(getFldPrice(), 1, 8, 1);
		addComponent(getErrorLabel(indexErrorLabel++), 1, 9, 1, 1, JUSTIFY_LEFT);

		addComponent(new JLabel(TYPE_HEADER), 0, 10, 1, 2, JUSTIFY_LEFT);
		addComponent(getRbtnPaper(), 1, 10, 1, 1, JUSTIFY_LEFT);
		addComponent(getRbtnPencil(), 1, 11, 1, 1, JUSTIFY_LEFT);

		addComponent(new JLabel(WEIGHT_HEADER), 0, 12, 1, 2, JUSTIFY_LEFT);
		addComponent(getChkWeight80(), 1, 12, 1, 1, JUSTIFY_LEFT);
		addComponent(getChkWeight100(), 1, 13, 1, 1, JUSTIFY_LEFT);

		addComponent(new JLabel(COLOR_HEADER), 0, 14, 1, 1, JUSTIFY_LEFT);
		addComponent(getColorSelector(), 1, 14, 1, 1, JUSTIFY_LEFT);
		addComponent(getErrorLabel(indexErrorLabel), 1, 15, 1, 1, JUSTIFY_LEFT);

		addComponent(actionButtons, 0, 16, 2);

		setVisible(true);
	}

	/**
	 * Create a color selector to select a color for pencil article type. This
	 * list of colors comes from the already defined pencils in the database.
	 *
	 * @return A color selector.
	 */

	private JComboBox<String> getColorSelector()
	{
		if (colorSelector == null)
		{
			colorsList = articlesController.getColorsList();

			// Create the list.
			colorSelector = new JComboBox<>(colorsList);
			// And mark it as editable.
			colorSelector.setEditable(true);
		}

		return colorSelector;
	}

	/**
	 * Create the paper button. Create the radio typed button for selected the
	 * paper type of the item.
	 *
	 * @return Button related to the paper.
	 */

	private JRadioButton getRbtnPaper()
	{
		if (rbtnPaper == null)
		{
			rbtnPaper = new JRadioButton(PAPER_RB_HEADER);
			rbtnPaper.addActionListener(this::handleHide);

			btnGroupType.add(rbtnPaper);
		}

		return rbtnPaper;
	}

	/**
	 * Create the pencil button. Create the radio typed button for selected the
	 * pencil type of the item.
	 *
	 * @return Button related to the pencil.
	 */

	private JRadioButton getRbtnPencil()
	{
		if (rbtnPencil == null)
		{
			rbtnPencil = new JRadioButton(PENCIL_RB_HEADER);
			rbtnPencil.addActionListener(this::handleHide);

			btnGroupType.add(rbtnPencil);
		}

		return rbtnPencil;
	}

	/**
	 * Create the weight paper button. Create the radio typed button for selected
	 * the 80g paper weight of the item.
	 *
	 * @return Button related to the 80g paper weight.
	 */

	private JCheckBox getChkWeight80()
	{
		if (chkWeight80 == null) chkWeight80 = new JCheckBox(WEIGHT80_HEADER);

		btnGroupWeight.add(chkWeight80);

		return chkWeight80;
	}

	/**
	 * Create the weight paper button. Create the radio typed button for selected
	 * the 100g paper weight of the item.
	 *
	 * @return Button related to the 100g paper weight.
	 */

	private JCheckBox getChkWeight100()
	{
		if (chkWeight100 == null) chkWeight100 = new JCheckBox(WEIGHT100_HEADER);

		btnGroupWeight.add(chkWeight100);

		return chkWeight100;
	}

	/**
	 * Create the field text object for editing the item reference. The
	 * VISIBLE_CHARS constant determine the length of the field displayed, not the
	 * length of its content.
	 *
	 * @return Created text editing object.
	 */

	private JTextField getFldReference()
	{
		if (fldReference == null)
		{
			fldReference = new JTextField(VISIBLE_CHARS);
			fldReference.addFocusListener(focusListener);
		}

		return fldReference;
	}

	/**
	 * Create the field text object for editing the item designation. The
	 * VISIBLE_CHARS constant determine the length of the field displayed, not the
	 * length of its content.
	 *
	 * @return Created text editing object.
	 */

	private JTextField getFldDesignation()
	{
		if (fldDesignation == null)
		{
			fldDesignation = new JTextField(VISIBLE_CHARS);
			fldDesignation.addFocusListener(focusListener);
		}

		return fldDesignation;
	}

	/**
	 * Create the field text object for editing the item manufacturer. The
	 * VISIBLE_CHARS constant determine the length of the field displayed, not the
	 * length of its content.
	 *
	 * @return Created text editing object.
	 */

	private JTextField getFldManufacturer()
	{
		if (fldManufacturer == null)
		{
			fldManufacturer = new JTextField(VISIBLE_CHARS);
			fldManufacturer.addFocusListener(focusListener);
		}

		return fldManufacturer;
	}

	/**
	 * Create the field text object for editing the item quantity in stock. The
	 * VISIBLE_CHARS constant determine the length of the field displayed, not the
	 * length of its content.
	 *
	 * @return Created text editing object.
	 */

	private JFormattedTextField getFldQuantity()
	{
		if (fldQuantity == null)
		{
			fldQuantity = new JFormattedTextField(0);
			fldQuantity.setColumns(VISIBLE_CHARS);
			fldQuantity.addFocusListener(focusListener);
		}

		return fldQuantity;
	}

	/**
	 * Create the field text object for editing the item price. The VISIBLE_CHARS
	 * constant determine the length of the field displayed, not the length of its
	 * content.
	 *
	 * @return Created text editing object.
	 */

	private JFormattedTextField getFldPrice()
	{
		if (fldPrice == null)
		{
			fldPrice = new JFormattedTextField(0f);
			fldPrice.setColumns(VISIBLE_CHARS);
			fldPrice.addFocusListener(focusListener);
		}

		return fldPrice;
	}

	/**
	 * Create an error label that is invisible by default. An error label is a
	 * small text placed under a text field to warn the user it has entered a
	 * wrong data.
	 *
	 * @param index Index of the label to create
	 *
	 * @return Created label.
	 */

	private JLabel getErrorLabel(int index)
	{
		if (errorLabels[index] == null)
		{
			errorLabels[index] = new JLabel();

			errorLabels[index].setFont(new Font("Serif", Font.PLAIN, 8));

			errorLabels[index].setForeground(Color.RED);

			setErrorLabelState(index, false);
		}

		return errorLabels[index];
	}

	/**
	 * Set the state of an error label. An error label is a small text placed
	 * under a text field to warn the user it has entered a wrong data.
	 *
	 * @param index Index of the label.
	 * @param state State of the label (visible or not).
	 */

	private void setErrorLabelState(int index, boolean state)
	{
		errorLabels[index].setText((state) ? (ERROR) : (ERROR_EMPTY));
	}

	/**
	 * Add an object into the form at the specified place. The object can span on
	 * multiple columns and rows
	 *
	 * @param object Object to add.
	 * @param x      Column number.
	 * @param y      Row number.
	 * @param attrs  Index 0 = width in columns number, index 1 = height in rows
	 *               number, index 2 = position in the cell.
	 */

	private void addComponent(JComponent object, int x, int y, int... attrs)
	{
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		int nbrParams = attrs.length;

		// Position of the object.
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;

		// Default values for that object.
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;

		// Change the desired default value.
		if (nbrParams >= 1) gridBagConstraints.gridwidth = attrs[0];
		if (nbrParams >= 2) gridBagConstraints.gridheight = attrs[1];
		if (nbrParams >= 3) gridBagConstraints.anchor = attrs[2];

		// Now, put the object at the desired place.
		panel.add(object, gridBagConstraints);
	}

	/**
	 * Update the form content to reflect the article supplied as parameter.
	 *
	 * @param article     Article to display.
	 * @param listChanged Indicateif the colors list need to be done again.
	 */

	public void updateCurrent(Article article, boolean listChanged)
	{
		boolean type, weight;
		String str;
		int colorIndex;

		// Determine if the colors list need to be red again.
		if (listChanged)
		{
			// Remove all colors in the selector.
			getColorSelector().removeAllItems();

			// Read the new colors list.
			colorsList = articlesController.getColorsList();

			// Add the new list to the selector.
			for (String color : colorsList) getColorSelector().addItem(color);
		}

		// Determine the type and weight of the article.
		weight = (type = (article instanceof Ramette)) && (((Ramette) article).getGrammage() == 80);

		// When displaying an article, it is not possible to modify its type.
		getRbtnPaper().setEnabled(false);
		getRbtnPencil().setEnabled(false);

		// Set the commun attributes.
		getFldReference().setText(article.getReference());
		getFldDesignation().setText(article.getDesignation());
		getFldManufacturer().setText(article.getMarque());
		getFldQuantity().setText(String.valueOf(article.getQteStock()));
		getFldPrice().setText(String.valueOf(article.getPrixUnitaire()));

		// Update the type.
		getRbtnPaper().setSelected(type);
		getRbtnPencil().setSelected(!type);

		// Update the weight.
		getChkWeight80().setSelected(type && weight);
		getChkWeight100().setSelected(type && !weight);

		getChkWeight80().setEnabled(type);
		getChkWeight100().setEnabled(type);
		getColorSelector().setEnabled(!type);

		// By default, no color is selected
		colorIndex = -1;

		// If the article is a pencil, his color is in the list.
		if (!type)
		{
			// Color of the pencil to find in the list to compute his index.
			str = ((Stylo) article).getCouleur();

			// Search the color in the list.
			for (colorIndex = 0; colorIndex < colorsList.size(); colorIndex++)
				if (colorsList.get(colorIndex).equals(str)) break;

			// Let the index unmodified if the color is found.
			if (colorIndex >= colorsList.size()) colorIndex = -1;
		}

		// Set the color.
		getColorSelector().setSelectedIndex(colorIndex);
	}

	/**
	 * Prepare the form to create a new article by initialising all text fields,
	 * button states and color selector.
	 *
	 * @param pencil Type of article to create by default.
	 */

	public void prepareForNew(boolean pencil)
	{
		// Enable buttons to select the article type.
		getRbtnPencil().setEnabled(true);
		getRbtnPaper().setEnabled(true);

		// By default, prepare for creating an article of supplied type.
		getRbtnPencil().setSelected(pencil);
		getRbtnPaper().setSelected(!pencil);
		getColorSelector().setEnabled(pencil);
		getColorSelector().setSelectedIndex(0);

		getChkWeight80().setEnabled(!pencil);
		getChkWeight100().setEnabled(!pencil);
		getChkWeight80().setSelected(!pencil);
		getChkWeight100().setSelected(!pencil);

		// Set the commun attributes.
		getFldReference().setText("");
		getFldDesignation().setText("");
		getFldManufacturer().setText("");
		getFldQuantity().setText(String.valueOf(0));
		getFldPrice().setText(String.valueOf(0));
	}

	/**
	 * Retrieve all information from the user interface objects and construct an
	 * article to be inserted or update in the database. At this stage, for an
	 * updating, it is not possible to retrieve the identifier or the article to
	 * update.
	 */

	public Article prepareToSave()
	{
		Article article;

		String reference = fldReference.getText();
		String designation = fldDesignation.getText();
		String manufacturer = fldManufacturer.getText();
		int stock = Integer.parseInt(fldQuantity.getText());
		float price = Float.parseFloat(fldPrice.getText());
		int weight = (chkWeight80.isSelected()) ? (80) : (100);
		String color;

		if (getColorSelector().getSelectedItem() != null)
			color = getColorSelector().getSelectedItem().toString();
		else
			color = "";

		if (getRbtnPencil().isSelected())
			article = new Stylo(manufacturer, reference, designation, price, stock, color);
		else
			article = new Ramette(manufacturer, reference, designation, price, stock, weight);

		// Article to save or update.
		return article;
	}

	/**
	 * Inform the user about eventually wrong data entered in the form.
	 */

	public void showErrors(java.util.List<Integer> errors)
	{
		for (Integer error : errors) setErrorLabelState(error, true);
	}

	/**
	 * Determine the state of components depending on the current article edited.
	 *
	 * @param e Triggered event.
	 */

	private void handleHide(ActionEvent e)
	{
		// Determine the components states.
		boolean state = (e.getSource() == rbtnPaper);

		// Components for paper.
		chkWeight80.setEnabled(state);
		chkWeight100.setEnabled(state);
		chkWeight80.setSelected(state);
		chkWeight100.setSelected(false);

		// Components for pencil.
		colorSelector.setEnabled(!state);
		colorSelector.setSelectedIndex(-1);
	}
}
