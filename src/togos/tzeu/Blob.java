package togos.tzeu;

import java.io.IOException;
import java.io.OutputStream;

public interface Blob
{
	public long getLength() throws IOException;
	public int read(long blobOffset, byte[] dest, int destOffset, int length) throws IOException;
	public void writeTo( OutputStream os ) throws IOException;
}
