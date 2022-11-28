//package Algorithm;

import java.awt.Color;
import java.util.Calendar;
import java.util.Random;

public class Guest extends Thread{
	
	int tableNum;		// 紐뉖쾲 �뀒�씠釉붿뿉 �븠�븘�엳�뒗吏� (0~5)
	int[] entryTime = {0, 0};	// �엯�옣 �떆媛�
	int settingTimer;	// �꽭�똿 湲곕떎由щ뒗 ���씠癒� count
	int servingTimer;	// �꽌鍮� 湲곕떎由щ뒗 ���씠癒� count
	int timeToCook;		// �슂由ы븯�뒗 �떆媛�
	int timeToStay;		// �넀�떂�씠 癒몃Т瑜대뒗 �떆媛�
	int[] endCook = {0, 0};		// �슂由� �걹�굹�뒗 �떆媛�
	int[] endStay = {0, 0};		// �넀�떂 �굹媛��뒗 �떆媛�
	int[] temp = {0, 0};
	int satisfaction;	// 留뚯”�룄
	static final int LIMIT = 90;	// �젣�븳�떆媛� - 30�젙�룄濡� 以꾩씠硫� �솗 �뱶�윭�궓.


	public Guest(int num)
	{
		init(num);
	}

	// Set guest(table) Information
	public void init(int tableNum)
	{
		this.tableNum = tableNum+1;	// �씠�젣 1~6
		
		Calendar time = Calendar.getInstance();
		this.entryTime[0] = time.get(Calendar.MINUTE);
		this.entryTime[1] = time.get(Calendar.SECOND);
		
		this.settingTimer = 0;
		this.servingTimer = 0;
		
		Random random = new Random();
		//random.nextInt(max - min) + min; //min ~ max
		
		this.timeToCook = random.nextInt(240 - 120) + 120;	// 2~4遺�. 珥덈떒�쐞濡� ��. 蹂댁뿬二쇨린�뿉 �뒓�젮�꽌 議곗젅�븿.
		this.timeToStay = random.nextInt(420 - 300) + 300;	// 5~7遺�
		
		this.timeToCook = timeToCook / 10;	// 10諛곗냽
		this.timeToStay = timeToStay / 10;
		
		this.satisfaction = 10;
//		System.out.println("- �넀�떂 "+ String.valueOf(this.tableNum) + "踰� �뀒�씠釉붿뿉 李⑹꽍");
//		System.out.println(this.tableNum + "踰� �엯�옣�떆媛�-" + this.entryTime[0]+":"+this.entryTime[1]);
		

	}
	
