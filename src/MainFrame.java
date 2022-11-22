
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainFrame extends JFrame{
	
    public void init() {
        setTitle("Serving Robot Simulator");	
        
        // â �ּ�ũ�� ����(���� ����׷�������)
        double magn = 1080 / Toolkit.getDefaultToolkit().getScreenSize().getHeight(); //ȭ�����
        double minX = 1000*magn;
        double minY = 722*magn;	//(580+40+60)+42
        setMinimumSize(new Dimension((int)minX, (int)minY));
        //setResizable(false);
        //setLocationRelativeTo(null);	//����� â �߰�
        System.out.println("Frame Size = " + getSize());
        
        // ��ü ȭ��
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(this);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    
    public static void main(String args[]) {
    	MainFrame frame = new MainFrame();
        MapPane mapPane = new MapPane();
        
        JPanel centerPane = new JPanel();
        JPanel emptyPane1 = new JPanel();
        JPanel emptyPane2 = new JPanel();
        
        //frame.setContentPane(mapPane);
        //frame.getContentPane().setLayout(null);
        //frame.setLayout(new BorderLayout(frame.getWidth()/2-250, 50));
        
        frame.getContentPane().add(centerPane, BorderLayout.CENTER);
        //frame.getContentPane().add(mapPane, BorderLayout.CENTER);
        frame.getContentPane().add(emptyPane1, BorderLayout.NORTH);
        frame.getContentPane().add(emptyPane2, BorderLayout.SOUTH);
        
        emptyPane1.setPreferredSize(new Dimension(1000, 40));
        emptyPane2.setPreferredSize(new Dimension(1000, 60));
        
        centerPane.add(mapPane);
        mapPane.setPreferredSize(new Dimension(600, 580));
        
        //frame.setUndecorated(true);
        frame.pack();
        frame.init();
        //mapPane.repaint();
    }
}
