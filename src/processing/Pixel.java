package processing;


public class Pixel implements Cloneable {
	public static final boolean MARKED = true;
	public static final boolean DARK = true;
	private boolean marked;
	private boolean dark;
	
	
	public Pixel(boolean marked, boolean dark) {
		super();
		this.marked = marked;
		this.dark = dark;
	}
	
	public boolean isMarked() {
		return marked;
	}
	public void setMarked(boolean marked) {
		this.marked = marked;
	}
	public boolean isDark() {
		return dark;
	}
	public void setDark(boolean dark) {
		this.dark = dark;
	}
	
	@Override
	public Pixel clone() {
		Pixel cloning = null;
		try{
		cloning = (Pixel) super.clone();
		} catch(CloneNotSupportedException e) {
			throw new AssertionError(); //IMPOSSIBLE
		}
		cloning.marked = marked;
		cloning.dark = dark;
		return cloning;
	}
	
}
