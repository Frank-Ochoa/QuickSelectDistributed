package QuickSelectMessages;

import java.io.Serializable;

public class ChoosePivotMessage implements IMessage, Serializable
{
	// Required for serizlizability
	public static long serialVersionUID = 3L;

	public ChoosePivotMessage()
	{
		super();
	}

}
