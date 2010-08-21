package togos.tzeu.level;

public class Linedef implements Cloneable
{
	/**
	 * These flags are chosen to be compatible
	 * with ZDoom/Hexen format maps.  Other flags
	 * will be given arbitrary >0xFFFF numbers.
	 */
	public static final int BLOCKING        = 0x0001;
	public static final int BLOCKMONSTERS   = 0x0002;
	public static final int TWOSIDED        = 0x0004;
	public static final int DONTPEGTOP      = 0x0008;
	public static final int DONTPEGBOTTOM   = 0x0010;
	public static final int SECRET          = 0x0020;
	public static final int SOUNDBLOCK      = 0x0040;
	public static final int DONTDRAW        = 0x0080;
	public static final int MAPPED          = 0x0100;
	public static final int REPEAT_SPECIAL  = 0x0200;

	// These will probably be changed
	// to be implemented the same as the same flags in ZDoom
	public static final int SPAC_Use   = 0x0400;
	public static final int SPAC_MCross = 0x0800;
	public static final int SPAC_Impact  = 0x0C00;
	public static final int SPAC_Push     = 0x1000;
	public static final int SPAC_PCross    = 0x1400;
	public static final int SPAC_UseThrough = 0x1800;
	
	public static final int MONSTERSCANACTIVATE = 0x2000;
	public static final int BLOCK_PLAYERS   = 0x4000;
	public static final int BLOCKEVERYTHING = 0x8000;
	
	// Mask to separate flags from trigger in hexen-formatted maps
	public static final int HEXEN_FLAG_MASK = 0xE3FF;
	public static final int HEXEN_TRIGGER_MASK = ~HEXEN_FLAG_MASK & 0xFFFF;
	
	public int vertex1Index;
	public int vertex2Index;
	public int flags;
	public int trigger;
	public int special=0;
	public int arg1=0,arg2=0,arg3=0,arg4=0,arg5=0;
	public int sidedef1Index=-1,sidedef2Index=-1;
	
	public Object clone() {
		try {
			return super.clone();
		} catch( CloneNotSupportedException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean equals( Object o ) {
		if( o instanceof Linedef ) {
			Linedef ol = (Linedef)o;
			return
				ol.vertex1Index == vertex1Index &&
				ol.vertex2Index == vertex2Index &&
				ol.flags == flags &&
				ol.trigger == trigger &&
				ol.special == special &&
				ol.arg1 == arg1 &&
				ol.arg2 == arg2 &&
				ol.arg3 == arg3 &&
				ol.arg4 == arg4 &&
				ol.arg5 == arg5 &&
				ol.sidedef1Index == sidedef1Index &&
				ol.sidedef2Index == sidedef2Index;
		}
		return false;
	}
	
	public int hashCode() {
		return
			vertex1Index ^ vertex2Index ^ flags ^ trigger ^
			special ^ arg1 ^ arg2 ^ arg3 ^ arg4 ^ arg5 ^
			sidedef1Index ^ sidedef2Index;
	}
}
