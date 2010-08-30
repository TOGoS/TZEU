package togos.tzeu.io;

import togos.tzeu.level.Special;

public class HexenSpecialCodec
{
	public static HexenSpecialCodec instance = new HexenSpecialCodec();
	
	protected Special decodeSpecial( byte[] buf, int offset ) {
		Special s = new Special();
		s.specialNumber = ByteUtil.leUByte(  buf, offset+0 );
		s.arg0          = ByteUtil.leUByte(  buf, offset+1 );
		s.arg1          = ByteUtil.leUByte(  buf, offset+2 );
		s.arg2          = ByteUtil.leUByte(  buf, offset+3 );
		s.arg3          = ByteUtil.leUByte(  buf, offset+4 );
		s.arg4          = ByteUtil.leUByte(  buf, offset+5 );
		return Special.internIfZero(s);
	}
	
	protected void encodeSpecial( Special s, byte[] buf, int offset ) {
		if( s == null ) s = new Special(); 
		ByteUtil.encodeByte(s.specialNumber, buf, offset+0);
		ByteUtil.encodeByte(s.arg0, buf, offset+1);
		ByteUtil.encodeByte(s.arg1, buf, offset+2);
		ByteUtil.encodeByte(s.arg2, buf, offset+3);
		ByteUtil.encodeByte(s.arg3, buf, offset+4);
		ByteUtil.encodeByte(s.arg4, buf, offset+5);
	}
}
