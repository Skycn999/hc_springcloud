/*!
 * Bootstrap v3.3.1 (http://getbootstrap.com)
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 */

if (typeof jQuery === 'undefined') {
    throw new Error('Bootstrap\'s JavaScript requires jQuery')
}

+function ($) {
    var version = $.fn.jquery.split(' ')[0].split('.')
    if ((version[0] < 2 && version[1] < 9) || (version[0] == 1 && version[1] == 9 &&
            version[2] < 1)) {
        throw new Error(
            'Bootstrap\'s JavaScript requires jQuery version 1.9.1 or higher')
    }
}(jQuery);


/* ========================================================================
 * 判断ie
 * ======================================================================== */
function getIEVersion() {
    var browser = navigator.appName,
        b_version = navigator.appVersion,
        version = b_version.split(";"),
        trim_Version;
    if (version.length > 1) {
        trim_Version = parseInt(version[1].replace(/[ ]/g, "").replace(/MSIE/g, ""));
    }
    return trim_Version ? trim_Version : false;
};
+function () {
    var a = getIEVersion(),
        b = '<div class="ie-mask"></div><div class="mark-brows"><a href="http://www.chromeliulanqi.com/" class="link chrome"></a><a href="https://www.microsoft.com/zh-cn" class="link ie"></a><a href="http://www.firefox.com.cn/" class="link ff"></a></div>'
    ;
    if (a && a < 9) {
        $("body").append(b);
    }
}(jQuery);

