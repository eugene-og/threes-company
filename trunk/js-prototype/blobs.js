/* Table definition */
var cells = new Array();
cells[0] = 'id';
cells[1] = 'type';
cells[2] = 'px';
cells[3] = 'py';
cells[4] = 'using_force';
cells[5] = 'vx';
cells[6] = 'vy';
cells[7] = 'size';
cells[8] = 'strength';
cells[9] = 'status';

var maxBlobSize = 4;
var blobGrowRate = 0.095;
var universalConstantOfForce = 8;

var boardPixelSize = 300;

var svgNS = "http://www.w3.org/2000/svg";
var xlinkNS = "http://www.w3.org/1999/xlink";

function initialDrawBlobs() {
	var blobs = getBlobsFromTable();
	if (useGraphics()) drawBlobs(blobs);
	fillTableWithBlobs(blobs);
}

function takeTurn() {
	var blobs = getBlobsFromTable();
	var livingBlobs = getLivingBlobs(blobs);
	
	growBlobs(livingBlobs);
	recalculateVelocities(livingBlobs);
	moveBlobs(livingBlobs);
	checkForCollisions(livingBlobs);

	updateDisplay(blobs);
}

function updateDisplay(blobs) {
	fillTableWithBlobs(blobs);
	if (useGraphics()) drawBlobs(blobs);
}

function useGraphics() {
	return (navigator.product == "Gecko");
}

function getLivingBlobs(blobs) {
	var result = new Array();
	for (var i = 0; i < blobs.length; i++)
		if (!isDead(blobs[i])) result[result.length] = blobs[i];
	return result;
}

function growBlobs(blobs) {
	for (var i = 0; i < blobs.length; i++) {
		blobs[i]['size'] *= (1 + blobGrowRate);
		if (blobs[i]['size'] > maxBlobSize)
			blobs[i]['size'] = maxBlobSize;
	}
}

function recalculateVelocities(blobs) {
	for (var i = 0; i < blobs.length; i++) {
		blobs[i].last_force = '';
		for (var j = 0; j < blobs.length; j++)
			applyForceFrom(blobs[j], blobs[i]);
	}
}

function applyForceFrom(actor, victim) {
	if (actor.id == victim.id || actor.using_force != 'Yes') return;

	var r = distanceFrom(actor, victim);
	var theta = angleBetween(actor, victim);
	
	var force = actor.strength * universalConstantOfForce;
	if (actor.type == 'Pull') force *= -1;
	
	// Rule: Force is strength * const over square of distance
	force /= r * r;

	var Fx = force * Math.cos(theta);
	var Fy = force * Math.sin(theta);
	
	// Weight is proportional to square of size
	victim.vx = (victim.vx * 1) + (Fx / (victim.size * victim.size));
	victim.vy = (victim.vy * 1) + (Fy / (victim.size * victim.size));
	// Don't forget Newton's 3rd law
	actor.vx = (actor.vx * 1) - (Fx / (actor.size * actor.size));
	actor.vy = (actor.vy * 1) - (Fy / (actor.size * actor.size));
}

function drawBlobs(blobs) {
	var group = blobSvgGroup();
	removeAllChildNodes(group);
	
	var doc = group.ownerDocument;
	
	var livingBlobs = getLivingBlobs(blobs);
	/* Determine scaling parameters for the board. */
	var minX = getMinimum(livingBlobs, "x");
	var maxX = getMaximum(livingBlobs, "x");
	var minY = getMinimum(livingBlobs, "y");
	var maxY = getMaximum(livingBlobs, "y");
	
	var scaleFactor = getScaleFactor(minX, maxX, minY, maxY);
	
	for (var i = 0; i < blobs.length; i++) {
		if (isDead(blobs[i])) continue;
		var blob = doc.createElementNS(svgNS, "circle");
		blob.setAttributeNS(null, "cx", scale(blobs[i].px, minX, scaleFactor));
		blob.setAttributeNS(null, "cy", scale(blobs[i].py, minY, scaleFactor));
		blob.setAttributeNS(null, "r",  scale(blobs[i].size,  0, scaleFactor));
		blob.id = "blob" + blobs[i].id;
		var clName = '';
		if (blobs[i].type == 'Pull')	clName += " puller";
		if (blobs[i].type == 'Push')	clName += " pusher";
		if (blobs[i].using_force == 'No') clName += " inactive";
		
		blob.setAttribute("class", clName);
		group.appendChild(blob);
		
		addActivationEvent(blob, blobs[i]);
		checkSpawnable(blobs[i], blob, group, doc);
	}
}

function addActivationEvent(obj, blob) {
	obj.onclick = function(ev) { activateBlob(blob); };
}

function checkSpawnable(blob, obj, group, doc) {
	if (!canSpawn(blob)) return;
			
	var overlay = doc.createElementNS(svgNS, "circle");
	overlay.setAttributeNS(null, "cx", obj.getAttributeNS(null, "cx"));
	overlay.setAttributeNS(null, "cy", obj.getAttributeNS(null, "cy"));
	overlay.setAttributeNS(null, "r",  obj.getAttributeNS(null, "r") / 2);
	overlay.setAttribute("class", "spawnable");
	group.appendChild(overlay);

	overlay.onclick = function(ev) {
		spawnBlob(blob);
	};
}

