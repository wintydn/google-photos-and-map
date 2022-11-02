class MapActions {

	constructor(args) {
		this.map = args.map;
	}

	addControl({ onclick }) {
		const btn = document.createElement('button');
		btn.textContent = 'Add an album';
		btn.className = 'm-3 btn btn-light shadow-lg hide-when-adding';
		btn.onclick = onclick;
		map.controls[google.maps.ControlPosition.TOP_RIGHT].push(btn);
	}

}