/*   
 JavaScript Name: ShopNC-B2B2C-Java版admincp模板风格控制JS
 Version: 1.0.0
 */

var green = "#00acac",
    red = "#ff5b57",
    blue = "#348fe2",
    purple = "#727cb6",
    orange = "#f59c1a",
    black = "#2d353c";
var renderSwitcher = function () {
    if ($("[data-render=switchery]").length !== 0) {
        $("[data-render=switchery]").each(function () {
            var e = green;
            if ($(this).attr("data-theme")) {
                switch ($(this).attr("data-theme")) {
                    case "red":
                        e = red;
                        break;
                    case "blue":
                        e = blue;
                        break;
                    case "purple":
                        e = purple;
                        break;
                    case "orange":
                        e = orange;
                        break;
                    case "black":
                        e = black;
                        break
                }
            }
            var t = {};
            t.color = e;
            t.secondaryColor = $(this).attr("data-secondary-color") ? $(this).attr("data-secondary-color") : "#dfdfdf";
            t.className = $(this).attr("data-classname") ? $(this).attr("data-classname") : "switchery";
            t.disabled = $(this).attr("data-disabled") ? true : false;
            t.disabledOpacity = $(this).attr("data-disabled-opacity") ? $(this).attr("data-disabled-opacity") : .5;
            t.speed = $(this).attr("data-speed") ? $(this).attr("data-speed") : "0.5s";
            var n = new Switchery(this, t)
        })
    }
};
var checkSwitcherState = function () {
    $('[data-click="check-switchery-state"]').live("click", function () {
        alert($('[data-id="switchery-state"]').prop("checked"))
    });
    $('[data-change="check-switchery-state-text"]').live("change", function () {
        $('[data-id="switchery-state-text"]').text($(this).prop("checked"))
    })
};
var renderPowerRangeSlider = function () {
    if ($('[data-render="powerange-slider"]').length !== 0) {
        $('[data-render="powerange-slider"]').each(function () {
            var e = {};
            e.decimal = $(this).attr("data-decimal") ? $(this).attr("data-decimal") : false;
            e.disable = $(this).attr("data-disable") ? $(this).attr("data-disable") : false;
            e.disableOpacity = $(this).attr("data-disable-opacity") ? $(this).attr("data-disable-opacity") : .5;
            e.hideRange = $(this).attr("data-hide-range") ? $(this).attr("data-hide-range") : false;
            e.klass = $(this).attr("data-class") ? $(this).attr("data-class") : "";
            e.min = $(this).attr("data-min") ? $(this).attr("data-min") : 0;
            e.max = $(this).attr("data-max") ? $(this).attr("data-max") : 100;
            e.start = $(this).attr("data-start") ? $(this).attr("data-start") : null;
            e.step = $(this).attr("data-step") ? $(this).attr("data-step") : null;
            e.vertical = $(this).attr("data-vertical") ? $(this).attr("data-vertical") : false;
            if ($(this).attr("data-height")) {
                $(this).closest(".slider-wrapper").height($(this).attr("data-height"))
            }
            var t = new Switchery(this, e);
            var n = new Powerange(this, e)
        })
    }
};
var FormSliderSwitcher = function () {
    "use strict";
    return {
        init: function () {
            renderSwitcher();
            checkSwitcherState();
            renderPowerRangeSlider()
        }
    }
}()