package by.brstu.tst.ui.simulation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.HashMap;

/**
 * Created by kwetril on 9/5/16.
 */
public class MapImageCache {
    private HashMap<Integer, Image> cache;

    public void MapImageCache() {
        cache = new HashMap<>();
    }

    public Image getImage(int scaleIndex) {
        if (cache == null) {
            cache = new HashMap<>();
        }
        return cache.get(scaleIndex);
    }

    public void putImage(int scaleIndex, Image image) {
        cache.put(scaleIndex, image);
    }

    public void reset() {
        cache.clear();
    }
}
