package togos.tzeu.level;

public class Sidedef
{
	
	public int xOffset = 0;
	public int yOffset = 0;
	public String upperTexture = "-";
	public String normalTexture = "-";
	public String lowerTexture = "-";
	public int sectorIndex;
	
	public boolean equals( Object o ) {
		if( o instanceof Sidedef ) {
			Sidedef os = (Sidedef)o;
			return
				os.xOffset == xOffset &&
				os.yOffset == yOffset &&
				os.upperTexture.equals(upperTexture) &&
				os.normalTexture.equals(normalTexture) &&
				os.lowerTexture.equals(lowerTexture) &&
				os.sectorIndex == sectorIndex;
		}
		return false;
	}
	
	public int hashCode() {
		return
			xOffset ^ yOffset ^ sectorIndex ^
			upperTexture.hashCode() ^ lowerTexture.hashCode() ^ normalTexture.hashCode(); 
	}
}
