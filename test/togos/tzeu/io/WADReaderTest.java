package togos.tzeu.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;
import togos.tzeu.Lump;

public class WADReaderTest extends TestCase
{
	static WADReader wr = new WADReader();
	
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
}
