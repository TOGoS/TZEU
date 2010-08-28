package togos.tzeu.level;

import java.text.DecimalFormat;

public final class Vertex
{
	public final float x;
	public final float y;
	
	public Vertex( float x, float y ) {
		this.x = x;
		this.y = y;
	}
	
	protected final int getCx() {  return (int)(x*0x1000);  }
	protected final int getCy() {  return (int)(y*0x1000);  }
	
	public final int getIx() {  return (int)x;  }
	public final int getIy() {  return (int)y;  }

	public Object clone() {
		try {
			return super.clone();
		} catch( CloneNotSupportedException e ) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean equals( Object o ) {
		if( o instanceof Vertex ) {
			Vertex ov = (Vertex)o;
			return ov.getCx() == getCx() && ov.getCy() == getCy();
		}
		return false;
	}
	
	public int hashCode() {
		return getCx() ^ (getCy()<<16 | getCy()>>16);
	}
	
	protected static DecimalFormat TSFMT = new DecimalFormat("0.000");
	
	public String toString() {
		return TSFMT.format(x)+","+TSFMT.format(y);
	}
}
