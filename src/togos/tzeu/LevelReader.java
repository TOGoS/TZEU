package togos.tzeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * For reading binary-format maps
 */
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
	
	//// Linedefs ////
	
	public Linedef readHexenLinedef( Blob b, int offset ) throws IOException {
		byte[] buf = new byte[16];
		b.read( offset, buf, 0, 16 );
		Linedef l = new Linedef();
		l.vertex1Index  = ByteUtil.leUShort( buf,  0 );
		l.vertex2Index  = ByteUtil.leUShort( buf,  2 );
		l.flags         = ByteUtil.leUShort( buf,  4 ) & Linedef.HEXEN_FLAG_MASK;
		l.trigger       = ByteUtil.leUShort( buf,  4 ) & Linedef.HEXEN_TRIGGER_MASK;
		l.special       = ByteUtil.leUByte(  buf,  6 );
		l.arg1          = ByteUtil.leUByte(  buf,  7 );
		l.arg2          = ByteUtil.leUByte(  buf,  8 );
		l.arg3          = ByteUtil.leUByte(  buf,  9 );
		l.arg4          = ByteUtil.leUByte(  buf, 10 );
		l.arg5          = ByteUtil.leUByte(  buf, 11 );
		l.sidedef1Index = ByteUtil.leShortSidedefIndex( buf, 12 );
		l.sidedef2Index = ByteUtil.leShortSidedefIndex( buf, 14 );
		return l;
	}
	
	public List readHexenLinedefs( Blob b ) throws IOException {
		int bloblen = ByteUtil.integer(b.getLength());
		if( bloblen % 16 != 0 ) {
			System.err.println("Warning: hexen linedef lump is not multiple of 16 bytes");
		}
		ArrayList linedefs = new ArrayList(bloblen/16);
		for( int i=0; i<bloblen; i+=16 ) {
			linedefs.add(readHexenLinedef(b, i));
		}
		return linedefs;
	}
	
	//// Sidedefs ////
	
	public Sidedef readSidedef( Blob b, int offset ) throws IOException {
		byte[] buf = new byte[30];
		b.read( offset, buf, 0, 30 );
		Sidedef s = new Sidedef();
		s.xOffset       = ByteUtil.leShort( buf, 0 );
		s.yOffset       = ByteUtil.leShort( buf, 2 );
		s.upperTexture  = ByteUtil.paddedString( buf,  4, 8 );
		s.lowerTexture  = ByteUtil.paddedString( buf, 12, 8 );
		s.normalTexture = ByteUtil.paddedString( buf, 20, 8 );
		s.sectorIndex   = ByteUtil.leUShort( buf, 28 );
		return s;
	}
	
	public List readSidedefs( Blob b ) throws IOException {
		int bloblen = ByteUtil.integer(b.getLength());
		if( bloblen % 30 != 0 ) {
			System.err.println("Warning: sidedef lump is not multiple of 30 bytes");
		}
		ArrayList sidedefs = new ArrayList(bloblen/30);
		for( int i=0; i<bloblen; i+=30 ) {
			sidedefs.add(readSidedef(b, i));
		}
		return sidedefs;
	}
	
	public Level readLevel( List levelLumps ) throws IOException {
		Level l = new Level();
		l.lumps = levelLumps;
		l.linedefs = readHexenLinedefs(l.getLump("LINEDEFS").getData());
		l.sidedefs = readSidedefs(l.getLump("SIDEDEFS").getData());
		return l;
	}

	public Level readLevel( List lumps, String levelName ) throws IOException {
		return readLevel( readLevelLumps(lumps, levelName) );
	}
	
	public Level readLevel( Blob wadBlob, String levelName ) throws IOException {
		WADReader wr = new WADReader();
		return readLevel( wr.readLumps(wadBlob), levelName );
	}
}
