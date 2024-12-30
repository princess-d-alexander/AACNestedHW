import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents the mappings for a single category of items that should
 * be displayed.
 * 
 * @author Catie Baker & YOUR NAME HERE
 *
 */
public class AACCategory implements AACPage {

  // Category name
  private String categoryName;

  // List of image-location and text pairs
  public List<AACItem> items;

  /**
   * Creates a new empty category with the given name.
   * 
   * @param name the name of the category
   */
  public AACCategory(String name) {
    this.categoryName = name;
    this.items = new ArrayList<>();
  }

  /**
   * Adds the image location, text pairing to the category.
   * 
   * @param imageLoc the location of the image
   * @param text the text that the image should speak
   */
  public void addItem(String imageLoc, String text) {
    items.add(new AACItem(imageLoc, text));
  }

  /**
   * Returns an array of all the images in the category.
   * 
   * @return the array of image locations; if there are no images,
   *         it should return an empty array.
   */
  public String[] getImageLocs() {
    String[] imageLocs = new String[items.size()];
    for (int i = 0; i < items.size(); i++) {
      imageLocs[i] = items.get(i).getImageLoc();
    }
    return imageLocs;
  }

  /**
   * Returns the name of the category.
   * 
   * @return the name of the category.
   */
  public String getCategory() {
    return categoryName;
  }

  /**
   * Returns the text associated with the given image in this category.
   * 
   * @param imageLoc the location of the image
   * @return the text associated with the image
   * @throws NoSuchElementException if the image provided is not in the current
   *           category.
   */
  public String select(String imageLoc) {
    for (AACItem item : items) {
      if (item.getImageLoc().equals(imageLoc)) {
        return item.getText();
      }
    }
    throw new NoSuchElementException("Image not found in this category.");
  }

  /**
   * Determines if the provided image is stored in the category.
   * 
   * @param imageLoc the location of the category.
   * @return true if it is in the category, false otherwise.
   */
  public boolean hasImage(String imageLoc) {
    for (AACItem item : items) {
      if (item.getImageLoc().equals(imageLoc)) {
        return true;
      }
    }
    return false;
  }
}

/**
 * Helper class to store the image-location and associated text pair
 */
class AACItem {
  private String imageLoc;
  private String text;

  /**
   * Creates an AACItem with the given image location and associated text.
   * 
   * @param imageLoc the location of the image
   * @param text the text that the image should speak
   */
  public AACItem(String imageLoc, String text) {
    this.imageLoc = imageLoc;
    this.text = text;
  }

  /**
   * Returns the image location.
   * 
   * @return the image location.
   */
  public String getImageLoc() {
    return imageLoc;
  }

  /**
   * Returns the text associated with the image.
   * 
   * @return the text associated with the image.
   */
  public String getText() {
    return text;
  }
}