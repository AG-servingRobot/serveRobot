
import java.awt.*;
import javax.swing.*;
import java.util.TimerTask;
import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;
import java.awt.Point;
import javax.swing.JPanel;

public class MapPane extends JPanel {
//	public static Dijkstra dj = new Dijkstra();
	static final int ROBOT_WIDTH = 80;
	static final int ROBOT_HEIGHT = 80;

	//images
	Image map, robot_front1, robot_back1, robot_front2, robot_back2;

	//timer for Robot Moving GUI
	java.util.Timer r1Timer;
	java.util.Timer r2Timer;
	
	//timer for robot's back place
	java.util.Timer timerUtil;
	TimerTask task1;
	TimerTask task2;

	//x, y Velocity
	int xVel = 2, yVel = 2;
	//init x and y coordinate
	int x1 = 400, y1 = 80, x2 = 400, y2 = 80;
	//robot to be commanded
	int robot;
	//queue that stores the way the robot will go
	Queue<Point> way1 = new LinkedList<Point>();
	Queue<Point> way2 = new LinkedList<Point>();
	//point for queue
	Point p1 = null;
	Point p2 = null;
	//whether the robot is moving or not
	//0 is not moving, 1 is moving
	int flag1 = 0, flag2 = 0;
	//Last coordinates in queue
	int[] temp1 = {400, 80}, temp2 = {400, 80};
	//destination coordinate
	int[] dest1, dest2;


	//initialize robot
	public MapPane() {
		//initialize map
		initComponents();

		robot_front1 = new ImageIcon("./images/robot1.png").getImage();
		//set robot1's coordinate
		x1 = x1 - ROBOT_WIDTH/2;
		y1 = y1 - ROBOT_HEIGHT/2;

		robot_front2 = new ImageIcon("./images/robot2.png").getImage();
		//set robot2's coordinate
		x2 = x2 - ROBOT_WIDTH/2;
		y2 = y2 - ROBOT_HEIGHT/2;

//		//save robot's coordinate
//		int[] robot1 = {0,0}, robot2 = {0,0};
//		robot1[0] = temp1[0];
//		robot1[1] = temp1[1];
//		robot2[0] = temp2[0];
//		robot2[1] = temp2[1];

		//set GUI timer
		//        timer = new Timer(10, this);
		//set Robot Moving GUI timer
		r1Timer = new java.util.Timer();
		r2Timer = new java.util.Timer();
		r1Timer.scheduleAtFixedRate(r1Task, 10, 10);
		r2Timer.scheduleAtFixedRate(r2Task, 10, 10);

		//set util timer
		//        timerUtil = new java.util.Timer();
		//timer start
		//        timer.start();
	}


	//set the path for the robot to move
	public void setRobot(int robot, int[][] position, int[] dest) {
		//robot : robot to move
		//position : path to move
		//dest : destination
		this.robot = robot;

		//to move robot 1
		if(robot == 1) {
			this.dest1 = dest;
			//save way
			for(int i = 0; i < position.length; i++) {
				if(position[i][0] != 0 && position[i][1] != 0) { 
					this.way1.add(new Point(position[i][0], position[i][1]));
					this.temp1[0] = position[i][0];
					this.temp1[1] = position[i][1];
				}
			}
		}
		//to move robot 2
		if(robot == 2) {
			this.dest2 = dest;
			//save way
			for(int i = 0; i < position.length; i++) {
				if(position[i][0] != 0 && position[i][1] != 0) { 
					this.way2.add(new Point(position[i][0], position[i][1]));
					this.temp2[0] = position[i][0];
					this.temp2[1] = position[i][1];
				}
			}
		}
	}

