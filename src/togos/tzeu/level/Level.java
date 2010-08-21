package togos.tzeu.level;

import java.util.Iterator;
import java.util.List;

import togos.tzeu.Lump;

public class Level implements Cloneable
{
	public Level() { }
	
	public List things;
	public List linedefs;
	public List sidedefs;
	public List vertexes;
	public List sectors;
	
	/** List of lumps defining this map, including the map
	 * header (MAPxx), SCRIPTS, and ENDMAP, if they exist */
	public List lumps;
	
	public Lump getLump(String name) {
		for( Iterator i=lumps.iterator(); i.hasNext(); ) {
			Lump l = (Lump)i.next();
			if( name.equals(l.getName()) ) {
				return l;
			}
		}
		return null;
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch( CloneNotSupportedException e ) {
			throw new RuntimeException(e);
		}
	}
}
