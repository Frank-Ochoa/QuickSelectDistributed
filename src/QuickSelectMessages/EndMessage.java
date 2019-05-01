package QuickSelectMessages;

import java.io.Serializable;

public class EndMessage implements IMessage, Serializable
{
	// Required for serizlizability
	public static long serialVersionUID = 4L;

	public EndMessage()
	{
		super();
	}

}
