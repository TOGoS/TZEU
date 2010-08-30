package togos.tzeu.io;

import togos.tzeu.level.Thing;

public class HexenThingCodec extends RegularRecordCodec
{
	public static HexenThingCodec instance = new HexenThingCodec();
	
	protected Object decodeRecord( byte[] src, int offset ) {
		Thing t = new Thing();
		t.thingId = ByteUtil.leShort( src, offset+0 );
		t.x      = ByteUtil.leShort( src, offset+2 );
		t.y      = ByteUtil.leShort( src, offset+4 );
		t.height  = ByteUtil.leShort( src, offset+6 );
		t.angle    = ByteUtil.leShort( src, offset+8 );
		t.doomEdNum = ByteUtil.leUShort( src, offset+10 );
		t.flags     = ByteUtil.leUShort( src, offset+12 );
		t.special  = HexenSpecialCodec.instance.decodeSpecial( src, offset+14 );
		return t;
	}

	protected void encodeRecord( Object rec, byte[] dest, int offset) {
		Thing t = (Thing)rec;
		ByteUtil.encodeShort(t.thingId,  dest, offset+0);
		ByteUtil.encodeShort((int)t.x,    dest, offset+2);
		ByteUtil.encodeShort((int)t.y,     dest, offset+4);
		ByteUtil.encodeShort((int)t.height, dest, offset+6);
		ByteUtil.encodeShort(t.angle,      dest, offset+8);
		ByteUtil.encodeShort(t.doomEdNum, dest, offset+10);
		ByteUtil.encodeShort(t.flags,    dest, offset+12);
		HexenSpecialCodec.instance.encodeSpecial(t.special, dest, 14);
	}

	protected String getItemDescription() {
		return "hexen-format thing";
	}

	protected int getRecordLength() {
		return 20;
	}

}
