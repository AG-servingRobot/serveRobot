import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

//main method is placed main class
//this class has 3method operating in the main class
//init(String, int) method is add queue
//out() method is return next operation to main class 
//priority() is  rearrange queues

public class Queueing {

	public static String message = null;
	public static int dish=10;

	private static Queue<String> Queue = new LinkedList<>();
	private static Queue<String> clean_Queue = new LinkedList<>();
	private static Queue<String> priority_Queue = new LinkedList<>();
	private static Queue<String> temp_Queue = new LinkedList<>();
	public static int[] table_state =new int[6];
	public static boolean[] isSettingDone =new boolean[6];
	public static boolean[] isServingDone =new boolean[6];

	//kind of priority
	//'.' is Delimiter to robot go to where table
	//ex) 'setting.5' is robot should go table number5 to do 'setting' at table 5

	
	
	
	/*
	public static void main(String[] args) throws Exception
	{
	
	}
	*/

	public static void priority(){	// static �־�þ��
	
		

		//1st priority "refull" setting and lowest priority "refull" setting
		if(dish<=5&&Queue.isEmpty())//Queue is empty then do lowest priority "refull" 	
			Queue.add("refull.0");
		if(dish==0&&!priority_Queue.isEmpty()&&priority_Queue.element()!="refull.0")//dish is zero then do 1st priority "refull"
		{
			

			if(priority_Queue.contains("refull.0"))
				priority_Queue.remove("refull.0");
			if(Queue.contains("refull.0"))
				Queue.remove("refull.0");
			priorityQueue();
			priority_Queue.add("refull.0");
			repriorityQueue();
		}
		else if(dish==0&&priority_Queue.isEmpty())
		{
			if(Queue.contains("refull.0"))
				Queue.remove("refull.0");
			priority_Queue.add("refull.0");
		}
			
		//setting the 2nd, 3rd priority "setting" or "serving"
		//In one table, Necessarily "setting" > "serving"
		//At different tables,"setting" >= "serving"
		if(!(message==null))
		{
			if(Queue.contains(message))
				Queue.remove(message);
			if(priority_Queue.contains(message))
				return;
			else
			{
				priority_Queue.add(message);
				message = null;//main.message = null;
			}
				

			
		}
		
			
		//Thread.sleep(100); this page or main page in infinity loop
			
			
	}
		
	
	
	//input where the main class of project
	public static void init(String operation, int table){	// static �־�þ��

		if(operation.equals("clean."))
		{
			clean_Queue.add(operation+table);
		}
		else
			Queue.add(operation+table);
		priority();	// init ���Ŀ� �׻� priority ȣ���ϸ� ���� �� ���Ƽ� �־�þ��
	}
	
	
	public static String[] out() throws Exception {	// static �־�þ��
		
		String str1;
		String str2;
		String opr = null;
		

		//if another robot do refull operation
		//other robot do serving operation that finished setting operation tables
		if(dish==0&&priority_Queue.element().equals("refull.0"))
		{
			opr = priority_Queue.poll();

		}
		else if(dish==0&&!priority_Queue.element().equals("refull.0")) 
		{

			if(!priority_Queue.isEmpty())
				opr=zeroDishPriority();
			if(opr==null)
				opr=zeroDishClean();
			if(opr==null)
				opr=zeroDishQueue();
			
			
		}
		else
		{
			if(!priority_Queue.isEmpty())
				opr = priority_Queue.poll();
			else if(!clean_Queue.isEmpty())
				opr = clean_Queue.poll();
			else if(!Queue.isEmpty())
				opr = Queue.poll();
			else
				return null;
		}

		
		//we return operation and table number to other class of same project
		System.out.println("�κ�-"+opr);		
		
		if(opr == null) {
			return null;	// ������� �Դµ��� null�� ��찡 �Ȱɷ����� ��찡 �ֳ����� ������ �� �� �������
		}
		str1 =opr.substring(0, opr.indexOf("."));
		str2 = opr.substring(opr.indexOf(".")+1);
		String[] str = {str1, str2};
		if(str1.equals("setting")) {
//			table_state[Integer.parseInt(str2)-1]=1;	// Guest ������ �� �ٲ㼭 �켱 ���ֺý��ϴ�
			isSettingDone[Integer.parseInt(str2)-1] = true;	// �κ��� ���� ���� �Ŀ� �ٲ��� ������ �켱 ���⿡...
			dish -= 1;	
		}
		if(str1.equals("serving")) {
			isServingDone[Integer.parseInt(str2)-1] = true;	// �κ��� ���� ���� �Ŀ� �ٲ��� ������ �켱 ���⿡...
		}
		if(str1.equals("clean")) {
			table_state[Integer.parseInt(str2)-1]=0;	// �κ��� ���� ���� �Ŀ� �ٲ��� ������ �켱 ���⿡...
			isSettingDone[Integer.parseInt(str2)-1] = false;
			isServingDone[Integer.parseInt(str2)-1] = false;
		}

		
		
 		if(opr.equals("refull.0"))// temporary if 
 			dish=10;
 		
		return str;
	}
	
