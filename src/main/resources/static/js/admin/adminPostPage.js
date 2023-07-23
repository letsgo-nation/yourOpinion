function displayPosts(setPostResponseDtoList){
    for (const postResponseDto of setPostResponseDtoList) {
        console.log(postResponseDto);
        const id = postResponseDto.id;
        const title = postResponseDto.title;
        const content = postResponseDto.content;
        const username = postResponseDto.username;
        const nickname = postResponseDto.nickname;
        const opinionA = postResponseDto.opinionA;
        const opinionB = postResponseDto.opinionB;
        const createdAt = postResponseDto.createdAt;
        const modifiedAt = postResponseDto.modifiedAt;
        const commentCnt = postResponseDto.commentCnt;

        const html_temp = `
                              <hr style="border-top: 1px solid #6c757d;">
                                 <div class="user">
                                    <form class="row gy-2 gx-3 align-items-center">
                      
                                            <label class="visually-hidden" for="Username">id</label>
                                            <div class="input-group">
                                                <div class="input-group-text">id</div>
                                                <input type="text" class="form-control" placeholder="${id}" disabled='disabled'>
                                            </div>
                           
                           
                                            <label class="visually-hidden" for="Username">title</label>
                                            <div class="input-group">
                                                <div class="input-group-text">title</div>
                                                <input type="text" class="form-control" placeholder="${title}" disabled='disabled'>
                                            </div>
                              
                          
                                            <label class="visually-hidden" for="nickName">content</label>
                                            <div class="input-group">
                                                <div class="input-group-text">content</div>
                                                <input type="text" class="form-control" placeholder="${content}" disabled='disabled'>
                                            </div>
                             
                             
                                            <label class="visually-hidden" for="email">username</label>
                                            <div class="input-group">
                                                <div class="input-group-text">username</div>
                                                <input type="text" class="form-control" placeholder="${username}" disabled='disabled'>
                                            </div>
                                
                                
                            
                                            <label class="visually-hidden" for="Role">nickname</label>
                                            <div class="input-group">
                                                <div class="input-group-text">nickname</div>
                                                <input type="text" class="form-control" placeholder="${nickname}" disabled='disabled'>
                                            </div>
                          
                   
                                            <label class="visually-hidden" for="voteCnt">opinionA</label>
                                            <div class="input-group">
                                                <div class="input-group-text">opinionA</div>
                                                <input type="text" class="form-control" placeholder="${opinionA}" disabled='disabled'>
                                            </div>
                             
                                
                                            <label class="visually-hidden" for="postCnt">opinionB</label>
                                            <div class="input-group">
                                                <div class="input-group-text">opinionB</div>
                                                <input type="text" class="form-control" placeholder="${opinionB}" disabled='disabled'>
                                            </div>
                                  
                               
                                            <label class="visually-hidden" for="commentCnt">createdAt</label>
                                            <div class="input-group">
                                                <div class="input-group-text">createdAt</div>
                                                <input type="text" class="form-control" placeholder="${createdAt}" disabled='disabled'>
                                            </div>
                                            
                                             <label class="visually-hidden" for="commentCnt">modifiedAt</label>
                                            <div class="input-group">
                                                <div class="input-group-text">modifiedAt</div>
                                                <input type="text" class="form-control" placeholder="${modifiedAt}" disabled='disabled'>
                                            </div>
                                            
                                             <label class="visually-hidden" for="commentCnt">commentCnt</label>
                                            <div class="input-group">
                                                <div class="input-group-text">commentCnt</div>
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
    fetch("/api/admin/post")
        .then(response => response.json())
        .then(data => displayPosts(data))
        .catch(error => console.error("Error fetching data:" , error));

}

window.onload = function() {
    //checkAdmin();
    getUsers();
}
function deleteUserPost(id){
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