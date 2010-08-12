package togos.tzeu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

public class WADReaderTest extends TestCase
{
	Blob wadBlob;
	WADReader wr;
	LevelReader lr;
	List lumps;
	
	public WADReaderTest() throws IOException {
		wr = new WADReader();
		lr = new LevelReader();
		
		InputStream wadStream = this.getClass().getResourceAsStream("C30.wad");
		if( wadStream == null ) {
			fail("Couldn't open C30.wad");
			return;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int z;
		byte[] buf = new byte[1024];
		while( (z = wadStream.read(buf)) > 0 ) {
			baos.write(buf,0,z);
		}
		wadBlob = new ByteArrayBlob( baos.toByteArray() );
		lumps = wr.readLumps(wadBlob);
	}
	
	public void testReadWad() throws IOException {
		assertEquals( 15, lumps.size() );
		
		String lumpNames = "";
		for( int i=0; i<lumps.size(); ++i ) {
			Lump l = (Lump)lumps.get(i);
			if( lumpNames.length() > 0 ) lumpNames += ",";
			lumpNames += l.getName() + "(" + l.getSize() + ")";
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
	 * Line 254 has sidedef 1 = 325, sidedef 2 = 315
	 * Sidedef 98:
	 *   up = BROWN1, normal = -, lower = BROWN1,
	 *   xoffset = 91, yoffset = 64, sector = 11
	 */

	public void testReadLinedefs() throws IOException {
		Lump linedefLump = wr.findLump( lumps, "LINEDEFS", "MAP30" );
		assertNotNull( linedefLump );
		List linedefs = lr.readHexenLinedefs( linedefLump.getData() );
		assertEquals( 385, linedefs.size() );
	}
	
	public void testReadSidedefs() throws IOException {
		Lump sidedefLump = wr.findLump( lumps, "SIDEDEFS", "MAP30" );
		assertNotNull( sidedefLump );
		List sidedefs = lr.readSidedefs( sidedefLump.getData() );
		assertEquals( 490, sidedefs.size() );
	}
}
