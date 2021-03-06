function pageLoad() {
    /* Add this to the end of pageLoad function if your function already has code in it! */
    document.getElementById("EventImageUploadForm").addEventListener("submit", uploadImageEvent);
}

function uploadImageEvent(event) {
    event.preventDefault();
    const imageUploadForm = document.getElementById('EventImageUploadForm');
    if (document.getElementById('file').value !== '') {
        imageUploadForm.style.display = 'none';
        document.getElementById('uploading').style.display = 'block';
        let fileData = new FormData(imageUploadForm);
        fetch('/image/upload', {method: 'post', body: fileData},
        ).then(response => response.json()
        ).then(data => {
                if (data.hasOwnProperty('error')) {
                    alert(data.error);
                } else {
                    document.getElementById('file').value = '';
                }
                imageUploadForm.style.display = 'block';
                document.getElementById('uploading').style.display = 'none';
            }
        );

        //placing data into events table

        let Title = fileData.get("Title");
        let Description = fileData.get("Description");
        let file = fileData.get("file");
        let ImageReference = file.name;


        let id = JSON.parse(window.sessionStorage.user);
        id = JSON.stringify(id.userID);
        let formData = new FormData();
        formData.append("UserID", id);
        formData.append("Title", Title);
        formData.append("Description", Description);
        formData.append("ImageReference", ImageReference);
        fetch('/Event/create', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    window.location.href = 'Events.html';
                }
            }
        );
        //-----------------------------------------------//
    } else {
        alert('No file specified');
    }
}


