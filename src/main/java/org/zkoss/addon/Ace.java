/* Ace.java

	Purpose:
		
	Description:
		
	History:
		Fri, Oct 12, 2012 11:53:46 AM, Created by jumperchen

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
package org.zkoss.addon;

import java.util.Map;

import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.impl.XulElement;

/**
 * A ACE editor
 * @author jumperchen
 *
 */
public class Ace extends XulElement {

	static {
		addClientEvent(Ace.class, Events.ON_CHANGE, CE_REPEAT_IGNORE|CE_IMPORTANT);
		addClientEvent(Ace.class, Events.ON_CHANGING, CE_DUPLICATE_IGNORE);
	}
	
	private String _mode;
	private String _value;
	private String _theme;

	public String getMode() {
		return _mode;
	}

	public void setMode(String mode) {
		if (!Objects.equals(_mode, mode)) {
			_mode = mode;
			smartUpdate("mode", _mode);
		}
	}

	public String getTheme() {
		return _theme;
	}

	public void setTheme(String theme) {
		if (!Objects.equals(_theme, theme)) {
			_theme = theme;
			smartUpdate("theme", _theme);
		}
	}


	/** Returns the value.
	 * <p>Default: "".
	 * <p>Deriving class can override it to return whatever it wants
	 * other than null.
	 */
	public String getValue() {
		return _value;
	}
	/** Sets the value.
	 */
	public void setValue(String value) {
		if (value == null) value = "";
		if (!Objects.equals(_value, value)) {
			_value = value;
			smartUpdate("value", getValue());
		}
	}


	//super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		render(renderer, "mode", _mode);
		render(renderer, "value", _value);
		render(renderer, "theme", _theme);
	}
	
	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();		
		if (cmd.equals(Events.ON_CHANGE)) {
			InputEvent evt = InputEvent.getInputEvent(request, _value);
			_value = evt.getValue();
			Events.postEvent(evt);
		} else if (cmd.equals(Events.ON_CHANGING)) {
			final Map<String, Object> data = request.getData();
			final Object clientv = data.get("value");
			final Object oldval = _value;
			final InputEvent evt = new InputEvent(cmd, this,
				clientv == null ? "" : clientv.toString(), oldval,
				AuRequests.getBoolean(data, "bySelectBack"),
				AuRequests.getInt(data, "start", 0));
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}

	/**
	 * The default zclass is "z-ace"
	 */
	public String getZclass() {
		return (this._zclass != null ? this._zclass : "z-ace");
	}
}

