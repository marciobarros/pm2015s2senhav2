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
	 * Conta o número de caracteres numéricos
	 */
	private int countNumeric(String password) 
	{
		int count = 0;
		
		for (char c : password.toCharArray()) 
			if (Character.isDigit(c))
				count++;
		
		return count;
	}

	/**
	 * Conta o número de letras minúsculas
	 */
	private int countSmallLetter(String password) 
	{
		int count = 0;
		
		for (char c : password.toCharArray())
			if (Character.isLowerCase(c))
				count++;
		
		return count;
	}

	/**
	 * Conta o número de letras maiúsculas
	 */
	private int countCapitalLetter(String password) 
	{
		int count = 0;
		
		for (char c : password.toCharArray())
			if (Character.isUpperCase(c))
				count++;
		
		return count;
	}

	/**
	 * Verifica a força de uma senha
	 */
	public int checkPasswordStrength(String password) 
	{
		// Tratamento de senhas nulas
		if (StringUtils.equalsNull(password)) 
			throw new IllegalArgumentException("password is empty");

		// Conta o número de cada componente da senha
		int len = password.length();
		int countNumeric = countNumeric(password);
		int countSmall = countSmallLetter(password); 
		int countCapital = countCapitalLetter(password); 
		int countSpecial = len - countNumeric - countSmall - countCapital;

		int level = 0;

		// adiciona pontos
		if (countNumeric > 0) 
			level++;
		
		if (countSmall > 0) 
			level++;
		
		if (len > 4 && countCapital > 0) 
			level++;
		
		if (len > 6 && countSpecial > 0) 
			level++;

		int components = countComponents(countNumeric, countSmall, countCapital, countSpecial);
		
		if (len > 4 && components >= 2)
			level++;

		if (len > 6 && components >= 3)
			level++;

		if (len > 8 && components == 4) 
			level++;

		if (len > 6 && hasAtLeastTwoComplexComponents(countNumeric, countSmall, countCapital, countSpecial)) 
			level++;

		if (len > 8 && countComplexComponents(countNumeric, countSmall, countCapital, countSpecial) >= 3)
			level++;

		if (len > 10 && countComplexComponents(countNumeric, countSmall, countCapital, countSpecial) == 4) 
			level++;

		if (countSpecial >= 3) 
			level++;
		
		if (countSpecial >= 6) 
			level++;

		if (len > 12) 
			level++;
		
		if (len >= 16) 
			level++;

		// desconta pontos
		if ("abcdefghijklmnopqrstuvwxyz".indexOf(password) > 0 || "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(password) > 0) 
		{
			level--;
		}
		
		if ("qwertyuiop".indexOf(password) > 0 || "asdfghjkl".indexOf(password) > 0 || "zxcvbnm".indexOf(password) > 0) 
		{
			level--;
		}
		
		if (StringUtils.isNumeric(password) && ("01234567890".indexOf(password) > 0 || "09876543210".indexOf(password) > 0)) 
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
			String part1 = password.substring(0, len / 2);
			String part2 = password.substring(len / 2);
			
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
			String part1 = password.substring(0, len / 3);
			String part2 = password.substring(len / 3, len / 3 * 2);
			String part3 = password.substring(len / 3 * 2);

			if (part1.equals(part2) && part2.equals(part3)) 
			{
				level--;
			}
		}

		// 19881010 ou 881010
		if (checkPasswordIsDate(password))
			level--;

		// dicionario
		if (checkPasswordInDictionary(password))
			level--;

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

		if (StringUtils.isCharEqual(password)) 
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
	 * Conta o número de componentes distintos na senha
	 */
	private int countComponents(int countNumeric, int countSmall, int countCapital, int countSpecial) 
	{
		int componentCount = 0;
		
		if (countNumeric > 0)
			componentCount++;
		
		if (countSmall > 0)
			componentCount++;
		
		if (countCapital > 0)
			componentCount++;
		
		if (countSpecial > 0)
			componentCount++;
		
		return componentCount;
	}

	/**
	 * Verifica se a senha tem pelo menos dois componentes complexos
	 */
	private boolean hasAtLeastTwoComplexComponents(int countNumeric, int countSmall, int countCapital, int countSpecial)
	{
		if (countNumeric >= 3 && (countSmall >= 3 || countCapital >= 3 || countSpecial >= 2))
			return true;
		
		if (countSmall >= 3 && (countCapital >= 3 || countSpecial >= 2))
			return true;
		
		if (countCapital >= 3 && countSpecial >= 2)
			return true;
		
		return false;
	}
	
	/**
	 * Conta o número de componentes complexos de uma senha
	 */
	private int countComplexComponents(int countNumeric, int countSmall, int countCapital, int countSpecial)
	{
		int complexComponentCount = 0;
		
		if (countNumeric >= 2)
			complexComponentCount++;
		
		if (countSmall >= 2)
			complexComponentCount++;
		
		if (countCapital >= 2)
			complexComponentCount++;
		
		if (countSpecial >= 2)
			complexComponentCount++;
		
		return complexComponentCount;
	}

	/**
	 * Verifica se a senha é uma data
	 */
	private boolean checkPasswordIsDate(String password) 
	{
		int len = password.length();
		
		if (StringUtils.isNumeric(password) && len >= 6) 
		{ 
			int year = 0;
			
			if (len == 8 || len == 6) 
				year = Integer.parseInt(password.substring(0, len - 4));
			
			int size = StringUtils.sizeOfInt(year);
			int month = Integer.parseInt(password.substring(size, size + 2));
			int day = Integer.parseInt(password.substring(size + 2, len));
			
			if (year >= 1950 && year < 2050 && month >= 1 && month <= 12 && day >= 1 && day <= 31) 
				return true;
		}

		return false;
	}

	/**
	 * Verifica se a senha está no dicionário
	 */
	private boolean checkPasswordInDictionary(String password) 
	{
		if (DICTIONARY == null)
			return false;
		
		for (int i = 0; i < DICTIONARY.length; i++) 
			if (password.equals(DICTIONARY[i]) || DICTIONARY[i].indexOf(password) >= 0)
				return true;

		return false;
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