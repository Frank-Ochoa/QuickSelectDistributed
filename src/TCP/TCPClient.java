package TCP;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class TCPClient
{
	public static void main(String[] argv) throws Exception
	{

		Socket clientSocket = new Socket("10.100.41.82", 6789);

		// This will be the
		ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());


		ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());


		ArrayMessage clientWork = (ArrayMessage) inputStream.readObject();

		outputStream.writeObject(clientWork.getResults());


		while(true)
		{
			IMessage fromServer = (IMessage) inputStream.readObject();

			if(fromServer instanceof EndMessage)
			{
				return;
			}

			if(fromServer instanceof KeepHalfMessage)
			{
				// If 0, use left values, if 1 use right values
				clientWork.keepHalf(((KeepHalfMessage) fromServer).getValue());
			}

			if(fromServer instanceof ChoosePivotMessage)
			{
				if (clientWork.getHi() - clientWork.getLo() >= 0)
				{
					outputStream.writeObject(new SendPivotMessage(clientWork.chooseNewPivot()));
				}
				else
				{
					outputStream.writeObject(new NoPivotMessage());
				}
			}

			if(fromServer instanceof SendPivotMessage)
			{
				clientWork.setPivotValue(((SendPivotMessage) fromServer).getPivot());
				outputStream.writeObject(clientWork.getResults());
			}

		}

	}
}
