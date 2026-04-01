function checkWebsite(){

    const url = document.getElementById("urlInput").value;

    fetch("/check?url=" + url)
    .then(response => response.text())
    .then(data => {
        document.getElementById("result").innerText = data;
    });

}