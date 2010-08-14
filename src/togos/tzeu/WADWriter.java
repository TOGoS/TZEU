package togos.tzeu;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class WADWriter
{
	public void writeWad( String header, List lumps, OutputStream os ) throws IOException {
		byte[] buf = new byte[16];
		ByteUtil.padString(header, buf, 0, 4);
		ByteUtil.encodeInteger(lumps.size(), buf, 4);
		ByteUtil.encodeInteger(12, buf, 8);
		os.write( buf, 0, 12 );
		int lumpOffset = 12 + 16*lumps.size();
		for( Iterator i=lumps.iterator(); i.hasNext(); ) {
			Lump l = (Lump)i.next();
			ByteUtil.encodeInteger(lumpOffset, buf, 0);
			ByteUtil.encodeInteger(ByteUtil.integer(l.getLength()), buf, 4);
			ByteUtil.padString(l.getName(), buf, 8, 8);
			os.write( buf, 0, 16 );
			lumpOffset += l.getLength();
		}
		for( Iterator i=lumps.iterator(); i.hasNext(); ) {
			Lump l = (Lump)i.next();
			l.getData().writeTo(0, os, l.getLength());
		}
	}
}
