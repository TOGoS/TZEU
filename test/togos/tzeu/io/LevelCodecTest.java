package togos.tzeu.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import togos.tzeu.level.Linedef;
import togos.tzeu.level.Sidedef;
import togos.tzeu.level.Vertex;

public abstract class LevelCodecTest extends TestCase
{
	Random r;
	public void setUp() {
		 r = new Random();
	}
	
	//// Linedefs ////
	
	protected int randLinedefVertexIndex() {
		return r.nextInt(0x10000);
	}
	protected int randLinedefSidedefIndex() {
		if( r.nextBoolean() ) {
			return -1;
		} else {
			return r.nextInt(0xFFFF);
		}
	}
	protected int randLinedefFlags() {
		return r.nextInt() & Linedef.HEXEN_FLAG_MASK; 
	}
	protected int randLinedefTrigger() {
		return r.nextInt() & Linedef.HEXEN_TRIGGER_MASK; 
	}
	protected int randLinedefSpecial() {
		return r.nextInt(0x100);
	}
	protected int randLinedefArg() {
		return r.nextInt(0x100);
	}
	
	protected Linedef randomLinedef() {
		Linedef l = new Linedef();
		l.vertex1Index = randLinedefVertexIndex();
		l.vertex2Index = randLinedefVertexIndex();
		l.special = randLinedefSpecial();
		l.flags = randLinedefFlags();
		l.trigger = randLinedefTrigger();
		l.arg1 = randLinedefArg();
		l.arg2 = randLinedefArg();
		l.arg3 = randLinedefArg();
		l.arg4 = randLinedefArg();
		l.arg5 = randLinedefArg();
		l.sidedef1Index = randLinedefSidedefIndex();
		l.sidedef2Index = randLinedefSidedefIndex();
		return l;
	}
	
	protected List randomLinedefs() {
		List linedefList = new ArrayList();
		for( int i=0; i<300; ++i ) {
			linedefList.add(randomLinedef());
		}
		return linedefList;
	}
	protected abstract Blob encodeLinedefs( List linedefs ) throws IOException;
	protected abstract List decodeLinedefs( Blob blob ) throws IOException;
	
	public void testLinedefCodec() throws IOException {
		List randomLinedefs = randomLinedefs();
		Blob linedefBlob = encodeLinedefs(randomLinedefs);
		List loadedLinedefs = decodeLinedefs(linedefBlob);
		assertEquals( randomLinedefs, loadedLinedefs );
	}
	
	//// Sidedefs ////
	
	protected int randomSidedefTextureOffset() {
		return r.nextInt(0x8000);
	}
	
	protected int randomSidedefSectorIndex() {
		return r.nextInt(0x1000);
	}
	
	static char[] rcs = new char[] {
		'_','A','B','C','D','E','F','G','H',
		'I','J','K','L','M','N','O','P','Q',
		'R','S','T','U','V','W','X','Y','Z',
		'0','1','2','3','4','5','6','7','8',
		'9' };
	
	protected char randomTextureChar() {
		return rcs[r.nextInt(rcs.length)];
	}
	
	protected String randomTexture() {
		if( r.nextBoolean() ) {
			return "-";
		} else {
			String s = "";
			for( int i=1+r.nextInt(7); i>0; --i ) {
				s += randomTextureChar();
			}
			return s;
		}
	}
	
	protected Sidedef randomSidedef() {
		Sidedef s = new Sidedef();
		s.xOffset = randomSidedefTextureOffset();
		s.yOffset = randomSidedefTextureOffset();
		s.lowerTexture = randomTexture();
		s.normalTexture = randomTexture();
		s.sectorIndex = randomSidedefSectorIndex();
		return s;
	}

	
	protected List randomSidedefs() {
		List sidedefList = new ArrayList();
		for( int i=0; i<300; ++i ) {
			sidedefList.add(randomSidedef());
		}
		return sidedefList;
	}
	protected abstract Blob encodeSidedefs( List sidedefs ) throws IOException;
	protected abstract List decodeSidedefs( Blob blob ) throws IOException;
	
	public void testSidedefCodec() throws IOException {
		List randomSidedefs = randomSidedefs();
		Blob SidedefBlob = encodeSidedefs(randomSidedefs);
		List loadedSidedefs = decodeSidedefs(SidedefBlob);
		assertEquals( randomSidedefs, loadedSidedefs );
	}
	
	//// Vertexes ////
	
	protected Vertex randomVertex() {
		return new Vertex( r.nextInt(0xfFFF)-0x8000, r.nextInt(0xFFFF)-0x8000 );
	}

	protected List randomVertexes() {
		List vertexList = new ArrayList();
		for( int i=0; i<300; ++i ) {
			vertexList.add(randomVertex());
		}
		return vertexList;
	}
	protected abstract Blob encodeVertexes( List sidedefs ) throws IOException;
	protected abstract List decodeVertexes( Blob blob ) throws IOException;
	
	public void testVertexCodec() throws IOException {
		List randomVertexes = randomVertexes();
		Blob VertexBlob = encodeVertexes(randomVertexes);
		List loadedVertexes = decodeVertexes(VertexBlob);
		assertEquals( randomVertexes, loadedVertexes );
	}
	
}
