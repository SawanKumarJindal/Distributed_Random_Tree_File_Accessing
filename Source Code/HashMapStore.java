import java.util.Map;
import java.util.List;
import java.util.HashMap;
class HashMapStore
{
	private Map<String,List<String>> hmap;
	public HashMapStore()
	{
		hmap=new HashMap<String,List<String>>();
	}
	public Map<String,List<String>> getHashMap()
	{
		return hmap;
	}
	public void setHashMap(Map<String,List<String>> hmap)
	{
		this.hmap=hmap;
	}
}	