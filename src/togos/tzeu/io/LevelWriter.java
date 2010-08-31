package togos.tzeu.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import togos.tzeu.Lump;
import togos.tzeu.level.Level;

public class LevelWriter
{
	protected Lump extractOrBlank( Level l, String name, List items, ItemListCodec ilc ) {
		Lump u = l.getLump(name);
		if( u != null ) return u;
		if( items != null && ilc != null ) {
			try {
				return new Lump(name,ilc.encodeItems(items));
			} catch( IOException e ) {
				throw new RuntimeException(e);
			}
		}
		return Lump.blankLump(name);
	}
	
	protected Lump extractOrBlank( Level l, String name ) {
		return extractOrBlank( l, name, null, null );
	}
	
	//// Level ////
	
	public List lumpify( Level l ) {
		List newLumps = new ArrayList();
		Lump u;
		
		newLumps.add(l.lumps.get(0));
		
		newLumps.add(extractOrBlank(l, "THINGS",   l.things,   HexenThingCodec.instance));
		newLumps.add(extractOrBlank(l, "LINEDEFS", l.linedefs, HexenLinedefCodec.instance));
		newLumps.add(extractOrBlank(l, "SIDEDEFS", l.sidedefs, DoomSidedefCodec.instance));
		newLumps.add(extractOrBlank(l, "VERTEXES", l.vertexes, DoomVertexCodec.instance));
		newLumps.add(extractOrBlank(l, "SEGS"     ));
		newLumps.add(extractOrBlank(l, "SSECTORS" ));
		newLumps.add(extractOrBlank(l, "NODES"    ));
		newLumps.add(extractOrBlank(l, "SECTORS",  l.vertexes, DoomSectorCodec.instance));
		newLumps.add(extractOrBlank(l, "REJECT"   ));
		newLumps.add(extractOrBlank(l, "BLOCKMAP" ));
		newLumps.add(extractOrBlank(l, "BEHAVIOR" ));
		newLumps.add(extractOrBlank(l, "SCRIPTS"  ));
		if( (u=l.getLump("ENDMAP")) != null ) newLumps.add(u);
		
		return newLumps;
	}
}
