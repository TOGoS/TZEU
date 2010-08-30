package togos.tzeu.io;

import java.io.IOException;
import java.util.List;

public interface ItemListCodec
{
	public Blob encodeItems( List items ) throws IOException;
	public List decodeItems( Blob b ) throws IOException;
}