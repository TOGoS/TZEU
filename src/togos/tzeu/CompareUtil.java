package togos.tzeu;

public class CompareUtil
{
	public static boolean areEqual( Object o1, Object o2 ) {
		if( o1 == o2 ) return true;
		if( o1 == null || o2 == null ) return false;
		return o1.equals(o2);
	}
	
	public static int hashCode( Object o ) {
		if( o == null ) return 0;
		return o.hashCode();
	}
}
