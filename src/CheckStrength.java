/**
 * Classe que verifica a força de uma senha
 */
public class CheckStrength 
{
	/**
	 * Níveis de força da senha
	 */
	public enum LEVEL 
	{
		EASY, MEDIUM, STRONG, VERY_STRONG, EXTREMELY_STRONG
	}

	/**
	 * Tipos de caracateres
	 */
	enum CharType
	{
		NUM, SMALL_LETTER, CAPITAL_LETTER, OTHER_CHAR
	}
	
	/**
	 * Dicionário de senhas simples
	 */
	private final static String[] DICTIONARY = 
	{ 
		"password", 
		"abc123", 
		"iloveyou", 
		"adobe123", 
		"123123", 
		"sunshine", 
		"1314520", 
		"a1b2c3", 
		"123qwe", 
		"aaa111", 
		"qweasd", 
		"admin", 
		"passwd" 
	};

	/**
	 * Verifica o tipo de um caractere
	 */
	private static CharType checkCharacterType(char c) 
	{
		if (c >= 48 && c <= 57) 
		{
			return CharType.NUM;
		}
		
		if (c >= 65 && c <= 90) 
		{
			return CharType.CAPITAL_LETTER;
		}
		
		if (c >= 97 && c <= 122) 
		{
			return CharType.SMALL_LETTER;
		}
		
		return CharType.OTHER_CHAR;
	}

	/**
	 * Conta o número de caracteres de um determinado tipo em uma senha
	 */
	private static int countLetter(String passwd, CharType type) 
	{
		int count = 0;
		
		if (null != passwd && passwd.length() > 0) 
		{
			for (char c : passwd.toCharArray()) 
			{
				if (checkCharacterType(c) == type) 
				{
					count++;
				}
			}
		}
		
		return count;
	}

