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


import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;


/**
 * Command line configuration.
 *
 * @author Keith Webster Johnston.
 */
public class Configuration {

    @Option(name = "-r", usage = "Recurse into sub-directories.")
    private boolean _recursive;

    @Argument(multiValued = true, usage = "Zero or more file-system paths")
    private final List<String> _paths = new ArrayList<String>();


    /**
     * Accessor.
     *
     * @return True if the app should recurse into sub-directories; false
     *  otherwise.
     */
    public boolean isRecursive() {
        return _recursive;
    }


    /**
     * Accessor.
     *
     * @return The paths to scan for URLs.
     */
    public List<String> getPaths() {
        return _paths;
    }
}
