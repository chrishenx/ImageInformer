package model;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.HashMap;


/**
 * Clase para obtener informacion sobre una imagen.
 * @author Christian González León
 * @version 1.0
 */
public class ImageProcessor {
  private BufferedImage image;
 
  private static final double LOG_OF_2;
  
  static {
    LOG_OF_2 = Math.log(2);
  }
  
  public ImageProcessor(BufferedImage image) {
    this.image = image;
  }
  
  public HashMap<Color, Integer> countColors() {
    if (image == null) return null;
    HashMap<Color, Integer> colorsCount = new HashMap<>();
    int width = image.getWidth();
    int height = image.getHeight();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Color rgb = new Color(image.getRGB(i, j));
        if (colorsCount.containsKey(rgb)) {
          colorsCount.put(rgb, colorsCount.get(rgb) + 1);
        } else {
          colorsCount.put(rgb, 1);
        }
      }
    }
    return colorsCount;
  }
  
  public ImageInformation calculateInformation() {
    HashMap<Color, Integer> colorsCount = countColors();
    double numPixels = image.getWidth() * image.getHeight();
    int information = 0;
    double entropy = 0;
    for (Integer colorOcurrences : colorsCount.values()) {
      double probability = colorOcurrences / numPixels; 
      if (probability > 0) {
        information += Math.ceil(log2(1 / probability));
        entropy += log2(1 / probability) * probability;
      }
    }
    return new ImageInformation(information, entropy);
  }
  
  public static double log2(double val) {
    return Math.log(val) / LOG_OF_2;
  }
  
  

}
