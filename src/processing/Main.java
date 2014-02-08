package processing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	
	public static class Rect{
		int lx;
		int rx;
		int uy;
		int dy;
		public Rect(int lx, int rx, int uy, int dy) {
			super();
			this.lx = lx;
			this.rx = rx;
			this.uy = uy;
			this.dy = dy;
		}
		
	}



	private static final int MAX_WHITE = 10;
	private static final int G_AREA_LEN = 100;
	private static final int G_AREA_WID = 100;
	private static final int SCR_AREA_LEN = 500;
	private static final int SCR_AREA_WID = 500;
	private static final int MAX_DIST = 0;
	private static final int PLAYER_MATCH_THRESHOLD = 0;
	private static Rect INITIAL_PLAYER_POS = new Rect(0, 0, 0, 0);
	
	public static void main(String[] args) {
		
		if(args.length < 4){
			printUsageOptions();
		}
		else{
			String imageFile = args[0];
			int patchX = Integer.parseInt(args[1]);
			int patchY = Integer.parseInt(args[2]);
			int tolerance = Integer.parseInt(args[3]);
			int rVal = Integer.parseInt(args[4]);
			int gVal = Integer.parseInt(args[5]);
			int bVal = Integer.parseInt(args[6]);
			Rect pl = INITIAL_PLAYER_POS;
			ArrayList<Rect> pipes = null;
			Rect gameBounds = getGameArea(imageFile, patchX, patchY, tolerance, new Color(rVal, gVal, bVal));
			Scanner sc = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
			while(true){
				String nextFile = sc.next();
				Picture inp = Utils.loadPicture(nextFile);
				PixelMap pm = new PixelMap(inp, gameBounds);
				pl = getPlayer(pm, pl);
				pipes = getPipes(pm, pipes);
				String action = nextMove(pl, pipes);
				System.out.println(action);
			}
		}		
		
	}

	
	
	private static String nextMove(Rect pl, ArrayList<Rect> pipes) {
		// TODO Auto-generated method stub
		return null;
	}



	private static ArrayList<Rect> getPipes(PixelMap pm, ArrayList<Rect> pipes) {
		// TODO Auto-generated method stub
		return null;
	}



	private static Rect getPlayer(PixelMap pm, Rect prev) {
		for(int i = Math.max(prev.uy - MAX_DIST, 0); i < Math.min(prev.uy + MAX_DIST, pm.pixels.length); i--){
			int match = matchPl(pm, new Rect(prev.lx, prev.rx, i, prev.dy-(i-prev.uy)));
			if(match > PLAYER_MATCH_THRESHOLD){
				return new Rect(prev.lx, prev.rx, i, prev.dy-(i-prev.uy));
			}
		}
	}



	private static int matchPl(PixelMap pm, Rect rect) {
		// TODO Auto-generated method stub
		return 0;
	}



	private static Rect getGameArea(String imageFile, int patchX, int patchY,
			int threshold, Color c) {
		Picture inp = Utils.loadPicture(imageFile);
		PixelMap pm = new PixelMap(inp);
		for(int i = 0; i < pm.pixels.length; i++){
			
			for(int j = 0; j < pm.pixels[0].length; j++){
				int match = 0;
				int startX = 0;
				int startY = 0;
				for(int x = i; x < i+ patchX; x++){
					for(int y = j; y < j + patchY; y++){
						if(c.equals(pm.pixels[x][y])){
							match++;
							startX = x;
							startY = y;
						}
					}
				}
				if(match > threshold){
					return getBoundsToWhite(pm, startX, startY);
				}
				
			}
				
			}
		
		return null;
		
	}



	private static Rect getBoundsToWhite(PixelMap pm, int startX, int startY) {
		int lx = startY;
		int rx = 0;
		int uy = startX;
		int dy = 0;
		int count = 0;
		for(int i = startX; i < pm.pixels.length; i++){
			if(Color.isWhite(pm.pixels[i][startY])){
				count++;
				if(count > MAX_WHITE){
					break;
				}
			}
			else{
				rx =  i;
				count = 0;
			}
		}
		count = 0;
		for(int i = startX; i >= 0; i--){
			if(Color.isWhite(pm.pixels[i][startY])){
				count++;
				if(count > MAX_WHITE){
					break;
				}
			}
			else{
				lx =  i;
				count = 0;
			}
		}
		count = 0;
		for(int i = startY; i < pm.pixels[0].length; i++){
			if(Color.isWhite(pm.pixels[startX][i])){
				count++;
				if(count > MAX_WHITE){
					break;
				}
			}
			else{
				dy =  i;
				count = 0;
			}
		}
		count = 0;
		for(int i = startY; i >= 0; i--){
			if(Color.isWhite(pm.pixels[startX][i])){
				count++;
				if(count > MAX_WHITE){
					break;
				}
			}
			else{
				uy =  i;
				count = 0;
			}
		}
		
		Rect r = new Rect();
		r.dy = dy;
		r.lx = lx;
		r.rx = rx;
		r.uy = uy;
		return r;
	}



	private static void printUsageOptions() {
		System.out.println("Insufficient number of arguments supplied.\nUsage parameters: <path to image file> <height of patch> <width of patch> <tolerance to detect dark spot>");
	}

}
