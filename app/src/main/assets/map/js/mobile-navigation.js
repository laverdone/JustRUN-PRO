// initialize map when page ready
var map;
var markers = new OpenLayers.Layer.Markers( "Markers" );
var size = new OpenLayers.Size(21,25);
var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
var icon = new OpenLayers.Icon('./js/img/biking.png',size,offset);

var init = function () {
    // create map
    map = new OpenLayers.Map({
        div: "map",
        theme: null,
        controls: [
            new OpenLayers.Control.Attribution(),
            new OpenLayers.Control.TouchNavigation({
                dragPanOptions: {
                    enableKinetic: true
                }
            }),
            new OpenLayers.Control.ZoomPanel()
        ],
        layers: [
            new OpenLayers.Layer.OSM("OpenStreetMap", null, {
                transitionEffect: 'resize'
            })
        ],
        center: new OpenLayers.LonLat(0, 0),
        zoom: 16
    });
};

function movepoint(){
 	
 	  		
	var newLonLat = new OpenLayers.LonLat( iLon,iLat)
      .transform(
        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
        map.getProjectionObject() // to Spherical Mercator Projection
      );
	
 	map.addLayer(markers);

  	markers.addMarker(new OpenLayers.Marker(newLonLat,icon));

 	map.setCenter (newLonLat , zoom);
}
