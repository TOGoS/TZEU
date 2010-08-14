package togos.tzeu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ByteUtil
{
	//// Encoding functions ////
	
	public static final void padString( String s, byte[] buf, int offset, int length ) {
		int i;
		for( i=0; i<length && i<s.length(); ++i ) {
			buf[offset+i] = (byte)s.charAt(i);
		}
		for( ; i<length; ++i ) {
			buf[offset+i] = 0;
		}
	}
	
	public static final void encodeInteger( int i, byte[] buf, int offset ) {
		buf[offset+0] = (byte)(i >>  0);
		buf[offset+1] = (byte)(i >>  8);
		buf[offset+2] = (byte)(i >> 16);
		buf[offset+3] = (byte)(i >> 24);
	}
	
	public static final void encodeShort( int i, byte[] buf, int offset ) {
		buf[offset+0] = (byte)(i >>  0);
		buf[offset+1] = (byte)(i >>  8);
	}

	public static final void encodeByte( int i, byte[] buf, int offset ) {
		buf[offset+0] = (byte)(i >>  0);
	}

	//// Decoding functions ////
	
	public static final String paddedString( byte[] buf, int offset, int length ) {
		for( int i=offset; i<offset+length; ++i ) {
			if( buf[i] == 0 ) {
				length = i-offset;
				break;
			}
		}
		try {
			return new String(buf,offset,length,"ASCII");
		} catch( UnsupportedEncodingException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public static final String string(byte[] arr, int offset, int length) {
		try {
			return new String(arr,offset,length,"ASCII");
		} catch( UnsupportedEncodingException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public static final String string(byte[] arr) {
		return string(arr,0,arr.length);
	}
	
	public static final String string(Blob b) {
		try {
			long len = b.getLength();
			if( len > Integer.MAX_VALUE ) throw new RuntimeException("Blob too big!");
			byte[] buf = new byte[(int)len];
			b.read(0, buf, 0, buf.length);
			return string(buf);
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public static final int integer(long l) {
		if( l < Integer.MIN_VALUE ) throw new RuntimeException("Long too large (negative) to cast to int: "+l);
		if( l > Integer.MAX_VALUE ) throw new RuntimeException("Long too large to cast to int: "+l);
		return (int)l;
	}
	
	public static final int leUByte( byte[] d, int offset ) {
		return d[offset+0]&0xFF;
	}
	
	public static final int leUShort( byte[] d, int offset ) {
		return
			((d[offset+0]&0xFF) <<  0) +
			((d[offset+1]&0xFF) <<  8);
	}

	public static final short leShort( byte[] d, int offset ) {
		return (short)(
			((d[offset+0]&0xFF) <<  0) +
			((d[offset+1]&0xFF) <<  8));
	}
	
	public static final int leInteger( byte[] d, int offset ) {
		return
			((d[offset+0]&0xFF) <<  0) +
			((d[offset+1]&0xFF) <<  8) +
			((d[offset+2]&0xFF) << 16) +
			((d[offset+3]&0xFF) << 24);
	}
	
	public static final int leInteger( Blob b, int offset ) throws IOException {
		byte[] data = new byte[4];
		b.read(offset, data, 0, 4);
		return leInteger(data,0);
	}
	
	/**
	 * Treats the number as an unsigned short, unless it's 0xFFFF, in which case it is treated as -1.
	 */
	public static final int leShortSidedefIndex( byte[] d, int offset ) {
		int num =
			((d[offset+0]&0xFF) <<  0) +
			((d[offset+1]&0xFF) <<  8);
		return (num == 0xFFFF) ? -1 : num;
	}
	
	public static int compare( byte[] arr1, byte[] arr2 ) {
		for( int i=0; i<arr1.length && i<arr2.length; ++i ) {
			if( arr1[i] > arr2[i] ) return 1;
			if( arr1[i] < arr2[i] ) return -1;
		}
		if( arr1.length > arr2.length ) return 1;
		if( arr2.length > arr1.length ) return -1;
		return 0;
	}
}
