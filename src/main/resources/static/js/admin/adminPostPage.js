function setDateFormat(inputDate) {
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

function displayPosts(setPostResponseDtoList) {
    for (const postResponseDto of setPostResponseDtoList) {
        console.log(postResponseDto);
        const id = postResponseDto.id;
        const title = postResponseDto.title;
        const content = postResponseDto.content;
        const username = postResponseDto.username;
        const nickname = postResponseDto.nickname;
        const opinionA = postResponseDto.opinionA;
        const opinionB = postResponseDto.opinionB;
        const opinionACnt = postResponseDto.opinionACnt;
        const opinionBCnt = postResponseDto.opinionBCnt;
        const createdAt = postResponseDto.createdAt;
        const modifiedAt = postResponseDto.modifiedAt;
        const commentCnt = postResponseDto.commentCnt;

        const formattedCreatedAt = setDateFormat(createdAt);

        const html_temp = `
                              <hr style="border-top: 1px solid #6c757d;">
                                 <div class="user">
                                    <form class="row gy-2 gx-3 align-items-center">
                      
                                            <label class="visually-hidden" for="Username">게시글 ID</label>
                                            <div class="input-group">
                                                <div class="input-group-text">id</div>
                                                <input type="text" class="form-control" placeholder="${id}번" disabled='disabled'>
                                            </div>
                           
                           
                                            <label class="visually-hidden" for="Username">게시글 제목</label>
                                            <div class="input-group">
                                                <div class="input-group-text">게시글 제목</div>
                                                <input type="text" class="form-control" placeholder="${title}" disabled='disabled'>
                                            </div>
                              
                          
                                            <label class="visually-hidden" for="nickName">게시글 내용</label>
                                            <div class="input-group">
                                                <div class="input-group-text">게시글 내용</div>
                                                <input type="text" class="form-control" placeholder="${content}" disabled='disabled'>
                                            </div>
                             
                             
                                            <label class="visually-hidden" for="email">작성자 ID(닉네임)</label>
                                            <div class="input-group">
                                                <div class="input-group-text">작성자 ID(닉네임)</div>
                                                <input type="text" class="form-control" placeholder="${username} (${nickname})" disabled='disabled'>
                                            </div>
                   
                                            <label class="visually-hidden" for="voteCnt">1번째 투표 옵션 내용</label>
                                            <div class="input-group">
                                                <div class="input-group-text">1번째 투표 옵션 내용</div>
                                                <input type="text" class="form-control" placeholder="${opinionA}" disabled='disabled'>
                                            </div>
                             
                                            <label class="visually-hidden" for="voteCnt">1번째 투표 계수</label>
                                            <div class="input-group">
                                                <div class="input-group-text">1번째 투표 계수</div>
                                                <input type="text" class="form-control" placeholder="${opinionACnt}개" disabled='disabled'>
                                            </div>
                                
                                            <label class="visually-hidden" for="postCnt">2번째 투표 옵션 내용</label>
                                            <div class="input-group">
                                                <div class="input-group-text">2번째 투표 옵션 내용</div>
                                                <input type="text" class="form-control" placeholder="${opinionB}" disabled='disabled'>
                                            </div>
                                  
                                            <label class="visually-hidden" for="voteCnt">2번째 투표 계수</label>
                                            <div class="input-group">
                                                <div class="input-group-text">2번째 투표 계수</div>
                                                <input type="text" class="form-control" placeholder="${opinionBCnt}개" disabled='disabled'>
                                            </div>
                               
                                            <label class="visually-hidden" for="commentCnt">게시글 작성일자</label>
                                            <div class="input-group">
                                                <div class="input-group-text">게시글 작성일자</div>
                                                <input type="text" class="form-control" placeholder="${formattedCreatedAt}" disabled='disabled'>
                                            </div>
                                           
                                            
                                             <label class="visually-hidden" for="commentCnt">게시글에 따른 댓글 갯수</label>
                                            <div class="input-group">
                                                <div class="input-group-text">게시글에 따른 댓글 갯수</div>
                                                <input type="text" class="form-control" placeholder="${commentCnt}" disabled='disabled'>
                                            </div>
                                    </form>
                                        
                                              <div class="col-auto" id="btns">
                                                <button onclick="deleteUserPost(${id})" class="btn btn-primary">게시글 삭제</button>
                                              </div>
                                </div>`;

        $('#posts').append(html_temp);

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

    fetch("/api/admin/post", options)
        .then(response => response.json())
        .then(data => displayPosts(data))
        .catch(error => console.error("Error fetching data:", error));

}

window.onload = function () {
    //checkAdmin();
    getUsers();
}

function deleteUserPost(id) {
    const token = Cookies.get('Authorization');
    const isConfirmed = confirm("정말로 삭제하시겠습니까? \n삭제할 경우 게시글과 관련된 데이터 모두 삭제 됩니다.");
    $.ajax({
        url: `/api/admin/posts/${id}`,
        type: "DELETE",
        dataType: "json",
        headers: {              // Http header
            "Content-Type": "application/json",
            "Authorization": token
        },
        success: function (data) {
            Swal.fire({
                    icon: 'success',
                    title: '게시글 삭제 성공',
                    text: '게시글 삭제가 성공적으로 완료되었습니다.'
                }
            ).then(function () {
                window.location.reload();
            })
        },
        error: function (error) {
            Toast.fire({
                icon: 'error',
                title: '게시글 삭제  실패',
                text: '게시글 삭제를 실패하였습니다.',
            }).then(function () {
                window.location.reload();
            });
        }
    });
}

function cancel(){
    window.location.href = "/"
}