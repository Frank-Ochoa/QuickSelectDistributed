package QuickSelectMessages;

import java.io.Serializable;
import java.util.Arrays;

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
	private int arrayLength;
	private int pivotLocation;


	public ArrayMessage(int[] array, int pivotValue)
	{
		this.array = array;
		this.pivotValue = pivotValue;
		this.lo = 0;
		this.hi = array.length - 1;
		this.arrayLength = array.length;

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

		System.out.println("LO: " + left + " :: HI: " + right + " :: PIVOT VALUE: " + pivotValue);
		System.out.println("ARRAY BEFORE PART: " + Arrays.toString(array));

		int storeIndex = left;
		int thingsLeft = 0;
		int pivotStoreIndex = right;
		int pivotInst = 0;

		for (int i = left; i <= pivotStoreIndex; i++)
		{
			if (array[i] < pivotValue)
			{
				swap(i, storeIndex, array);
				thingsLeft++;
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


		System.out.println("ARRAY AFTER PART: " + Arrays.toString(array));
		System.out.println("THINGS LEFT: " + thingsLeft);


		this.pivotLocation = thingsLeft + 1;

		// Return the left and the right
		return new int[] { thingsLeft, arrayLength - thingsLeft - pivotInst};
	}

	public void keepHalf(int half)
	{

		if(half == 0)
		{
			System.out.println("KEPT LEFT");
			hi = pivotLocation;
			System.out.println("NEW HI " + hi);
		}
		else
		{
			System.out.println("KEPT RIGHT");
			lo = pivotLocation;
			System.out.println("NEW LO " + lo);
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
		return this.array[lo];
	}

	private void swap(int first, int second, int[] array)
	{
		int temp = array[first];
		array[first] = array[second];
		array[second] = temp;
	}
}
