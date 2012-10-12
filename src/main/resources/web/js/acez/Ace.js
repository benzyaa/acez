/* Ace.js

	Purpose:
		A wrapped ACE editor for ZK
	Description:
		ACE Editor license under BSD
			- https://github.com/ajaxorg/ace/blob/master/LICENSE
		Version: manual build in 10/12/2012
	History:
		Thu, Oct 11, 2012  3:34:18 PM, Created by jumperchen

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
acez.Ace = zk.$extends(zul.Widget, {
	$define: {
		value: _zkf = function () {
			if (this._ed) {
				this._ed.getSession().setValue(this.getEncodedText());
			}
		},
		mode: function () {
			if (this._ed) {
				var mode = require("ace/mode/"  + this._mode).Mode;
				if (mode)
					this._ed.getSession().setMode(new mode());
			}
		},
		theme: function () {
			if (this._ed) {
				this._ed.setTheme('ace/theme/' + this._theme);
			}
		}
	},
	/**
	 * Returns the encoded text.
	 * @see zUtl#encodeXML
	 * @return String
	 */
	getEncodedText: function () {
		return zUtl.encodeXML(this._value, {multiline:true});
	},
	bind_: function () {
		this.$supers(acez.Ace, 'bind_', arguments);
		this._ed = ace.edit(this.uuid);
		if (this._mode) {
			var mode = require("ace/mode/"  + this._mode).Mode;
			this._ed.getSession().setMode(new mode());
		}
		if (this._theme) {
			this._ed.setTheme('ace/theme/' + this._theme);
		}
		this._ed.getSession().on("change", this.proxy(function (evt)   {
			this.fire('onChanging', {value: evt.data.text});
		}));
		this._ed.on("blur", this.proxy(this.doBlur_));
		this._ed.on("focus", this.proxy(this.doFocus_));
	},
	doBlur_: function () {
		if (this._ed) {
			var newVal = this._ed.getSession().getValue();
			if (newVal != this._value) {
				this.fire('onChange', {value: newVal});
			}
		}
		this.fire('onBlur');
	},
	doFocus_: function () {
		this.fire('onFocus');
	},
	unbind_: function () {
		if (this._ed) {
			this._ed.destroy();
			this._ed = null;
		}
		this.$supers(acez.Ace,'unbind_', arguments);
	},
	redraw: function (out) {
		out.push('<div', this.domAttrs_(), '>', this.getEncodedText(), '</div>');
	}
});