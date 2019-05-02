package QuickSelectMessages;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ArrayMessageTest implements IMessage, Serializable
{
	// Required for serizlizability
	public static long serialVersionUID = 1L;

	public ArrayMessageTest()
	{
		super();
	}

	private int[] array;
	private int pivotValue;
	private int lo;
	private int hi;
	private int pivotLocation;
	private int thingsLeft;

	public ArrayMessageTest(int[] array, int pivotValue)
	{
		this.array = array;
		this.pivotValue = pivotValue;
		this.lo = 0;
		this.hi = array.length - 1;
		this.pivotLocation = 0;
	}


	public int[] getResults()
	{
		return partition(lo, hi, pivotValue);
	}

	public void setPivotValue(int pivotValue)
	{
		this.pivotValue = pivotValue;
	}


	@SuppressWarnings("Duplicates") public int[] partition(int left, int right, int pivotValue)
	{


		// Scan the array looking for pivot
		boolean pivotFound = false;
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] == pivotValue)
			{
				pivotFound = true;
				pivotLocation = i;
				break;
			}
		}

		// If no pivot was found, add it in :(
		if (!pivotFound)
		{
			int[] newArray = new int[array.length + 1];
			newArray[0] = pivotValue;
			System.arraycopy(array, 0, newArray, 1, newArray.length - 1);
			array = newArray;
			pivotLocation = 0;
		}


		System.err.println("LO: " + left + " :: HI: " + right + " :: PIVOT VALUE: " + pivotValue);
		System.err.println("ARRAY BEFORE PART: " + Arrays.toString(array));

		// Same logic as sequential
		swap(pivotLocation, right - 1, array);

		int storeIndex = left;
		int thingsLeft = 0;

		for (int i = left; i < right; i++)
		{
			if (array[i] < pivotValue)
			{
				swap(storeIndex, i, array);
				storeIndex++;
				thingsLeft++;
			}

		}

		// Pivot to final place
		swap(storeIndex, right - 1, array);

		pivotLocation = storeIndex;

		// Remove it
		if (!pivotFound)
		{
			array = IntStream.range(0, array.length).filter(i -> i != pivotLocation).map(i -> array[i]).toArray();
		}

		System.out.println("PIVOT INDEX " + pivotLocation);
		System.err.println("ARRAY AFTER PART: " + Arrays.toString(array));
		System.err.println("THINGS LEFT: " + (thingsLeft));


		return new int[] {pivotLocation, array.length - thingsLeft};
	}

	public void keepHalf(int half)
	{

		if(half == 0)
		{
			System.out.println("KEEP LEFT");
			hi = pivotLocation;
			System.out.println("NEW HI " + hi);
		}
		else
		{
			System.out.println("KEEP RIGHT");
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
