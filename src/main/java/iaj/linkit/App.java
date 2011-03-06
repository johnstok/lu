/*
 * This file is part of lu.
 *
 *  lu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  lu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with lu.  If not, see <http://www.gnu.org/licenses/>.
 */
package iaj.linkit;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.plist.XMLPropertyListConfiguration;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class App {

	private static void handleIni(final File f) {
		try {
			final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			for (String s=r.readLine(); null!=s; s=r.readLine()) {
				if (s.startsWith("URL=")) {
					print(s.substring(4).trim());
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
			print(plist.getString("URL"));
		} catch (final ConfigurationException e) {
			System.err.println("Failed to read "+f.getAbsolutePath()+" - "+e.getMessage());
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Configuration config = new Configuration();
		final CmdLineParser parser = new CmdLineParser(config);
		try {
			parser.parseArgument(args);

			final File f =
				(config.getPaths().size()>0) 
					? new File(config.getPaths().get(0)).getCanonicalFile()
					: new File(".").getCanonicalFile();
//			System.out.println("path: "+f.getCanonicalPath());
//			System.out.println();
			if (!f.exists()) {
				System.err.println("No such path: "+f.getCanonicalFile());
				System.exit(1);
			} else if (!f.canRead()) {
				System.err.println("Can't read path.");
				System.exit(1);
			} else if (f.isDirectory()) {
				handleFiles(config.isRecursive(), f.listFiles());
			} else {
				handleFiles(config.isRecursive(), f);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (final CmdLineException e) {
			System.err.println(e.getMessage());
			System.err.println("lu [options...] [path]");             //$NON-NLS-1$
			parser.printUsage(System.err);
			return;
		}
	}

	private static void handleFiles(boolean recurse, File... listFiles) {
		for (File f : listFiles) {
			if (f.getName().endsWith("webloc")) {
				handlePlist(f);
			} else if (f.getName().endsWith("URL")) {
				handleIni(f);
			} else if (f.getName().endsWith("desktop")) {
				handleIni(f);
			} else if (recurse && f.isDirectory() && !f.isHidden()) {
				handleFiles(recurse, f.listFiles());
			}
		}
	}
	
	

//	private static void open(final String urlString) {
//		try {
//			final java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
//			final java.net.URI uri = new java.net.URI(urlString);
//			desktop.browse( uri );
//		} catch (final URISyntaxException e) {
//			e.printStackTrace();
//		} catch (final IOException e) {
//			e.printStackTrace();
//		}
//	}

	private static void print(final String urlString) {
		System.out.println(urlString);
	}
}
