import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Field2 extends JPanel {
	private ArrayList<ArrayList<Integer>> listPoint;
	private ArrayList<ArrayList<Integer>> listPersist;
	private ArrayList<ArrayList<Integer>> rectangleBoundList;
	JLabel labelXY;
	int radius;
	int xLeft, xRight, yTop, yBottom;
	int ballValue;
	int boundary;

	public Field2(int val, int xLeft, int xRight, int yTop, int yBottom) {
		listPoint = new ArrayList<ArrayList<Integer>>();
		listPersist = new ArrayList<ArrayList<Integer>>();
		rectangleBoundList = new ArrayList<ArrayList<Integer>>();
		labelXY = new JLabel("sample");
		ballValue = val;
		radius = 20;
		this.xLeft = xLeft;
		this.xRight = xRight;
		this.yTop = yTop;
		this.yBottom = yBottom;
		boundary = 50;

	}

	public void addLabel() {
		labelXY.setBounds(10, 10, 100, 30);
		labelXY.setVisible(false);
		this.add(labelXY);
	}

	public void addCircleCenterPoint(int x, int y, int level) {
		System.out.println("ball value: " + ballValue + " x: " + x + " y:" + y);
		if (listPoint.size() < ballValue) {
			ArrayList<Integer> tempList = new ArrayList<>();
			tempList.add(x);
			tempList.add(y);
			listPoint.add(tempList);
			repaint();

			if (listPoint.size() == ballValue) {
				for (int i = 0; i < listPoint.size(); i++) {
					ArrayList<Integer> tempListPersist = new ArrayList<>();
					tempListPersist.add(listPoint.get(i).get(0));
					tempListPersist.add(listPoint.get(i).get(1));
					listPersist.add(tempListPersist);
				}

				calculateBoundary(level);

			}
		}
	}

	public void calculateBoundary(int level) {
		for (int i = 0; i < listPoint.size(); i++) {
			ArrayList<Integer> tempBoundList = new ArrayList<>();

			tempBoundList.add(Math.max(listPoint.get(i).get(0) - (boundary + (level * 18)), xLeft));
			tempBoundList.add(Math.min(listPoint.get(i).get(0) + (boundary + (level * 18)), xRight));

			tempBoundList.add(Math.max(listPoint.get(i).get(1) - (boundary + (level * 18)), yTop));
			tempBoundList.add(Math.min(listPoint.get(i).get(1) + (boundary + (level * 18)), yBottom));

			System.out.println("point " + (i + 1) + " boundary = " + tempBoundList.get(0) + "   " + tempBoundList.get(1)
					+ "    " + tempBoundList.get(2) + "    " + tempBoundList.get(3));

			rectangleBoundList.add(tempBoundList);
		}
	}

	public int burstBubble(int x, int y) {
		int size = listPoint.size();
		for (int i = 0; i < listPoint.size(); i++) {
			if (isPointInsideCircle(x, y, listPoint.get(i).get(0), listPoint.get(i).get(1))) {
				listPoint.remove(i);
				rectangleBoundList.remove(i);
			}
		}
		repaint();

		if (listPoint.size() == 0)
			return 2;

		if (size != listPoint.size())
			return 1;
		return 0;

	}

	// https://www.geeksforgeeks.org/find-if-a-point-lies-inside-or-on-circle/

	public boolean isPointInsideCircle(int x, int y, int cx, int cy) {

		if (Math.sqrt(x - cx)  + Math.sqrt(y - cy) <= Math.sqrt(radius))
			return true;
		return false;
	}

	// public void updateLabel(int x, int y) {
	// labelXY.setText(x+" , "+y);
	// }
	//

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (listPoint != null) {

			for (int i = 0; i < listPoint.size(); i++) {
				g.setColor(Color.red);
				// g.fillOval(list.get(i).get(0)-(radius+17) , list.get(i).get(1)-(radius+80),
				// radius*2, radius*2); // for local system
				g.fillOval(listPoint.get(i).get(0) - (radius + 10), listPoint.get(i).get(1) - (radius + 70), radius * 2,
						radius * 2); // for replit
			}
		}
	}

	public void moveBubbleRandomly() {
		Random random = new Random();
		for (int i = 0; i < rectangleBoundList.size(); i++) {

			int x = random
					.nextInt(Math.abs(rectangleBoundList.get(i).get(1) - rectangleBoundList.get(i).get(0) - radius));
			int y = random
					.nextInt(Math.abs(rectangleBoundList.get(i).get(3) - rectangleBoundList.get(i).get(2) - radius));

			// dealing with case where circle may get co-ordinates far left or far top.
			if (x <= 20) {
				x = x + rectangleBoundList.get(i).get(0) + radius;
			} else {
				x = x + rectangleBoundList.get(i).get(0);
			}

			if (y <= 20) {
				y = y + rectangleBoundList.get(i).get(2) + radius;
			} else {
				y = y + rectangleBoundList.get(i).get(2);
			}

			ArrayList<Integer> tempList = new ArrayList<>();
			tempList.add(x);
			tempList.add(y);

			listPoint.set(i, tempList);

		}
		repaint();
	}

	public void prepareForNextLevel(int level) {
		System.out.println("prepare for level " + (level + 1) + " list size " + listPersist.size());
		for (int i = 0; i < listPersist.size(); i++) {
			ArrayList<Integer> tempListPersist = new ArrayList<>();
			tempListPersist.add(listPersist.get(i).get(0));
			tempListPersist.add(listPersist.get(i).get(1));
			listPoint.add(tempListPersist);
		}

		calculateBoundary(level);
		moveBubbleRandomly();
		repaint();
	}
}
