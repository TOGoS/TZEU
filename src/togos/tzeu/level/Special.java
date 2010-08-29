package togos.tzeu.level;

public class Special
{
	public static Special ZERO = new Special();
	
	public int specialNumber;
	public int arg0, arg1, arg2, arg3, arg4;
	
	public boolean equals(Object o) {
		if( o instanceof Special ) {
			Special os = (Special)o;
			return os.specialNumber == specialNumber &&
				os.arg0 == arg0 &&
				os.arg1 == arg1 &&
				os.arg2 == arg2 &&
				os.arg3 == arg3 &&
				os.arg4 == arg4;
		}
		return false;
	}
	
	public int hashCode() {
		return specialNumber ^ arg0 ^ arg1 ^ arg2 ^ arg3 ^ arg4;
	}
	
	public String toString() {
		return "special"+specialNumber+"("+arg0+","+arg1+","+arg2+","+arg3+","+arg4+")";
	}
	
	public boolean isZero() {
		return specialNumber == 0 && arg0 == 0 && arg1 == 0 &&
			arg2 == 0 && arg3 == 0 && arg4 == 0; 
	}
	
	public static Special internIfZero(Special s) {
		return s.isZero() ? ZERO : s;
	}
}
