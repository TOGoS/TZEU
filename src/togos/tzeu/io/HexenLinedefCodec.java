package togos.tzeu.io;

import togos.tzeu.level.Linedef;

public class HexenLinedefCodec extends RegularRecordCodec
{
	public static HexenLinedefCodec instance = new HexenLinedefCodec();
	
	protected Object decodeRecord( byte[] src, int offset ) {
		Linedef l = new Linedef();
		l.vertex1Index  = ByteUtil.leUShort( src,  offset+0 );
		l.vertex2Index  = ByteUtil.leUShort( src,  offset+2 );
		l.flags         = ByteUtil.leUShort( src,  offset+4 ) & Linedef.HEXEN_FLAG_MASK;
		l.trigger       = ByteUtil.leUShort( src,  offset+4 ) & Linedef.HEXEN_TRIGGER_MASK;
		l.special       = HexenSpecialCodec.instance.decodeSpecial( src, offset+6 );
		l.sidedef1Index = ByteUtil.leShortSidedefIndex( src, offset+12 );
		l.sidedef2Index = ByteUtil.leShortSidedefIndex( src, offset+14 );
		return l;	}

	protected void encodeRecord( Object rec, byte[] dest, int offset) {
		Linedef l = (Linedef)rec;
		ByteUtil.encodeShort(l.vertex1Index, dest, offset+0);
		ByteUtil.encodeShort(l.vertex2Index, dest, offset+2);
		ByteUtil.encodeShort(l.flags | l.trigger, dest, offset+4);
		HexenSpecialCodec.instance.encodeSpecial(l.special, dest, 6);
		ByteUtil.encodeShort(l.sidedef1Index, dest, offset+12);
		ByteUtil.encodeShort(l.sidedef2Index, dest, offset+14);
	}

	protected String getItemDescription() {
		return "hexen-format linedef";
	}

	protected int getRecordLength() {
		return 16;
	}
}
