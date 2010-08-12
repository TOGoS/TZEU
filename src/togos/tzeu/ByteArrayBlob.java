package togos.tzeu;

import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayBlob implements Blob
{
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
		int l = (int)(blobOffset + length > this.length ? this.length - blobOffset : length);
		for( int i=0; i<l; ++i ) {
			dest[destOffset+i] = data[(int)blobOffset+i];
		}
		return l;
	}

	public void writeTo(OutputStream os) throws IOException {
		os.write(data, offset, length);
	}
}
