package QuickSelectRunnables;

import QuickSelectMessages.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@SuppressWarnings("Duplicates") public class Server implements Runnable
{
	private List<BlockingQueue<Object>> input;
	private List<BlockingQueue<IMessage>> output;
	private int[] arrayToSort;
	private int numClients;
	private int k;
	private int pivotValue;

	public Server(List<BlockingQueue<Object>> input, List<BlockingQueue<IMessage>> output, int[] arrayToSort, int numClients,
			int k)
	{
		this.input = input;
		this.output = output;
		this.arrayToSort = arrayToSort;
		this.numClients = numClients;
		this.k = k;
		this.pivotValue = arrayToSort[arrayToSort.length / 2];
	}

	private void sendOutHalfs(List<BlockingQueue<IMessage>> queues, int half)
	{
		for (BlockingQueue<IMessage> queue : queues)
		{
			try
			{
				queue.put(new KeepHalfMessage(half));
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void chooseNewAndRepart(List<BlockingQueue<IMessage>> queues, List<BlockingQueue<Object>> input, int numClients)
	{
		try
		{

			for (int i = 0; i < numClients; i++)
			{
				queues.get(i).put(new ChoosePivotMessage());
				IMessage message = (IMessage) input.get(i).take();


				if (message instanceof SendPivotMessage)
				{
					pivotValue = ((SendPivotMessage) message).getPivot();
					// Redistribute out the pivot to each client
					for (BlockingQueue<IMessage> queue : queues)
					{
						queue.put(message);
					}

					// Don't need to ask any more clients to choose a new pivot
					break;
				}

				// If, NoPivotMessage found, the loop will proceed to ask the next client
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override public void run()
	{
		final int chunkSize = arrayToSort.length / numClients;
		int left;
		int pivotInstance;

		try
		{

			for (int i = 1; i <= numClients; i++)
			{
				// Give the remainder, if the chunk size is not evenly distributed, could do this better but ehh for now
				if (i == numClients)
				{
					System.out.println(
							Arrays.toString(Arrays.copyOfRange(arrayToSort, (i - 1) * chunkSize, arrayToSort.length)));
					output.get(i - 1).put(new ArrayMessageTest(
							Arrays.copyOfRange(arrayToSort, (i - 1) * chunkSize, arrayToSort.length), pivotValue));
					break;
				}

				System.out.println(
						Arrays.toString(Arrays.copyOfRange(arrayToSort, (i - 1) * chunkSize, (i * chunkSize))));
				output.get(i - 1)
						.put(new ArrayMessageTest(Arrays.copyOfRange(arrayToSort, (i - 1) * chunkSize, (i * chunkSize)),
								pivotValue));
			}

			while (true)
			{
				left = 0;

				// Wait to here back from the clients, and collect their results
				for (int i = 0; i < numClients; i++)
				{
					int[] clientResult = (int[]) input.get(i).take();
					left += clientResult[0];
				}

				// Run the select
				if (left == k)
				{
					System.out.println(pivotValue);
					for (BlockingQueue<IMessage> queue : output)
					{
						queue.put(new EndMessage());
					}
					return;
				}
				else if (left > k)
				{
					// Send Keep left half out the clients
					sendOutHalfs(output, 0);

					// Choose new pivot and repartition around it
					chooseNewAndRepart(output, input, numClients);
				}
				else
				{
					this.k -= left + 1;
					System.out.println("WENT RIGHT, CHANGED K TO VALUE : " + k);
					// Send Keep right half out the clients
					sendOutHalfs(output, 1);

					// Choose new pivot and repartition around it
					chooseNewAndRepart(output, input, numClients);
				}

			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}
}
