package togos.tzeu.io;

import java.io.IOException;
import java.util.List;

public class HexenLevelCodecTest extends LevelCodecTest
{
	LevelWriter lw;
	LevelReader lr;
	public void setUp() {
		super.setUp();
		lw = new LevelWriter();
		lr = new LevelReader();
	}

	//// Linedefs ////
	
	protected Blob encodeLinedefs(List lines) throws IOException {
		return lw.hexenLinedefBlob(lines);
	}
	protected List decodeLinedefs(Blob blob) throws IOException {
		return lr.readHexenLinedefs(blob);
	}
	
	//// Sidedefs ////
	
	protected List decodeSidedefs(Blob blob) throws IOException {
		return lr.readSidedefs(blob);
	}
	protected Blob encodeSidedefs(List sidedefs) throws IOException {
		return lw.sidedefBlob(sidedefs);
	}
}
