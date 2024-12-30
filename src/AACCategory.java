import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;


/**
 * Represents the mappings for a single category of items that should
 * be displayed.
 * 
 * @author Catie Baker & YOUR NAME HERE
 *
 */
public class AACCategory implements AACPage {

  // Name of the category
  public String categoryName;

  // Associative array mapping image locations to text
  public AssociativeArray<String, String> items;

  /**
   * Creates a new empty category with the given name.
   * 
   * @param name the name of the category
   */
  public AACCategory(String name) {
    this.categoryName = name;
    this.items = new AssociativeArray<>();
  }

  /**
   * Adds the image location and text pairing to the category.
   * 
   * @param imageLoc the location of the image
   * @param text the text that the image should speak
   */
  @Override
  public void addItem(String imageLoc, String text) {
    if (imageLoc == null || text == null) {
      throw new IllegalArgumentException("Image location and text cannot be null.");
    }
    items.set(imageLoc, text); // Add to the associative array
  }

  /**
   * Returns an array of all the images in the category.
   * 
   * @return the array of image locations; if there are no images,
   *         it should return an empty array.
   */
  @Override
  public String[] getImageLocs() {
    String[] imageLocs = new String[items.size()];
    int index = 0;

    // Retrieve all keys (image locations)
    for (int i = 0; i < items.size(); i++) {
      try {
        imageLocs[index++] = items.pairs[i].key; // Access each key
      } catch (Exception e) {
        throw new RuntimeException("Error retrieving image locations.", e);
      }
    }
    return imageLocs;
  }

  /**
   * Returns the name of the category.
   * 
   * @return the name of the category.
   */
  @Override
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
  @Override
  public String select(String imageLoc) {
    if (items.hasKey(imageLoc)) {
      return items.get(imageLoc);
    }
    throw new NoSuchElementException("Image not found in this category.");
  }

  /**
   * Determines if the provided image is stored in the category.
   * 
   * @param imageLoc the location of the image
   * @return true if it is in the category, false otherwise
   */
  @Override
  public boolean hasImage(String imageLoc) {
    return items.hasKey(imageLoc);
  }
}
