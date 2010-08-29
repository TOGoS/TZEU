package togos.tzeu;

import junit.framework.TestCase;
import togos.tzeu.level.Special;

public class SpecialCompareTest extends TestCase
{
	Special s1,s2,s3;
	
	public void setUp() {
		s1 = new Special();
		s1.specialNumber = 123;
		s1.arg0 = 110;
		s1.arg1 = 167;
		s1.arg2 = 193;
		s1.arg3 =   0;
		s1.arg4 =   7;
		
		s2 = new Special();
		s2.specialNumber = 123;
		s2.arg0 = 110;
		s2.arg1 = 167;
		s2.arg2 = 193;
		s2.arg3 =   0;
		s2.arg4 =   7;
		
		s3 = new Special();
		s3.specialNumber = 123;
		s3.arg0 = 110;
		s3.arg1 = 167;
		s3.arg2 = 193;
		s3.arg3 =   0;
		s3.arg4 =   6;
	}
	
	public void testCompare0() {
		assertTrue( s1.equals(s1) );
		assertTrue( CompareUtil.areEqual(s1, s1) );
		assertTrue( CompareUtil.areEqual(null, null) );
		assertFalse( CompareUtil.areEqual(s1, null) );
		assertFalse( CompareUtil.areEqual(null, s1) );
	}
	
	public void testCompare1() {
		assertTrue( s1.equals(s2) );
		assertFalse( s1.equals(s3) );
	}
}
