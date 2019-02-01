/*******************************************************************************
 * Copyright (c) 2007, 2019 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ai.tts.sapi.engine;

import org.eclipse.actf.util.win32.MemoryUtil;

/**
 * COM wrapper of SPEVENT structure.
 * 
 * @see "Microsoft Speech API SPEVENT"
 */
public class SpEvent {

	private long address = MemoryUtil.GlobalAlloc(24);

	public long getAddress() {
		return address;
	}

	public short getEventId() {
		short[] eventId = new short[1];
		MemoryUtil.MoveMemory(eventId, address, 2);
		return eventId[0];
	}

	public short getLParamType() {
		short[] lParamType = new short[1];
		MemoryUtil.MoveMemory(lParamType, address + 2, 2);
		return lParamType[0];
	}

	public int getStreamNum() {
		int[] streamNum = new int[1];
		MemoryUtil.MoveMemory(streamNum, address + 4, 4);
		return streamNum[0];
	}

	public long getAudioStreamOffset() {
		int[] audioStreamOffset = new int[2];
		MemoryUtil.MoveMemory(audioStreamOffset, address + 8, 4 * 2);
		return audioStreamOffset[0]
				+ ((long) audioStreamOffset[1] << 32);
	}

	public int getWParam() {
		int[] wParam = new int[1];
		MemoryUtil.MoveMemory(wParam, address + 16, 4);
		return wParam[0];
	}

	public int getLParam() {
		int[] lParam = new int[1];
		MemoryUtil.MoveMemory(lParam, address + 20, 4);
		return lParam[0];
	}

	public void dispose() {
		MemoryUtil.GlobalFree(address);
	}
}
