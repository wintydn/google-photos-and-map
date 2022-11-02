class HTMLMapMarker extends google.maps.OverlayView {
	constructor(args) {
		super();
		this.latlng = args.latlng;
		this.content = args.content;
		this.containerClass = args.containerClass;
		this.setMap(args.map);
	}
	createDiv() {
		this.div = document.createElement('div');
		this.div.style.position = 'absolute';
		this.div.classList.add(this.containerClass);
		if (this.content) {
			this.div.appendChild(this.content);
		}
	}

	appendDivToOverlay() {
		const panes = this.getPanes();
		panes.floatPane.appendChild(this.div);
	}

	positionDiv() {
		const point = this.getProjection().fromLatLngToDivPixel(this.latlng);
		if (point) {
			this.div.style.left = `${point.x}px`;
			this.div.style.top = `${point.y}px`;
		}
	}

	draw() {
		if (!this.div) {
			this.createDiv();
			this.appendDivToOverlay();
		}
		this.positionDiv();
	}

	remove() {
		if (this.div) {
			this.div.parentNode.removeChild(this.div);
			this.div = null;
		}
	}

	getPosition() {
		return this.latlng;
	}
}

class Api {

	static albums() {
		return new Promise(function(success, error) {
			$.ajax({ url: '/api/albums', type: 'GET', success, error });
		});
	}

	static mediaItems(albumId, nextPageToken) {
		return new Promise(function(success, error) {
			$.ajax({
				url: '/api/album/media-items', type: 'POST', success, error,
				data: JSON.stringify({ albumId, nextPageToken }),
				contentType: "application/json",
			});
		});
	}

	static addAlbum({ albumName, lat, lng }) {
		return new Promise(function(success, error) {
			const data = new FormData();
			data.append('albumName', albumName);
			data.append('lat', lat);
			data.append('lng', lng);
			$.ajax({
				url: '/api/create-album', type: 'POST', success, error, data,
				processData: false, contentType: false
			});
		});
	}
	
	static upload({ file, albumId }) {
		return new Promise(function(success, error) {
			const data = new FormData();
			data.append('albumId', albumId);
			data.append('file', file);
			$.ajax({
				url: '/api/upload', type: 'POST', success, error, data,
				processData: false, contentType: false
			});
		});
	}

}
