package togos.tzeu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class RegularRecordBlobTest extends TestCase
{
	class TestRecordBlob extends RegularRecordBlob {
		public TestRecordBlob( List items ) {
			super(items);
		}
		protected int getRecordLength( ) {
			return 111;
		}
		protected void encodeRecord( int index, byte[] buf, int offset) {
			int q = ((Integer)items.get(index)).intValue();
			for( int i=0; i<getRecordLength(); ++i ) {
				buf[offset+i] = (byte)(i+(q*10));
			}
		}
	}
	
	public void testReadOnBoundary() throws IOException {
		List items = new ArrayList();
		items.add(new Integer(1)); items.add(new Integer(2)); items.add(new Integer(3));
		Blob tb = new TestRecordBlob(items);
		
		byte[] buf = new byte[222];
		tb.read(0, buf, 0, 222);
		assertEquals(   10, buf[  0] );
		assertEquals(  120, buf[110] );
		assertEquals(   20, buf[111] );
		assertEquals( -126, buf[221] );
	}

	public void testReadOffBoundary() throws IOException {
		List items = new ArrayList();
		items.add(new Integer(1)); items.add(new Integer(2)); items.add(new Integer(3));
		Blob tb = new TestRecordBlob(items);
		
		byte[] buf = new byte[222];
		tb.read(1, buf, 0, 222);
		assertEquals(   11, buf[  0] );
		assertEquals(  120, buf[109] );
		assertEquals(   20, buf[110] );
		assertEquals( -126, buf[220] );
	}

	public void testReadOffEnd() throws IOException {
		List items = new ArrayList();
		items.add(new Integer(1)); items.add(new Integer(2)); items.add(new Integer(3));
		Blob tb = new TestRecordBlob(items);
		
		byte[] buf = new byte[222];
		int readed = tb.read(223, buf, 0, 222);
		assertEquals(110, readed);
		assertEquals(   31, buf[  0] );
		assertEquals( -116, buf[109] );
		assertEquals(    0, buf[110] );
		assertEquals(    0, buf[220] );
	}
}
