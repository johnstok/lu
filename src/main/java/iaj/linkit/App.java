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


/**
 * Main class for the 'lu' application.
 *
 * @author Keith Webster Johnston.
 */
public final class App {

    private static final String DIR_CURRENT       = ".";           //$NON-NLS-1$
    private static final String EXTENSION_DESKTOP = "desktop";     //$NON-NLS-1$
    private static final String EXTENSION_URL     = "URL";         //$NON-NLS-1$
    private static final String EXTENSION_WEBLOC  = "webloc";      //$NON-NLS-1$


    private App() { super(); }


    /**
     * Application entry method.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        final Configuration config = new Configuration();
        final CmdLineParser parser = new CmdLineParser(config);

        try {
            parser.parseArgument(args);

            final File f =
                (config.getPaths().size() > 0) ? new File(config
                    .getPaths()
                    .get(0)).getCanonicalFile() : new File(DIR_CURRENT)
                    .getCanonicalFile();
            if (!f.exists()) {
                System.err.println("No such path: " + f.getCanonicalFile());
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
            System.err.println("lu [options...] [paths...]");      //$NON-NLS-1$
            parser.printUsage(System.err);
            System.exit(1);
        }
    }


    private static void handleFiles(final boolean recurse,
                                    final File... listFiles) {
        for (File f : listFiles) {
            if (f.getName().endsWith(EXTENSION_WEBLOC)) {
                handlePlist(f);
            } else if (f.getName().endsWith(EXTENSION_URL)) {
                handleIni(f);
            } else if (f.getName().endsWith(EXTENSION_URL.toLowerCase())) {
                handleIni(f);
            } else if (f.getName().endsWith(EXTENSION_DESKTOP)) {
                handleIni(f);
            } else if (recurse && f.isDirectory() && !f.isHidden()) {
                handleFiles(recurse, f.listFiles());
            }
        }
    }


    private static void handleIni(final File f) {
        try {
            final BufferedReader r =
                new BufferedReader(
                    new InputStreamReader(new FileInputStream(f)));
            try {
                for (String s = r.readLine(); null != s; s = r.readLine()) {
                    if (s.startsWith("URL=")) {
                        print(s.substring(4).trim());
                    }
                }
            } finally {
                try {
                    r.close();
                } catch (Exception e) {
                    // Ignore & continue.
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
            final XMLPropertyListConfiguration plist =
                new XMLPropertyListConfiguration(f);
            print(plist.getString(EXTENSION_URL));
        } catch (final ConfigurationException e) {
            System.err.println("Failed to read "
                               + f.getAbsolutePath()
                               + " - "
                               + e.getMessage());
        }
    }


    private static void print(final String urlString) {
        System.out.println(urlString);
    }


//    private static void open(final String urlString) {
//        try {
//            final java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
//            final java.net.URI uri = new java.net.URI(urlString);
//            desktop.browse(uri);
//        } catch (final URISyntaxException e) {
//            e.printStackTrace();
//        } catch (final IOException e) {
//            e.printStackTrace();
//        }
//    }
}
