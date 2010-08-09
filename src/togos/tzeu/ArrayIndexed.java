package togos.tzeu;

import java.util.ArrayList;

public class ArrayIndexed extends ArrayList implements ImmutableList
{
	public ImmutableList with( int idx, Object with ) {
		set( idx, with );
		return this;
	}
}
