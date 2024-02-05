const myAction = () => {
    const btn = document.getElementById("test");
    btn.addEventListener("click", () => {
        alert("Helloooo");
    });
}

const loadGetMsg = () => {
    let movieVar = document.getElementById("movie").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        document.getElementById("getrespmsg").innerHTML = this.responseText;
    }
    xhttp.open("GET", "/movie?t=" + encodeURIComponent(movieVar));
    xhttp.send();
}

const handleKeyPress = (event) => {
    if (event.keyCode === 13) {
        event.preventDefault();
        loadGetMsg();
    }
};



