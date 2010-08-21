package togos.tzeu.io;

import java.io.IOException;
import java.util.List;

import togos.tzeu.level.Level;

import junit.framework.TestCase;

public class LevelWriterTest extends TestCase
{
	public void testWriteLinedefs() throws IOException {
		LevelWriter lw = new LevelWriter();
		LevelReader lr = new LevelReader();
		
		Level l = WADReaderTest.getLevel();
		
		List inLines = l.linedefs;
		Blob lineBlob = lw.hexenLinedefBlob(inLines);
		List outLines = lr.readHexenLinedefs(lineBlob);
		
		assertEquals( inLines.size(), outLines.size() );
		assertEquals( inLines, outLines );
	}

	public void testWriteSidedefs() throws IOException {
		LevelWriter lw = new LevelWriter();
		LevelReader lr = new LevelReader();
		
		Level l = WADReaderTest.getLevel();
		
		List inSides = l.sidedefs;
		Blob sideBlob = lw.sidedefBlob(inSides);
		List outSides = lr.readSidedefs(sideBlob);
		
		assertEquals( inSides.size(), outSides.size() );
		assertEquals( inSides, outSides );
	}
}
