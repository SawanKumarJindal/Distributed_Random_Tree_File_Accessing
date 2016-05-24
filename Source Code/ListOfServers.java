import java.util.List;
import java.util.ArrayList;
class ListOfServers
{
	private List<String> serverList;
	public ListOfServers()
	{
		serverList=new ArrayList<String>();
		serverList.add("maine.cs.rit.edu");
		serverList.add("yes.cs.rit.edu");
        serverList.add("newyork.cs.rit.edu");
		serverList.add("doors.cs.rit.edu");
		serverList.add("kinks.cs.rit.edu");
		serverList.add("joplin.cs.rit.edu");
		serverList.add("buddy.cs.rit.edu");
	}
	public List<String> getServersList()
	{
		return serverList;
	}
}	
	