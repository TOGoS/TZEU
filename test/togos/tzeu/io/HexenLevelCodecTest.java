package togos.tzeu.io;

import java.io.IOException;

public class HexenLevelCodecTest extends LevelCodecTest
{
	LevelWriter lw;
	LevelReader lr;
	public void setUp() {
		super.setUp();
		lw = new LevelWriter();
		lr = new LevelReader();
	}
	
	//// Things ////

	public void testHexenThingCodec() throws IOException {
		testCodec( HexenThingCodec.instance, randomThings() );
	}
	
	//// Linedefs ////

	public void testLinedefCodec() throws IOException {
		testCodec( HexenLinedefCodec.instance, randomLinedefs() );
	}
	
	//// Sidedefs ////
	
	public void testSidedefCodec() throws IOException {
		testCodec( DoomSidedefCodec.instance, randomSidedefs() );
	}
	
	//// Vertexes ////
	
	public void testVertexCodec() throws IOException {
		testCodec( DoomVertexCodec.instance, randomVertexes() );
	}
	
	//// Sectors ////

	public void testSectorCodec() throws IOException {
		testCodec( DoomSectorCodec.instance, randomSectors() );
	}
}
