import java.awt.Color;


public class line{
	int startX;
	int startY;
	int endX;
	int endY;
	Color color;
	void update(int sx, int sy, int ex, int ey, Color c){
		startX = sx;
		startY = sy;
		endX = ex;
		endY = ey;
		color = c;
	}
}