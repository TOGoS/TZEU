package togos.tzeu;

import java.io.IOException;
import java.io.OutputStream;

public class SubBlob implements Blob
{
	Blob parent;
	long offset;
	long length;
	
	public SubBlob( Blob parent, long offset, long length ) {
		this.parent = parent;
		this.offset = offset;
		this.length = length;
	}
	
	public long getLength() throws IOException {
		return length;
	}

	public int read(long blobOffset, byte[] dest, int destOffset, int length)
		throws IOException
	{
		if( length + blobOffset > this.length ) length = ByteUtil.integer(this.length - blobOffset);
		if( length < 0 ) return 0;
		return parent.read(this.offset+blobOffset, dest, destOffset, length);
	}

	public long writeTo(long blobOffset, OutputStream os, long length) throws IOException {
		throw new RuntimeException("This not dfund");
	}
}
