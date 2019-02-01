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

import org.eclipse.actf.ai.voice.IVoiceEventListener;
import org.eclipse.actf.util.win32.COMUtil;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.GUID;
import org.eclipse.swt.internal.ole.win32.IDispatch;
import org.eclipse.swt.ole.win32.OLE;

/**
 * COM wrapper of ISpNotifySource interface.
 * 
 * @see "Microsoft Speech API ISpNotifySource"
 */
public class ISpNotifySource extends IDispatch {

	public static final GUID IID = COMUtil
			.IIDFromString("{5EFF4AEF-8487-11D2-961C-00C04F8EE628}"); //$NON-NLS-1$

	public static final int SPEI_START_INPUT_STREAM = 1;
	public static final int SPEI_END_INPUT_STREAM = 2;
	public static final int SPEI_VOICE_CHANGE = 3;
	public static final int SPEI_TTS_BOOKMARK = 4;
	public static final int SPEI_WORD_BOUNDARY = 5;
	public static final int SPEI_PHONEME = 6;
	public static final int SPEI_SENTENCE_BOUNDARY = 7;
	public static final int SPEI_VISEME = 8;
	public static final int SPEI_TTS_AUDIO_LEVEL = 9;

	private long address;
	private Callback callback = null;
	private IVoiceEventListener eventListener = null;

	public ISpNotifySource(long address) {
		super(address);
		this.address = address;
		callback = new Callback(this, "SAPINotifyCallback", 2); //$NON-NLS-1$
		SetNotifyCallbackFunction(callback.getAddress(), 0, 0);
		SetInterest(1 << SPEI_TTS_BOOKMARK, 1 << SPEI_TTS_BOOKMARK);
	}

	public static ISpNotifySource getNotifySource(ISpVoice dispatch) {
		long[] ppv = new long[1];
		if (OLE.S_OK == dispatch.QueryInterface(IID, ppv)) {
			return new ISpNotifySource(ppv[0]);
		}
		return null;
	}

	public void setEventListener(IVoiceEventListener eventListener) {
		this.eventListener = eventListener;
	}

	long SAPINotifyCallback(long wParam, long lParam) {
		SpEvent se = new SpEvent();
		try {
			while (OLE.S_OK == GetEvent(se.getAddress())) {
				if (null != eventListener) {
					if (SPEI_TTS_BOOKMARK == se.getEventId()) {
						eventListener.indexReceived(se.getWParam());
					}
					continue;
				}
				String eventName;
				switch (se.getEventId()) {
				case SPEI_START_INPUT_STREAM:
					eventName = "SPEI_START_INPUT_STREAM"; //$NON-NLS-1$
					break;
				case SPEI_END_INPUT_STREAM:
					eventName = "SPEI_END_INPUT_STREAM"; //$NON-NLS-1$
					break;
				case SPEI_VOICE_CHANGE:
					eventName = "SPEI_VOICE_CHANGE"; //$NON-NLS-1$
					break;
				case SPEI_TTS_BOOKMARK:
					eventName = "SPEI_TTS_BOOKMARK"; //$NON-NLS-1$
					break;
				case SPEI_WORD_BOUNDARY:
					eventName = "SPEI_WORD_BOUNDARY"; //$NON-NLS-1$
					break;
				case SPEI_PHONEME:
					eventName = "SPEI_PHONEME"; //$NON-NLS-1$
					break;
				case SPEI_SENTENCE_BOUNDARY:
					eventName = "SPEI_SENTENCE_BOUNDARY"; //$NON-NLS-1$
					break;
				case SPEI_VISEME:
					eventName = "SPEI_VISEME"; //$NON-NLS-1$
					break;
				case SPEI_TTS_AUDIO_LEVEL:
					eventName = "SPEI_TTS_AUDIO_LEVEL"; //$NON-NLS-1$
					break;
				default:
					eventName = "Unknown+(" + se.getEventId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
					break;
				}
				System.out
						.println(eventName
								+ ": " + se.getLParamType() + "/" + se.getStreamNum() + "/" + se.getAudioStreamOffset() + "/" + se.getLParam() + "/" + se.getWParam()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}
		} finally {
			se.dispose();
		}
		return 0;
	}

	public int SetNotifyCallbackFunction(long pCallbackAddress, int wParam,
			int lParam) {
		return COM.VtblCall(5, address, pCallbackAddress, wParam, lParam);
	}

	public int SetInterest(int eventInterest, int queuedInterest) {
		return COMUtil.VtblCall(10, address, 0x40000000 | eventInterest, 2,
				0x40000000 | queuedInterest, 2);
	}

	public int GetEvent(long pSPEVENTAddress) {
		return COM.VtblCall(11, address, 1, pSPEVENTAddress, 0);
	}
}
