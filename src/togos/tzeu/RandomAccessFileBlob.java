package togos.tzeu;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileBlob implements Blob
{
	RandomAccessFile raf;
	
	public RandomAccessFileBlob(RandomAccessFile raf) {
		this.raf = raf;
	}
	
	public long getLength() throws IOException {
		return raf.length();
	}
	
	public int read(long blobOffset, byte[] dest, int destOffset, int length) throws IOException {
		raf.seek(blobOffset);
		int read = 0;
		int z;
		while( (z = raf.read(dest, read+destOffset, length-read)) > 0 ) {
			read += z;
		}
		return read;
	}
	
	public void writeTo(OutputStream os) throws IOException {
		raf.seek(0);
		byte[] dest = new byte[1024];
		int read = 0;
		int z;
		while( (z = raf.read(dest, 0, dest.length)) > 0 ) {
			os.write(dest, 0, z);
			read += z;
		}
	}
}
