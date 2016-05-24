import java.net.*;
import java.io.*;
import java.util.*;
public class Client 
{
	Socket s=null;
	DataInputStream dataIStream;
	DataOutputStream dataOStream;
	ObjectOutputStream o;
	public Client(String filename,String rootNode,String msg)
	{
		try
		{
			int portNo = 4334;
			s = new Socket(rootNode, portNo);
			dataIStream = new DataInputStream( s.getInputStream());
			dataOStream =new DataOutputStream( s.getOutputStream());
			o=new ObjectOutputStream(s.getOutputStream());
			System.out.println("Haha");
			if(msg.equals("File send"))
				sendRequest(filename,rootNode);
		}
		catch (UnknownHostException e)
		{
			System.out.println("Sock:"+e.getMessage());
		}
		catch (EOFException e)
		{
			System.out.println("EOF:"+e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO:"+e.getMessage());
		}
		finally 
		{
			if(s!=null)
				try 
				{
					s.close();
				}
				catch (IOException e)
				{/*close failed*/}
		}
	}
	public Client(String filename,String leafNode,String msg,List<String> list)
	{
		try
		{
			int portNo = 3334;
			s = new Socket(leafNode, portNo);
			dataIStream = new DataInputStream( s.getInputStream());
			dataOStream =new DataOutputStream( s.getOutputStream());
			o=new ObjectOutputStream(s.getOutputStream());
			if(msg.equals("Searching"))
				leafSendRequest(filename,leafNode,list);
		}
		catch (UnknownHostException e)
		{
			System.out.println("Sock:"+e.getMessage());
		}
		catch (EOFException e)
		{
			System.out.println("EOF:"+e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO:"+e.getMessage());
		}
		finally 
		{
			if(s!=null)
				try 
				{
					s.close();
				}
				catch (IOException e)
				{/*close failed*/}
		}
	}
	public void sendRequest(String filename,String root)
	{
		try
		{
			System.out.println("Sending InformTION TO Server");
		dataOStream.writeUTF("File Check");
		
		dataOStream.writeUTF(filename);
		dataOStream.writeUTF(root);
		System.out.println(" InformTION Sent TO Server");
		boolean dataReceived = Boolean.parseBoolean(dataIStream.readUTF());
		
		if(!dataReceived)
		{
			System.out.println("File not present");
			transferFile(filename,root);
			System.out.println("File transfered");
		}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}	
	}
	public void leafSendRequest(String filename,String root,List<String> list)
	{
		try
		{
			dataOStream.writeUTF("Leaf File Check");
			dataOStream.writeUTF(filename);
			dataOStream.writeUTF(root);
			o.writeObject(list);
			boolean dataReceived = Boolean.parseBoolean(dataIStream.readUTF());
		
			if(dataReceived)
			{
				System.out.println("File present at:" + root);
			}
			
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}	
	}
	public void transferFile(String fileName,String root)
	{
		try
		{
			byte[] byteArray=new byte[(int)fileName.length()];
			BufferedInputStream br = new BufferedInputStream(new FileInputStream(fileName));
			br.read(byteArray, 0, byteArray.length);
			OutputStream o = s.getOutputStream();
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
	public static void receiveFile(Socket s,String file)
	{
		try
		{
			byte[] byteArray = new byte[1024];
			InputStream inp = s.getInputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(file);
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
}