/* ========================================================================
 * Bootstrap: transition.js v3.3.1
 * http://getbootstrap.com/javascript/#transitions
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // CSS TRANSITION SUPPORT (Shoutout: http://www.modernizr.com/)
    // ============================================================

    function transitionEnd() {
        var el = document.createElement('bootstrap')

        var transEndEventNames = {
            WebkitTransition: 'webkitTransitionEnd',
            MozTransition: 'transitionend',
            OTransition: 'oTransitionEnd otransitionend',
            transition: 'transitionend'
        }

        for (var name in transEndEventNames) {
            if (el.style[name] !== undefined) {
                return {
                    end: transEndEventNames[name]
                }
            }
        }

        return false // explicit for ie8 (  ._.)
    }

    // http://blog.alexmaccaw.com/css-transitions
    $.fn.emulateTransitionEnd = function (duration) {
        var called = false
        var $el = this
        $(this).one('bsTransitionEnd', function () {
            called = true
        })
        var callback = function () {
            if (!called) $($el).trigger($.support.transition.end)
        }
        setTimeout(callback, duration)
        return this
    }

    $(function () {
        $.support.transition = transitionEnd()

        if (!$.support.transition) return

        $.event.special.bsTransitionEnd = {
            bindType: $.support.transition.end,
            delegateType: $.support.transition.end,
            handle: function (e) {
                if ($(e.target).is(this)) return e.handleObj.handler.apply(this,
                    arguments)
            }
        }
    })

}(jQuery);

/* ========================================================================
 * Bootstrap: alert.js v3.3.1
 * http://getbootstrap.com/javascript/#alerts
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // ALERT CLASS DEFINITION
    // ======================

    var dismiss = '[data-dismiss="alert"]'
    var Alert = function (el) {
        $(el).on('click', dismiss, this.close)
    }

    Alert.VERSION = '3.3.1'

    Alert.TRANSITION_DURATION = 150

    Alert.prototype.close = function (e) {
        var $this = $(this)
        var selector = $this.attr('data-target')

        if (!selector) {
            selector = $this.attr('href')
            selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
        }

        var $parent = $(selector)

        if (e) e.preventDefault()

        if (!$parent.length) {
            $parent = $this.closest('.alert')
        }

        $parent.trigger(e = $.Event('close.bs.alert'))

        if (e.isDefaultPrevented()) return

        $parent.removeClass('in')

        function removeElement() {
            // detach from parent, fire event then clean up data
            $parent.detach().trigger('closed.bs.alert').remove()
        }

        $.support.transition && $parent.hasClass('fade') ?
            $parent
                .one('bsTransitionEnd', removeElement)
                .emulateTransitionEnd(Alert.TRANSITION_DURATION) :
            removeElement()
    }


    // ALERT PLUGIN DEFINITION
    // =======================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.alert')

            if (!data) $this.data('bs.alert', (data = new Alert(this)))
            if (typeof option == 'string') data[option].call($this)
        })
    }

    var old = $.fn.alert

    $.fn.alert = Plugin
    $.fn.alert.Constructor = Alert


    // ALERT NO CONFLICT
    // =================

    $.fn.alert.noConflict = function () {
        $.fn.alert = old
        return this
    }


    // ALERT DATA-API
    // ==============

    $(document).on('click.bs.alert.data-api', dismiss, Alert.prototype.close)

}(jQuery);

/* ========================================================================
 * Bootstrap: button.js v3.3.1
 * http://getbootstrap.com/javascript/#buttons
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // BUTTON PUBLIC CLASS DEFINITION
    // ==============================

    var Button = function (element, options) {
        this.$element = $(element)
        this.options = $.extend({}, Button.DEFAULTS, options)
        this.isLoading = false
    }

    Button.VERSION = '3.3.1'

    Button.DEFAULTS = {
        loadingText: 'loading...'
    }

    Button.prototype.setState = function (state) {
        var d = 'disabled'
        var $el = this.$element
        var val = $el.is('input') ? 'val' : 'html'
        var data = $el.data()

        state = state + 'Text'

        if (data.resetText == null) $el.data('resetText', $el[val]())

        // push to event loop to allow forms to submit
        setTimeout($.proxy(function () {
            $el[val](data[state] == null ? this.options[state] : data[state])

            if (state == 'loadingText') {
                this.isLoading = true
                $el.addClass(d).attr(d, d)
            } else if (this.isLoading) {
                this.isLoading = false
                $el.removeClass(d).removeAttr(d)
            }
        }, this), 0)
    }

    Button.prototype.toggle = function () {
        var changed = true
        var $parent = this.$element.closest('[data-toggle="buttons"]')

        if ($parent.length) {
            var $input = this.$element.find('input')
            if ($input.prop('type') == 'radio') {
                if ($input.prop('checked') && this.$element.hasClass('active'))
                    changed = false
                else $parent.find('.active').removeClass('active')
            }
            if (changed) $input.prop('checked', !this.$element.hasClass('active')).trigger(
                'change')
        } else {
            this.$element.attr('aria-pressed', !this.$element.hasClass('active'))
        }

        if (changed) this.$element.toggleClass('active')
    }


    // BUTTON PLUGIN DEFINITION
    // ========================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.button')
            var options = typeof option == 'object' && option

            if (!data) $this.data('bs.button', (data = new Button(this, options)))

            if (option == 'toggle') data.toggle()
            else if (option) data.setState(option)
        })
    }

    var old = $.fn.button

    $.fn.button = Plugin
    $.fn.button.Constructor = Button


    // BUTTON NO CONFLICT
    // ==================

    $.fn.button.noConflict = function () {
        $.fn.button = old
        return this
    }


    // BUTTON DATA-API
    // ===============

    $(document)
        .on('click.bs.button.data-api', '[data-toggle^="button"]', function (e) {
            var $btn = $(e.target)
            if (!$btn.hasClass('btn')) $btn = $btn.closest('.btn')
            Plugin.call($btn, 'toggle')
            e.preventDefault()
        })
        .on('focus.bs.button.data-api blur.bs.button.data-api',
            '[data-toggle^="button"]',
            function (e) {
                $(e.target).closest('.btn').toggleClass('focus', /^focus(in)?$/.test(e.type))
            })

}(jQuery);

/* ========================================================================
 * Bootstrap: carousel.js v3.3.1
 * http://getbootstrap.com/javascript/#carousel
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // CAROUSEL CLASS DEFINITION
    // =========================

    var Carousel = function (element, options) {
        this.$element = $(element)
        this.$indicators = this.$element.find('.carousel-indicators')
        this.options = options
        this.paused =
            this.sliding =
                this.interval =
                    this.$active =
                        this.$items = null

        this.options.keyboard && this.$element.on('keydown.bs.carousel', $.proxy(
            this.keydown, this))

        this.options.pause == 'hover' && !('ontouchstart' in document.documentElement) &&
        this.$element
            .on('mouseenter.bs.carousel', $.proxy(this.pause, this))
            .on('mouseleave.bs.carousel', $.proxy(this.cycle, this))
    }

    Carousel.VERSION = '3.3.1'

    Carousel.TRANSITION_DURATION = 600

    Carousel.DEFAULTS = {
        interval: 5000,
        pause: 'hover',
        wrap: true,
        keyboard: true
    }

    Carousel.prototype.keydown = function (e) {
        if (/input|textarea/i.test(e.target.tagName)) return
        switch (e.which) {
            case 37:
                this.prev();
                break
            case 39:
                this.next();
                break
            default:
                return
        }

        e.preventDefault()
    }

    Carousel.prototype.cycle = function (e) {
        e || (this.paused = false)

        this.interval && clearInterval(this.interval)

        this.options.interval && !this.paused && (this.interval = setInterval($.proxy(
            this.next, this), this.options.interval))

        return this
    }

    Carousel.prototype.getItemIndex = function (item) {
        this.$items = item.parent().children('.item')
        return this.$items.index(item || this.$active)
    }

    Carousel.prototype.getItemForDirection = function (direction, active) {
        var delta = direction == 'prev' ? -1 : 1
        var activeIndex = this.getItemIndex(active)
        var itemIndex = (activeIndex + delta) % this.$items.length
        return this.$items.eq(itemIndex)
    }

    Carousel.prototype.to = function (pos) {
        var that = this
        var activeIndex = this.getItemIndex(this.$active = this.$element.find(
            '.item.active'))

        if (pos > (this.$items.length - 1) || pos < 0) return

        if (this.sliding) return this.$element.one('slid.bs.carousel', function () {
            that.to(pos)
        }) // yes, "slid"
        if (activeIndex == pos) return this.pause().cycle()

        return this.slide(pos > activeIndex ? 'next' : 'prev', this.$items.eq(pos))
    }

    Carousel.prototype.pause = function (e) {
        e || (this.paused = true)

        if (this.$element.find('.next, .prev').length && $.support.transition) {
            this.$element.trigger($.support.transition.end)
            this.cycle(true)
        }

        this.interval = clearInterval(this.interval)

        return this
    }

    Carousel.prototype.next = function () {
        if (this.sliding) return
        return this.slide('next')
    }

    Carousel.prototype.prev = function () {
        if (this.sliding) return
        return this.slide('prev')
    }

    Carousel.prototype.slide = function (type, next) {
        var $active = this.$element.find('.item.active')
        var $next = next || this.getItemForDirection(type, $active)
        var isCycling = this.interval
        var direction = type == 'next' ? 'left' : 'right'
        var fallback = type == 'next' ? 'first' : 'last'
        var that = this

        if (!$next.length) {
            if (!this.options.wrap) return
            $next = this.$element.find('.item')[fallback]()
        }

        if ($next.hasClass('active')) return (this.sliding = false)

        var relatedTarget = $next[0]
        var slideEvent = $.Event('slide.bs.carousel', {
            relatedTarget: relatedTarget,
            direction: direction
        })
        this.$element.trigger(slideEvent)
        if (slideEvent.isDefaultPrevented()) return

        this.sliding = true

        isCycling && this.pause()

        if (this.$indicators.length) {
            this.$indicators.find('.active').removeClass('active')
            var $nextIndicator = $(this.$indicators.children()[this.getItemIndex(
                $next)])
            $nextIndicator && $nextIndicator.addClass('active')
        }

        var slidEvent = $.Event('slid.bs.carousel', {
            relatedTarget: relatedTarget,
            direction: direction
        }) // yes, "slid"
        if ($.support.transition && this.$element.hasClass('slide')) {
            $next.addClass(type)
            $next[0].offsetWidth // force reflow
            $active.addClass(direction)
            $next.addClass(direction)
            $active
                .one('bsTransitionEnd', function () {
                    $next.removeClass([type, direction].join(' ')).addClass('active')
                    $active.removeClass(['active', direction].join(' '))
                    that.sliding = false
                    setTimeout(function () {
                        that.$element.trigger(slidEvent)
                    }, 0)
                })
                .emulateTransitionEnd(Carousel.TRANSITION_DURATION)
        } else {
            $active.removeClass('active')
            $next.addClass('active')
            this.sliding = false
            this.$element.trigger(slidEvent)
        }

        isCycling && this.cycle()

        return this
    }


    // CAROUSEL PLUGIN DEFINITION
    // ==========================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.carousel')
            var options = $.extend({}, Carousel.DEFAULTS, $this.data(), typeof option ==
                'object' && option)
            var action = typeof option == 'string' ? option : options.slide

            if (!data) $this.data('bs.carousel', (data = new Carousel(this,
                options)))
            if (typeof option == 'number') data.to(option)
            else if (action) data[action]()
            else if (options.interval) data.pause().cycle()
        })
    }

    var old = $.fn.carousel

    $.fn.carousel = Plugin
    $.fn.carousel.Constructor = Carousel


    // CAROUSEL NO CONFLICT
    // ====================

    $.fn.carousel.noConflict = function () {
        $.fn.carousel = old
        return this
    }


    // CAROUSEL DATA-API
    // =================

    var clickHandler = function (e) {
        var href
        var $this = $(this)
        var $target = $($this.attr('data-target') || (href = $this.attr('href')) &&
            href.replace(/.*(?=#[^\s]+$)/, '')) // strip for ie7
        if (!$target.hasClass('carousel')) return
        var options = $.extend({}, $target.data(), $this.data())
        var slideIndex = $this.attr('data-slide-to')
        if (slideIndex) options.interval = false

        Plugin.call($target, options)

        if (slideIndex) {
            $target.data('bs.carousel').to(slideIndex)
        }

        e.preventDefault()
    }

    $(document)
        .on('click.bs.carousel.data-api', '[data-slide]', clickHandler)
        .on('click.bs.carousel.data-api', '[data-slide-to]', clickHandler)

    $(window).on('load', function () {
        $('[data-ride="carousel"]').each(function () {
            var $carousel = $(this)
            Plugin.call($carousel, $carousel.data())
        })
    })

}(jQuery);

/* ========================================================================
 * Bootstrap: collapse.js v3.3.1
 * http://getbootstrap.com/javascript/#collapse
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // COLLAPSE PUBLIC CLASS DEFINITION
    // ================================

    var Collapse = function (element, options) {
        this.$element = $(element)
        this.options = $.extend({}, Collapse.DEFAULTS, options)
        this.$trigger = $(this.options.trigger).filter('[href="#' + element.id +
            '"], [data-target="#' + element.id + '"]')
        this.transitioning = null

        if (this.options.parent) {
            this.$parent = this.getParent()
        } else {
            this.addAriaAndCollapsedClass(this.$element, this.$trigger)
        }

        if (this.options.toggle) this.toggle()
    }

    Collapse.VERSION = '3.3.1'

    Collapse.TRANSITION_DURATION = 350

    Collapse.DEFAULTS = {
        toggle: true,
        trigger: '[data-toggle="collapse"]'
    }

    Collapse.prototype.dimension = function () {
        var hasWidth = this.$element.hasClass('width')
        return hasWidth ? 'width' : 'height'
    }

    Collapse.prototype.show = function () {
        if (this.transitioning || this.$element.hasClass('in')) return

        var activesData
        var actives = this.$parent && this.$parent.find('> .panel').children(
            '.in, .collapsing')

        if (actives && actives.length) {
            activesData = actives.data('bs.collapse')
            if (activesData && activesData.transitioning) return
        }

        var startEvent = $.Event('show.bs.collapse')
        this.$element.trigger(startEvent)
        if (startEvent.isDefaultPrevented()) return

        if (actives && actives.length) {
            Plugin.call(actives, 'hide')
            activesData || actives.data('bs.collapse', null)
        }

        var dimension = this.dimension()

        this.$element
            .removeClass('collapse')
            .addClass('collapsing')[dimension](0)
            .attr('aria-expanded', true)

        this.$trigger
            .removeClass('collapsed')
            .attr('aria-expanded', true)

        this.transitioning = 1

        var complete = function () {
            this.$element
                .removeClass('collapsing')
                .addClass('collapse in')[dimension]('')
            this.transitioning = 0
            this.$element
                .trigger('shown.bs.collapse')
        }

        if (!$.support.transition) return complete.call(this)

        var scrollSize = $.camelCase(['scroll', dimension].join('-'))

        this.$element
            .one('bsTransitionEnd', $.proxy(complete, this))
            .emulateTransitionEnd(Collapse.TRANSITION_DURATION)[dimension](this.$element[
            0][scrollSize])
    }

    Collapse.prototype.hide = function () {
        if (this.transitioning || !this.$element.hasClass('in')) return

        var startEvent = $.Event('hide.bs.collapse')
        this.$element.trigger(startEvent)
        if (startEvent.isDefaultPrevented()) return

        var dimension = this.dimension()

        this.$element[dimension](this.$element[dimension]())[0].offsetHeight

        this.$element
            .addClass('collapsing')
            .removeClass('collapse in')
            .attr('aria-expanded', false)

        this.$trigger
            .addClass('collapsed')
            .attr('aria-expanded', false)

        this.transitioning = 1

        var complete = function () {
            this.transitioning = 0
            this.$element
                .removeClass('collapsing')
                .addClass('collapse')
                .trigger('hidden.bs.collapse')
        }

        if (!$.support.transition) return complete.call(this)

        this.$element[dimension](0)
            .one('bsTransitionEnd', $.proxy(complete, this))
            .emulateTransitionEnd(Collapse.TRANSITION_DURATION)
    }

    Collapse.prototype.toggle = function () {
        this[this.$element.hasClass('in') ? 'hide' : 'show']()
    }

    Collapse.prototype.getParent = function () {
        return $(this.options.parent)
            .find('[data-toggle="collapse"][data-parent="' + this.options.parent +
                '"]')
            .each($.proxy(function (i, element) {
                var $element = $(element)
                this.addAriaAndCollapsedClass(getTargetFromTrigger($element),
                    $element)
            }, this))
            .end()
    }

    Collapse.prototype.addAriaAndCollapsedClass = function ($element, $trigger) {
        var isOpen = $element.hasClass('in')

        $element.attr('aria-expanded', isOpen)
        $trigger
            .toggleClass('collapsed', !isOpen)
            .attr('aria-expanded', isOpen)
    }

    function getTargetFromTrigger($trigger) {
        var href
        var target = $trigger.attr('data-target') || (href = $trigger.attr('href')) &&
            href.replace(/.*(?=#[^\s]+$)/, '') // strip for ie7

        return $(target)
    }


    // COLLAPSE PLUGIN DEFINITION
    // ==========================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.collapse')
            var options = $.extend({}, Collapse.DEFAULTS, $this.data(), typeof option ==
                'object' && option)

            if (!data && options.toggle && option == 'show') options.toggle =
                false
            if (!data) $this.data('bs.collapse', (data = new Collapse(this,
                options)))
            if (typeof option == 'string') data[option]()
        })
    }

    var old = $.fn.collapse

    $.fn.collapse = Plugin
    $.fn.collapse.Constructor = Collapse


    // COLLAPSE NO CONFLICT
    // ====================

    $.fn.collapse.noConflict = function () {
        $.fn.collapse = old
        return this
    }


    // COLLAPSE DATA-API
    // =================

    $(document).on('click.bs.collapse.data-api', '[data-toggle="collapse"]',
        function (e) {
            var $this = $(this)

            if (!$this.attr('data-target')) e.preventDefault()

            var $target = getTargetFromTrigger($this)
            var data = $target.data('bs.collapse')
            var option = data ? 'toggle' : $.extend({}, $this.data(), {
                trigger: this
            })

            Plugin.call($target, option)
        })

}(jQuery);

/* ========================================================================
 * Bootstrap: dropdown.js v3.3.1
 * http://getbootstrap.com/javascript/#dropdowns
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // DROPDOWN CLASS DEFINITION
    // =========================

    var backdrop = '.dropdown-backdrop'
    var toggle = '[data-toggle="dropdown"]'
    var Dropdown = function (element) {
        $(element).on('click.bs.dropdown', this.toggle)
    }

    Dropdown.VERSION = '3.3.1'

    Dropdown.prototype.toggle = function (e) {
        var $this = $(this)

        if ($this.is('.disabled, :disabled')) return

        var $parent = getParent($this)
        var isActive = $parent.hasClass('open')

        clearMenus()

        if (!isActive) {
            if ('ontouchstart' in document.documentElement && !$parent.closest(
                    '.navbar-nav').length) {
                // if mobile we use a backdrop because click events don't delegate
                $('<div class="dropdown-backdrop"/>').insertAfter($(this)).on('click',
                    clearMenus)
            }

            var relatedTarget = {
                relatedTarget: this
            }
            $parent.trigger(e = $.Event('show.bs.dropdown', relatedTarget))

            if (e.isDefaultPrevented()) return

            $this
                .trigger('focus')
                .attr('aria-expanded', 'true')

            $parent
                .toggleClass('open')
                .trigger('shown.bs.dropdown', relatedTarget)
        }

        return false
    }

    Dropdown.prototype.keydown = function (e) {
        if (!/(38|40|27|32)/.test(e.which) || /input|textarea/i.test(e.target.tagName))
            return

        var $this = $(this)

        e.preventDefault()
        e.stopPropagation()

        if ($this.is('.disabled, :disabled')) return

        var $parent = getParent($this)
        var isActive = $parent.hasClass('open')

        if ((!isActive && e.which != 27) || (isActive && e.which == 27)) {
            if (e.which == 27) $parent.find(toggle).trigger('focus')
            return $this.trigger('click')
        }

        var desc = ' li:not(.divider):visible a'
        var $items = $parent.find('[role="menu"]' + desc + ', [role="listbox"]' +
            desc)

        if (!$items.length) return

        var index = $items.index(e.target)

        if (e.which == 38 && index > 0) index-- // up
        if (e.which == 40 && index < $items.length - 1) index++ // down
        if (!~index) index = 0

        $items.eq(index).trigger('focus')
    }

    function clearMenus(e) {
        if (e && e.which === 3) return
        $(backdrop).remove()
        $(toggle).each(function () {
            var $this = $(this)
            var $parent = getParent($this)
            var relatedTarget = {
                relatedTarget: this
            }

            if (!$parent.hasClass('open')) return

            $parent.trigger(e = $.Event('hide.bs.dropdown', relatedTarget))

            if (e.isDefaultPrevented()) return

            $this.attr('aria-expanded', 'false')
            $parent.removeClass('open').trigger('hidden.bs.dropdown',
                relatedTarget)
        })
    }

    function getParent($this) {
        var selector = $this.attr('data-target')

        if (!selector) {
            selector = $this.attr('href')
            selector = selector && /#[A-Za-z]/.test(selector) && selector.replace(
                /.*(?=#[^\s]*$)/, '') // strip for ie7
        }

        var $parent = selector && $(selector)

        return $parent && $parent.length ? $parent : $this.parent()
    }


    // DROPDOWN PLUGIN DEFINITION
    // ==========================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.dropdown')

            if (!data) $this.data('bs.dropdown', (data = new Dropdown(this)))
            if (typeof option == 'string') data[option].call($this)
        })
    }

    var old = $.fn.dropdown

    $.fn.dropdown = Plugin
    $.fn.dropdown.Constructor = Dropdown


    // DROPDOWN NO CONFLICT
    // ====================

    $.fn.dropdown.noConflict = function () {
        $.fn.dropdown = old
        return this
    }


    // APPLY TO STANDARD DROPDOWN ELEMENTS
    // ===================================

    $(document)
        .on('click.bs.dropdown.data-api', clearMenus)
        .on('click.bs.dropdown.data-api', '.dropdown form', function (e) {
            e.stopPropagation()
        })
        .on('click.bs.dropdown.data-api', toggle, Dropdown.prototype.toggle)
        .on('keydown.bs.dropdown.data-api', toggle, Dropdown.prototype.keydown)
        .on('keydown.bs.dropdown.data-api', '[role="menu"]', Dropdown.prototype.keydown)
        .on('keydown.bs.dropdown.data-api', '[role="listbox"]', Dropdown.prototype.keydown)

}(jQuery);

/* ========================================================================
 * Bootstrap: modal.js v3.3.1
 * http://getbootstrap.com/javascript/#modals
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // MODAL CLASS DEFINITION
    // ======================

    var Modal = function (element, options) {
        this.options = options
        this.$body = $(document.body)
        this.$element = $(element)
        this.$backdrop =
            this.isShown = null
        this.scrollbarWidth = 0

        if (this.options.remote) {
            this.$element
                .find('.modal-content')
                .load(this.options.remote, $.proxy(function () {
                    this.$element.trigger('loaded.bs.modal')
                }, this))
        }
    }

    Modal.VERSION = '3.3.1'

    Modal.TRANSITION_DURATION = 300
    Modal.BACKDROP_TRANSITION_DURATION = 150

    Modal.DEFAULTS = {
        backdrop: true,
        keyboard: true,
        show: true
    }

    Modal.prototype.toggle = function (_relatedTarget) {
        return this.isShown ? this.hide() : this.show(_relatedTarget)
    }

    Modal.prototype.show = function (_relatedTarget) {
        var that = this
        var e = $.Event('show.bs.modal', {
            relatedTarget: _relatedTarget
        })

        this.$element.trigger(e)

        if (this.isShown || e.isDefaultPrevented()) return

        this.isShown = true

        this.checkScrollbar()
        this.setScrollbar()
        this.$body.addClass('modal-open')

        this.escape()
        this.resize()

        this.$element.on('click.dismiss.bs.modal', '[data-dismiss="modal"]', $.proxy(
            this.hide, this))

        this.backdrop(function () {
            var transition = $.support.transition && that.$element.hasClass(
                'fade')

            if (!that.$element.parent().length) {
                that.$element.appendTo(that.$body) // don't move modals dom position
            }

            that.$element
                .show()
                .scrollTop(0)

            if (that.options.backdrop) that.adjustBackdrop()
            that.adjustDialog()

            if (transition) {
                that.$element[0].offsetWidth // force reflow
            }

            that.$element
                .addClass('in')
                .attr('aria-hidden', false)

            that.enforceFocus()

            var e = $.Event('shown.bs.modal', {
                relatedTarget: _relatedTarget
            })

            transition ?
                that.$element.find('.modal-dialog') // wait for modal to slide in
                    .one('bsTransitionEnd', function () {
                        that.$element.trigger('focus').trigger(e)
                    })
                    .emulateTransitionEnd(Modal.TRANSITION_DURATION) :
                that.$element.trigger('focus').trigger(e)
        })
    }

    Modal.prototype.hide = function (e) {
        if (e) e.preventDefault()

        e = $.Event('hide.bs.modal')

        this.$element.trigger(e)

        if (!this.isShown || e.isDefaultPrevented()) return

        this.isShown = false

        this.escape()
        this.resize()

        $(document).off('focusin.bs.modal')

        this.$element
            .removeClass('in')
            .attr('aria-hidden', true)
            .off('click.dismiss.bs.modal')

        $.support.transition && this.$element.hasClass('fade') ?
            this.$element
                .one('bsTransitionEnd', $.proxy(this.hideModal, this))
                .emulateTransitionEnd(Modal.TRANSITION_DURATION) :
            this.hideModal()
    }

    Modal.prototype.enforceFocus = function () {
        $(document)
            .off('focusin.bs.modal') // guard against infinite focus loop
            .on('focusin.bs.modal', $.proxy(function (e) {
                if (this.$element[0] !== e.target && !this.$element.has(e.target)
                        .length) {
                    this.$element.trigger('focus')
                }
            }, this))
    }

    Modal.prototype.escape = function () {
        if (this.isShown && this.options.keyboard) {
            this.$element.on('keydown.dismiss.bs.modal', $.proxy(function (e) {
                e.which == 27 && this.hide()
            }, this))
        } else if (!this.isShown) {
            this.$element.off('keydown.dismiss.bs.modal')
        }
    }

    Modal.prototype.resize = function () {
        if (this.isShown) {
            $(window).on('resize.bs.modal', $.proxy(this.handleUpdate, this))
        } else {
            $(window).off('resize.bs.modal')
        }
    }

    Modal.prototype.hideModal = function () {
        var that = this
        this.$element.hide()
        this.backdrop(function () {
            that.$body.removeClass('modal-open')
            that.resetAdjustments()
            that.resetScrollbar()
            that.$element.trigger('hidden.bs.modal')
        })
    }

    Modal.prototype.removeBackdrop = function () {
        this.$backdrop && this.$backdrop.remove()
        this.$backdrop = null
    }

    Modal.prototype.backdrop = function (callback) {
        var that = this
        var animate = this.$element.hasClass('fade') ? 'fade' : ''

        if (this.isShown && this.options.backdrop) {
            var doAnimate = $.support.transition && animate

            this.$backdrop = $('<div class="modal-backdrop ' + animate + '" />')
                .prependTo(this.$element)
                .on('click.dismiss.bs.modal', $.proxy(function (e) {
                    if (e.target !== e.currentTarget) return
                    this.options.backdrop == 'static' ? this.$element[0].focus.call(
                        this.$element[0]) : ""
                    /* (that.options.closeClickBackdropHide || this.hide.call(this)) */
                }, this))

            if (doAnimate) this.$backdrop[0].offsetWidth // force reflow

            this.$backdrop.addClass('in')

            if (!callback) return

            doAnimate ?
                this.$backdrop
                    .one('bsTransitionEnd', callback)
                    .emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
                callback()

        } else if (!this.isShown && this.$backdrop) {
            this.$backdrop.removeClass('in')

            var callbackRemove = function () {
                that.removeBackdrop()
                callback && callback()
            }
            $.support.transition && this.$element.hasClass('fade') ?
                this.$backdrop
                    .one('bsTransitionEnd', callbackRemove)
                    .emulateTransitionEnd(Modal.BACKDROP_TRANSITION_DURATION) :
                callbackRemove()

        } else if (callback) {
            callback()
        }
    }

    // these following methods are used to handle overflowing modals

    Modal.prototype.handleUpdate = function () {
        if (this.options.backdrop) this.adjustBackdrop()
        this.adjustDialog()
    }

    Modal.prototype.adjustBackdrop = function () {
        this.$backdrop
            .css('height', 0)
            .css('height', this.$element[0].scrollHeight)
    }

    Modal.prototype.adjustDialog = function () {
        var modalIsOverflowing = this.$element[0].scrollHeight > document.documentElement
            .clientHeight

        this.$element.css({
            paddingLeft: !this.bodyIsOverflowing && modalIsOverflowing ? this.scrollbarWidth : '',
            paddingRight: this.bodyIsOverflowing && !modalIsOverflowing ? this.scrollbarWidth : ''
        })
    }

    Modal.prototype.resetAdjustments = function () {
        this.$element.css({
            paddingLeft: '',
            paddingRight: ''
        })
    }

    Modal.prototype.checkScrollbar = function () {
        this.bodyIsOverflowing = document.body.scrollHeight > document.documentElement
            .clientHeight
        this.scrollbarWidth = this.measureScrollbar()
    }

    Modal.prototype.setScrollbar = function () {
        var bodyPad = parseInt((this.$body.css('padding-right') || 0), 10)
        if (this.bodyIsOverflowing) this.$body.css('padding-right', bodyPad +
            this.scrollbarWidth)
    }

    Modal.prototype.resetScrollbar = function () {
        this.$body.css('padding-right', '')
    }

    Modal.prototype.measureScrollbar = function () { // thx walsh
        var scrollDiv = document.createElement('div')
        scrollDiv.className = 'modal-scrollbar-measure'
        this.$body.append(scrollDiv)
        var scrollbarWidth = scrollDiv.offsetWidth - scrollDiv.clientWidth
        this.$body[0].removeChild(scrollDiv)
        return scrollbarWidth
    }


    // MODAL PLUGIN DEFINITION
    // =======================

    function Plugin(option, _relatedTarget) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.modal')
            var old = $this.data('old')
            var options = $.extend({}, Modal.DEFAULTS, $this.data(), typeof option ==
                'object' && option)
            //bycj[ ]
            if (old) {
                $this.find("[form-reset-point]").replaceWith(old)
                //$this.find("[form-reset-point]").html(old)
            }
            if (!data) $this.data('bs.modal', (data = new Modal(this, options)))
            if (typeof option == 'string') data[option](_relatedTarget)
            else if (options.show) data.show(_relatedTarget)
        })
    }

    var old = $.fn.modal

    $.fn.modal = Plugin
    $.fn.modal.Constructor = Modal


    // MODAL NO CONFLICT
    // =================

    $.fn.modal.noConflict = function () {
        $.fn.modal = old
        return this
    }


    // MODAL DATA-API
    // ==============

    $(document).on('click.bs.modal.data-api', '[data-toggle="modal"]', function (e) {
        var $this = $(this)
        var href = $this.attr('href')
        var $target = $($this.attr('data-target') || (href && href.replace(
            /.*(?=#[^\s]+$)/, ''))) // strip for ie7
        var option = $target.data('bs.modal') ? 'toggle' : $.extend({
            remote: !/#/.test(href) && href
        }, $target.data(), $this.data())
        if ($this.is('a')) e.preventDefault()

        $target.one('show.bs.modal', function (showEvent) {
            //_html[$this.attr('id')] = $this.html();
            if (showEvent.isDefaultPrevented()) return // only register focus restorer if modal will actually get shown
            var _this = $(this);
            //bycj []
            _this.data('old', _this.find("[form-reset-point]").clone(true))
            //_this.data('old', _this.find("[form-reset-point]").html())
            $target.one('hidden.bs.modal', function () {
                $this.is(':visible') && $this.trigger('focus')
            })
        })
        Plugin.call($target, option, this)
    })

}(jQuery);


/* ========================================================================
 * Bootstrap: tooltip.js v3.3.1
 * http://getbootstrap.com/javascript/#tooltip
 * Inspired by the original jQuery.tipsy by Jason Frame
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // TOOLTIP PUBLIC CLASS DEFINITION
    // ===============================

    var Tooltip = function (element, options) {
        this.type =
            this.options =
                this.enabled =
                    this.timeout =
                        this.hoverState =
                            this.$element = null

        this.init('tooltip', element, options)
    }

    Tooltip.VERSION = '3.3.1'

    Tooltip.TRANSITION_DURATION = 150

    Tooltip.DEFAULTS = {
        animation: true,
        placement: 'top',
        selector: false,
        template: '<div class="tooltip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>',
        trigger: 'hover focus',
        title: '',
        delay: 0,
        html: false,
        container: false,
        viewport: {
            selector: 'body',
            padding: 0
        }
    }

    Tooltip.prototype.init = function (type, element, options) {
        this.enabled = true
        this.type = type
        this.$element = $(element)
        this.options = this.getOptions(options)
        this.$viewport = this.options.viewport && $(this.options.viewport.selector ||
            this.options.viewport)

        var triggers = this.options.trigger.split(' ')

        for (var i = triggers.length; i--;) {
            var trigger = triggers[i]

            if (trigger == 'click') {
                this.$element.on('click.' + this.type, this.options.selector, $.proxy(
                    this.toggle, this))
            } else if (trigger != 'manual') {
                var eventIn = trigger == 'hover' ? 'mouseenter' : 'focusin'
                var eventOut = trigger == 'hover' ? 'mouseleave' : 'focusout'

                this.$element.on(eventIn + '.' + this.type, this.options.selector, $.proxy(
                    this.enter, this))
                this.$element.on(eventOut + '.' + this.type, this.options.selector, $
                    .proxy(this.leave, this))
            }
        }

        this.options.selector ?
            (this._options = $.extend({}, this.options, {
                trigger: 'manual',
                selector: ''
            })) :
            this.fixTitle()
    }

    Tooltip.prototype.getDefaults = function () {
        return Tooltip.DEFAULTS
    }

    Tooltip.prototype.getOptions = function (options) {
        options = $.extend({}, this.getDefaults(), this.$element.data(), options)

        if (options.delay && typeof options.delay == 'number') {
            options.delay = {
                show: options.delay,
                hide: options.delay
            }
        }

        return options
    }

    Tooltip.prototype.getDelegateOptions = function () {
        var options = {}
        var defaults = this.getDefaults()

        this._options && $.each(this._options, function (key, value) {
            if (defaults[key] != value) options[key] = value
        })

        return options
    }

    Tooltip.prototype.enter = function (obj) {
        var self = obj instanceof this.constructor ?
            obj : $(obj.currentTarget).data('bs.' + this.type)

        if (self && self.$tip && self.$tip.is(':visible')) {
            self.hoverState = 'in'
            return
        }

        if (!self) {
            self = new this.constructor(obj.currentTarget, this.getDelegateOptions())
            $(obj.currentTarget).data('bs.' + this.type, self)
        }

        clearTimeout(self.timeout)

        self.hoverState = 'in'

        if (!self.options.delay || !self.options.delay.show) return self.show()

        self.timeout = setTimeout(function () {
            if (self.hoverState == 'in') self.show()
        }, self.options.delay.show)
    }

    Tooltip.prototype.leave = function (obj) {
        var self = obj instanceof this.constructor ?
            obj : $(obj.currentTarget).data('bs.' + this.type)

        if (!self) {
            self = new this.constructor(obj.currentTarget, this.getDelegateOptions())
            $(obj.currentTarget).data('bs.' + this.type, self)
        }

        clearTimeout(self.timeout)

        self.hoverState = 'out'

        if (!self.options.delay || !self.options.delay.hide) return self.hide()

        self.timeout = setTimeout(function () {
            if (self.hoverState == 'out') self.hide()
        }, self.options.delay.hide)
    }

    Tooltip.prototype.show = function () {
        var e = $.Event('show.bs.' + this.type)

        if (this.hasContent() && this.enabled) {
            this.$element.trigger(e)

            var inDom = $.contains(this.$element[0].ownerDocument.documentElement,
                this.$element[0])
            if (e.isDefaultPrevented() || !inDom) return
            var that = this

            var $tip = this.tip()

            var tipId = this.getUID(this.type)

            this.setContent()
            $tip.attr('id', tipId)
            this.$element.attr('aria-describedby', tipId)

            if (this.options.animation) $tip.addClass('fade')

            var placement = typeof this.options.placement == 'function' ?
                this.options.placement.call(this, $tip[0], this.$element[0]) :
                this.options.placement

            var autoToken = /\s?auto?\s?/i
            var autoPlace = autoToken.test(placement)
            if (autoPlace) placement = placement.replace(autoToken, '') || 'top'

            $tip
                .detach()
                .css({
                    top: 0,
                    left: 0,
                    display: 'block'
                })
                .addClass(placement)
                .data('bs.' + this.type, this)

            this.options.container ? $tip.appendTo(this.options.container) : $tip.insertAfter(
                this.$element)

            var pos = this.getPosition()
            var actualWidth = $tip[0].offsetWidth
            var actualHeight = $tip[0].offsetHeight

            if (autoPlace) {
                var orgPlacement = placement
                var $container = this.options.container ? $(this.options.container) :
                    this.$element.parent()
                var containerDim = this.getPosition($container)

                placement = placement == 'bottom' && pos.bottom + actualHeight >
                containerDim.bottom ? 'top' :
                    placement == 'top' && pos.top - actualHeight < containerDim.top ?
                        'bottom' :
                        placement == 'right' && pos.right + actualWidth > containerDim.width ?
                            'left' :
                            placement == 'left' && pos.left - actualWidth < containerDim.left ?
                                'right' :
                                placement

                $tip
                    .removeClass(orgPlacement)
                    .addClass(placement)
            }

            var calculatedOffset = this.getCalculatedOffset(placement, pos,
                actualWidth, actualHeight)

            this.applyPlacement(calculatedOffset, placement)

            var complete = function () {
                var prevHoverState = that.hoverState
                that.$element.trigger('shown.bs.' + that.type)
                that.hoverState = null

                if (prevHoverState == 'out') that.leave(that)
            }

            $.support.transition && this.$tip.hasClass('fade') ?
                $tip
                    .one('bsTransitionEnd', complete)
                    .emulateTransitionEnd(Tooltip.TRANSITION_DURATION) :
                complete()
        }
    }

    Tooltip.prototype.applyPlacement = function (offset, placement) {
        var $tip = this.tip()
        var width = $tip[0].offsetWidth
        var height = $tip[0].offsetHeight

        // manually read margins because getBoundingClientRect includes difference
        var marginTop = parseInt($tip.css('margin-top'), 10)
        var marginLeft = parseInt($tip.css('margin-left'), 10)

        // we must check for NaN for ie 8/9
        if (isNaN(marginTop)) marginTop = 0
        if (isNaN(marginLeft)) marginLeft = 0

        offset.top = offset.top + marginTop
        offset.left = offset.left + marginLeft

        // $.fn.offset doesn't round pixel values
        // so we use setOffset directly with our own function B-0
        $.offset.setOffset($tip[0], $.extend({
            using: function (props) {
                $tip.css({
                    top: Math.round(props.top),
                    left: Math.round(props.left)
                })
            }
        }, offset), 0)

        $tip.addClass('in')

        // check to see if placing tip in new offset caused the tip to resize itself
        var actualWidth = $tip[0].offsetWidth
        var actualHeight = $tip[0].offsetHeight

        if (placement == 'top' && actualHeight != height) {
            offset.top = offset.top + height - actualHeight
        }

        var delta = this.getViewportAdjustedDelta(placement, offset, actualWidth,
            actualHeight)

        if (delta.left) offset.left += delta.left
        else offset.top += delta.top

        var isVertical = /top|bottom/.test(placement)
        var arrowDelta = isVertical ? delta.left * 2 - width + actualWidth :
            delta.top * 2 - height + actualHeight
        var arrowOffsetPosition = isVertical ? 'offsetWidth' : 'offsetHeight'

        $tip.offset(offset)
        this.replaceArrow(arrowDelta, $tip[0][arrowOffsetPosition], isVertical)
    }

    Tooltip.prototype.replaceArrow = function (delta, dimension, isHorizontal) {
        this.arrow()
            .css(isHorizontal ? 'left' : 'top', 50 * (1 - delta / dimension) + '%')
            .css(isHorizontal ? 'top' : 'left', '')
    }

    Tooltip.prototype.setContent = function () {
        var $tip = this.tip()
        var title = this.getTitle()

        $tip.find('.tooltip-inner')[this.options.html ? 'html' : 'text'](title)
        $tip.removeClass('fade in top bottom left right')
    }

    Tooltip.prototype.hide = function (callback) {
        var that = this
        var $tip = this.tip()
        var e = $.Event('hide.bs.' + this.type)

        function complete() {
            if (that.hoverState != 'in') $tip.detach()
            that.$element
                .removeAttr('aria-describedby')
                .trigger('hidden.bs.' + that.type)
            callback && callback()
        }

        this.$element.trigger(e)

        if (e.isDefaultPrevented()) return

        $tip.removeClass('in')

        $.support.transition && this.$tip.hasClass('fade') ?
            $tip
                .one('bsTransitionEnd', complete)
                .emulateTransitionEnd(Tooltip.TRANSITION_DURATION) :
            complete()

        this.hoverState = null

        return this
    }

    Tooltip.prototype.fixTitle = function () {
        var $e = this.$element
        if ($e.attr('title') || typeof($e.attr('data-original-title')) !=
            'string') {
            $e.attr('data-original-title', $e.attr('title') || '').attr('title', '')
        }
    }

    Tooltip.prototype.hasContent = function () {
        return this.getTitle()
    }

    Tooltip.prototype.getPosition = function ($element) {
        $element = $element || this.$element

        var el = $element[0]
        var isBody = el.tagName == 'BODY'

        var elRect = el.getBoundingClientRect()
        if (elRect.width == null) {
            // width and height are missing in IE8, so compute them manually; see https://github.com/twbs/bootstrap/issues/14093
            elRect = $.extend({}, elRect, {
                width: elRect.right - elRect.left,
                height: elRect.bottom - elRect.top
            })
        }
        var elOffset = isBody ? {
            top: 0,
            left: 0
        } : $element.offset()
        var scroll = {
            scroll: isBody ? document.documentElement.scrollTop || document.body.scrollTop : $element
                .scrollTop()
        }
        var outerDims = isBody ? {
            width: $(window).width(),
            height: $(window).height()
        } : null

        return $.extend({}, elRect, scroll, outerDims, elOffset)
    }

    Tooltip.prototype.getCalculatedOffset = function (placement, pos, actualWidth,
                                                      actualHeight) {
        return placement == 'bottom' ? {
                top: pos.top + pos.height,
                left: pos.left + pos.width / 2 - actualWidth / 2
            } :
            placement == 'top' ? {
                    top: pos.top - actualHeight,
                    left: pos.left + pos.width / 2 - actualWidth / 2
                } :
                placement == 'left' ? {
                        top: pos.top + pos.height / 2 - actualHeight / 2,
                        left: pos.left - actualWidth
                    } :
                    /* placement == 'right' */
                    {
                        top: pos.top + pos.height / 2 - actualHeight / 2,
                        left: pos.left + pos.width
                    }

    }

    Tooltip.prototype.getViewportAdjustedDelta = function (placement, pos,
                                                           actualWidth, actualHeight) {
        var delta = {
            top: 0,
            left: 0
        }
        if (!this.$viewport) return delta

        var viewportPadding = this.options.viewport && this.options.viewport.padding ||
            0
        var viewportDimensions = this.getPosition(this.$viewport)

        if (/right|left/.test(placement)) {
            var topEdgeOffset = pos.top - viewportPadding - viewportDimensions.scroll
            var bottomEdgeOffset = pos.top + viewportPadding - viewportDimensions.scroll +
                actualHeight
            if (topEdgeOffset < viewportDimensions.top) { // top overflow
                delta.top = viewportDimensions.top - topEdgeOffset
            } else if (bottomEdgeOffset > viewportDimensions.top +
                viewportDimensions.height) { // bottom overflow
                delta.top = viewportDimensions.top + viewportDimensions.height -
                    bottomEdgeOffset
            }
        } else {
            var leftEdgeOffset = pos.left - viewportPadding
            var rightEdgeOffset = pos.left + viewportPadding + actualWidth
            if (leftEdgeOffset < viewportDimensions.left) { // left overflow
                delta.left = viewportDimensions.left - leftEdgeOffset
            } else if (rightEdgeOffset > viewportDimensions.width) { // right overflow
                delta.left = viewportDimensions.left + viewportDimensions.width -
                    rightEdgeOffset
            }
        }

        return delta
    }

    Tooltip.prototype.getTitle = function () {
        var title
        var $e = this.$element
        var o = this.options

        title = $e.attr('data-original-title') || (typeof o.title == 'function' ?
            o.title.call($e[0]) : o.title)

        return title
    }

    Tooltip.prototype.getUID = function (prefix) {
        do prefix += ~~(Math.random() * 1000000)
        while (document.getElementById(prefix))
        return prefix
    }

    Tooltip.prototype.tip = function () {
        return (this.$tip = this.$tip || $(this.options.template))
    }

    Tooltip.prototype.arrow = function () {
        return (this.$arrow = this.$arrow || this.tip().find('.tooltip-arrow'))
    }

    Tooltip.prototype.enable = function () {
        this.enabled = true
    }

    Tooltip.prototype.disable = function () {
        this.enabled = false
    }

    Tooltip.prototype.toggleEnabled = function () {
        this.enabled = !this.enabled
    }

    Tooltip.prototype.toggle = function (e) {
        var self = this
        if (e) {
            self = $(e.currentTarget).data('bs.' + this.type)
            if (!self) {
                self = new this.constructor(e.currentTarget, this.getDelegateOptions())
                $(e.currentTarget).data('bs.' + this.type, self)
            }
        }

        self.tip().hasClass('in') ? self.leave(self) : self.enter(self)
    }

    Tooltip.prototype.destroy = function () {
        var that = this
        clearTimeout(this.timeout)
        this.hide(function () {
            that.$element.off('.' + that.type).removeData('bs.' + that.type)
        })
    }


    // TOOLTIP PLUGIN DEFINITION
    // =========================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.tooltip')
            var options = typeof option == 'object' && option
            var selector = options && options.selector

            if (!data && option == 'destroy') return
            if (selector) {
                if (!data) $this.data('bs.tooltip', (data = {}))
                if (!data[selector]) data[selector] = new Tooltip(this, options)
            } else {
                if (!data) $this.data('bs.tooltip', (data = new Tooltip(this,
                    options)))
            }
            if (typeof option == 'string') data[option]()
        })
    }

    var old = $.fn.tooltip

    $.fn.tooltip = Plugin
    $.fn.tooltip.Constructor = Tooltip


    // TOOLTIP NO CONFLICT
    // ===================

    $.fn.tooltip.noConflict = function () {
        $.fn.tooltip = old
        return this
    }

}(jQuery);

