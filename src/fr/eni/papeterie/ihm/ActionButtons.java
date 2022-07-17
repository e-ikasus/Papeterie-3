package fr.eni.papeterie.ihm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ActionButtons extends JPanel implements ActionListener
{
	// Icons toolbox.
	private final static String BACKWARD_ICON = "Back24.gif";
	private final static String NEW_ICON = "New24.gif";
	private final static String SAVE_ICON = "Save24.gif";
	private final static String DELETE_ICON = "Delete24.gif";
	private final static String FORWARD_ICON = "Forward24.gif";

	// Path to the folder where the icons are.
	private final static String ASSETS_PATH = "assets/";

	// Action buttons.
	private JButton backButton;
	private JButton newButton;
	private JButton saveButton;
	private JButton delButton;
	private JButton forButton;

	// List of registered observers.
	List<ActionButtonsObserver> observers;

	public ActionButtons()
	{
		// Create the observers list.
		observers = new ArrayList<>();

		// Create an instance of each button and add them to the group.
		add(backButton = (new JButton(Utils.createIcon(BACKWARD_ICON))));
		add(newButton = (new JButton(Utils.createIcon(NEW_ICON))));
		add(saveButton = (new JButton(Utils.createIcon(SAVE_ICON))));
		add(delButton = (new JButton(Utils.createIcon(DELETE_ICON))));
		add(forButton = (new JButton(Utils.createIcon(FORWARD_ICON))));

		// Add action handler on all action buttons
		backButton.addActionListener(this);
		newButton.addActionListener(this);
		saveButton.addActionListener(this);
		delButton.addActionListener(this);
		forButton.addActionListener(this);
	}

	/**
	 * Handle action buttons. Each time a button is pressed, the observers that
	 * are registered will be notified.
	 *
	 * @param e Triggered event.
	 */

	@Override
	public void actionPerformed(ActionEvent e)
	{
		ActionButtonEnum action;

		JComponent object = (JComponent) e.getSource();

		if (object == forButton) action = ActionButtonEnum.NEXT;
		else if (object == backButton) action = ActionButtonEnum.PREVIOUS;
		else if (object == newButton) action = ActionButtonEnum.NEW;
		else if (object == delButton) action = ActionButtonEnum.DELETE;
		else action = ActionButtonEnum.SAVE;

		for (ActionButtonsObserver observer : observers)
			observer.notifyAction(action);
	}

	/**
	 * Register an observer to this observable. Once added, every action made on
	 * the buttons will be notified to this observer.
	 *
	 * @param observer Observer to add.
	 */

	public void register(ActionButtonsObserver observer)
	{
		observers.add(observer);
	}

	/**
	 * Unregister an observer. Once done, this observer will not be informed of
	 * action on buttons.
	 *
	 * @param observer Observer to unregister.
	 */

	public void unRegister(ActionButtonsObserver observer)
	{
		observers.remove(observer);
	}
}
