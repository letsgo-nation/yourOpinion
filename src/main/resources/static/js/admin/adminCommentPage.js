function setDateFormat(inputDate){
    const date = new Date(inputDate);

    return date.toLocaleString("ko-KR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    });
}

function displayComments(commentList) {
    for (const comment of commentList) {
        console.log(comment);
        const id = comment.id;
        const content = comment.content;
        const createdAt = comment.createdAt;
        const postId = comment.postId;
        const postTitle = comment.postTitle;
        const userId = comment.userId;
        const username = comment.username;
        const nickname = comment.nickname;

        const formattedCreatedAt = setDateFormat(createdAt);

        const html_temp = `
                              <hr style="border-top: 1px solid #6c757d;">
                                 <div class="user">
                                    <form class="row gy-2 gx-3 align-items-center">
                      
                                            <label class="visually-hidden" for="Username">댓글 ID</label>
                                            <div class="input-group">
                                                <div class="input-group-text">댓글 ID</div>
                                                <input type="text" class="form-control" placeholder="${id}번" disabled='disabled'>
                                            </div>
                                            
                                            
                                            <label class="visually-hidden" for="Username">댓글 내용</label>
                                            <div class="input-group">
                                                <div class="input-group-text">댓글 내용</div>
                                                <input type="text" class="form-control" placeholder="${content}" disabled='disabled'>
                                            </div>
                                            
                                            <label class="visually-hidden" for="postCnt">댓글 작성자 ID(닉네임)</label>
                                            <div class="input-group">
                                                <div class="input-group-text">ID(nickname)</div>
                                                <input type="text" class="form-control" placeholder="${username} (${nickname})" 
                                                disabled='disabled'>
                                            </div>
                          
                                            <label class="visually-hidden" for="nickName">댓글 작성일자</label>
                                            <div class="input-group">
                                                <div class="input-group-text">댓글 작성 일자</div>
                                                <input type="text" class="form-control" placeholder="${formattedCreatedAt}" disabled='disabled'>
                                            </div>
                             
                             
                                            <label class="visually-hidden" for="email">게시글 ID</label>
                                            <div class="input-group">
                                                <div class="input-group-text">게시글 ID</div>
                                                <input type="text" class="form-control" placeholder="${postId}" disabled='disabled'>
                                            </div>
                                
                            
                                            <label class="visually-hidden" for="Role">게시글 제목</label>
                                            <div class="input-group">
                                                <div class="input-group-text">게시글 제목</div>
                                                <input type="text" class="form-control" placeholder="${postTitle}" disabled='disabled'>
                                            </div>
                          
                                    </form>
                                            
                                              <div class="col-auto" id="btns">
                                                <button onclick="commentDelete(${id})" class="btn btn-primary">댓글 삭제</button>
                                              </div>
                                </div>`;

        $('#comments').append(html_temp);
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

function getComments() {
    const token = Cookies.get('Authorization');

    const headers = new Headers();
    headers.append("Authorization", token); // Add your custom header here

    const options = {
        method: "GET",
        headers: headers,
    }

    fetch("/api/admin/comment", options)
        .then(response => response.json())
        .then(data => displayComments(data))
        .catch(error => {
            console.error("Error fetching data:", error);
            //window.history.back();
        })
}

window.onload = function () {
    getComments();
}

function commentDelete(id) {
    const token = Cookies.get('Authorization');
    const isConfirmed = confirm("정말로 삭제하시겠습니까?");
    if (isConfirmed) {
        $.ajax({
            url: `/api/admin/comments/${id}`,
            type: "DELETE",
            dataType: "json",
            headers: {              // Http header
                "Content-Type": "application/json",
                "Authorization": token
            },
            success: function (data) {
                Swal.fire({
                        icon: 'success',
                        title: '댓글 삭제 성공',
                        text: '댓글 삭제가 성공적으로 완료되었습니다.'
                    }
                ).then(function () {
                    window.location.reload();
                })
            },
            error: function (error) {
                Toast.fire({
                    icon: 'error',
                    title: '댓글 삭제 실패',
                    text: '댓글 삭제를 실패하였습니다.',
                }).then(function () {
                    window.location.reload();
                });
            }
        });
    }
}


