package iaj.linkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.plist.XMLPropertyListConfiguration;

public class App {

	private static void handleIni(final File f) {
		try {
			final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			for (String s=r.readLine(); null!=s; s=r.readLine()) {
				if (s.startsWith("URL=")) {
					open(s.substring(4).trim());
				}
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private static void handlePlist(final File f) {
		try {
			final XMLPropertyListConfiguration plist = new XMLPropertyListConfiguration(f);
			open(plist.getString("URL"));
		} catch (final ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final File f = new File(args[0]);

		if (f.getName().endsWith("webloc")) {
			handlePlist(f);
		} else if (f.getName().endsWith("URL")) {
			handleIni(f);
		} if (f.getName().endsWith("desktop")) {
			handleIni(f);
		}
	}

	private static void open(final String urlString) {
		try {
			final java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			final java.net.URI uri = new java.net.URI(urlString);
			desktop.browse( uri );
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
