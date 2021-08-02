// see https://shadow-cljs.github.io/docs/UsersGuide.html#_javascript_dialects
// these files are compiled by babel and then included in the cljs
import * as React from "react";

class Author extends React.Component {
	render() {
		return (<div>"react interop!" -- look! at me, i'm cool</div>);
	}
}

export default Author;
