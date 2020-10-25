'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

function _toConsumableArray(arr) {
  return _arrayWithoutHoles(arr) || _iterableToArray(arr) || _nonIterableSpread();
}

function _arrayWithoutHoles(arr) {
  if (Array.isArray(arr)) {
    for (var i = 0, arr2 = new Array(arr.length); i < arr.length; i++) arr2[i] = arr[i];

    return arr2;
  }
}

function _iterableToArray(iter) {
  if (Symbol.iterator in Object(iter) || Object.prototype.toString.call(iter) === "[object Arguments]") return Array.from(iter);
}

function _nonIterableSpread() {
  throw new TypeError("Invalid attempt to spread non-iterable instance");
}

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
var script = {
  name: "GooglePlacesAutocomplete",
  data: function data() {
    return {
      autocomplete: {
        service: null,
        sessionToken: null,
        results: [],
        resultsHighlight: 0,
        status: null,
        selected: this.place
      },
      context: {
        input: this.value,
        disableSearch: false
      }
    };
  },
  props: {
    bounds: {
      type: Object,
      required: false,
      default: null
    },
    fields: {
      type: Array,
      required: false,
      default: function _default() {
        return [];
      }
    },
    value: {
      type: String,
      required: false,
      default: ''
    },
    place: {
      type: Object,
      required: false,
      default: function _default() {
        return {};
      }
    }
  },
  computed: {
    hasResults: function hasResults() {
      return this.autocomplete.results.length > 0;
    },
    searchValue: function searchValue() {
      return this.context.input;
    },
    resultField: function resultField() {
      return ['formatted_address', 'geometry'].concat(_toConsumableArray(this.fields));
    }
  },
  watch: {
    value: {
      handler: function handler(value) {
        if (!value) return;
        this.$set(this.context, 'input', value);
      },
      immediate: true
    },
    place: {
      handler: function handler(value) {
        if (!value) return;
        this.$set(this.autocomplete, 'selected', value);
      },
      immediate: true
    },
    searchValue: function searchValue(newValue, oldValue) {
      if (newValue || !oldValue) return;
      this.$emit('resultCleared');
    }
  },
  methods: {
    initGoogleAutoCompleteService: function initGoogleAutoCompleteService() {
      this.$set(this.autocomplete, 'sessionToken', new window.google.maps.places.AutocompleteSessionToken());
      this.$set(this.autocomplete, 'service', new window.google.maps.places.AutocompleteService());
    },
    selectItemFromList: function selectItemFromList() {
      var _this$autocomplete = this.autocomplete,
          results = _this$autocomplete.results,
          resultsHighlight = _this$autocomplete.resultsHighlight,
          selected = _this$autocomplete.selected;
      var input = this.context.input;
      /**
       * Bail if there is nothing to work with
       */

      if (!input && !results.length) {
        return;
      }
      /**
       * Return the last result if things haven't changed
       */


      if (input === this.value && Object.keys(selected).length) {
        return this.returnLastSelection();
      }
      /**
       * Show the search results again
       */


      if (input && !results.length) {
        return this.inputHasChanged();
      }
      /**
       * The expected standard user journey. The user selected a result from the list.
       */


      this.resultHasBeenSelected(results[resultsHighlight] || place);
    },
    shiftResultsSelection: function shiftResultsSelection() {
      var _this$autocomplete2 = this.autocomplete,
          results = _this$autocomplete2.results,
          resultsHighlight = _this$autocomplete2.resultsHighlight;
      var newIndex = Math.min(results.length, resultsHighlight) + 1;
      if (newIndex >= results.length) newIndex = 0;
      this.$set(this.autocomplete, 'resultsHighlight', newIndex);
    },
    unshiftResultsSelection: function unshiftResultsSelection() {
      var _this$autocomplete3 = this.autocomplete,
          results = _this$autocomplete3.results,
          resultsHighlight = _this$autocomplete3.resultsHighlight;
      var newIndex = Math.min(results.length, resultsHighlight) - 1;
      if (newIndex < 0) newIndex = results.length - 1;
      this.$set(this.autocomplete, 'resultsHighlight', newIndex);
    },
    inputHasReceivedFocus: function inputHasReceivedFocus() {
      if (this.autocomplete.service) return;
      this.initGoogleAutoCompleteService();
    },
    inputHasChanged: function inputHasChanged() {
      var _this = this;

      var _this$autocomplete4 = this.autocomplete,
          service = _this$autocomplete4.service,
          sessionToken = _this$autocomplete4.sessionToken;
      var input = this.context.input;
      var bounds = this.bounds;
      this.$set(this.autocomplete, 'resultsHighlight', 0);

      if (!input) {
        this.$set(this.autocomplete, 'selected', {});
        this.$set(this.autocomplete, 'results', []);
        return;
      }

      service.getPlacePredictions({
        input: input,
        sessionToken: sessionToken,
        bounds: bounds
      }, function (predictions, status) {
        _this.$set(_this.autocomplete, 'status', status);

        if (status !== window.google.maps.places.PlacesServiceStatus.OK) return;

        _this.$set(_this.autocomplete, 'results', predictions);
      });
    },
    resultHasBeenSelected: function resultHasBeenSelected(_ref) {
      var _this2 = this;

      var placeId = _ref.place_id,
          description = _ref.description;
      var placeService = new window.google.maps.places.PlacesService(document.createElement('div'));
      placeService.getDetails({
        placeId: placeId,
        fields: this.resultFields
      }, function (place) {
        _this2.$set(_this2.autocomplete, 'selected', place);

        _this2.$set(_this2.context, 'input', description);

        _this2.$set(_this2.autocomplete, 'results', []);

        _this2.$emit('resultChanged', place);
      });
    },
    returnLastSelection: function returnLastSelection() {
      var place = this.autocomplete.selected;
      if (!place) return;
      this.$emit('resultChanged', place);
    }
  }
};

