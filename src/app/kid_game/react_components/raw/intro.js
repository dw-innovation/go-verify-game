// see https://shadow-cljs.github.io/docs/UsersGuide.html#_javascript_dialects
// these files are compiled by babel and then included in the cljs
import * as React from "react";

class Author extends React.Component {
	render() {
		return (<div>Welcome, what is your name?</div>);
	}
}

export default Author;
