package togos.tzeu.io;

import java.io.IOException;
import java.io.OutputStream;


public class ByteArrayBlob implements Blob
{
	public static ByteArrayBlob EMPTY = new ByteArrayBlob(new byte[0]);
	
	byte[] data;
	int offset;
	int length;
	
	public ByteArrayBlob( byte[] data, int offset, int length ) {
		this.data = data;
		this.offset = offset;
		this.length = length;
	}
	public ByteArrayBlob( byte[] data ) {
		this( data, 0, data.length );
	}
	
	public long getLength() throws IOException {
		return length;
	}

	public int read(long blobOffset, byte[] dest, int destOffset, int length)
		throws IOException
	{
		if( blobOffset + length > this.length ) length = ByteUtil.integer(this.length - blobOffset);
		for( int i=0; i<length; ++i ) {
			dest[destOffset+i] = data[(int)blobOffset+i];
		}
		return length;
	}

	public long writeTo( long blobOffset, OutputStream os, long length ) throws IOException {
		if( blobOffset + length > this.length ) length = ByteUtil.integer(this.length - blobOffset);
		os.write(data, ByteUtil.integer(offset+blobOffset), ByteUtil.integer(length));
		return length;
	}
}