function normalizeComponent(template, style, script, scopeId, isFunctionalTemplate, moduleIdentifier
/* server only */
, shadowMode, createInjector, createInjectorSSR, createInjectorShadow) {
  if (typeof shadowMode !== 'boolean') {
    createInjectorSSR = createInjector;
    createInjector = shadowMode;
    shadowMode = false;
  } // Vue.extend constructor export interop.


  var options = typeof script === 'function' ? script.options : script; // render functions

  if (template && template.render) {
    options.render = template.render;
    options.staticRenderFns = template.staticRenderFns;
    options._compiled = true; // functional template

    if (isFunctionalTemplate) {
      options.functional = true;
    }
  } // scopedId


  if (scopeId) {
    options._scopeId = scopeId;
  }

  var hook;

  if (moduleIdentifier) {
    // server build
    hook = function hook(context) {
      // 2.3 injection
      context = context || // cached call
      this.$vnode && this.$vnode.ssrContext || // stateful
      this.parent && this.parent.$vnode && this.parent.$vnode.ssrContext; // functional
      // 2.2 with runInNewContext: true

      if (!context && typeof __VUE_SSR_CONTEXT__ !== 'undefined') {
        context = __VUE_SSR_CONTEXT__;
      } // inject component styles


      if (style) {
        style.call(this, createInjectorSSR(context));
      } // register component module identifier for async chunk inference


      if (context && context._registeredComponents) {
        context._registeredComponents.add(moduleIdentifier);
      }
    }; // used by ssr in case component is cached and beforeCreate
    // never gets called


    options._ssrRegister = hook;
  } else if (style) {
    hook = shadowMode ? function () {
      style.call(this, createInjectorShadow(this.$root.$options.shadowRoot));
    } : function (context) {
      style.call(this, createInjector(context));
    };
  }

  if (hook) {
    if (options.functional) {
      // register for functional component in vue file
      var originalRender = options.render;

      options.render = function renderWithStyleInjection(h, context) {
        hook.call(context);
        return originalRender(h, context);
      };
    } else {
      // inject component registration as beforeCreate hook
      var existing = options.beforeCreate;
      options.beforeCreate = existing ? [].concat(existing, hook) : [hook];
    }
  }

  return script;
}

var normalizeComponent_1 = normalizeComponent;

/* script */
const __vue_script__ = script;

