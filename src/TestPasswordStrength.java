import static org.junit.Assert.*;

import org.junit.Test;

public class TestPasswordStrength 
{
	@Test(expected=IllegalArgumentException.class)
	public void testBreakOnExplicitNull() 
	{
		CheckStrength checker = new CheckStrength();
		assertEquals(0, checker.checkPasswordStrength(null));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBreakOnEncodedNull() 
	{
		CheckStrength checker = new CheckStrength();
		assertEquals(0, checker.checkPasswordStrength("null"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBreakOnBlank() 
	{
		CheckStrength checker = new CheckStrength();
		assertEquals(0, checker.checkPasswordStrength(""));
	}

	@Test
	public void testSimplePasswordPoints() 
	{
		CheckStrength checker = new CheckStrength();
		assertEquals(0, checker.checkPasswordStrength("abc"));
		assertEquals(0, checker.checkPasswordStrength("ccccccccccccccccccc"));
		assertEquals(0, checker.checkPasswordStrength("abc1"));
		assertEquals(1, checker.checkPasswordStrength("Abc1"));
		assertEquals(4, checker.checkPasswordStrength("Abc1@"));
		assertEquals(7, checker.checkPasswordStrength("Abc1@#;"));
		assertEquals(7, checker.checkPasswordStrength("ABbc1#;"));
		assertEquals(5, checker.checkPasswordStrength("Ab123456"));
		assertEquals(11, checker.checkPasswordStrength("Bsi@CCet@Un1r10"));
		assertEquals(13, checker.checkPasswordStrength("Bsi@C#Cet@Un1r10"));
		assertEquals(14, checker.checkPasswordStrength("Bsi@C#Cet@Un1r10:-)"));
		
		// ç é letra minúscula, Ç é letra maiúscula
		assertEquals(0, checker.checkPasswordStrength("abç1"));
		assertEquals(1, checker.checkPasswordStrength("abÇ1"));
		assertEquals(2, checker.checkPasswordStrength("ab;1"));
		
		// mesma coisa para os caracteres acentuados
		assertEquals(0, checker.checkPasswordStrength("abá1"));
		assertEquals(1, checker.checkPasswordStrength("abÁ1"));
		assertEquals(2, checker.checkPasswordStrength("ab;1"));
	}
}