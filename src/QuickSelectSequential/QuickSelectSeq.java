package QuickSelectSequential;

import java.util.Arrays;

public class QuickSelectSeq
{
	public static int parition(int left, int right, int pivotIndex, int[] array)
	{
		int pivotValue = array[pivotIndex];

		swap(array, right, pivotIndex);

		int storeIndex = left;

		for(int i = left; i < right; i++)
		{
			if (array[i] < pivotValue)
			{
				swap(array, storeIndex, i);
				storeIndex++;
			}
		}

		swap(array, storeIndex, right);

		System.out.println(Arrays.toString(array));

		return storeIndex;
	}

	public static void swap(int[] array, int first, int second)
	{
		int temp = array[first];
		array[first] =  array[second];
		array[second] = temp;
	}

	public static int select(int[] array, int left, int right, int k)
	{
		if (left == right)
		{
			return array[left];
		}

		int pivotIndex = array.length / 2;

		pivotIndex = parition(left, right, pivotIndex, array);

		System.out.println(pivotIndex);


		if(k == pivotIndex)
		{
			return array[pivotIndex];
		}
		else if (k < pivotIndex)
		{
			return select(array, left, pivotIndex - 1, k);
		}
		else
		{
			return select(array, pivotIndex + 1, right, k);
		}

	}

	public static void main(String[] args)
	{
		int[] array = {3, 8, 9, 1, 22, 6, 100};

		System.out.println(select(array, 0, array.length - 1, 2));

	}
}
