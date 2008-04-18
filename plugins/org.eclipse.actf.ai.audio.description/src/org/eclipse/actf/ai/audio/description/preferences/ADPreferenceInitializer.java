/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Daisuke SATO - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.ai.audio.description.preferences;

import org.eclipse.actf.ai.audio.description.DescriptionPlugin;
import org.eclipse.actf.ai.tts.TTSRegistry;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


public class ADPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = DescriptionPlugin.getDefault().getPreferenceStore();
		store.setDefault(DescriptionPlugin.PREF_ENGINE, TTSRegistry.getDefaultEngine());
	}
}