	//priority_Queue value -> temp_Queue 
	private static void priorityQueue()
	{
		for(int i=0;i<priority_Queue.size();i++)
			temp_Queue.add(priority_Queue.poll());
		
	}
	
	//temp_Queue -> Queue value 
	private static void repriorityQueue()
	{
		for(int i=0;i<temp_Queue.size();i++)
			priority_Queue.add(temp_Queue.poll());
	}
	
	
	//Queue value -> temp_Queue 
		private static void tempQueue()
		{
			for(int i=0;i<Queue.size();i++)
				temp_Queue.add(Queue.poll());
		}
		
		//temp_Queue -> priority_Queue value 
		private static void retempQueue()
		{
			for(int i=0;i<temp_Queue.size();i++)
				Queue.add(temp_Queue.poll());
		}
	
	private static String zeroDishPriority()
	{
		String str=null;
		while(true)
		{
			if(!priority_Queue.isEmpty())
			{
				if(priority_Queue.element().substring(0, priority_Queue.element().indexOf(".")).equals("serving"))
					temp_Queue.add(priority_Queue.poll());
				else if(priority_Queue.element().substring(0, priority_Queue.element().indexOf(".")).equals("setting")
						&& table_state[Integer.parseInt(priority_Queue.element().substring(priority_Queue.element().indexOf("0")+1))-1]==0)
						temp_Queue.add(priority_Queue.poll());
				else if(priority_Queue.element().substring(0, priority_Queue.element().indexOf(".")).equals("setting")
						&& table_state[Integer.parseInt(priority_Queue.element().substring(priority_Queue.element().indexOf("0")+1))-1]==1)
						
						{
							str = priority_Queue.poll();
							priorityQueue();
							repriorityQueue();
							break;
						}
				else
					temp_Queue.add(priority_Queue.poll());
			}
			else 
			{
				if(!temp_Queue.isEmpty())
					repriorityQueue();
				return null;
			}
		}
		return str;
	}
	
	private static String zeroDishQueue()
	{
		String str=null;
		while(true)
		{
			if(!Queue.isEmpty())
			{
				if(Queue.element().substring(0, Queue.element().indexOf(".")).equals("serving"))
					temp_Queue.add(Queue.poll());
				else if(Queue.element().substring(0, Queue.element().indexOf(".")).equals("setting")
						&& table_state[Integer.parseInt(Queue.element().substring(Queue.element().indexOf(".")+1))-1]==0)
						temp_Queue.add(Queue.poll());
				else if(Queue.element().substring(0, Queue.element().indexOf(".")).equals("setting")
						&& table_state[Integer.parseInt(Queue.element().substring(Queue.element().indexOf(".")+1))-1]==1)
						{
							str = Queue.poll();
							tempQueue();
							retempQueue();
							break;
						}
				else
					temp_Queue.add(priority_Queue.poll());
			}
			else 
			{
				if(!temp_Queue.isEmpty())
					retempQueue();
				return null;
			}
		}
		return str;
	}
	
	
	private static String zeroDishClean()
	{
		String str=null;
		if(clean_Queue.isEmpty())
			return null;
		else
			str=clean_Queue.poll();
		
		return str;
	}
	
}