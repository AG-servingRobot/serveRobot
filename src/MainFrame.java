
import java.awt.BorderLayout;
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


public class MainFrame extends JFrame implements Runnable, ActionListener{
	
	public static void main(String args[]) throws Exception {	// out()�� ���� throws Exception �߰�
    	
		MainFrame frame = new MainFrame();
		MapPane mapPane = new MapPane();
		JPanel centerPane = new JPanel();

		frame.init();
		centerPane.add(mapPane);
        mapPane.setPreferredSize(new Dimension(600, 580));
        frame.getContentPane().add(centerPane, BorderLayout.CENTER);
        frame.pack();
		
		frame.setVisible(true);

		int[][] position = {
				{400, 80},
				{400, 180},
				{400, 280},
				{400, 380},
				{400, 280}
		};
		mapPane.setRobot(1, position);
		int[][] position2 = {
				{200, 80},
				{200,180},
				{200,280},
				{200,80},
				{300, 80},
				{400,80}
		};
		mapPane.setRobot(2, position2);
		
		
		// Queue and guest test
		while(true)
		{
			String[] temp_str = Queueing.out();
			
			 
			//temporary check
			if(temp_str!=null)
			{
//				System.out.println(temp_str[0]);
//				System.out.println(temp_str[1]);//use to parseInt, so String -> int 
			}
			else
			{
				System.out.println("Running");
			}
			
			Thread.sleep(3000);
		}
    }
	
	
    public void init() {
    	// initialize GUI
        
        // â �ּ�ũ�� ����(���� ����׷�������)
        double magn = 1080 / Toolkit.getDefaultToolkit().getScreenSize().getHeight(); //ȭ�����
        double minX = 1000*magn;
        double minY = 722*magn;	//(580+40+60)+42
        setMinimumSize(new Dimension((int)minX, (int)minY));
        //setResizable(false);
        //setLocationRelativeTo(null);	//����� â �߰�
        //System.out.println("Frame Size = " + getSize());
        
        // ��ü ȭ��
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(this);

        setTitle("Serving Robot Simulator");	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //mapPane = new MapPane();
        //centerPane = new JPanel();
        emptyPane1 = new JPanel();
        emptyPane2 = new JPanel();
        
        // �ð� ���� �׽�Ʈ
        timeTestLabel = new JLabel("00 : 00 : 00");
        emptyPane1.add(timeTestLabel);
        new Thread(this).start();
        
        // guest ���� �� �ڸ� ����
        guest = new Guest[6];
        
        // �մ� ���� ��ư
        guestEntranceBtn = new JButton();
        guestEntranceBtn.setText("Accept Guests");
        guestEntranceBtn.addActionListener(this);
        emptyPane2.add(guestEntranceBtn);
        
        //getContentPane().add(centerPane, BorderLayout.CENTER);
        getContentPane().add(emptyPane1, BorderLayout.NORTH);
        getContentPane().add(emptyPane2, BorderLayout.SOUTH);
        
        emptyPane1.setPreferredSize(new Dimension(1000, 40));
        emptyPane2.setPreferredSize(new Dimension(1000, 60));
        
//        centerPane.add(mapPane);
//        mapPane.setPreferredSize(new Dimension(600, 580));
//        
//        pack();
    }
    
      
    
    
    
    // Real-time updates
    public void run()
    {
 	   //�ݺ�x�̸� �ݺ��� �ۿ�
 	   
 	   while(true) {
 		   Calendar time = Calendar.getInstance();
 		   int hh, mm, ss;
 		   hh = time.get(Calendar.HOUR_OF_DAY);
 	       mm = time.get(Calendar.MINUTE);
 	       ss = time.get(Calendar.SECOND);
 		   timeTestLabel.setText(hh + "��" + mm + "��" + ss + "��");
 		   
 		   try {
 			   Thread.sleep(1000);	// 1 second
 		   } catch(InterruptedException e) {
 			   e.printStackTrace();
 		   }
 	   }
 	   
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       // TODO Auto-generated method stub
 	  
 	   if (e.getSource() == guestEntranceBtn) {
 		   // �� ���̺� üũ
 		   for (int i = 0; i < 6; i++) { 
 			   if (Queueing.table_state[i] == 0) {
 				   // �� ���̺� �ѹ��� �Ѱ��ְ� guest ����.  ���̺� �ɴ� �ڸ��� �ϴ� ���� �ƴ�
 				   guest[i] = new Guest(i);	// guest ����, �ʱ�ȭ
 				   guest[i].start();	// guest thread ����
 				  Queueing.table_state[i] = 1;	// �ڸ� ��
 				  break;
 			   }
 			   else {
 				   if (i == 5) {	// �� á���� ���ְ�. 
 					   JOptionPane.showMessageDialog(null, "The restaurant is full","alert", JOptionPane.WARNING_MESSAGE);
 				   }
 			   }
 		   }
 		   
 		   
 	   }
 	   
    }
    
    
    //MapPane mapPane;
    //JPanel centerPane;
    JPanel emptyPane1;
    JPanel emptyPane2;
    JLabel timeTestLabel;
    JButton guestEntranceBtn;
    
    //public static int table_state[] = {0,0,0,0,0,0};
    Guest[] guest;

}
