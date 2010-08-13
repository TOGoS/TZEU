package togos.tzeu;

import junit.framework.TestCase;

public class SidedefCompressorTest extends TestCase
{
	public void testCompressLevel() {
		Level l = WADReaderTest.getLevel();
		SidedefCompressor sc = new SidedefCompressor();
		
		Level cl = sc.compress(l);
		assertNotSame( l, cl );
		assertNotSame( l.linedefs, cl.linedefs );
		assertNotSame( l.sidedefs, cl.sidedefs );
		assertEquals( l.linedefs.size(), cl.linedefs.size() );
		assertTrue( cl.sidedefs.size() < l.sidedefs.size() );
	}
}
