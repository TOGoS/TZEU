package togos.tzeu.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import togos.tzeu.Lump;
import togos.tzeu.level.Level;

public class LevelReader
{
	static HashSet mapLumpNames = new HashSet();
	static {
		mapLumpNames.add("TEXTMAP");
		mapLumpNames.add("ENDMAP");
		mapLumpNames.add("THINGS");
		mapLumpNames.add("LINEDEFS");
		mapLumpNames.add("SIDEDEFS");
		mapLumpNames.add("VERTEXES");
		mapLumpNames.add("SEGS");
		mapLumpNames.add("SSECTORS");
		mapLumpNames.add("NODES");
		mapLumpNames.add("SECTORS");
		mapLumpNames.add("REJECT");
		mapLumpNames.add("BLOCKMAP");
		mapLumpNames.add("BEHAVIOR");
		mapLumpNames.add("SCRIPTS");
	}
	
	public List readLevelLumps( List lumps, String levelName ) {
		for( Iterator i=lumps.iterator(); i.hasNext(); ) {
			Lump l = (Lump)i.next();
			if( levelName.equals(l.getName()) ) {
				List levelLumps = new ArrayList();
				levelLumps.add(l);
				levelLumps: while( i.hasNext() && mapLumpNames.contains( (l = (Lump)i.next()).getName() ) ) {
					levelLumps.add(l);
					if( "ENDMAP".equals(l.getName()) ) {
						// Explicit end!  So quit
						break levelLumps;
					}
				}
				return levelLumps;
			}
		}
		return null;
	}
	
	//// Level ////
	
	public static final int PARSE_THINGS   = 0x001;
	public static final int PARSE_LINEDEFS = 0x002;
	public static final int PARSE_SIDEDEFS = 0x004;
	public static final int PARSE_VERTEXES = 0x008;
	public static final int PARSE_SECTORS  = 0x010;
	public static final int PARSE_ALL      = 0x01F;
	public static final int SAVE_UNPARSED_LUMPS = 0x100;
	public static final int COPY_LUMPS          = 0x200;
	
	public static Map lumpCodecs = new HashMap();
	public static Map lumpParseFlags = new HashMap();
	static {
		lumpCodecs.put("THINGS", HexenThingCodec.instance);
		lumpCodecs.put("LINEDEFS", HexenLinedefCodec.instance);
		lumpCodecs.put("SIDEDEFS", DoomSidedefCodec.instance);
		lumpCodecs.put("VERTEXES", DoomVertexCodec.instance);
		lumpCodecs.put("SECTORS", DoomSectorCodec.instance);
	}
	
	protected Blob memoryBlob( Blob b ) throws IOException {
		if( b instanceof ByteArrayBlob ) return b;
		byte[] buf = new byte[ByteUtil.integer(b.getLength())];
		b.read(0, buf, 0, buf.length);
		return new ByteArrayBlob( buf );
	}
	public Lump blankLump(String name) {
		return new Lump(name, new ByteArrayBlob(new byte[0]));
	}
	protected List parseItems( Level l, String lumpName, int parseFlag, int flags, List destLumps )
		throws IOException
	{
		List items = null;
		Lump lump = l.getLump(lumpName);
		ItemListCodec ilc = (ItemListCodec)lumpCodecs.get(lumpName);
		if( lump != null && ilc != null && (parseFlag&flags) != 0 ) {
			items = ilc.decodeItems( lump.getData() );
		}
		if( items == null && (flags&SAVE_UNPARSED_LUMPS) != 0 ) {
			if( lump == null ) lump = blankLump(lumpName);
			destLumps.add(lump);
		}
		return items;
	}
	
	protected void mbcplmp( Level l, String lumpName, int flags, List destLumps ) {
		Lump lump = l.getLump(lumpName);
		if( (flags&SAVE_UNPARSED_LUMPS) != 0 ) {
			if( lump == null ) lump = blankLump(lumpName);
			destLumps.add(lump);
		}
	}
	
	public Level readLevel( List levelLumps, int flags ) throws IOException {
		Level l = new Level();
		l.lumps = levelLumps;
		List savedLumps = new ArrayList();
		savedLumps.add( levelLumps.get(0) );
		l.things   = parseItems(l,"THINGS"  ,PARSE_THINGS  ,flags,savedLumps);
		l.linedefs = parseItems(l,"LINEDEFS",PARSE_LINEDEFS,flags,savedLumps);
		l.sidedefs = parseItems(l,"SIDEDEFS",PARSE_SIDEDEFS,flags,savedLumps);
		l.vertexes = parseItems(l,"VERTEXES",PARSE_VERTEXES,flags,savedLumps);
		mbcplmp(l,"SEGS",flags,savedLumps);
		mbcplmp(l,"SSECTORS",flags,savedLumps);
		mbcplmp(l,"NODES",flags,savedLumps);
		l.sectors  = parseItems(l,"SECTORS" ,PARSE_SECTORS ,flags,savedLumps);
		mbcplmp(l,"REJECT",flags,savedLumps);
		mbcplmp(l,"BLOCKMAP",flags,savedLumps);
		mbcplmp(l,"BEHAVIOR",flags,savedLumps);
		mbcplmp(l,"SCRIPTS",flags,savedLumps);
		l.lumps = savedLumps;
		return l;
	}

	public Level readLevel( List lumps, String levelName, int flags ) throws IOException {
		return readLevel( readLevelLumps(lumps, levelName), flags );
	}
	
	public Level readLevel( Blob wadBlob, String levelName, int flags ) throws IOException {
		WADReader wr = new WADReader();
		return readLevel( wr.readLumps(wadBlob), levelName, flags );
	}
}
