package togos.tzeu;

import java.io.IOException;
import java.io.OutputStream;

public class SubBlob implements Blob
{
	long length;
	
	public SubBlob( Blob parent, long offset, long length ) {
		this.length = length;
	}
	
	public long getLength() throws IOException {
		return length;
	}

	public int read(long blobOffset, byte[] dest, int destOffset, int length)
			throws IOException {
		throw new RuntimeException("This not dfund");
	}

	public void writeTo(OutputStream os) throws IOException {
		throw new RuntimeException("This not dfund");
	}

}
