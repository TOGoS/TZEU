package togos.tzeu.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import togos.tzeu.level.Linedef;
import togos.tzeu.level.Sector;
import togos.tzeu.level.Sidedef;
import togos.tzeu.level.Special;
import togos.tzeu.level.Thing;
import togos.tzeu.level.Vertex;

public abstract class LevelCodecTest extends TestCase
{
	Random r;
	public void setUp() {
		 r = new Random();
	}
	
	public void testCodec( ItemListCodec ilc, List items ) throws IOException {
		Blob encodedItems = ilc.encodeItems(items);
		List decodedItems = ilc.decodeItems(encodedItems);
		//assertEquals( items, decodedItems );
		
		assertEquals( items.size(), decodedItems.size() );
		for( int i=0; i<items.size(); ++i ) {
			if( !items.get(i).equals(decodedItems.get(i)) ) {
				assertEquals( items.get(i), decodedItems.get(i) );
			}
		}
		
	}
	
	protected Special randomSpecial() {
		if( r.nextBoolean() ) return Special.ZERO;
		
		Special s = new Special();
		s.specialNumber = r.nextInt(0x100);
		s.arg0 = r.nextInt(0x100);
		s.arg1 = r.nextInt(0x100);
		s.arg2 = r.nextInt(0x100);
		s.arg3 = r.nextInt(0x100);
		s.arg4 = r.nextInt(0x100);
		return s;
	}
	
	protected int randomShort() {
		return r.nextInt(0xfFFF)-0x8000;
	}
	
	protected int randomUShort() {
		return r.nextInt(0x10000);
	}
	
	//// Things ////
	
	protected Thing randomThing() {
		Thing t = new Thing();
		t.x = randomShort();
		t.y = randomShort();
		t.height = randomShort();
		t.flags = randomUShort();
		t.special = randomSpecial();
		return t;
	}
	protected List randomThings() {
		List thingList = new ArrayList();
		for( int i=0; i<500; ++i ) {
			thingList.add(randomThing());
		}
		return thingList;
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
	
	protected Linedef randomLinedef() {
		Linedef l = new Linedef();
		l.vertex1Index = randLinedefVertexIndex();
		l.vertex2Index = randLinedefVertexIndex();
		l.flags = randLinedefFlags();
		l.trigger = randLinedefTrigger();
		l.special = randomSpecial();
		l.sidedef1Index = randLinedefSidedefIndex();
		l.sidedef2Index = randLinedefSidedefIndex();
		return l;
	}
	
	protected List randomLinedefs() {
		List linedefList = new ArrayList();
		for( int i=0; i<500; ++i ) {
			linedefList.add(randomLinedef());
		}
		return linedefList;
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
	
	//// Vertexes ////
	
	protected Vertex randomVertex() {
		return new Vertex( r.nextInt(0xfFFF)-0x8000, r.nextInt(0xFFFF)-0x8000 );
	}

	protected List randomVertexes() {
		List vertexList = new ArrayList();
		for( int i=0; i<1000; ++i ) {
			vertexList.add(randomVertex());
		}
		return vertexList;
	}
	
	//// Sectors ////
	
	protected Sector randomSector() {
		Sector s = new Sector();
		s.floorHeight = randomShort();
		s.ceilingHeight = randomShort();
		s.floorTexture = randomTexture();
		s.ceilingTexture = randomTexture();
		s.lightLevel = r.nextInt(0x100);
		s.sectorSpecialNumber = randomUShort();
		s.sectorTag = randomUShort();
		return s;
	}
	
	protected List randomSectors() {
		List vertexList = new ArrayList();
		for( int i=0; i<1000; ++i ) {
			vertexList.add(randomSector());
		}
		return vertexList;
	}
}
