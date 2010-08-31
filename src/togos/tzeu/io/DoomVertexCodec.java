package togos.tzeu.io;

import togos.tzeu.level.Vertex;

public class DoomVertexCodec extends RegularRecordCodec
{
	public static DoomVertexCodec instance = new DoomVertexCodec();
	
	protected Object decodeRecord( byte[] src, int offset ) {
		return new Vertex( ByteUtil.leShort(src,offset+0), ByteUtil.leShort(src,offset+2) );
	}

	protected void encodeRecord( Object rec, byte[] dest, int offset) {
		Vertex v = (Vertex)rec;
		ByteUtil.encodeShort((short)v.getX(), dest, offset+0);
		ByteUtil.encodeShort((short)v.getY(), dest, offset+2);
	}

	protected String getItemDescription() {
		return "doom-format vertex";
	}

	protected int getRecordLength() {
		return 4;
	}

}
