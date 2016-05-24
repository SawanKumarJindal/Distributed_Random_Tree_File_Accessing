import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
public class GladosFile
{
	static Map<String,List<String>> hmap= new HashMapStore().getHashMap();
	static List<String> list=new ListOfServers().getServersList();
	static List<String> newlist;
	public static List<String> makeList(String filename)
	{
		List<String> list1=new ArrayList<String>();
		Random r=new Random();
		while(list1.size() < 7)
		{
			int randomValue =r.nextInt(list.size());
			if(!list1.contains((list.get(randomValue))))
				list1.add(list.get(randomValue));
		}
		return list1;
	}
	public static String hashFunction(String filename,int i,int j)
	{
		
		if(hmap.containsKey(filename))
		{
			newlist=hmap.get(filename);
		}
		else
		{
			newlist=makeList(filename);
			hmap.put(filename,newlist);
		}
		System.out.println(newlist);
		if(i==0)
			return newlist.get(0);
		else if(i==1)
			return newlist.get(j+1);
		else
			return newlist.get(j+3);
	}
	public static void sendFileRoot(String root,String filename)
	{
		System.out.println("Root:"+root);
		Client cl=new Client(filename,root,"File send");
	}
	public static String randomLeafNode()
	{
		int min=3;
		int max=6;
		Random r=new Random();
		int randomValue =r.nextInt(max - min + 1) + min;
		return newlist.get(randomValue);
	}
	public static void searchFile(String leafNode,String filename)
	{
		Client c=new Client(filename,leafNode,"Searching",newlist);
	}
	public static void main(String[] args)
	{
		
		Scanner sc=new Scanner(System.in);
		List<String> newlist;
		char choice;
		do
		{
			System.out.println("Enter the file name:");
			String filename = sc.next();
			String root=hashFunction(filename,0,0);
			sendFileRoot("newyork.cs.rit.edu",filename);
			String leafNode=randomLeafNode();
			searchFile(leafNode,filename);
			System.out.println("Do you want to continue");
			choice=sc.next().charAt(0);
		}while(choice=='Y' || choice=='y');
	}
}