package togos.tzeu.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public abstract class RegularRecordBlob implements Blob
{
	List items;
	public RegularRecordBlob( List items ) {
		this.items = items;
	}
	
	protected abstract int getRecordLength();
	
	protected abstract void encodeRecord( int index, byte[] buf, int offset );
	
	public long getLength() {
		return getRecordLength()*items.size();
	}
	
	protected void cpy(byte[] src, byte[] dest, int destIndex, int destWindowStart, int destWindowEnd) {
		int srcIndex = 0;
		if( destIndex < destWindowStart ) {
			srcIndex += (destWindowStart - destIndex);
			destIndex += (destWindowStart - destIndex);
		}
		for( ; srcIndex < src.length && destIndex < destWindowEnd ; ++srcIndex, ++destIndex ) {
			dest[destIndex] = src[srcIndex];
		}
	}
	
	public int read( long blobOffset, byte[] dest, int destOffset, int length ) throws IOException {
		int rl = getRecordLength();
		int ri = ByteUtil.integer(blobOffset/rl);
		if( ri >= items.size() ) return 0;
		byte[] buf = new byte[rl];
		long blobEndOffset = blobOffset+length;
		if( blobEndOffset > rl*(long)items.size() ) blobEndOffset = rl*(long)items.size();
		long recordOffset;
		for( ; (recordOffset = ri*((long)rl)) < blobEndOffset; ++ri ) {
			encodeRecord(ri, buf, 0);
			cpy( buf, dest, (int)(destOffset+recordOffset-blobOffset), destOffset, destOffset+length );
		}
		return (int)(blobEndOffset - blobOffset);
	}
	
	public long writeTo( long blobOffset, OutputStream os, long length ) throws IOException {
		int rl = getRecordLength();
		int ri = ByteUtil.integer(blobOffset/rl);
		if( ri >= items.size() ) return 0;
		byte[] buf = new byte[rl];
		long blobEndOffset = blobOffset+length;
		if( blobEndOffset > rl*(long)items.size() ) blobEndOffset = rl*(long)items.size();
		long recordOffset = ri*((long)rl);
		int destOffset = (int)(recordOffset - blobOffset);
		for( ; recordOffset < blobEndOffset; ++ri, recordOffset = ri*((long)rl) ) {
			encodeRecord(ri, buf, 0);
			
			int srcOffset;
			int cpyLength;
			if( destOffset < 0 ) {
				srcOffset = -destOffset;
				cpyLength = rl - srcOffset;
			} else {
				srcOffset = 0;
				cpyLength = rl;
			}
			if( recordOffset + cpyLength > blobEndOffset ) {
				cpyLength = (int)(blobEndOffset-recordOffset);
			}
			os.write(buf, srcOffset, cpyLength );
			destOffset = 0;
		}
		return blobEndOffset - blobOffset;
	}
}
