function pageLoad() {
    /* Add this to the end of pageLoad function if your function already has code in it! */
    document.getElementById("PostImageUploadForm").addEventListener("submit", uploadImagePost);
}

function uploadImagePost(event){
    event.preventDefault();
    const imageUploadForm = document.getElementById('PostImageUploadForm');
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

        //placing data into Posts table


        let Caption = fileData.get("Caption");
        let file = fileData.get("file");
        let ImageReference = file.name;


        let id = JSON.parse(window.sessionStorage.user);
        id = JSON.stringify(id.userID);
        let formData = new FormData();
        formData.append("UserID", id);
        formData.append("Type", "image");
        formData.append("FileReference", ImageReference);
        formData.append("DateAdded", "18/01/2020");
        formData.append("Caption", Caption);

        alert("here");

        fetch('/Post/create', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    window.location.href = 'Profile.html';
                }
            }
        );
        //-----------------------------------------------//
    } else {
        alert('No file specified');
    }
}