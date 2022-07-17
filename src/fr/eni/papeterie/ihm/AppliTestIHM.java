package fr.eni.papeterie.ihm;

import javax.swing.*;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class AppliTestIHM
{
	public static void main(String[] args) throws UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(new NimbusLookAndFeel());

		SwingUtilities.invokeLater(() -> ArticlesController.getInstance().start());
	}
}