	//get robot's coordinate(the last coordinate of the way)
	public int[] getInfo(int robot) {
		//robot : robot to get information
		int[] coordinate = new int[2];
		//robot1's coordinate
		if(robot == 1) {
			coordinate[0] = temp1[0];
			coordinate[1] = temp1[1];
		}
		//robot2's coordinate
		if(robot == 2) {
			coordinate[0] = temp2[0];
			coordinate[1] = temp2[1];
		}

		return coordinate;
	}

	
	//if robot doesn't have a job
	public int isFree() {
		//return robot's state
		if(way1.isEmpty() && !way2.isEmpty()) return 1; //if robot1 is not working
		else if(way2.isEmpty() && !way1.isEmpty()) return 2; //if robot2 is not working
		else if(way2.isEmpty() && way1.isEmpty()) return 3; //if both not working
		else return 0; //if both working
	}

	
	//paint GUI
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		//draw robot1
		g2D.drawImage(robot_front1, x1, y1, null);
		//draw robot2
		g2D.drawImage(robot_front2, x2, y2, null);
	}

	
	//task for Robot1 Moving GUI timer
	TimerTask r1Task = new TimerTask() {
		@Override
		public synchronized void run() {
			int[] robot1 = {0,0};
			robot1[0] = temp1[0];
			robot1[1] = temp1[1];

			//if robot 1 is not moving
			if(flag1 == 0) {
				//get work from queue
				p1 = way1.poll();
			}

			//if robot 1 has work
			if(p1 != null) {
				//if x coordinate of the robot are not the path to move
				if(p1.x != (x1 + ROBOT_WIDTH/2)) {
					//flag for the robot is moving
					flag1 = 1;

					//if there is a road on the right
					if((y1 + ROBOT_HEIGHT/2) == 80 ||
							(y1 + ROBOT_HEIGHT/2) == 380) {
						//if robot goes to the right
						if(p1.x > (x1 + ROBOT_WIDTH/2)) {
							//increase the x-coordinate
							x1=x1+xVel;
						}
						//if robot goes to the left
						else if(p1.x < (x1 + ROBOT_WIDTH/2)) {
							//decrease the x-coordinate
							x1=x1-xVel;
						}
					}
				}
				//if y coordinate of the robot are not the path to move
				if(p1.y != (y1 + ROBOT_HEIGHT/2)) {
					//flag for the robot is moving
					flag1 = 1;

					//if robot goes to the up
					if(p1.y > (y1 + ROBOT_HEIGHT/2))
						//increase the y-coordinate
						y1 = y1+yVel;
					//if robot goes to the down
					else if(p1.y < (y1 + ROBOT_HEIGHT/2))
						//decrease the y-coordinate
						y1 = y1-yVel;
				}

				if(p1.x == (x1 + ROBOT_WIDTH/2) && p1.y == (y1 + ROBOT_HEIGHT/2)) {
					//if robot arrive at the next coordinate, change the flag to 0
					flag1 = 0;
				}

				if(dest1[0] == (x1 + ROBOT_WIDTH/2) && dest1[1] == (y1 + ROBOT_HEIGHT/2)) {
					// robot1 arrived to destination.
					System.out.println("도착");
					// what is done?
					if (!(dest1[0] == 200 && dest1[1] == 80) && MainFrame.robot_doing[0].equals("setting")) {	// destination is not settingBar
						MainFrame.isSettingDone[MainFrame.robot_table[0] - 1] = true;	// setting done
						MapPane.table[MainFrame.robot_table[0] - 1].repaint();	// repaint the table
						MainFrame.robot_doing[0] = "";	// reset
						MainFrame.robot_table[0] = -1;
						System.out.println("setting done");
					}
					else if (!(dest1[0] == 400 && dest1[1] == 80) && MainFrame.robot_doing[0].equals("serving")) {	// destination is not kitchen
						MainFrame.isServingDone[MainFrame.robot_table[0] - 1] = true;	// serving done
						MapPane.table[MainFrame.robot_table[0] - 1].repaint();	// repaint the table
						MainFrame.robot_doing[0] = "";	// reset
						MainFrame.robot_table[0] = -1;
						System.out.println("serving done");
					}
					else if (MainFrame.robot_doing[0].equals("clean")) {
						MainFrame.table_state[MainFrame.robot_table[0] - 1] = 0;	// guest can come
						MainFrame.haveToClean[MainFrame.robot_table[0] - 1] = false;	// reset
						MapPane.table[MainFrame.robot_table[0] - 1].repaint();	// repaint the table
						MapPane.state[MainFrame.robot_table[0] - 1].setText("");	// reset
						MainFrame.isSettingDone[MainFrame.robot_table[0] - 1] = false;
						MainFrame.isServingDone[MainFrame.robot_table[0] - 1] = false;
						MainFrame.robot_doing[0] = "";
						MainFrame.robot_table[0] = -1;
						System.out.println("clean done");
					}	
					else if (!(dest1[0] == 400 && dest1[1] == 80) && MainFrame.robot_doing[0].equals("refull")) {		// destination is not kitchen
						Queueing.dish = 5;	//	refull
						MainFrame.robot_doing[0] = "";	// reset
						MainFrame.robot_table[0] = -1;
						System.out.println("refull done");
					}
					else {
						System.out.println("로봇1-  "+"현재x: " + String.valueOf(x1+40) + " 현재y: " + String.valueOf(y1+40)+"   목적지x: " + String.valueOf(dest1[0]) + " 목적지y: " + String.valueOf(dest1[1]));
					}
					//After moving one route, check if there is work, if not, go to the waiting area
					tempTask(1);
				}
			}
			//repaint GUI
			MainFrame.mp.repaint();
		}
	};


	//task for Robot2 Moving GUI timer
	TimerTask r2Task = new TimerTask() {
		@Override
		public synchronized void run() {
			int[] robot2 = {0,0};
			robot2[0] = temp2[0];
			robot2[1] = temp2[1];

			//if robot 2 is not moving
			if(flag2 == 0) {
				//get work from queue
				p2 = way2.poll();
			}

			//if robot 1 has work
			if(p2 != null) {
				//if x coordinate of the robot are not the path to move
				if(p2.x != (x2 + ROBOT_WIDTH/2)) { 
					//flag for the robot is moving
					flag2 = 1;
					//if there is a road on the right
					if((y2 + ROBOT_HEIGHT/2) == 80 ||
							(y2 + ROBOT_HEIGHT/2) == 380) {
						//if robot goes to the right
						if(p2.x > (x2 + ROBOT_WIDTH/2)) {
							//increase the x-coordinate
							x2=x2+xVel;
						}
						//if robot goes to the left
						else if(p2.x < (x2 + ROBOT_WIDTH/2)) {
							//decrease the x-coordinate
							x2=x2-xVel;
						}
					}
				}
				//if y coordinate of the robot are not the path to move
				if(p2.y != (y2 + ROBOT_HEIGHT/2)) {
					//flag for the robot is moving
					flag2 = 1;
					//if robot goes to the up
					if(p2.y > (y2 + ROBOT_HEIGHT/2))
						//increase the y-coordinate
						y2 = y2+yVel;
					//if robot goes to the down
					else if(p2.y < (y2 + ROBOT_HEIGHT/2))
						//decrease the y-coordinate
						y2 = y2-yVel;
				}
				if(p2.x == (x2 + ROBOT_WIDTH/2) && p2.y == (y2 + ROBOT_HEIGHT/2)) {
					//				System.out.println("flag2 변경" + flag2);
					flag2 = 0;
				}

				if(dest2[0] == (x2 + ROBOT_WIDTH/2) && dest2[1] == (y2 + ROBOT_HEIGHT/2)) {
					// robot2 arrived to destination.

					// what is done?
					if (!(dest2[0] == 200 && dest2[1] == 80) && MainFrame.robot_doing[1].equals("setting")) {	// destination is not settingBar
						MainFrame.isSettingDone[MainFrame.robot_table[1] - 1] = true;	// setting done
						MapPane.table[MainFrame.robot_table[1] - 1].repaint();	// repaint the table
						MainFrame.robot_doing[1] = "";	// reset
						MainFrame.robot_table[1] = -1;
					}
					else if (!(dest2[0] == 400 && dest2[1] == 80) && MainFrame.robot_doing[1].equals("serving")) {	// destination is not kitchen
						MainFrame.isServingDone[MainFrame.robot_table[1] - 1] = true;	// serving done
						MapPane.table[MainFrame.robot_table[1] - 1].repaint();	// repaint the table
						MainFrame.robot_doing[1] = "";	// reset
						MainFrame.robot_table[1] = -1;
					}
					else if (MainFrame.robot_doing[1].equals("clean")) {
						MainFrame.table_state[MainFrame.robot_table[1] - 1] = 0;	// guest can come
						MainFrame.haveToClean[MainFrame.robot_table[1] - 1] = false;	// reset
						MapPane.table[MainFrame.robot_table[1] - 1].repaint();	// repaint the table
						MapPane.state[MainFrame.robot_table[1] - 1].setText("");	// reset
						MainFrame.isSettingDone[MainFrame.robot_table[1] - 1] = false;
						MainFrame.isServingDone[MainFrame.robot_table[1] - 1] = false;
						MainFrame.robot_doing[1] = "";
						MainFrame.robot_table[1] = -1;
					}else if (!(dest2[0] == 400 && dest2[1] == 80) && MainFrame.robot_doing[1].equals("refull")) {		// destination is not kitchen
						Queueing.dish = 5;	//	refull
						MainFrame.robot_doing[1] = "";	// reset
						MainFrame.robot_table[1] = -1;
					}
					else {
//						System.out.println("로봇2-  "+"현재x: " + String.valueOf(x1+40) + " 현재y: " + String.valueOf(y1+40)+"   목적지x: " + String.valueOf(dest1[0]) + " 목적지y: " + String.valueOf(dest1[1]));
					}
					//After moving one route, check if there is work, if not, go to the waiting area
					tempTask(2);
				}
			}
			//repaint GUI
			MainFrame.mp.repaint();
		}
	};




	//returns to the waiting area
	public void tempTask(int num) {
		//robot's coordinate
		int[] robot1 = {0,0}, robot2 = {0,0};
		robot1[0] = temp1[0];
		robot1[1] = temp1[1];
		robot2[0] = temp2[0];
		robot2[1] = temp2[1];

		//robot 1 returns to the waiting area
		task1 = new TimerTask() {
			@Override
			public void run() {
				//if there is no work for robot 1
				if(way1.isEmpty() == true) {
					//dijkstra to waiting area
					MainFrame.dj.init(1, robot1, robot2, 1);
					//save result
					int[][] position = { {0,}};
					position = MainFrame.dj.list_result();
					//push to queue, save last coordinate
					for(int i = 0; i < position.length; i++) {
						if(position[i][0] != 0 && position[i][1] != 0) {
							way1.add(new Point(position[i][0], position[i][1]));
							temp1[0] = position[i][0];
							temp1[1] = position[i][1];
						}
					}
				}
			}
		};
		//robot 1 returns to the waiting area
		task2 = new TimerTask() {
			@Override
			public void run() {
				//if there is no work for robot 2
				if(way2.isEmpty() == true) {
					//dijkstra to waiting area
					MainFrame.dj.init(1, robot1, robot2, 2);
					//save result
					int[][] position = { {0,}};
					position = MainFrame.dj.list_result();
					//push to queue, save last coordinate
					for(int i = 0; i < position.length; i++) {
						if(position[i][0] != 0 && position[i][1] != 0) {
							way2.add(new Point(position[i][0], position[i][1]));
							temp2[0] = position[i][0];
							temp2[1] = position[i][1];
						}
					}
				}
			}
		};
		//set util timer
		timerUtil = new java.util.Timer();
		//if robot1 has no work
		if(num == 1) {
			//until 3 seconds
			timerUtil.schedule(task1, 3000);
		}
		//if robot2 has no work
		else if(num==2) {
			//until 3 seconds
			timerUtil.schedule(task2, 3000);
		}
	}


	private void initComponents() {
		// for GUI

		serveBar = new JPanel();
		kitchen = new JPanel();
		tableArea = new JPanel[6];
		for (int i = 0; i < 6; i++) {
			tableArea[i] = new JPanel();
			tableArea[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
			tableArea[i].setBackground(Color.WHITE);
		}
		serveBar.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		kitchen.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		serveBar.setBackground(Color.WHITE);
		kitchen.setBackground(Color.WHITE);

		table = new TablePanel[6];
		name = new JLabel[6];
		state = new JLabel[6];
		for (int i = 0; i < 6; i++) {
			table[i] = new TablePanel(i);
			table[i].setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.DARK_GRAY));
			table[i].setBackground(Color.WHITE);
			name[i] = new JLabel(" table " + String.valueOf(i+1));
			state[i] = new JLabel("");
		}

		GroupLayout[] t = new GroupLayout[6];
		for (int i = 0; i < 6; i++) {
			t[i] = new GroupLayout(tableArea[i]);
			tableArea[i].setLayout(t[i]);
			t[i].setHorizontalGroup(
					t[i].createParallelGroup()
					.addComponent(name[i], 120, 120, 120)
					.addComponent(table[i], 118, 118, 118)
					.addComponent(state[i], 120, 120, 120)
					);
			t[i].setVerticalGroup(
					t[i].createSequentialGroup()
					.addComponent(name[i], 30, 30, 30)
					.addComponent(table[i], 60, 60, 60)
					.addComponent(state[i], 30, 30, 30)
					);
		}



		setBackground(Color.WHITE);
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addGap(140, 140, 140)
						.addComponent(serveBar, 120, 120, 120)
						.addGap(80, 80, 80)
						.addComponent(kitchen, 120, 120, 120))
				.addGroup(layout.createSequentialGroup()
						.addGap(40, 40, 40)
						.addGroup(layout.createParallelGroup()
								.addComponent(tableArea[0], 120, 120, 120)
								.addComponent(tableArea[3], 120, 120, 120))
						.addGap(80, 80, 80)
						.addGroup(layout.createParallelGroup()
								.addComponent(tableArea[1], 120, 120, 120)
								.addComponent(tableArea[4], 120, 120, 120))
						.addGap(80, 80, 80)
						.addGroup(layout.createParallelGroup()
								.addComponent(tableArea[2], 120, 120, 120)
								.addComponent(tableArea[5], 120, 120, 120))
						)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(serveBar, 40, 40, 40)
						.addComponent(kitchen, 40, 40, 40))
				.addGap(180, 180, 180)
				.addGroup(layout.createParallelGroup()
						.addComponent(tableArea[0], 120, 120, 120)
						.addComponent(tableArea[1], 120, 120, 120)
						.addComponent(tableArea[2], 120, 120, 120))
				.addGap(80, 80, 80)
				.addGroup(layout.createParallelGroup()
						.addComponent(tableArea[3], 120, 120, 120)
						.addComponent(tableArea[4], 120, 120, 120)
						.addComponent(tableArea[5], 120, 120, 120))
				);
	}

	JPanel serveBar;
	JPanel kitchen;
	public static JPanel tableArea[];
	public static TablePanel table[];
	public static JLabel name[];
	public static JLabel state[]; 

}