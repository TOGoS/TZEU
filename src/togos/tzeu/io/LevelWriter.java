package togos.tzeu.io;

import java.util.ArrayList;
import java.util.List;

import togos.tzeu.Lump;
import togos.tzeu.level.Level;
import togos.tzeu.level.Linedef;
import togos.tzeu.level.Sidedef;
import togos.tzeu.level.Vertex;

public class LevelWriter
{
	public Lump lump( String name, Blob data ) {
		Lump l = new Lump();
		l.name = name;
		l.data = data;
		return l;
		
	}
	
	public Lump blankLump(String name) {
		return lump(name, new ByteArrayBlob(new byte[0]));
	}
	
	protected Lump extractOrBlank( Level l, String name ) {
		Lump u = l.getLump(name);
		if( u != null ) return u;
		return blankLump(name);
	}
	
	//// Linedefs ////
	
	public void encodeHexenLinedef( Linedef l, byte[] buf, int offset )
	{
		ByteUtil.encodeShort(l.vertex1Index, buf, offset+0);
		ByteUtil.encodeShort(l.vertex2Index, buf, offset+2);
		ByteUtil.encodeShort(l.flags | l.trigger, buf, offset+4);
		HexenSpecialCodec.instance.encodeSpecial(l.special, buf, 6);
		ByteUtil.encodeShort(l.sidedef1Index, buf, offset+12);
		ByteUtil.encodeShort(l.sidedef2Index, buf, offset+14);
	}
	
	class HexenLinedefBlob extends RegularRecordBlob {
		public HexenLinedefBlob( List items ) {
			super(items);
		}
		protected void encodeRecord( int index, byte[] buf, int offset) {
			Linedef l = (Linedef)items.get(index);
			encodeHexenLinedef( l, buf, 0 );
		}
		protected int getRecordLength() {
			return 16;
		}
	}
	
	public Blob hexenLinedefBlob( List linedefs ) {
		return new HexenLinedefBlob( linedefs );
	}
	
	//// Sidedef ////
	
	public void encodeSidedef( Sidedef s, byte[] buf, int offset )
	{
		ByteUtil.encodeShort(s.xOffset, buf, offset+0);
		ByteUtil.encodeShort(s.yOffset, buf, offset+2);
		ByteUtil.padString(s.upperTexture, buf, offset+4, 8);
		ByteUtil.padString(s.lowerTexture, buf, offset+12, 8);
		ByteUtil.padString(s.normalTexture, buf, offset+20, 8);
		ByteUtil.encodeShort(s.sectorIndex, buf, offset+28);
	}
	
	class SidedefBlob extends RegularRecordBlob {
		public SidedefBlob( List items ) {
			super(items);
		}
		protected void encodeRecord( int index, byte[] buf, int offset) {
			Sidedef l = (Sidedef)items.get(index);
			encodeSidedef( l, buf, 0 );
		}
		protected int getRecordLength() {
			return 30;
		}
	}
	
	public Blob sidedefBlob( List sidedefs ) {
		return new SidedefBlob( sidedefs );
	}
	
	//// Vertex ////
	
	public void encodeVertex( Vertex v, byte[] buf, int offset )
	{
		ByteUtil.encodeShort((short)v.getX(), buf, offset+0);
		ByteUtil.encodeShort((short)v.getY(), buf, offset+2);
	}
	
	class VertexBlob extends RegularRecordBlob {
		public VertexBlob( List items ) {
			super(items);
		}
		protected void encodeRecord( int index, byte[] buf, int offset) {
			Vertex l = (Vertex)items.get(index);
			encodeVertex( l, buf, 0 );
		}
		protected int getRecordLength() {
			return 4;
		}
	}
	
	public Blob vertexBlob( List vertexes ) {
		return new VertexBlob( vertexes ); 
	}
	
	//// Level ////
	
	public List lumpify( Level l ) {
		List newLumps = new ArrayList();
		Lump u;
		
		newLumps.add(l.lumps.get(0));
		
		newLumps.add(extractOrBlank(l, "THINGS"));
		
		if( l.linedefs != null ) newLumps.add( lump("LINEDEFS",hexenLinedefBlob(l.linedefs)) );
		else newLumps.add(extractOrBlank(l, "LINEDEFS"));
		
		if( l.sidedefs != null ) newLumps.add( lump("SIDEDEFS",sidedefBlob(l.sidedefs)) );
		else newLumps.add(extractOrBlank(l, "SIDEDEFS"));
		
		newLumps.add(extractOrBlank(l, "VERTEXES"));
		newLumps.add(extractOrBlank(l, "SEGS"));
		newLumps.add(extractOrBlank(l, "SSECTORS"));
		newLumps.add(extractOrBlank(l, "NODES"));
		newLumps.add(extractOrBlank(l, "SECTORS"));
		newLumps.add(extractOrBlank(l, "REJECT"));
		newLumps.add(extractOrBlank(l, "BLOCKMAP"));
		newLumps.add(extractOrBlank(l, "BEHAVIOR"));
		newLumps.add(extractOrBlank(l, "SCRIPTS"));
		if( (u=l.getLump("ENDMAP")) != null ) newLumps.add(u);
		
		return newLumps;
	}
}
