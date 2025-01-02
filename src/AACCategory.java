import java.util.NoSuchElementException;
import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;


/**
 * Represents the mappings for a single category of items that should
 * be displayed.
 *
 * @author Catie Baker & Princess Alexander
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
  } // AACCategory

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
    } // if
    try {
      // Attempt to add the item to the associative array
      items.set(imageLoc, text); // Add to the associative array
    } catch (NullKeyException e) {
      System.err.println("Error: Attempt to add item with a null key - " + e.getMessage());
    } // try/catch
  } // addItem

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
    for (String key : items.keys()) {
      imageLocs[index++] = key;
    } // for
    return imageLocs;
  } // getImageLocs

  /**
  * Returns the name of the category.
  *
  * @return The name of the category.
  */
  @Override
  public String getCategory() {
    if (categoryName == null || categoryName.trim().isEmpty()) {
      return "Unnamed Category";  // if no category name is set
    } // if
    return categoryName;
  } // getCategory()


  /**
   * Returns the text associated with the given image in this category.
   *
   * @param imageLoc the location of the image
   * @return the text associated with the image
   * @throws NoSuchElementException if the image provided is not in the current
   *         category.
   */
  @Override
  public String select(String imageLoc) {
    try {
      // Attempt to retrieve the text associated with the image location
      return items.get(imageLoc);
    } catch (KeyNotFoundException e) {
      // If the image location is not found, throw a NoSuchElementException
      throw new NoSuchElementException("Image not found in this category: " + imageLoc);
    } // try/catch
  } // select

  /**
   * Determines if the provided image is stored in the category.
   *
   * @param imageLoc the location of the image
   * @return true if it is in the category, false otherwise
   */
  @Override
  public boolean hasImage(String imageLoc) {
    return items.hasKey(imageLoc);
  } // hasImage
} // AACCategory
