package togos.tzeu.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import togos.tzeu.Lump;
import togos.tzeu.io.Blob;
import togos.tzeu.io.LevelReader;
import togos.tzeu.io.WADReader;
import togos.tzeu.level.Level;
import togos.tzeu.level.Linedef;
import togos.tzeu.level.Sector;
import togos.tzeu.level.Sidedef;
import togos.tzeu.level.Thing;
import togos.tzeu.level.Vertex;

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
	 * Thing 5:
	 *   type = 4002 (Player 6 start)
	 *   facing = north (90)
	 *   x,y,z = 320,32,0
	 * Sector 43:
	 *   -40 to 104
	 *   STEP1, TLITE6_4
	 *   light = 160
	 *   tag = 2
	 *   special = 0
	 */
	
	public void testReadThings() throws IOException {
		Lump thingLump = wr.findLump( getLumps(), "THINGS", "MAP30" );
		List things = HexenThingCodec.instance.decodeItems(thingLump.getData());
		assertEquals( 15, things.size() );
		Thing t = (Thing)things.get(5);
		assertEquals( 4002, (int)t.doomEdNum );
		assertEquals( 320, (int)t.x );
		assertEquals( 32, (int)t.y );
		assertEquals( 0, (int)t.height );
		assertEquals( 90, t.angle );
		assertTrue( t.special.isZero() );
	}
	
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
		assertEquals( eflags, testLinedef.flags );
		assertEquals( Linedef.SPAC_Use, testLinedef.trigger );
		assertNotNull( testLinedef.special );
		assertEquals( 206, testLinedef.special.specialNumber );
		assertEquals(   0, testLinedef.special.arg0 );
		assertEquals(  16, testLinedef.special.arg1 );
		assertEquals(  50, testLinedef.special.arg2 );
		assertEquals(   8, testLinedef.special.arg3 );
		assertEquals(   0, testLinedef.special.arg4 );
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
	
	public void testReadVertexes() throws IOException {
		Lump vertexLump = wr.findLump( getLumps(), "VERTEXES", "MAP30" );
		List vertexes = lr.readVertexes(vertexLump.getData());
		assertEquals( 321, vertexes.size() );
		Vertex v = (Vertex)vertexes.get(254);
		assertEquals( 288, (int)v.getX() );
		assertEquals( -224, (int)v.getY() );
	}
	
	public void testReadSectors() throws IOException {
		Lump thingLump = wr.findLump( getLumps(), "SECTORS", "MAP30" );
		List sectors = DoomSectorCodec.instance.decodeItems(thingLump.getData());
		assertEquals( 60, sectors.size() );
		Sector s = (Sector)sectors.get(43);
		assertEquals( -40, s.floorHeight );
		assertEquals( 104, s.ceilingHeight );
		assertEquals( "STEP1", s.floorTexture );
		assertEquals( "TLITE6_4", s.ceilingTexture );
		assertEquals( 2, s.sectorTag );
		assertEquals( 160, s.lightLevel );
		assertEquals( 0, s.sectorSpecialNumber );
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
