<!DOCTYPE HTML>
<html lang="en">
<head>
<title>Wint Google Demo</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="https://maps.googleapis.com/maps/api/js?key="></script>
<script src="/js/jquery.js"></script>
<script src="/js/bootstrap.js"></script>
<script src="/js/app.js"></script>
<script src="/js/map-actions.js"></script>
<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/css/app.css" />
</head>
<body>
	<div>
		<div id="add-place">
			<div class="center-pin-shadow"></div>
			<img alt="" width="44px" height="auto"
				src="data:image/png;base64,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"
				class="center-pin" />
			<div class="m-3">
				<div class="card border-light mb-3 shadow" style="max-width: 20rem; margin-left: auto; margin-right: 0">
					<div class="card-header">Add an album</div>
					<div class="card-body">
						<div class="form-group">
							<div class="form-floating mb-3 mt-1">
								<input type="text" class="form-control" id="album-name"
									placeholder="Album Name"> <label for="album-name">Album
									Name</label>
							</div>
							<button type="button" class="btn btn-sm btn-primary" onclick="addAlbum(this)">Add</button>
							<button type="button" class="btn btn-sm btn-light"
								onclick="toggleAddPlace('off')">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="place-details" class="p-3 hide-when-adding hidden border-right"></div>
		<div id="map"></div>
	</div>
	<div class="modal-backdrop fade app-modal"></div>
	<div class="app-modal m-2" id="add-image">
		<div class="card border-light mb-3 shadow w-100">
			<div class="card-header">Add an image</div>
				<div class="card-body">
					<div class="form-group">
						<div class="form-group mb-3">
	      					<input class="form-control" type="file" id="file">
	    				</div>
						<button type="button" class="btn btn-sm btn-primary" onclick="upload(this)">Upload</button>
						<button type="button" class="btn btn-sm btn-light"
							onclick="closeModal()">Cancel</button>
					</div>
				</div>
			</div>
	</div>
	<script>
		let map, add, ready;		
		const markers = [];
		const pin = document.querySelector('.center-pin');
		const addPlaceEl = $('#add-place').hide();
		const albumNameEl = $('#album-name');
		const modal = $('.app-modal').hide();
		const file = document.getElementById('file');

		window.onload = async function() {	
			const sg = { lat: 1.360362, lng: 103.816097 };
			
			map = new google.maps.Map(document.getElementById("map"), {
				center : sg,
				zoom : 12,
				fullscreenControl: false,
				clickableIcons: false
			});
			
 			const actions = new MapActions({ map });
 			actions.addControl({ onclick: () => ready && toggleAddPlace('on') });

			google.maps.event.addListener(map, 'dragstart', () => pin.classList.add('moving'));
			google.maps.event.addListener(map, 'dragend', () => pin.classList.remove('moving'));
			
			await refreshMarkers();
			ready = true;
		}
		
		function closeModal() {
			file.value = '';
			modal.hide().removeClass('show');
		}
		
		function toggleAddPlace(mode) {
			if (mode === 'on') {
				add = true;
 				$('.hide-when-adding').hide();
 				setTimeout(() => addPlaceEl.show(), 200);
			} else {
				add = false;
				albumNameEl.val('');
 				addPlaceEl.hide();
 				$('.hide-when-adding').show();
			}
		}
		
		function addAlbum(btn) {
			const { lat, lng } = map.center;
			const albumName = albumNameEl.val();
			
			if (!albumName || !albumName.trim().length) {
				alert('Album name should not be empty!');
				return;
			}
			
			btn.disabled = true;
			Api.addAlbum({ lat: lat(), lng: lng(), albumName })
				.then(() => {
					toggleAddPlace('off');
					refreshMarkers();
				})
				.finally(_ => btn.disabled = false);
		}
		
		async function refreshMarkers() {
			while (markers.length)
				markers.pop().setMap(null);
			
			await Api.albums().then(albums => albums && albums.forEach(addMarker(map)));
		}
		
		function addMarker(map) {
			return async function ({ id, title, lat, lng }) {
				const markerEl = document.createElement('div');
				markerEl.className = 'custom-marker shadow-sm hide-when-adding';
				markerEl.textContent = title;
				
				const marker = new HTMLMapMarker({
					latlng: { lat, lng }, map, content: markerEl,
					containerClass: 'custom-marker-container'
				});
				markers.push(marker);
				
				markerEl.addEventListener("click", async function() {
					if (add) return;					
					selectAlbum(markerEl);
					selected.clear();
					const data = await Api.mediaItems(id, null);
					data.title = title;
					selected.id = data.id = id;
					selected.el = markerEl;
					selected.update(data);
				});
			}
		}
		
		const placeDetails = $('#place-details');
		const selected = {
			clear() {
				placeDetails.text('');
				placeDetails.addClass('hidden');
			},
			refresh() {
				this.clear();
				this.el.click();
			},
			update(data) {
				const titleContainer = $('<div>').addClass('d-flex justify-content-between align-items-center');
				const title = $('<span>').addClass('h4 m-0').text(data.title);
				const addImgBtn = $('<btn>').text('Add Image').addClass('btn btn-dark');
				
				addImgBtn.on('click', () => {
					modal.show().addClass('show');
				});
				
				titleContainer.append(title).append(addImgBtn);
				placeDetails.append(titleContainer).append($('<hr>'));
				this.loadImages(data.mediaItems, data.id, data.nextPageToken);
				placeDetails.removeClass('hidden');
			},
			checkNextPage(id, nextPageToken) {
				if (nextPageToken) {
					const link = $('<button>').addClass('btn btn-link')
						.text('Load more');
					link.on('click', async () => {
						placeDetails.find(link).remove();
						const data = await Api.mediaItems(id, nextPageToken);
						this.loadImages(data.mediaItems, id, data.nextPageToken);
					});
					placeDetails.append(link);
				}
			},
			loadImages(images, id, nextPageToken) {
				if (images && images.length) {
					images.forEach(img => {
						const imgEl = $('<img>')
							.attr('alt', '')
							.attr('src', img.baseUrl)
							.css('width', '100%')
							.css('height', 'auto')
							.css('border-radius', '12px');
						placeDetails.append(
							$('<div>')
								.addClass('mt-3')
								.append(imgEl));
					});
				} else {
					const noRecordTxt = $('<div>').addClass('text-muted text-sm mt-4');
					placeDetails.append(noRecordTxt.text('No image found in this album.'));
				}
				this.checkNextPage(id, nextPageToken);
			}
		};
		
		function selectAlbum(newEl) {
			const className = 'selected';
			selected.el && selected.el.classList.remove(className);
			selected.el = newEl;
			newEl && newEl.classList.add(className);
		}
		
		function upload(btn) {
			if (!file.files.length) return;
			btn.disabled = true;
			Api.upload({ file: file.files[0], albumId: selected.id })
				.then(closeModal)
				.then(_ =>selected.refresh())
				.finally(_ => btn.disable = false);
		}
		
	</script>
</body>
</html>