package togos.tzeu;

public class Linedef
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
}
