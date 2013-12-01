package drawing;

public class User {
    // CLASS CONSTANTS
    // all values refer to respective index in relevant list
    /**
     * Brush setting to draw
     */
    public static final int DRAW = 0;
    
    /**
     * Brush setting to erase
     */
    public static final int ERASE = 1;
    
    /**
     * Brush size to small
     */
    public static final int SMALL = 0;
    
    /**
     * Brush size to medium
     */
    public static final int MEDIUM = 1;
    
    /**
     * Brush size to large
     */
    public static final int LARGE = 2;
    
    /**
     * Brush color to black (#000000)
     */
    public static final int BLACK = 0;
    
    /**
     * Brush color to red (#FF0000)
     */
    public static final int RED = 1;
    
    /**
     * Brush color to orange (#FF7700)
     */
    public static final int ORANGE = 2;
    
    /**
     * Brush color to yellow (#FFFF00)
     */
    public static final int YELLOW = 3;
    
    /**
     * Brush color to green (#00FF00)
     */
    public static final int GREEN = 4;
    
    /**
     * Brush color to blue (#0000FF)
     */
    public static final int BLUE = 5;
    
  // INSTANCE VARIABLES
  /**
   * Current brush setting (see brush constants)
   */
  private static int currentBrush;
  
  /**
   * Current brush size (see size constants)
   */
  private static int currentSize;
  
  /**
   * Current brush color (see color constants)
   */
  private static int currentColor;
  
  
	/**
	 * or should this receive an arraylist of toolspanels???from the multiple screens?
	 * or should they just be seen as different users?
	 */
	public User(ToolsPanel toolsPanel){
		this.currentSize = SMALL;
		this.currentColor =BLACK;
		this.currentBrush =DRAW;
	}
	
	public static void setCurrentBrush(int newBrush){
		currentBrush = newBrush;
	}
	
	public static void setCurrentSize(int newSize){
		currentSize = newSize;
	}
	
	public static void setCurrentColor(int newColor){
		currentColor = newColor;
	}
	
	public static int getCurrentBrush(){
		return currentBrush;
	}
	
	public static int getCurrentSize(){
		return currentSize;
	}
	
	public static int getCurrentColor(){
		return currentColor;
	}
	
	
}
