package QuickSelectRunnables;

import QuickSelectMessages.*;

import java.util.concurrent.BlockingQueue;

@SuppressWarnings("Duplicates") public class Client implements Runnable
{
	private BlockingQueue<IMessage> input;
	// Could make this of IMessage also and just pass back a message that only holds an array
	private BlockingQueue<Object> output;

	public Client(BlockingQueue<IMessage> input, BlockingQueue<Object> output)
	{
		this.input = input;
		this.output = output;
	}

	@Override public void run()
	{
		try
		{
			// First thing each Client will take is an ArrayMessage
			ArrayMessageTest clientWork = (ArrayMessageTest) input.take();
			output.put(clientWork.getResults());

			while (true)
			{
				IMessage message = input.take();

				// Test for what message is and do appropriate work

				if (message instanceof EndMessage)
				{
					return;
				}

				if (message instanceof KeepHalfMessage)
				{
					// If 0, use left values, if 1 use right values
					clientWork.keepHalf(((KeepHalfMessage) message).getValue());
				}

				if (message instanceof ChoosePivotMessage)
				{

					if (clientWork.getHi() - clientWork.getLo() >= 0)
					{
						output.put(new SendPivotMessage(clientWork.chooseNewPivot()));
					}
					else
					{
						output.put(new NoPivotMessage());
					}

				}

				if (message instanceof SendPivotMessage)
				{
					clientWork.setPivotValue(((SendPivotMessage) message).getPivot());
					output.put(clientWork.getResults());
				}

			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
