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
                      
                                            <label class="visually-hidden" for="autoSizingInput">nickname</label>
                                            <input type="text" class="form-control" placeholder="${nickname}" disabled='disabled'>
                           
                           
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
                                
                                          <div class="col-auto">
                                            <button type="submit" class="btn btn-primary">권한 수정</button>
                                            <button type="submit" class="btn btn-primary">유저 삭제</button>
                                          </div>
                                    </form>
                                </div>`;

       $('#users').append(html_temp);

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
