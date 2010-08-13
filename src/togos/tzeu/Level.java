package togos.tzeu;

import java.util.Iterator;
import java.util.List;

public class Level
{
	List things;
	List linedefs;
	List sidedefs;
	List vertexes;
	List sectors;
	
	/** List of lumps defining this map, including the map
	 * header (MAPxx), SCRIPTS, and ENDMAP, if they exist */
	List lumps;
	
	public Lump getLump(String name) {
		for( Iterator i=lumps.iterator(); i.hasNext(); ) {
			Lump l = (Lump)i.next();
			if( name.equals(l.getName()) ) {
				return l;
			}
		}
		return null;
	}
}
