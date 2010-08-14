package togos.tzeu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

public class WADReaderTest extends TestCase
{
	static WADReader wr = new WADReader();
	static LevelReader lr = new LevelReader();
	
	public static Blob getWad() {
		try {
			InputStream wadStream = WADReaderTest.class.getResourceAsStream("C30.wad");
			if( wadStream == null ) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int z;
			byte[] buf = new byte[1024];
			while( (z = wadStream.read(buf)) > 0 ) {
				baos.write(buf,0,z);
			}
			return new ByteArrayBlob( baos.toByteArray() );
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public static List getLumps() {
		try {
			return wr.readLumps(getWad());
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public static Level getLevel() {
		try {
			return lr.readLevel( getLumps(), "MAP30" );
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public void testReadWad() throws IOException {
		List lumps = getLumps();
		
		assertEquals( 15, lumps.size() );
		
		String lumpNames = "";
		for( int i=0; i<lumps.size(); ++i ) {
			Lump l = (Lump)lumps.get(i);
			if( lumpNames.length() > 0 ) lumpNames += ",";
			lumpNames += l.getName() + "(" + l.getLength() + ")";
		}
		assertEquals( "MAP30(0),THINGS(300),LINEDEFS(6160),SIDEDEFS(14700),VERTEXES(1284),SEGS(6864)," +
				"SSECTORS(728),NODES(5068),SECTORS(1560),REJECT(0),BLOCKMAP(0)," +
				"BEHAVIOR(162),SCRIPTS(228),MAPINFO(1486),SNDINFO(823)", lumpNames );
		
		look4scripts: {
			for( int i=0; i<lumps.size(); ++i ) {
				Lump l = (Lump)lumps.get(i);
				if( "SCRIPTS".equals(l.getName()) ) {
					String s = ByteUtil.string(l.getData());
					String[] lines = s.split("\n");
					assertEquals( 14, lines.length );
					assertEquals( "script 1 OPEN {", lines[4].trim() );
					break look4scripts;
				}
			}
			fail("SCRIPTS lump not found!?");
		}
	}
	
	/*** C30 contains *** 
	 *   321 vertexes
	 *   385 lines
	 *   490 sidedefs
	 *    60 sectors
	 *    15 things
	 *    
	 * Vertex 254 @ 288,-224
	 * Line 254:
	 *   vertexes 222 to 221
	 *   sidedef 1 = 325, sidedef 2 = 315
	 * Sidedef 98:
	 *   up = BROWN1, normal = -, lower = BROWN1,
	 *   xoffset = 91, yoffset = 64, sector = 11
	 */

	public void testReadLinedefs() throws IOException {
		List lumps = getLumps();
		
		Lump linedefLump = wr.findLump( lumps, "LINEDEFS", "MAP30" );
		assertNotNull( linedefLump );
		List linedefs = lr.readHexenLinedefs( linedefLump.getData() );
		assertEquals( 385, linedefs.size() );
		
		int eflags =
			Linedef.REPEAT_SPECIAL |
			Linedef.TWOSIDED |
			Linedef.DONTPEGTOP;
		
		Linedef testLinedef = (Linedef)linedefs.get(290);
		assertEquals( 239, testLinedef.vertex1Index );
		assertEquals( 242, testLinedef.vertex2Index );
		assertEquals( 206, testLinedef.special );
		assertEquals( eflags, testLinedef.flags );
		assertEquals( Linedef.SPAC_Use, testLinedef.trigger );
		assertEquals(   0, testLinedef.arg1 );
		assertEquals(  16, testLinedef.arg2 );
		assertEquals(  50, testLinedef.arg3 );
		assertEquals(   8, testLinedef.arg4 );
		assertEquals(   0, testLinedef.arg5 );
		assertEquals( 379, testLinedef.sidedef1Index );
		assertEquals( 373, testLinedef.sidedef2Index );
	}
	
	public void testReadSidedefs() throws IOException {
		Lump sidedefLump = wr.findLump( getLumps(), "SIDEDEFS", "MAP30" );
		assertNotNull( sidedefLump );
		List sidedefs = lr.readSidedefs( sidedefLump.getData() );
		assertEquals( 490, sidedefs.size() );
		
		Sidedef testSidedef = (Sidedef)sidedefs.get(98);
		assertEquals( 91, testSidedef.xOffset );
		assertEquals( 64, testSidedef.yOffset );
		assertEquals( 11, testSidedef.sectorIndex );
		assertEquals( "BROWN1", testSidedef.upperTexture );
		assertEquals( "BROWN1", testSidedef.lowerTexture );
		assertEquals( "-",      testSidedef.normalTexture );
	}
	
	public void testReadLevelLumps() {
		List mapLumps = lr.readLevelLumps( getLumps(), "MAP30" );
		assertEquals( 13, mapLumps.size() );
	}

	public void testReadLevel() {
		Level l = getLevel();
		assertEquals( 385, l.linedefs.size() );
		assertEquals( 490, l.sidedefs.size() );
	}
}