	/**
	 * Verifica a força de uma senha
	 */
	public int checkPasswordStrength(String passwd) 
	{
		if (StringUtils.equalsNull(passwd)) 
		{
			throw new IllegalArgumentException("password is empty");
		}
		
		int len = passwd.length();
		int level = 0;
		
		int countNumeric = countLetter(passwd, CharType.NUM);
		int countSmall = countLetter(passwd, CharType.SMALL_LETTER); 
		int countCapital = countLetter(passwd, CharType.CAPITAL_LETTER); 
		int countSpecial = len - countNumeric - countSmall - countCapital;

		// adiciona pontos
		if (countNumeric > 0) 
		{
			level++;
		}
		
		if (countSmall > 0) 
		{
			level++;
		}
		
		if (len > 4 && countCapital > 0) 
		{
			level++;
		}
		
		if (len > 6 && countSpecial > 0) 
		{
			level++;
		}

		if (len > 4 && countNumeric > 0 && countSmall > 0
				|| countNumeric > 0 && countCapital > 0
				|| countNumeric > 0 && countSpecial > 0
				|| countSmall > 0 && countCapital > 0
				|| countSmall > 0 && countSpecial > 0
				|| countCapital > 0 && countSpecial > 0) 
		{
			level++;
		}

		if (len > 6 && countNumeric > 0 && countSmall > 0 && countCapital > 0 
				|| countNumeric > 0 && countSmall > 0 && countSpecial > 0
				|| countNumeric > 0 && countCapital > 0 && countSpecial > 0 
				|| countSmall > 0 && countCapital > 0 && countSpecial > 0) 
		{
			level++;
		}

		if (len > 8 && countNumeric > 0 && countSmall > 0 && countCapital > 0 && countSpecial > 0) 
		{
			level++;
		}

		if (len > 6 && countNumeric >= 3 && countSmall >= 3
				|| countNumeric >= 3 && countCapital >= 3
				|| countNumeric >= 3 && countSpecial >= 2
				|| countSmall >= 3 && countCapital >= 3
				|| countSmall >= 3 && countSpecial >= 2
				|| countCapital >= 3 && countSpecial >= 2) 
		{
			level++;
		}

		if (len > 8 && countNumeric >= 2 && countSmall >= 2 && countCapital >= 2 
				|| countNumeric >= 2 && countSmall >= 2 && countSpecial >= 2
				|| countNumeric >= 2 && countCapital >= 2 && countSpecial >= 2 
				|| countSmall >= 2 && countCapital >= 2 && countSpecial >= 2) 
		{
			level++;
		}

		if (len > 10 && countNumeric >= 2 && countSmall >= 2 && countCapital >= 2 && countSpecial >= 2) 
		{
			level++;
		}

		if (countSpecial >= 3) 
		{
			level++;
		}
		
		if (countSpecial >= 6) 
		{
			level++;
		}

		if (len > 12) 
		{
			level++;
			
			if (len >= 16) 
			{
				level++;
			}
		}

		// desconta pontos
		if ("abcdefghijklmnopqrstuvwxyz".indexOf(passwd) > 0 || "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(passwd) > 0) 
		{
			level--;
		}
		
		if ("qwertyuiop".indexOf(passwd) > 0 || "asdfghjkl".indexOf(passwd) > 0 || "zxcvbnm".indexOf(passwd) > 0) 
		{
			level--;
		}
		
		if (StringUtils.isNumeric(passwd) && ("01234567890".indexOf(passwd) > 0 || "09876543210".indexOf(passwd) > 0)) 
		{
			level--;
		}

		if (countNumeric == len || countSmall == len || countCapital == len) 
		{
			level--;
		}

		// aaabbb
		if (len % 2 == 0) 
		{ 
			String part1 = passwd.substring(0, len / 2);
			String part2 = passwd.substring(len / 2);
			
			if (part1.equals(part2)) 
			{
				level--;
			}
			
			if (StringUtils.isCharEqual(part1) && StringUtils.isCharEqual(part2)) 
			{
				level--;
			}
		}
		
		// ababab
		if (len % 3 == 0) 
		{ 
			String part1 = passwd.substring(0, len / 3);
			String part2 = passwd.substring(len / 3, len / 3 * 2);
			String part3 = passwd.substring(len / 3 * 2);

			if (part1.equals(part2) && part2.equals(part3)) 
			{
				level--;
			}
		}

		// 19881010 ou 881010
		if (StringUtils.isNumeric(passwd) && len >= 6) 
		{ 
			int year = 0;
			
			if (len == 8 || len == 6) 
			{
				year = Integer.parseInt(passwd.substring(0, len - 4));
			}
			
			int size = StringUtils.sizeOfInt(year);
			int month = Integer.parseInt(passwd.substring(size, size + 2));
			int day = Integer.parseInt(passwd.substring(size + 2, len));
			
			if (year >= 1950 && year < 2050 && month >= 1 && month <= 12 && day >= 1 && day <= 31) 
			{
				level--;
			}
		}

		// dicionario
		if (null != DICTIONARY && DICTIONARY.length > 0) 
		{
			for (int i = 0; i < DICTIONARY.length; i++) 
			{
				if (passwd.equals(DICTIONARY[i]) || DICTIONARY[i].indexOf(passwd) >= 0) 
				{
					level--;
					break;
				}
			}
		}

		if (len <= 6) 
		{
			level--;

			if (len <= 4) 
			{
				level--;
			
				if (len <= 3) 
				{
					level = 0;
				}
			}
		}

		if (StringUtils.isCharEqual(passwd)) 
		{
			level = 0;
		}

		if (level < 0) 
		{
			level = 0;
		}

		return level;
	}

	/**
	 * Retorna o nível de segurança de uma senha
	 */
	public LEVEL getPasswordLevel(String passwd) 
	{
		int level = checkPasswordStrength(passwd);
		
		if (level <= 3)
			return LEVEL.EASY;

		if (level <= 6)
			return LEVEL.MEDIUM;
			
		if (level <= 9)
			return LEVEL.STRONG;
			
		if (level <= 12)
			return LEVEL.VERY_STRONG;
			
		return LEVEL.EXTREMELY_STRONG;
	}
}