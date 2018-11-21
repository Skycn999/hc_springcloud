$.fn.extend({
    smiliesInsert: function(html) {
        var self = this;
        var ta = $(this)[0];

        if (ta.onbeforedeactivate) {
            var ieRangeBookMark;
            ta.attachEvent('onbeforedeactivate', function() {
                ieRangeBookMark = self.im_getRange().selection.getBookmark();
            });
            ta.attachEvent('onactivate', function() {
                self.im_getRange().selection.moveToBookmark(ieRangeBookMark);
            });
        } else {
            $(ta).bind('keyup mouseup', function() {
                self.im_saveLastRange();
            });
        }

        ta.focus();
        if (document.selection) {
            var c = document.selection.createRange();
            if (c) {
                if (document.selection.type.toLowerCase() != "none") {
                    document.selection.clear(); //清除选中
                }
                c.pasteHTML(html);
                c.collapse(false);
                c.select();
            }
        } else {
            var range = this.im_getRange().range,
                selection = this.im_getRange().selection;
            if (!range) {
                ta.innerHTML += html;
                self.im_saveLastRange();
                return this;
            }
            var oFragment = range.createContextualFragment(html), //把插入内容转变为DocumentFragment
                oLastNode = oFragment.lastChild; //用于修正编辑光标的位置
            range.insertNode(oFragment);
            range.setEndAfter(oLastNode); //把编辑光标放到我们插入内容之后
            range.setStartAfter(oLastNode);
            selection.removeAllRanges(); //清除所有选择，要不我们插入的内容与刚才的文本处于选中状态
            selection.addRange(range); //插入内容
        }
        self.im_saveLastRange();
    },
    im_saveLastRange: function() {
        this.im_getRange() && (this.lastRange = this.im_getRange().range);
    },
    im_getRange: function() {
        var selection = (window.getSelection) ? window.getSelection() : document.selection;
        if (!selection) {
            return null;
        }
        try {
            var range = selection.createRange ? selection.createRange() : selection.getRangeAt(0);
            var text = window.getSelection ? range : range.text;
            var rangeNode = null;
            if (range.commonAncestorContainer) {
                rangeNode = range.commonAncestorContainer;
            } else {
                if (range.parentElement)
                    rangeNode = range.parentElement();
            }
            return {
                node: rangeNode,
                range: range,
                text: text,
                selection: selection
            };
        } catch (e) {
            return null;
        }
    }
});



///
$.extend({
    /**
     * 获取表情数组
     */
    smiliesGetArray: function() {
        return [
            ['1', ':smile:', 'smile.png', '28', '28', '28', '微笑'],
            ['2', ':sad:', 'sad.png', '28', '28', '28', '难过'],
            ['3', ':biggrin:', 'biggrin.png', '28', '28', '28', '呲牙'],
            ['4', ':cry:', 'cry.png', '28', '28', '28', '大哭'],
            ['5', ':arrogant:', 'arrogant.png', '28', '28', '28', '傲慢'],
            ['6', ':awkward:', 'awkward.png', '28', '28', '28', '尴尬'],
            ['7', ':blink:', 'blink.png', '28', '28', '28', '眨眼'],
            ['8', ':shy:', 'shy.png', '28', '28', '28', '害羞'],
            ['9', ':titter:', 'titter.png', '28', '28', '28', '偷笑'],
            ['10', ':embarrassed:', 'embarrassed.png', '28', '28', '28', '囧'],
            ['11', ':mad:', 'mad.png', '28', '28', '28', '抓狂'],
            ['12', ':lol:', 'lol.png', '28', '28', '28', '阴险'],
            ['13', ':loveliness:', 'loveliness.png', '28', '28', '28', '可爱'],
            ['14', ':depressed:', 'depressed.png', '28', '28', '28', '沮丧'],
            ['15', ':curse:', 'curse.png', '28', '28', '28', '诅咒'],
            ['16', ':dizzy:', 'dizzy.png', '28', '28', '28', '晕'],
            ['17', ':shutup:', 'shutup.png', '28', '28', '28', '闭嘴'],
            ['18', ':sleep:', 'sleep.png', '28', '28', '28', '睡'],
            ['19', ':despise:', 'despise.png', '28', '28', '28', '鄙视'],
            ['20', ':love:', 'love.png', '28', '28', '28', '爱'],
            ['21', ':mouth:', 'mouth.png', '28', '28', '28', '撇嘴'],
            ['22', ':stare:', 'stare.png', '28', '28', '28', '凝视'],
            ['23', ':kiss:', 'kiss.png', '28', '28', '28', '示爱'],
            ['24', ':swear:', 'swear.png', '28', '28', '28', '咒骂']
        ];
    },
    /**
     * 将表情特殊符号变成img标签
     */
    smiliesFormatHtml: function(msg ,url) {
        var smiliesArray = $.smiliesGetArray();
        if(!msg) return '';
        if (typeof smiliesArray !== "undefined") {
            msg = '' + msg;
            smiliesArray.forEach(function(item , index) {
                var re = new RegExp("" + item[1], "g");
                var smilieimg = '<img title="' + item[6] + '" alt="' + item[6] + '" src="' + url + 'smilies/images/' + item[2] + '">';
                msg = msg.replace(re, smilieimg);
            });
        }
        return msg;
    },

    /**
     * 将字符串中的img 标签转换成:hub:格式
     * @param  {[type]} html [description]
     * @return {[type]}      [description]
     */
    smiliesFormatHtmlToSign: function(html) {

        var a =  html.replace(/<img[^>]data-sign.*?>/gi, function(match) {
            var patt = /<img[^>]+data-sign=['"]:([^'"]+):['"]+/g,
                result='', temp;
            while ((temp = patt.exec(match)) != null) {
                result = ":" + temp[1] + ":";
            }
            return result;
        }) ;

        return a ? a : html;
    }
});