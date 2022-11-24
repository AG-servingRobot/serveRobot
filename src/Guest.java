import java.util.Calendar;
import java.util.Random;

public class Guest extends Thread{
	
	int tableNum;		// ��� ���̺� �ɾ��ִ��� (0~5)
	int[] entryTime = {0, 0};	// ���� �ð�
	int settingTimer;	// ���� ��ٸ��� Ÿ�̸� count
	int servingTimer;	// ���� ��ٸ��� Ÿ�̸� count
	int timeToCook;		// �丮�ϴ� �ð�
	int timeToStay;		// �մ��� �ӹ����� �ð�
	int[] endCook = {0, 0};		// �丮 ������ �ð�
	int[] endStay = {0, 0};		// �մ� ������ �ð�
	int[] temp = {0, 0};
	int satisfaction;	// ������


	public Guest(int num)
	{
		init(num);
	}

	// Set guest(table) Information
	public void init(int tableNum)
	{
		this.tableNum = tableNum+1;	// ���� 1~6
		
		Calendar time = Calendar.getInstance();
		this.entryTime[0] = time.get(Calendar.MINUTE);
		this.entryTime[1] = time.get(Calendar.SECOND);
//		this.entryTime[2] = time.get(Calendar.MILLISECOND);
		
		this.settingTimer = 0;
		this.servingTimer = 0;
		
		Random random = new Random();
		//random.nextInt(max - min) + min; //min ~ max
		
		this.timeToCook = random.nextInt(420 - 300) + 300;	// 5~7��. �ʴ����� ��
		this.timeToStay = random.nextInt(720 - 600) + 600;	// 10�� ���� (10~12������ �ϴ� ����)
		
		this.timeToCook = timeToCook / 10;	// 10���
		this.timeToStay = timeToStay / 10;
		
		this.satisfaction = 10;
		System.out.println("�մ� "+ String.valueOf(this.tableNum) + "�� ���̺� ����");
		System.out.println(this.tableNum + "�� ����ð�-" + this.entryTime[0]+":"+this.entryTime[1]);
	}
	
	public void run() {
		
		
		// ť�� setting push
		Queueing.init("setting.", tableNum);
		// init �Ŀ� priority ȣ��. �ϴ� init() �ȿ� ���� �־��.
		System.out.println("ť�� push: " + String.valueOf(this.tableNum) + "�� ���̺� ����");
		
		// ���� ��ٸ���
		settingCountThread t1 = new settingCountThread(this);
		t1.start();
		
		// endCook ���
		// entryTime�� timeToCook ���ϱ�
		this.endCook[0] = (this.entryTime[0] + (this.timeToCook / 60)) % 60;	//minute
		this.endCook[1] = (this.entryTime[1] + this.timeToCook) % 60;	//second
//		this.endCook[2] = this.entryTime[2];	//millisecond
		System.out.println(this.tableNum + "�� �丮�Ϸ�-" + this.endCook[0]+":"+this.endCook[1]);
		
		// endCook �ð��� �Ǹ� ť�� serving push
		while(true) {
			Calendar now = Calendar.getInstance();
			this.temp[0] = now.get(Calendar.MINUTE);
			this.temp[1] = now.get(Calendar.SECOND);
//			this.temp[2] = now.get(Calendar.MILLISECOND);
			
			if(this.temp[0] == this.endCook[0] && this.temp[1] == this.endCook[1]) {
				// ť�� serving push
				Queueing.init("serving.", this.tableNum);
				System.out.println("ť�� push: " + String.valueOf(this.tableNum) + "�� ���̺� ����");
				break;
			}
		}
		
		// ���� ��ٸ���
		servingCountThread t2 = new servingCountThread(this);
		t2.start();
		
		// ���� �Ϸ� ������ �ƹ� �͵� ���ϱ�
		while(true) {
			if(Queueing.isServingDone[this.tableNum-1]) {
				System.out.println("�ݺ��� Ż��");
				break;	// ������ �����ٴ� ��ȣ�� ���� break
			}
		}
		System.out.println("�ݺ��� Ż��2");
		// ���� �Ϸ� �� endStay ���
		// ���� �ð��� timeToStay ���ϱ�
		Calendar afterServe = Calendar.getInstance();
		this.endStay[0] = (afterServe.get(Calendar.MINUTE) + (this.timeToStay / 60)) % 60;
		this.endStay[1] = (afterServe.get(Calendar.SECOND) + this.timeToStay) % 60;
//		this.endStay[2] = afterServe.get(Calendar.MILLISECOND);
		System.out.println(this.tableNum + "�� ����-" + this.endStay[0]+":"+this.endStay[1]);
		
		// endStay �ð��� �Ǹ� ť�� clean push
		while(true) {
			Calendar now = Calendar.getInstance();
			this.temp[0] = now.get(Calendar.MINUTE);
			this.temp[1] = now.get(Calendar.SECOND);
//			this.temp[2] = now.get(Calendar.MILLISECOND);
			
			if(this.temp[0] == this.endStay[0] && this.temp[1] == this.endStay[1]) {
				// ť�� clean push
				Queueing.init("clean.", tableNum);
				System.out.println("ť�� push: " + String.valueOf(this.tableNum) + "�� ���̺� Ŭ��");
				break;
			}
		}
		
		// ������ ����ϰ� ���ո������� ������
		this.settingTimer -= 300;
		this.servingTimer -= 300;
		if(this.settingTimer > 0) {
			this.satisfaction -= this.settingTimer / 10;	// 10�� �ѱ� ������ �������� ����
		}
		if(this.servingTimer > 0) {
			this.satisfaction -= this.servingTimer / 10;
		}
		System.out.println(String.valueOf(this.tableNum) + "�� ���̺� ������: " + this.satisfaction);
		
		// clean ���Ŀ� table_state �ٲٰ� �̷� �Ŵ� ���� ���� �ٸ� ���� �ֱ�
	}
}


