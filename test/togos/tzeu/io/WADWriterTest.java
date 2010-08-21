package togos.tzeu.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import togos.tzeu.Lump;

import junit.framework.TestCase;

public class WADWriterTest extends TestCase
{
	protected Lump mkLump( String name, String contents ) {
		try {
			byte[] data = contents.getBytes("ASCII");
			Lump l = new Lump();
			l.name = name;
			l.data = new ByteArrayBlob( data, 0, data.length );
			return l;
		} catch( UnsupportedEncodingException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public void testWriteWad() throws IOException {
		List lumps = new ArrayList();
		lumps.add( mkLump("POOD","Oh my gosh I have poods in up in here!") );
		lumps.add( mkLump("DOOP","Loop, fr00t") );
		lumps.add( mkLump("UNWISE","bj0rk") );
		lumps.add( mkLump("MAP01","") );
		lumps.add( mkLump("TEXTMAP","linedef { }") );
		lumps.add( mkLump("ENDMAP","") );
		lumps.add( mkLump("MAPINFO","some junk goes here") );
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		WADWriter ww = new WADWriter();
		ww.writeWad("PWAD", lumps, baos);
		
		byte[] wadData = baos.toByteArray();
		Blob wadBlob = new ByteArrayBlob( wadData, 0, wadData.length );
		WADReader wr = new WADReader();
		List readedLumps = wr.readLumps(wadBlob);
		
		assertEquals( lumps, readedLumps );
	}
}
