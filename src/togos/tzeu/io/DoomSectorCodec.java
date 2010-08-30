package togos.tzeu.io;

import togos.tzeu.level.Sector;

public class DoomSectorCodec extends RegularRecordCodec
{
	public static DoomSectorCodec instance = new DoomSectorCodec();
	
	protected Object decodeRecord( byte[] src, int offset ) {
		Sector s = new Sector();
		s.floorHeight = ByteUtil.leShort( src, offset+0 );
		s.ceilingHeight = ByteUtil.leShort( src, offset+2 );
		s.floorTexture = ByteUtil.paddedString( src, 4, offset+8 );
		s.ceilingTexture = ByteUtil.paddedString( src, 12, offset+8 );
		s.lightLevel = ByteUtil.leShort( src, offset+20 );
		s.sectorSpecialNumber = ByteUtil.leUShort( src, offset+22 );
		s.sectorTag = ByteUtil.leUShort( src, offset+24 );
		return s;
	}

	protected void encodeRecord( Object rec, byte[] dest, int offset) {
		Sector s = (Sector)rec;
		ByteUtil.encodeShort( s.floorHeight, dest, offset+0 );
		ByteUtil.encodeShort( s.ceilingHeight, dest, offset+2 );
		ByteUtil.padString( s.floorTexture, dest, offset+4, 8 );
		ByteUtil.padString( s.ceilingTexture, dest, offset+12, 8 );
		ByteUtil.encodeShort( s.lightLevel, dest, offset+20 );
		ByteUtil.encodeShort( s.sectorSpecialNumber, dest, offset+22 );
		ByteUtil.encodeShort( s.sectorTag, dest, offset+24 );
	}

	protected String getItemDescription() {
		return "doom-format sector";
	}

	protected int getRecordLength() {
		return 26;
	}

}
