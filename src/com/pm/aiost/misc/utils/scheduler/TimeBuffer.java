package com.pm.aiost.misc.utils.scheduler;

public class TimeBuffer {

	protected static final int DEFAULT_CAPACITY = 1024;

	protected int[] timeArray;
	protected Runnable[] taskArray;
	protected int size;

	public TimeBuffer() {
		this(DEFAULT_CAPACITY);
	}

	public TimeBuffer(int capacity) {
		timeArray = new int[capacity];
		taskArray = new Runnable[capacity];
		size = 0;
	}

	public void add(int time, Runnable task) {
		if (size == timeArray.length)
			ensureCapacity(size + 1);
		add(time, task, getIndex(time));
	}

	private void add(int time, Runnable task, int index) {
		if (size > 0 && index != size) {
			int lastIndex = index + 1;
			System.arraycopy(timeArray, index, timeArray, lastIndex, size - index);
			System.arraycopy(taskArray, index, taskArray, lastIndex, size - index);
		}

		timeArray[index] = time;
		taskArray[index] = task;
		size++;
	}

	private int getIndex(int time) {
		if (size == 0)
			return 0;
		if (time <= timeArray[0])
			return 0;
		if (time >= timeArray[size - 1])
			return size;
		return searchIndex(time);
	}

	private int searchIndex(int time) {
		for (int i = 0; i < size; i++) {
			if (timeArray[i] >= time)
				return i;
		}
		return 0;
	}

	public void update(int time) {
		if (size == 0)
			return;
		if (time <= timeArray[0])
			return;
		if (time >= timeArray[size - 1]) {
			callRunnables(size);
			this.size = 0;
			return;
		}
		updateIndex(searchIndex(time));
	}

	private void updateIndex(int index) {
		callRunnables(index);
		System.arraycopy(timeArray, index, timeArray, 0, size - index);
		System.arraycopy(taskArray, index, taskArray, 0, size - index);
		size -= index;
	}

	private void callRunnables(int index) {
		for (int i = 0; i < index; i++)
			taskArray[i].run();
	}

	public void clear() {
		this.size = 0;
	}

	protected void ensureCapacity(int minCapacity) {
		int length = timeArray.length;
		if (minCapacity > length)
			resize(Math.max(length * 2, minCapacity));
	}

	protected void resize(int size) {
		int[] newArray = new int[size];
		System.arraycopy(timeArray, 0, newArray, 0, this.size);
		timeArray = newArray;

		Runnable[] newArray2 = new Runnable[size];
		System.arraycopy(taskArray, 0, newArray2, 0, this.size);
		taskArray = newArray2;
	}

	public void trimToSize() {
		if (size != timeArray.length) {
			int[] newArray = new int[size];
			System.arraycopy(timeArray, 0, newArray, 0, size);
			timeArray = newArray;

			Runnable[] newArray2 = new Runnable[size];
			System.arraycopy(taskArray, 0, newArray2, 0, size);
			taskArray = newArray2;
		}
	}

	public int size() {
		return size;
	}
}