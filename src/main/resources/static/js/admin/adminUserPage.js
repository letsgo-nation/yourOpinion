function displayPosts(adminUserResponseDtoList){
    for (const adminUserResponseDto of adminUserResponseDtoList) {
        console.log(adminUserResponseDto);
        const id = adminUserResponseDto.id;
        const username = adminUserResponseDto.username;
        const nickname = adminUserResponseDto.nickname;
        const email = adminUserResponseDto.email;
        const role = adminUserResponseDto.role;
        const voteCnt = adminUserResponseDto.voteCnt;
        const postCnt = adminUserResponseDto.postCnt;
        const commentCnt = adminUserResponseDto.commentCnt;
    }
}

function getUsers() {
    fetch("/api/admin/user")
        .then(response => response.json())
        .then(data => displayPosts(data))
        .catch(error => console.error("Error fetching data:" , error));
}

window.onload = function() {
    //checkAdmin();
    getUsers();
}

function checkAdmin() {

}