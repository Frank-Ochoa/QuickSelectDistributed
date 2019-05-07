package QuickSelectParallel;

import QuickSelectMessages.IMessage;
import QuickSelectRunnables.Client;
import QuickSelectRunnables.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QuickSelectParallel
{
	public static void startThreads(Thread[] threads)
	{
		for (int i = 0; i < threads.length; i++)
		{
			threads[i].start();
		}
	}


	public static void main(String[] args)
	{
		int numClients = 3;
		int[] arrayToSort = {3, 8, 9, 1, 22, 6, 100};
		int k = 2;

		// Server Thread takes from Q1 and puts on respective client Queues
		BlockingQueue<Object> Q1 = new ArrayBlockingQueue<>(numClients);

		// Add as many Queues as there are clients
		List<BlockingQueue<IMessage>> outPutQueues = new ArrayList<>(numClients);
		for(int i = 0; i < numClients; i++)
		{
			outPutQueues.add(new ArrayBlockingQueue<>(1));
		}

		Thread[] threads = new Thread[numClients + 1];

		// Server Thread
		threads[0] = new Thread(new Server(Q1, outPutQueues, arrayToSort, numClients, k));

		// Client Threads, take from respective Queue, put on Q1
		for(int i = 1; i < threads.length; i++)
		{
			threads[i] = new Thread(new Client(outPutQueues.get(i - 1), Q1));
		}

		startThreads(threads);
	}
}
