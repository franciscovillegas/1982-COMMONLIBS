/*
This file is part of Ext JS 5.0.1.1255

Copyright (c) 2011-2014 Sencha Inc

Contact:  http://www.sencha.com/contact

Commercial Usage
Licensees holding valid commercial licenses may use this file in accordance with the Commercial
Software License Agreement provided with the Software or, alternatively, in accordance with the
terms contained in a written agreement between you and Sencha.

If you are unsure which license is appropriate for your use, please contact the sales department
at http://www.sencha.com/contact.

Version: 5.0.1.1255 Build date: 2014-08-05 20:14:52 (2852ec9b146a917f790d13cfa9b9c2fa041fccf8)

*/
Ext.define('EXTJS-14607.picker.Date', {
    override: 'Ext.picker.Date',


    runAnimation: function(isHide) {
        var me = this,
            picker = this.monthPicker,
            options = {
                duration: 200,
                callback: function() {
                    picker.setVisible(!isHide);
                    // See showMonthPicker
                    picker.ownerCmp = isHide ? null : me;
                }
            };


        if (isHide) {
            picker.el.slideOut('t', options);
        } else {
            picker.el.slideIn('t', options);
        }
    },


    hideMonthPicker: function(animate) {
        var me = this,
            picker = me.monthPicker;


        if (picker && picker.isVisible()) {
            if (me.shouldAnimate(animate)) {
                me.runAnimation(true);
            } else {
                picker.hide();
                // See showMonthPicker
                picker.ownerCmp = null;
            }
        }
        return me;
    },


    showMonthPicker: function(animate) {
        var me = this,
            el = me.el,
            picker;


        if (me.rendered && !me.disabled) {
            picker = me.createMonthPicker();
            if (!picker.isVisible()) {
                picker.setValue(me.getActive());
                picker.setSize(el.getSize());
                picker.setPosition(-el.getBorderWidth('l'), -el.getBorderWidth('t'));
                if (me.shouldAnimate(animate)) {
                    me.runAnimation(false);
                } else {
                    picker.show();
                    // We need to set the ownerCmp so that owns() can correctly
                    // match up the component hierarchy, however when positioning the picker
                    // we don't want it to position like a normal floater because we render it to 
                    // month picker element itself.
                    picker.ownerCmp = me;
                }
            }
        }
        return me;
    }
});