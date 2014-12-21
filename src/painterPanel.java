import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

public class painterPanel extends JPanel{

	/**
	 * @param args
	 */
	public ArrayList<line> lines = new ArrayList<line>();
	public ArrayList<circle> cycles = new ArrayList<circle>();
	public ArrayList<rectangle> recs = new ArrayList<rectangle>();
	public ArrayList<text> texts = new ArrayList<text>();
	public boolean hasImage = false;
	public ImageIcon image;
	public boolean textEditing = false;
	public String editingText;
	
	public painterPanel(){
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(textEditing){
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
						if(editingText.length() > 0)
							editingText = editingText.substring(0, editingText.length()-1);
					}
					else
						editingText += e.getKeyChar();
					texts.get(texts.size()-1).text = editingText;
					repaint();
				}
			}
		});
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(hasImage){
			image.paintIcon(this, g, 0, 0);
		}
		
		Iterator<line> itLine = lines.iterator();
		line currentLine;
		while(itLine.hasNext()){
			currentLine = itLine.next();
			g.setColor(currentLine.color);
			g.drawLine(currentLine.startX, currentLine.startY, currentLine.endX, currentLine.endY);
		}
		Iterator<circle> itCycle = cycles.iterator();
		circle currentCycle;
		while(itCycle.hasNext()){
			currentCycle = itCycle.next();
			g.setColor(currentCycle.color);
			g.drawOval(currentCycle.x, currentCycle.y, currentCycle.w, currentCycle.h);
		}
		Iterator<rectangle> itRec = recs.iterator();
		rectangle currentRec;
		while(itRec.hasNext()){
			currentRec = itRec.next();
			g.setColor(currentRec.color);
			g.drawRect(currentRec.x, currentRec.y, currentRec.w, currentRec.h);
		}
		Iterator<text> itText = texts.iterator();
		text currentText;
		while(itText.hasNext()){
			currentText = itText.next();
			g.setColor(currentText.color);
			g.drawString(currentText.text, currentText.x, currentText.y);
		}
	}
}
