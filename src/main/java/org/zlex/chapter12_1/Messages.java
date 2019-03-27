/**
 * 2009-12-4
 */
package org.zlex.chapter12_1;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author 梁栋
 *
 */
public class Messages {
	private static final String BUNDLE_NAME = "org.zlex.chapter12_1.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
