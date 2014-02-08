package processing;


public class BitMap {
	private Pixel[][] pixelMap; // the first co-ordinate gives the row number (matrix representation)
	private final int width;
	private final int height;
	/** Counts the number of dark spots per column */
	private int[] darkSpotsPerColumn;	
	/** Counts the number of dark spots per row */
	private int[] darkSpotsPerRow;
	private String stringRep;
	
	public BitMap(Pixel[][] pixelMap) {
		super();
		this.pixelMap = new Pixel[ pixelMap.length ] [ pixelMap[0].length ];
		height = pixelMap.length;
		
		if (height == 0) {
			throw new IllegalArgumentException("Array");
		}
		
		width = pixelMap[0].length;
		darkSpotsPerRow = new int[height];
		darkSpotsPerColumn = new int[width];
		
		for(int i = 0; i < height; i++) {
			for(int z = 0; z < width; z++){
				this.pixelMap[ i ] [ z ] = pixelMap[ i ][ z ];
			}
		}
		
		calculateDarkSpotCount();
		
		setStringRep();
	}
	
	private void calculateDarkSpotCount() {
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if(pixelMap[i][j].isDark()){
					darkSpotsPerColumn[j]++;
					darkSpotsPerRow[i]++;
				}
			}
		}
	}
	
	
	
	public Pixel[][] getPixelMap() {
		return pixelMap;
	}
	public void setPixelMap(Pixel[][] pixelMap) {
		this.pixelMap = pixelMap;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int[] getDarkSpotsPerColumn() {
		return darkSpotsPerColumn;
	}
	public int[] getDarkSpotsPerRow() {
		return darkSpotsPerRow;
	}
	
	public Pixel obtainPixel(int row, int column) {
		return pixelMap[row][column].clone();
	}
	
	private void setStringRep(){
		
		StringBuilder stringRep = new StringBuilder();
		
		stringRep.setLength( height * width);
		
		for(int i = 0 ; i < height ; i++){
			for(int z = 0; z < width ; z++){
			
				if( pixelMap[ i ][ z ].isDark() )
					stringRep.append( 1 );
				else
					stringRep.append( ' ' );
		
			}
			
			stringRep.append( '\n' );
		}
		
		this.stringRep = stringRep.toString();
	
	}
	
	@Override
	public String toString(){
		
		return stringRep;
	
	}
	
}
