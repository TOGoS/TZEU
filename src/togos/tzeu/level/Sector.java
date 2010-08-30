package togos.tzeu.level;

public class Sector
{
	public int floorHeight;
	public int ceilingHeight;
	public String floorTexture = "-";
	public String ceilingTexture = "-";
	public int lightLevel;
	public int sectorSpecialNumber;
	public int sectorTag;
	
	public boolean equals( Object o ) {
		if( o instanceof Sector ) {
			Sector os = (Sector)o;
			return os.floorHeight == floorHeight &&
				os.ceilingHeight == ceilingHeight &&
				os.floorTexture.equals( floorTexture ) &&
				os.ceilingTexture.equals( ceilingTexture ) &&
				os.lightLevel == lightLevel &&
				os.sectorSpecialNumber == sectorSpecialNumber &&
				os.sectorTag == sectorTag;
		}
		return false;
	}
}
