package togos.tzeu;

import java.io.IOException;

public class Lump
{
	public String name;
	public Blob data;
	
	public String getName() {
		return name;
	}
	public long getSize() {
		try {
			return data.getLength();
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
	}
	public Blob getData() {
		return data;
	}
}