/* template */
var __vue_render__ = function() {
  var _vm = this;
  var _h = _vm.$createElement;
  var _c = _vm._self._c || _h;
  return _c(
    "div",
    [
      _vm._t(
        "input",
        [
          _c("input", {
            directives: [
              {
                name: "model",
                rawName: "v-model",
                value: _vm.context.input,
                expression: "context.input"
              }
            ],
            staticClass: "vbga-input",
            attrs: { type: "search" },
            domProps: { value: _vm.context.input },
            on: {
              focus: _vm.inputHasReceivedFocus,
              input: [
                function($event) {
                  if ($event.target.composing) {
                    return
                  }
                  _vm.$set(_vm.context, "input", $event.target.value);
                },
                _vm.inputHasChanged
              ],
              keydown: [
                function($event) {
                  if (
                    !$event.type.indexOf("key") &&
                    _vm._k($event.keyCode, "enter", 13, $event.key, "Enter")
                  ) {
                    return null
                  }
                  $event.preventDefault();
                  return _vm.selectItemFromList($event)
                },
                function($event) {
                  if (
                    !$event.type.indexOf("key") &&
                    _vm._k($event.keyCode, "down", 40, $event.key, [
                      "Down",
                      "ArrowDown"
                    ])
                  ) {
                    return null
                  }
                  $event.preventDefault();
                  return _vm.shiftResultsSelection($event)
                },
                function($event) {
                  if (
                    !$event.type.indexOf("key") &&
                    _vm._k($event.keyCode, "up", 38, $event.key, [
                      "Up",
                      "ArrowUp"
                    ])
                  ) {
                    return null
                  }
                  $event.preventDefault();
                  return _vm.unshiftResultsSelection($event)
                }
              ]
            }
          })
        ],
        {
          context: _vm.context,
          actions: {
            selectItemFromList: _vm.selectItemFromList,
            shiftResultsSelection: _vm.shiftResultsSelection,
            unshiftResultsSelection: _vm.unshiftResultsSelection
          },
          events: {
            inputHasReceivedFocus: _vm.inputHasReceivedFocus,
            inputHasChanged: _vm.inputHasChanged
          }
        }
      ),
      _vm._v(" "),
      _vm.hasResults
        ? _c(
            "ul",
            { staticClass: "vbga-results" },
            _vm._l(_vm.autocomplete.results, function(result, index) {
              return _c(
                "li",
                {
                  key: result.id,
                  class: {
                    highlighted: index === _vm.autocomplete.resultsHighlight
                  },
                  style:{
                    listStyleType:"none"
                  },
                  on: {
                    click: function($event) {
                      return _vm.resultHasBeenSelected(result)
                    }
                  }
                },
                [
                  index !== _vm.autocomplete.resultsHighlight
                    ? _vm._t(
                        "item",
                        [
                          _vm._v(
                            "\n                " +
                              _vm._s(result.description) +
                              "\n            "
                          )
                        ],
                        { place: result }
                      )
                    : _vm._e(),
                  _vm._v(" "),
                  index === _vm.autocomplete.resultsHighlight
                    ? _vm._t(
                        "activeItem",
                        [
                          _vm._v(
                            "\n                " +
                              _vm._s(result.description) +
                              "\n            "
                          )
                        ],
                        { place: result }
                      )
                    : _vm._e()
                ],
                2
              )
            }),
            0
          )
        : _vm._e()
    ],
    2
  )
};
var __vue_staticRenderFns__ = [];
__vue_render__._withStripped = true;

  /* style */
  const __vue_inject_styles__ = undefined;
  /* scoped */
  const __vue_scope_id__ = undefined;
  /* module identifier */
  const __vue_module_identifier__ = undefined;
  /* functional template */
  const __vue_is_functional_template__ = false;
  /* style inject */
  
  /* style inject SSR */
  

  
  var GooglePlacesAutocomplete = normalizeComponent_1(
    { render: __vue_render__, staticRenderFns: __vue_staticRenderFns__ },
    __vue_inject_styles__,
    __vue_script__,
    __vue_scope_id__,
    __vue_is_functional_template__,
    __vue_module_identifier__,
    undefined,
    undefined
  );

var GooglePlacesAutocompletePlugin = {
  install: function install(Vue) {
    Vue.component(GooglePlacesAutocomplete);
  }
};

exports.GooglePlacesAutocomplete = GooglePlacesAutocomplete;
exports.GooglePlacesAutocompletePlugin = GooglePlacesAutocompletePlugin;
exports.default = GooglePlacesAutocompletePlugin;
