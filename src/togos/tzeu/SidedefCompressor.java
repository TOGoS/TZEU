package togos.tzeu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class SidedefCompressor
{
	public class RemapResult {
		/** New (compressed) list of items */
		public List remappedItems;
		/** Maps old indexes to new */
		public int[] indexMap;
		
		public RemapResult( List items, int[] indexMap ) {
			this.remappedItems = items;
			this.indexMap = indexMap;
		}
	}
	
	HashSet compressableTextures = new HashSet();
	
	public SidedefCompressor() {
		compressableTextures.add("-");
		compressableTextures.add("STEP4");
		compressableTextures.add("STEP6");
		compressableTextures.add("BLACK");
		compressableTextures.add("CEIL5_2");
	}
	
	protected boolean sidedefIsCompressable( Sidedef s, int index ) {
		return
			compressableTextures.contains(s.lowerTexture) && 
			compressableTextures.contains(s.normalTexture) &&
			compressableTextures.contains(s.upperTexture);
	}
	
	public RemapResult preCompress( List sidedefs ) {
		ArrayList newItems = new ArrayList();
		int[] indexMap = new int[sidedefs.size()];
		HashMap newIndexes = new HashMap();
		for( int i=0; i<sidedefs.size(); ++i ) {
			Sidedef side = (Sidedef)sidedefs.get(i);
			Integer exiso;
			if( sidedefIsCompressable(side,i) && (exiso = (Integer)newIndexes.get(side)) != null ) {
				indexMap[i] = exiso.intValue();
			} else {
				newIndexes.put(side, new Integer(newItems.size()));
				indexMap[i] = newItems.size();
				newItems.add(side);
			}
		}
		return new RemapResult( newItems, indexMap );
	}
	
	protected int remapSidedefRef( int ref, int[] map ) {
		if( ref == -1 ) return ref;
		return map[ref];
	}
	
	public Linedef applyCompressionToLineDestructively( Linedef l, RemapResult cr ) {
		l.sidedef1Index = remapSidedefRef( l.sidedef1Index, cr.indexMap );
		l.sidedef2Index = remapSidedefRef( l.sidedef2Index, cr.indexMap );
		return l;
	}
	
	public List applyCompressionToLines( List linedefs, RemapResult cr ) {
		ArrayList newLines = new ArrayList(linedefs.size());
		for( Iterator i=linedefs.iterator(); i.hasNext(); ) {
			Linedef l = (Linedef)i.next();
			l = (Linedef)l.clone();
			newLines.add( applyCompressionToLineDestructively( l, cr ));
		}
		return newLines;
	}
	
	public Level compress( Level l ) {
		RemapResult cr = preCompress( l.sidedefs );
		l = (Level)l.clone();
		l.sidedefs = cr.remappedItems;
		l.linedefs = applyCompressionToLines(l.linedefs, cr);
		return l;
	}
}
