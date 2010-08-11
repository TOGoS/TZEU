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
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int z;
		byte[] buf = new byte[1024];
		while( (z = wadStream.read(buf)) > 0 ) {
			baos.write(buf,0,z);
		}
		Blob wadBlob = new ByteArrayBlob( baos.toByteArray() );
		List lumps = new WADReader().readLumps(wadBlob);
		// TODO: test them lumps
	}
}
