package togos.tzeu.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import togos.tzeu.Lump;

public class WADReader
{
	public Lump readLumpFromDirectory( Blob wadBlob, int deOffset ) throws IOException {
		byte[] buf = new byte[16];
		wadBlob.read(deOffset, buf, 0, 16);
		
		Lump lump = new Lump();
		lump.data = new SubBlob(wadBlob, ByteUtil.leInteger(buf,0), ByteUtil.leInteger(buf,4));
		lump.name = ByteUtil.paddedString( buf, 8, 8 );
		return lump;
	}
	
	public List readLumps( Blob wadBlob ) throws IOException {
		int lumpCount = ByteUtil.leInteger( wadBlob, 4 );
		int directoryOffset = ByteUtil.leInteger( wadBlob, 8 );
		List lumps = new ArrayList(lumpCount);
		for( int i=0; i<lumpCount; ++i ) {
			lumps.add(readLumpFromDirectory(wadBlob, directoryOffset));
			directoryOffset += 16;
		}
		return lumps;
	}
	
	public Lump findLump( List lumps, String name, String afterName ) {
		for( Iterator i=lumps.iterator(); i.hasNext(); ) {
			Lump l = (Lump)i.next();
			if( afterName == null && name.equals(l.getName()) ) {
				return l;
			} else if( afterName != null && afterName.equals(l.getName()) ) {
				afterName = null;
			}
		}
		return null;
	}
}
