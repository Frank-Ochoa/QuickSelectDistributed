package QuickSelectMessages;

import java.io.Serializable;
import java.util.Arrays;

public class ArrayMessage implements IMessage, Serializable
{
	// Required for serizlizability
	public static long serialVersionUID = 1L;
	private int[] array;
	private int pivotValue;
	private int lo;
	private int hi;
	private int pivotLocation;
	private int thingsLeft;
	private int thingsRight;



	public ArrayMessage()
	{
		super();
	}

	public ArrayMessage(int[] array, int pivotValue)
	{
		this.array = array;
		this.pivotValue = pivotValue;
		this.lo = 0;
		this.hi = array.length - 1;
		this.pivotLocation = 0;
	}

	public int[] getResults()
	{
		return partition(lo, hi);
	}

	public void setPivotValue(int pivotValue)
	{
		this.pivotValue = pivotValue;
	}

	@SuppressWarnings("Duplicates") public int[] partition(int lo, int hi)
	{
		// Scan the array looking for pivot
		// TODO: REPLACE THIS SCAN
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
			hi++;
		}

		System.err.println("LO: " + lo + " :: HI: " + (hi) + " :: PIVOT VALUE: " + pivotValue);
		System.err.println("ARRAY BEFORE PART: " + Arrays.toString(array));

		// Same logic as sequential
		swap(pivotLocation, hi, array);

		int storeIndex = lo;
		int thingsLeft = 0;

		for (int i = lo; i < hi; i++)
		{
			if (array[i] < pivotValue)
			{
				swap(storeIndex, i, array);
				storeIndex++;
				thingsLeft++;
			}
		}

		// Pivot to final place
		swap(storeIndex, hi, array);

		this.pivotLocation = storeIndex;

		System.err.println("ARRAY AFTER PART: " + Arrays.toString(array));
		System.err.println("THINGS LEFT: " + (thingsLeft));
		System.err.println("THINGS RIGHT: " + (array.length - thingsLeft - 1));


		this.thingsLeft = thingsLeft;
		// -1 for instance of pivot
		this.thingsRight = array.length - thingsLeft - 1;

		//System.exit(0);


		return new int[] {thingsLeft};
	}

	public void keepHalf(int half)
	{
		//TODO : STOP COPYING ARRAYS HERE, JUST USE LO AND HI

		if (half == 0)
		{
			// Go left, want array of size of thingsLeft, copy from 0 to pivotLocation - 1
			int[] newArray = new int[thingsLeft];
			System.arraycopy(array, 0, newArray, 0, pivotLocation);
			array = newArray;
			this.lo = 0;
			this.hi = array.length - 1;
		}
		else
		{
			// Go right, want array of size of thingsRight, copy from pivotLocation + 1, to array.length - 1
			int[] newArray = new int[thingsRight];
			System.arraycopy(array, pivotLocation + 1, newArray, 0, thingsRight);
			array = newArray;
			this.lo = 0;
			this.hi = array.length - 1;
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
