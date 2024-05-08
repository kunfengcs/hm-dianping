navigator.geolocation.getCurrentPosition(function(position) {
    let latitude = position.coords.latitude;
    let longitude = position.coords.longitude;
    app.addMaker('https://a.amap.com/jsapi/static/image/plugin/point.png', longitude, latitude);
});