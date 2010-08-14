package togos.tzeu;

import java.util.List;

public class LevelWriter
{
	public void encodeHexenLinedef( Linedef l, byte[] buf, int offset )
	{
		ByteUtil.encodeShort(l.vertex1Index, buf, offset+0);
		ByteUtil.encodeShort(l.vertex2Index, buf, offset+2);
		ByteUtil.encodeShort(l.flags | l.trigger, buf, offset+4);
		ByteUtil.encodeByte(l.special, buf, offset+6);
		ByteUtil.encodeByte(l.arg1, buf, offset+ 7);
		ByteUtil.encodeByte(l.arg2, buf, offset+ 8);
		ByteUtil.encodeByte(l.arg3, buf, offset+ 9);
		ByteUtil.encodeByte(l.arg4, buf, offset+10);
		ByteUtil.encodeByte(l.arg5, buf, offset+11);
		ByteUtil.encodeShort(l.sidedef1Index, buf, offset+12);
		ByteUtil.encodeShort(l.sidedef2Index, buf, offset+14);
	}
	
	public void encodeSidedef( Sidedef s, byte[] buf, int offset )
	{
		ByteUtil.encodeShort(s.xOffset, buf, offset+0);
		ByteUtil.encodeShort(s.yOffset, buf, offset+2);
		ByteUtil.padString(s.upperTexture, buf, offset+4, 8);
		ByteUtil.padString(s.lowerTexture, buf, offset+12, 8);
		ByteUtil.padString(s.normalTexture, buf, offset+20, 8);
		ByteUtil.encodeShort(s.sectorIndex, buf, offset+28);
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
	
	public Blob hexenLinedefBlob( List linedefs ) {
		return new HexenLinedefBlob( linedefs );
	}

	public Blob sidedefBlob( List sidedefs ) {
		return new SidedefBlob( sidedefs );
	}
}
