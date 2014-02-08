package processing;

import processing.Main.Rect;

public class PixelMap {
	Color[][] pixels;
	
	public PixelMap(Picture inp){
		for(int i = 0; i < inp.getHeight(); i++){
			for(int j = 0; j < inp.getWidth(); j++){
				pixels[i][j] = inp.getPixel(i, j);
			}
		}
	}
	
	public PixelMap(Picture inp, Rect bounds){
		for(int i = bounds.lx; i <= bounds.rx; i++){
			for(int j = bounds.uy; j < bounds.dy; j++){
				pixels[i-bounds.lx][j-bounds.uy] = inp.getPixel(i, j);
			}
		}
	}
	
	
}