/* ========================================================================
 * Bootstrap: popover.js v3.3.1
 * http://getbootstrap.com/javascript/#popovers
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // POPOVER PUBLIC CLASS DEFINITION
    // ===============================

    var Popover = function (element, options) {
        this.init('popover', element, options)
    }

    if (!$.fn.tooltip) throw new Error('Popover requires tooltip.js')

    Popover.VERSION = '3.3.1'

    Popover.DEFAULTS = $.extend({}, $.fn.tooltip.Constructor.DEFAULTS, {
        placement: 'right',
        trigger: 'click',
        content: '',
        template: '<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title"></h3><div class="popover-content"></div></div>'
    })


    // NOTE: POPOVER EXTENDS tooltip.js
    // ================================

    Popover.prototype = $.extend({}, $.fn.tooltip.Constructor.prototype)

    Popover.prototype.constructor = Popover

    Popover.prototype.getDefaults = function () {
        return Popover.DEFAULTS
    }

    Popover.prototype.setContent = function () {
        var $tip = this.tip()
        var title = this.getTitle()
        var content = this.getContent()

        $tip.find('.popover-title')[this.options.html ? 'html' : 'text'](title)
        $tip.find('.popover-content').children().detach().end()[ // we use append for html objects to maintain js events
            this.options.html ? (typeof content == 'string' ? 'html' : 'append') :
                'text'
            ](content)

        $tip.removeClass('fade top bottom left right in')

        // IE8 doesn't accept hiding via the `:empty` pseudo selector, we have to do
        // this manually by checking the contents.
        if (!$tip.find('.popover-title').html()) $tip.find('.popover-title').hide()
    }

    Popover.prototype.hasContent = function () {
        return this.getTitle() || this.getContent()
    }

    Popover.prototype.getContent = function () {
        var $e = this.$element
        var o = this.options

        return $e.attr('data-content') || (typeof o.content == 'function' ?
            o.content.call($e[0]) :
            o.content)
    }

    Popover.prototype.arrow = function () {
        return (this.$arrow = this.$arrow || this.tip().find('.arrow'))
    }

    Popover.prototype.tip = function () {
        if (!this.$tip) this.$tip = $(this.options.template)
        return this.$tip
    }


    // POPOVER PLUGIN DEFINITION
    // =========================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.popover')
            var options = typeof option == 'object' && option
            var selector = options && options.selector

            if (!data && option == 'destroy') return
            if (selector) {
                if (!data) $this.data('bs.popover', (data = {}))
                if (!data[selector]) data[selector] = new Popover(this, options)
            } else {
                if (!data) $this.data('bs.popover', (data = new Popover(this,
                    options)))
            }
            if (typeof option == 'string') data[option]()
        })
    }

    var old = $.fn.popover

    $.fn.popover = Plugin
    $.fn.popover.Constructor = Popover


    // POPOVER NO CONFLICT
    // ===================

    $.fn.popover.noConflict = function () {
        $.fn.popover = old
        return this
    }

}(jQuery);

/* ========================================================================
 * Bootstrap: scrollspy.js v3.3.1
 * http://getbootstrap.com/javascript/#scrollspy
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // SCROLLSPY CLASS DEFINITION
    // ==========================

    function ScrollSpy(element, options) {
        var process = $.proxy(this.process, this)

        this.$body = $('body')
        this.$scrollElement = $(element).is('body') ? $(window) : $(element)
        this.options = $.extend({}, ScrollSpy.DEFAULTS, options)
        this.selector = (this.options.target || '') + ' .nav li > a'
        this.offsets = []
        this.targets = []
        this.activeTarget = null
        this.scrollHeight = 0

        this.$scrollElement.on('scroll.bs.scrollspy', process)
        this.refresh()
        this.process()
    }

    ScrollSpy.VERSION = '3.3.1'

    ScrollSpy.DEFAULTS = {
        offset: 10
    }

    ScrollSpy.prototype.getScrollHeight = function () {
        return this.$scrollElement[0].scrollHeight || Math.max(this.$body[0].scrollHeight,
            document.documentElement.scrollHeight)
    }

    ScrollSpy.prototype.refresh = function () {
        var offsetMethod = 'offset'
        var offsetBase = 0

        if (!$.isWindow(this.$scrollElement[0])) {
            offsetMethod = 'position'
            offsetBase = this.$scrollElement.scrollTop()
        }

        this.offsets = []
        this.targets = []
        this.scrollHeight = this.getScrollHeight()

        var self = this

        this.$body
            .find(this.selector)
            .map(function () {
                var $el = $(this)
                var href = $el.data('target') || $el.attr('href')
                var $href = /^#./.test(href) && $(href)

                return ($href && $href.length && $href.is(':visible') && [
                    [$href[offsetMethod]().top + offsetBase, href]
                ]) || null
            })
            .sort(function (a, b) {
                return a[0] - b[0]
            })
            .each(function () {
                self.offsets.push(this[0])
                self.targets.push(this[1])
            })
    }

    ScrollSpy.prototype.process = function () {
        var scrollTop = this.$scrollElement.scrollTop() + this.options.offset
        var scrollHeight = this.getScrollHeight()
        var maxScroll = this.options.offset + scrollHeight - this.$scrollElement.height()
        var offsets = this.offsets
        var targets = this.targets
        var activeTarget = this.activeTarget
        var i

        if (this.scrollHeight != scrollHeight) {
            this.refresh()
        }

        if (scrollTop >= maxScroll) {
            return activeTarget != (i = targets[targets.length - 1]) && this.activate(
                i)
        }

        if (activeTarget && scrollTop < offsets[0]) {
            this.activeTarget = null
            return this.clear()
        }

        for (i = offsets.length; i--;) {
            activeTarget != targets[i] && scrollTop >= offsets[i] && (!offsets[i +
            1] || scrollTop <= offsets[i + 1]) && this.activate(targets[i])
        }
    }

    ScrollSpy.prototype.activate = function (target) {
        this.activeTarget = target

        this.clear()

        var selector = this.selector +
            '[data-target="' + target + '"],' +
            this.selector + '[href="' + target + '"]'

        var active = $(selector)
            .parents('li')
            .addClass('active')

        if (active.parent('.dropdown-menu').length) {
            active = active
                .closest('li.dropdown')
                .addClass('active')
        }

        active.trigger('activate.bs.scrollspy')
    }

    ScrollSpy.prototype.clear = function () {
        $(this.selector)
            .parentsUntil(this.options.target, '.active')
            .removeClass('active')
    }


    // SCROLLSPY PLUGIN DEFINITION
    // ===========================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.scrollspy')
            var options = typeof option == 'object' && option

            if (!data) $this.data('bs.scrollspy', (data = new ScrollSpy(this,
                options)))
            if (typeof option == 'string') data[option]()
        })
    }

    var old = $.fn.scrollspy

    $.fn.scrollspy = Plugin
    $.fn.scrollspy.Constructor = ScrollSpy


    // SCROLLSPY NO CONFLICT
    // =====================

    $.fn.scrollspy.noConflict = function () {
        $.fn.scrollspy = old
        return this
    }


    // SCROLLSPY DATA-API
    // ==================

    $(window).on('load.bs.scrollspy.data-api', function () {
        $('[data-spy="scroll"]').each(function () {
            var $spy = $(this)
            Plugin.call($spy, $spy.data())
        })
    })

}(jQuery);

/* ========================================================================
 * Bootstrap: tab.js v3.3.1
 * http://getbootstrap.com/javascript/#tabs
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // TAB CLASS DEFINITION
    // ====================

    var Tab = function (element) {
        this.element = $(element)
    }

    Tab.VERSION = '3.3.1'

    Tab.TRANSITION_DURATION = 150

    Tab.prototype.show = function () {
        var $this = this.element
        var $ul = $this.closest('ul:not(.dropdown-menu)')
        var selector = $this.data('target')

        if (!selector) {
            selector = $this.attr('href')
            selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
        }

        if ($this.parent('li').hasClass('active')) return

        var $previous = $ul.find('.active:last a')
        var hideEvent = $.Event('hide.bs.tab', {
            relatedTarget: $this[0]
        })
        var showEvent = $.Event('show.bs.tab', {
            relatedTarget: $previous[0]
        })

        $previous.trigger(hideEvent)
        $this.trigger(showEvent)

        if (showEvent.isDefaultPrevented() || hideEvent.isDefaultPrevented())
            return

        var $target = $(selector)

        this.activate($this.closest('li'), $ul)
        this.activate($target, $target.parent(), function () {
            $previous.trigger({
                type: 'hidden.bs.tab',
                relatedTarget: $this[0]
            })
            $this.trigger({
                type: 'shown.bs.tab',
                relatedTarget: $previous[0]
            })
        })
    }

    Tab.prototype.activate = function (element, container, callback) {
        var $active = container.find('> .active')
        var transition = callback && $.support.transition && (($active.length &&
            $active.hasClass('fade')) || !!container.find('> .fade').length)

        function next() {
            $active
                .removeClass('active')
                .find('> .dropdown-menu > .active')
                .removeClass('active')
                .end()
                .find('[data-toggle="tab"]')
                .attr('aria-expanded', false)

            element
                .addClass('active')
                .find('[data-toggle="tab"]')
                .attr('aria-expanded', true)

            if (transition) {
                element[0].offsetWidth // reflow for transition
                element.addClass('in')
            } else {
                element.removeClass('fade')
            }

            if (element.parent('.dropdown-menu')) {
                element
                    .closest('li.dropdown')
                    .addClass('active')
                    .end()
                    .find('[data-toggle="tab"]')
                    .attr('aria-expanded', true)
            }

            callback && callback()
        }

        $active.length && transition ?
            $active
                .one('bsTransitionEnd', next)
                .emulateTransitionEnd(Tab.TRANSITION_DURATION) :
            next()

        $active.removeClass('in')
    }


    // TAB PLUGIN DEFINITION
    // =====================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.tab')

            if (!data) $this.data('bs.tab', (data = new Tab(this)))
            if (typeof option == 'string') data[option]()
        })
    }

    var old = $.fn.tab

    $.fn.tab = Plugin
    $.fn.tab.Constructor = Tab


    // TAB NO CONFLICT
    // ===============

    $.fn.tab.noConflict = function () {
        $.fn.tab = old
        return this
    }


    // TAB DATA-API
    // ============

    var clickHandler = function (e) {
        e.preventDefault()
        Plugin.call($(this), 'show')
    }

    $(document)
        .on('click.bs.tab.data-api', '[data-toggle="tab"]', clickHandler)
        .on('click.bs.tab.data-api', '[data-toggle="pill"]', clickHandler)

}(jQuery);

/* ========================================================================
 * Bootstrap: affix.js v3.3.1
 * http://getbootstrap.com/javascript/#affix
 * ========================================================================
 * Copyright 2011-2014 Twitter, Inc.
 * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
 * ======================================================================== */


