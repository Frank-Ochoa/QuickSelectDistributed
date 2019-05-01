package QuickSelectMessages;

import java.io.Serializable;

public class ArrayMessage implements IMessage, Serializable
{
	// Required for serizlizability
	public static long serialVersionUID = 1L;

	public ArrayMessage()
	{
		super();
	}

	private int[] array;
	private int pivotValue;
	private int lo;
	private int hi;
	private int pivotLocation;

	public ArrayMessage(int[] array, int pivotValue)
	{
		this.array = array;
		this.pivotValue = pivotValue;
		this.lo = 0;
		this.hi = array.length - 1;
	}


	public int[] getResults()
	{
		return partitionNoPI(lo, hi, pivotValue);
	}

	public void setPivotValue(int pivotValue)
	{
		this.pivotValue = pivotValue;
	}


	@SuppressWarnings("Duplicates") public int[] partitionNoPI(int left, int right, int pivotValue)
	{
		// Works similarly to the previous partition method, but does not use
		// pivot index (in case of no pivot value in array) and also accounts
		// for possibility of multiple of the partition. Uses a partition store
		// index at the far end to shift any instance of the partition value to
		// the end, and only partitions up to the partitions.

		int storeIndex = left;
		int pivotStoreIndex = right;
		int pivotInst = 0;

		System.out.println("pivot: " + pivotValue);
		for (int i = left; i <= pivotStoreIndex; i++)
		{

			if (array[i] < pivotValue)
			{
				swap(i, storeIndex, array);
				storeIndex++;
			}
			else if (array[i] == pivotValue)
			{
				swap(i, pivotStoreIndex, array);
				pivotStoreIndex--;
				pivotInst++;
				i--;
			}

		}

		this.pivotLocation = storeIndex + 1;
		// Return the left and the right
		return new int[] { storeIndex, array.length - storeIndex - pivotInst };
	}

	public void keepHalf(int half)
	{
		if(half == 0)
		{
			hi = pivotLocation;
		}
		else
		{
			lo = pivotLocation;
		}
	}

	public int getLo()
	{
		return lo;
	}

	public int getHi()
	{
		return hi;
	}

	public int chooseNewPivot()
	{
		return this.array[(hi - lo) / 2];
	}

	private static void swap(int first, int second, int[] array)
	{
		int temp = array[first];
		array[first] = array[second];
		array[second] = temp;
	}
}