class settingCountThread extends Thread{
	
	Guest myG;
	
	public settingCountThread(Guest g)
	{
		myG = g;
	}
	
	public void run() {
		while(true) {
			
			if(Queueing.isSettingDone[myG.tableNum-1]) {
				System.out.println("- " + String.valueOf(myG.tableNum) + "�� ���� ī����: " + myG.settingTimer + "��");
				break;	// ������ �����ٴ� ��ȣ�� ���� break
			}
		
			// ���� ī���� ++
			myG.settingTimer = myG.settingTimer + 1;
			//System.out.println("- " + String.valueOf(myG.tableNum) + "�� ���� ī����: " + myG.settingTimer + "��");
			
			if(myG.settingTimer == 300) {
				// ������ 5�� �Ѱ� �Ȱ��� ������ queue�� �˷��ֱ�	(���ѽð����� �ϴ� �̷���)
				Queueing.message = "setting." + String.valueOf(myG.tableNum);
				Queueing.priority();
			}
			
			try {
				Thread.sleep(100);	// 0.1 second (10���)
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class servingCountThread extends Thread{
	
	Guest myG;
	
	public servingCountThread(Guest g)
	{
		myG = g;
	}
	
	public void run() {
		while(true) {
			
			if(Queueing.isServingDone[myG.tableNum-1]) {
				System.out.println("- " + String.valueOf(myG.tableNum) + "�� ���� ī����: " + myG.servingTimer + "��");
				break;	// ������ �����ٴ� ��ȣ�� ���� break
			}
		
			// ���� ī���� ++
			myG.servingTimer = myG.servingTimer + 1;
			//System.out.println("- " + String.valueOf(myG.tableNum) + "�� ���� ī����: " + myG.servingTimer + "��");
			
			if(myG.servingTimer == 300) {
				// ������ 5�� �Ѱ� �Ȱ��� ������ queue�� �˷��ֱ�	(���ѽð����� �ϴ� �̷���)
				Queueing.message = "serving." + String.valueOf(myG.tableNum);
				Queueing.priority();
			}
			
			try {
				Thread.sleep(100);	// 0.1 second (10���)
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}


