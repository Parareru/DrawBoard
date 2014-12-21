import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class miniCAD extends JFrame{

	/**
	 * @param args
	 */
	public painterPanel painter = new painterPanel();
	private JRadioButton jrbLine, jrbCircle, jrbRectangle, jrbText;
	private JButton fColorSet = new JButton("Set Front Color");
	private JButton bColorSet = new JButton("Set Back Color");
	private JMenu menu = new JMenu("File");
	JMenuItem miSaveFile = new JMenuItem("Save file");
	JMenuItem miOpenFile = new JMenuItem("Open file");
	private Color fColor;
	private Color bgColor;
	private JTextField fR = new JTextField();
	private JTextField fG = new JTextField();
	private JTextField fB = new JTextField();
	private JTextField bgR = new JTextField();
	private JTextField bgG = new JTextField();
	private JTextField bgB = new JTextField();
	private int oldX, oldY;


	public miniCAD(){
		final MouseAdapter maLine = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent me){
				line newLine = new line();
				newLine.update(me.getX(), me.getY(), me.getX(), me.getY(), fColor);
				painter.lines.add(newLine);
			}
			
			@Override
			public void mouseDragged(MouseEvent me){
				painter.lines.get(painter.lines.size() - 1).endX = me.getX();
				painter.lines.get(painter.lines.size() - 1).endY = me.getY();
				painter.repaint();
			}
		};
		final MouseAdapter maCircle = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent me){
				circle newCircle = new circle();
				oldX = me.getX();
				oldY = me.getY();
				newCircle.update(me.getX(), me.getY(), 0, 0, fColor);
				painter.cycles.add(newCircle);
			}
			
			@Override
			public void mouseDragged(MouseEvent me){
				int x = me.getX() < oldX ? me.getX() : oldX;
				int y = me.getY() < oldY ? me.getY() : oldY;
				int h = Math.abs(oldY - me.getY());
				int w = Math.abs(oldX - me.getX());
				painter.cycles.get(painter.cycles.size()-1).update(x, y, w, h, fColor);
				painter.repaint();
			}
		};
		final MouseAdapter maRec = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent me){
				rectangle newRec = new rectangle();
				oldX = me.getX();
				oldY = me.getY();
				newRec.update(me.getX(), me.getY(), 0, 0, fColor);
				painter.recs.add(newRec);
			}
			
			@Override
			public void mouseDragged(MouseEvent me){
				int x = me.getX() < oldX ? me.getX() : oldX;
				int y = me.getY() < oldY ? me.getY() : oldY;
				int h = Math.abs(oldY - me.getY());
				int w = Math.abs(oldX - me.getX());
				painter.recs.get(painter.recs.size()-1).update(x, y, w, h, fColor);
				painter.repaint();
			}
		};
		final MouseAdapter maText = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent me){
				if(!painter.textEditing){
					text newText = new text();
					newText.update(me.getX(), me.getY(), "Key in the text.", Color.RED);
					painter.texts.add(newText);
					painter.textEditing = true;
					painter.editingText = "";
					painter.requestFocus();
				}
				else{
					painter.texts.get(painter.texts.size()-1).color = fColor;
					painter.textEditing = false;
				}
				painter.repaint();
			}
		};
		
		add(painter);
		fColor = Color.BLACK;
		bgColor = Color.WHITE;
		painter.setBackground(bgColor);
		
		
		menu.add(miOpenFile);
		menu.add(miSaveFile);
		JMenuBar mBar = new JMenuBar();
		mBar.add(menu);
		setJMenuBar(mBar);
		
		JPanel toolBar = new JPanel();
		toolBar.setLayout(new GridLayout(4,1));
		JPanel modeSel = new JPanel();
		modeSel.setLayout(new GridLayout(5,1));
		modeSel.add(new JLabel("Draw Mode:"));
		modeSel.add(jrbCircle = new JRadioButton(" Circle"));
		modeSel.add(jrbLine = new JRadioButton(" Line"));
		modeSel.add(jrbRectangle = new JRadioButton(" Rectangle"));
		modeSel.add(jrbText = new JRadioButton(" Text"));
		
		toolBar.add(modeSel);
		
		JPanel frontSet = new JPanel();
		frontSet.setLayout(new GridLayout(5,1));
		frontSet.add(new JLabel("Front Color:"));
		JPanel RLayout = new JPanel();
		RLayout.setLayout(new GridLayout(1,2));
		RLayout.add(new JLabel("R:"));
		RLayout.add(fR);
		frontSet.add(RLayout);
		JPanel GLayout = new JPanel();
		GLayout.setLayout(new GridLayout(1,2));
		GLayout.add(new JLabel("G:"));
		GLayout.add(fG);
		frontSet.add(GLayout);
		JPanel BLayout = new JPanel();
		BLayout.setLayout(new GridLayout(1,2));
		BLayout.add(new JLabel("B:"));
		BLayout.add(fB);
		frontSet.add(BLayout);
		frontSet.add(fColorSet);
		toolBar.add(frontSet);
		
		JPanel bgSet = new JPanel();
		bgSet.setLayout(new GridLayout(5,1));
		bgSet.add(new JLabel("Background Color:"));
		JPanel bRLayout = new JPanel();
		bRLayout.setLayout(new GridLayout(1,2));
		bRLayout.add(new JLabel("R:"));
		bRLayout.add(bgR);
		bgSet.add(bRLayout);
		JPanel bGLayout = new JPanel();
		bGLayout.setLayout(new GridLayout(1,2));
		bGLayout.add(new JLabel("G:"));
		bGLayout.add(bgG);
		bgSet.add(bGLayout);
		JPanel bBLayout = new JPanel();
		bBLayout.setLayout(new GridLayout(1,2));
		bBLayout.add(new JLabel("B:"));
		bBLayout.add(bgB);
		bgSet.add(bBLayout);
		bgSet.add(bColorSet);
		toolBar.add(bgSet);
		
		ButtonGroup drawModeSet = new ButtonGroup();
		drawModeSet.add(jrbCircle);
		drawModeSet.add(jrbLine);
		drawModeSet.add(jrbRectangle);
		drawModeSet.add(jrbText);
		
		add(toolBar, BorderLayout.WEST);
		add(painter,BorderLayout.CENTER);
		
		jrbLine.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				painter.removeMouseListener(maCircle);
				painter.removeMouseMotionListener(maCircle);
				painter.removeMouseListener(maRec);
				painter.removeMouseMotionListener(maRec);
				painter.removeMouseListener(maText);
				painter.removeMouseMotionListener(maText);
				painter.addMouseListener(maLine);
				painter.addMouseMotionListener(maLine);
			}
		});
		jrbCircle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				painter.removeMouseListener(maLine);
				painter.removeMouseMotionListener(maLine);
				painter.removeMouseListener(maRec);
				painter.removeMouseMotionListener(maRec);
				painter.removeMouseListener(maText);
				painter.removeMouseMotionListener(maText);
				painter.addMouseListener(maCircle);
				painter.addMouseMotionListener(maCircle);
			}
		});
		jrbRectangle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				painter.removeMouseListener(maLine);
				painter.removeMouseMotionListener(maLine);
				painter.removeMouseListener(maCircle);
				painter.removeMouseMotionListener(maCircle);
				painter.removeMouseListener(maText);
				painter.removeMouseMotionListener(maText);
				painter.addMouseListener(maRec);
				painter.addMouseMotionListener(maRec);
			}
		});
		jrbText.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				painter.removeMouseListener(maLine);
				painter.removeMouseMotionListener(maLine);
				painter.removeMouseListener(maCircle);
				painter.removeMouseMotionListener(maCircle);
				painter.removeMouseListener(maRec);
				painter.removeMouseMotionListener(maRec);
				painter.addMouseListener(maText);
				painter.addMouseMotionListener(maText);
			}
		});
		fColorSet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int R = Integer.parseInt(fR.getText().toString());
				int G = Integer.parseInt(fG.getText().toString());
				int B = Integer.parseInt(fB.getText().toString());
				fColor = new Color(R, G, B);
			}
		});
		bColorSet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int R = Integer.parseInt(bgR.getText().toString());
				int G = Integer.parseInt(bgG.getText().toString());
				int B = Integer.parseInt(bgB.getText().toString());
				bgColor = new Color(R, G, B);
				painter.setBackground(bgColor);
			}
		});
		miSaveFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jpg", "jpg");
				JFileChooser dialog = new JFileChooser();
				dialog.setFileFilter(filter);
				dialog.setDialogTitle("Save jpg file");
				int result = dialog.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					File file = dialog.getSelectedFile();
					if(!file.getPath().endsWith(".jpg")){
						file = new File(file.getPath() + ".jpg");
					}
					BufferedImage image = new BufferedImage(painter.getWidth(), painter.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics2D g2 = image.createGraphics();
					painter.paint(g2);
					try {
						ImageIO.write(image, "jpeg", file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		miOpenFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jpg", "jpg");
				JFileChooser dialog = new JFileChooser();
				dialog.setFileFilter(filter);
				dialog.setDialogTitle("Open jpg file");
				int result = dialog.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					File file = dialog.getSelectedFile();
					painter.image = new ImageIcon(file.getPath());
					painter.hasImage = true;
					painter.repaint();
				}
			}
		});
		
		painter.setFocusable(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		miniCAD minicad = new miniCAD();
		minicad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		minicad.setSize(640, 480);
		minicad.setLocationRelativeTo(null);
		minicad.setVisible(true);
	}
}
