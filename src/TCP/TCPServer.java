package TCP;

import QuickSelectMessages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TCPServer
{
	private static int pivotValue;

	public static void sendOutHalfs(List<ObjectOutputStream> oStreams, int half) throws IOException
	{
		for (ObjectOutputStream stream : oStreams)
		{
			stream.writeObject(new KeepHalfMessage(half));
		}
	}

	public static void chooseNewAndRepart(List<ObjectOutputStream> oStreams, List<ObjectInputStream> iStreams,
			int numClients) throws IOException, ClassNotFoundException
	{
		for (int i = 0; i < numClients; i++)
		{
			oStreams.get(i).writeObject(new ChoosePivotMessage());
			IMessage message = (IMessage) iStreams.get(i).readObject();

			if (message instanceof SendPivotMessage)
			{
				pivotValue = ((SendPivotMessage) message).getPivot();

				// Redistribute out the pivot to each client
				for (ObjectOutputStream stream : oStreams)
				{
					stream.writeObject(message);
				}

				// Don't need to ask any more clients to choose a new pivot
				break;
			}

			// If, NoPivotMessage found, the loop will proceed to ask the next client
		}
	}

	public static void main(String[] argv) throws Exception
	{

		ServerSocket welcomeSocket = new ServerSocket(6789);
		int[] arrayToSort = { 3, 8, 9, 1, 22, 6, 100 };
		final int numClients = 2;
		//final int numComputers = numClients + 1;
		final int chunkSize = arrayToSort.length / numClients;
		pivotValue = arrayToSort[arrayToSort.length / 2];
		int k = 2;

		List<Socket> connectionSockets = new ArrayList<>();
		List<ObjectOutputStream> oStreams = new ArrayList<>();
		List<ObjectInputStream> iStreams = new ArrayList<>();
		for (int i = 0; i < numClients; i++)
		{
			// Get the sockets
			connectionSockets.add(welcomeSocket.accept());
			// Get each sockets respective Output and Input Stream
			oStreams.add(new ObjectOutputStream(connectionSockets.get(i).getOutputStream()));
			iStreams.add(new ObjectInputStream(connectionSockets.get(i).getInputStream()));
		}

		// Give them their initial sets of data to work with, sub-section of array as well as the pivotValue
		//ArrayMessage serverWork = null;
		for (int i = 1; i <= numClients; i++)
		{
			// Give the remainder, if the chunk size is not evenly distributed, could do this better but ehh
			if (i == numClients)
			{
				oStreams.get(i - 1).writeObject(
						new ArrayMessageTest(Arrays.copyOfRange(arrayToSort, (i - 1) * chunkSize, arrayToSort.length),
								pivotValue));
				break;
			}

			oStreams.get(i - 1).writeObject(
					new ArrayMessageTest(Arrays.copyOfRange(arrayToSort, (i - 1) * chunkSize, (i * chunkSize)),
							pivotValue));
		}

		while (true)
		{
			int left = 0;

			// Wait to here back from the clients, and collect their results
			for (int i = 0; i < numClients; i++)
			{
				int[] clientResult = (int[]) iStreams.get(i).readObject();
				left += clientResult[0];
			}

			// Run the select
			if (left == k)
			{
				System.out.println(arrayToSort[pivotValue]);
				for(ObjectOutputStream stream : oStreams)
				{
					stream.writeObject(new EndMessage());
				}
				return;
			}
			else if (left > k)
			{
				// Send Keep left half out the clients
				sendOutHalfs(oStreams, 0);

				// Choose new pivot and repartition around it
				chooseNewAndRepart(oStreams, iStreams, numClients);
			}
			else
			{
				k -= left + 1;
				// Send Keep right half out the clients
				sendOutHalfs(oStreams, 1);

				// Choose new pivot and repartition around it
				chooseNewAndRepart(oStreams, iStreams, numClients);
			}

		}
	}
}
