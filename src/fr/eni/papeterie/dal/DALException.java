package fr.eni.papeterie.dal;

public class DALException extends Exception
{
	public DALException()
	{
		super();
	}

	public DALException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DALException(String message)
	{
		super(message);
	}
}
