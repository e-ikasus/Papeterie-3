package fr.eni.papeterie.ihm;

import javax.swing.*;
import java.net.URL;

public class Utils
{
	// Path to the folder where the icons are.
	private final static String ASSETS_PATH = "assets/";

	/**
	 * Create an ImageIcon object.
	 *
	 * @param name name of the file containing the image.
	 *
	 * @return Object created or null in case of problem.
	 */

	public static ImageIcon createIcon(String name)
	{
		// Get the path to the image file.
		URL url = Utils.class.getResource(ASSETS_PATH + name);

		// And return the ImageIcon object if image is found.
		return (url != null) ? (new ImageIcon(url)) : (null);
	}
}
