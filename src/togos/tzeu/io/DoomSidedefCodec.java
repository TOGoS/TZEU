package togos.tzeu.io;

import togos.tzeu.level.Sidedef;

public class DoomSidedefCodec extends RegularRecordCodec
{
	public static DoomSidedefCodec instance = new DoomSidedefCodec();
	
	protected Object decodeRecord( byte[] src, int offset ) {
		Sidedef s = new Sidedef();
		s.xOffset       = ByteUtil.leShort( src, offset+0 );
		s.yOffset       = ByteUtil.leShort( src, offset+2 );
		s.upperTexture  = ByteUtil.paddedString( src, offset+ 4, 8 );
		s.lowerTexture  = ByteUtil.paddedString( src, offset+12, 8 );
		s.normalTexture = ByteUtil.paddedString( src, offset+20, 8 );
		s.sectorIndex   = ByteUtil.leUShort( src, offset+28 );
		return s;
	}

	protected void encodeRecord( Object rec, byte[] dest, int offset) {
		Sidedef s = (Sidedef)rec;
		ByteUtil.encodeShort(s.xOffset, dest, offset+0);
		ByteUtil.encodeShort(s.yOffset, dest, offset+2);
		ByteUtil.padString(s.upperTexture, dest, offset+4, 8);
		ByteUtil.padString(s.lowerTexture, dest, offset+12, 8);
		ByteUtil.padString(s.normalTexture, dest, offset+20, 8);
		ByteUtil.encodeShort(s.sectorIndex, dest, offset+28);
	}

	protected String getItemDescription() {
		return "doom-format sidedef";
	}

	protected int getRecordLength() {
		return 30;
	}

}
