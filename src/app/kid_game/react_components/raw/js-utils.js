
// copied from https://gist.github.com/mlocati/7210513
//  i liked the color scale
// but, it's wayyyy too slow
export function perc2color(perc) {
	var r, g, b = 0;
	if(perc < 50) {
		r = 255;
		g = Math.round(5.1 * perc);
	}
	else {
		g = 255;
		r = Math.round(510 - 5.10 * perc);
	}
	var h = r * 0x10000 + g * 0x100 + b * 0x1;
	return '#' + ('000000' + h.toString(16)).slice(-6);
}

// hsl(127, 47%, 45%)
// this one is faster
export function percentageToColor(percentage) {
  const hue = (percentage / 100) * 127;
  return `hsl(${hue}, 47%, 45%)`;
}
