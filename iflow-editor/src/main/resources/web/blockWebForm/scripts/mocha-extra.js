/**
 * Custom extensions to mocha-ui
 * 
 */
MochaUI.extend({
	NewWindowsFromHTMLP: function NewWindowsFromHTMLP(props){
		$$('div.mocha').each(function(el) {
			// Get the window title and destroy that element, so it does not end up in window content
			if ( Browser.Engine.presto || Browser.Engine.trident5 ){
				el.setStyle('display','block'); // Required by Opera, and probably IE7
			}
			var title = el.getElement('h3.mochaTitle');
			var elDimensions = el.getStyles('height', 'width');
			var properties = $merge({
				id: el.getProperty('id'),
				height: elDimensions.height.toInt(),
				width: elDimensions.width.toInt(),
				x: el.getStyle('left').toInt(),
				y: el.getStyle('top').toInt()
			}, props);
			// If there is a title element, set title and destroy the element so it does not end up in window content
			if ( title ) {
				properties.title = title.innerHTML;
				title.destroy();
			}
		
			// Get content and destroy the element
			properties.content = el.innerHTML;
			el.destroy();
			
			// Create window
			new MochaUI.Window(properties, true);
		}.bind(this));
	},
	
	updateResize: function updateResize(element) {
		var coords, windowEl
		element = $(element);
		windowEl = MochaUI.Windows.instances.get(element.id);
		windowEl.resizeOnStart();
		coords = element.getCoordinates();
		windowEl.contentWrapperEl.setStyle('height', coords.height.toInt());
		FormEditor.debug('height: '+coords.height);
		windowEl.contentWrapperEl.setStyle('width', coords.width.toInt());
		FormEditor.debug('width: '+coords.width);
		windowEl.drawWindow(windowEl);
		windowEl.adjustHandles();
		windowEl.resizeOnComplete();
	}
});
