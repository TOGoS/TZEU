package togos.tzeu;

import java.io.UnsupportedEncodingException;

import togos.tzeu.io.ByteArrayBlob;

import junit.framework.TestCase;

public class LumpCompareTest extends TestCase
{
	protected Lump mkLump( String name, byte[] data ) {
		Lump l = new Lump();
		l.name = name;
		l.data = new ByteArrayBlob( data, 0, data.length );
		return l;
	}
	
	protected Lump mkLump( String name, String contents ) {
		try {
			return mkLump( name, contents.getBytes("ASCII") );
		} catch( UnsupportedEncodingException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public void testBlobsEqual() {
		int length = 1234;
		byte[] d1 = new byte[length];
		byte[] d2 = new byte[length];
		for( int i=0; i<length; ++i ) {
			d1[i] = d2[i] = (byte)(i&0xFF); 
		}
		Lump l1 = mkLump( "ABC", d1 );
		Lump l2 = mkLump( "ABC", d2 );
		assertEquals( l1, l2 );
	}

	public void testDifferentLengthBlobsNotEqual() {
		int length = 1234;
		byte[] d1 = new byte[length];
		byte[] d2 = new byte[length+2];
		for( int i=0; i<length; ++i ) {
			d1[i] = d2[i] = (byte)(i&0xFF);
		}
		Lump l1 = mkLump( "ABC", d1 );
		Lump l2 = mkLump( "ABC", d2 );
		assertFalse( l1.equals(l2) );
	}

	public void testDifferentNamedBlobsNotEqual() {
		int length = 1234;
		byte[] d1 = new byte[length];
		for( int i=0; i<length; ++i ) {
			d1[i] = (byte)(i&0xFF);
		}
		Lump l1 = mkLump( "ABC", d1 );
		Lump l2 = mkLump( "ARF", d1 );
		assertFalse( l1.equals(l2) );
	}

	public void testSameLengthBlobsNotEqual() {
		int length = 1234;
		byte[] d1 = new byte[length];
		byte[] d2 = new byte[length];
		for( int i=0; i<length; ++i ) {
			d1[i] = (byte)(i&0xFF);
			d2[i] = (byte)((i^0xFF)&0xFF);
		}
		Lump l1 = mkLump( "ABC", d1 );
		Lump l2 = mkLump( "ABC", d2 );
		assertFalse( l1.equals(l2) );
	}
}
