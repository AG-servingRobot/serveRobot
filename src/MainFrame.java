import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements Runnable, ActionListener {
	public static Queueing queue = new Queueing();// add Queueing ++++++++++++++++++++++++
	public static Dijkstra dj = new Dijkstra(); // add Dijkstra ++++++++++++++++++++++++++
	public static MapPane mp = new MapPane();// add MapPane ++++++++++++++++++++++++++
	public static int[][] visit_xy = null;// add visited nodes++++++++++++++++++++++++++
	public static int[] dest = null;// add dest nodes
	private static int[][] transfer = null;// add transfer nodes++++++++++++++++++++

	public static int[] table_state = new int[6];
	public static boolean[] isSettingDone = new boolean[6];
	public static boolean[] isServingDone = new boolean[6];
	public static boolean[] haveToClean = new boolean[6];
	public static String[] robot_doing = new String[] { "", "" };
	public static int[] robot_table = new int[2];
	public static int running_Robot;
	public static void main(String args[]) throws Exception { // out()占쎈쐻占쎈윪獄�占� 占쎈쐻占쎈윪筌띻쐼�쐻占쎈윥占쎈뤅 throws Exception �뜝�럥�돯占쎄껀占쎈짗占쎌굲

		MainFrame frame = new MainFrame();
		// MapPane mapPane = new MapPane();
		JPanel centerPane = new JPanel();

		frame.init();
		centerPane.add(mp);
		mp.setPreferredSize(new Dimension(600, 580));
		frame.getContentPane().add(centerPane, BorderLayout.CENTER);
		frame.pack();

		frame.setVisible(true);

		int[][] position = { { 400, 80 }, { 400, 180 }, { 400, 280 }, { 400, 380 }, { 400, 280 } };
		// mapPane.setRobot(1, position);
		int[][] position2 = { { 200, 80 }, { 200, 180 }, { 200, 280 }, { 200, 80 }, { 300, 80 }, { 400, 80 } };
		// mapPane.setRobot(2, position2);

		while (true) {
			while (true)// check robot state
			{
				
				if (mp.isFree() != 0 && Queueing.state() == 0)
					break;
				Thread.sleep(100);
			}

			visit_xy = null;
			transfer = null;
			dest = null;
			int working_robot;
			int[] robot1 = null;
			int[] robot2 = null;

			int node;
			int[] setting = { 200, 80 };
			int[] serving = { 400, 80 };

			String[] temp_str = queue.out();
			node = change_node(Integer.parseInt(temp_str[1]), temp_str[0]);
			working_robot = mp.isFree();

			robot1 = mp.getInfo(1);
			robot2 = mp.getInfo(2);

			System.out.println(running_Robot);
			if (temp_str[0].equals("serving") || temp_str[0].equals("setting"))// if "serving" or "settting"
			{
				if (temp_str[0].equals("setting"))// if "setting"
				{
					// 占쎈あ 嚥≪뮆�겦 餓ο옙 占쎈듇揶쏉옙 占쎈막 筌욑옙
					dj.init(1, robot1, robot2, working_robot);

					dest = dj.dest_num();
					/*
					 * for(int i = 0; i < dj.list_result().length; i++)
					 * System.out.println(dj.list_result()[i][0] + "," + dj.list_result()[i][1]);
					 * System.out.println(dest); System.out.println(running_Robot);
					 */
					// System.out.println(dj.list_result()[0]);()
					visit_xy = dj.list_result();
					running_Robot = dj.workRobot();
					mp.setRobot(running_Robot, visit_xy, dest);

					if (running_Robot == 1) {
						dj.init(node, setting, robot2, 1);
						robot_doing[0] = temp_str[0];
						robot_table[0] = Integer.parseInt(temp_str[1]);
					} else if (running_Robot == 2) {
						dj.init(node, robot1, setting, 2);
						robot_doing[1] = temp_str[0];
						robot_table[1] = Integer.parseInt(temp_str[1]);
					}

					while (true) {
						Queueing.priority();
						if (mp.getInfo(running_Robot)[0] == setting[0]
								&& mp.getInfo(running_Robot)[1] == setting[1])
							break;
						Thread.sleep(10);
					}
					Queueing.dish -=1;
					visit_xy = null;
					visit_xy = dj.list_result();
					dest = dj.dest_num();
					mp.setRobot(running_Robot, visit_xy, dest);

				} else if (temp_str[0].equals("serving")) // if "serving"
				{
					// 占쎈あ 嚥≪뮆�겦 餓ο옙 占쎈듇揶쏉옙 占쎈막 筌욑옙
					dj.init(3, robot1, robot2, working_robot);

					dest = dj.dest_num();
					/*
					 * for(int i = 0; i < dj.list_result().length; i++)
					 * System.out.println(dj.list_result()[i][0] + "," + dj.list_result()[i][1]);
					 * System.out.println(dest); System.out.println(running_Robot);
					 */
					// System.out.println(dj.list_result()[0]);()
					visit_xy = dj.list_result();
					running_Robot = dj.workRobot();
					mp.setRobot(running_Robot, visit_xy, dest);

					if (running_Robot == 1) {
						dj.init(node, serving, robot2, 1);
						robot_doing[0] = temp_str[0];
						robot_table[0] = Integer.parseInt(temp_str[1]);
					} else if (running_Robot == 2) {
						dj.init(node, robot1, serving, 2);
						robot_doing[1] = temp_str[0];
						robot_table[1] = Integer.parseInt(temp_str[1]);
					}

					while (true) {

						if (mp.getInfo(running_Robot)[0] == serving[0]
								&& mp.getInfo(running_Robot)[1] == serving[1])
							break;
						Thread.sleep(10);

						System.out.println(mp.getInfo(running_Robot)[0]);
					}

					visit_xy = null;
					visit_xy = dj.list_result();
					dest = dj.dest_num();
					mp.setRobot(running_Robot, visit_xy, dest);
				}

			} else if (temp_str[0].equals("refull"))// if refull
			{
				// 占쎈あ 嚥≪뮆�겦 餓ο옙 占쎈듇揶쏉옙 占쎈막 筌욑옙
				dj.init(3, robot1, robot2, working_robot);

				dest = dj.dest_num();
				
				/*
				 * for(int i = 0; i < dj.list_result().length; i++)
				 * System.out.println(dj.list_result()[i][0] + "," + dj.list_result()[i][1]);
				 * System.out.println(dest); System.out.println(running_Robot);
				 */
				// System.out.println(dj.list_result()[0]);()
				visit_xy = dj.list_result();
				running_Robot = dj.workRobot();
				mp.setRobot(running_Robot, visit_xy, dest);
				System.out.println(running_Robot);
				if (running_Robot == 1) {
					dj.init(1, serving, robot2, 1);
					robot_doing[0] = temp_str[0];
					robot_table[0] = Integer.parseInt(temp_str[1]);
				} else if (running_Robot == 2) {
					dj.init(1, robot1, serving, 2);
					robot_doing[1] = temp_str[0];
					robot_table[1] = Integer.parseInt(temp_str[1]);
				}

				while (true) {

					if (mp.getInfo(running_Robot)[0] == serving[0]
							&& mp.getInfo(running_Robot)[1] == serving[1])
						break;
					Thread.sleep(10);

					System.out.println(mp.getInfo(running_Robot)[0]);
				}

				visit_xy = null;
				visit_xy = dj.list_result();
				dest = dj.dest_num();
				mp.setRobot(running_Robot, visit_xy, dest);

			} else if (temp_str[0].equals("clean"))// if clean
			{
				dj.init(node, robot1, robot2, working_robot);
				running_Robot = dj.workRobot();
				if (running_Robot == 1) {
					robot_doing[0] = temp_str[0];
					robot_table[0] = Integer.parseInt(temp_str[1]);
				} else if (running_Robot == 2) {
					robot_doing[1] = temp_str[0];
					robot_table[1] = Integer.parseInt(temp_str[1]);
				}
				dest = dj.dest_num();
				// don't need node?
				visit_xy = dj.list_result();
				mp.setRobot(running_Robot, visit_xy, dest);

			}
		}
	}



public void init() {
	// initialize GUI

		// 癲ル슓�뙔占쎌굲 癲ル슔�걠獒뺣돍�삕�댆戮⑸쐻占쎈윞�떋�슌堉⑨옙癒��굲 占쎈쐻占쎈윞占쎈쭫占쎈쐻占쎈윪占쎌젳(癲ル슢�뵯甕곕뵃�삕�억옙
		// 占쎈쐻占쎈윥占쎈떛癲ル슔占쎈낌�뮚�뜝�럩�읇占쎈쐻占쎈윪占쎈첓癲ル슣�돸占쎌굲占쎈쐻占쎈윥筌앷엥�뒙占쎈뙔占쎌굲)
		double magn = 1080 / Toolkit.getDefaultToolkit().getScreenSize().getHeight(); // 占쎈쐻占쎈윪占쎄섀癲ル슢�닪�뜝�뜴泥롳옙�땬占쎌굲�뜝�럥泥�
		double minX = 1000 * magn;
		double minY = 722 * magn; // (580+40+60)+42
		setMinimumSize(new Dimension((int) minX, (int) minY));
		// setResizable(false);
		// setLocationRelativeTo(null); //占쎈쨬占쎈즸占쎌굲占쎈쐻占쎈윪占쎈�㏆옙�쐻占쎈윥占쎈ぅ占쎈쐻占쎈윥占쎈군 癲ル슓�뙔占쎌굲 占쎈쐻占쎈윪筌륁룂�눇�뙼蹂��굲
		// System.out.println("Frame Size = " + getSize());

		// 占쎈쐻占쎈윪占쎌벁癲ル슪�맔占쎌굲 占쎈쐻占쎈윪占쎄섀癲ル슢�뺧옙�굲
		GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = graphics.getDefaultScreenDevice();
		device.setFullScreenWindow(this);

		setTitle("Serving Robot Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// mapPane = new MapPane();
		// centerPane = new JPanel();
		emptyPane1 = new JPanel();
		emptyPane2 = new JPanel();

		// 占쎈쐻占쎈윥筌묒궏琉놅옙猷딉옙�굲 占쎈쐻占쎈윪獄��뜴�쐻占쎈윪占쎈�∽옙堉⑨옙癒��굲
		timeTestLabel = new JLabel("00 : 00");
		emptyPane1.add(timeTestLabel);
		new Thread(this).start();

		// guest 占쎈쐻占쎈윪占쎌젳占쎌녃域밟뫁�굲 占쎈쐻占쎈윥�뤃�쑚�쐻占쎈윥占쎄퐨占쎈쨬占쎈즸占쎌굲 占쎈쐻占쎈윪占쎄껑�뜝�럥�렓占쎈쐻�뜝占� 占쎈쐻占쎈윞�눧硫⑤쐻占쎈윞占쎈렰
		guest = new Guest[6];

		// 占쎈쐻占쎈윥�뜝�뜴�쐻占쎈윥筌묕옙 占쎈쐻占쎈윪�굢占쏙옙�쐻占쎈윪占쎄텑 占쎈탶�⑤슣維볟뜝�럥愿�
		guestEntranceBtn = new JButton();
		guestEntranceBtn.setText("Accept Guests");
		guestEntranceBtn.addActionListener(this);
		emptyPane2.add(guestEntranceBtn);

		// getContentPane().add(centerPane, BorderLayout.CENTER);
		getContentPane().add(emptyPane1, BorderLayout.NORTH);
		getContentPane().add(emptyPane2, BorderLayout.SOUTH);

		emptyPane1.setPreferredSize(new Dimension(1000, 40));
		emptyPane2.setPreferredSize(new Dimension(1000, 60));

		// centerPane.add(mapPane);
		// mapPane.setPreferredSize(new Dimension(600, 580));
		//
		// pack();
	}

	// table number -> node number ++++++++++++++++++++++++++++++++++++++++
	private static int change_node(int table, String operation) {
		if (table == 0)
			return 1;
		else if (table == 1)
			return 6;
		else if (operation.equals("setting") && table == 2)
			return 6;
		else if (operation.equals("serving") && table == 2)
			return 7;
		else if (operation.equals("clean") && table == 2) {
			if (mp.getInfo(running_Robot)[0] < 300)
				return 6;
			else
				return 7;
		} else if (table == 3)
			return 7;
		else if (table == 4)
			return 11;
		else if (operation.equals("setting") && table == 5)
			return 11;
		else if (operation.equals("serving") && table == 5)
			return 12;
		else if (operation.equals("clean") && table == 5) {
			if (mp.getInfo(running_Robot)[0] < 300)
				return 11;
			else
				return 12;
		} else if (table == 6)
			return 12;
		return 0;
	}

	// Real-time updates
	public void run() {
		// 占쎈쎗占쎈즵�몭�씛�삕亦낅쓺占쎈쐻占쎈윪�얠쥉異�嚥〓끃�굲 占쎈쎗占쎈즵�몭�씛�삕亦낆���삕占쎈떛占쎈쐻�뜝占� 占쎈쎗占쎈즴占쎈뤉占쎄덩�뜝占�

		while (true) {
			Calendar time = Calendar.getInstance();
			int mm, ss;
			mm = time.get(Calendar.MINUTE);
			ss = time.get(Calendar.SECOND);
			timeTestLabel.setText(mm + ":" + ss);

   
			try {
				Thread.sleep(1000); // 1 second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == guestEntranceBtn) {
			// �뜝�럥�몡占쎈쐻�뜝占� 占쎈쐻占쎈윥�뜝�럥占쏙옙占쎌굲�뜝�럥�꼧�뜝�럥��占쎈쐻�뜝占� 癲ル슪�맊占쎌깙�뇦猿볦삕
			for (int i = 0; i < 6; i++) {
				if (table_state[i] == 0) {
					// 占쎈쐻占쎈윪�억옙 占쎈쐻占쎈윥�뜝�럥占쏙옙占쎌굲�뜝�럥�꼧�뜝�럥��占쎈쐻�뜝占� 占쎈쐻占쎈윞占쎈쭆占쎈탶�⑤슣維딉옙�쐻占쎈짗占쎌굲 占쎈쐻占쎈윞占쎈쭆占쎈눇�댆�슜爾쎾죰�봿裕��뜝�뜦維낉옙�굲 guest
					// 占쎈쐻占쎈윞�눧硫⑤쐻占쎈윞占쎈렰. 占쎈쐻占쎈윥�뜝�럥占쏙옙占쎌굲�뜝�럥�꼧�뜝�럥��占쎈쐻�뜝占� 占쎈쐻占쎈윥占쎈떢占쎈쐻占쎈윥獒뺧옙
					// 占쎈쐻占쎈윪占쎄껑�뜝�럥�렓�뜝�럥留㎬짆占� 占쎈쐻占쎈윪�앗껊쐻占쎈윥�젆占� 占쎈쐻占쎈윪占쎄뎀占쎈쐻占쎈윥占쎈첆 占쎈쐻占쎈윥占쎈떋占쎈쐻占쎈윥筌묕옙
					guest[i] = new Guest(i); // guest 占쎈쐻占쎈윞�눧硫⑤쐻占쎈윞占쎈렰, �뜝�럥竊뤷뜝�럥�맃占쎈뎨�ⓦ끉�굲�뜝�럥瑗�
					guest[i].start(); // guest thread 占쎈쐻占쎈윥筌묒궍�쐻占쎈윪占쎄탾
					table_state[i] = 1; // 占쎈쐻占쎈윪占쎄껑�뜝�럥�렓占쎈쐻�뜝占� 癲ル슓�뙔占쎌굲
					MapPane.table[i].setBackground(Color.LIGHT_GRAY); // �뜝�럩�겱占쎈뎨�뜝占� 嶺뚢돦�닑占쎈펲�뜝�럥裕� �뜝�럥�걫�뜝�럥六� gui
					break;
				} else {
					if (i == 5) { // 占쎈쐻占쎈윥�젆占� 癲ル슓�룱占쎈땾�뜝�럥�걬癲ル슢�뺧옙�굲 癲ル슢履뉑쾮醫꾨빝�겫猷밸Ъ占쎈쐻�뜝占�.
						JOptionPane.showMessageDialog(null, "The restaurant is full", "alert",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		}
	}

	JPanel emptyPane1;
	JPanel emptyPane2;
	JLabel timeTestLabel;
	JButton guestEntranceBtn;

	Guest[] guest;
}		