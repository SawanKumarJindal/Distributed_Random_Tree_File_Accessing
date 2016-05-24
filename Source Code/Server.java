import java.net.*;
import java.io.*;
import java.util.*;
public class Server
{
	public static void main (String args[]) 
	{
		try
		{
			int portNo = 4334;
			ServerSocket serverSocket = new ServerSocket(portNo);
			while(true)
			{
				Socket clientSocket = serverSocket.accept();
				Connection c = new Connection(clientSocket);
			}
		}
		catch(IOException e)
		{
			System.out.println("Listen :"+e.getMessage());
		}
	}
}

class Connection extends Thread 
{
	static String fileFoundServer="";
	DataInputStream inputStream;
	DataOutputStream outputStream;
	ObjectInputStream o;
	Socket clientSocket;
	static Map<String,Map<String,Integer>> hmap=new HashMap<String,Map<String,Integer>>();
	boolean filePresentVar;
	File f;
	public Connection (Socket clientSocket)
	{
		try 
		{
			this.clientSocket = clientSocket;
			System.out.println("Client Socket:"+clientSocket);
			inputStream = new DataInputStream( clientSocket.getInputStream());
			System.out.println("Client Socket:"+clientSocket);
			outputStream =new DataOutputStream( clientSocket.getOutputStream());
			System.out.println("Client Socket:"+clientSocket);
			o= new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Connection established");
			this.start();
		}
		catch(IOException e)
		{
			System.out.println("Connection:"+e.getMessage());
		}
	}
	public boolean processData(String dataReceived)
	{
		System.out.println("File URL:"+dataReceived);
		f = new File(dataReceived);
		return f.exists();
	} 
	public void transferFile(String fileName)
	{
		try
		{
			byte[] byteArray=new byte[(int)f.length()];
			BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
			br.read(byteArray, 0, byteArray.length);
			OutputStream o = clientSocket.getOutputStream();
			o.write(byteArray, 0, byteArray.length);
			o.flush();
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	public  void receiveFile(String file,String root)
	{
		try
		{
			byte[] byteArray = new byte[100000];
			InputStream inp = clientSocket.getInputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(root + "." +file);
			BufferedOutputStream br = new BufferedOutputStream(fileOutputStream);
			int readBytes = inp.read(byteArray, 0, byteArray.length);
			br.write(byteArray, 0, readBytes);
			br.close();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}	
	}
	public void run()
	{
		try
		{ 
			String defaultPath="/home/stu14/s4/sj6390/Desktop/";
			System.out.println("Defau;t path stored in server:"+defaultPath);
			String dataReceived = inputStream.readUTF();
			System.out.println("Data received:"+dataReceived);
			String filename,hostName;
			List<String> list;
			if(dataReceived.equals("File Check"))
			{
				filename=inputStream.readUTF();
				hostName=inputStream.readUTF();
				System.out.println("hostname:"+hostName);
				filePresentVar=processData(defaultPath+hostName+"."+filename);
				System.out.println("file Present or not:" + filePresentVar);
				outputStream.writeUTF(Boolean.toString(filePresentVar));
				if(!filePresentVar)
				{
					System.out.println("Fiule not presnet");
					receiveFile(filename,hostName);
					System.out.println("File received");
				}
				
			}
			else if(dataReceived.equals("Leaf File Check"))
			{
				try{
				filename=inputStream.readUTF();
				hostName=inputStream.readUTF();
				list=(List)o.readObject();
				filePresentVar=processData(defaultPath+hostName+"."+filename);
				outputStream.writeUTF(Boolean.toString(filePresentVar));
				if(!filePresentVar)
				{
					if(!hmap.containsKey(filename))
					{
						Map<String,Integer> map=new HashMap<String,Integer>();
						map.put(hostName,1);
						hmap.put(filename,map);
					}
					else
					{
						Map<String,Integer> map=hmap.get(filename);
						if(map.containsKey(hostName))
						{
							map.put(hostName,map.get(hostName)+1);
						}
						else
						{
							map.put(hostName,1);
						}
						hmap.put(filename,map);
					}
					String leafNode=list.get((int)(list.indexOf(hostName) - 1)/2);
					Client cl=new Client(filename,leafNode,"Searching",list);
				}
				else
				{
					fileFoundServer=hostName;
					System.out.println("Server which contains file:"+fileFoundServer);
				}
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
		catch(EOFException e) 
		{
			System.out.println("EOF:"+e.getMessage());
		}
		catch(IOException e) 
		{
			System.out.println("IO:"+e.getMessage());
		}
		finally
		{ 
			try
			{
				clientSocket.close();
			}
			catch (IOException e)
			{/*close failed*/}}
	}	
}