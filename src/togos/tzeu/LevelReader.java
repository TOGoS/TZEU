package togos.tzeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * For reading binary-format maps
 */
public class LevelReader
{
	//// Linedefs ////
	
	public Linedef readHexenLinedef( Blob b, int offset ) throws IOException {
		byte[] buf = new byte[16];
		b.read( offset, buf, 0, 16 );
		Linedef l = new Linedef();
		l.vertex1Index  = ByteUtil.leShort( buf,  0 );
		l.vertex2Index  = ByteUtil.leShort( buf,  2 );
		l.flags         = ByteUtil.leShort( buf,  4 );
		l.special       = buf[  6 ];
		l.arg1          = buf[  7 ];
		l.arg2          = buf[  8 ];
		l.arg3          = buf[  9 ];
		l.arg4          = buf[ 10 ];
		l.arg5          = buf[ 11 ];
		l.sidedef1Index = ByteUtil.leShort( buf, 12 );
		l.sidedef2Index = ByteUtil.leShort( buf, 14 );
		return l;
	}
	
	public List readHexenLinedefs( Blob b ) throws IOException {
		int bloblen = ByteUtil.integer(b.getLength());
		if( bloblen % 16 != 0 ) {
			System.err.println("Warning: hexen linedef lump is not multiple of 16 bytes");
		}
		ArrayList linedefs = new ArrayList(bloblen/16);
		for( int i=0; i<bloblen; i+=16 ) {
			linedefs.add(readHexenLinedef(b, i));
		}
		return linedefs;
	}
	
	//// Sidedefs ////
	
	public List readSidedefs( Blob b ) throws IOException {
		return Collections.EMPTY_LIST;
	}
}
