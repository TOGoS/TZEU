package togos.tzeu;

import junit.framework.TestCase;
import togos.tzeu.io.LevelReaderTest;
import togos.tzeu.level.Level;

public class SidedefCompressorTest extends TestCase
{
	public void testCompressLevel() {
		Level l = LevelReaderTest.getLevel();
		SidedefCompressor sc = new SidedefCompressor();
		
		Level cl = sc.compress(l);
		assertNotSame( l, cl );
		assertNotSame( l.linedefs, cl.linedefs );
		assertNotSame( l.sidedefs, cl.sidedefs );
		assertEquals( l.linedefs.size(), cl.linedefs.size() );
		assertTrue( cl.sidedefs.size() < l.sidedefs.size() );
	}
}
