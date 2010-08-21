package togos.tzeu.io;

import junit.framework.TestCase;

public class ByteUtilTest extends TestCase
{
	public void testCompare() {
		byte[] d1 = new byte[]{ 0,1,2,3 };
		byte[] d2 = new byte[]{ 0,1,2,3 };
		byte[] d3 = new byte[]{ 0,1,2,3,4 };
		byte[] d4 = new byte[]{ 3,2,1,0 };

		assertTrue( ByteUtil.compare(d1,d2) ==  0 );
		assertTrue( ByteUtil.compare(d1,d3) == -1 );
		assertTrue( ByteUtil.compare(d1,d4) == -1 );

		assertTrue( ByteUtil.compare(d2,d1) ==  0 );
		assertTrue( ByteUtil.compare(d3,d1) ==  1 );
		assertTrue( ByteUtil.compare(d4,d1) ==  1 );
	}
}
