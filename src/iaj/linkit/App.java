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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		File f = new File("Eplsite Channels.desktop");
//		File f = new File("Skins - 4oD - Channel 4.URL");
		File f = new File("www.engadget.com:.webloc");
		
		if (f.getName().endsWith("webloc")) {
			handlePlist(f);
		} else if (f.getName().endsWith("URL")) {
			handleIni(f);
		} if (f.getName().endsWith("desktop")) {
			handleIni(f);
		}
	}

	private static void handleIni(File f) {
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			for (String s=r.readLine(); null!=s; s=r.readLine()) {
				if (s.startsWith("URL=")) {
					open(s.substring(4).trim());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handlePlist(File f) {
		try {
			XMLPropertyListConfiguration plist = new XMLPropertyListConfiguration(f);
			open(plist.getString("URL"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} 
	}
	
	private static void open(String urlString) {
		try {
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			java.net.URI uri = new java.net.URI(urlString);
			desktop.browse( uri );
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
