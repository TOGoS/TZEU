package togos.tzeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WADReader
{
	protected int decodeInt32( byte[] d, int offset ) {
		return
			((d[offset+0]&0xFF) <<  0) +
			((d[offset+1]&0xFF) <<  8) +
			((d[offset+2]&0xFF) << 16) +
			((d[offset+3]&0xFF) << 24);
	}
	
	protected int readInt32( Blob b, int offset ) throws IOException {
		byte[] data = new byte[4];
		b.read(offset, data, 0, 4);
		return decodeInt32(data,0);
	}
	
	public Lump readLumpFromDirectory( Blob wadBlob, int deOffset ) throws IOException {
		byte[] buf = new byte[16];
		wadBlob.read(deOffset, buf, 0, 16);
		
		Lump lump = new Lump();
		lump.data = new SubBlob(wadBlob, decodeInt32(buf,0), decodeInt32(buf,4)); //not right!
		int nameLength = 8;
		for( int i=8; i<16; ++i ) {
			if( buf[i] == 0 ) {nameLength = i-8; break; }
		}
		lump.name = new String(buf,8,nameLength,"ASCII");
		return lump;
	}
	
	public List readLumps( Blob wadBlob ) throws IOException {
		int lumpCount = readInt32( wadBlob, 4 );
		int directoryOffset = readInt32( wadBlob, 8 );
		List lumps = new ArrayList(lumpCount);
		for( int i=0; i<lumpCount; ++i ) {
			lumps.add(readLumpFromDirectory(wadBlob, directoryOffset));
			directoryOffset += 16;
		}
		return lumps;
	}
}
