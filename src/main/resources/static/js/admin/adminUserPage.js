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

       const html_temp = `<div class="user">
                                    <form class="row gy-2 gx-3 align-items-center">
                      
                                            <label class="visually-hidden" for="Username">id</label>
                                            <div class="input-group">
                                                <div class="input-group-text">id</div>
                                                <input type="text" class="form-control" placeholder="${id}" disabled='disabled'>
                                            </div>
                           
                           
                                            <label class="visually-hidden" for="Username">username</label>
                                            <div class="input-group">
                                                <div class="input-group-text">username</div>
                                                <input type="text" class="form-control" placeholder="${username}" disabled='disabled'>
                                            </div>
                              
                          
                                            <label class="visually-hidden" for="nickName">nickname</label>
                                            <div class="input-group">
                                                <div class="input-group-text">nickname</div>
                                                <input type="text" class="form-control" placeholder="${nickname}" disabled='disabled'>
                                            </div>
                             
                             
                                            <label class="visually-hidden" for="email">email</label>
                                            <div class="input-group">
                                                <div class="input-group-text">email</div>
                                                <input type="text" class="form-control" placeholder="${email}" disabled='disabled'>
                                            </div>
                                
                                
                            
                                            <label class="visually-hidden" for="Role">role</label>
                                            <div class="input-group">
                                                <div class="input-group-text">role</div>
                                                <input type="text" class="form-control" placeholder="${role}" disabled='disabled'>
                                            </div>
                          
                   
                                            <label class="visually-hidden" for="voteCnt">voteCnt</label>
                                            <div class="input-group">
                                                <div class="input-group-text">voteCnt</div>
                                                <input type="text" class="form-control" placeholder="${voteCnt}" disabled='disabled'>
                                            </div>
                             
                                
                                            <label class="visually-hidden" for="postCnt">postCnt</label>
                                            <div class="input-group">
                                                <div class="input-group-text">postCnt</div>
                                                <input type="text" class="form-control" placeholder="${postCnt}" disabled='disabled'>
                                            </div>
                                  
                               
                                            <label class="visually-hidden" for="commentCnt">commentCnt</label>
                                            <div class="input-group">
                                                <div class="input-group-text">commentCnt</div>
                                                <input type="text" class="form-control" placeholder="${commentCnt}" disabled='disabled'>
                                            </div>
                                            <div class="col-auto" id="authUdate" > 
                                                <input type="text" class="form-control" id="auth" placeholder="권한 수정">
                                            </div>
                                    </form>
                                          <div class="col-auto">
                                            <button onclick="userUpdate(${id})" class="btn btn-primary">권한 수정</button>
                                            <button onclick="userDelete(${id})" class="btn btn-primary">유저 삭제</button>
                                          </div>
                                </div>`;

       $('#users').append(html_temp);

    }
}

const Toast = Swal.mixin({
    toast: true,
    position: 'center-center',
    showConfirmButton: false,
    timer: 2000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
})

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

function userDelete(id) {
    const token = Cookies.get('Authorization');

    $.ajax({
        url: `/api/admin/users/${id}`,
        type: "DELETE",
        dataType: "json",
        headers: {              // Http header
            "Content-Type": "application/json",
            "Authorization": token
        },
        success: function (data) {
            Swal.fire({
                    icon: 'success',
                    title: '삭제 성공',
                    text: '게시글 삭제가 성공적으로 완료되었습니다.'
                }
            ).then(function () {
                window.location.reload();
            })
        },
        error: function (error) {
            Toast.fire({
                icon: 'error',
                title: '삭제 실패',
                text: '작성자가 맞는지 확인 부탁드립니다.',
            }).then(function () {
                window.location.reload();
            });
        }
    });
}

function userUpdate(){
    $('#authUdate').show()
}

