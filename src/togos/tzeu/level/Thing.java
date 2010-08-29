package togos.tzeu.level;


public class Thing
{
	public int thingId;
	public float x, y, height;
	public int angle;
	public int doomEdNum;
	public int flags;
	public Special special = Special.ZERO;
	
	public boolean equals( Object o ) {
		if( o instanceof Thing ) {
			Thing ot = (Thing)o;
			return ot.thingId == thingId && ot.x == x && ot.y == y &&
				ot.height == height && ot.angle == angle &&
				ot.doomEdNum == doomEdNum && ot.flags == flags &&
				ot.special.equals(special);
		}
		return false;
	}
	
	public int hashCode() {
		return
			thingId ^ (int)x ^ (int)y ^ (int)height ^ doomEdNum ^ flags ^
			special.hashCode();
	}
}
