package QuickSelect;

import java.io.Serializable;

public class SendPivotMessage implements IMessage, Serializable
{
	// Required for serizlizability
	public static long serialVersionUID = 2L;

	public SendPivotMessage()
	{
		super();
	}

	private int pivot;

	public SendPivotMessage(int pivot)
	{
		this.pivot = pivot;
	}

	public int getPivot()
	{
		return pivot;
	}
}
