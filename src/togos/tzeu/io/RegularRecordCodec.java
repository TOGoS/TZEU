package togos.tzeu.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class RegularRecordCodec implements ItemListCodec
{
	protected abstract String getItemDescription();
	protected abstract int getRecordLength();
	protected abstract Object decodeRecord( byte[] src, int offset );
	protected abstract void encodeRecord( Object rec, byte[] dest, int offset );
	
	public Blob encodeItems( List items ) {
		return new RegularRecordBlob(items) {
			protected int getRecordLength() {
				return RegularRecordCodec.this.getRecordLength();
			}
			
			protected void encodeRecord(int index, byte[] buf, int offset) {
				Object o = this.items.get(index);
				RegularRecordCodec.this.encodeRecord( o, buf, offset );
			}
		};
	}
	
	public List decodeItems( Blob b ) throws IOException {
		int reclen = getRecordLength();
		int bloblen = ByteUtil.integer(b.getLength());
		if( bloblen % reclen != 0 ) {
			System.err.println("Warning: "+getItemDescription()+" blob is not multiple of "+reclen+" bytes");
		}
		ArrayList items = new ArrayList(bloblen/16);
		for( int i=0; i<bloblen; i+=reclen ) {
			byte[] buf = new byte[reclen];
			b.read(i, buf, 0, reclen);
			items.add(decodeRecord(buf, 0));
		}
		return items;
	}
}
