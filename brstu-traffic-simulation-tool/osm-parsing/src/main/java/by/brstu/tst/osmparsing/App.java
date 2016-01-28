package by.brstu.tst.osmparsing;

import de.topobyte.osm4j.core.access.OsmIterator;
import de.topobyte.osm4j.xml.dynsax.OsmXmlIterator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        if (args.length < 1) {
            System.out.println("Path to osm file should be provided!");
            return;
        }
        String fileName = args[0];
        try (InputStream osmStream = new FileInputStream(fileName)) {
            OsmIterator iterator = new OsmXmlIterator(osmStream, false);
            OsmXmlParser parser = new OsmXmlParser();
            OsmMapObjects mapObjects = parser.parse(iterator);
            MapRenderer renderer = new MapRenderer();
            renderer.render(mapObjects);
        }
        catch (FileNotFoundException exception) {
            System.out.println("File by given path was not found!");
            return;
        }
        catch (IOException exception) {
            System.out.println("Error during file processing!");
            return;
        }
        catch (Exception exception) {
            System.out.println("Error during map rendering!");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return;
        }
    }
}
