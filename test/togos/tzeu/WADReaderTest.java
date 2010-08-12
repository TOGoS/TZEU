package togos.tzeu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

public class WADReaderTest extends TestCase
{
	public void testReadWad() throws IOException {
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
		Blob wadBlob = new ByteArrayBlob( baos.toByteArray() );
		List lumps = new WADReader().readLumps(wadBlob);
		String lumpNames = "";
		for( int i=0; i<lumps.size(); ++i ) {
			Lump l = (Lump)lumps.get(i);
			if( lumpNames.length() > 0 ) lumpNames += ",";
			lumpNames += l.getName() + "(" + l.getSize() + ")";
		}
		assertEquals( "MAP30(0),THINGS(300),LINEDEFS(6160),SIDEDEFS(14700),VERTEXES(1284),SEGS(6864)," +
				"SSECTORS(728),NODES(5068),SECTORS(1560),REJECT(0),BLOCKMAP(0)," +
				"BEHAVIOR(162),SCRIPTS(228),MAPINFO(1486),SNDINFO(823)", lumpNames );
	}
}
