package QuickSelectMessages;

import java.io.Serializable;

public class KeepHalfMessage implements IMessage, Serializable
{
	// Required for serizlizability
	public static long serialVersionUID = 5L;

	public KeepHalfMessage()
	{
		super();
	}

	// 0 is left, 1 is right
	private int value;

	public KeepHalfMessage(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}
