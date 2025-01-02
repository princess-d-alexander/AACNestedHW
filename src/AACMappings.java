import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;
import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;
import java.util.NoSuchElementException;


/**
 * Creates a set of mappings of an AAC that has two levels,
 * one for categories and then within each category, it has
 * images that have associated text to be spoken. This class
 * provides the methods for interacting with the categories
 * and updating the set of images that would be shown and handling
 * an interactions.
 * 
 * The AACMappings class manages categories of images and their corresponding text.
 * It implements the AACPage interface to provide functionality for adding, selecting, and 
 * manipulating items in the system.
 * 
 * @author Catie Baker & Princess Alexander
 *
 */

public class AACMappings implements AACPage {

  /** Map holding the categories and associated images */
  private AssociativeArray<String, AssociativeArray<String, String>> categories;

  /** The current selected category */
  private String currentCategory;

  /**
   * In short: Constructs an AACMappings object using the file name.
   *
	 * Creates a set of mappings for the AAC based on the provided
	 * file. The file is read in to create categories and fill each
	 * of the categories with initial items. The file is formatted as
	 * the text location of the category followed by the text name of the
	 * category and then one line per item in the category that starts with
	 * > and then has the file name and text of that image
	 * 
	 * for instance:
	 * img/food/plate.png food
	 * >img/food/icons8-french-fries-96.png french fries
	 * >img/food/icons8-watermelon-96.png watermelon
	 * img/clothing/hanger.png clothing
	 * >img/clothing/collaredshirt.png collared shirt
	 * 
	 * represents the file with two categories, food and clothing
	 * and food has french fries and watermelon and clothing has a 
	 * collared shirt
	 *
   * @param filename The file containing image categories and text mappings
   *
   */
  public AACMappings(String filename) {
    categories = new AssociativeArray<String, AssociativeArray<String, String>>();
    currentCategory = null;

    try (Scanner scanner = new Scanner(new File(filename))) {
      String category = null;
      AssociativeArray<String, String> imageTextMap = null;

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine().trim();
        if (line.startsWith("img/")) {
          String[] parts = line.split(" ", 2);
          if (line.startsWith(">")) {
            String imageLoc = parts[0].substring(1);
            String text = parts[1];
            if (imageTextMap != null) {
              imageTextMap.set(imageLoc, text);
            } // if
          } else {
            if (category != null && imageTextMap != null) {
              categories.set(category, imageTextMap);
            }
            category = parts[1];
            imageTextMap = new AssociativeArray<String, String>();
          } // else/if
        } // if
      } // while
      if (category != null && imageTextMap != null) {
        categories.set(category, imageTextMap);
      } // if
    } catch (Exception e) {
      e.printStackTrace();
    } // catch 
  } // AACMappings

  /**
   * Adds an image and corresponding text to the current category.
   *
   *  * @param imageLoc The location of the image
   *  * @param text The text description of the image
   *  */
  @Override
  public void addItem(String imageLoc, String text) {
    if (imageLoc == null || text == null) {
	    throw new IllegalArgumentException("Image location and text cannot be null.");
	  } // if

    try {
      // Check if currentCategory exists in categories
      if (categories.hasKey(currentCategory)) {
      // Attempting to add the item to the associative array for the current category
      categories.get(currentCategory).set(imageLoc, text); 
      } else {
      // Handling the case when currentCategory is not found
      System.err.println("Error: Current category not found - " + currentCategory);
      } // else
    } catch (NullKeyException e) {
      // Handling the exception.
      System.err.println("Error: Attempt to add item with a null key - " + e.getMessage());
    } catch (KeyNotFoundException e) {
      // Handle the case where currentCategory is not found in categories
      System.err.println("Error: Category not found - " + e.getMessage());
    } // try
  } // addItem

  /**
   * Retrieves all image locations for the current category.
   *
   * @return An array of image locations
   */
  @Override
  public String[] getImageLocs() {
    try {
      String[] imageLocs = new String[categories.get(currentCategory).size()];
      int index = 0;

      // Retrieve all keys (image locations)
      for (String key : categories.get(currentCategory).keys()) {
        imageLocs[index++] = key;
      } // for
      return imageLocs;
    } catch (Exception e) {
      // In case of any exception (e.g., null pointer or other unexpected issues)
      System.err.println("Error while retrieving image locations: " + e.getMessage());
      return new String[0];  // Return an empty array in case of error
    } // catch
  } // getImageLocs

  /**
   * Gets the name of the current category.
   *
   * @return The name of the current category
   */
  @Override
  public String getCategory() {
    return currentCategory;
  } // getCategory

  /**
   * Selects a specific image from the current category.
   *
   * @param imageLoc The location of the image to select
   * @return The text associated with the selected image
   */
  @Override
  public String select(String imageLoc) {
    try {
      if (categories.hasKey(currentCategory)) {
        AssociativeArray<String, String> currentImages = categories.get(currentCategory);
        return currentImages.get(imageLoc);
      } // if
    } catch (KeyNotFoundException e) {
      throw new NoSuchElementException("Image location not found in current category: " + imageLoc);
    } //catch
    return "";
  } // select

  /**
   * Checks if the given image location exists in the current category.
   *
   * @param imageLoc The image location to check
   * @return true if the image exists in the current category, false otherwise
   */
  @Override
  public boolean hasImage(String imageLoc) {
    try {
      return categories.get(currentCategory).hasKey(imageLoc); 
    } catch (KeyNotFoundException e) {
      // Log the error if needed
      System.err.println("Error checking for image: " + imageLoc + " - " + e.getMessage());
      return false;
    } // try
  } // hasImage


  /**
   * Resets the category to the first available category in the list.
   */
  public void reset() {
    if (categories.size() > 0) {
      currentCategory = categories.keys()[0];
    } // if
  } // reset

  /**
   * Writes the current categories and their corresponding image-text mappings to a file.
   *
   * @param filename The file to which the mappings should be written
   */
  public void writeToFile(String filename) {
    try (PrintWriter writer = new PrintWriter(new File(filename))) {
      for (String category : categories.keys()) {
        writer.println("img/" + category + " " + category);
        AssociativeArray<String, String> imageTextMap = categories.get(category);
        for (String imageLoc : imageTextMap.keys()) {
          writer.println(">" + imageLoc + " " + imageTextMap.get(imageLoc));
        } // for
      } // for
    } catch (Exception e) {
      e.printStackTrace();
    } // catch
  } // writeToFile

} // AACMappings
