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
  private String currentCategory = ""; // Default to empty string


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
    currentCategory = "";

    try (Scanner scanner = new Scanner(new File(filename))) {
      String category = null;
      AssociativeArray<String, String> imageTextMap = null;

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine().trim();
        if (line.startsWith("img/")) {
            String[] parts = line.split(" ", 2);
            if (line.startsWith(">")) {
                // This is an image item line
                String imageLoc = parts[0].substring(1);
                String text = parts[1];
                if (imageTextMap != null) {
                    imageTextMap.set(imageLoc, text);
                }
            } else {
                // This is a new category line
                if (category != null && imageTextMap != null) {
                    categories.set(category, imageTextMap);
                }
                category = parts[1];
                imageTextMap = new AssociativeArray<String, String>();
            }
        }
    }
    // Final check to store the last category
    if (category != null && imageTextMap != null) {
        categories.set(category, imageTextMap);
    }
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
  
    if (currentCategory == null || currentCategory.trim().isEmpty()) {
        throw new IllegalStateException("Current category is not set.");
    }

    try {
        if (categories.hasKey(currentCategory)) {
            categories.get(currentCategory).set(imageLoc, text);
        } else {
            System.err.println("Error: Current category not found - " + currentCategory);
        }
    } catch (NullKeyException | KeyNotFoundException e) {
        System.err.println("Error: " + e.getMessage());
    }
  }

  /**
   * Retrieves all image locations for the current category.
   *
   * @return An array of image locations
   */
  @Override
  public String[] getImageLocs() {
      try {
          if (categories.hasKey(currentCategory)) {
              AssociativeArray<String, String> currentCategoryItems = categories.get(currentCategory);
              String[] imageLocs = new String[currentCategoryItems.size()];
              int index = 0;
  
              for (String key : currentCategoryItems.keys()) {
                  imageLocs[index++] = key;
              }
              return imageLocs;
          } else {
              return new String[0]; // Return empty array if category is not found
          }
      } catch (KeyNotFoundException e) {
          System.err.println("Error retrieving image locations: " + e.getMessage());
          return new String[0];
      }
  }  

  /**
   * Gets the name of the current category.
   *
   * @return The name of the current category
   */
  public String getCategory() {
    if (currentCategory == null || currentCategory.trim().isEmpty()) {
        return "Unnamed Category"; 
    }
    return currentCategory;
  }

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
        currentCategory = categories.keys()[0];  // Select the first available category
    } else {
        currentCategory = "";  // Default to empty if no categories are available, but should handle this better
        throw new IllegalStateException("No categories available to reset.");
    }
  }

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
