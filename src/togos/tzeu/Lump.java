package togos.tzeu;

import java.io.IOException;

public class Lump
{
	public String name;
	public Blob data;
	
	public String getName() {
		return name;
	}
	public long getLength() {
		try {
			return data.getLength();
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
	}
	public Blob getData() {
		return data;
	}
	
	public boolean equals( Object otherThing ) {
		if( otherThing instanceof Lump ) {
			Lump otherLump = (Lump)otherThing;
			if( !name.equals(otherLump.name) ) return false;
			if( getLength() != otherLump.getLength() ) return false;
			try {
				byte[] b1 = new byte[ByteUtil.integer(data.getLength())];
				byte[] b2 = new byte[ByteUtil.integer(data.getLength())];
				data.read(0, b1, 0, b1.length);
				otherLump.getData().read(0, b2, 0, b2.length);
				return ByteUtil.compare(b1, b2) == 0;
			} catch( IOException e ) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}
}