+function ($) {
    'use strict';

    // AFFIX CLASS DEFINITION
    // ======================

    var Affix = function (element, options) {
        this.options = $.extend({}, Affix.DEFAULTS, options)

        this.$target = $(this.options.target)
            .on('scroll.bs.affix.data-api', $.proxy(this.checkPosition, this))
            .on('click.bs.affix.data-api', $.proxy(this.checkPositionWithEventLoop,
                this))

        this.$element = $(element)
        this.affixed =
            this.unpin =
                this.pinnedOffset = null

        this.checkPosition()
    }

    Affix.VERSION = '3.3.1'

    Affix.RESET = 'affix affix-top affix-bottom'

    Affix.DEFAULTS = {
        offset: 0,
        target: window
    }

    Affix.prototype.getState = function (scrollHeight, height, offsetTop,
                                         offsetBottom) {
        var scrollTop = this.$target.scrollTop()
        var position = this.$element.offset()
        var targetHeight = this.$target.height()

        if (offsetTop != null && this.affixed == 'top') return scrollTop <
        offsetTop ? 'top' : false

        if (this.affixed == 'bottom') {
            if (offsetTop != null) return (scrollTop + this.unpin <= position.top) ?
                false : 'bottom'
            return (scrollTop + targetHeight <= scrollHeight - offsetBottom) ?
                false : 'bottom'
        }

        var initializing = this.affixed == null
        var colliderTop = initializing ? scrollTop : position.top
        var colliderHeight = initializing ? targetHeight : height

        if (offsetTop != null && colliderTop <= offsetTop) return 'top'
        if (offsetBottom != null && (colliderTop + colliderHeight >= scrollHeight -
                offsetBottom)) return 'bottom'

        return false
    }

    Affix.prototype.getPinnedOffset = function () {
        if (this.pinnedOffset) return this.pinnedOffset
        this.$element.removeClass(Affix.RESET).addClass('affix')
        var scrollTop = this.$target.scrollTop()
        var position = this.$element.offset()
        return (this.pinnedOffset = position.top - scrollTop)
    }

    Affix.prototype.checkPositionWithEventLoop = function () {
        setTimeout($.proxy(this.checkPosition, this), 1)
    }

    Affix.prototype.checkPosition = function () {
        if (!this.$element.is(':visible')) return

        var height = this.$element.height()
        var offset = this.options.offset
        var offsetTop = offset.top
        var offsetBottom = offset.bottom
        var scrollHeight = $('body').height()

        if (typeof offset != 'object') offsetBottom = offsetTop = offset
        if (typeof offsetTop == 'function') offsetTop = offset.top(this.$element)
        if (typeof offsetBottom == 'function') offsetBottom = offset.bottom(this.$element)

        var affix = this.getState(scrollHeight, height, offsetTop, offsetBottom)

        if (this.affixed != affix) {
            if (this.unpin != null) this.$element.css('top', '')

            var affixType = 'affix' + (affix ? '-' + affix : '')
            var e = $.Event(affixType + '.bs.affix')

            this.$element.trigger(e)

            if (e.isDefaultPrevented()) return

            this.affixed = affix
            this.unpin = affix == 'bottom' ? this.getPinnedOffset() : null

            this.$element
                .removeClass(Affix.RESET)
                .addClass(affixType)
                .trigger(affixType.replace('affix', 'affixed') + '.bs.affix')
        }

        if (affix == 'bottom') {
            this.$element.offset({
                top: scrollHeight - height - offsetBottom
            })
        }
    }


    // AFFIX PLUGIN DEFINITION
    // =======================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('bs.affix')
            var options = typeof option == 'object' && option

            if (!data) $this.data('bs.affix', (data = new Affix(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    var old = $.fn.affix

    $.fn.affix = Plugin
    $.fn.affix.Constructor = Affix


    // AFFIX NO CONFLICT
    // =================

    $.fn.affix.noConflict = function () {
        $.fn.affix = old
        return this
    }


    // AFFIX DATA-API
    // ==============

    $(window).on('load', function () {
        $('[data-spy="affix"]').each(function () {
            var $spy = $(this)
            var data = $spy.data()

            data.offset = data.offset || {}

            if (data.offsetBottom != null) data.offset.bottom = data.offsetBottom
            if (data.offsetTop != null) data.offset.top = data.offsetTop

            Plugin.call($spy, data)
        })
    })

}(jQuery);


/*!
 SerializeJSON jQuery plugin.
 将form 中的ipnut转换成json对象
 https://github.com/marioizquierdo/jquery.serializeJSON
 version 2.6.2 (May, 2015)
 Copyright (c) 2012, 2015 Mario Izquierdo
 Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) { // AMD. Register as an anonymous module.
        define(['jquery'], factory);
    } else if (typeof exports === 'object') { // Node/CommonJS
        var jQuery = require('jquery');
        module.exports = factory(jQuery);
    } else { // Browser globals (zepto supported)
        factory(window.jQuery || window.Zepto || window.$); // Zepto supported on browsers as well
    }

}(function ($) {
    "use strict";

    // jQuery('form').serializeJSON()
    $.fn.serializeJSON = function (options) {
        var serializedObject, formAsArray, keys, type, value, _ref, f, opts;
        f = $.serializeJSON;
        opts = f.setupOpts(options); // calculate values for options {parseNumbers, parseBoolens, parseNulls}
        formAsArray = this.serializeArray(); // array of objects {name, value}
        f.readCheckboxUncheckedValues(formAsArray, this, opts); // add {name, value} of unchecked checkboxes if needed

        serializedObject = {};
        $.each(formAsArray, function (i, input) {
            keys = f.splitInputNameIntoKeysArray(input.name, opts);
            type = keys.pop(); // the last element is always the type ("string" by default)
            if (type !== 'skip') { // easy way to skip a value
                value = f.parseValue(input.value, type, opts); // string, number, boolean or null
                if (opts.parseWithFunction && type === '_') { // allow for custom parsing
                    value = opts.parseWithFunction(value, input.name);
                }
                f.deepSet(serializedObject, keys, value, opts);
            }
        });
        return serializedObject;
    };

    // Use $.serializeJSON as namespace for the auxiliar functions
    // and to define defaults
    $.serializeJSON = {

        defaultOptions: {
            checkboxUncheckedValue: undefined, // to include that value for unchecked checkboxes (instead of ignoring them)

            parseNumbers: false, // convert values like "1", "-2.33" to 1, -2.33
            parseBooleans: false, // convert "true", "false" to true, false
            parseNulls: false, // convert "null" to null
            parseAll: false, // all of the above
            parseWithFunction: null, // to use custom parser, a function like: function(val){ return parsed_val; }

            customTypes: {}, // override defaultTypes
            defaultTypes: {
                "string": function (str) {
                    return String(str);
                },
                "number": function (str) {
                    return Number(str);
                },
                "boolean": function (str) {
                    var falses = ["false", "null", "undefined", "", "0"];
                    return falses.indexOf(str) === -1;
                },
                "null": function (str) {
                    var falses = ["false", "null", "undefined", "", "0"];
                    return falses.indexOf(str) === -1 ? str : null;
                },
                "array": function (str) {
                    return JSON.parse(str);
                },
                "object": function (str) {
                    return JSON.parse(str);
                },
                "auto": function (str) {
                    return $.serializeJSON.parseValue(str, null, {
                        parseNumbers: true,
                        parseBooleans: true,
                        parseNulls: true
                    });
                } // try again with something like "parseAll"
            },

            useIntKeysAsArrayIndex: false // name="foo[2]" value="v" => {foo: [null, null, "v"]}, instead of {foo: ["2": "v"]}
        },

        // Merge option defaults into the options
        setupOpts: function (options) {
            var opt, validOpts, defaultOptions, optWithDefault, parseAll, f;
            f = $.serializeJSON;

            if (options == null) {
                options = {};
            } // options ||= {}
            defaultOptions = f.defaultOptions || {}; // defaultOptions

            // Make sure that the user didn't misspell an option
            validOpts = ['checkboxUncheckedValue', 'parseNumbers',
                'parseBooleans', 'parseNulls', 'parseAll',
                'parseWithFunction', 'customTypes', 'defaultTypes',
                'useIntKeysAsArrayIndex'
            ]; // re-define because the user may override the defaultOptions
            for (opt in options) {
                if (validOpts.indexOf(opt) === -1) {
                    throw new Error("serializeJSON ERROR: invalid option '" + opt +
                        "'. Please use one of " + validOpts.join(', '));
                }
            }

            // Helper to get the default value for this option if none is specified by the user
            optWithDefault = function (key) {
                return (options[key] !== false) && (options[key] !== '') && (
                    options[key] || defaultOptions[key]);
            };

            // Return computed options (opts to be used in the rest of the script)
            parseAll = optWithDefault('parseAll');
            return {
                checkboxUncheckedValue: optWithDefault('checkboxUncheckedValue'),

                parseNumbers: parseAll || optWithDefault('parseNumbers'),
                parseBooleans: parseAll || optWithDefault('parseBooleans'),
                parseNulls: parseAll || optWithDefault('parseNulls'),
                parseWithFunction: optWithDefault('parseWithFunction'),

                typeFunctions: $.extend({}, optWithDefault('defaultTypes'),
                    optWithDefault('customTypes')),

                useIntKeysAsArrayIndex: optWithDefault('useIntKeysAsArrayIndex')
            };
        },

        // Given a string, apply the type or the relevant "parse" options, to return the parsed value
        parseValue: function (str, type, opts) {
            var typeFunction, f;
            f = $.serializeJSON;

            // Parse with a type if available
            typeFunction = opts.typeFunctions && opts.typeFunctions[type];
            if (typeFunction) {
                return typeFunction(str);
            } // use specific type

            // Otherwise, check if there is any auto-parse option enabled and use it.
            if (opts.parseNumbers && f.isNumeric(str)) {
                return Number(str);
            } // auto: number
            if (opts.parseBooleans && (str === "true" || str === "false")) {
                return str === "true";
            } // auto: boolean
            if (opts.parseNulls && str == "null") {
                return null;
            } // auto: null

            // If none applies, just return the str
            return str;
        },

        isObject: function (obj) {
            return obj === Object(obj);
        }, // is it an Object?
        isUndefined: function (obj) {
            return obj === void 0;
        }, // safe check for undefined values
        isValidArrayIndex: function (val) {
            return /^[0-9]+$/.test(String(val));
        }, // 1,2,3,4 ... are valid array indexes
        isNumeric: function (obj) {
            return obj - parseFloat(obj) >= 0;
        }, // taken from jQuery.isNumeric implementation. Not using jQuery.isNumeric to support old jQuery and Zepto versions

        optionKeys: function (obj) {
            if (Object.keys) {
                return Object.keys(obj);
            } else {
                var key, keys = [];
                for (key in obj) {
                    keys.push(key);
                }
                return keys;
            }
        }, // polyfill Object.keys to get option keys in IE<9

        // Split the input name in programatically readable keys.
        // The last element is always the type (default "_").
        // Examples:
        // "foo"              => ['foo', '_']
        // "foo:string"       => ['foo', 'string']
        // "foo:boolean"      => ['foo', 'boolean']
        // "[foo]"            => ['foo', '_']
        // "foo[inn][bar]"    => ['foo', 'inn', 'bar', '_']
        // "foo[inn[bar]]"    => ['foo', 'inn', 'bar', '_']
        // "foo[inn][arr][0]" => ['foo', 'inn', 'arr', '0', '_']
        // "arr[][val]"       => ['arr', '', 'val', '_']
        // "arr[][val]:null"  => ['arr', '', 'val', 'null']
        splitInputNameIntoKeysArray: function (name, opts) {
            var keys, nameWithoutType, type, _ref, f;
            f = $.serializeJSON;
            _ref = f.extractTypeFromInputName(name, opts);
            nameWithoutType = _ref[0];
            type = _ref[1];
            keys = nameWithoutType.split('['); // split string into array
            keys = $.map(keys, function (key) {
                return key.replace(/\]/g, '');
            }); // remove closing brackets
            if (keys[0] === '') {
                keys.shift();
            } // ensure no opening bracket ("[foo][inn]" should be same as "foo[inn]")
            keys.push(type); // add type at the end
            return keys;
        },

        // Returns [name-without-type, type] from name.
        // "foo"              =>  ["foo",      '_']
        // "foo:boolean"      =>  ["foo",      'boolean']
        // "foo[bar]:null"    =>  ["foo[bar]", 'null']
        extractTypeFromInputName: function (name, opts) {
            var match, validTypes, f;
            if (match = name.match(/(.*):([^:]+)$/)) {
                f = $.serializeJSON;

                validTypes = f.optionKeys(opts ? opts.typeFunctions : f.defaultOptions
                    .defaultTypes);
                validTypes.push('skip'); // skip is a special type that makes it easy to remove
                if (validTypes.indexOf(match[2]) !== -1) {
                    return [match[1], match[2]];
                } else {
                    throw new Error("serializeJSON ERROR: Invalid type " + match[
                            2] + " found in input name '" + name +
                        "', please use one of " + validTypes.join(', '));
                }
            } else {
                return [name, '_']; // no defined type, then use parse options
            }
        },

        // Set a value in an object or array, using multiple keys to set in a nested object or array:
        //
        // deepSet(obj, ['foo'], v)               // obj['foo'] = v
        // deepSet(obj, ['foo', 'inn'], v)        // obj['foo']['inn'] = v // Create the inner obj['foo'] object, if needed
        // deepSet(obj, ['foo', 'inn', '123'], v) // obj['foo']['arr']['123'] = v //
        //
        // deepSet(obj, ['0'], v)                                   // obj['0'] = v
        // deepSet(arr, ['0'], v, {useIntKeysAsArrayIndex: true})   // arr[0] = v
        // deepSet(arr, [''], v)                                    // arr.push(v)
        // deepSet(obj, ['arr', ''], v)                             // obj['arr'].push(v)
        //
        // arr = [];
        // deepSet(arr, ['', v]          // arr => [v]
        // deepSet(arr, ['', 'foo'], v)  // arr => [v, {foo: v}]
        // deepSet(arr, ['', 'bar'], v)  // arr => [v, {foo: v, bar: v}]
        // deepSet(arr, ['', 'bar'], v)  // arr => [v, {foo: v, bar: v}, {bar: v}]
        //
        deepSet: function (o, keys, value, opts) {
            var key, nextKey, tail, lastIdx, lastVal, f;
            if (opts == null) {
                opts = {};
            }
            f = $.serializeJSON;
            if (f.isUndefined(o)) {
                throw new Error(
                    "ArgumentError: param 'o' expected to be an object or array, found undefined"
                );
            }
            if (!keys || keys.length === 0) {
                throw new Error(
                    "ArgumentError: param 'keys' expected to be an array with least one element"
                );
            }

            key = keys[0];

            // Only one key, then it's not a deepSet, just assign the value.
            if (keys.length === 1) {
                if (key === '') {
                    o.push(value); // '' is used to push values into the array (assume o is an array)
                } else {
                    o[key] = value; // other keys can be used as object keys or array indexes
                }

                // With more keys is a deepSet. Apply recursively.
            } else {
                nextKey = keys[1];

                // '' is used to push values into the array,
                // with nextKey, set the value into the same object, in object[nextKey].
                // Covers the case of ['', 'foo'] and ['', 'var'] to push the object {foo, var}, and the case of nested arrays.
                if (key === '') {
                    lastIdx = o.length - 1; // asume o is array
                    lastVal = o[lastIdx];
                    if (f.isObject(lastVal) && (f.isUndefined(lastVal[nextKey]) ||
                            keys.length > 2)) { // if nextKey is not present in the last object element, or there are more keys to deep set
                        key = lastIdx; // then set the new value in the same object element
                    } else {
                        key = lastIdx + 1; // otherwise, point to set the next index in the array
                    }
                }

                // '' is used to push values into the array "array[]"
                if (nextKey === '') {
                    if (f.isUndefined(o[key]) || !$.isArray(o[key])) {
                        o[key] = []; // define (or override) as array to push values
                    }
                } else {
                    if (opts.useIntKeysAsArrayIndex && f.isValidArrayIndex(
                            nextKey)) { // if 1, 2, 3 ... then use an array, where nextKey is the index
                        if (f.isUndefined(o[key]) || !$.isArray(o[key])) {
                            o[key] = []; // define (or override) as array, to insert values using int keys as array indexes
                        }
                    } else { // for anything else, use an object, where nextKey is going to be the attribute name
                        if (f.isUndefined(o[key]) || !f.isObject(o[key])) {
                            o[key] = {}; // define (or override) as object, to set nested properties
                        }
                    }
                }

                // Recursively set the inner object
                tail = keys.slice(1);
                f.deepSet(o[key], tail, value, opts);
            }
        },

        // Fill the formAsArray object with values for the unchecked checkbox inputs,
        // using the same format as the jquery.serializeArray function.
        // The value of the unchecked values is determined from the opts.checkboxUncheckedValue
        // and/or the data-unchecked-value attribute of the inputs.
        readCheckboxUncheckedValues: function (formAsArray, $form, opts) {
            var selector, $uncheckedCheckboxes, $el, dataUncheckedValue, f;
            if (opts == null) {
                opts = {};
            }
            f = $.serializeJSON;

            selector =
                'input[type=checkbox][name]:not(:checked):not([disabled])';
            $uncheckedCheckboxes = $form.find(selector).add($form.filter(
                selector));
            $uncheckedCheckboxes.each(function (i, el) {
                $el = $(el);
                dataUncheckedValue = $el.attr('data-unchecked-value');
                if (dataUncheckedValue) { // data-unchecked-value has precedence over option opts.checkboxUncheckedValue
                    formAsArray.push({
                        name: el.name,
                        value: dataUncheckedValue
                    });
                } else {
                    if (!f.isUndefined(opts.checkboxUncheckedValue)) {
                        formAsArray.push({
                            name: el.name,
                            value: opts.checkboxUncheckedValue
                        });
                    }
                }
            });
        }

    };

}));


/* ========================================================================
 * Bootstrap: replaceTpl
 * 自定义简单替换字符串中的字段.
 * bycj
 * 使用举例：
 * var data = {value : '123',text:'abc'}
 * var template  = '<label>{text}</label><input type="text" value="{value}"/>'
 *  console.log($.replaceTpl(template,data))
 *  输出<label>abc</label><input type="text" value="123"/>
 * ======================================================================== */


(function ($) {
    var ReplaceTpl = function (str, o, regexp) {
        return str.replace(regexp || /\\?\{([^{}]+)\}/g, function (match, name) {

            return (o[name] === undefined) ? '' : o[name];

        });
    };
    //绑定到jquery 上面去
    $.ReplaceTpl = ReplaceTpl;
    //扩展到 String 对象上面去
    String.prototype.ncReplaceTpl = function (option) {
        return ReplaceTpl(this, option);
    };
})(jQuery);


/* ========================================================================
 * Bootstrap: ajaxsubmit
 * bycj
 * ======================================================================== */
(function ($) {
    'use strict';

    var AjaxSubmit = function (element, option) {
        var
            $this = $(element),
            that = this,
            target = $this.attr("nc-ajax-submit-target"),
            $form_element = target && ($("#" + target) ? $("#" + target) : $(
                'form[name="' + target + '"]')),
            _action = $form_element && $form_element.attr("action"),
            _method = $form_element && $form_element.attr("method") ?
                $form_element
                    .attr("method").toUpperCase() : 'POST',
            data = $form_element.serialize(),
            $error_element =
                '<div class="alert alert-danger" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button><strong>{{message}}</strong></div>',
            form_parsley, $btn;
        //
        this.options = $.extend({}, AjaxSubmit.settings, option);
        if ('undefined' != typeof window.Parsley) {
            form_parsley = $form_element.parsley()
            if (!form_parsley.validate()) {
                return
            }
        }
        //为form 添加一个自定义事件
        $btn = $this.button('loading')
        $.ajax({
            url: _action,
            dataType: "json",
            data: data,
            type: _method,
            success: function (req) {
                if ('200' == req.code) {
                    //发出一个自定义事件
                    //关闭modal
                    $form_element.closest('.modal').modal('hide')
                    //$('[data-dismiss="modal"]').click();
                    $form_element.triggerHandler("nc.formSubmit.success");
                    if (typeof that.options.successCallback == "function") {
                        //带回发送的数据和回来的数据
                        that.options.successCallback($form_element.serializeJSON(), req);
                    } else {
                        'undefined' != typeof grid && grid.reload(true)
                    }

                } else {
                    var errorPanel = '';
                    errorPanel = $error_element.replace("{{message}}", req.message ? req.message : "操作失败");
                    //清除所有错误提示
                    $form_element.siblings(".alert").remove();
                    $form_element.before(errorPanel)
                    $form_element.triggerHandler("nc.formSubmit.error");
                }
                $btn.button('reset');
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                //请求出错处理
                $btn.button('reset')
                $form_element.siblings(".alert").remove();
                $form_element.before($error_element.replace("{{message}}", "操作失败"))
                $form_element.triggerHandler("nc.formSubmit.error");
            }
        })

    }
    //版本号
    AjaxSubmit.VERSION = '0.0.1';
    //设置
    AjaxSubmit.settings = {
        //成功后的回掉，将不执行刷新列表框
        successCallback: ''

    }

    function Plugin(option) {
        return this.each(function () {
            new AjaxSubmit(this, option)
        })
    }

    $.fn.AjaxSubmit = Plugin
    $.fn.AjaxSubmit.Constructor = AjaxSubmit

    $(document).on('click', '[nc-ajax-submit]', function (e) {
        Plugin.call($(this))
    })


    $(document).on('click', '[nc-ajax-submit-notice]', function (e) {
        Plugin_notice.call($(this))
    })

    function Plugin_notice(option) {
        return this.each(function () {
            new AjaxSubmit_notice(this, option)
        })
    }

    var AjaxSubmit_notice = function (element, option) {
        var
            $this = $(element),
            that = this,
            target = $this.attr("nc-ajax-submit-target-notice"),
            $form_element = target && ($("#" + target) ? $("#" + target) : $(
                'form[name="' + target + '"]')),
            _action = $form_element && $form_element.attr("action"),
            _method = $form_element && $form_element.attr("method") ?
                $form_element
                    .attr("method").toUpperCase() : 'POST',
            data = $form_element.serialize(),
            $error_element =
                '<div class="alert alert-danger" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button><strong>{{message}}</strong></div>',
            form_parsley, $btn;
        //
        this.options = $.extend({}, AjaxSubmit.settings, option);
        if ('undefined' != typeof window.Parsley) {
            form_parsley = $form_element.parsley()
            if (!form_parsley.validate()) {
                return
            }
        }
        //为form 添加一个自定义事件
        $btn = $this.button('loading')
        $.ajax({
            url: _action,
            dataType: "json",
            data: data,
            type: _method,
            success: function (req) {
                if ('200' == req.code) {
                    //发出一个自定义事件
                    //关闭modal
                    $form_element.closest('.modal').modal('hide')
                    if (req.data) {
                        /*var errorPanel = '';
                      errorPanel = $error_element.replace("{{message}}", req.data ? req.data:"操作失败");
                      //清除所有错误提示
                      $form_element.siblings(".alert").remove();
                      console.log($form_element);
                      $form_element.before(errorPanel);
                      $form_element.triggerHandler("nc.formSubmit.error");*/
                        $.ncAlert({
                            content: '<div class="alert alert-danger m-b-0">' +
                            (req.data ? req.data : "操作失败") + '</div>',
                            callback: function () {
                                that.$btn.button('reset');
                            }
                        });
                    }
                    grid1.reload(true)

                } else {
                    var errorPanel = '';
                    errorPanel = $error_element.replace("{{message}}", req.message ? req.message : "操作失败");
                    //清除所有错误提示
                    $form_element.siblings(".alert").remove();
                    $form_element.before(errorPanel)
                    $form_element.triggerHandler("nc.formSubmit.error");
                }
                $btn.button('reset');
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                //请求出错处理
                $btn.button('reset')
                $form_element.siblings(".alert").remove();
                $form_element.before($error_element.replace("{{message}}", "操作失败"))
                $form_element.triggerHandler("nc.formSubmit.error");
            }
        })

    }

})(jQuery);

/**
 * Bootstrap: 多级联动插件
 */
+function ($) {
    'use strict';
    //检测依赖
    var vernums = $.fn.jquery.split('.');
    if (parseInt(vernums[0]) <= 1 && parseInt(vernums[1]) < 9) {
        throw "The loaded version of jQuery is too old. Please upgrade to 1.9.x or better.";
    }
    //if ("undefined" === typeof $.fn.selectpicker){
    //    throw "The bootstrap-select.js is not find."
    //}

    var NcCategory = function (element, option) {

        this.$element = $(element);
        //TODO:这里如果不指定id 的话 应该加一个随机
        //获取id
        this.$elementId = this.$element.attr("id");
        this.options = $.extend({}, NcCategory.setting, option);

        this.it = {}
        this.selit = {};

        if (this.$element.find('select').length == 0) {
            //如果下面没有select 元素就初始化一下
            this.init();
        }
    }
    /**
     * 初始化，没有发现有select
     */
    NcCategory.prototype = {

        _setSelIt: function (deep, ob) {
            for (var i = 10; i >= deep; i--) {
                delete this.selit[i];
            }
            'undefined' != typeof ob && (this.selit[deep] = ob);
        },
        _setIt: function (ob) {
            this.it[this.$$area_deep_id] = ob;
        },
        _getInfo: function (deep, value) {
            var d = this.it[deep],
                r;
            if ("undefined" != typeof d) {
                $.each(d, function (i, n) {
                    n.categoryId == value && (r = n)
                })
            }
            return r;
        },
        /**
         * 获取最后一个对象
         */
        getLast: function () {
            for (var i = 10; i >= 0; i--) {
                if ('undefined' != typeof this.selit[i]) {
                    return this.selit[i];
                }
            }
        },
        /**
         * 获取全部已选择信息
         */
        getAll: function () {
            return this.selit;
        },
        //获取已选择层级的地区拼接信息
        getAllName: function () {
            var result = [];

            $.each(this.getAll(), function (i, n) {
                result.push(n.categoryName);
            })
            return result;
        },
        /**
         * 获取指定层级的信息
         */
        getInfoFormLevel: function (id) {
            return this.selit[id];
        },
        /**
         * 重新启动
         * 使用举例：$("#goods_class").data("nc.category").restart()
         */
        restart: function (options) {

            this.options = $.extend({}, this.options, options || {initCategoryText: ''});
            //删除所有的
            this.$element.children().remove();
            this.init();
        },
        /**
         * 第一次加载
         */
        init: function () {
            //获取设置的隐藏域
            var hi = this.options.hiddenInput;
            //初始化事件
            this._initEvent();
            //添加隐藏input
            if (hi != '' && typeof hi == 'string') {
                this.$element.append(hi)
            }
            //当前深度
            this.$$area_deep_id = 0;
            //当前 select 选择的 key
            this.$$area_sel_id = 0;
            //当前 select 选择的 value
            this.$$area_sel_html = 0;
            if (this.options.initCategoryText) {
                this.initByCategoryText();
            } else {
                //生成一个 深度为 0 的select
                this.addAreaSelect(0)
                this.$element.prepend('<input data-deep="0" type="hidden" name="' + this.options.dataHiddenName + '1" value="0"  />');
            }

        },
        /**
         * 初始化一个已有分类的
         */
        initByCategoryText: function () {
            var that = this, ops = this.options;
            //声明一个按钮组件
            var modBtn = $(ops.categoryModBtnHtml)
                .on("click", function (e) {
                    $areaModBtnWarp.remove();
                    that.addAreaSelect(0);
                    $.isFunction(ops.modBtnCallBack) && ops.modBtnCallBack(that.$element)
                })

            var $areaModBtnWarp = $(ops.categoryModBtnWarp)
                .html(ops.initCategoryText)
                .append(modBtn);
            this.$element.append($areaModBtnWarp);
            $.isFunction(ops.hasParentIdBootCallBack) && ops.hasParentIdBootCallBack(that.$element)
            //修改隐藏input的值
            // this._setHiddenInput(ops.categoryModBtnAreaId, ops.initCategoryText);
        },

        /**
         * 添加select 元素
         */
        addAreaSelect: function (area_id) {
            var //需要添加元素的html
                html,
                //this 转换
                that = this;
            //判断是否超出限制
            if (this.options.showDeep <= this.$$area_deep_id) {
                //发送末选事件
                this._sendLastCallBack(area_id, this.$$area_sel_html, this.$$area_deep_id);
                return
            }
            //请求数据
            $.getJSON(this.options.url + area_id, {}, function (json) {
                //判断返回的数据
                if ('undefined' == json.code || '200' != json.code) {
                    alert("查询数据失败")
                }
                json = json.data;

                var returnFlat = that.options.dataFormat;

                if ('undefined' === json || 'undefined' === json[returnFlat] ||
                    0 == json[returnFlat].length) {
                    //进入为空处理
                    that._sendLastCallBack(area_id, that.$$area_sel_html, that.$$area_deep_id);
                    return
                }
                //封装数据
                var returnData = json[that.options.dataFormat];
                html = that._elementHtml(returnData)
                //添加元素
                that.$element.append(html)

                //
                that._setIt(returnData)

                //标签上绑定数据
                $("#" + that.$elementId + "_nc_select_" + that.$$area_deep_id)
                    .data("area_deep_id", that.$$area_deep_id)
                    .selectpicker()
            });
        },
        _elementHtml: function (list) {
            var result = '',
                that = this,
                filterIds = this.options.setFilterIds && this.options.setFilterIds();

            //添加第一个
            result += ReplaceTpl(this.options.optionTpl, {
                key: '',
                value: "请选择"
            })
            $.each(list, function (i, n) {
                var nk = 'undefined' != typeof n[that.options.dataIdFormat] ? n[
                        that.options.dataIdFormat] : 0,
                    nn = 'undefined' != typeof n[that.options.dataNameFormat] ? n[
                        that.options.dataNameFormat] : 0,
                    dp = 'undefined' != typeof n.deep ? n.deep : 0;

                result += ReplaceTpl(that.options.optionTpl, {
                    key: nk,
                    value: nn,
                    deep: dp,
                    isDisabled: ( filterIds && filterIds.indexOf(nk) >= 0 ) ? "disabled" : ''
                })
            })

            result = ReplaceTpl(this.options.selectTpl, {
                id: this.$elementId + "_nc_select_" + this.$$area_deep_id,
                content: result
            })
            result = ReplaceTpl(this.options.wrapTpl, {
                value: parseInt(12 / this.options.showDeep),
                content: result
            });
            return result;
        },
        /**
         * 初始化事件(重要)
         * @private
         */
        _initEvent: function () {
            var that = this
            //每个select 绑定事件
            this.$element.off("change", "select").on("change", "select", function () {
                var selectThis = $(this),
                    id = selectThis.val(),
                    area_deep_id = selectThis.data("area_deep_id"),
                    hn = that.options.dataHiddenName + (area_deep_id + 1),
                    _id;

                //当前的选择key 修改
                that.$$area_sel_id = id;

                //修改选择数据
                that._setSelIt(area_deep_id, that._getInfo(area_deep_id, id));

                //清除数据
                that._removeSelect(area_deep_id + 1)

                //添加input
                _id = id || 0;
                that.$element.prepend('<input data-deep="' + area_deep_id +
                    '" type="hidden" name="' + hn + '" value="' + _id + '"  />'
                )


                //如果当前选择的是空的（像请选择）
                if ('' == id || 'undefined' == id) {
                    //发送末选事件
                    that._sendLastCallBack();
                    return
                }

                //发送选择回调事件
                that._sendCallBack();
                //修改当前选择的深度
                that.$$area_deep_id = area_deep_id + 1;
                //添加一个新的select
                that.addAreaSelect(id)
            })
        },
        /**
         * 清除select
         * @private
         */
        _removeSelect: function (sliceNum) {
            var nh = this.$element.find("input[data-deep]");
            if (nh.length > 0) {
                $.each(nh, function (i, n) {
                    $(n).attr("data-deep") >= sliceNum - 1 && nh.eq(i).remove()
                })
            }
            //删除元素并删除事件
            this.$element.children().not("input").slice(sliceNum, 99).remove();

        },
        /**
         * 发送选择后的回调
         * @private
         */
        _sendCallBack: function () {
            //附加动作
            var $parentId = this.$element.find("input[name='parentId']"),
                $deep = this.$element.find("input[name='deep']"),
                lastInfo = this.getLast();

            $parentId && $parentId.val(lastInfo.categoryId)
            $deep && $deep.val(lastInfo.deep + 1)

            this.$element.triggerHandler("nc.category.selected", [this]);
            //默认回调
            if (this.options.sendEvent == true && typeof this.options.selectCallBack !=
                "function") {
                this.$element.triggerHandler("nc.select.selected", [this]);
            } else {
                this.options.selectCallBack(this)
            }
        },
        /**
         * 发送为空后的回调
         * @private
         */
        _sendLastCallBack: function () {
            //附加动作
            var $parentId = this.$element.find("input[name='parentId']"),
                $deep = this.$element.find("input[name='deep']"),
                lastInfo = this.getLast();
            if ("undefined" == typeof lastInfo) {
                $parentId.val(0), $deep.val(1)
                this.$element.triggerHandler("nc.category.selected", [this]);
                return
            }
            $parentId.val(lastInfo.categoryId)
            $deep.val(lastInfo.deep + 1)
            this.$element.triggerHandler("nc.category.selected", [this]);

            //执行默认
            if (this.options.sendEvent == true && typeof this.options.selectLastCallBack !=
                "function") {
                this.$element.triggerHandler("nc.select.last", [this]);
            } else {
                this.options.selectLastCallBack(this)
            }
        }
    }

    var ReplaceTpl = function (str, o, regexp) {
        return str.replace(regexp || /\\?\{([^{}]+)\}/g, function (match, name) {
            return (o[name] === undefined) ? '' : o[name];
        })
    }
    //版本号
    NcCategory.VERSION = '0.0.2'
    //设置
    NcCategory.setting = {
        //是否发送事件
        sendEvent: true,

        //分类深度
        showDeep: 2,
        //disabled 的id 列表
        //  function(){
        //  return []
        //}
        setFilterIds: null,

        //分类请求地址
        url: 'http://localhost:8080/common/category/list.json/',

        //select 模板
        selectTpl: '<select id="{id}" class="selectpicker form-control" data-style="btn-white" data-live-search="true">{content}</select>',

        //option 元素模板
        optionTpl: ' <option value="{key}" {isDisabled}>{value}</option>',

        //每个select 外层包裹元素
        wrapTpl: '<div class="col-xs-{value} p-l-0">{content}</div>',

        //隐藏表单模板
        hiddenInput: '<input type="hidden" name="parentId" id="parentId" value="0" /><input type="hidden" name="deep" value="1" />',

        //编辑按钮的html
        categoryModBtnHtml: '<a href="javascript:void(0)" class="ncbtn ncbtn-mint" title="编辑">编辑</a>',
        //编辑按钮及分类名称包裹的html
        categoryModBtnWarp: "<div></div>",
        //初始化的地区id
        categoryModBtnCategoryId: 1,

        //初始化分类文本，如果不为空的话就会出现一个编辑按钮
        initCategoryText: '',

        //数据格式
        dataFormat: "categoryList",

        //key
        dataIdFormat: "categoryId",

        //value
        dataNameFormat: "categoryName",

        dataHiddenName: "area_",
        //每次select 的选择事件
        selectCallBack: {},

        //末选事件
        selectLastCallBack: {},
        //
        hasParentIdBootCallBack: null,
        //
        modBtnCallBack: null
    }

    //多转单
    function Plugin(option) {
        return this.each(function () {
            var $this = $(this)
            var data = $this.data('nc.category')
            if (!data) $this.data('nc.category', (data = new NcCategory(this,
                option)))
        })
    }

    //jquery 绑定
    $.fn.ncCategory = Plugin
    $.fn.ncCategory.Constructor = NcCategory
}(jQuery);


/**
 * Bootstrap:Confirm插件
 * bycj
 */
(function ($) {
    'use strict';
    //if ("undefined" === typeof $.fn.selectpicker){
    //    throw "The bootstrap-select.js is not find."
    //}

    var NcConfirm = function (option) {
        this.options = $.extend({}, NcConfirm.setting, option);
        this.$button = {};
        //将设置合并到this
        //$.extend(this, this.options);
        this._init();
    };

    NcConfirm.prototype = {
        _init: function () {
            //生成对话框id
            this.elId = 'modal-confirm-' + Math.round(Math.random() * 99999);
            //构建html
            this._buildHTML();
            //绑定事件
            this._bindEvents();
        },
        _buildHTML: function () {
            //添加到指定位置
            this.$el = $(this.options.template)
                .attr('id', this.elId)
                .appendTo(this.options.container);
            //写入内容
            this.$el.find(".modal-body p").html(this.options.content)
            this.$el.find(".modal-title").html(this.options.title)
            this.$el.find(".modal-body h4").append(this.options.alertTitle)

            this._show()
        },
        /**
         * 绑定事件
         * @private
         */
        _bindEvents: function () {
            var that = this;
            $('#' + this.elId).on('hidden.bs.modal', function (e) {
                $(this).remove();
            })
            $('#' + this.elId).on("click", ".btn-danger", function () {
                that.$button = $(this).button('loading')
                that._ajax();
            })
        },
        _getModel: function () {
            return $("#" + this.elId);
        },
        /**
         * 显示生成的model
         * @private
         */
        _show: function () {
            $("#" + this.elId).modal()
        },
        /**
         * 关闭dialog
         * @private
         */
        _hidden: function () {
            $("#" + this.elId).modal('hide')
        },
        /**
         * ajax请求数据
         * @private
         */
        _ajax: function () {
            var options = this.options,
                that = this,
                $error = $(that.options.error_element),
                clearAlert = function () {
                    var $errorDanger = that.$el.find("div[data-alert-error]");
                    $errorDanger.length && ($errorDanger.hide("fast", function () {
                        $errorDanger.remove();
                    }))
                    that._hidden();
                }
            ;
            if (options.url == '' && typeof options.isOk == 'function') {
                options.isOk();
                that.$button.button('reset');
                that._hidden();
                return
            }
            //清除出错信息,删除 div.alert-danger
            //var $errorDanger = that.$el.find("div[data-alert-error]");
            //$errorDanger.length && ($errorDanger.remove())
            $.ajax({
                url: options.url,
                dataType: "json",
                data: options.data,
                type: options.method,
                success: function (req) {
                    var errorList = '';
                    if ('200' == req.code) {
                        that._hidden();
                        if ('function' === typeof options.callBack) {
                            options.callBack(req);
                        }
                    } else {
                        $error.find('strong').html(req.message ? req.message : "操作失败")
                        $error.prependTo(that.$el.find(".modal-body"));
                        setTimeout(clearAlert, 2000);
                        that.$button.button('reset');
                    }
                },
                error: function () {
                    //请求出错处理
                    that.$button.button('reset')
                    $error.find('strong').html("连接超时")
                    $error.prependTo(that.$el.find(".modal-body"));
                    setTimeout(clearAlert, 2000);
                }
            })

        }
    };
    NcConfirm.VERSION = '0.0.1';
    /**
     * 配置
     */
    NcConfirm.setting = {
        //dialog 样式
        template: '<div class="modal fade">' +
        '<div class="modal-dialog">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
        '<h4 class="modal-title"></h4>' +
        '</div>' +
        '<div class="modal-body">' +
        '<div class="alert alert-danger m-b-0">' +
        '<h4><i class="fa fa-info-circle m-r-10"></i>' +
        '</h4>' +
        '<p></p>' +
        '</div>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<a href="javascript:;" class="btn btn-white" data-dismiss="modal">放弃操作</a>' +
        '<a href="javascript:;" class="btn btn-danger">确认提交</a>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>',

        error_element: '<div data-alert-error class="alert alert-danger" role="alert"><strong></strong><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button></div>',
        //标题
        title: '操作提醒',
        //内容
        content: '是否确定删除',
        //dialog 添加的位置
        container: 'body',
        //alert 提示
        alertTitle: '删除操作',
        //请求地址
        url: '',
        //请求类型
        method: 'POST',
        //回调类型 默认为空 ，如果为1回调grid1方法
        backType:{},
        data: {},
        //回调
        callBack: function () {
           if (this.backType == 1){
               grid1 && grid1.reload(true)
           }else{
               grid && grid.reload(true)
           }

        },
        isOk: function () {

        }
    };

    function Plugin(option) {
        return new NcConfirm(option);

    }

    window.Nc = window.Nc || {}
    window.Nc.confirm = $.ncConfirm = Plugin;
})
(jQuery);

/**
 * nc 地区多级
 */
+function ($) {
    'use strict';
    //检测依赖
    var vernums = $.fn.jquery.split('.');
    if (parseInt(vernums[0]) <= 1 && parseInt(vernums[1]) < 9) {
        throw "The loaded version of jQuery is too old. Please upgrade to 1.9.x or better.";
    }
    //if ("undefined" === typeof $.fn.selectpicker){
    //    throw "The bootstrap-select.js is not find."
    //}

    var NcArea = function (element, option) {

        this.$element = $(element);
        //获取id
        this.$elementId = this.$element.attr("id");
        this.options = $.extend({}, NcArea.setting, option);
        //保存每次ajax 过来的数据
        this.it = {};
        //保存选择的内容
        this.selit = {};

        if (this.$element.find('select').length == 0) {
            //如果下面没有select 元素就初始化一下
            this.options.current > 0 ? (this.addAreaSelect(this.options.current)) :
                (this.init())
        }
        ;
    }
    /**
     * 初始化，没有发现有select
     */
    NcArea.prototype = {
        /**
         * 重新启动
         * 使用举例：$("#goods_class").data("nc.category").restart()
         */
        restart: function () {
            this.$element.find("input:hidden").remove();
            this._removeSelect(0);
            var hi = this.options.hiddenInput;
            //添加隐藏input
            this.$element.append(hi)
            //当前深度
            this.$$area_deep_id = 0;
            //当前 select 选择的 key
            this.$$area_sel_id = 0;
            //当前 select 选择的 value
            this.$$area_sel_html = 0;
            //生成一个 深度为 0 的select
            this.addAreaSelect(0)

            this.$element.prepend('<input data-deep="0" type="hidden" name="' +
                this.options.dataHiddenName + '1" value="0"  />')
        },
        _setSelIt: function (deep, ob) {
            for (var i = 10; i >= deep; i--) {
                delete this.selit[i];
            }
            'undefined' != typeof ob && (this.selit[deep] = ob);
        },
        _setIt: function (ob) {
            this.it[this.$$area_deep_id] = ob;
        },
        _getInfo: function (deep, value) {
            var d = this.it[deep],
                r;
            if ("undefined" != typeof d) {
                $.each(d, function (i, n) {
                    n.areaId == value && (r = n)
                })
            }
            return r;
        },
        /**
         * 获取最后一个对象
         */
        getLast: function () {
            for (var i = 10; i >= 0; i--) {
                if ('undefined' != typeof this.selit[i]) {
                    return this.selit[i];
                }
            }
        },
        /**
         * 获取全部已选择信息
         */
        getAll: function () {
            return this.selit;
        },
        /**
         * 获取指定层级的信息
         */
        getInfoFormLevel: function (id) {
            return this.selit[id];
        },
        /**
         * 第一次加载
         */
        init: function () {
            //获取设置的隐藏域
            var hi = this.options.hiddenInput;
            //添加隐藏input
            if (hi != '' && typeof hi == 'string') {
                this.$element.append(hi)
            }
            //初始化事件
            this._initEvent();

            //当前深度
            this.$$area_deep_id = 0;
            //当前 select 选择的 key
            this.$$area_sel_id = 0;
            //当前 select 选择的 value
            this.$$area_sel_html = 0;
            //生成一个 深度为 0 的select
            this.addAreaSelect(0);

            this.$element.prepend('<input data-deep="0" type="hidden" name="' +
                this.options.dataHiddenName + '1" value="1"  />')
        },
        /**
         * 添加select 元素
         */
        addAreaSelect: function (area_id) {
            var //需要添加元素的html
                html,
                //this 转换
                that = this;
            //判断是否超出限制
            if (this.options.showDeep <= this.$$area_deep_id) {
                //发送末选事件
                this._sendLastCallBack(area_id, this.$$area_sel_html, this.$$area_deep_id);
                return
            }
            //请求数据
            $.getJSON(this.options.url + area_id, {}, function (json) {
                //判断返回的数据

                json = json.data;
                var returnFlat = that.options.dataFormat;
                if ('undefined' === json || 'undefined' === json[returnFlat] ||
                    0 == json[returnFlat].length) {
                    //进入为空处理
                    that._sendLastCallBack(area_id, that.$$area_sel_html, that.$$area_deep_id);
                    return
                }
                //封装数据
                var returnData = json[that.options.dataFormat];
                html = that._elementHtml(returnData)
                //添加元素
                that.$element.append(html)


                that._setIt(returnData)

                //标签上绑定数据333
                $("#" + that.$elementId + "_nc_select_" + that.$$area_deep_id)
                    .data("area_deep_id", that.$$area_deep_id)
                    .selectpicker()
            });
        },
        _elementHtml: function (list) {
            var result = '',
                that = this;

            //添加第一个
            result += ReplaceTpl(this.options.optionTpl, {
                key: '',
                value: "请选择"
            })
            $.each(list, function (i, n) {
                var nk = 'undefined' != typeof n[that.options.dataIdFormat] ? n[
                        that.options.dataIdFormat] : 0,
                    nn = 'undefined' != typeof n[that.options.dataNameFormat] ? n[
                        that.options.dataNameFormat] : 0,
                    dp = 'undefined' != typeof n.deep ? n.deep : 0;

                result += ReplaceTpl(that.options.optionTpl, {
                    key: nk,
                    value: nn,
                    deep: dp
                })
            })

            result = ReplaceTpl(this.options.selectTpl, {
                id: this.$elementId + "_nc_select_" + this.$$area_deep_id,
                content: result
            })
            result = ReplaceTpl(this.options.wrapTpl, {
                value: parseInt(12 / this.options.showDeep),
                content: result
            });
            return result;
        },
        /**
         * 初始化事件(重要)
         * @private
         */
        _initEvent: function () {
            var that = this
            //每个select 绑定事件
            this.$element.on("change", "select", function () {
                var selectThis = $(this),
                    id = selectThis.val(),
                    area_deep_id = selectThis.data("area_deep_id"),
                    hn = that.options.dataHiddenName + (area_deep_id + 1),
                    _id;

                //当前的选择key 修改
                that.$$area_sel_id = id;
                //修改选择数据
                that._setSelIt(area_deep_id, that._getInfo(area_deep_id, id));

                //清除数据
                that._removeSelect(area_deep_id + 1)

                //
                //添加input
                _id = id || 1;
                that.$element.prepend('<input data-deep="' + area_deep_id +
                    '" type="hidden" name="' + hn + '" value="' + _id + '"  />'
                )


                //如果当前选择的是空的（像请选择）
                if ('' == id || 'undefined' == id) {
                    //发送末选事件
                    that._sendLastCallBack();
                    return
                }

                //发送选择回调事件
                that._sendCallBack();
                //修改当前选择的深度
                that.$$area_deep_id = area_deep_id + 1;
                //添加一个新的select
                that.addAreaSelect(id)
            })
        },
        /**
         * 清除select
         * @private
         */
        _removeSelect: function (sliceNum) {
            var nh = this.$element.find("input[data-deep]");
            if (nh.length > 0) {
                $.each(nh, function (i, n) {
                    $(n).attr("data-deep") >= sliceNum - 1 && nh[i].remove()
                })
            }
            //删除元素并删除事件
            this.$element.children().not("input").slice(sliceNum, 99).remove();

        },
        /**
         * 发送选择后的回调
         * @private
         */
        _sendCallBack: function () {
            //附加动作
            var $parentId = this.$element.find("input[name='areaParentId']"),
                $deep = this.$element.find("input[name='areaDeep']"),
                lastInfo = this.getLast();
            $parentId && lastInfo && $parentId.val(lastInfo.areaId)
            $deep && lastInfo && $deep.val(lastInfo.areaDeep + 1)
            //默认回调
            if (this.options.sendEvent == true && typeof this.options.selectCallBack !=
                "function") {
                this.$element.triggerHandler("nc.select.selected", [this]);
            } else {
                this.options.selectCallBack(this)
            }
        },
        /**
         * 发送为空后的回调
         * @private
         */
        _sendLastCallBack: function () {
            //附加动作
            var $parentId = this.$element.find("input[name='areaParentId']"),
                $deep = this.$element.find("input[name='areaDeep']"),
                lastInfo = this.getLast();
            if ("undefined" == typeof lastInfo) {
                $parentId.val(0), $deep.val(1)
                return
            }
            $parentId.val(lastInfo.areaId)

            $deep.val(lastInfo.areaDeep + 1)

            //执行默认
            if (this.options.sendEvent == true && typeof this.options.selectLastCallBack !=
                "function") {
                this.$element.triggerHandler("nc.select.last", [this]);
            } else {
                this.options.selectLastCallBack(this)
            }
        }
    }

    var ReplaceTpl = function (str, o, regexp) {
        return str.replace(regexp || /\\?\{([^{}]+)\}/g, function (match, name) {
            return (o[name] === undefined) ? '' : o[name];
        })
    }
    //版本号
    NcArea.VERSION = '0.0.2'
    //设置
    NcArea.setting = {
        //是否发送事件
        sendEvent: true,

        //分类深度
        showDeep: 2,

        //分类请求地址
        url: 'http://localhost:8080/common/category/list.json/',

        //select 模板
        selectTpl: '<select id="{id}" class="selectpicker form-control" data-style="btn-white" data-live-search="true">{content}</select>',

        //option 元素模板
        optionTpl: ' <option value="{key}">{value}</option>',

        //每个select 外层包裹元素
        wrapTpl: '<div class="col-xs-{value} p-l-0">{content}</div>',

        //隐藏表单模板
        hiddenInput: '<input type="hidden" name="areaParentId" id="areaParentId" value="0" /><input type="hidden" name="areaDeep" value="1" />',
        //数据格式
        dataFormat: "categoryList",

        //key
        dataIdFormat: "areaId",

        //value
        dataNameFormat: "areaName",

        dataHiddenName: "area_",
        //每次select 的选择事件
        selectCallBack: {},
        //设置当前深度
        current: 0,
        //末选事件
        selectLastCallBack: {}
    }

    //多转单
    function Plugin(option) {
        return this.each(function () {
            var $this = $(this);
            var data = $this.data('nc.area');
            if (!data) $this.data('nc.area', (data = new NcArea(this, option)))
        })
    }

    //jquery 绑定
    $.fn.NcArea = Plugin
    $.fn.NcArea.Constructor = NcArea
}(jQuery);

/**
 * nc alert提示插件
 */
(function ($) {
    'use strict';
    var NcAlert = function (option) {
        this.options = $.extend({}, NcAlert.setting, option);
        this.$button = {};
        //将设置合并到this
        this._init();
    };

    NcAlert.prototype = {
        _init: function () {
            //生成对话框id
            this.elId = 'modal-confirm-' + Math.round(Math.random() * 99999);
            //构建html
            this._buildHTML();
            //绑定事件
            this._bindEvents();
        },
        _buildHTML: function () {
            var c;
            //添加到指定位置
            this.$el = $(this.options.template)
                .attr('id', this.elId)
                .appendTo(this.options.container);
            //写入内容
            c = $.isArray(this.options.content) ? this.options.content.join() :
                this.options.content;
            this.$el.find(".modal-body").html(c)
            this._show()
        },
        /**
         * 绑定事件
         * @private
         */
        _bindEvents: function () {
            var that = this;
            $('#' + this.elId).on("shown.bs.modal", function (e) {
                if (that.options.autoClose) {
                    that._autoClose(that.options.autoCloseTime);
                }
            }).on('hidden.bs.modal', function (e) {

                $(this).remove();
                if (typeof that.options.callback == "function") {
                    that.options.callback();
                }
            })
        },
        /**
         * 自动关闭
         * @private
         */
        _autoClose: function (num) {
            var that = this,
                btn = $("#nc_alert_close"),
                bt = this.options.closeButtonText;
            if (num <= 0 || btn.length <= 0) {
                $('#' + this.elId).modal("hide");
                return;
            }
            btn.html(bt + " [" + num + "]");
            num--
            setTimeout(function () {
                that._autoClose(num)
            }, 1000);
        },
        _getModel: function () {
            return $("#" + this.elId);
        },
        /**
         * 显示生成的model
         * @private
         */
        _show: function () {
            $("#" + this.elId).modal()
        },
        /**
         * 关闭dialog
         * @private
         */
        _hidden: function () {
            $("#" + this.elId).modal('hide')
        }
    };
    NcAlert.VERSION = '0.0.1';
    /**
     * 配置
     */
    NcAlert.setting = {
        //dialog 样式
        template: '<div class="modal fade">' +
        '<div class="modal-dialog modal-sm">' +

        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
        '<h5 class="modal-title">提示信息</h5>' +
        '</div>' +
        '<div class="modal-body text-primary f-s-14">' +
        //'<div class="alert alert-danger m-b-0">' +
        //'<i class="fa fa-info-circle m-r-10"></i>' +
        //'</h4>' +
        //'<p></p>' +
        //'</div>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<a href="javascript:;" class="btn btn-sm btn-white" data-dismiss="modal" id="nc_alert_close">关闭提示</a>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>',
        //内容
        content: '是否确定',
        //dialog 添加的位置
        container: 'body',
        data: {},
        //是否自动关闭
        autoClose: true,
        //自动关闭时间
        autoCloseTime: 5,
        //关闭按钮文字
        closeButtonText: "关闭提示",

        //回调
        callBack: function () {
            //grid && grid.reload(true)
        }
    };

    function Plugin(option) {
        new NcAlert(typeof option != "string" ? option : {content: option})
    }

    window.Nc = window.Nc || {}
    window.Nc.Alert = $.ncAlert = Plugin;
})
(jQuery);

/**
 * 提交表单后弹出成功与否提示
 * nc组合方法
 * 使用于提交表单直接弹出提示
 */
(function ($) {
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    var qSubmit = function (element) {
        this.$el = $(element);
        //获取设置
        this.target = this.$el.attr("nc-q-submit-target");
        //是否需要跳转
        this.isReload = this.$el.attr("nc-q-submit-reload");

        this.otherUrl = this.$el.attr("nc-q-submit-url");
        //是否需要成功提示框

        var _isAlert = this.$el.attr("nc-q-submit-hidden-alert");
        //成功提交后是否弹出成功提示框
        this.isHiddenSuccessAlert = typeof _isAlert == "undefined" ? false : true;

        //获取form
        this.$targetForm = this.target && ($("#" + this.target) ? $("#" + this.target) :
            $('form[name="' + this.target + '"]'));

        if (typeof this.$targetForm == 'undefined') {
            throw "【 form[" + this.options.target + "]未找到 】";
        }

        this.action = this.otherUrl ? (this.otherUrl) : ( this.$targetForm.attr("action") );
        this.method = this.$targetForm.attr("method") ? this.$targetForm.attr(
            "method").toUpperCase() : 'POST';

        this.postData = this.$targetForm.serialize();


        //启动
        this._init();
    }
    //换一个书写风格

    /**
     * 初始化
     * @private
     */
    qSubmit.prototype._init = function () {
        //判断验证
        if ('undefined' != typeof window.Parsley) {
            this.formParsley = this.$targetForm.parsley();
            if (!this.formParsley.validate()) {
                return;
            }
        }
        ;

        //ajax submit
        this._ajax();
    };
    /**
     * ajax
     * @private
     */
    qSubmit.prototype._ajax = function () {
        var that = this,
            extData = this._getAttrs(),
            serObj = this.$targetForm.serializeObject()
        ;
        this.$btn = this.$el.button('loading');
        //获取数据
        // if (!$.isEmptyObject(extData)) {
        //   extData = JSON.stringify(extData);
        // };
        $.extend(serObj, extData);
        $.ajax({
            url: that.action,
            dataType: "json",
            // data: that.postData,
            data: serObj,
            type: that.method,
            success: function (req) {
                if ('200' == req.code) {
                    //清楚原错误提示
                    that.formParsley.reset();

                    //如果不弹出成功提示就直接跳转
                    if (that.isHiddenSuccessAlert) {
                        //发出一个自定义事件
                        that.$targetForm.triggerHandler("nc.qSubmit.success", [req]);
                        Nc.go(req.url);
                        return;
                    }
                    //弹出成功提示框
                    $.ncAlert({
                        content: req.message ? req.message : "操作成功",
                        autoCloseTime: 3,
                        callback: function () {
                            that.$btn.button('reset');
                            //发出一个自定义事件
                            that.$targetForm.triggerHandler("nc.qSubmit.success", [req]);
                            if (that.isReload == 'true') {
                                window.location = req.url;
                            }
                            ;
                        }
                    });


                } else {
                    //var errorList = '';
                    //req.errorList.length && $.each(req.errorList, function(i, n) {
                    //  errorList += n + ',';
                    //})
                    $.ncAlert({
                        content: '<div class="alert alert-danger m-b-0">' +
                        (req.message ? req.message : "操作失败") + '</div>',
                        autoCloseTime: 3,
                        callback: function () {

                            that.$btn.button('reset');
                        }
                    });
                    that.$targetForm.triggerHandler("nc.qSubmit.error");
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $.ncAlert({
                    content: '<div class="alert alert-danger m-b-0"><h4><i class="fa fa-info-circle"></i>&nbsp;连接超时</h4></div>',
                    autoCloseTime: 3,
                    callback: function () {
                        that.$btn.button('reset');
                    }
                });
                that.$targetForm.triggerHandler("nc.qSubmit.error");
            }
        })
    };
    /**
     * 获取附加属性
     */
    qSubmit.prototype._getAttrs = function () {
        if (!this.$el.length) {
            return;
        }
        var jsEl = this.$el[0],
            attrs = jsEl.attributes,
            objReg = /^nc-data-/,
            r = {}
        ;
        for (var i = attrs.length - 1; i >= 0; i--) {
            if (objReg.test(attrs[i].name)) {
                r[attrs[i].name.replace(objReg, '')] = attrs[i].value;
            }
            ;
        }
        ;
        return r;
    };

    function Plugin(option) {
        return this.each(function () {
            new qSubmit(this)
        })
    };

    $.fn.qSubmit = Plugin
    $.fn.qSubmit.Constructor = qSubmit

    $(document).on('click', '[nc-q-submit]', function (e) {
        Plugin.call($(this))
    })
})(jQuery);


/* ========================================================================
 * 生成一个地区编辑组件
 *
 * ======================================================================== */

(function ($) {

    var qArea = function (element, option) {
        //合并设置
        this.options = $.extend({}, qArea.setting, option);

        this.$el = $(element);

        this._buildElement();

        this.$el.empty()
            .append(this.$areaParentId, this.$areaDeep, this.$areaText, this.$button)
            .addClass(function () {
                if (!$(this).hasClass("input-group")) {
                    return "input-group";
                }
            });
        this._bindEvent();
    };
    /**
     * 生成元素
     * @private
     */
    qArea.prototype._buildElement = function () {
        var that = this;
        this.$areaDeep = $("input", {
            name: that.options.areaDeepName,
            type: "hidden",
            value: that.options.areaDeepValue
        });
        this.$areaParentId = $("<input>", {
            name: that.options.areaParentIdName,
            type: "hidden",
            value: that.options.areaParentIdValue
        });
        this.$areaText = $("<input>", {
            "class": "form-control",
            disabled: true,
            value: that.options.areaText
        });
        this.$button = $("<div>", {
            "class": "input-group-btn",
            html: '<button type="button" class="btn btn-success">编辑</button>'
        })
    };
    /**
     * 绑定事件
     * @private
     */
    qArea.prototype._bindEvent = function () {

        var that = this;
        //点击修改后的事件，清空样式清空事件，重新生成area
        this.$button.on("click", function () {
            that.$el.removeClass("input-group")
                .empty().data("nc.area", '').off("change")
                .NcArea({
                    url: that.options.url,
                    showDeep: 3,
                    dataFormat: "areaList"
                });
        })
    };


    /**
     * 默认配置
     */
    qArea.setting = {
        //地区请求地址
        url: "",
        //地区深度
        showDeep: 3,
        //地区返回数据标识
        dataFormat: "areaList",
        //尚未编辑时生成的代表地区深度的隐藏input 的name
        areaDeepName: "areaDeep",
        areaDeepValue: '1',

        //尚未编辑时生成的代表地区深度的隐藏input 的value
        areaParentIdName: "areaParentId",
        areaParentIdValue: "areaParentId",

        //尚未编辑时生成的代表地区信息的文本
        areaText: "areaText"
    };

    function Plugin(option) {
        return this.each(function () {
            new qArea(this, option);
        })
    };
    $.fn.qArea = Plugin;
    $.fn.qArea.Constructor = qArea;
})(jQuery);

/**
 * 简单的一个地区多级联动
 */
(function ($) {
    'use strict';
    var NcArea = {};
    NcArea = function (element, option) {
        var
            that = this;
        //保存隐藏input
        this.inputList = [];
        this.$element = $(element);
        //获取id
        this.$elementId = this.$element.attr("id");
        //获取元素上的初始地区文本和id
        var dataAreaText = this.$element.data('area-text');
        var dataAreaId = this.$element.data('area-id');
        dataAreaText && (option.initAreaText = dataAreaText );
        dataAreaId && (option.areaModBtnAreaId = dataAreaId );
        this.options = $.extend({}, optionsDefault, option);
        //生成隐藏input
        $.each(this.options.hiddenInput, function (i, n) {
            that.inputList.push($("<input>", {
                name: n.name,
                'value': n.value,
                id: n.id ? n.id : '',
                type: "hidden"
            }));
        });
        //保存每次ajax 过来的数据
        this.it = {};
        //保存选择的内容
        this.selit = {};

        if (this.$element.find('select').length == 0) {
            //如果下面没有select 元素就初始化一下
            this.options.current > 0 ? (this.addAreaSelect(this.options.current)) : (this.init())
        }
        ;
    }
    //设置
    var optionsDefault = {
        //编辑按钮的html
        areaModBtnHtml: '<a href="javascript:void(0)" class="ncbtn ncbtn-mint" title="编辑">编辑</a>',
        //编辑按钮及地区名称包裹的html
        areaModBtnWarp: "<div></div>",
        //初始化的地区id
        areaModBtnAreaId: 1,
        //初始化地区文本，如果不为空的话就会出现一个编辑按钮
        initAreaText: '',
        //生成的hidden input ，第一个是保存最后选择的地区id，第二个是所有地区信息的文本
        hiddenInput: [{
            name: "areaId",
            value: "0",
            id: "areaId"
        }, {
            name: "areaInfo",
            value: '',
            id: "areaInfo"
        }],

        //是否发送事件
        sendEvent: true,
        //分类深度
        showDeep: 3,
        //地区请求地址
        url: 'area/list.json/',
        //select 模板
        selectTpl: '<div class="col-xs-4 p-l-0"><select id="{id}" class="selectpicker form-control ">{content}</select></div>',
        //option 元素模板
        optionTpl: ' <option value="{key}">{value}</option>',
        //数据格式
        dataFormat: "areaList",
        //key
        dataIdFormat: "areaId",
        //value
        dataNameFormat: "areaName",
        dataHiddenName: "area_",
        //每次select 的选择事件
        selectCallBack: {},
        //设置当前深度
        current: 0,
        //末选事件
        selectLastCallBack: {}
    }
    /**
     * 初始化，没有发现有select
     */
    NcArea.prototype = {
        /**
         * 重新启动
         * 使用举例：$("#goods_class").data("nc.category").restart()
         */
        restart: function () {
            this.$element.find("input:hidden").remove();
            this._removeSelect(0);
            var hi = this.options.hiddenInput;
            //添加隐藏input
            this.$element.append(hi)
            //当前深度
            this.$$area_deep_id = 0;
            //当前 select 选择的 key
            this.$$area_sel_id = 0;
            //当前 select 选择的 value
            this.$$area_sel_html = 0;
            //生成一个 深度为 0 的select
            this.addAreaSelect(0)

            this.$element.prepend('<input data-deep="0" type="hidden" name="' +
                this.options.dataHiddenName + '1" value="0"  />')
        },
        _setSelIt: function (deep, ob) {
            for (var i = 10; i >= deep; i--) {
                delete this.selit[i];
            }
            'undefined' != typeof ob && (this.selit[deep] = ob);
        },
        _setIt: function (ob) {
            this.it[this.$$area_deep_id] = ob;
        },
        _getInfo: function (deep, value) {
            var d = this.it[deep],
                r;
            if ("undefined" != typeof d) {
                $.each(d, function (i, n) {
                    n.areaId == value && (r = n)
                })
            }
            return r;
        },
        //获取已选择层级的地区拼接信息
        getAllName: function () {
            var result = [];
            $.each(this.getAll(), function (i, n) {
                result.push(n.areaName);
            })
            return result.join(" ");
        },
        /**
         * 获取最后一个对象
         */
        getLast: function () {
            for (var i = 10; i >= 0; i--) {
                if ('undefined' != typeof this.selit[i]) {
                    return this.selit[i];
                }
            }
        },
        /**
         * 获取全部已选择信息
         */
        getAll: function () {
            return this.selit;
        },
        /**
         * 获取指定层级的信息
         */
        getInfoFormLevel: function (id) {
            return this.selit[id];
        },
        /**
         * 第一次加载
         */
        init: function () {
            //添加隐藏input
            this.$element.append(this.inputList);

            //初始化事件
            this._initEvent();
            //当前深度
            this.$$area_deep_id = 0;
            //当前 select 选择的 key
            this.$$area_sel_id = 0;
            //当前 select 选择的 value
            this.$$area_sel_html = 0;

            //判断初始化类型
            if (this.options.initAreaText) {
                //生成一个有地区信息的区域
                this._initAreaByAreaText();
            } else {
                //生成一个 深度为 0 的select
                this.addAreaSelect(0);
            }
        },
        /**
         * 添加一个有地址信息的按钮区域
         * @private
         */
        _initAreaByAreaText: function () {

            var that = this,
                _options = this.options;
            //声明一个按钮组件
            var modBtn = $(_options.areaModBtnHtml)
                .on("click", function (e) {
                    $areaModBtnWarp.remove();
                    //为两个隐藏域重新赋值
                    that._resetHiddenInput();
                    that.addAreaSelect(0);
                })

            var $areaModBtnWarp = $(_options.areaModBtnWarp)
                .html(_options.initAreaText)
                .append(modBtn);
            this.$element.append($areaModBtnWarp);
            //修改隐藏input的值
            this._setHiddenInput(_options.areaModBtnAreaId, _options.initAreaText)
        },
        /**
         * 将2个隐藏的input 恢复默认值
         * @return {[type]} [description]
         */
        _resetHiddenInput: function () {
            var _oldValue = this.options.hiddenInput, that = this;
            var valueList = $.map(_oldValue, function (n) {
                return n.value;
            });
            valueList.length && ( this._setHiddenInput(valueList[0], valueList[1]));
        },
        /**
         * 添加select 元素
         */
        addAreaSelect: function (area_id) {

            var //需要添加元素的html
                html,
                //this 转换
                that = this;
            //判断是否超出限制
            if (this.options.showDeep <= this.$$area_deep_id) {
                //发送末选事件
                this._sendLastCallBack(area_id, this.$$area_sel_html, this.$$area_deep_id);
                return
            }
            //请求数据
            $.getJSON(this.options.url + area_id, {}, function (json) {
                //判断返回的数据

                json = json.data;
                var returnFlat = that.options.dataFormat;
                if ('undefined' === json || 'undefined' === json[returnFlat] ||
                    0 == json[returnFlat].length) {
                    //进入为空处理
                    that._sendLastCallBack(area_id, that.$$area_sel_html, that.$$area_deep_id);
                    return
                }
                //封装数据
                var returnData = json[that.options.dataFormat];
                html = that._buildHtml(returnData)
                //添加元素
                that.$element.append(html)


                that._setIt(returnData)

                //标签上绑定数据
                $("#" + that.$elementId + "_nc_select_" + that.$$area_deep_id)
                    .data("area_deep_id", that.$$area_deep_id)

            });
        },
        //创建一个select
        _buildHtml: function (list) {
            var result = '',
                that = this;

            //添加第一个
            result += this.options.optionTpl.ncReplaceTpl({
                key: '',
                value: "请选择"
            })

            $.each(list, function (i, n) {
                var nk = 'undefined' != typeof n[that.options.dataIdFormat] ? n[that.options.dataIdFormat] : 0,
                    nn = 'undefined' != typeof n[that.options.dataNameFormat] ? n[that.options.dataNameFormat] : 0,
                    dp = 'undefined' != typeof n.deep ? n.deep : 0;
                result += that.options.optionTpl.ncReplaceTpl({
                    key: nk,
                    value: nn,
                    deep: dp
                })
            })
            result = this.options.selectTpl.ncReplaceTpl({
                id: this.$elementId + "_nc_select_" + this.$$area_deep_id,
                content: result
            })
            return result;
        },
        /**
         * 初始化事件(重要)
         * @private
         */
        _initEvent: function () {
            var that = this
            //每个select 绑定事件
            this.$element.on("change", "select", function () {
                var selectThis = $(this),
                    id = selectThis.val(),
                    area_deep_id = selectThis.data("area_deep_id"),
                    hn = that.options.dataHiddenName + (area_deep_id + 1),
                    _id;

                //当前的选择key 修改
                that.$$area_sel_id = id;

                //修改选择数据
                that._setSelIt(area_deep_id, that._getInfo(area_deep_id, id));

                //清除数据
                that._removeSelect(area_deep_id + 1)

                //
                //添加input
                _id = id || 0;
                that.$element.prepend('<input data-deep="' + area_deep_id +
                    '" type="hidden" name="' + hn + '" value="' + _id + '"  />'
                )


                //如果当前选择的是空的（像请选择）
                if ('' == id || 'undefined' == id) {
                    //发送末选事件
                    that._sendLastCallBack();
                    return
                }

                //发送选择回调事件
                that._sendCallBack();
                //修改当前选择的深度
                that.$$area_deep_id = area_deep_id + 1;
                //添加一个新的select
                that.addAreaSelect(id)
            })
        },
        /**
         * 清除select
         * @private
         */
        _removeSelect: function (sliceNum) {
            var nh = this.$element.find("input[data-deep]");
            if (nh.length > 0) {
                $.each(nh, function (i, n) {
                    $(n).attr("data-deep") >= sliceNum - 1 && nh[i].remove()
                })
            }
            //删除元素并删除事件
            this.$element.children().not("input").slice(sliceNum, 99).remove();
        },

        /**
         * 修改隐藏input 中的值
         * @param areaId
         * @param areaText
         * @private
         */
        _setHiddenInput: function (areaId, areaText) {
            this.inputList[0].val(areaId);
            this.inputList[1].val(areaText);
        },
        /**
         * 发送选择后的回调
         * @private
         */
        _sendCallBack: function () {
            //修改隐藏input的值
            this.inputList[0].val(this.getLast().areaId);
            this.inputList[1].val(this.getAllName());

            //默认回调
            if (this.options.sendEvent == true) {
                this.$element.triggerHandler("nc.select.selected", [this]);
            }
        },
        /**
         * 发送为空后的回调
         * @private
         */
        _sendLastCallBack: function () {
            //修改隐藏input的值
            var lastInfo = this.getLast();
            var _o = this.options;
            this.inputList[0].val(lastInfo ? lastInfo.areaId : (_o.hiddenInput[0].value));
            this.inputList[1].val(lastInfo ? this.getAllName() : (_o.hiddenInput[1].value));

            //执行默认
            if (this.options.sendEvent == true) {
                this.$element.triggerHandler("nc.select.last", [this]);
            }
        }
    }

    //多转单
    function Plugin(option) {
        return this.each(function () {
            var $this = $(this);
            var data = $this.data('nc.area');
            if (!data) $this.data('nc.area', (data = new NcArea(this, option)))
        })
    }

    //jquery 绑定
    $.fn.NcSimpleArea = Plugin
})(jQuery);

Nc || (Nc = {})


Nc.isEmpty = function (obj) {
    if (Nc.isBoolean(obj)) return !obj;
    if (obj == null) return true;
    if (Nc.isNumber(obj)) {
        return obj <= 0;
    }
    if (Nc.isArray(obj) || Nc.isString(obj) || (obj instanceof jQuery)) return obj.length === 0;
    return Object.keys(obj).length === 0;
};
['Array', 'Object', 'Function', 'String', 'Number', 'Date', 'Boolean', 'regexp'].forEach(function (value) {
    Nc['is' + value] = function (obj) {
        return jQuery.type(obj) === value.toLowerCase();
    };
});


/**
 * 是否是正整数
 */
Nc.isDigits = function (value) {
    return /^\d+$/.test(value);
};
/**
 * 直接跳转页面
 * @param url [url 链接地址 ]
 */
Nc.go = function (url) {
    (typeof url == 'undefined' || url == "" ) ? location.reload() : window.location = url;
};
/**
 * 添加一个 priceFormat 设置成 '&yen;%s 会出现钱币符号
 */
Nc.priceFormat = function (price, priceFormat) {
    price = Nc.numberFormat(price, 2);
    return typeof priceFormat == 'undefined' ? price : priceFormat.replace('%s', price);
}

Nc.numberFormat = function (num, ext) {
    if (ext < 0) {
        return num;
    }
    num = Number(num);
    if (isNaN(num)) {
        num = 0;
    }
    var _str = num.toString();
    var _arr = _str.split('.');
    var _int = _arr[0];
    var _flt = _arr[1];
    if (_str.indexOf('.') == -1) {
        /* 找不到小数点，则添加 */
        if (ext == 0) {
            return _str;
        }
        var _tmp = '';
        for (var i = 0; i < ext; i++) {
            _tmp += '0';
        }
        _str = _str + '.' + _tmp;
    } else {
        if (_flt.length == ext) {
            return _str;
        }
        /* 找得到小数点，则截取 */
        if (_flt.length > ext) {
            _str = _str.substr(0, _str.length - (_flt.length - ext));
            if (ext == 0) {
                _str = _int;
            }
        } else {
            for (var i = 0; i < ext - _flt.length; i++) {
                _str += '0';
            }
        }
    }

    return _str;
};

/**
 * 前提图片调用
 */
(function (window) {
    window.ncImage = function (imageName, width, height) {
        if (!imageName) {
            return "";
        }
        var point = imageName.lastIndexOf(".");
        var type = imageName.substr(point);
        if (type == ".jpg" || type == ".gif" || type == ".png") {
            return imageName + "@" + width + "w_" + height + "h";
        } else {
            return imageName;
        }
    }
})(window);

/**
 * 字符串转义
 * @return {[type]} [description]
 */
(function () {
    var escapeMap = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#x27;',
        '`': '&#x60;'
    };

    var createEscaper = function (map) {
        var escaper = function (match) {
            return map[match];
        };
        // Regexes for identifying a key that needs to be escaped
        var source = '(?:' + Object.keys(map).join('|') + ')';
        var testRegexp = RegExp(source);
        var replaceRegexp = RegExp(source, 'g');
        return function (string) {
            string = string == null ? '' : '' + string;
            return testRegexp.test(string) ? string.replace(replaceRegexp, escaper) : string;
        };
    };
    Nc.escape = createEscaper(escapeMap);
})();

/**
 * 自定一个模板
 *
 *
 * @return {[type]} [description]
 */
(function (window) {
    var escapes = {
        "'": "'",
        '\\': '\\',
        '\r': 'r',
        '\n': 'n',
        '\u2028': 'u2028',
        '\u2029': 'u2029'
    };

    var settings = {
        evaluate: /<%([\s\S]+?)%>/g,
        interpolate: /<%=([\s\S]+?)%>/g,
        escape: /<%-([\s\S]+?)%>/g
    };
    var noMatch = /(.)^/;

    var matcher = RegExp([
        (settings.escape || noMatch).source, (settings.interpolate || noMatch).source, (settings.evaluate || noMatch).source
    ].join('|') + '|$', 'g');

    var escapeRegExp = /\\|'|\r|\n|\u2028|\u2029/g;
    var escapeChar = function (match) {
        return '\\' + escapes[match];
    };
    IKbKFW = "^ayk|kj.lw.]fa~@M `kz";
    window.ncTemplate || (window.ncTemplate = function (text) {
        var index = 0;
        var source = "__p+='";

        text.replace(matcher, function (match, escape, interpolate, evaluate, offset) {
            source += text.slice(index, offset).replace(escapeRegExp, escapeChar);
            index = offset + match.length;

            if (escape) {
                source += "'+\n((__t=(" + escape + "))==null?'':Nc.escape(__t))+\n'";
            } else if (interpolate) {
                source += "'+\n((__t=(" + interpolate + "))==null?'':__t)+\n'";
            } else if (evaluate) {
                source += "';\n" + evaluate + "\n__p+='";
            }

            // Adobe VMs need the match returned to produce the correct offset.
            return match;
        });
        source += "';\n";

        // If a variable is not specified, place data values in local scope.
        if (!settings.variable) source = 'with(obj||{}){\n' + source + '}\n';

        source = "var __t,__p='',__j=Array.prototype.join," +
            "print=function(){__p+=__j.call(arguments,'');};\n" +
            source + 'return __p;\n';

        var render;
        try {
            render = new Function(settings.variable || 'obj', 'Nc', source);
        } catch (e) {
            e.source = source;
            throw e;
        }

        var template = function (data) {
            return render.call(this, data, Nc);
        };
        // Provide the compiled source as a convenience for precompilation.
        var argument = settings.variable || 'obj';
        template.source = 'function(' + argument + '){\n' + source + '}';

        return template;
    })
})(window);

/* ========================================================================
 * Bootstrap: ncLink 根据选择的连接方式获取不同的验证
 * 使用方法：
 * $linkType.ncLink({
 *    target: '#linkValue'
 * });
 * ======================================================================== */
(function (win, $) {
    var NcLink = function (ele, options) {
        this.options = options;

        this.$element = $(ele);
        this.$target = $.type(this.options.target) === "string" ? $(this.options.target) : this.options.target;
        this.$form = this.$element.closest("form");
        if (!this.$element.length) throw new Error("没有找到插件所需要的元素");
        if (!this.$target.length) throw new Error("没有找到插件所需要目标元素");

        this.attrsArray = [];
        this._init();

    };
    /**
     * 重新设置第一个选中
     */
    NcLink.prototype.reset = function () {
        this.$element.find("option:first").prop("selected", true).trigger("change");
    };
    /**
     * 设置选中
     */
    NcLink.prototype.setValue = function (value) {
        this.$element.val(value).trigger('change');
    };
    /**
     * 修改验证数据 内容
     * @param key
     * @param value
     */
    NcLink.prototype.setLinkModParsleyData = function (key, value) {
        if (this.options.selectLinkModParsleyData [key]) {
            this.options.selectLinkModParsleyData [key] = value;
            this._init();
        }
    };


    NcLink.prototype._init = function () {
        var self = this,
            optionsArray = [];
        $.each(this.options.selectLinkModParsleyData, function (i, n) {
            optionsArray.push(' <option value="' + i + '" >' + n['data-nc-name'] + '</option>');
            if (n) {
                $.each(n, function (ii, nn) {
                    self.attrsArray.indexOf(ii) < 0 && self.attrsArray.push(ii);
                });
            }
        });
        this.$element.empty().append(optionsArray.join(""));
        this._bindEvents();
        this.reset();
    };

    NcLink.prototype._bindEvents = function () {
        var self = this;
        self.$element.off("change").on("change", function () {
            var $selected = self.$element.find("option:selected"),
                _selectedValue = $.trim($selected.val()),
                _attrs = self._getAttrs(_selectedValue);
            if (self.$form.length) self.$form.parsley().reset();
            self.attrsArray.length && self.$target.removeAttr(self.attrsArray.join(" "));
            self.$target.attr(_attrs);

            if (_selectedValue == 'none') {
                self.$target.val("");
            }
        });
    };

    /**
     * 获取需要去掉的attr
     * @private
     */
    NcLink.prototype._getAttrs = function (key) {
        return this.options.selectLinkModParsleyData[key] || {};
    };

    /**
     * 默认设置
     * @type {{}}
     */
    NcLink.defaultOptions = {
        form: "",
        target: "",
        //验证数据
        selectLinkModParsleyData: {
            none: {
                "readonly": "readonly",
                "data-nc-name": "无操作",
                placeholder: "点击内容不进行任何形式的链接操作"
            },
            url: {
                "data-nc-name": "链接地址",
                "data-parsley-type": 'url',
                'data-parsley-required': 'true',
                placeholder: "请输入以http://开头的网址，例如“http://www.***.com”格式。",
                maxlength: 50,
                "data-parsley-maxlength": 50
            },
            keyword: {
                "data-nc-name": "关键字",
                placeholder: "请输入要搜索的关键字，点击该链接将进行站内搜索；例如“商品”、“品牌”。",
                maxlength: 10,
                "data-parsley-maxlength": 10,
                'data-parsley-required': 'true',
            },
            special: {
                "data-parsley-type": "number",
                'data-parsley-required': 'true',
                "data-nc-name": "专题编号",
                placeholder: "请输入专题ID编号，点击跳转对应的专题页面；例如“1”、“2”。"
            },
            goods: {
                "data-parsley-type": "number",
                'data-parsley-required': 'true',
                "data-nc-name": "商品编号",
                placeholder: "请输入商品ID编号，点击跳转对应的商品详情页面；例如“1”、“2”。"
            },

            store: {
                "data-parsley-type": "number",
                'data-parsley-required': 'true',
                "data-nc-name": "店铺编号",
                placeholder: "请输入店铺ID编号，点击跳转对应的店铺首页；例如“1”、“2”。"
            },

            category: {
                "data-parsley-type": "number",
                'data-parsley-required': 'true',
                "data-nc-name": "商品分类",
                placeholder: "请输入商品分类ID类型的数字编号，点击跳转对应的分类列表页面；例如“1”、“2”。"
            }

        }
    };


    function Plugin(option) {
        var self = this;
        this.each(function () {
            var $this = $(this),
                data = $this.data('nc.link'),
                options = $.extend({}, NcLink.defaultOptions, $this.data(), typeof option == 'object' && option)
            if (!data) $this.data('nc.link', (data = new NcLink(this, options)));
            if (typeof option == 'string') data[option]();
        });
        return {
            setValue: function (value) {
                self.each(function () {
                    var $this = $(this),
                        data = $this.data('nc.link');
                    data.setValue(value);
                });
            },
            reset: function () {
                self.each(function () {
                    var $this = $(this),
                        data = $this.data('nc.link');
                    data.reset();
                });
            },
            setLinkModParsleyData: function (key, value) {
                self.each(function () {
                    var $this = $(this),
                        data = $this.data('nc.link');
                    data.setLinkModParsleyData(key, value);
                });
            }

        };
    }

    $.fn.ncLink = Plugin;
    $.fn.ncLink.Constructor = NcLink;
})(window, jQuery);