	public void run() {
		
		
		// �걧�뿉 setting push
		Queueing.init("setting.", tableNum);
		// �빆�긽 init �썑�뿉 priority �샇異�. init() �븞�뿉 吏곸젒 �꽔�뼱�몺.
		System.out.println("! 큐에 push: " + String.valueOf(this.tableNum) + "번 테이블 세팅");
		
		// �꽭�똿 湲곕떎由ш린
		settingCountThread t1 = new settingCountThread(this);
		t1.start();
		
		// endCook 怨꾩궛
		// entryTime�뿉 timeToCook �뜑�븯湲�
		this.endCook[0] = (this.entryTime[0] + (this.timeToCook / 60)) % 60;	//minute
		this.endCook[1] = (this.entryTime[1] + this.timeToCook) % 60;	//second
		if (this.entryTime[1] + this.timeToCook >= 60) {
			this.endCook[0] = (this.endCook[0] + 1) % 60;
		}
		
		// endCook �떆媛꾩씠 �릺硫� �걧�뿉 serving push
		while(true) {
			Calendar now = Calendar.getInstance();
			this.temp[0] = now.get(Calendar.MINUTE);
			this.temp[1] = now.get(Calendar.SECOND);
			
			if(this.temp[0] == this.endCook[0] && this.temp[1] == this.endCook[1]) {
				// �걧�뿉 serving push
				Queueing.init("serving.", this.tableNum);
				System.out.println("! 큐 push: " + String.valueOf(this.tableNum) + "번 테이블 서빙");
				break;
			}
		}
		
		// �꽌鍮� 湲곕떎由ш린
		servingCountThread t2 = new servingCountThread(this);
		t2.start();
		
		// �꽌鍮� �셿猷� �쟾源뚯� �븘臾� 寃껊룄 �븞�븯湲�
		while(true) {
			if(MainFrame.isServingDone[this.tableNum-1]) {
				break;	// �꽌鍮숈씠 �걹�궗�떎�뒗 �떊�샇媛� �삤硫� break
			}
			try {
				Thread.sleep(100);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// �꽌鍮� �셿猷� �썑 endStay 怨꾩궛
		// �쁽�옱 �떆媛꾩뿉 timeToStay �뜑�븯湲�
		Calendar afterServe = Calendar.getInstance();
		this.endStay[0] = (afterServe.get(Calendar.MINUTE) + (this.timeToStay / 60)) % 60;
		this.endStay[1] = (afterServe.get(Calendar.SECOND) + this.timeToStay) % 60;
		if (afterServe.get(Calendar.SECOND) + this.timeToStay >= 60) {
			this.endStay[0] = (this.endStay[0] + 1) % 60;
		}
		
		// endStay �떆媛꾩씠 �릺硫� �걧�뿉 clean push
		while(true) {
			Calendar now = Calendar.getInstance();
			this.temp[0] = now.get(Calendar.MINUTE);
			this.temp[1] = now.get(Calendar.SECOND);
			
			if(this.temp[0] == this.endStay[0] && this.temp[1] == this.endStay[1]) {
				// �걧�뿉 clean push
				MainFrame.haveToClean[this.tableNum - 1] = true;
				MapPane.table[this.tableNum - 1].repaint();
				Queueing.init("clean.", tableNum);
				System.out.println("! 큐에 push: " + String.valueOf(this.tableNum) + "클린");
				break;
			}
		}
		
		// 留뚯”�룄 怨꾩궛�븯怨� 珥앺빀留뚯”�룄濡� 蹂대궡湲�
		this.settingTimer -= LIMIT;
		this.servingTimer -= LIMIT;
		if(this.settingTimer > 0) {
			this.satisfaction -= this.settingTimer / 5;	// 5珥� �꽆湲� �븣留덈떎 留뚯”�룄媛� 源롮엫
		}
		if(this.servingTimer > 0) {
			this.satisfaction -= this.servingTimer / 5;
		}

		MapPane.state[this.tableNum-1].setText("clean. score: " + this.satisfaction);//�넀�떂 �굹媛�硫� 留뚯”�룄 �쓣�슦湲�
		
		// clean �씠�썑�뿉 table_state 諛붽씀怨� �씠�윴 嫄곕뒗 �뿬湲� 留먭퀬 �떎瑜� 怨녹뿉 �꽔湲�
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
			
			if(MainFrame.isSettingDone[myG.tableNum-1]) {
//				MapPane.state[myG.tableNum-1].setText("took " + myG.settingTimer + "s to setting");	// 湲곕떎由� �떆媛� 寃곌낵
//				MapPane.state[myG.tableNum-1].setText("setting done in " + myG.settingTimer + "s");	// 湲곕떎由� �떆媛� 寃곌낵
				MapPane.state[myG.tableNum-1].setForeground(Color.BLACK);	// �샊�떆 湲��옄 �깋 諛붽펷�쑝硫� �룎�젮�넄二쇨퀬
				MapPane.state[myG.tableNum-1].setText("cook done at " + myG.endCook[0] + ":" + myG.endCook[1]);//�슂由щ걹�굹�뒗�떆媛� �쓣�슦湲�
				break;	// �꽭�똿�씠 �걹�궗�떎�뒗 �떊�샇媛� �삤硫� break
			}
		
			// �꽭�똿 移댁슫�꽣 ++
			myG.settingTimer = myG.settingTimer + 1;
			MapPane.state[myG.tableNum-1].setText("wait setting..." + myG.settingTimer);	// �꽭�똿 ��湲� ���씠癒� �몴�떆
			
			if(myG.settingTimer == myG.LIMIT) {	// �뀒�뒪�듃�븷 �븣 �닽�옄 �솗 以꾩뿬蹂대㈃ �옒 �릺�뒗吏� �굹�샂
				// �꽭�똿�씠 3遺� �꽆寃� �븞媛�怨� �엳�쑝硫� queue�뿉 �븣�젮二쇨린	(�젣�븳�떆媛꾨쭚怨� �씪�떒 �씠�젃寃�)
				Queueing.message = "setting." + String.valueOf(myG.tableNum);
				Queueing.priority();
				MapPane.state[myG.tableNum-1].setForeground(Color.RED);	// 寃쎄퀬 gui濡� �몴�떆
//				System.out.println("~ " + myG.tableNum + "踰� �꽭�똿 �젣�븳�떆媛� �꽆源� ~");
			}
			
			try {
				Thread.sleep(100);	// 0.1 second (10諛곗냽)
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
			
			if(MainFrame.isServingDone[myG.tableNum-1]) {
				try {
					Thread.sleep(500);	// endStay 怨꾩궛�븯湲곌퉴吏� �옞源� �돩怨�
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
//				MapPane.state[myG.tableNum-1].setText("took " + myG.settingTimer + "s to serving");	// 湲곕떎由� �떆媛� 寃곌낵
				MapPane.state[myG.tableNum-1].setForeground(Color.BLACK);	// �샊�떆 湲��옄 �깋 諛붽펷�쑝硫� �룎�젮�넄二쇨퀬
				MapPane.state[myG.tableNum-1].setText("stay until " + myG.endStay[0] + ":" + myG.endStay[1]);//�넀�떂�눜�옣�떆媛� �쓣�슦湲�
				break;	// �꽌鍮숈씠 �걹�궗�떎�뒗 �떊�샇媛� �삤硫� break
			}
		
			// �꽌鍮� 移댁슫�꽣 ++
			myG.servingTimer = myG.servingTimer + 1;
			MapPane.state[myG.tableNum-1].setText("wait serving..." + myG.servingTimer);	// �꽌鍮� ��湲� ���씠癒� �몴�떆
			
			if(myG.servingTimer == myG.LIMIT) {
				// �꽌鍮숈씠 3遺� �꽆寃� �븞媛�怨� �엳�쑝硫� queue�뿉 �븣�젮二쇨린	(�젣�븳�떆媛꾨쭚怨� �씪�떒 �씠�젃寃�)
				Queueing.message = "serving." + String.valueOf(myG.tableNum);
				Queueing.priority();
				MapPane.state[myG.tableNum-1].setForeground(Color.RED);	// 寃쎄퀬 gui濡� �몴�떆
//				System.out.println("~ " + myG.tableNum + "踰� �꽌鍮� �젣�븳�떆媛� �꽆源� ~");
			}
			
			try {
				Thread.sleep(100);	// 0.1 second (10諛곗냽)
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}


