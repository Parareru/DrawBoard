import java.awt.Color;

public class rectangle{
	int x;
	int y;
	int h;
	int w;
	Color color;
	void update(int xIn, int yIn, int wIn, int hIn, Color c){
		x = xIn;
		y = yIn;
		h = hIn;
		w = wIn;
		color = c;
	}
}