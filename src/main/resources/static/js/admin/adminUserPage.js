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

       const html_temp = `
                              <hr style="border-top: 1px solid #6c757d;">
                                 <div class="user">
                                    <form class="row gy-2 gx-3 align-items-center">
                      
                                            <label class="visually-hidden" for="Username">회원 식별값</label>
                                            <div class="input-group">
                                                <div class="input-group-text">회원 식별값</div>
                                                <input type="text" class="form-control" placeholder="${id}번" disabled='disabled'>
                                            </div>
                           
                           
                                            <label class="visually-hidden" for="Username">회원 ID(닉네임)</label>
                                            <div class="input-group">
                                                <div class="input-group-text">회원 ID(닉네임)</div>
                                                <input type="text" class="form-control" placeholder="${username} (${nickname})" disabled='disabled'>
                                            </div>
                             
                             
                                            <label class="visually-hidden" for="email">이메일</label>
                                            <div class="input-group">
                                                <div class="input-group-text">이메일</div>
                                                <input type="text" class="form-control" placeholder="${email}" disabled='disabled'>
                                            </div>
                                
                                
                            
                                            <label class="visually-hidden" for="Role">회원 권한</label>
                                            <div class="input-group">
                                                <div class="input-group-text">회원 권한</div>
                                                <input type="text" class="form-control" placeholder="${role}" disabled='disabled'>
                                            </div>
                          
                   
                                            <label class="visually-hidden" for="voteCnt">투표에 참여한 갯수 합계</label>
                                            <div class="input-group">
                                                <div class="input-group-text">투표에 참여한 갯수 합계</div>
                                                <input type="text" class="form-control" placeholder="${voteCnt}" disabled='disabled'>
                                            </div>
                             
                                
                                            <label class="visually-hidden" for="postCnt">작성한 게시글 갯수 합계</label>
                                            <div class="input-group">
                                                <div class="input-group-text">작성한 게시글 갯수 합계</div>
                                                <input type="text" class="form-control" placeholder="${postCnt}" disabled='disabled'>
                                            </div>
                                  
                               
                                            <label class="visually-hidden" for="commentCnt">작성한 댓글 갯수 합계</label>
                                            <div class="input-group">
                                                <div class="input-group-text">작성한 댓글 갯수 합계</div>
                                                <input type="text" class="form-control" placeholder="${commentCnt}" disabled='disabled'>
                                            </div>
                                    </form>
                                            <div class="col-auto" id="${id}" style="display: none" > 
                                                <input type="text" class="form-control" id="auth-${id}" placeholder="수정에 필요한 키값을 입력하세요">
                                                <button onclick="userUpdate(${id}, '${role}')" class="btn btn-primary">수정하기</button>
                                                <button onclick="window.location.reload()" class="btn btn-primary">수정 취소</button>
                                            </div>
                                            
                                              <div class="col-auto" id="btns">
                                                <button onclick="userUpdateBtn(${id})" class="btn btn-primary">권한 수정</button>
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
    const token = Cookies.get('Authorization');

    const headers = new Headers();
    headers.append("Authorization", token); // Add your custom header here

    const options = {
        method: "GET",
        headers: headers,
    }

    fetch("/api/admin/user", options)
        .then(response => response.json())
        .then(data => displayPosts(data))
        .catch(error =>  {
            console.error("Error fetching data:", error);
            window.history.back();
        })
}

window.onload = function() {
    getUsers();
}

function userDelete(id) {
    const token = Cookies.get('Authorization');
    const isConfirmed = confirm("정말로 삭제하시겠습니까? \n삭제할 경우 회원과 관련된 데이터 모두 삭제 됩니다.");
    if (isConfirmed) {
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
                        title: '회원 삭제 성공',
                        text: '회원 삭제가 성공적으로 완료되었습니다.'
                    }
                ).then(function () {
                    window.location.reload();
                })
            },
            error: function (error) {
                Toast.fire({
                    icon: 'error',
                    title: '회원 삭제 실패',
                    text: '회원 삭제를 실패하였습니다.',
                }).then(function () {
                    window.location.reload();
                });
            }
        });
    }
}

function userUpdateBtn(id){
    $(`#${id}`).show();
    $(`#users`).find($("div[id^='btns']")).empty();
}

function userUpdate(id,role){
    const token = Cookies.get('Authorization');

    let bool;

    //true면 user
    //false면 dmin

    if(role == 'USER'){
        bool = true;
    }else {
        bool = false;
    }

    $.ajax({
        url: `/api/admin/users/${id}`,
        type: "PUT",
        dataType: "json",
        headers: {              // Http header
            "Content-Type": "application/json",
            "Authorization": token
        },
        data: JSON.stringify({admin: bool,adminToken: $(`#auth-${id}`).val() }),
        success: function (data) {
            Swal.fire({
                    icon: 'success',
                    title: '권한 수정 성공',
                    text: '권한 수정이 성공적으로 완료되었습니다.'
                }
            ).then(function () {
                window.location.reload();
            })
        },
        error: function (error) {
            Toast.fire({
                icon: 'error',
                title: '권한 수정 실패',
                text: '권한이 있는지 확인 부탁드립니다.',
            }).then(function () {
                window.location.reload();
            });
        }
    });

}