function activateBlob(blob) {
	var blobs = getBlobsFromTable();
	for (var i = 0; i < blobs.length; i++)
	if (blob.id == blobs[i].id)
		blobs[i].using_force = (blobs[i].using_force == 'Yes'
					? 'No' : 'Yes');
	updateDisplay(blobs);
}

function spawnBlob(blob) {
	var blobs = getBlobsFromTable();

	for (var i = 0; i < blobs.length; i++)
	if (blob.id == blobs[i].id) {
		blob = blobs[i];
		break;
	}

	var newSize = blob.size / 2.5;
	blob.px -= (blob.size / 2);
	blob.py -= (blob.size / 2);
	
	var newBlob = new Object();
	for (i in blob) newBlob[i] = blob[i];
	newBlob.px += (1*blob.size);
	newBlob.id = blobs.length;
	
	newBlob.using_force = blob.using_force = 'No';
	newBlob.size = blob.size = newSize;
	
	blobs[blobs.length] = newBlob;
	
	/* TODO: Resultant velocity? */
	updateDisplay(blobs);
}

function canSpawn(blob) {
	return (blob.size >= Math.sqrt(2));
}

function getScaleFactor(minX, maxX, minY, maxY) {
	var boardSize = max(maxX - minX, maxY - minY);
	return (boardPixelSize / boardSize);
}

function scale(n, base, by) {
	return (n - base) * by;
}

function getMinimum(blobs, attr) {
	var result = "undef";
	for (var i = 0; i < blobs.length; i++) {
		var coord = blobs[i]["p" + attr] - blobs[i].size;
		if (result == "undef" || coord < result) result = coord;
	}
	return parseFloat(result, 10);
}

function getMaximum(blobs, attr) {
	var result = "undef";
	for (var i = 0; i < blobs.length; i++) {
		var coord = (1 * blobs[i]["p" + attr]) + (1 * blobs[i].size);
		if (result == "undef" || coord > result) result = coord;
	}
	return parseFloat(result, 10);
}

function min(a, b) {
	return (a < b) ? a : b;
}

function max(a, b) {
	return (a > b) ? a : b;
}

function removeAllChildNodes(obj) {
	while (obj.hasChildNodes()) obj.removeChild(obj.firstChild);
}

function angleBetween(a, b) {
	return Math.atan2((b.py - a.py), (b.px - a.px));
}

function moveBlobs(blobs) {
	for (var i = 0; i < blobs.length; i++) {
		blobs[i].px = (blobs[i].px * 1) + (blobs[i].vx * 1);
		blobs[i].py = (blobs[i].py * 1) + (blobs[i].vy * 1);
	}
}

function checkForCollisions(blobs) {
	for (var i = 0; i < blobs.length; i++)
	for (var j = i + 1; j < blobs.length; j++)
		checkCollision(blobs[i], blobs[j]);
}

function checkCollision(a, b) {
	if (!blobsCollide(a, b)) return;
	if (a.size >= b.size) destroyBlob(b);
	if (b.size >= a.size) destroyBlob(a);
}

function destroyBlob(blob) {
	blob.status = 'Dead';
}

function blobsCollide(a, b) {
	var collisionDistance = (a.size * 1) + (b.size * 1);
	return (distanceFrom(a, b) <= collisionDistance);
}

function distanceFrom(a, b) {
	var dx = a.px - b.px;
	var dy = a.py - b.py;
	return (Math.sqrt(dx*dx + dy*dy));
}

function getBlobsFromTable() {
	var blobs = new Array();
	while (blobTableHasRows())
		blobs[blobs.length] = getBlobFromTable();
	return blobs;
}

function blobTableHasRows() {
	var table = blobTable();
	return (table.getElementsByTagName('tr').length > 0);
}

function isDead(blob) {
	return blob.status != 'Alive';
}

function getBlobFromTable() {
	var table = blobTable();
	var rows = table.getElementsByTagName('tr');
	var row = table.removeChild(rows[0]);
	var values = row.getElementsByTagName('td');
	var blob = new Object();
	
	for (var i = 0; i < cells.length; i++) {
		blob[cells[i]] = values[i].innerHTML;
	}
	
	return blob;
}

function blobTable() {
	return document.getElementById('data');
}

function blobSvgGroup() {
	var iframe = document.getElementById('svg')
	return iframe.contentDocument.getElementById('blobs');
}

function fillTableWithBlobs(blobs) {
	var table = blobTable();
	for (var i = 0; i < blobs.length; i++) {
		var row = document.createElement('tr');
		for (var j = 0; j < cells.length; j++) {
			var cell = document.createElement('td');
			if (isFixed(cells[j]))
				cell.innerHTML = (1 * blobs[i][cells[j]]).toFixed(3);
			else
				cell.innerHTML = blobs[i][cells[j]];
			row.appendChild(cell);
		}
		
		if (isDead(blobs[i])) row.className = 'dead';
		table.appendChild(row);
	}
}

function isFixed(col) {
	return (col == 'px' || col == 'py' || col == 'vx' || col == 'vy' ||
		col == 'strength' || col == 'size');
}