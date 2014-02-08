package processing;

public class Line{
	private double r;
	private double theta;
	private double yIntercept;
	private double slope;
	public Line(double r, double theta) {
		super();
		this.r = r;
		this.theta = theta;
		yIntercept = r/Math.sin(theta);
		slope = -1/Math.tan(theta);
	}
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	public double getTheta() {
		return theta;
	}
	public void setTheta(double theta) {
		this.theta = theta;
	}
	
	public void setSlope(double slope){
		this.slope = slope;
	}
	public void setYIntercept(double yIntercept){
		this.yIntercept = yIntercept;
	}
	public double getSlope()
	{
		return slope;
	}
	
	public double getYIntercept() {
	    return yIntercept;
	}
	@Override
	public String toString() {
		return "Line [y = " + getSlope()+ "*x + " + getYIntercept() + "]";
	}
}